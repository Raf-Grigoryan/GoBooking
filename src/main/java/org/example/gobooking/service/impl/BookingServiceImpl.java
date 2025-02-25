package org.example.gobooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.gobooking.customException.InsufficientFundsException;
import org.example.gobooking.dto.booking.*;
import org.example.gobooking.dto.subscription.BookingStatistics;
import org.example.gobooking.entity.booking.Booking;
import org.example.gobooking.entity.booking.PaymentMethod;
import org.example.gobooking.entity.booking.Type;
import org.example.gobooking.entity.user.Card;
import org.example.gobooking.entity.user.User;
import org.example.gobooking.entity.work.Service;
import org.example.gobooking.entity.work.WorkGraphic;
import org.example.gobooking.mapper.BookingMapper;
import org.example.gobooking.repository.BookingRepository;
import org.example.gobooking.repository.ServiceRepository;
import org.example.gobooking.service.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;


@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final WorkGraphicService workGraphicService;

    private final WorkService workService;

    private final BookingRepository bookingRepository;

    private final ServiceRepository serviceRepository;

    private final BookingMapper bookingMapper;

    private final CardService cardService;

    private final BookingBalanceService bookingBalanceService;

    @Override
    public SelectTimeResponse getSelectTimeByWorkerIdAndServiceId(int workerId, int serviceId, Date bookingDate) {
        if (bookingDate == null) {
            bookingDate = java.sql.Date.valueOf(LocalDate.now());
        }

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(bookingDate);
        String weekDay = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH).toUpperCase();

        WorkGraphic workGraphic = workGraphicService.getWorkGraphicByWorkerIdAndDayWeek(workerId, weekDay);
        List<LocalTime> availableSlots = new ArrayList<>();

        if (workGraphic != null && workGraphic.isActive()) {
            List<Booking> bookings = bookingRepository.findBookingByBookingDateAndServiceWorkerId(bookingDate, workerId);
            LocalTime startOfWork = workGraphic.getStartWorkDate();
            LocalTime endOfWork = workGraphic.getEndedWorkDate();

            Service service = workService.getServiceByIdForBooking(serviceId);
            int serviceDurationMinutes = service.getDuration();

            LocalTime currentSlot = startOfWork;

            while (!currentSlot.isAfter(endOfWork.minusMinutes(serviceDurationMinutes))) {
                boolean isSlotAvailable = true;

                for (Booking booking : bookings) {
                    LocalTime bookingStart = booking.getStartedTime();
                    LocalTime bookingEnd = booking.getEndedTime();

                    if (bookingStart.equals(currentSlot)) {
                        currentSlot = bookingEnd;
                        isSlotAvailable = false;
                        break;
                    }
                    if (removeTime(new Date()).equals(removeTime(booking.getBookingDate())) && LocalTime.now().isAfter(currentSlot)) {
                        isSlotAvailable = false;
                        currentSlot = currentSlot.plusMinutes(30);
                    }
                }
                if (isSlotAvailable) {
                    availableSlots.add(currentSlot);
                    currentSlot = currentSlot.plusMinutes(30);
                }
            }
        }

        SelectTimeResponse response = new SelectTimeResponse();
        response.setServiceResponse(workService.getById(serviceId));
        response.setAvailableSlots(availableSlots);
        response.setWorkerId(workerId);
        response.setBookingDate(bookingDate);

        return response;
    }


    @Override
    public void save(SaveBookingRequest saveBookingRequest, User user, Date bookingDate, String cardNumber) {
        Service service = serviceRepository.findById(saveBookingRequest.getServiceId()).orElse(null);
        if (service != null && !service.getWorker().equals(user)) {
            if (cardNumber != null && !cardNumber.isEmpty()) {
                Card card = cardService.getCardByCardNumber(cardNumber);
                if (card.getBalance().compareTo(BigDecimal.valueOf(service.getPrice())) > 0) {
                    Date date = Objects.requireNonNullElseGet(bookingDate, Date::new);
                    bookingSave(service, user, date, saveBookingRequest);
                    card.setBalance(card.getBalance().subtract(BigDecimal.valueOf(service.getPrice())));
                    cardService.editCard(card);
                    bookingBalanceService.addFunds(service.getPrice());
                } else {
                    throw new InsufficientFundsException("There may be insufficient funds on your card.");
                }
            } else {
                Date date = Objects.requireNonNullElseGet(bookingDate, Date::new);
                bookingSave(service, user, date, saveBookingRequest);
            }
        }

    }

    private synchronized void bookingSave(Service service, User user, Date bookingDate, SaveBookingRequest saveBookingRequest) {
        if ((removeTime(new Date()).equals(removeTime(bookingDate)) && saveBookingRequest.getStartTime().isAfter(LocalTime.now()))
        || (removeTime(new Date()).before(removeTime(bookingDate)))) {
            Date date = Objects.requireNonNullElseGet(bookingDate, Date::new);
            bookingRepository.save(Booking.builder()
                    .service(service)
                    .client(user)
                    .startedTime(saveBookingRequest.getStartTime())
                    .endedTime(saveBookingRequest.getStartTime().plusMinutes(service.getDuration()))
                    .paymentMethod(saveBookingRequest.getPaymentMethod())
                    .type(Type.APPROVED)
                    .bookingDate(date)
                    .build());
        }
    }

    @Override
    public Page<WorkerBookingResponse> clientFinishedBookings(int clientId, Type type, PageRequest pageRequest) {
        Page<Booking> bookings = bookingRepository.getBookingByService_Worker_IdAndType(clientId, type, pageRequest);
        return bookings.map(bookingMapper::toBookingResponse);
    }

    @Override
    public List<WorkerBookingResponse> clientFinishedBookings(int clientId, Type type) {
        return bookingMapper.workerBookingResponses(bookingRepository.getBookingsByClient_IdAndType(clientId, type));
    }


    @Override
    public Page<PendingBookingResponse> getUnfinishedServices(int workerId, PageRequest pageRequest) {
        Page<Booking> bookings = bookingRepository.getBookingByService_Worker_IdAndType(workerId, Type.APPROVED, pageRequest);
        return bookings.map(bookingMapper::toPendingBookingResponse);
    }

    @Override
    public BookingAnalyticsWorker getBookingAnalyticsWorker(int workerId) {
        return BookingAnalyticsWorker.builder()
                .clientCount(bookingRepository.countDistinctClientsByWorker(workerId))
                .totalEarnings(bookingRepository.sumTotalEarningsByWorker(workerId))
                .bookingCount(bookingRepository.countBookingsByWorker(workerId))
                .potentialEarnings(bookingRepository.potentialEarnings(workerId))
                .build();
    }

    @Override
    public Page<PendingBookingResponse> getFinishedBookings(int workerId, PageRequest pageRequest) {
        Page<Booking> bookings = bookingRepository.getBookingByService_Worker_IdAndType(workerId, Type.APPROVED, pageRequest);
        return bookings.map(bookingMapper::toPendingBookingResponse);
    }

    @Override
    public synchronized void reject(int bookingId) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            booking.setType(Type.REJECTED);
            if(booking.getPaymentMethod().equals(PaymentMethod.CARD)){
            Card card = cardService.getCardByUserIdAndMainIs(booking.getClient().getId(), true);
            card.setBalance(card.getBalance().add(BigDecimal.valueOf(booking.getService().getPrice())));
            bookingBalanceService.subtractFunds(booking.getService().getPrice());
            cardService.editCard(card);
            }
            bookingRepository.save(booking);


        }
    }

    @Override
    public synchronized void finished(int bookingId) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            Date date = removeTime(new Date());
            Date bookingDate = removeTime(booking.getBookingDate());
            if (bookingDate.equals(date) && booking.getEndedTime().isBefore(LocalTime.now())) {
                finishBooking(booking);
            } else if (bookingDate.before(date)) {
                finishBooking(booking);
            }

        }
    }

    @Override
    public double getSumTotalEarningsByWorkerWhereTypeApproved(int workerId) {
        return bookingRepository.sumTotalEarningsByWorkerWhereTypeApproved(workerId);
    }

    @Override
    public double getSumTotalEarningsByWorkerWhereTypeFinished(int workerId) {
        return bookingRepository.sumTotalEarningsByWorkerWhereTypeFinished(workerId);
    }

    private void finishBooking(Booking booking) {
        if(booking.getPaymentMethod().equals(PaymentMethod.CARD)){
            Card card = cardService.getCardByUserIdAndMainIs(booking.getService().getWorker().getId(), true);
            card.setBalance(card.getBalance().add(BigDecimal.valueOf(booking.getService().getPrice())));
            bookingBalanceService.subtractFunds(booking.getService().getPrice());
            cardService.editCard(card);
        }
        booking.setType(Type.FINISHED);
        bookingRepository.save(booking);
    }

    private Date removeTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    @Override
    public int getBookingCountByCompanyId(int companyId) {
        return bookingRepository.countBookingsByCompanyId(companyId);
    }

    @Override
    public double getMonthEarningByCompanyId(int companyId) {
        return bookingRepository.sumMonthlyEarningsByCompany(companyId);
    }

    @Override
    public BookingStatistics getRandomServicesByCompanyId(int companyId) {
        return BookingStatistics.builder()
                .approvedBookingsCount(bookingRepository.countApprovedBookings(companyId))
                .finishedBookingsCount(bookingRepository.countFinishedBookings(companyId))
                .rejectedBookingsCount(bookingRepository.countRejectedBookings(companyId))
                .build();
    }

    @Override
    public List<WorkerBookingResponse> getFinishedBookingsByCompanyId(int companyId) {
        return bookingMapper.workerBookingResponses(bookingRepository.findFinishedBookingsByCompanyId(companyId));
    }

    @Override
    public List<WorkerBookingResponse> getFinishedBookingsByDirectorId(int directorId) {
        return bookingMapper.workerBookingResponses(bookingRepository.findBookingsByDirectorIdInCurrentMonth(directorId));
    }
}

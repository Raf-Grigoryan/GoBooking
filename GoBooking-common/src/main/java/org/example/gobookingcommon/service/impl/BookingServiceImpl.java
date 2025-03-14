package org.example.gobookingcommon.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.gobookingcommon.customException.InsufficientFundsException;
import org.example.gobookingcommon.customException.SlotAlreadyBookedException;
import org.example.gobookingcommon.customException.TypeNotExistException;
import org.example.gobookingcommon.customException.UsersMismatchException;
import org.example.gobookingcommon.dto.booking.*;
import org.example.gobookingcommon.dto.subscription.BookingStatistics;
import org.example.gobookingcommon.entity.booking.Booking;
import org.example.gobookingcommon.entity.booking.PaymentMethod;
import org.example.gobookingcommon.entity.booking.Type;
import org.example.gobookingcommon.entity.user.Card;
import org.example.gobookingcommon.entity.user.User;
import org.example.gobookingcommon.entity.work.Service;
import org.example.gobookingcommon.entity.work.WorkGraphic;
import org.example.gobookingcommon.mapper.BookingMapper;
import org.example.gobookingcommon.repository.BookingRepository;
import org.example.gobookingcommon.repository.ServiceRepository;
import org.example.gobookingcommon.service.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
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
                }
                if (removeTime(new Date()).equals(removeTime(bookingDate)) && LocalTime.now().isAfter(currentSlot)) {
                    isSlotAvailable = false;
                    currentSlot = currentSlot.plusMinutes(30);
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
        if (bookingDate == null) {
            bookingDate = removeTime(new Date());
        }
        Service service = serviceRepository.findById(saveBookingRequest.getServiceId()).orElse(null);
        if (service != null && !service.getWorker().equals(user)) {
            if (bookingRepository.existsAllByBookingDateAndStartedTimeAndService_Worker(bookingDate, saveBookingRequest.getStartTime(), service.getWorker())) {
                throw new SlotAlreadyBookedException("Slot already booked!");
            }
            if (cardNumber != null && !cardNumber.isEmpty()) {
                Card card = cardService.getCardByCardNumber(cardNumber);
                if (card.getBalance().compareTo(BigDecimal.valueOf(service.getPrice())) > 0) {
                    bookingSave(service, user, bookingDate, saveBookingRequest);
                    card.setBalance(card.getBalance().subtract(BigDecimal.valueOf(service.getPrice())));
                    cardService.editCard(card);
                    bookingBalanceService.addFunds(service.getPrice());
                } else {
                    throw new InsufficientFundsException("There may be insufficient funds on your card.");
                }
            } else {
                bookingSave(service, user, bookingDate, saveBookingRequest);
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
    public List<WorkerBookingResponse> clientFinishedBookings(int clientId) {
        return bookingMapper.workerBookingResponses(bookingRepository.getBookingsByClient_IdAndType(clientId, Type.FINISHED));
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
        Page<Booking> bookings = bookingRepository.getBookingByService_Worker_IdAndType(workerId, Type.FINISHED, pageRequest);
        return bookings.map(bookingMapper::toPendingBookingResponse);
    }

    @Override

    public synchronized void reject(int bookingId, User user) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            if (!user.getEmail().equals(booking.getService().getWorker().getEmail())) {
                throw new UsersMismatchException("You are not allowed to reject this booking.");
            }
            if (!booking.getType().equals(Type.APPROVED)) {
                throw new TypeNotExistException("Booking type is not approved ");
            }
            booking.setType(Type.REJECTED);
            if (booking.getPaymentMethod().equals(PaymentMethod.CARD)) {
                Card card = cardService.getCardByUserIdAndMainIs(booking.getClient().getId(), true);
                card.setBalance(card.getBalance().add(BigDecimal.valueOf(booking.getService().getPrice())));
                bookingBalanceService.subtractFunds(booking.getService().getPrice());
                cardService.editCard(card);
            }
            bookingRepository.save(booking);
        }
    }

    @Override
    public synchronized void finished(int bookingId, User user) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            if (!user.getEmail().equals(booking.getService().getWorker().getEmail())) {
                throw new UsersMismatchException("You are not allowed to reject this booking");
            }
            if (!booking.getType().equals(Type.APPROVED)) {
                throw new TypeNotExistException("Booking type is not approved.");
            }
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
    public Optional<Double> getSumTotalEarningsByWorkerWhereTypeApproved(int workerId) {
        return bookingRepository.sumTotalEarningsByWorkerWhereTypeApproved(workerId);
    }

    @Override
    public Optional<Double> getSumTotalEarningsByWorkerWhereTypeFinished(int workerId) {
        return bookingRepository.sumTotalEarningsByWorkerWhereTypeFinished(workerId);
    }

    @Override
    public Page<PendingBookingResponse> getUnfinishedBookings(int workerId, PageRequest pageRequest) {
        Page<Booking> bookings = bookingRepository.getBookingByService_Worker_IdAndType(workerId, Type.APPROVED, pageRequest);
        return bookings.map(bookingMapper::toPendingBookingResponse);
    }

    private void finishBooking(Booking booking) {
        if (booking.getPaymentMethod().equals(PaymentMethod.CARD)) {
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

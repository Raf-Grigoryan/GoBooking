package org.example.gobooking.service;

public interface BookingBalanceService {

     void addFunds(double money);

     void subtractFunds(double money);

     double getBookingBalance();

}

package org.example.gobookingcommon.service;

public interface BookingBalanceService {

     void addFunds(double money);

     void subtractFunds(double money);

     double getBookingBalance();

}

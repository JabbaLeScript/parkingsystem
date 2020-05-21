package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.sql.Time;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }
/*
        int inHour = ticket.getInTime().getHours();
        int outHour = ticket.getOutTime().getHours();*/

        long inHour = ticket.getInTime().getTime();
        long outHour = ticket.getOutTime().getTime();


        //TODO: Some tests are failing here. Need to check if this logic is correct

        long duration = outHour - inHour;
        System.out.println(duration);

        double durationHour = TimeUnit.MILLISECONDS.toHours(duration);

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                if (durationHour>0){
                ticket.setPrice(durationHour * Fare.CAR_RATE_PER_HOUR);
                }
                else {
                    long durationMinute = TimeUnit.MILLISECONDS.toMinutes(duration);
                    double rate = (double) durationMinute / (double) 60;
                    ticket.setPrice(rate * Fare.CAR_RATE_PER_HOUR);
                }
                break;
            }
            case BIKE: {
                if (durationHour>0){
                ticket.setPrice(durationHour * Fare.BIKE_RATE_PER_HOUR);
                }
                else {
                    long durationMinute = TimeUnit.MILLISECONDS.toMinutes(duration);
                    double rate = (double) durationMinute / (double) 60;
                    ticket.setPrice(rate * Fare.BIKE_RATE_PER_HOUR);
                }
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}
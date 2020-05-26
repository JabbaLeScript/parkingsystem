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

        long inHour = ticket.getInTime().getTime();
        long outHour = ticket.getOutTime().getTime();

        //TODO: Some tests are failing here. Need to check if this logic is correct
        long duration = outHour - inHour;


        double durationHour = TimeUnit.MILLISECONDS.toHours(duration);

        long durationMinute = TimeUnit.MILLISECONDS.toMinutes(duration);

        double percentRate = (double) durationMinute / (double) 60;

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                if (durationHour>0){
                ticket.setPrice(durationHour * Fare.CAR_RATE_PER_HOUR);
                }
                else {
                    if (durationMinute<30){
                        ticket.setPrice(0.0);
                    }
                    else {
                    ticket.setPrice(percentRate * Fare.CAR_RATE_PER_HOUR);
                    }
                }
                break;
            }
            case BIKE: {
                if (durationHour>0){
                ticket.setPrice(durationHour * Fare.BIKE_RATE_PER_HOUR);
                }
                else {
                    ticket.setPrice(percentRate * Fare.BIKE_RATE_PER_HOUR);
                }
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}
package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Date;
import java.util.stream.Stream;

public class FareCalculatorServiceTest {
    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;

    @BeforeAll
    private static void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }

    @BeforeEach
    private void setUpPerTest() {
        ticket = new Ticket();
    }

    private static Stream<Arguments> createFareCarAndBike(){
        long oneHour = System.currentTimeMillis() - (60 * 60 * 1000);
        long lessThanOneHour= System.currentTimeMillis() - (45 * 60 * 1000);
        long lessThanThirtyMinutes = System.currentTimeMillis() - (20 * 60 * 1000);
        long moreThanADay = System.currentTimeMillis() - (24 * 60 * 60 * 1000);
        return Stream.of(
                Arguments.of(ParkingType.CAR, Fare.CAR_RATE_PER_HOUR, oneHour, 1),
                Arguments.of(ParkingType.BIKE, Fare.BIKE_RATE_PER_HOUR, oneHour, 1),
                Arguments.of(ParkingType.BIKE, Fare.BIKE_RATE_PER_HOUR, lessThanOneHour, 0.75),
                Arguments.of(ParkingType.CAR, Fare.CAR_RATE_PER_HOUR, lessThanOneHour, 0.75),
                Arguments.of(ParkingType.CAR, Fare.CAR_RATE_PER_HOUR, lessThanThirtyMinutes , 0.00),
                Arguments.of(ParkingType.CAR, Fare.CAR_RATE_PER_HOUR, moreThanADay , 24)
        );
    }

    //[STORY#1 : Free 30-min parking for car]
    @ParameterizedTest
    @MethodSource({"createFareCarAndBike"})
    public void calculateFare(ParkingType parkingType, Double fare, long spentTimeInParking, double percentage){

        Date inTime = new Date();
        inTime.setTime(spentTimeInParking);
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, parkingType,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertThat(ticket.getPrice()).isEqualTo(percentage*fare);
    }

    @Test
    public void calculateFareUnkownType(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, null,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @ParameterizedTest
    @EnumSource(ParkingType.class)
    public void calculateFareWithFutureInTime(ParkingType parkingType){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() + (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, parkingType,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    // [STORY#2 : 5%-discount for recurring users](https://www.notion.so/STORY-2-5-discount-for-recurring-users-a75f51e971aa4679b0e0a40dd022c081)
    @Test
    public void calculateFareCarWithDiscountForRecuringUser(){
        //ARRANGE
        /*
        save a ticket into the DB
        * */
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  24 * 60 * 60 * 1000) );//24 hours parking time should give 24 * parking fare per hour
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("abcd1");

        //ACT
        /*
        * verify that the vehicule was already registered in the DB
        * */



        //ASSERT
        double percentCar = 5 / 100 * Fare.CAR_RATE_PER_HOUR;
        assertThat(ticket.getPrice()).isEqualTo(percentCar);
    }

}

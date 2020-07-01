package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class ParkingSpotTest {
    private ParkingSpot parkingSpot1;
    private ParkingSpot parkingSpot2;

    @BeforeEach
    void setUp() {
        parkingSpot1 = new ParkingSpot(1343, ParkingType.CAR, true);
        parkingSpot2 = new ParkingSpot(1343, ParkingType.CAR, true);
    }

    @Test
    void testTheEqualityIsBasedOnTheIdOfTheParkingSpot() {
        boolean result = parkingSpot1.equals(parkingSpot2);
        assertThat(result).isEqualTo(true);
    }

    @Test
    void testHashCodeEquality(){
        assertThat(parkingSpot1.hashCode()).isEqualTo(parkingSpot2.hashCode());
    }
}
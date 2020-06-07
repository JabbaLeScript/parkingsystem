package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;

public class ParkingSpot {
    /*
    * rename to "id" to better match with busines logic and cause getter and setter were 'id'
    * */
    private int id;
    private ParkingType parkingType;
    private boolean isAvailable;

    /*
    * ajout d'un constructeur vide pour tester la classe
    * */
    public ParkingSpot() {
    }

    public ParkingSpot(int id, ParkingType parkingType, boolean isAvailable) {
        this.id = id;
        this.parkingType = parkingType;
        this.isAvailable = isAvailable;
    }

    public int getId() {
        return id;
    }

    public void setId(int number) {
        this.id = number;
    }

    public ParkingType getParkingType() {
        return parkingType;
    }

    public void setParkingType(ParkingType parkingType) {
        this.parkingType = parkingType;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingSpot that = (ParkingSpot) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}

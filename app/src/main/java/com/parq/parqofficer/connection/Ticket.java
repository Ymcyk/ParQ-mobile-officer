package com.parq.parqofficer.connection;

/**
 * Created by piotr on 28.12.16.
 */

public class Ticket {
    private String plateCountry;
    private String plateNumber;
    private String parking;

    public Ticket(String plateCountry, String plateNumber, String parking){
        this.plateCountry = plateCountry;
        this.plateNumber = plateNumber;
        this.parking = parking;
    }

    public String getPlateCountry() {
        return plateCountry;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public String getParking() {
        return parking;
    }
}

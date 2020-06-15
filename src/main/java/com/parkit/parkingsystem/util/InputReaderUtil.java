package com.parkit.parkingsystem.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class InputReaderUtil {

    private static Scanner scan = new Scanner(System.in);
    private static final Logger logger = LogManager.getLogger("InputReaderUtil");

    public int readSelection() {
        try {
            int input = Integer.parseInt(scan.nextLine());
            return input;
        }catch(Exception e){
            logger.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input. Please enter valid number for proceeding further");
            return -1;
        }
    }

    public int readSelection(Asker asker) {
        try {
            String separator = System.lineSeparator();
            String messages = "Please select vehicle type from menu" + separator
                    + "1 CAR" + separator
                    + "2 BIKE";
            int input = asker.askInt(messages);
            return input;
        }catch(Exception e){
            logger.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input. Please enter valid number for proceeding further");
            return -1;
        }
    }

    public int readSelectionMultipleInput(Asker asker) {
        try {
            List<String> messages = Arrays.asList("Please select vehicle type from menu",
                    "1 CAR",
                    "2 Bike");
            int input = asker.askIntMultipleMessage(messages);
            return input;
        }catch(Exception e){
            logger.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input. Please enter valid number for proceeding further");
            return -1;
        }
    }

    /*
    public String readVehicleRegistrationNumber() throws Exception {
        String vehicleRegNumber= scan.nextLine();

        try {
            if(vehicleRegNumber == null || vehicleRegNumber.trim().length()==0) {
                throw new IllegalArgumentException("Invalid input provided");
            }
        }catch(Exception e){
            logger.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input. Please enter a valid string for vehicle registration number");
            throw e;
        }
            return vehicleRegNumber;

    }*/

    public String readVehicleRegistrationNumber(Asker asker) throws Exception {
        String vehicleRegNumber = asker.askString("Please type the vehicle registration number and press enter key");
        try {
            if(vehicleRegNumber == null || vehicleRegNumber.trim().length()==0) {
                System.out.println("length after trim" + vehicleRegNumber.length());
                throw new IllegalArgumentException("Invalid input provided");
            }
        }catch(Exception e){
            logger.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input. Please enter a valid string for vehicle registration number");
            throw e;
        }
        return vehicleRegNumber;

    }


}

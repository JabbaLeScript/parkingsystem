package com.parkit.parkingsystem.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class InputReaderUtil {
    private static final Logger logger = LogManager.getLogger("InputReaderUtil");

    /*
     * refactoring method : injecting asker wrapper to ask user input
     * */
    public int readSelection(Asker asker, String message) {
        try {
            int input = asker.askInt(message);
            return input;
        }catch(Exception e){
            logger.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input. Please enter valid number for proceeding further");
            return -1;
        }
    }

    /*
    * refactoring method : injecting asker wrapper to ask user input
    * */
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

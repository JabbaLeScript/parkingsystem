package com.parkit.parkingsystem.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class InputReaderUtil {

    //private static Scanner scan = new Scanner(System.in);
    /**
    * injectiondependency
    * */
    private static Scanner scan = new Scanner(System.in);
    private static final Logger logger = LogManager.getLogger("InputReaderUtil");

    public InputReaderUtil() {
      //  this.scan = new Scanner(System.in);
    }

    public int readSelection(){
        try {
            int input = Integer.parseInt(scan.nextLine());
            return input;
        }catch(Exception e){
            logger.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input. Please enter valid number for proceeding further");
            return -1;
        }
    }


    /*
    new constructor added to test this method only
     */
    public int readSelection(Scanner scan){
        try {
            int input = Integer.parseInt(scan.nextLine());
            System.out.println(input);
            return input;
        }catch(Exception e){
            logger.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input. Please enter valid number for proceeding further");
            return -1;
        }
    }

    public String readVehicleRegistrationNumber() throws Exception{
        try {
            String vehicleRegNumber= scan.nextLine();
            if(vehicleRegNumber == null ||vehicleRegNumber.trim().length()==0){
                throw new IllegalArgumentException("Invalid input provided");
            }
            return vehicleRegNumber;
        }catch(Exception e){
            logger.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input. Please enter a valid string for vehicle registration number");
            throw e;
        }
    }

    /**
     * new constructor added to test this method only
     * */
    public String readVehicleRegistrationNumber(Scanner scan) throws Exception{
        try {
            String vehicleRegNumber= scan.nextLine();
            if(vehicleRegNumber == null ||vehicleRegNumber.trim().length()==0){
                throw new IllegalArgumentException("Invalid input provided");
            }
            return vehicleRegNumber;
        }catch(Exception e){
            logger.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input. Please enter a valid string for vehicle registration number");
            throw e;
        }
    }

    /*public int readSelection(Asker asker, String message) {
        try {
            int input = asker.askInt(message);
            return input;
        }catch(Exception e){
            logger.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input. Please enter valid number for proceeding further");
            return -1;
        }
    }*/

    /*public String readVehicleRegistrationNumber(Asker asker) throws Exception {
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
    }*/


}

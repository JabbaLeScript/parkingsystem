package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;
import com.parkit.parkingsystem.util.Asker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Date;

public class ParkingService {

    /**
     * this constante replace the original method designed to ask for user input
     * */
    public static final String CONS_SELECT_VEHICULE_TYPE = "Please select vehicle type from menu" + System.lineSeparator()
            + "1 CAR" + System.lineSeparator()
            + "2 BIKE";

    private Logger logger = LogManager.getLogger("ParkingService");

    //private static FareCalculatorService fareCalculatorService = new FareCalculatorService();
    //private FareCalculatorService fareCalculatorService;
    private InputReaderUtil inputReaderUtil;
    private ParkingSpotDAO parkingSpotDAO;
    private  TicketDAO ticketDAO;

    public ParkingService(InputReaderUtil inputReaderUtil, ParkingSpotDAO parkingSpotDAO, TicketDAO ticketDAO){
        this.inputReaderUtil = inputReaderUtil;
        this.parkingSpotDAO = parkingSpotDAO;
        this.ticketDAO = ticketDAO;
    }

    //to handle logger during test
    public ParkingService(Logger logger, InputReaderUtil inputReaderUtil, ParkingSpotDAO parkingSpotDAO, TicketDAO ticketDAO) {
        this.logger = logger;
        this.inputReaderUtil = inputReaderUtil;
        this.parkingSpotDAO = parkingSpotDAO;
        this.ticketDAO = ticketDAO;
    }


    /*
    * ajout des parametres ParkingSpot et ticket pour tester la classe
    * */
    public void processIncomingVehicle(ParkingSpot parkingSpot, Ticket ticket, Asker asker) throws Exception{
        try{
            getNextParkingNumberIfAvailable(parkingSpot, asker);
            if(parkingSpot !=null && parkingSpot.getId() > 0){
                 String vehicleRegNumber = getVehichleRegNumber(asker);
                 System.out.println(vehicleRegNumber);
                /*
                *Story 2
                * return if the user is recurrent or not
                 * */
                boolean isRecurringUser = ticketDAO.getReccuringUser(vehicleRegNumber);
                if (isRecurringUser){
                    System.out.println("Welcome back ! As a recurring user of our parking lot, you will benefit from a 5% discount");
                }
                parkingSpot.setAvailable(false);
                parkingSpotDAO.updateParking(parkingSpot);//allot this parking space and mark it's availability as false

                Date inTime = new Date();
                //Ticket ticket = new Ticket();
                //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
                //ticket.setId(ticketID);
                ticket.setParkingSpot(parkingSpot);
                ticket.setVehicleRegNumber(vehicleRegNumber);
                ticket.setPrice(0);
                ticket.setInTime(inTime);
                ticket.setOutTime(null);
                //Story 2
                ticket.setReccurent(isRecurringUser);
                ticketDAO.saveTicket(ticket);
                System.out.println("Generated Ticket and saved in DB");
                System.out.println("Please park your vehicle in spot number:"+parkingSpot.getId());
                System.out.println("Recorded in-time for vehicle number:"+vehicleRegNumber+" is:"+inTime);
            }
        }catch(Exception e){
            e.getMessage();
            logger.error("Unable to process incoming vehicle",e);
            throw  e;
        }
    }

    private String getVehichleRegNumber(Asker asker) throws Exception {
        //System.out.println("Please type the vehicle registration number and press enter key");
        String vehicleRegNumber = inputReaderUtil.readVehicleRegistrationNumber(asker);
        return vehicleRegNumber;
    }

    /*
    * - explicit dependencies added
    * - set property of parkingSpot object throught setter and not directly in the constructor
    * */
    public ParkingSpot getNextParkingNumberIfAvailable(ParkingSpot parkingSpot, Asker asker) throws Exception {
        int parkingId=0;
        try{
            ParkingType parkingType = getVehichleType(asker);
            parkingId = parkingSpotDAO.getNextAvailableSlot(parkingType);
            if(parkingId > 0){
                parkingSpot.setId(parkingId);
                parkingSpot.setParkingType(parkingType);
                parkingSpot.setAvailable(true);
            }else throw new Exception("Error fetching parking number from DB. Parking slots might be full");
        } catch(IllegalArgumentException ie){
            logger.error("Error parsing user input for type of vehicle", ie);
        }
        catch(Exception e){
            logger.error("Error fetching next available parking slot", e);
        }
        return parkingSpot;
    }

    private ParkingType getVehichleType(Asker asker){
        int input = inputReaderUtil.readSelection(asker, CONS_SELECT_VEHICULE_TYPE);
        switch(input){
            case 1: {
                return ParkingType.CAR;
            }
            case 2: {
                return ParkingType.BIKE;
            }
            default: {
                System.out.println("Incorrect input provided");
                throw new IllegalArgumentException("Entered input is invalid");
            }
        }
    }

    /*
     * ajout des parametres ticket et FareCalculatorService pour tester la classe
     * */
    public void processExitingVehicle(Ticket ticket, FareCalculatorService fareCalculatorService, Asker asker) {
        try{
            String vehicleRegNumber = getVehichleRegNumber(asker);
            ticket = ticketDAO.getTicket(vehicleRegNumber);
            Date outTime = new Date();
            ticket.setOutTime(outTime);

            fareCalculatorService.calculateFare(ticket);

            if(ticketDAO.updateTicket(ticket)) {
                ParkingSpot parkingSpot = ticket.getParkingSpot();
                parkingSpot.setAvailable(true);
                //here the method return a boolean but nothing is checked
                parkingSpotDAO.updateParking(parkingSpot);
                System.out.println("Please pay the parking fare:" + ticket.getPrice());
                System.out.println("Recorded out-time for vehicle number:" + ticket.getVehicleRegNumber() + " is:" + outTime);
            }else{
                System.out.println("Unable to update ticket information. Error occurred");
            }
        }catch(Exception e){
            logger.error("Unable to process exiting vehicle",e);
        }
    }
}

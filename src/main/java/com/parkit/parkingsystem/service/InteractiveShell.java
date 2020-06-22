package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.Asker;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InteractiveShell {

    private static final Logger logger = LogManager.getLogger("InteractiveShell");
    /**
    * this constante replace the original method designed to ask for user input
    * */
    public static String CONS_SELECT_OPTION_TO_LOAD_MENU = "Please select an option. Simply enter the number to choose an action" + System.lineSeparator()
            + "1 New Vehicle Entering - Allocate Parking Space" + System.lineSeparator()
            + "2 Vehicle Exiting - Generate Ticket Price" + System.lineSeparator()
            + "3 Shutdown System";

    private InputReaderUtil inputReaderUtil;
    private ParkingSpotDAO parkingSpotDAO;
    private TicketDAO ticketDAO;
    private ParkingService parkingService;

    public InteractiveShell() {
    }

    /*
    * refactor the code to remove all static and introduce a constructor that sets the dependencies
    * it's in order to reduce dependency between my objects and my class
     * */
    public InteractiveShell(InputReaderUtil inputReaderUtil, ParkingSpotDAO parkingSpotDAO, TicketDAO ticketDAO, ParkingService parkingService) {
        this.inputReaderUtil = inputReaderUtil;
        this.parkingSpotDAO = parkingSpotDAO;
        this.ticketDAO = ticketDAO;
        this.parkingService = parkingService;
    }


    /*
    * this method was previously static ...
    *
    * */
    public void loadInterface() throws Exception {

        logger.info("App initialized!!!");
        System.out.println("Welcome to Parking System!");

        ParkingSpot parkingSpot = new ParkingSpot();
        Ticket ticket = new Ticket();

        boolean continueApp = true;

        while(continueApp){
            Asker asker = new Asker(System.in, System.out);
            int option = inputReaderUtil.readSelection(asker, CONS_SELECT_OPTION_TO_LOAD_MENU);
            switch(option){
                case 1: {
                    Asker askerIncoming = new Asker(System.in, System.out);
                    parkingService.processIncomingVehicle(parkingSpot, ticket, askerIncoming);
                    break;
                }
                case 2: {
                    parkingService.processExitingVehicle(ticket, new FareCalculatorService(), asker);
                    break;
                }
                case 3: {
                    System.out.println("Exiting from the system!");
                    break;
                }
                default: System.out.println("Unsupported option. Please enter a number corresponding to the provided menu");
            }
            continueApp = false;
        }
    }
}

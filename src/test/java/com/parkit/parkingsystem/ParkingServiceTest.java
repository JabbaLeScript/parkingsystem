package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Date;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    //private static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;

    @InjectMocks
    private ParkingService service;

    @BeforeEach
    private void setUpPerTest(){
        MockitoAnnotations.initMocks(this);
    }


    @ParameterizedTest(name ="test incoming vehicule")
    @ValueSource(ints = {1, 2})
    public void processIncomingVehicle(int input){
        when(inputReaderUtil.readSelection()).thenReturn(input);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(3);
        when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(false);
        when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);
        /*
        * next available vehicule Verification
        * */
        service.processIncomingVehicle();
        verify(parkingSpotDAO, times(1)).getNextAvailableSlot(any(ParkingType.class));
        verify(parkingSpotDAO, times(1)).updateParking(any(ParkingSpot.class));
        verify(ticketDAO,times(1)).saveTicket(any(Ticket.class));

    }

    @Test
    public void processExitingVehicle() throws Exception {

        Ticket ticket = new Ticket();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
        ticket.setVehicleRegNumber("1234");
        ticket.setParkingSpot(parkingSpot);

        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("1234");
        when(ticketDAO.getTicket("1234")).thenReturn(ticket);
        when(ticketDAO.updateTicket(ticket)).thenReturn(true);
        when(parkingSpotDAO.updateParking(parkingSpot)).thenReturn(true);


        service.processExitingVehicle();
        verify(inputReaderUtil).readVehicleRegistrationNumber();
        //verify(ticketDAO).getTicket(anyString());
        verify(ticketDAO).getTicket("1234");
        //verify(ticketDAO, times(1)).updateTicket(any(Ticket.class));
        verify(ticketDAO, times(1)).updateTicket(ticket);
        //verify(parkingSpotDAO, times(1)).updateParking(any(ParkingSpot.class));
        verify(parkingSpotDAO, times(1)).updateParking(parkingSpot);

    }


    @Test
    void checkIfReccuringUser(){
        service.processIncomingVehicle();
        verify(ticketDAO.getTicket("1234"));
    }

}

package com.parkit.parkingsystem;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.InteractiveShell;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class InteractiveShellTest {

    @Mock
    InputReaderUtil inputReaderUtil;

    @Mock
    ParkingSpotDAO parkingSpotDAO;

    @Mock
    TicketDAO ticketDAO;

    @Mock
    ParkingService parkingService;

    @InjectMocks
    InteractiveShell interactiveShell;

    @BeforeEach
    void setUp() {
        /*
        when(inputReaderUtil.readSelection()).thenReturn(1);
        interactiveShell = new InteractiveShell();*/
        MockitoAnnotations.initMocks(this);

    }


    @Test
    void allowNewVehicletoEnterandAllocateParkingSpace(){

        when(inputReaderUtil.readSelection()).thenReturn(1);
        interactiveShell.loadInterface();
        verify(parkingService).processIncomingVehicle(new ParkingSpot(), new Ticket());
    }

    @Test
    void allowVehicleExiting(){
        when(inputReaderUtil.readSelection()).thenReturn(2);
        interactiveShell.loadInterface();
        verify(parkingService).processExitingVehicle();
    }

    @Test
    void exitingFromTheSystem(){
        //arrange
       ByteArrayOutputStream errContent = new ByteArrayOutputStream();
       System.setErr(new PrintStream(errContent));

        System.err.print("Exiting from the system!");
        when(inputReaderUtil.readSelection()).thenReturn(3);

        interactiveShell.loadInterface();
        assertThat("Exiting from the system!").isEqualTo(errContent.toString());
    }

    @Test
    void unsuppotedOptionMessage(){
        //Arrange
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));

        System.err.print("Unsupported option. Please enter a number corresponding to the provided menu");
        when(inputReaderUtil.readSelection()).thenReturn(5);

        interactiveShell.loadInterface();
        assertThat("Unsupported option. Please enter a number corresponding to the provided menu")
                .isEqualTo(errContent.toString());
    }


}

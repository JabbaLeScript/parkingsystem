package com.parkit.parkingsystem;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.service.InteractiveShell;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.Asker;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
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
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void testAllowNewVehicletoEnterandAllocateParkingSpace() throws Exception {

        Asker askerLoad = new Asker(System.in, System.out);
        Asker askerIncome = new Asker(System.in, System.out);
        ParkingSpot ps = new ParkingSpot();
        Ticket ticket = new Ticket();

        when(inputReaderUtil.readSelection(askerLoad, interactiveShell.CONS_SELECT_OPTION_TO_LOAD_MENU)).thenReturn(1);

        interactiveShell.loadInterface();

        //verify(inputReaderUtil).readSelection(askerLoad, interactiveShell.CONS_SELECT_OPTION_TO_LOAD_MENU);
        verify(parkingService).processIncomingVehicle(ps, ticket, askerIncome);
    }

    @Test
    void testAllowVehicleExiting() throws Exception {

        Asker asker = new Asker(System.in, System.out);
        Asker askerIncome = new Asker(System.in, System.out);

        when(inputReaderUtil.readSelection(asker, anyString())).thenReturn(2);


        interactiveShell.loadInterface();

        verify(parkingService).processExitingVehicle(new Ticket(), new FareCalculatorService(), askerIncome);
    }

    @Test
    void testExitingFromTheSystem() throws Exception {
        //arrange
        Asker asker = new Asker(System.in, System.out);
        Asker askerIncome = new Asker(System.in, System.out);

        ByteArrayOutputStream errContent = new ByteArrayOutputStream();

        System.setErr(new PrintStream(errContent));

        System.err.print("Exiting from the system!");

        when(inputReaderUtil.readSelection(asker, anyString())).thenReturn(3);

        interactiveShell.loadInterface();
        assertThat("Exiting from the system!").isEqualTo(errContent.toString());
    }

    @Test
    void testUnsupportedOptionMessage() throws Exception {
        //Arrange
        Asker asker = new Asker(System.in, System.out);
        Asker askerIncome = new Asker(System.in, System.out);


        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));

        System.err.print("Unsupported option. Please enter a number corresponding to the provided menu");
        when(inputReaderUtil.readSelection(asker, anyString())).thenReturn(5);

        interactiveShell.loadInterface();
        assertThat("Unsupported option. Please enter a number corresponding to the provided menu")
                .isEqualTo(errContent.toString());
    }


}

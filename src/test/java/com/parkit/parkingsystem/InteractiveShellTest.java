package com.parkit.parkingsystem;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.service.InteractiveShell;
import com.parkit.parkingsystem.service.ParkingService;
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
    ParkingService parkingService;

    @Mock
    ParkingSpot parkingSpot;

    @Mock
    Ticket ticket;

    @Mock
    FareCalculatorService fareCalculatorService;

    @Mock
    InputReaderUtil inputReaderUtil;

    @InjectMocks
    InteractiveShell interactiveShell;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * rename:
     * testLoadInterfaceCallProcessIncomingVehicle
    * */
    @Test
    void testAllowNewVehicletoEnterandAllocateParkingSpace() throws Exception {

        //when(inputReaderUtil.readSelection(asker, interactiveShell.CONS_SELECT_OPTION_TO_LOAD_MENU)).thenReturn(1);

        when(inputReaderUtil.readSelection()).thenReturn(1);

        interactiveShell.loadInterface();

        verify(parkingService).processIncomingVehicle(parkingSpot, ticket);
    }
    /**
    * rename:
     * testLoadInterfaceProcessExitingVehicule
    * */
    @Test
    void testAllowVehicleExiting() throws Exception {

        //when(inputReaderUtil.readSelection(asker, interactiveShell.CONS_SELECT_OPTION_TO_LOAD_MENU)).thenReturn(2);

        when(inputReaderUtil.readSelection()).thenReturn(2);

        interactiveShell.loadInterface();

        verify(parkingService).processExitingVehicle(ticket, fareCalculatorService);
    }

    /**
     * rename:
     * testLoadIntefaceExitingSystem
     */
    @Test
    void testExitingFromTheSystem() throws Exception {

        PrintStream save_out=System.out;
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        when(inputReaderUtil.readSelection()).thenReturn(3);
        interactiveShell.loadInterface();

        assertThat(out.toString()).contains("Exiting from the system!\r\n");
    }

    /**
     * rename:
     *testLoadInterfaceThrowException
     *
     */
    @Test
    void testUnsupportedOptionMessage() throws Exception {


        PrintStream save_out = System.out;
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));


        //when(inputReaderUtil.readSelection(asker, interactiveShell.CONS_SELECT_OPTION_TO_LOAD_MENU)).thenReturn(5);
        when(inputReaderUtil.readSelection()).thenReturn(10);

        interactiveShell.loadInterface();
        assertThat(out.toString()).contains("Unsupported option. Please enter a number corresponding to the provided menu");
    }


}

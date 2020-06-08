package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

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

    /*
    * parkingsport is not null and his id is > 0
    * */

    @ParameterizedTest(name ="test incoming vehicule")
    @ValueSource(ints = {1, 2})
    public void testProcessIncomingVehicleDisplayParkingSpotID(int input){

        ParkingSpot parkingSpot = new ParkingSpot();
        Ticket ticket = new Ticket();

        when(inputReaderUtil.readSelection()).thenReturn(input);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(3);
        //when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(false);
        when(parkingSpotDAO.updateParking(parkingSpot)).thenReturn(false);
        when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);
        /*
        * next available vehicule Verification
        *
        * */
        service.processIncomingVehicle(parkingSpot,ticket);
        verify(parkingSpotDAO, times(1)).getNextAvailableSlot(any(ParkingType.class));
        verify(parkingSpotDAO, times(1)).updateParking(parkingSpot);
        verify(ticketDAO,times(1)).saveTicket(any(Ticket.class));

        assertThat(parkingSpot.getId()).isEqualTo(3);

    }

    @Test
    void testProcessIncomingVehiculeDisplayVehiculeRegNumberAndInTime() throws Exception {

        Ticket ticket = new Ticket();
        ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
        ticket.setVehicleRegNumber("1234");
        ticket.setOutTime(new Date());


        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("1234");
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(3);
        when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(false);
        //when(parkingSpotDAO.updateParking(parkingSpot)).thenReturn(false);
        when(ticketDAO.saveTicket(ticket)).thenReturn(true);

        service.processIncomingVehicle(new ParkingSpot(),ticket);

        verify(parkingSpotDAO, times(1)).getNextAvailableSlot(any(ParkingType.class));
        verify(parkingSpotDAO, times(1)).updateParking(any(ParkingSpot.class));
        verify(ticketDAO,times(1)).saveTicket(ticket);

        assertThat(ticket.getVehicleRegNumber()).isEqualTo("1234");
        assertThat(ticket.getInTime()).isEqualTo(ticket.getInTime());
    }

    @Test
    void testUnableToProcessIncomingVehicle(){

    }


    /*
    * Assert that the instance variable of parkingspot object are correctly setted
    * */

    private static Stream<Arguments> paramValueNextParkingNumberIsAvailable(){
        return Stream.of(
                Arguments.of(1, ParkingType.CAR),
                Arguments.of(2, ParkingType.BIKE)
        );
    }

    @ParameterizedTest
    @MethodSource({"paramValueNextParkingNumberIsAvailable"})
    void testTheNextParkingNumberisAvailable(int input, ParkingType parkingType){

        ParkingSpot parkingSpot = new ParkingSpot();
        when(inputReaderUtil.readSelection()).thenReturn(input);
        when(parkingSpotDAO.getNextAvailableSlot(parkingType)).thenReturn(100);


        service.getNextParkingNumberIfAvailable(parkingSpot);
        verify(inputReaderUtil).readSelection();
        verify(parkingSpotDAO).getNextAvailableSlot(parkingType);

        //assert
        assertThat(parkingSpot.getId()).isEqualTo(100);
        assertThat(parkingSpot.getParkingType()).isEqualTo(parkingType);
        assertThat(parkingSpot.isAvailable()).isEqualTo(true);
    }

    /*
    * assert parking
    * */

    /*
    * throws exception if parking spot is null or parking sport id >=0
    * */
    @Test
    void testNextParkingNumberIsNotAvailbleCauseParkingSpotis0OrNull(){
        //when
        ParkingSpot parkingSpot = new ParkingSpot();
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(0);

        // then
        service.getNextParkingNumberIfAvailable(parkingSpot);
        verify(inputReaderUtil).readSelection();
        verify(parkingSpotDAO, times(1)).getNextAvailableSlot(ParkingType.CAR);

        assertThrows(Exception.class, () -> service.getNextParkingNumberIfAvailable(parkingSpot));

    }





    /*
    *
    * */
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

    /*
    * Story 2 : checking if the user is a recurrent user
    * Step 1) initialise objects
    * Step2) initialise stubs
    * step3) verification that the method is invoked
    * */
    @Test
    void checkIfReccuringUserisInvoked() throws Exception {

        ParkingSpot parkingSpotA = new ParkingSpot(1, ParkingType.CAR, true);
        ParkingSpot parkingSpotB = new ParkingSpot(1, ParkingType.CAR, true);

        Ticket ticketA = new Ticket();
        ticketA.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
        ticketA.setVehicleRegNumber("1234");
        ticketA.setParkingSpot(parkingSpotA);

        Ticket ticketB = new Ticket();
        ticketB.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
        ticketB.setVehicleRegNumber("1234");
        ticketB.setParkingSpot(parkingSpotB);

        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("1234");
        // passer un objet à place de any
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(3);
        when(ticketDAO.getReccuringUser(ticketA.getVehicleRegNumber())).thenReturn(true);

        service.processIncomingVehicle(parkingSpotA, ticketA);

        //
        verify(ticketDAO, times(1)).getReccuringUser(ticketA.getVehicleRegNumber());
    }

}

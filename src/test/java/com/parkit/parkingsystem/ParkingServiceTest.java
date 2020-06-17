package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import com.parkit.parkingsystem.util.Asker;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ParkingServiceTest {

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;
    @Mock
    private static Logger logger;

    @InjectMocks
    private ParkingService service;

    @BeforeEach
    private void setUpPerTest()
    {
        MockitoAnnotations.initMocks(this);
    }

    /*
    * parkingsport is not null and his id is > 0
    * */

    @ParameterizedTest(name ="test incoming vehicule")
    @ValueSource(ints = {1, 2})
    public void testProcessIncomingVehicleDisplayParkingSpotID(int input) throws Exception {

        ParkingSpot parkingSpot = new ParkingSpot();
        Ticket ticket = new Ticket();
        Asker asker = new Asker(System.in, System.out);

        when(inputReaderUtil.readSelection(asker, service.CONS_SELECT_VEHICULE_TYPE)).thenReturn(input);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(3);
        //when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(false);
        when(parkingSpotDAO.updateParking(parkingSpot)).thenReturn(false);
        when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);
        /*
        * next available vehicule Verification
        *
        * */
        service.processIncomingVehicle(parkingSpot,ticket,asker);
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
        Asker asker = new Asker(System.in, System.out);

        when(inputReaderUtil.readSelection(asker, service.CONS_SELECT_VEHICULE_TYPE)).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber(asker)).thenReturn("1234");
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(3);
        when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(false);
        //when(parkingSpotDAO.updateParking(parkingSpot)).thenReturn(false);
        when(ticketDAO.saveTicket(ticket)).thenReturn(true);

        service.processIncomingVehicle(new ParkingSpot(),ticket, asker);

        verify(parkingSpotDAO, times(1)).getNextAvailableSlot(any(ParkingType.class));
        verify(parkingSpotDAO, times(1)).updateParking(any(ParkingSpot.class));
        verify(ticketDAO,times(1)).saveTicket(ticket);

        assertThat(ticket.getVehicleRegNumber()).isEqualTo("1234");
        assertThat(ticket.getInTime()).isEqualTo(ticket.getInTime());
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
    void testTheNextParkingNumberisAvailable(int input, ParkingType parkingType) throws Exception {

        ParkingSpot parkingSpot = new ParkingSpot();
        Asker asker = new Asker(System.in, System.out);

        when(inputReaderUtil.readSelection(asker, service.CONS_SELECT_VEHICULE_TYPE)).thenReturn(input);
        when(parkingSpotDAO.getNextAvailableSlot(parkingType)).thenReturn(100);


        service.getNextParkingNumberIfAvailable(parkingSpot, asker);
        verify(inputReaderUtil).readSelection(asker, service.CONS_SELECT_VEHICULE_TYPE);
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
    * throws exception if parking spot is null or parkingspot id <= 0
    *
    * */
    @Test()
    void testNextParkingNumberIsNotAvailableCauseIsFull() throws Exception {
        //when
        ParkingSpot parkingSpot = new ParkingSpot();
        Asker asker = new Asker(System.in, System.out);

        when(inputReaderUtil.readSelection(asker,service.CONS_SELECT_VEHICULE_TYPE)).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(0);

        try {
        service.getNextParkingNumberIfAvailable(parkingSpot, asker);
        }catch (Exception e){
            assertThat(e).isInstanceOf(Exception.class);
        }

        verify(inputReaderUtil).readSelection(asker, service.CONS_SELECT_VEHICULE_TYPE);
        verify(parkingSpotDAO, times(1)).getNextAvailableSlot(ParkingType.CAR);

    }

    /*
     * when the user enter a wrong vehicule type, throw exception.
     * Ie: wrong input provided
     * */
    @Test
    void testExceptionWhenUserEnterAWrongVehiculeTypeNumber() throws Exception {
        ParkingSpot parkingSpot = new ParkingSpot();
        Asker asker = new Asker(System.in, System.out);
        when(inputReaderUtil.readSelection(asker, service.CONS_SELECT_VEHICULE_TYPE)).thenReturn(3);
        try {
            service.getNextParkingNumberIfAvailable(parkingSpot, asker);

        }catch (IllegalArgumentException e){
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
            assertThat(inputReaderUtil.readSelection(asker, anyString())).isEqualTo(-1);
        }

    }


    /*
    * test all information are up to date
    *
    * */
    @Test
    void testTicketInformationAreUpdatedAtExiting() throws Exception {

        Asker asker = new Asker(System.in, System.out);
        FareCalculatorService calculator = new FareCalculatorService();
        ParkingSpot parkingSpot = new ParkingSpot(12, ParkingType.CAR, false);
        Ticket ticket = new Ticket();
        ticket.setInTime(new Date(System.currentTimeMillis() - (  60 * 60 * 1000)));
        ticket.setVehicleRegNumber("1234");
        ticket.setParkingSpot(parkingSpot);

        when(inputReaderUtil.readVehicleRegistrationNumber(asker)).thenReturn("1234");
        when(ticketDAO.getTicket("1234")).thenReturn(ticket);
        when(ticketDAO.updateTicket(ticket)).thenReturn(true);
        when(parkingSpotDAO.updateParking(parkingSpot)).thenReturn(true);

        service.processExitingVehicle(ticket, calculator, asker);
        //assert
        assertThat(ticket.getPrice()).isEqualTo(1.5);
        assertThat(ticket.getVehicleRegNumber()).isEqualTo("1234");
        assertThat(ticket.getOutTime()).isEqualTo(ticket.getOutTime());

    }

    @Test
    void testTicketInformationAreNotUpdatedAtExiting() throws Exception {

        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));
        System.err.print("Unable to update ticket information. Error occurred");

        Asker asker = new Asker(System.in, System.out);

        FareCalculatorService calculator = new FareCalculatorService();
        ParkingSpot parkingSpot = new ParkingSpot(12, ParkingType.CAR, false);
        Ticket ticket = new Ticket();
        ticket.setInTime(new Date(System.currentTimeMillis() - (  60 * 60 * 1000)));
        ticket.setVehicleRegNumber("1234");
        ticket.setParkingSpot(parkingSpot);

        when(inputReaderUtil.readVehicleRegistrationNumber(new Asker(System.in, System.out))).thenReturn("1234");
        when(ticketDAO.getTicket("1234")).thenReturn(ticket);
        when(ticketDAO.updateTicket(ticket)).thenReturn(false);

        service.processExitingVehicle(ticket, calculator, asker);
        //assert
        assertThat("Unable to update ticket information. Error occurred").isEqualTo(errContent.toString());
    }

    /*
    * if a user enter a null number or empty value, return an exception
    * */
    @Test
    void testExceptionImpossibleToGetVehiculeRegNumber() throws Exception {

        ParkingSpot parkingSpot = new ParkingSpot(12, ParkingType.CAR, false);
        Ticket ticket = new Ticket();
        ticket.setInTime(new Date(System.currentTimeMillis() - (  60 * 60 * 1000)));
        ticket.setParkingSpot(parkingSpot);
        Asker asker = new Asker(System.in, System.out);


        when(inputReaderUtil.readSelection(asker, service.CONS_SELECT_VEHICULE_TYPE)).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(2);
        when(inputReaderUtil.readVehicleRegistrationNumber(asker)).thenReturn("");

        try {
            service.processIncomingVehicle(parkingSpot,ticket, asker);

        }catch (Exception e){
            assertThat(e).isInstanceOf(Exception.class);
        }
       verify(inputReaderUtil,times(1)).readSelection(asker, service.CONS_SELECT_VEHICULE_TYPE);
       verify(inputReaderUtil, times(1)).readVehicleRegistrationNumber(asker);

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

        Asker asker = new Asker(System.in, System.out);


        Ticket ticketB = new Ticket();
        ticketB.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
        ticketB.setVehicleRegNumber("1234");
        ticketB.setParkingSpot(parkingSpotB);

        when(inputReaderUtil.readSelection(asker, service.CONS_SELECT_VEHICULE_TYPE)).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber(new Asker(System.in, System.out))).thenReturn("1234");
        // passer un objet à place de any
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(3);
        when(ticketDAO.getReccuringUser(ticketA.getVehicleRegNumber())).thenReturn(true);

        service.processIncomingVehicle(parkingSpotA, ticketA, asker);

        //
        verify(ticketDAO, times(1)).getReccuringUser(ticketA.getVehicleRegNumber());
    }

}

package com.parkit.parkingsystem;

import com.parkit.parkingsystem.util.InputReaderUtil;
import com.parkit.parkingsystem.util.Asker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InputReaderUtilTest {

    private InputReaderUtil inputReaderUtil;

    @BeforeEach
    void setUp()
    {
        inputReaderUtil = new InputReaderUtil();
    }

    @Test
    void testGetStringWhenNotNullOrZeroLength() throws Exception {
        Asker asker = mock(Asker.class);
        when(asker.askString("Please type the vehicle registration number and press enter key")).thenReturn("abcd");
        assertThat(inputReaderUtil.readVehicleRegistrationNumber(asker)).isEqualTo("abcd");

    }

    @Test
    void testExceptionIfVehiculeRegNumberIsNull() {
        Asker asker = mock(Asker.class);
        when(asker.askString("Please type the vehicle registration number and press enter key")).thenReturn(null);
        try{
            inputReaderUtil.readVehicleRegistrationNumber(asker);
        }catch (Exception e){
            assertThat(e).isInstanceOf(Exception.class);
        }
    }

    @Test
    void testExceptionIfVehiculeRegNumberIsLengght0() {
        Asker asker = mock(Asker.class);
        when(asker.askString("Please type the vehicle registration number and press enter key")).thenReturn("");
        try{
            inputReaderUtil.readVehicleRegistrationNumber(asker);
        }catch (Exception e){
            assertThat(e).isInstanceOf(Exception.class);
        }
    }

    @Test
    void testExceptionIfVehiculeRegNumberIsLengght0WithSpaces() {
        Asker asker = mock(Asker.class);
        when(asker.askString("Please type the vehicle registration number and press enter key")).thenReturn("  ");
        try{
            inputReaderUtil.readVehicleRegistrationNumber(asker);
        }catch (Exception e){
            assertThat(e).isInstanceOf(Exception.class);
        }
    }

    @Test
    void testUserEnterValidInput(){
        Asker asker = mock(Asker.class);
        String separator = System.getProperty("line.separator");
        String messages = "Please select vehicle type from menu" + separator
                + "1 CAR" + separator
                + "2 BIKE";
        when(asker.askInt(messages)).thenReturn(1);
        assertThat(inputReaderUtil.readSelection(asker)).isEqualTo(1);
    }

    /*
    * the check is done in ParkingService.getVehicleType method
    * */
    @Test
    void testUserEnterInvalidInput(){
        Asker asker = mock(Asker.class);
        List<String> messages = Arrays.asList("Please select vehicle type from menu",
                "1 CAR",
                "2 Bike");
        when(asker.askIntMultipleMessage(messages)).thenReturn(1);
        assertThat(inputReaderUtil.readSelectionMultipleInput(asker)).isEqualTo(1);
    }


}

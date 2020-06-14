package com.parkit.parkingsystem;

import com.parkit.parkingsystem.util.InputReaderUtil;
import com.parkit.parkingsystem.util.StringAsker;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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
        StringAsker asker = mock(StringAsker.class);
        when(asker.ask("Please type the vehicle registration number and press enter key")).thenReturn("abcd");
        assertThat(inputReaderUtil.readVehicleRegistrationNumber(asker)).isEqualTo("abcd");

    }

    @Test
    void testExceptionIfVehiculeRegNumberIsNull() {
        StringAsker asker = mock(StringAsker.class);
        when(asker.ask("Please type the vehicle registration number and press enter key")).thenReturn(null);
        try{
            inputReaderUtil.readVehicleRegistrationNumber(asker);
        }catch (Exception e){
            assertThat(e).isInstanceOf(Exception.class);
        }
    }

    @Test
    void testExceptionIfVehiculeRegNumberIsLengght0() {
        StringAsker asker = mock(StringAsker.class);
        when(asker.ask("Please type the vehicle registration number and press enter key")).thenReturn("");
        try{
            inputReaderUtil.readVehicleRegistrationNumber(asker);
        }catch (Exception e){
            assertThat(e).isInstanceOf(Exception.class);
        }
    }

    @Test
    void testExceptionIfVehiculeRegNumberIsLengght0WithSpaces() {
        StringAsker asker = mock(StringAsker.class);
        when(asker.ask("Please type the vehicle registration number and press enter key")).thenReturn("  ");
        try{
            inputReaderUtil.readVehicleRegistrationNumber(asker);
        }catch (Exception e){
            assertThat(e).isInstanceOf(Exception.class);
        }
    }


}

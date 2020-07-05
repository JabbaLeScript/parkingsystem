package com.parkit.parkingsystem;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.apache.commons.io.input.NullInputStream;
import org.apache.commons.lang.NullArgumentException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.internal.matchers.Null;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class InputReaderUtilTest {

   // private InputReaderUtil inputReaderUtil;

    @BeforeEach
    void setUp()
    {
     //   inputReaderUtil = new InputReaderUtil();
    }

    /**
     * testReadSelectionRetunAnInt
    * */
    @Test
    void testUserEnterAValidInput() throws Exception {

        String input = "1";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        InputReaderUtil inputReaderUtil2 = new InputReaderUtil();
        int result = inputReaderUtil2.readSelection();
        assertThat(result).isEqualTo(1);

    }

   @Test
    void testUserEnterAInValidInput(){
       String input = "b";
       InputStream in = new ByteArrayInputStream(input.getBytes());
       System.setIn(in);

       InputReaderUtil inputReaderUtil = new InputReaderUtil();
        int result = inputReaderUtil.readSelection();

        assertThat(result).isEqualTo(-1);
    }

   @Test
    void testUserEnterAValidVehicleRegNumber() throws Exception {
        /*InputReaderUtil inputReaderUtil = new InputReaderUtil();
        String input = "b";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);*/

       String input = "13421";
       InputStream in = new ByteArrayInputStream(input.getBytes());
       System.setIn(in);

       InputReaderUtil inputReaderUtil = new InputReaderUtil();
       String result = inputReaderUtil.readVehicleRegistrationNumber();
        assertThat(result).isEqualTo("13421");
    }

    @Test
    void testExceptionIfVehiculeRegNumberIsNull() throws Exception {

        String input = "";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        InputReaderUtil inputReaderUtil = new InputReaderUtil();
        //inputReaderUtil.readVehicleRegistrationNumber();
        assertThrows(Exception.class,
                () -> {inputReaderUtil.readVehicleRegistrationNumber();});

    }

    @Test
    void testExceptionIfVehiculeRegNumberIsLengght0WithSpaces() throws Exception {

        String input = " ";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        InputReaderUtil inputReaderUtil = new InputReaderUtil();
        assertThrows(Exception.class, ()->{
            inputReaderUtil.readVehicleRegistrationNumber();
        });
    }
}

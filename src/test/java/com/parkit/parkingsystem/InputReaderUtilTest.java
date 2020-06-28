package com.parkit.parkingsystem;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.apache.commons.lang.NullArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class InputReaderUtilTest {


    @BeforeEach
    void setUp()
    {
    }

    /**
     * testReadSelectionRetunAnInt
    * */
    @Test
    void testUserEnterAValidInput() throws Exception {

        InputReaderUtil inputReaderUtil = new InputReaderUtil();
        String input = "1";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        Scanner scan = new Scanner(System.in);

        int result = inputReaderUtil.readSelection(scan);
        assertThat(result).isEqualTo(1);

    }
    @Test
    void testUserEnterAInValidInput(){
        InputReaderUtil inputReaderUtil = new InputReaderUtil();
        String input = "b";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        Scanner scan = new Scanner(System.in);

        int result = inputReaderUtil.readSelection(scan);
        assertThat(result).isEqualTo(-1);
    }

   @Test
    void testUserEnterAValidVehicleRegNumber() throws Exception {
        InputReaderUtil inputReaderUtil = new InputReaderUtil();
        String input = "b";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        String result = inputReaderUtil.readVehicleRegistrationNumber(new Scanner(System.in));
        assertThat(result).isEqualTo("b");
    }

    /**
    * not working gor now
    * */
    @Test
    void testExceptionIfVehiculeRegNumberIsNull() throws Exception {

        InputReaderUtil inputReaderUtil = new InputReaderUtil();
        String input = null;
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        Scanner scan = new Scanner(System.in);
        inputReaderUtil.readVehicleRegistrationNumber(scan);

    }

    @Test
    void testExceptionIfVehiculeRegNumberIsLengght0WithSpaces() throws Exception {

        InputReaderUtil inputReaderUtil = new InputReaderUtil();
        String input = " ";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        Scanner scan = new Scanner(System.in);
        assertThrows(Exception.class, ()->{
            inputReaderUtil.readVehicleRegistrationNumber(scan);
        });
    }
}

package com.parkit.parkingsystem.util;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;
/**
* Wrapper Class for system input/output functions. This class allow me to test drive my code for InputReaderUtil
 *
* */
public class Asker {
    private final Scanner scanner;
    private final PrintStream out;

    public Asker(InputStream in, PrintStream out) {
        this.scanner = new Scanner(in);
        this.out = out;
    }

    /**
    * this method ask for a string and return a String
    * */
    public String askString(String message){
        out.println(message);
        return scanner.nextLine();
    }
    /**
    * This method ask for a String and return an Interger
    * */
    public int askInt(String message){
        out.println(message);
        return Integer.parseInt(scanner.nextLine());
    }

}

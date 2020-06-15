package com.parkit.parkingsystem.util;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

public class Asker {
    private final Scanner scanner;
    private final PrintStream out;

    public Asker(InputStream in, PrintStream out) {
        this.scanner = new Scanner(in);
        this.out = out;
    }

    public String askString(String message){
        out.println(message);
        return scanner.nextLine();
    }

    public int askInt(String message){
        out.println(message);
        return Integer.parseInt(scanner.nextLine());
    }

    public int askIntMultipleMessage(List<String> messages){
        for (String message:messages
             ) {
            out.println(message);
        }
        return Integer.parseInt(scanner.nextLine());
        //out.println(messages);
    }
}

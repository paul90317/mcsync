package com.paul90317.mcsync.util;

import java.util.Scanner;

public class Console {
    private static Scanner scanner = new Scanner(System.in);
    public static void WriteLine(Object x){
        System.out.println(x);
    }

    public static String ReadLine(){
        return scanner.nextLine();
    }
}

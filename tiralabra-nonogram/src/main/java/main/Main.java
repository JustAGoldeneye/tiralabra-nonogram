package main;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        sanityCheck();
    }
    
    private static void sanityCheck() {
        System.out.println("Is this working? ");
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.next();
        System.out.println("OK, " + answer);
    }
}

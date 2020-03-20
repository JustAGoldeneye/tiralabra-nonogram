package main;

import java.util.Scanner;
import domain.structs.Chart;

public class Main {
    public static void main(String[] args) {
        manualTestChart();
    }
    
    private static void sanityCheck() {
        System.out.println("Is this working? ");
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.next();
        System.out.println("OK, " + answer);
    }
    
    private static void manualTestChart() {
        Chart chart = new Chart(15, 10);
        chart.PrintChart();
    }
}

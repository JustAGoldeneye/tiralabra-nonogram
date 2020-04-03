package main;

import java.util.Scanner;
import domain.structs.*;
import domain.solvers.*;

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
        chart.changeSquareStaus(1, 6, SquareStatus.BLACK);
        chart.changeSquareStaus(6, 4, SquareStatus.BLACK);
        chart.changeSquareStaus(6, 6, SquareStatus.BLACK);
        
        chart.printChart();
        System.out.println("");
        
        Row row = chart.horizontalChartRowToRow(6);
        row.PrintRow();
        System.out.println("");
        
        Row row2 = chart.verticalChartRowToRow(6);
        row2.changeSquareStatus(2, SquareStatus.CROSS);
        row2.PrintRow();
        System.out.println("");
        
        //row.subRow(1, 7).PrintRow();
        
        RowSolver rs = new RowSolver(row2);
        rs.solve();
        row2.PrintRow();
    }
}

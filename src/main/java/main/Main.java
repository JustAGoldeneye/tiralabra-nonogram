package main;

import java.util.Scanner;
import domain.structs.*;
import domain.solvers.*;
import dao.*;

public class Main {
    public static void main(String[] args) {
        CWDManTest();
    }
    
    private static void sanityCheck() {
        System.out.println("Is this working? ");
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.next();
        System.out.println("OK, " + answer);
    }
    
    private static void structureManTests() {
        Chart chart = new Chart(15, 10);
        chart.changeSquareStaus(1, 6, SquareStatus.BLACK);
        chart.changeSquareStaus(6, 4, SquareStatus.BLACK);
        chart.changeSquareStaus(6, 6, SquareStatus.BLACK);
        
        chart.printChart();
        System.out.println("");
        
        Row row = chart.horizontalChartRowToRow(6);
        row.printRow();
        System.out.println("");
    }
    
    private static void rowDifficultManTest() {
        /*
        Input:
        1 5 | ? ? 0 ? 1 ? 1 ? ? ?
        
        Expected solution:
        1 5 | ? ? 0 ? 1 1 1 1 ? 0 
        */
        
        Chart chart = new Chart(15, 10);
        chart.changeSquareStaus(1, 6, SquareStatus.BLACK);
        chart.changeSquareStaus(6, 4, SquareStatus.BLACK);
        chart.changeSquareStaus(6, 6, SquareStatus.BLACK);
        
        Row row2 = chart.verticalChartRowToRow(6);
        row2.changeSquareStatus(2, SquareStatus.CROSS);
        row2.setNumberRow(new NumberRow(new int[]{1, 5}));
        row2.printRow();
        System.out.println("");
        
        Row orig = new Row(row2.getNumberRow(), row2.copySquares(0));
        RowSolver rs = new RowSolver(row2);
        rs.solve();
        row2.printRow();
        orig.printRow();
        Boolean[] changes = rs.getChangedSquares();
        System.out.print("    | ");
        for (int i = 0; i < changes.length; i++) {
            if (changes[i]) {
                System.out.print("c ");
            } else {
                System.out.print("_ ");
            }
        }
    }
    
    private static void rowShortManTest() {
        Row row3 = new Row(new NumberRow(new int[]{1, 3}),
                new SquareStatus[]{SquareStatus.EMPTY, SquareStatus.CROSS,
                    SquareStatus.BLACK, SquareStatus.BLACK, SquareStatus.BLACK});
        row3.printRow();
        RowSolver rs2 = new RowSolver(row3);
        rs2.solve();
        row3.printRow();
    }
    
    private static void chart5X5ManTest() {
        /*
        Input:
                1 3
              2 1 1 4 3
            -----------
          3 | ? ? ? ? ?  
          4 | ? ? ? ? ? 
        1 3 | ? ? ? ? ? 
        1 1 | ? ? ? ? ? 
          2 | ? ? ? ? ?
        
        Expected solution:
                1 3
              2 1 1 4 3
            -----------
          3 | 0 0 1 1 1 
          4 | 0 1 1 1 1 
        1 3 | 1 0 1 1 1 
        1 1 | 1 0 0 1 0 
          2 | 0 1 1 0 0 
        */
        
        NumberRow topNR1 = new NumberRow(new int[]{2});
        NumberRow topNR2 = new NumberRow(new int[]{1, 1});
        NumberRow topNR3 = new NumberRow(new int[]{3, 1});
        NumberRow topNR4 = new NumberRow(new int[]{4});
        NumberRow topNR5 = new NumberRow(new int[]{3});
        
        NumberRow[] topNRs = new NumberRow[]{topNR1, topNR2, topNR3, topNR4,
            topNR5};
        
        NumberRow leftNR1 = new NumberRow(new int[]{3});
        NumberRow leftNR2 = new NumberRow(new int[]{4});
        NumberRow leftNR3 = new NumberRow(new int[]{1, 3});
        NumberRow leftNR4 = new NumberRow(new int[]{1, 1});
        NumberRow leftNR5 = new NumberRow(new int[]{2});
        
        NumberRow[] leftNRs = new NumberRow[]{leftNR1, leftNR2, leftNR3,
            leftNR4, leftNR5};
        
        Chart chart = new Chart(topNRs, leftNRs);
        chart.printChart();
        System.out.println("");
        System.out.println("--------------------");
        System.out.println("");
        
        SimpleChartSolver cs = new SimpleChartSolver(chart);
        cs.solve();
        
        chart.printChart();
    }
    
    private static void chartUncertainManTest() {
        /*
        Input:
            2 2
            2 2
          -----
        1 | ? ? 
        1 | ? ? 
        1 | ? ? 
        1 | ? ? 
        0 | ? ? 
        2 | ? ? 
        2 | ? ?
        
        Expected solution:
            2 2
            2 2
          -----
        1 | ? ? 
        1 | ? ? 
        1 | ? ? 
        1 | ? ? 
        0 | 0 0 
        2 | 1 1 
        2 | 1 1 
        */
        
        NumberRow topNR1 = new NumberRow(new int[]{2, 2});
        NumberRow topNR2 = new NumberRow(new int[]{2, 2});
        
        NumberRow[] topNRs = new NumberRow[]{topNR1, topNR2};
        
        NumberRow leftNR1 = new NumberRow(new int[]{1});
        NumberRow leftNR2 = new NumberRow(new int[]{1});
        NumberRow leftNR3 = new NumberRow(new int[]{1});
        NumberRow leftNR4 = new NumberRow(new int[]{1});
        NumberRow leftNR5 = new NumberRow();
        NumberRow leftNR6 = new NumberRow(new int[]{2});
        NumberRow leftNR7 = new NumberRow(new int[]{2});
        
        NumberRow[] leftNRs = new NumberRow[]{leftNR1, leftNR2, leftNR3,
            leftNR4, leftNR5, leftNR6, leftNR7};
        
        Chart chart = new Chart(topNRs, leftNRs);
        chart.printChart();
        System.out.println("");
        System.out.println("--------------------");
        System.out.println("");
        
        SimpleChartSolver cs = new SimpleChartSolver(chart);
        cs.solve();
        
        chart.printChart();
    }
    
    private static void CWDManTest() {
        CWDReader r = new CWDReader("test_input/picross_s4_p002.cwd");
        Chart c = r.read();
        c.printChart();
        SimpleChartSolver s = new SimpleChartSolver(c);
        s.solve();
        c.printChart();
    }
    
    private static void NGRESManTest() {
        NGRESReader nr = new NGRESReader("test_input/uncertain_test_1_simple.ngres");
        SquareStatus[][] s = nr.read();
        for (int i = 0; i < s.length; i++) {
            for (int j = 0; j < s[0].length; j++) {
                System.out.print(s[i][j] + " ");
            }
            System.out.println("");
        }
    }
}

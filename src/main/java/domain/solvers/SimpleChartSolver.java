package domain.solvers;

import domain.structs.*;

/**
 * Loops through the given nonogram chart with RowSolver until the solution or
 * an uncertain situation has been reached. Has some optimizations to avoid
 * redundant checking of row and to vary between checking rows and columns to
 * improve efficency.
 * @author eemeli
 */
public class SimpleChartSolver {
    private Chart chart;
    private int currentRound;
    private int[] latestChangeToRows;
    private int[] latestChangeToColumns;
    private Boolean solvesInLastRound;
    private Boolean chartContainsEmpties;
    
    public SimpleChartSolver(Chart chart) {
        this.chart = chart;
        this.currentRound = 1;
        this.latestChangeToRows = new int[this.chart.verticalLength()];
        this.latestChangeToColumns = new int[this.chart.horizontalLength()];
        this.solvesInLastRound = true;
        this.chartContainsEmpties = true;
    }
    
    public void solve() {
        while (this.solvesInLastRound && this.chartContainsEmpties) {
            this.solvesInLastRound = false;
            this.chartContainsEmpties = false;
            /*
            //DB
            this.chart.printChart();
            System.out.println("");
            System.out.println(this.currentRound);
            System.out.println("");
            for (int i = 0; i < this.latestChangeToRows.length; i++) {
                System.out.println(this.latestChangeToRows[i]);
            }
            System.out.println("");
            for (int i = 0; i < this.latestChangeToColumns.length; i++) {
                System.out.println(this.latestChangeToColumns[i]);
            }
            System.out.println("");
            //-
            */
            this.checkChartOnce();
            //this.checkChartOnceSimple();
            currentRound++;
            //System.out.println("cr:" + this.currentRound);
        }
    }
    
    private void checkChartOnce() {
        int rowIndex = 0;
        int columnIndex = 0;
        while ((rowIndex < this.chart.verticalLength())
                || (columnIndex < this.chart.horizontalLength()))
            if (((double) rowIndex) / ((double) this.chart.verticalLength()) <= ((double) columnIndex) / ((double) this.chart.horizontalLength())) {
                //System.out.println("row");
                if (this.latestChangeToRows[rowIndex] >= this.currentRound - 1) {
                    this.solveRow(rowIndex);
                }
                rowIndex++;
            } else {
                //System.out.println("column");
                if (this.latestChangeToColumns[columnIndex] >= this.currentRound -1) {
                    this.solveColumn(columnIndex);
                }
                columnIndex++;
            }
        this.solvesInLastRound = true;
    }
    
    // Used for comparasion purposes
    private void checkChartOnceSimple() {
        for (int i = 0; i < this.chart.verticalLength(); i++) {
            if (this.latestChangeToRows[i] >= this.currentRound - 1) {
                this.solveRow(i);
            }
        }
        for (int i = 0; i < this.chart.horizontalLength(); i++) {
            if (this.latestChangeToColumns[i] >= this.currentRound -1) {
                this.solveColumn(i);
            }
        }
        this.solvesInLastRound = true;
    }
    
    private void solveRow(int index) {
        Row result = this.chart.horizontalChartRowToRow(index);
        RowSolver rs = new RowSolver(result);
        rs.solve();
        
        Boolean squaresChangedOnThisRow = false;
        for (int i = 0; i < this.chart.horizontalLength(); i++) {
            if (this.chart.lookSquareStatus(i, index) == SquareStatus.EMPTY) {
                this.chartContainsEmpties = true;
            }
            if (rs.getChangedSquares()[i]) {
                squaresChangedOnThisRow = true;
                this.chart.changeSquareStaus(i, index, result.lookSquareStatus(i));
                this.latestChangeToColumns[i] = this.currentRound;
            }
        }
        if (squaresChangedOnThisRow) {
            this.latestChangeToRows[index] = this.currentRound;
        }
    }
    
    private void solveColumn(int index) {
        Row result = this.chart.verticalChartRowToRow(index);
        RowSolver rs = new RowSolver(result);
        rs.solve();
        
        Boolean squaresChangedOnThisColumn = false;
        for (int i = 0; i < this.chart.verticalLength(); i++) {   
            if (this.chart.lookSquareStatus(index, i) == SquareStatus.EMPTY) {
                this.chartContainsEmpties = true;
            }
            if (rs.getChangedSquares()[i]) {
                squaresChangedOnThisColumn = true;
                this.chart.changeSquareStaus(index, i, result.lookSquareStatus(i));
                this.latestChangeToRows[i] = this.currentRound;
            } 
        }
        if (squaresChangedOnThisColumn) {
            this.latestChangeToColumns[index] = this.currentRound;
        }
    }
}

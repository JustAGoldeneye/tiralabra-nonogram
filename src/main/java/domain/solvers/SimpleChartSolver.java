package domain.solvers;

import domain.structs.*;

/**
 * Loops through the given nonogram chart with RowSolver until the solution or
 * an uncertain situation has been reached. Has some optimizations to avoid
 * redundant checking of row and to vary between checking rows and columns to
 * improve efficency.
 * @author eemeli
 */
public class SimpleChartSolver implements Solver {
    private Chart chart;
    private int currentRound;
    private int[] latestChangeToRows;
    private int[] latestChangeToColumns;
    private Boolean solvesInLastRound;
    private Boolean chartContainsEmpties;
    
    /**
     * Initializes SimpleChartSolver.
     * @param chart the chart to be solved
     */
    public SimpleChartSolver(Chart chart) {
        this.chart = chart;
        this.currentRound = 1;
        this.latestChangeToRows = new int[this.chart.verticalLength()];
        this.latestChangeToColumns = new int[this.chart.horizontalLength()];
        this.solvesInLastRound = true;
        this.chartContainsEmpties = true;
    }
    
    /**
     * Solves the chart given to the solver in the constructor
     */
    @Override
    public void solve() {
        while (this.solvesInLastRound && this.chartContainsEmpties) {
            this.solvesInLastRound = false;
            this.chartContainsEmpties = false;
            this.checkChartOnce();
            currentRound++;
        }
    }
    
    /**
     * Goes through and tries to solve all rows and columns in the chart once.
     * The rows and columns get checked in a way that at any moment the
     * percentage of the rows checked and the percentage of the columns checked
     * are as close to each other as possible.
     */
    private void checkChartOnce() {
        int rowIndex = 0;
        int columnIndex = 0;
        while ((rowIndex < this.chart.verticalLength())
                || (columnIndex < this.chart.horizontalLength())) {
            if (((double) rowIndex) / ((double) this.chart.verticalLength())
                    <= ((double) columnIndex) / ((double) this.chart.horizontalLength())) {
                if (this.latestChangeToRows[rowIndex] >= this.currentRound - 1) {
                    this.solveRow(rowIndex);
                }
                rowIndex++;
            } else {
                if (this.latestChangeToColumns[columnIndex] >= this.currentRound -1) {
                    this.solveColumn(columnIndex);
                }
                columnIndex++;
            }
        }
        this.solvesInLastRound = true;
    }
    
    /**
     * Goes through and tries to solve all rows and columns in the chart once.
     * The rows get checked first, then the columns. This method is not in use
     * currently.
     */
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
    
    /**
     * Solves the given row and saves the result to the chart.
     * @param index the index of row to be solves
     */
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
    
    /**
     * Solves the given column and saves the result to the chart.
     * @param index the index of column to be solves
     */
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

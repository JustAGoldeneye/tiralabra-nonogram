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
            //DB
            this.chart.printChart();
            //-
            this.checkChartOnce();
            currentRound++;
        }
    }
    
    private void checkChartOnce() {
        // temporary solution
        for (int i = 0; i < this.chart.verticalLength(); i++) {
            if (this.latestChangeToRows[i] >= this.currentRound - 1) {
                this.solveRow(i);
                this.solvesInLastRound = true;
            }
        }
        for (int i = 0; i < this.chart.horizontalLength(); i++) {
            if (this.latestChangeToColumns[i] >= this.currentRound -1) {
                this.solveColumn(i);
                this.solvesInLastRound = true;
            }
        }
    }
    
    private void solveRow(int index) {
        Row result = this.chart.horizontalChartRowToRow(index);
        RowSolver rs = new RowSolver(result);
        rs.solve();
        
        this.latestChangeToRows[index] = this.currentRound;
        for (int i = 0; i < this.chart.horizontalLength(); i++) {
            if (this.chart.lookSquareStatus(i, index) == SquareStatus.EMPTY) {
                this.chartContainsEmpties = true;
            }
            if (rs.getChangedSquares()[i]) {
                this.chart.changeSquareStaus(i, index, result.lookSquareStatus(i));
                this.latestChangeToColumns[i] = this.currentRound;
            }
        }
    }
    
    private void solveColumn(int index) {
        Row result = this.chart.verticalChartRowToRow(index);
        RowSolver rs = new RowSolver(result);
        rs.solve();
        
        this.latestChangeToColumns[index] = this.currentRound;
        for (int i = 0; i < this.chart.verticalLength(); i++) {   
            if (this.chart.lookSquareStatus(index, i) == SquareStatus.EMPTY) {
                this.chartContainsEmpties = true;
            }
            if (rs.getChangedSquares()[i]) {
                this.chart.changeSquareStaus(index, i, result.lookSquareStatus(i));
                this.latestChangeToRows[i] = this.currentRound;
            }
        }
    }
}

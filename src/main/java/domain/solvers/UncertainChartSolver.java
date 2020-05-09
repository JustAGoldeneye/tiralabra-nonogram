package domain.solvers;

import domain.structs.*;

/**
 * Unfinished. DO NOT USE
 * @author eemeli
 */
public class UncertainChartSolver{
    Chart chart;
    Boolean isTheHighestLevel;
    int likeliestRow;
    double likeliestRowkLikeliness;
    int  unlikeliestRow;
    double unlikeliestRowkLikeliness;
    int likeliestColumn;
    double likeliestColumnLikeliness;
    int  unlikeliestColumn;
    double unlikeliestColumnLikeliness;
    Boolean blackslikelier;
    
    public UncertainChartSolver(Chart chart, Boolean isTheHighestLevel) {
        this.chart = chart;
        
        this.isTheHighestLevel = isTheHighestLevel;
        this.likeliestRow = -1;
        this.likeliestRowkLikeliness = 0;
        this.unlikeliestRow = -1;
        this.unlikeliestRowkLikeliness = 1;
        this.likeliestColumn = -1;
        this.likeliestColumnLikeliness = -1;
        this.unlikeliestColumn = -1;
        this.unlikeliestColumnLikeliness = 1;
    }
    
    public Boolean solve() {
        SimpleChartSolver simpleSolver = new SimpleChartSolver(this.chart);
        simpleSolver.solve();
        
        int completionCheckResult = checkCompletedChart();
        if (completionCheckResult == 0) {
            return false;
        } else if (completionCheckResult == 2) {
            return true;
        }
        
        checkRowLikeliness();
        checkColumnLikeliness();
        
        UncertainChartSolver subSolver = new UncertainChartSolver(this.markLikeliestSquare(), false);
        
        if (subSolver.solve()) {
            return true;
        }
        
        if (this.blackslikelier) {
            this.chart.changeSquareStaus(this.likeliestColumn, this.likeliestRow, SquareStatus.CROSS);
        } else {
            this.chart.changeSquareStaus(this.unlikeliestColumn, this.unlikeliestRow, SquareStatus.BLACK);
        }
        
        subSolver = new UncertainChartSolver(this.chart.copyChart(), false);
        
        return subSolver.solve();
    }
    
    private void checkRowLikeliness() {
        for (int posVer = 0; posVer < this.chart.verticalLength(); posVer++) {
            int maxBlacksOnRow = 0;
            for (int num = 0; num < this.chart.leftNumberRowIn(posVer).length(); num++) {
                maxBlacksOnRow += this.chart.leftNumberRowIn(posVer).lookNumber(num);
            }
            int emptiesOnRow = 0;
            int blacksOnRow = 0;
            for (int posHor = 0; posHor < this.chart.horizontalLength(); posHor++) {
                if (this.chart.lookSquareStatus(posHor, posVer) == SquareStatus.EMPTY) {
                    emptiesOnRow++;
                } else if (this.chart.lookSquareStatus(posHor, posVer) == SquareStatus.CROSS) {
                    blacksOnRow++;
                }
            }
            double thisLikeliness = ((double) (maxBlacksOnRow - blacksOnRow)) / (double) emptiesOnRow;
            if (thisLikeliness > this.likeliestRowkLikeliness && thisLikeliness < 1) {
                //System.out.println("+");
                this.likeliestRow = posVer;
                this.likeliestRowkLikeliness = thisLikeliness;
            }   
            if (thisLikeliness < this.unlikeliestRowkLikeliness && thisLikeliness > 0) {
                //System.out.println("-");
                this.unlikeliestRow = posVer;
                this.unlikeliestRowkLikeliness = thisLikeliness;
            }
        }
        /*// DB
        System.out.println(this.likeliestRow);
        System.out.println(this.likeliestRowkLikeliness);
        System.out.println(this.unlikeliestRow);
        System.out.println(this.unlikeliestRowkLikeliness);
        // -*/
    }
    
    private void checkColumnLikeliness() {
        for (int posHor = 0; posHor < this.chart.horizontalLength(); posHor++) {
            int maxBlacksOnColumn = 0;
            for (int num = 0; num < this.chart.topNumberRowIn(posHor).length(); num++) {
                maxBlacksOnColumn += this.chart.topNumberRowIn(posHor).lookNumber(num);
            }
            int emptiesOnColumn = 0;
            int blacksOnColumn = 0;
            for (int posVer = 0; posVer < this.chart.verticalLength(); posVer++) {
                if (this.chart.lookSquareStatus(posHor, posVer) == SquareStatus.EMPTY) {
                    emptiesOnColumn++;
                } else if (this.chart.lookSquareStatus(posHor, posVer) == SquareStatus.CROSS) {
                    blacksOnColumn++;
                }
            }
            double thisLikeliness = ((double) (maxBlacksOnColumn - blacksOnColumn)) / (double) emptiesOnColumn;
            if (thisLikeliness > this.likeliestColumnLikeliness && thisLikeliness < 1) {
                //System.out.println("+");
                this.likeliestColumn = posHor;
                this.likeliestColumnLikeliness = thisLikeliness;
            }
            if (thisLikeliness < this.unlikeliestColumnLikeliness && thisLikeliness > 0) {
                //System.out.println("-");
                this.unlikeliestColumn = posHor;
                this.unlikeliestColumnLikeliness = thisLikeliness;
            }
        }
        /*// DB
        System.out.println(this.likeliestColumn);
        System.out.println(this.likeliestColumnLikeliness);
        System.out.println(this.unlikeliestColumn);
        System.out.println(this.unlikeliestColumnLikeliness);
        // -*/
    }
    
    private Chart markLikeliestSquare() {
        if (this.likeliestColumnLikeliness * this.likeliestColumnLikeliness
                >= (100 - this.unlikeliestRowkLikeliness) * (100 - this.unlikeliestColumnLikeliness)) {
            this.chart.changeSquareStaus(this.likeliestColumn, this.likeliestRow, SquareStatus.BLACK);
            this.blackslikelier = true;
        } else {
            this.chart.changeSquareStaus(this.unlikeliestColumn, this.unlikeliestRow, SquareStatus.CROSS);
            this.blackslikelier = false;
        }
        return this.chart.copyChart();
    }
    
    private int checkCompletedChart() {
        int rowsRes = checkCompletedRowsCorrect();
        int columnsRes = checkCompletedColumnsCorrect();
        
        if (rowsRes == 0 || columnsRes == 0) {
            return 0;
        } else if (rowsRes == 1 || columnsRes == 1) {
            return 1;
        }
                
        return 2;
    }
    
    private int checkCompletedRowsCorrect() {
        int currentNumberPos;
        int currentNumberLeft;
        int emptiesInTheChart = 0;
        SquareStatus currentSquare;
        for (int posVer = 0; posVer < this.chart.verticalLength(); posVer++) {
            currentNumberPos = 0;
            currentNumberLeft = this.chart.leftNumberRowIn(posVer)
                    .lookNumber(currentNumberPos);
            for (int posHor = 0; posHor < this.chart.horizontalLength(); posHor++) {
                currentSquare = this.chart.lookSquareStatus(posHor, posVer);
                if (currentSquare == SquareStatus.EMPTY) {
                    emptiesInTheChart = 1;
                    break;
                }
                if (currentSquare == SquareStatus.BLACK) {
                    currentNumberLeft--;
                    if (currentNumberLeft <= 0) {
                        if (posHor + 1 < this.chart.horizontalLength()) {
                            if (this.chart.lookSquareStatus(posHor+1, posVer)
                                    == SquareStatus.BLACK) {
                                return 0;
                            }
                            currentNumberPos++;
                            currentNumberLeft= this.chart
                                    .leftNumberRowIn(posVer)
                                    .lookNumber(currentNumberPos);
                        }
                    }
                }
                if (currentSquare == SquareStatus.CROSS && currentNumberLeft
                        < this.chart.leftNumberRowIn(posVer)
                                .lookNumber(currentNumberPos)) {
                    return 0;
                }
            }
        }
        return 2 - emptiesInTheChart;
    }
    
    private int checkCompletedColumnsCorrect() {
        int currentNumberPos;
        int currentNumberTop;
        int emptiesInTheChart = 0;
        SquareStatus currentSquare;
        for (int posHor = 0; posHor < this.chart.horizontalLength(); posHor++) {
            currentNumberPos = 0;
            currentNumberTop = this.chart.topNumberRowIn(posHor)
                    .lookNumber(currentNumberPos);
            for (int posVer = 0; posVer < this.chart.verticalLength(); posVer++) {
                currentSquare = this.chart.lookSquareStatus(posHor, posVer);
                if (currentSquare == SquareStatus.EMPTY) {
                    emptiesInTheChart = 1;
                    break;
                }
                if (currentSquare == SquareStatus.BLACK) {
                    currentNumberTop--;
                    if (currentNumberTop <= 0) {
                        if (posHor + 1 < this.chart.verticalLength()) {
                            if (this.chart.lookSquareStatus(posHor, posVer+1)
                                    == SquareStatus.BLACK) {
                                return 0;
                            }
                            currentNumberPos++;
                            currentNumberTop= this.chart
                                    .leftNumberRowIn(posHor)
                                    .lookNumber(currentNumberPos);
                        }
                    }
                }
                if (currentSquare == SquareStatus.CROSS && currentNumberTop
                        < this.chart.topNumberRowIn(posHor)
                                .lookNumber(currentNumberPos)) {
                    return 0;
                }
            }
        }
        return 2 - emptiesInTheChart;
    }
}

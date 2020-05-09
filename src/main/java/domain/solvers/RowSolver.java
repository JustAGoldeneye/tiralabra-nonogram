package domain.solvers;

import domain.structs.Row;
import domain.structs.SquareStatus;

/**
 * Used to get á solution for a nonogam row based on the current information on
 * the row.
 * @author eemeli
 */
public class RowSolver implements Solver {
    private Row row;
    private SquareStatus[] instanceSquares;
    private int[] numbers;
    private int currentInstanceNumber;
    private int startInstanceNumber;
    private SquareStatus[] previousSquares;
    private SquareStatus[] startSquares;
    private SquareStatus[] solutionSquares;
    private Boolean[] lockedSolutionSquares;
    private Boolean[] changedSquares;
    private int startNextSearchFrom;
    
    /**
     * Initializes RowSolver.
     * @param row the row to be solved
     */
    public RowSolver(Row row) {
        this.row = row;
        this.solutionSquares = this.row.copySquares(0);
        this.startSquares = this.row.copySquares(0);
        this.instanceSquares = new SquareStatus[this.row.squaresLength()];
        this.previousSquares = new SquareStatus[this.row.squaresLength()];
        this.numbers = new int[this.row.getNumberRow().length()];
        for (int i = 0; i < this.numbers.length; i++) {
            this.numbers[i] = this.row.getNumberRow().lookNumber(i);
        }
        this.currentInstanceNumber = 0;
        this.startInstanceNumber = 0;
        this.lockedSolutionSquares = new Boolean[this.solutionSquares.length];
        this.changedSquares = new Boolean[this.solutionSquares.length];
        for (int i = 0; i < this.solutionSquares.length; i++) {
            this.changedSquares[i] = false;
            this.lockedSolutionSquares[i] = (this.row.lookSquareStatus(i) != SquareStatus.EMPTY);
        }
    }
    
    /**
     * Solves the row given to the solver in the constructor
     */
    @Override
    public void solve() {
        if (this.numbers.length == 0) {
            this.fillSolutionWithCrosses();
            this.row.setSquares(this.solutionSquares);
            return;
        }
            
        this.startNextSearchFrom = -1;
        while (this.startNextSearchFrom < this.instanceSquares.length && this.startInstanceNumber < this.numbers.length) {
            this.startNextSearchFrom = this.findNextBlackSeriesStartAtOrigin(this.startNextSearchFrom);
            if (this.startNextSearchFrom == -1) {
                break;
            }
            this.resetInstanceSquares();
            if (!this.tryFillRow()) {
                break;
            }
        }
        this.row.setSquares(this.solutionSquares);
    } 
    
    private void resetInstanceSquares() {
        for (int i = 0; i < this.instanceSquares.length; i++) {
            this.instanceSquares[i] = this.startSquares[i];
        }
        this.currentInstanceNumber = this.startInstanceNumber;
    }
    
    private boolean tryFillRow() {
        int currentPos = this.startNextSearchFrom;
        while (true) {
            if (currentPos >= this.instanceSquares.length) {
                return false;
            }
            if (currentPos == -1) {
                this.lockNextNumberToStartSquares();
                return true;
            }
            
            if (this.countEmptyAndBlackSquaresFrom(currentPos) >= this.numbers[this.currentInstanceNumber]
                    && this.checkSeriesWillNotTouchOtherBlacksOnRight(currentPos, this.numbers[this.currentInstanceNumber])) {
                this.blackenNextSeries(currentPos);
                currentPos += this.numbers[this.currentInstanceNumber];
                this.currentInstanceNumber++;
            }
            if (this.currentInstanceNumber >= this.numbers.length) {
                break;
            }
            currentPos = this.findNextBlackSeriesStartAtInstance(currentPos);
        }
        this.writeToSolution();
        savePreviousSquares();
        return true;
    }
    
    /**
     * Finds the next possible start position for a new series of black squares.
     * Returns -1 if there are no possible positions.
     * Searches the current instance of the row.
     * Depends on resetInstanceSquares().
     * @param startPos Starts the search from the square after this
     * @return The start next position for next possible series as integer
     */
    private int findNextBlackSeriesStartAtInstance(int startPos) {
        int squaresBeyondExistingBlackSquare = 0;
        Boolean existingBlackSquarePassed = false;
        for (int i = startPos + 1; i < this.instanceSquares.length; i++) {
            if (existingBlackSquarePassed) {
                squaresBeyondExistingBlackSquare++;
            } else if (this.instanceSquares[i] == SquareStatus.BLACK) {
                existingBlackSquarePassed = true;
                squaresBeyondExistingBlackSquare++;
            }
            if (this.instanceSquares[i] == SquareStatus.EMPTY
                    || this.instanceSquares[i] == SquareStatus.BLACK) {
                if (squaresBeyondExistingBlackSquare > this.numbers[this.currentInstanceNumber]) {
                    return -1;
                }
                if (i <= 0) {
                    return 0;
                }
                if (this.instanceSquares[i-1] != SquareStatus.BLACK) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    /**
     * Finds the next possible start position for a new series of black squares.
     * Returns -1 if there are no possible positions.
     * Searches the original instance of the row.
     * Does not depend on resetInstanceSquares().
     * @param startPos Starts the search from the square after this
     * @return The start next position for next possible series as integer
     */
    private int findNextBlackSeriesStartAtOrigin(int startPos) {
        int squaresBeyondExistingBlackSquare = 0;
        Boolean existingBlackSquarePassed = false;
        for (int i = startPos + 1; i < this.startSquares.length; i++) {
            if (existingBlackSquarePassed) {
                squaresBeyondExistingBlackSquare++;
            } else if (this.startSquares[i] == SquareStatus.BLACK) {
                existingBlackSquarePassed = true;
                squaresBeyondExistingBlackSquare++;
            }
            if (this.startSquares[i] == SquareStatus.EMPTY
                    || this.startSquares[i] == SquareStatus.BLACK) {
                if (squaresBeyondExistingBlackSquare > this.numbers[this.startInstanceNumber]) {
                    return -1;
                }
                if (i <= 0) {
                    return 0;
                }
                if (this.startSquares[i-1] != SquareStatus.BLACK) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    /**
     * Counts the empty and black squares from the given position on the
     * instance squares until the next cross or null square.
     * @param startPos position to start the count from
     * @return amount of found empty and black squares
     */
    private int countEmptyAndBlackSquaresFrom(int startPos) {
        int validSquares = 0;
        while (startPos + validSquares < this.instanceSquares.length
                && (this.instanceSquares[startPos + validSquares] == SquareStatus.EMPTY
                || this.instanceSquares[startPos + validSquares] == SquareStatus.BLACK)) {
            validSquares++;
        }
        return validSquares;
    }
    
    /**
     * Check whether a new series of black squares on instance squares would
     * touch other black squares on the right side.
     * @param currentPos start position of the new series
     * @param SquaresToBlacken length of the new series
     * @return 
     */
    private Boolean checkSeriesWillNotTouchOtherBlacksOnRight(int currentPos, int SquaresToBlacken) {
        if (currentPos+SquaresToBlacken >= this.instanceSquares.length) {
            // NOTE: -1 has been removed from both sides of the comparasion to avoid redundancy.
            return true;
        }
        return this.instanceSquares[currentPos+SquaresToBlacken] != SquareStatus.BLACK;
    }
    
    /**
     * Fill the instance squares with blacks starting from the given position.
     * The amount of squares to blacken are obtained from numbers and
     * currentInstanceNumber.
     * @param startPos position to start the filling from
     */
    private void blackenNextSeries(int startPos) {
        for (int i = 0; i < this.numbers[this.currentInstanceNumber] && startPos + i < this.instanceSquares.length; i++) {
            if (this.instanceSquares[startPos + i] == SquareStatus.EMPTY) {
                this.instanceSquares[startPos + i] = SquareStatus.BLACK;
            }
        }
    }
    
    /**
     * Copies current instanceSquares to previousSquares.
     */
    private void savePreviousSquares() {
        for (int i = 0; i < this.instanceSquares.length; i++) {
            this.previousSquares[i] = this.instanceSquares[i];
        }
    }
    
    /**
     * Lock the next number on numbers to be included in the start state
     * after resetting instanceSquares.
     */
    private void lockNextNumberToStartSquares() {
        int seriesNumber = -1;
        if (this.previousSquares[0] == SquareStatus.BLACK) {
            seriesNumber++;
        }
        this.startSquares[0] = this.previousSquares[0];
        int i = 1;
        while (i < this.startSquares.length) {
            if (this.previousSquares[i] == SquareStatus.BLACK
                    && this.previousSquares[i-1] != SquareStatus.BLACK) {
                seriesNumber++;
                if (seriesNumber > this.startInstanceNumber) {
                    break;
                }
            }
            this.startSquares[i] = this.previousSquares[i];
            i++;
        }
        while (i < this.startSquares.length) {
            this.startSquares[i] = this.row.lookSquareStatus(i);
            i++;
        }
        this.startInstanceNumber++;
        this.startNextSearchFrom--;
    }
    
    /**
     * Saves the result of successful instanceSquares check to solutionSquares.
     */
    private void writeToSolution() {
        for (int i = 0; i < this.solutionSquares.length; i++) {
            if (!this.lockedSolutionSquares[i]) {
                if (this.instanceSquares[i] == SquareStatus.EMPTY
                        && (this.solutionSquares[i] == SquareStatus.CROSS
                        || this.solutionSquares[i] == SquareStatus.EMPTY)) {
                    this.solutionSquares[i] = SquareStatus.CROSS;
                    this.changedSquares[i] = true;
                    
                } else if (this.instanceSquares[i] == SquareStatus.BLACK
                        && (this.solutionSquares[i] == SquareStatus.BLACK
                        || this.solutionSquares[i] == SquareStatus.EMPTY)) {
                    this.solutionSquares[i] = SquareStatus.BLACK;
                    this.changedSquares[i] = true;
                    
                } else if ((this.instanceSquares[i] == SquareStatus.EMPTY
                        && this.solutionSquares[i] == SquareStatus.BLACK)
                        || (this.instanceSquares[i] == SquareStatus.BLACK
                        && this.solutionSquares[i] == SquareStatus.CROSS)) {
                    this.solutionSquares[i] = SquareStatus.EMPTY;
                    this.lockedSolutionSquares[i] = true;
                    this.changedSquares[i] = false;
                }
            }      
        }
    }
    
    /**
     * Fills the solutionSquares completely with crosses.
     */
    private void fillSolutionWithCrosses() {
        for (int i = 0; i < this.solutionSquares.length; i++) {
            if (this.solutionSquares[i] != SquareStatus.CROSS) {
                this.solutionSquares[i] = SquareStatus.CROSS;
                this.changedSquares[i] = true;
            }
        }
    }

    public Boolean[] getChangedSquares() {
        return changedSquares;
    }
}

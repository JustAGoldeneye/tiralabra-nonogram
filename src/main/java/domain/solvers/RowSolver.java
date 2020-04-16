package domain.solvers;

import domain.structs.*;

/**
 * Used to get á solution for a nonogam row based on the current information on the row.
 * @author eemeli
 */
public class RowSolver {
    private Row row;
    private SquareStatus[] instanceSquares; // Using Row for instance should be
    private int[] instanceNumbers;          // considered.
    private int currentInstanceNumber;
    private SquareStatus[] solutionSquares;
    private Boolean[] lockedSolutionSquares;
    int startNextSearchFrom;
    
    public RowSolver(Row row) {
        this.row = row;
        this.solutionSquares = this.row.copySquares(0);
        this.instanceSquares = new SquareStatus[this.row.squaresLength()];
        this.instanceNumbers = new int[this.row.getNumberRow().length()];
        for (int i = 0; i < this.instanceNumbers.length; i++) {
            this.instanceNumbers[i] = this.row.getNumberRow().lookNumber(i);
        }
        this.currentInstanceNumber = 0;
        this.lockedSolutionSquares = new Boolean[this.solutionSquares.length];
        for (int i = 0; i < this.lockedSolutionSquares.length; i++) {
            this.lockedSolutionSquares[i] = (this.row.lookSquareStatus(i) != SquareStatus.EMPTY);
        }
    }
    
    public void solve() {
        this.startNextSearchFrom = -1;
        while (this.startNextSearchFrom < this.instanceSquares.length) {
            /*
            this.resetInstanceSquares();
            for (int i = this.startNextSearchFrom; i < this.instanceSquares.length; i++) {
                this.tryToMarkSquare(i);
            }
            this.writeToSolution();
            this.startNextSearchFrom++;
            */
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
        for (int i = this.startNextSearchFrom; i < this.instanceSquares.length; i++) {
            this.instanceSquares[i] = this.row.lookSquareStatus(i);
        }
    }
    
    private boolean tryFillRow() {
        int currentPos = this.startNextSearchFrom;
        /*while (currentPos < this.instanceSquares.length) {
            
        }*/
        
        // count squares, retrun false if no more room
        // fill squares (adds to ctsf)
        // go next
        // return true
        this.writeToSolution();
        return true;
        // REMOVE COMMENTED CODE WHEN UNNECESSARY
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
        for (int i = startPos + 1; i < this.instanceSquares.length; i++) {
            if (this.instanceSquares[i] == SquareStatus.EMPTY) {
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
        for (int i = startPos + 1; i < this.row.squaresLength(); i++) {
            if (this.row.lookSquareStatus(i) == SquareStatus.EMPTY) {
                if (i <= 0) {
                    return 0;
                }
                if (this.row.lookSquareStatus(i-1) != SquareStatus.BLACK) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    private int countEmptySquaresFrom(int startPos) {
        int emptySquares = 0;
        while (startPos + emptySquares < this.instanceSquares.length
                && this.instanceSquares[startPos + emptySquares] == SquareStatus.EMPTY) {
            emptySquares++;
        }
        return emptySquares;
    }
    
    /*
    private void tryToMarkSquare(int pos) {
        if(this.instanceSquares[pos] != SquareStatus.EMPTY) {
            return;
        }
        if (pos > 0) {
            if (this.instanceSquares[pos-1] == SquareStatus.BLACK) {
                return;
            }
        }
        if (pos < this.instanceSquares.length - 1) {
            if (this.instanceSquares[pos+1] == SquareStatus.BLACK) {
                return;
            }
        }
        
        // Unfinished, at least backtracking and marking several squares in
        // a row needed. JUNIT + JAVADOC NEEDED
        // Make a stack structure and use it to replace instance number and
        // current instance number (top value should be editable and lookable
        // without popping)?
        
        this.instanceSquares[pos] = SquareStatus.BLACK;
    }
    */
    
    private void writeToSolution() {
        for (int i = this.startNextSearchFrom; i < this.solutionSquares.length; i++) {
            if (!this.lockedSolutionSquares[i]) {
                /*
                if (this.solutionSquares[i] == SquareStatus.EMPTY
                        && this.instanceSquares[i] != SquareStatus.EMPTY) {
                    this.solutionSquares[i] = this.instanceSquares[i];
                } else if ((this.solutionSquares[i] == SquareStatus.BLACK
                        && this.instanceSquares[i] == SquareStatus.CROSS)
                        || (this.solutionSquares[i] == SquareStatus.CROSS
                        && this.instanceSquares[i] == SquareStatus.BLACK)) {
                    this.solutionSquares[i] = SquareStatus.EMPTY;
                    this.lockedSolutionSquares[i] = true;
                }
                */
                if (this.instanceSquares[i] == SquareStatus.EMPTY
                        && (this.solutionSquares[i] == SquareStatus.CROSS)
                        || this.solutionSquares[i] == SquareStatus.EMPTY) {
                    this.solutionSquares[i] = SquareStatus.CROSS;
                } else if (this.instanceSquares[i] == SquareStatus.BLACK
                        && (this.solutionSquares[i] == SquareStatus.BLACK)
                        || this.solutionSquares[i] == SquareStatus.EMPTY) {
                    this.solutionSquares[i] = SquareStatus.BLACK;
                } else if ((this.instanceSquares[i] == SquareStatus.EMPTY
                        && this.solutionSquares[i] == SquareStatus.BLACK)
                        || (this.instanceSquares[i] == SquareStatus.BLACK
                        && this.solutionSquares[i] == SquareStatus.CROSS)) {
                    this.solutionSquares[i] = SquareStatus.EMPTY;
                }
            }      
        }
    }
}

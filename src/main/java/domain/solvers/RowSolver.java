package domain.solvers;

import domain.structs.*;

/**
 * Used to get á solution for a nonogam row based on the current information on the row.
 * @author eemeli
 */
public class RowSolver {
    private Row row;
    private SquareStatus[] instanceSquares; // Using Row for instance should be
    private int[] numbers;          // considered.
    private int currentInstanceNumber;
    private SquareStatus[] solutionSquares;
    private Boolean[] lockedSolutionSquares;
    private int startNextSearchFrom;
    
    public RowSolver(Row row) {
        this.row = row;
        this.solutionSquares = this.row.copySquares(0);
        this.instanceSquares = new SquareStatus[this.row.squaresLength()];
        this.numbers = new int[this.row.getNumberRow().length()];
        for (int i = 0; i < this.numbers.length; i++) {
            this.numbers[i] = this.row.getNumberRow().lookNumber(i);
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
            System.out.println("Start from " + this.startNextSearchFrom);
            if (this.startNextSearchFrom == -1) {
                break;
            }
            this.resetInstanceSquares();
            if (!this.tryFillRow()) {
                break;
            }
            System.out.println("");
        }
        this.row.setSquares(this.solutionSquares);
    } 
    
    private void resetInstanceSquares() {
        this.instanceSquares = this.row.copySquares(0);
        this.currentInstanceNumber = 0;
    }
    
    private boolean tryFillRow() {
        int currentPos = this.startNextSearchFrom;
        int nextSeriesStartPos;
        while (currentPos < this.instanceSquares.length
                && this.currentInstanceNumber < this.numbers.length) {
            nextSeriesStartPos = this.findNextBlackSeriesStartAtInstance(currentPos);
            System.out.println("Pos:  " + currentPos + ", Next: " + nextSeriesStartPos);
            if (nextSeriesStartPos == -1) {
                System.out.println("false");
                return false;
            }
            System.out.println("Current number: " + this.numbers[this.currentInstanceNumber]);
            System.out.println("Empty: " + this.countEmptyAndBlackSquaresFrom(currentPos));
            if (this.countEmptyAndBlackSquaresFrom(currentPos) >= this.numbers[this.currentInstanceNumber]
                    && this.checkSeriesWillNotTouchOtherBlacksOnRight(currentPos, this.numbers[this.currentInstanceNumber])) {
                this.blackenNextSeries(currentPos);
                this.currentInstanceNumber++;
            }
            currentPos = nextSeriesStartPos;
        }
        System.out.println("WRITING");
        this.writeToSolution();
        System.out.println("true");
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
            }
            if (this.instanceSquares[i] == SquareStatus.EMPTY) {
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
        for (int i = startPos + 1; i < this.row.squaresLength(); i++) {
            if (existingBlackSquarePassed) {
                squaresBeyondExistingBlackSquare++;
            } else if (this.instanceSquares[i] == SquareStatus.BLACK) {
                existingBlackSquarePassed = true;
            }
            if (this.row.lookSquareStatus(i) == SquareStatus.EMPTY) {
                if (squaresBeyondExistingBlackSquare > this.numbers[0]) {
                    return -1;
                }
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
    
    private int countEmptyAndBlackSquaresFrom(int startPos) {
        int validSquares = 0;
        while (startPos + validSquares < this.instanceSquares.length
                && (this.instanceSquares[startPos + validSquares] == SquareStatus.EMPTY
                || this.instanceSquares[startPos + validSquares] == SquareStatus.BLACK)) {
            validSquares++;
        }
        return validSquares;
    }
    
    private Boolean checkSeriesWillNotTouchOtherBlacksOnRight(int currentPos, int SquaresToBlacken) {
        if (currentPos+SquaresToBlacken >= this.instanceSquares.length - 1) {
            return true;
        }
        return this.instanceSquares[currentPos+SquaresToBlacken] != SquareStatus.BLACK;
    }
    
    private void blackenNextSeries(int startPos) {
        System.out.println("Black: " + this.numbers[this.currentInstanceNumber]);
        System.out.println(startPos);
        for (int i = 0; i < this.numbers[this.currentInstanceNumber] && startPos + i < this.instanceSquares.length; i++) {
            System.out.println("b: " + i);
            if (this.instanceSquares[startPos + i] == SquareStatus.EMPTY) {
                this.instanceSquares[startPos + i] = SquareStatus.BLACK;
            }
        }
        Row res = new Row(new NumberRow(this.numbers), this.instanceSquares);
        res.PrintRow();
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

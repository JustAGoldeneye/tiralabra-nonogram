package domain.solvers;

import domain.structs.Row;
import domain.structs.SquareStatus;

/**
 * Used to get á solution for a nonogam row based on the current information on the row.
 * @author eemeli
 */
public class RowSolver {
    private Row row;
    private SquareStatus[] instanceSquares; // Using Row for instance should be
    private int[] numbers;          // considered.
    private int currentInstanceNumber;
    private int startInstanceNumber;
    private SquareStatus[] previousSquares;
    private SquareStatus[] startSquares; // Replace with a better tryToFill()?
    private SquareStatus[] solutionSquares;
    private Boolean[] lockedSolutionSquares;
    private Boolean[] changedSquares;
    private int startNextSearchFrom;
    
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
    
    public void solve() {
        if (this.numbers.length == 0) {
            this.fillSolutionWithCrosses();
            this.row.setSquares(this.solutionSquares);
            return;
        }
            
        this.startNextSearchFrom = -1;
        while (this.startNextSearchFrom < this.instanceSquares.length && this.startInstanceNumber < this.numbers.length) {
            /*
            this.resetInstanceSquares();
            for (int i = this.startNextSearchFrom; i < this.instanceSquares.length; i++) {
                this.tryToMarkSquare(i);
            }
            this.writeToSolution();
            this.startNextSearchFrom++;
            */
            //System.out.println("Count start from based on " + this.startNextSearchFrom);
            this.startNextSearchFrom = this.findNextBlackSeriesStartAtOrigin(this.startNextSearchFrom);
            /*//Debugging
            Row db = new Row(this.row.getNumberRow(), this.startSquares);
            System.out.print("DBS: ");
            db.printRow();
            // -*/
            //System.out.println("Start from " + this.startNextSearchFrom);
            //System.out.println("Start series " + this.startInstanceNumber);
            if (this.startNextSearchFrom == -1) {
                break;
            }
            this.resetInstanceSquares();
            if (!this.tryFillRow()) {
                break;
            }
            //System.out.println("");
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
        //int nextSeriesStartPos;
        while (true) {
            if (currentPos >= this.instanceSquares.length) {
                //System.out.println("meni sinne");
                return false;
                // ???
            }
            if (currentPos == -1) {
                //System.out.println("lock");
                this.lockNextNumberToStartSquares();
                return true;
            }
            
            /*
            // DB
            System.out.println("Position: " + currentPos);
            System.out.println("Current number: " + this.numbers[this.currentInstanceNumber]);
            System.out.println("Empty: " + this.countEmptyAndBlackSquaresFrom(currentPos));
            System.out.println("Count:" + this.countEmptyAndBlackSquaresFrom(currentPos));
            // -
            */
            
            if (this.countEmptyAndBlackSquaresFrom(currentPos) >= this.numbers[this.currentInstanceNumber]
                    && this.checkSeriesWillNotTouchOtherBlacksOnRight(currentPos, this.numbers[this.currentInstanceNumber])) {
                this.blackenNextSeries(currentPos);
                this.currentInstanceNumber++;
            }
            if (this.currentInstanceNumber >= this.numbers.length) {
                break;
            }
            //System.out.println("");
            currentPos = this.findNextBlackSeriesStartAtInstance(currentPos);
            //System.out.println("Pos:  " + currentPos);
            /*if (currentPos >= this.instanceSquares.length) {
                System.out.println("meni sinne");
                return false;
                // ???
            }
            nextSeriesStartPos = this.findNextBlackSeriesStartAtInstance(currentPos);
            System.out.println("Pos:  " + currentPos + ", Next: " + nextSeriesStartPos);
            if (nextSeriesStartPos == -1) {
                System.out.println("false");
                this.lockNextNumberToStartSquares();
                return true;
            }
            System.out.println("Current number: " + this.numbers[this.currentInstanceNumber]);
            System.out.println("Empty: " + this.countEmptyAndBlackSquaresFrom(currentPos));
            System.out.println("Count:" + this.countEmptyAndBlackSquaresFrom(currentPos));
            if (this.countEmptyAndBlackSquaresFrom(currentPos) >= this.numbers[this.currentInstanceNumber]
                    && this.checkSeriesWillNotTouchOtherBlacksOnRight(currentPos, this.numbers[this.currentInstanceNumber])) {
                this.blackenNextSeries(currentPos);
                this.currentInstanceNumber++;
            }
            currentPos = nextSeriesStartPos;
            System.out.println("");*/
        }
        //System.out.println("WRITING");
        this.writeToSolution();
        savePreviousSquares();
        /* //TEST CODE, add to RowSolverTest
        Row justATest = new Row(this.row.getNumberRow(), this.previousSquares);
        justATest.printRow();
        */
        //System.out.println("true");
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
        //System.out.println("instance search");
        int squaresBeyondExistingBlackSquare = 0;
        Boolean existingBlackSquarePassed = false;
        for (int i = startPos + 1; i < this.instanceSquares.length; i++) {
            if (existingBlackSquarePassed) {
                squaresBeyondExistingBlackSquare++;
            } else if (this.instanceSquares[i] == SquareStatus.BLACK) {
                existingBlackSquarePassed = true;
                squaresBeyondExistingBlackSquare++;
            }
            /*
            if (i < this.instanceSquares.length - 1) {
                if (this.instanceSquares[i+1] == SquareStatus.BLACK) {
                    continue;
                }
            }
            */
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
        //System.out.println("origin search");
        int squaresBeyondExistingBlackSquare = 0;
        Boolean existingBlackSquarePassed = false;
        for (int i = startPos + 1; i < this.startSquares.length; i++) {
            if (existingBlackSquarePassed) {
                squaresBeyondExistingBlackSquare++;
            } else if (this.startSquares[i] == SquareStatus.BLACK) {
                existingBlackSquarePassed = true;
                squaresBeyondExistingBlackSquare++;
            }
            /*
            if (i < this.startSquares.length - 1) {
                if (this.startSquares[i+1] == SquareStatus.BLACK) {
                    continue;
                }
            }
            */
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
        if (currentPos+SquaresToBlacken >= this.instanceSquares.length) {
            // NOTE: -1 has been removed from both sides of the comparasion to avoid redundancy.
            return true;
        }
        return this.instanceSquares[currentPos+SquaresToBlacken] != SquareStatus.BLACK;
    }
    
    private void blackenNextSeries(int startPos) {
        //System.out.println("Black: " + this.numbers[this.currentInstanceNumber]);
        //System.out.println(startPos);
        for (int i = 0; i < this.numbers[this.currentInstanceNumber] && startPos + i < this.instanceSquares.length; i++) {
            //System.out.println("b: " + i);
            if (this.instanceSquares[startPos + i] == SquareStatus.EMPTY) {
                this.instanceSquares[startPos + i] = SquareStatus.BLACK;
                /*//Debugging
                Row db = new Row(this.row.getNumberRow(), this.instanceSquares);
                System.out.print("DB: ");
                db.printRow();
                // -*/
            }
        }
        Row res = new Row(this.row.getNumberRow(), this.instanceSquares);
        //res.printRow();
    }
    
    private void savePreviousSquares() {
        for (int i = 0; i < this.instanceSquares.length; i++) {
            this.previousSquares[i] = this.instanceSquares[i];
        }
    }
    
    private void lockNextNumberToStartSquares() {
        /*
        // DB
        System.out.println("lock");
        // -
        */
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
        
        /*
        // DB
        System.out.println("+ Go back +:");
        System.out.println(this.startSquares.length);
        System.out.println("+");
        for (int in = 0; in < this.startSquares.length; in++) {
            System.out.println(this.startSquares[in]);
        }
        System.out.println("+ + + + +");
        // -
        */
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
        /*
        //Debugging
        System.out.print("           ");
        for (int i = 0; i < this.lockedSolutionSquares.length; i++) {
            if (this.lockedSolutionSquares[i]) {
                System.out.print("L ");
            } else {
                System.out.print("_ ");
            }
        }
        System.out.println("");
        Row db = new Row(this.row.getNumberRow(), this.solutionSquares);
        System.out.print("SOL: ");
        db.printRow();
        System.out.println("");
        //-
        */
        /*
        // DB
        for (int i = 0; i < this.instanceSquares.length; i++) {
            System.out.println("i: " + this.instanceSquares[i]
                    + ", lock: "+ this.lockedSolutionSquares[i]);
        }
        System.out.println("-----");
        // -
        */
        
        for (int i = 0; i < this.solutionSquares.length; i++) {
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
        /*
        //Debugging
        Row dib = new Row(this.row.getNumberRow(), this.instanceSquares);
        System.out.print("INS: ");
        dib.printRow();
        System.out.println("");
        db = new Row(this.row.getNumberRow(), this.solutionSquares);
        System.out.print("SOL: ");
        db.printRow();
        System.out.print("           ");
        for (int i = 0; i < this.lockedSolutionSquares.length; i++) {
            if (this.lockedSolutionSquares[i]) {
                System.out.print("L ");
            } else {
                System.out.print("_ ");
            }
        }
        System.out.println("");
        //-
        */
    }
    
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

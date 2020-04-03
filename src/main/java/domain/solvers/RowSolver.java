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
        this.startNextSearchFrom = 0;
        // A better break condition could probably be made
        while (this.startNextSearchFrom < this.instanceSquares.length) {
            this.resetInstanceSquares();
            for (int i = this.startNextSearchFrom; i < this.instanceSquares.length; i++) {
                this.tryToBlackenSquare(i);
            }
            this.writeToSolution();
            this.startNextSearchFrom++;
        }
        this.row.setSquares(this.solutionSquares);
    }
    
    private void resetInstanceSquares() {
        for (int i = this.startNextSearchFrom; i < this.instanceSquares.length; i++) {
            this.instanceSquares[i] = this.row.lookSquareStatus(i);
        }
    }
    
    private void tryToBlackenSquare(int pos) {
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
        
        // Unfinished, at least backtracking and several squares in a row needed.
        // Make a stack structure and use it to replace instance number and
        // current instance number (top value should be editable and lookable
        // without popping)
        
        this.instanceSquares[pos] = SquareStatus.BLACK;
    }
    
    private void writeToSolution() {
        for (int i = this.startNextSearchFrom; i < this.solutionSquares.length; i++) {
            if (!this.lockedSolutionSquares[i]) {
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
            }      
        }
    }
}

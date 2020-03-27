package domain.structs;

/**
 * A data structure for handling single rows.
 * @author eemeli
 */
public class Row {
    private NumberRow numberRow;
    private SquareStatus[] squares;
    
    Row(NumberRow numberRow, SquareStatus[] squares) {
        this.numberRow = numberRow;
        this.squares = squares;
    }
    
    public SquareStatus lookSquareStatus(int position) {
        return this.squares[position];
    }
    
    public void changeSquareStatus(int position, SquareStatus newStatus) {
        this.squares[position] = newStatus;
    }
    
    /**
     * Creates a new row that is a sub row of the existing row.
     * @param firstPos start position for the squares of the sub row (inclusive)
     * @param endPos end position for the squares of the sub row (exclusive)
     */
    public Row subRow(int firstPos, int endPos) {
        SquareStatus[] newSquares = new SquareStatus[endPos - firstPos];
        int i = 0;
        while (firstPos + i < endPos) {
            newSquares[i] = this.squares[firstPos + i];
            i++;
        }
        return new Row(this.numberRow, newSquares);
    }

    public NumberRow getNumberRow() {
        return numberRow;
    }
    
    // Used for debugging
    public void PrintRow() {
        for (int i = 0; i < this.numberRow.length(); i++) {
            System.out.print(this.numberRow.lookNumber(i) + " ");
        }
        System.out.print("| ");
        for (SquareStatus square : this.squares) {
            if (square == null) {
                System.out.print("N ");
            } else if (square == SquareStatus.EMPTY) {
                System.out.print("? ");
            } else if (square == SquareStatus.BLACK) {
                System.out.print("1 ");
            } else if (square == SquareStatus.CROSS) {
                System.out.print("0 ");
            } else {
                System.out.print(square + " ");
            }
        }
        System.out.println("");
    }
}

package domain.structs;

/**
 * A data structure for handling single rows.
 * @author eemeli
 */
public class Row {
    NumberRow numberRow;
    SquareStatus[] squares;
    
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
    }
}

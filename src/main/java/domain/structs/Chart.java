package domain.structs;

/**
 * A data structure for nonogram charts.
 * @author eemeli
 */
public class Chart {
    private SquareStatus[][] table;
    private NumberRow[] leftNumbers;
    private NumberRow[] topNumbers;
    
    /**
     * Creates a nonograms chart with the given number rows. Sets the squares
     * in the table to EMPTY status.
     * @param leftNumbers
     * @param topNumbers
     */
    public Chart(NumberRow[] topNumbers, NumberRow[] leftNumbers) {
        this.leftNumbers = leftNumbers;
        this.topNumbers = topNumbers;
        this.table = new SquareStatus[this.leftNumbers.length][this.topNumbers.length];
    }
    
    /**
     * Creates an empty nonogram chart of given size. Sets the squares in the table
     * to EMPTY status and fills the number rows with zeros. Used for debugging.
     * @param width width of the table
     * @param height height of the table
     */
    public Chart(int width, int height) {
        this.table = new SquareStatus[height][width];
        populateTableWithEmpties();
        this.leftNumbers = new NumberRow[height];
        populateNumberRowsWithZeros(this.leftNumbers);
        this.topNumbers = new NumberRow[width];
        populateNumberRowsWithZeros(this.topNumbers);
    }
    
    /**
     * Sets squares in the table to EMPTY status.
     */
    private void populateTableWithEmpties() {
        for (int i = 0; i < this.table.length; i++) {
            for (int j = 0; j < this.table[i].length; j++) {
                this.table[i][j] = SquareStatus.EMPTY;
            }
        }
    }
    
    /**
     * Fills the given number rows with zeros.
     * @param numberRows number row to be filled
     */
    private void populateNumberRowsWithZeros(NumberRow[] numberRows) {
        for (int i = 0; i < numberRows.length; i++) {
            numberRows[i] = new NumberRow();
        }
    }
    
    /**
     * Looks the status of the square in the given position.
     * @param posHor    horizontal position to look a square from
     * @param posVer    vertical position to look a square from
     * @return          square in the given position
     */
    public SquareStatus lookSquareStatus(int posHor, int posVer) {
        return table[posVer][posHor];
    }
    
    /**
     * Looks the status of the square in the given position.
     * @param posHor    horizontal position to look a square from
     * @param posVer    vertical position to look a square from
     * @param newStatus replaces the status of the square in the given position
     */
    public void changeSquareStaus(int posHor, int posVer, SquareStatus newStatus) {
        table[posVer][posHor] = newStatus;
    }
    
    /**
     * Get a horizontal row of the chart as a row object.
     * @param position vertical position of a horizontal row
     * @return horizontal row of the chart as a row object
     */
    public Row horizontalChartRowToRow(int position) {
        return new Row(this.leftNumbers[position], this.table[position]);
    }
    
    /**
     * Get a vertical row of the chart as a row object.
     * @param position horizontal position of a vertical row
     * @return vertical row of the chart as a row object
     */
    public Row verticalChartRowToRow(int position) {
        SquareStatus[] squareStatusRow = new SquareStatus[this.table.length];
        for (int i=0; i < this.table.length; i++) {
            squareStatusRow[i] = this.table[i][position];
        }
        return new Row(this.topNumbers[position], squareStatusRow);
    }
    
    public int horizontalLength() {
        return this.table[0].length;
    }
    
    public int verticalLength() {
        return this.table.length;
    }
    
    public NumberRow leftNumberRowIn(int position) {
        return this.leftNumbers[position];
    }
    
    public NumberRow topNumberRowIn(int position) {
        return this.topNumbers[position];
    }
    
    // TODO
    
    /**
     * Prints the chart. Used for debugging
     */
    public void printChart() {
        System.out.println("Left:");
        printNumberRows(this.leftNumbers);
        System.out.println("");
        
        System.out.println("Top:");
        printNumberRows(this.topNumbers);
        System.out.println("");
        
        System.out.println("Table:");
        printTable();
    }
    
    /**
     * Prints the given number row. Used for debugging
     * @param numberRows number row to be printed
     */
    private void printNumberRows(NumberRow[] numberRows) {
        for (NumberRow numberRow : numberRows) {
            if (numberRow == null) {
                System.out.println("N");
            } else {
                for (int i = 0; i < numberRow.length(); i++) {
                    System.out.print(numberRow.lookNumber(i) + " ");
                }
                System.out.println("");
            }
        }
        System.out.println(numberRows.length + " rows");
    }
    
    /**
     * Prints the table. Used for debugging
     */
    private void printTable() {
        for (int i = 0; i < this.verticalLength(); i++) {
            for (int j = 0; j < this.horizontalLength(); j++) {
                SquareStatus result = this.table[i][j];
                if (result == null) {
                    System.out.print("N ");
                } else if (result == SquareStatus.EMPTY) {
                    System.out.print("? ");
                } else if (result == SquareStatus.BLACK) {
                    System.out.print("1 ");
                } else if (result == SquareStatus.CROSS) {
                    System.out.print("0 ");
                }else {
                    System.out.print(result + " ");
                }
            }
            System.out.println("");
        }
        System.out.println(this.horizontalLength() + " x " + this.verticalLength());
    }
}

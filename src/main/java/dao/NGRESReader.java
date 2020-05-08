package dao;

import domain.structs.SquareStatus;
import java.util.Scanner;
import java.io.File;

/**
 *
 * @author eemeli
 */
public class NGRESReader {
    File file;
    
    public NGRESReader(String fileName) {
        this.file = new File(fileName);
    }
    
    public SquareStatus[][] read() {
        SquareStatus[][] squares;
        try (Scanner fileReader = new Scanner(this.file)) {
            int height = Integer.valueOf(fileReader.nextLine());
            squares = new SquareStatus[height][Integer.valueOf(fileReader.nextLine())];
            for (int i = 0; i < squares.length; i++) {
                squares[i] = this.rowToSquareStatus(fileReader.nextLine());
            }
            return squares;
        } catch (Exception e) {
            System.out.println("File reading error: " + e.getMessage());
        }
        return null;
    }
    
    private SquareStatus[] rowToSquareStatus(String row) {
        if (row.isEmpty()) {
            return new SquareStatus[0];
        }
        String[] parts = row.split(" ");
        SquareStatus[] rowSquares = new SquareStatus[parts.length];
        for (int i = 0; i < rowSquares.length; i++) {
            if (parts[i].equals("0")) {
                rowSquares[i] = SquareStatus.CROSS;
            } else if (parts[i].equals("1")) {
                rowSquares[i] = SquareStatus.BLACK;
            } else if (parts[i].equals("?")) {
                rowSquares[i] = SquareStatus.EMPTY;
            }
        }
        return rowSquares;
    }
}

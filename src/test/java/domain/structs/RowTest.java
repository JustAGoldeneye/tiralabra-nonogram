package domain.structs;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class RowTest {
    private Row row;
    
    @Before
    public void setUp() {
        this.row = new Row(new NumberRow(new int[]{1, 2}),
                new SquareStatus[]{SquareStatus.BLACK, SquareStatus.CROSS,
                    SquareStatus.EMPTY, SquareStatus.BLACK, SquareStatus.EMPTY});
    }
    
    @Test
    public void subRowGetsTakenCorrectly() {
        this.row = this.row.subRow(2, 5);
        if (this.row.lookSquareStatus(0) != SquareStatus.EMPTY) {
            fail("Square 0 in the sub row was incorrect.");
        } else if (this.row.lookSquareStatus(1) != SquareStatus.BLACK) {
            fail("Square 1 in the sub row was incorrect.");
        } else if (this.row.lookSquareStatus(2) != SquareStatus.EMPTY) {
            fail("Square 2 in the sub row was incorrect.");
        }
    }
}

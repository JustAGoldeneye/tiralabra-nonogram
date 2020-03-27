package domain.structs;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class NumberRowTest {
    NumberRow numberRow;
    
    @Before
    public void setUp() {
        this.numberRow = new NumberRow(new int[]{2, 4, 6, 8});
    }
    
    @Test
    public void lookingNumberWorks() {
        assertEquals(this.numberRow.lookNumber(3),8);
    }
    
    @Test
    public void changingNumberWorks() {
        this.numberRow.changeNumber(3, 15);
        assertEquals(this.numberRow.lookNumber(3),15);
    }
    
    @Test
    public void lengthIsCorrect() {
        assertEquals(this.numberRow.length(), 4);
    }
    
    @Test
    public void creates1lengthRowWhenNothingGiven() {
        NumberRow row = new NumberRow();
        assertTrue((row.length() == 1));
    }
}

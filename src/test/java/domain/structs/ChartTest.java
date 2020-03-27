/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.structs;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class ChartTest {
    Chart chart;
    
    @Before
    public void setUp() {
        this.chart = new Chart(15, 10);
    }
    
    @Test
    public void chartTableStartsAsEmpty() {
        for (int i = 0; i < this.chart.verticalLength(); i++) {
            for (int j = 0; j < this.chart.horizontalLength(); j++) {
                if (this.chart.lookSquareStatus(14, 9) != SquareStatus.EMPTY) {
                    fail();
                }
            }
        }
    }
    
    @Test
    public void changingAndlookingSquareStatusWorks() {
        this.chart.changeSquareStaus(14, 9, SquareStatus.BLACK);
        assertTrue(SquareStatus.BLACK == this.chart.lookSquareStatus(14, 9));
    }
    
    @Test
    public void chartTableWithCustomNumberRowsCanBeCreated() {
        NumberRow[] numRowsT = new NumberRow[5];
        NumberRow[] numRowsL = new NumberRow[10];
        for (NumberRow numberRow : numRowsT) {
            numberRow = new NumberRow();
        }
        for (NumberRow numberRow : numRowsL) {
            numberRow = new NumberRow();
        }
        numRowsT[4] = new NumberRow(new int[]{2});
        Chart chart2 = new Chart(numRowsT, numRowsL);
        assertTrue(chart2.topNumberRowIn(4).lookNumber(0) == 2);    
    }
}

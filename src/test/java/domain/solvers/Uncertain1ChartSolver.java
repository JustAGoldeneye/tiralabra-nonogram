package domain.solvers;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author eemeli
 */
public class Uncertain1ChartSolver extends ChartSolverTest {
    
    @Before
    public void setUp() {
        super.importData("uncertain_test_1", "simple");
    }
    
    @Test
    public void chartGetsSolvedCorrectly() {
        assertTrue(super.saveAndReturnResult(new SimpleChartSolver(super.chart)));
    }
}

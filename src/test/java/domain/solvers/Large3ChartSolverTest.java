package domain.solvers;

import org.junit.Test;
import org.junit.Before;

/**
 *
 * @author eemeli
 */
public class Large3ChartSolverTest extends ChartSolverLoggingOnlyTest {
    
    @Before
    public void setUp() {
        super.importFile("webpbn-03541", "simple");
    }
    
    @Test
    public void measureTimeRequired() {
        super.measureAndLogTime(new SimpleChartSolver(super.chart));
    }
}

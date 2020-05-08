package domain.solvers;

import org.junit.Test;
import org.junit.Before;

/**
 *
 * @author eemeli
 */
public class Large2ChartSolverTest extends ChartSolverLoggingOnlyTest {
    
    @Before
    public void setUp() {
        super.importFile("webpbn-22336", "simple");
    }
    
    @Test
    public void measureTimeRequired() {
        super.measureAndLogTime(new SimpleChartSolver(super.chart));
    }
}

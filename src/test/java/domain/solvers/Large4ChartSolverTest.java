package domain.solvers;

import org.junit.Test;
import org.junit.Before;

/**
 *
 * @author eemeli
 */
public class Large4ChartSolverTest extends ChartSolverLoggingOnlyTest {
    
    @Before
    public void setUp() {
        super.importFile("webpbn-07604", "simple");
    }
    
    @Test
    public void measureTimeRequired() {
        super.measureAndLogTime(new SimpleChartSolver(super.chart));
    }
}

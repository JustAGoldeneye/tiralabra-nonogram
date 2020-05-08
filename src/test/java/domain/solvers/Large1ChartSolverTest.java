package domain.solvers;

import org.junit.Test;
import org.junit.Before;

/**
 *
 * @author eemeli
 */
public class Large1ChartSolverTest extends ChartSolverLoggingOnlyTest {
    
    @Before
    public void setUp() {
        super.importFile("webpbn-00436", "simple");
    }
    
    @Test
    public void measureTimeRequired() {
        super.measureAndLogTime(new SimpleChartSolver(super.chart));
    }
}

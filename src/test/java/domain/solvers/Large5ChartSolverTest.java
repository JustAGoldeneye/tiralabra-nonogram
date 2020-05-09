package domain.solvers;

import org.junit.Test;
import org.junit.Before;

/**
 *
 * @author eemeli
 */
public class Large5ChartSolverTest extends ChartSolverLoggingOnlyTest {
    
    @Before
    public void setUp() {
        super.importFile("meow", "simple");
    }
    
    @Test
    public void measureTimeRequired() {
        super.measureAndLogTime(new SimpleChartSolver(super.chart));
    }
}

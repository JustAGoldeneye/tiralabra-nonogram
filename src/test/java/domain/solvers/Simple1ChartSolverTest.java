package domain.solvers;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class Simple1ChartSolverTest extends ChartSolverTest {
    
    @Before
    public void setUp() {
        super.importFiles("picross_s4_p002", "simple");
    }
    
    @Test
    public void chartGetsSolvedCorrectly() {
        assertTrue(super.test(new SimpleChartSolver(super.chart)));
    }
}

package domain.solvers;

import dao.CWDReader;
import dao.TestLogWriter;
import domain.structs.Chart;

/**
 *
 * @author eemeli
 */
public abstract class ChartSolverLoggingOnlyTest {
    protected Chart chart;
    long solveTime;
    String name;
    String solutionType;
    
    protected void importFile(String name, String solutionType) {
        CWDReader cwdReader = new CWDReader("test_input/" + name + ".cwd");
        this.chart = cwdReader.read();
        this.name = name;
        this.solutionType = solutionType;
    }
    
    protected void solveAndCountTime(Solver solver) {
        long startTime = System.currentTimeMillis();
        solver.solve();
        this.solveTime = System.currentTimeMillis();
        this.solveTime -= startTime;
    }
    
    protected void measureAndLogTime(Solver solver) {
        this.solveAndCountTime(solver);
        this.logTime();
    }
    
    protected void logTime() {
        TestLogWriter writer = new TestLogWriter(this.name, this.solutionType);
        writer.writeSolveTime(solveTime);
    }
}

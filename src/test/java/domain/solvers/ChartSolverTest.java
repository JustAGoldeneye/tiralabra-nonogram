package domain.solvers;

import domain.structs.Chart;
import domain.structs.SquareStatus;
import dao.*;

public abstract class ChartSolverTest {
    protected Chart chart;
    private SquareStatus[][] solution;
    long solveTime;
    String name;
    String solutionType;
      
    protected void importData(String name, String solutionType) {
        CWDReader cwdReader = new CWDReader("test_input/" + name + ".cwd");
        this.chart = cwdReader.read();
        NGRESReader ngresReader = new NGRESReader("test_input/" + name + "_" + solutionType + ".ngres");
        this.solution = ngresReader.read();
        this.name = name;
        this.solutionType = solutionType;
    }
    
    private void solveAndCountTime(Solver solver) {
        long startTime = System.currentTimeMillis();
        solver.solve();
        this.solveTime = System.currentTimeMillis();
        this.solveTime -= startTime;
    }
    
    private Boolean checkResultMatches() {
        if (this.chart.verticalLength() != this.solution.length
                || this.chart.horizontalLength() != this.solution[0].length) {
            return false;
        }
        for (int i = 0; i < this.chart.verticalLength(); i++) {
            for (int j = 0; j < this.chart.horizontalLength(); j++) {
                if (this.chart.lookSquareStatus(j, i) != this.solution[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
    
    protected Boolean saveAndReturnResult(Solver solver) {
        this.solveAndCountTime(solver);
        if (this.checkResultMatches()) {
            TestLogWriter writer = new TestLogWriter(this.name, this.solutionType);
            writer.writeSolveTime(solveTime);
            return true;
        }
        return false;
    }
}

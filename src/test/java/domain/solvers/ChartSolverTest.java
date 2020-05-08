package domain.solvers;

import domain.structs.SquareStatus;
import dao.*;

public abstract class ChartSolverTest extends ChartSolverLoggingOnlyTest {
    private SquareStatus[][] solution;
      
    protected void importFiles(String name, String solutionType) {
        super.importFile(name, solutionType);
        NGRESReader ngresReader = new NGRESReader("test_input/" + name + "_" + solutionType + ".ngres");
        this.solution = ngresReader.read();
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
    
    protected Boolean test(Solver solver) {
        super.solveAndCountTime(solver);
        if (this.checkResultMatches()) {
            super.logTime();
            return true;
        }
        return false;
    }
}

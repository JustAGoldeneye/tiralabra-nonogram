package dao;

import java.util.Date;
import java.io.FileWriter;
import java.text.SimpleDateFormat;

/**
 *
 * @author eemeli
 */
public class TestLogWriter {
    String fileName;
    
    public TestLogWriter(String name, String solutionType) {
        this.fileName = "test_output/" + name + "_" + solutionType + ".log";
    }
    
    public void writeSolveTime(long solveTime) {
        try (FileWriter writer = new FileWriter(this.fileName, true)) {
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy=HH:mm:ss");
            writer.append(formatter.format(date) + " " + solveTime + "\n");
            writer.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

package dao;

import java.util.Date;
import java.io.FileWriter;
import java.text.SimpleDateFormat;

/**
 * Writes the given log info to the given log file.
 * @author eemeli
 */
public class TestLogWriter {
    String fileName;
    
    /**
     * Initializes the TestLogWriter.
     * @param name file name without file type ending
     * @param algorithmType the type of the algorithm this log info was from
     */
    public TestLogWriter(String name, String algorithmType) {
        this.fileName = "test_output/" + name + "_" + algorithmType + ".log";
    }
    
    /**
     * Writes a solve time report to the log file.
     * @param solveTime the time running the solving algorithm took
     */
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

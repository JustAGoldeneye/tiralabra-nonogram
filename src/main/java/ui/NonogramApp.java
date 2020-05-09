package ui;

import domain.structs.Chart;
import domain.structs.NumberRow;
import domain.structs.SquareStatus;
import domain.solvers.SimpleChartSolver;
import dao.CWDReader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

/**
 * Offers graphical user interface.
 * @author eemeli
 */
public class NonogramApp extends Application {
    static int squareSize = 16;
    static int borderWidth = 4 / 2;
    static Chart chart;
    static ImagePattern crossPattern = new ImagePattern(new Image("file:cross_pic_good.png"));

    /**
     * Controls JavaFX.
     * @param primaryStage 
     */
    @Override
    public void start(Stage primaryStage) {
        
        primaryStage.setTitle("Nonogram solver");
        
        HBox primaryLayout = new HBox();
        primaryLayout.setSpacing(10);
        
        VBox leftNumbers = new VBox();
        primaryLayout.getChildren().add(leftNumbers);
        VBox leftNumbersTitle = new VBox();
        VBox leftNumbersBody = new VBox();
        leftNumbers.getChildren().add(leftNumbersTitle);
        leftNumbersTitle.getChildren().add(new Label("Left"));
        leftNumbers.getChildren().add(leftNumbersBody);
        
        VBox topNumbers = new VBox();
        primaryLayout.getChildren().add(topNumbers);
        VBox topNumbersTitle = new VBox();
        VBox topNumbersBody = new VBox();
        topNumbers.getChildren().add(topNumbersTitle);
        topNumbersTitle.getChildren().add(new Label("Top"));
        topNumbers.getChildren().add(topNumbersBody);
        
        GridPane chartView = new GridPane();
        primaryLayout.getChildren().add(chartView);
        
        TextField fileNameField = new TextField();
        Label fileErrorLabel = new Label("");
        Button simpleSolve = new Button("Import and solve");
        Text timeText = new Text("Time: - ms");
        
        VBox controls = new VBox();
        primaryLayout.getChildren().add(controls);
        controls.setSpacing(10);
        controls.getChildren().add(fileNameField);
        controls.getChildren().add(fileErrorLabel);
        controls.getChildren().add(simpleSolve);
        controls.getChildren().add(timeText);
        
        simpleSolve.setOnAction((event) -> {
            if (importChartFromFile(fileNameField.getText())) {
                fileErrorLabel.setText("");
                
                leftNumbersBody.getChildren().clear();
                
                for (int i = 0; i < chart.verticalLength(); i++) {
                    leftNumbersBody.getChildren()
                            .add(new Label(chart
                                    .leftNumberRowIn(i).toString()));
                }
                topNumbersBody.getChildren().clear();
                for (int i = 0; i < chart.horizontalLength(); i++) {
                    topNumbersBody.getChildren()
                            .add(new Label(chart
                                    .topNumberRowIn(i).toString()));
                }
                
                timeText.setText("Time: " + solveChart() + " ms");
                
                chartView.getChildren().clear();
                for (int i = 0; i < chart.verticalLength(); i++) {
                    for (int j = 0; j < chart.horizontalLength(); j++) {
                        if (chart.lookSquareStatus(j, i) == SquareStatus.EMPTY) {
                            Pane background = new Pane();
                            background.setPrefSize(squareSize+borderWidth, squareSize+borderWidth);
                            chartView.add(background, j, i);
                            Rectangle emptySquare = new Rectangle(squareSize, squareSize, Color.LIGHTGRAY);
                            background.getChildren().add(emptySquare);
                        } else if (chart.lookSquareStatus(j, i) == SquareStatus.CROSS) {
                            Pane background = new Pane();
                            background.setPrefSize(squareSize+borderWidth, squareSize+borderWidth);    
                            chartView.add(background, j, i);
                            //Image crossImage = new Image("file:cross_pic_final.png");
                            Rectangle crossSquare = new Rectangle(squareSize, squareSize, crossPattern);
                            background.getChildren().add(crossSquare);
                        } else if (chart.lookSquareStatus(j, i) == SquareStatus.BLACK) {
                            Pane background = new Pane();
                            background.setPrefSize(squareSize+borderWidth, squareSize+borderWidth);
                            chartView.add(background, j, i);
                            Rectangle blackSquare = new Rectangle(squareSize, squareSize, Color.BLACK);
                            background.getChildren().add(blackSquare);
                        }
                    }
                    primaryLayout.autosize();
                }
            } else {
                fileErrorLabel.setText("Incorrect file path");
            }
        });
        
        Scene view = new Scene(primaryLayout);
        
        primaryStage.setScene(view);
        primaryStage.show();
    }
    
    /**
     * Handles importing Chart from a file.
     * @param filePath relative location of the file in the file system
     * @return whether the was able to be read and contained data or not
     */
    private static Boolean importChartFromFile(String filePath) {
        CWDReader reader = new CWDReader(filePath);
        chart = reader.read();
        return chart != null;
    }
    /**
     * Handles solving the chart and measuring the time solving takes.
     * @return time running solve() took
     */
    
    private static long solveChart() {
        SimpleChartSolver solver = new SimpleChartSolver(chart);
        long startTime = System.currentTimeMillis();
        solver.solve();
        long result = System.currentTimeMillis();
        result -= startTime;
        
        return result;
    }
}

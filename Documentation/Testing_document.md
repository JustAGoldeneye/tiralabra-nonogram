# Testing

## Unit testing

Unit testing was done using JUnit. Test coverage for the finished classes should be high. UI and Main packages are excluded from testing. Data structures have their own tests, but the testing for solvers is comined with performance testing.

## Performance testing

Performance testing, too, was done using JUnit. The tests read a CWD file as am input from test_input folder and write log files to test_output folder. The log file tells how long time solving the chart took. Running ./gradlew test in the main directory of the project will run all the tests.

There are two types of perfomance tests: The first type only measures the solving time and logs the information. The second type of tests, in addition to measuring time, test whether the solution is correct. The correct solution gets read from a NGRES file in test_input folder.

![alt text](https://github.com/JustAGoldeneye/tiralabra-nonogram/blob/master/Documentation/TestResults.png)

The tests were run using variety of different sizes puzzles from 14 squares to almost 6000. As I did not manage to finish the algorithm completely, the algorithm cannot solve nonograms of which solving involves any amount of guessing.

![alt text](https://github.com/JustAGoldeneye/tiralabra-nonogram/blob/master/Documentation/TestResultsC2.png)
The time it took to run the tests on different tries.

![alt text](https://github.com/JustAGoldeneye/tiralabra-nonogram/blob/master/Documentation/TestResultsC1.png)
The time it took to try to solve the puzzle in relation to the puzzle size.
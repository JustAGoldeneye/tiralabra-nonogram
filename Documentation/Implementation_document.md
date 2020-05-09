# Implemantation

## Structure

The program consists of five packages:

### domain.solvers

Contains the classes for solving aĺgorithms: RowSolver, SimpleChartSolver and unfinished UncertainChartSolver. SimpleChartSolver determines which rows should be checked in a given chart and determines when the puzzle has been solved. RowSolver does the most important part: It looks at the given row and fills the squares that can be filled with certainity.SimpleChartSolver saves newly found squares and crosses to the nonogram chart so the these results can be used again for row searching.

### domain.structs

The data structures for this program are quite specialized, as there was no need for general structures, like lists or stacks, in the project. NumberRow contains a list of numbers for one row or column of nonogram chart. Row used NumberRow and an array of SquareStatus to keep list of numbers and squres of one row. SquareStatus is enum with values for the states of the squares: black, cross and empty. Like Row, Chart uses numberRows, but instead of saving the data to Row objects, SquareStatuses are saved on their own two-dimensional chart to ease the uss.

### dao

dao contains the tools for handling files. CWDReader reads CWD files which store the puzzle data. NGRESReader reads puzzle solution files and is only used in JUnit tests. The same things applies to TestLogWriter which is used for writing test log files.

### ui

Contains the one class, NonogramApp, for handling the graphical user interface. The class has been made with JavaFX.

## main

Contains main class, used for starting the program and for manual testing.

## Performance

```
solveNonogram:
loop until solved
    checkChart:
    loop once for every row in chart
        checkRow:
        loop once for every possible starting position for row
            checkRowFromThisStartingPosition:
                loop once for every square in row after start:
                    checkSquare
```

This is a very simplified version of the program by its most significant parts for determining the performance. It may at first glance look like O(n^4) time algorithm, but the lowest two lowest loops take a less time every time they are run, so they are closer to O(logn). This would mean a O(n) time of O(2logn*n^2).

## Problems

I did not manage to finish class UncertainChartSolver in time. It would have controlled the SimpleChartSolver to allow it to make guesses in uncertain situations. Due to this, the program can not completely solve puzzles requiring some guessing.

While running the final performance tests I noticed that the algorithm does not seem to solve puzzle webpbn-00001 correctly. I had no time to investigate this further, so I do not have an idea what causes it.

## Sources

The puzzles webpbn##### and meow are from https://webpbn.com/survey/.
The puzzle picross_s4_p002 is from a game Picross S4 by Jupiter.
# Project Definition (14th March 2020)

The goal of the project is to make a program that can solve nonograms.

The core of the program is a nonogram row checker that checks whether or not there exists blocks that would be colored in every solution. If the checker finds blocks like this, it marks them as black or empty. For example, row [5 2 | _0______0_] would become [5 2 | _0000_X_0_] (_ unknown, 0 black, X empty). (In this text, instead of writing "row or column", I will be using the term "nonogram row" instead.)

If the row checker has a section that (supposedly) has only one group of blocks, the checker can determinne with simple math that wihch blocks of the section have to be black and which ahve to be empty.

The nonogram row checker also works recursively being able to test parts of rows divided by definitely or supposedly empty blocks as their own sub rows.

If there is known to be several groups of blocks on one row, the program will be looking through the different possibilities of placement of the groups related to the others by branching. The lowest branches will only have sub-rows with only one group of blocks. The lowest branches will write their information on a shared results row filled with nulls. When the result row block is written for the first time it will be marked as a black or empty. If a black or white row gets written again by an opposite state block, the block gets marked as uncertain state.

The program has to loop through every row and column of a nonogram table. The completed nonogram rows will be marked as completed so there is no need for the program to count the amount of black blocks every time it passed by.

To have the checking to be more effective, heuristics will be implemented. These may include checking a row after a column and a column after a row, and checking other nonogram rows near the checked nonogram row if new information was discovered.

## Algorithms

At this point it seems like I will not be implementing any specific algorithms, as the solutions will mostly be recursion, heuristics, and brute forcing.

## Data Stuctures

The data structures need will mostly be basic things like basic data types, enumerators and tables. Nonogram row checking heuristics may will need some type of list to remember the latest rows checked.

## Input

The program will be getting a nonogram table as an input. The input will be an human readable file, either created in a text editor or given to the application table creation UI. The program will read the data from the file to nonogram table object consisting of the two tables showing amounts of blocks and columns on the rows and a table that saves status for every block. The program will give its output in the same form as it gets the input. In theory, there is no limit for nonogram size, but solving large nonograms may take a very long time.

## Time requirements

Checking a nonogram row requires O((n/m)^m) amount of time where n is the length of row and m is the amount of block groups on the row. For the whole table the time requirement is O((n/m)^m(n^3)), as all rows may have to be checked for every block in table. The avarage time requirement will be a lot smaller, due to use of heuristics. Still, the algorithm will not be very good at solving very large nonograms.

## Space requirements

Recursion will be requiring the space O(n) amount where n is the length of the row as there cannot be more than n block groups on the row which makes it the maximum recursion depth.

## Sources

The current version has no sources used.
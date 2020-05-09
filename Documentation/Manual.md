# Manual

## Installation

1. Download the zip file from the newest release.
2. Extract the zip file to a direcotry of your choosing.
3. You should get a directory called ngsolver. Open it in terminal.
4. To start the program, run "java -jar tiralabra-nonogram.jar".

Directory ngsolver contains the program and the CWD files storing nonograms. You can use the alredy existing files or copy your own ones to ngsolver directory.

## Using the program

To solve and import a CWD file in ngsolver directory just write the name of the file, including file ending to the text field and click "Import and solve". You can also import files in sub-directories of ngsolver by writing "yoursubfoldernamehere/yourfile.cwd".

## Testing the program

Running tests is not possible with the jar file, so it is necessary to clone the repository to your computer. When in the main direcotry of the cloned repository, you can run the tests with "./gradlew test". You can find the test results in "test_output" directory. The test results are stored in simple log files. The latest result can be seen in at the last row: After date and time, the time the test took is shown in milliseconds. The files needed for tests are in direcotory "test_input". Just adding new cwd files will not add new tests, the test have to speciefied in testing classes.
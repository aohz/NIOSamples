package com.gft.filesystem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

/*
Java 7 allows you to directly open files for processing with buffered readers and writers
or (for compatibility with older Java I/O code) input and output streams. The following 
snippet demonstrates how open a file (using the Files.newBufferedReader
method) for reading lines in Java 7.
*/
public class FileDemo {
    public static void main(String[] args) {
        
    }
    
    /*
    Opening a file for reading
    */
    static void openFile() throws IOException{
        Path logFile = Paths.get("/tmp/app.log");
        try (BufferedReader reader = Files.newBufferedReader(logFile, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
               //Do something
            }
        }
    }
    
    /*
    Opening a file for writing
    */
    static void writeFile() throws IOException{
        Path logFile = Paths.get("/tmp/app.log");
        /*
        Note the use of the StandardOpenOption.WRITE , which is one of several varargs Open-
        Option options you can add. This ensures that the file has the correct permissions for
        writing to. Other commonly used open options include READ and APPEND .
        */
        try (BufferedWriter writer = Files.newBufferedWriter(logFile, StandardCharsets.UTF_8, StandardOpenOption.WRITE)) {
            writer.write("Hello World!");
        }
    }
    
    /*
    A common tasks of reading all of the lines in a file and reading all of the bytes in a file.
    */
    static void readAllFile() throws IOException{
        Path logFile = Paths.get("/tmp/app.log");
        List<String> lines = Files.readAllLines(logFile, StandardCharsets.UTF_8);
        byte[] bytes = Files.readAllBytes(logFile);
    }
}

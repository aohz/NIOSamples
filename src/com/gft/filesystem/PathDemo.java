package com.gft.filesystem;

import java.nio.file.Path;
import java.nio.file.Paths;


public class PathDemo {
    public static void main(String[] args) {
        //Create absolute Path
        Path listing = Paths.get("/home/administrator/tests");
        //Get filename
        System.out.println("File Name [" + listing.getFileName() + "]");
        //Get number of elements
        System.out.println("Number of Name Elements in the Path [" + listing.getNameCount() + "]");
        //Get Path information
        System.out.println("Parent Path [" + listing.getParent() + "]");
        System.out.println("Root of Path [" + listing.getRoot() + "]");
        System.out.println("Subpath from Root, 2 elements deep [" + listing.subpath(0, 2) + "]");

    }
}
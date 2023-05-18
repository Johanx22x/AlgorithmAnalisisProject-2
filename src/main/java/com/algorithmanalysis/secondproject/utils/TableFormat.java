package com.algorithmanalysis.secondproject.utils;

import java.util.ArrayList;
import com.algorithmanalysis.secondproject.models.*;

/**
 * TableFormat
 *
 * This class is used to generate a table of the data in the form of a string.
 * It takes in an ArrayList of Alleles, and returns a string that is formatted
 *
 * @author Johan Rodriguez
 * @version 1.0
 */
public class TableFormat {

    /**
     * getTable
     *
     * This method takes in an ArrayList of Alleles, and returns a string that is
     * formatted as a table.
     *
     * @param alleles ArrayList of Alleles 
     * @return String
     */
    public static String getTable(ArrayList<Allele> alleles) {
        String table = "";

        // Define headers
        String profHeader = "Professor";
        ArrayList<String> courseHeaders = new ArrayList<String>();
        for (Allele allele : alleles) {
            if (!courseHeaders.contains(allele.getCourse().getName())) {
                courseHeaders.add(allele.getCourse().getName());
            }
        }

        // Get data
        int maxProfNameLength = 0;
        ArrayList<String> profs = new ArrayList<String>();
        for (Allele allele : alleles) {
            if (!profs.contains(allele.getProfessor().getName())) {
                profs.add(allele.getProfessor().getName());
                if (allele.getProfessor().getName().length() > maxProfNameLength) {
                    maxProfNameLength = allele.getProfessor().getName().length();
                }
            } 
        }

        // Get grades, and put them in a 2D array
        ArrayList<ArrayList<Integer>> data = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < profs.size(); i++) {
            ArrayList<Integer> grades = new ArrayList<Integer>();
            for (int j = 0; j < courseHeaders.size(); j++) {
                grades.add(0);
            }
            data.add(grades);
        }
        for (Allele allele : alleles) {
            int profIndex = profs.indexOf(allele.getProfessor().getName());
            int courseIndex = courseHeaders.indexOf(allele.getCourse().getName());
            data.get(profIndex).set(courseIndex, allele.getGrade());
        }

        if (maxProfNameLength < profHeader.length()) {
            maxProfNameLength = profHeader.length();
        }

        // Get max length of course headers
        int maxCourseHeaderLength = 0;
        for (String courseHeader : courseHeaders) {
            if (courseHeader.length() > maxCourseHeaderLength) {
                maxCourseHeaderLength = courseHeader.length();
            }
        }

        // Print table 
        // Print top border
        table += ("-");
        for (int i = 0; i < maxProfNameLength + 2; i++) {
            table += ("-");
        }
        for (int i = 0; i < courseHeaders.size(); i++) {
            for (int j = 0; j < maxCourseHeaderLength + 2; j++) {
                table += ("-");
            }
        }
        for (int i = 0; i < courseHeaders.size(); i++) {
            table += ("-");
        }
        table += ("-\n");

        // Print headers 
        table += ("|");
        table += (" " + profHeader);
        for (int i = 0; i < maxProfNameLength - profHeader.length() + 1; i++) {
            table += (" ");
        }
        for (int i = 0; i < courseHeaders.size(); i++) {
            table += ("|");
            table += (" " + courseHeaders.get(i));
            for (int j = 0; j < maxCourseHeaderLength - courseHeaders.get(i).length() + 1; j++) {
                table += (" ");
            }
        }
        table += ("|\n");

        // Print middle border
        table += ("=");
        for (int i = 0; i < maxProfNameLength + 2; i++) {
            table += ("=");
        }
        for (int i = 0; i < courseHeaders.size(); i++) {
            for (int j = 0; j < maxCourseHeaderLength + 2; j++) {
                table += ("=");
            }
        }
        for (int i = 0; i < courseHeaders.size(); i++) {
            table += ("=");
        }
        table += ("=\n");

        // Print data
        for (int i = 0; i < data.size(); i++) {
            table += ("|");
            table += (" " + profs.get(i));
            for (int j = 0; j < maxProfNameLength - profs.get(i).length() + 1; j++) {
                table += (" ");
            }
            for (int j = 0; j < data.get(i).size(); j++) {
                table += ("|");
                table += (" " + data.get(i).get(j));
                for (int k = 0; k < maxCourseHeaderLength - data.get(i).get(j).toString().length() + 1; k++) {
                    table += (" ");
                }
            }
            table += ("|\n");
        }

        // Print bottom border
        table += ("-");
        for (int i = 0; i < maxProfNameLength + 2; i++) {
            table += ("-");
        }
        for (int i = 0; i < courseHeaders.size(); i++) {
            for (int j = 0; j < maxCourseHeaderLength + 2; j++) {
                table += ("-");
            }
        }
        for (int i = 0; i < courseHeaders.size(); i++) {
            table += ("-");
        }
        table += ("-\n");

        return table;
    }

    /**
     * Returns a String representation of the table in CSV format.
     *
     * @param alleles ArrayList of Alleles 
     * @return String
     */
    public static String getTableCSV(ArrayList<Allele> alleles) {
        String table = "";

        // Write headers 
        table += ("Professor,");
        ArrayList<String> courseHeaders = new ArrayList<String>();
        for (Allele allele : alleles) {
            if (!courseHeaders.contains(allele.getCourse().getName())) {
                courseHeaders.add(allele.getCourse().getName());
            }
        }
        for (int i = 0; i < courseHeaders.size(); i++) {
            table += (courseHeaders.get(i));
            if (i != courseHeaders.size() - 1) {
                table += (",");
            }
        }
        table += ("\n");

        // Write data
        ArrayList<String> profs = new ArrayList<String>();
        for (Allele allele : alleles) {
            if (!profs.contains(allele.getProfessor().getName())) {
                profs.add(allele.getProfessor().getName());
            } 
        }
        for (int i = 0; i < profs.size(); i++) {
            table += (profs.get(i));
            for (int j = 0; j < courseHeaders.size(); j++) {
                table += (",");
                for (Allele allele : alleles) {
                    if (allele.getProfessor().getName().equals(profs.get(i)) && allele.getCourse().getName().equals(courseHeaders.get(j))) {
                        table += (allele.getGrade());
                    }
                }
            }
            table += ("\n");
        }

        return table;
    }
}

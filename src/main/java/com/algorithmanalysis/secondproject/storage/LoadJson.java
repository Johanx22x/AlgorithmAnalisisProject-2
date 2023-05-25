package com.algorithmanalysis.secondproject.storage;

import com.algorithmanalysis.secondproject.models.*;
import java.util.ArrayList;
import java.io.FileReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

/**
 * LoadJson
 *
 * This class is responsible for loading the json files and parsing them into
 * Allele objects
 *
 * @author Johan Rodriguez
 * @version 1.0
 */
public class LoadJson {
    private ArrayList<ArrayList<Allele>> alleles = new ArrayList<ArrayList<Allele>>(); // Array of alleles for each file
    private ArrayList<Integer> population = new ArrayList<Integer>(); // Array of population for each file
    private ArrayList<Integer> totalOfCourses = new ArrayList<Integer>(); // Array of total of courses for each file
    private ArrayList<Integer> totalOfProfessors = new ArrayList<Integer>(); // Array of total of professors for each file

    /**
     * Constructor
     *
     * Load the json files and parse them into Allele objects
     */
    public LoadJson() {
        loadJson();
    }

    /** 
     * Get the alleles array 
     *
     * @return ArrayList<ArrayList<Allele>> The alleles array
     */
    public ArrayList<ArrayList<Allele>> getAlleles() {
        return alleles;
    }

    /**
     * Get the total of courses array 
     *
     * @return ArrayList<Integer> The total of courses array
     */
    public ArrayList<Integer> getTotalOfCourses() {
        return totalOfCourses;
    }

    /**
     * Get the total of professors array 
     *
     * @return ArrayList<Integer> The total of professors array
     */
    public ArrayList<Integer> getTotalOfProfessors() {
        return totalOfProfessors;
    }

    /** 
     * Get the population array 
     *
     * @return ArrayList<Integer> The population array
     */
    public ArrayList<Integer> getPopulation() {
        return population;
    }

    /**
     * Load the json files and parse them into Allele objects
     */
    private void loadJson() {
        for (int i = 0; i <= 5; i++) {
            String file = "data/data" + i + ".json";
            // Create an array of alleles for each files and add it to the alleles array 
            // Get the professor name and grades from the json file and add it to a new Allele object
            // Add the new Allele object to the alleles array 
            try {
                Object obj = new JSONParser().parse(new FileReader(file));
                JSONObject jo = (JSONObject) obj;
                JSONArray professors = (JSONArray) jo.get("professors");
                int totalOfProfessors = professors.size();
                this.totalOfProfessors.add(totalOfProfessors);
                ArrayList<Allele> allelesArray = new ArrayList<Allele>();
                population.add(((Long) jo.get("population")).intValue());
                for (int j = 0; j < professors.size(); j++) {
                    JSONObject professor = (JSONObject) professors.get(j);
                    String name = (String) professor.get("name");
                    JSONArray grades = (JSONArray) professor.get("grades");
                    for (int k = 0; k < grades.size(); k++) {
                        Professor professorObj = new Professor(name);
                        k = k + 1;
                        Course courseObj = new Course("course" + k);
                        k = k - 1;
                        int grade = ((Long) grades.get(k)).intValue();
                        Allele allele = new Allele(professorObj, courseObj, grade);
                        allelesArray.add(allele);
                    }
                    totalOfCourses.add(grades.size());
                }
                alleles.add(allelesArray);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

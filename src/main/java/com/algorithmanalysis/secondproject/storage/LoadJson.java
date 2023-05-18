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
    ArrayList<ArrayList<Allele>> alleles = new ArrayList<ArrayList<Allele>>(); // Array of alleles for each file
    ArrayList<Integer> population = new ArrayList<Integer>(); // Array of population for each file

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
                ArrayList<Allele> allelesArray = new ArrayList<Allele>();
                population.add(((Long) jo.get("population")).intValue());
                for (int j = 0; j < professors.size(); j++) {
                    JSONObject professor = (JSONObject) professors.get(j);
                    String name = (String) professor.get("name");
                    JSONArray grades = (JSONArray) professor.get("grades");
                    for (int k = 0; k < grades.size(); k++) {
                        Professor professorObj = new Professor(name);
                        Course courseObj = new Course("course" + k);
                        int grade = ((Long) grades.get(k)).intValue();
                        Allele allele = new Allele(professorObj, courseObj, grade);
                        allelesArray.add(allele);
                    }
                }
                alleles.add(allelesArray);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

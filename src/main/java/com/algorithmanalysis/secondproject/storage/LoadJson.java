package com.algorithmanalysis.secondproject.storage;

import com.algorithmanalysis.secondproject.models.*;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * LoadJson
 *
 * This class is responsible for loading the json files and parsing them into
 * Allele objects
 *
 * @author Johan Rodriguez
 * @author Aaron González
 * @version 2.0
 */
public class LoadJson {

    /**
     * Load the json files and parse them into Allele objects
     */
    public static ParsedData fromFile(String filename) throws FileNotFoundException, ParseException, IOException {

        ParsedData result = new ParsedData();

        JSONObject obj = (JSONObject) new JSONParser().parse(new FileReader(filename));
        JSONArray professors = (JSONArray) obj.get("professors");

        for (int i = 0; i < professors.size(); i++) {
            JSONObject professor = (JSONObject) professors.get(i);

            String name = (String) professor.get("name");
            JSONArray grades = (JSONArray) professor.get("grades");

            for (int j = 0; j < grades.size(); j++) {
                Professor prof = new Professor(name);
                Course cour = new Course("Course" + j);
                int grade = ((Long) grades.get(j)).intValue();
                Allele all = new Allele(prof, cour, grade);
                result.alleles.add(all);
            }
        }

        result.courses = ((Long) obj.get("courses")).intValue();
        result.population = ((Long) obj.get("population")).intValue();

        return result;
    }

    public static class ParsedData {
        public ArrayList<Allele> alleles = new ArrayList<>();
        public int courses;
        public int population;

        @Override
        public String toString() {
            return String.format("ParsedData {\nalleles=%s,\ncourses=%d,\npopulation=%d\n}", this.alleles, this.courses,
                    this.population);
        }
    }
}

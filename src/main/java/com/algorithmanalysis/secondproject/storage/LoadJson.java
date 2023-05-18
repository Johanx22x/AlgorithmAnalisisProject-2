/**
{
  "professors": [
    {
      "name": "Jo Bailey",
      "grades": [
        10,
        0,
        6,
        9,
        6,
        3,
        7,
        6,
        8
      ]
    },
    {
      "name": "Joseph Thomason",
      "grades": [
        9,
        10,
        6,
        4,
        9,
        7,
        7,
        0,
        1
      ]
    },
    {
      "name": "Richard Shea",
      "grades": [
        10,
        7,
        2,
        7,
        6,
        5,
        -1,
        9,
        8
      ]
    }
  ],
  "courses": 9,
  "population": 3
}
 */
package com.algorithmanalysis.secondproject.storage;


import com.algorithmanalysis.secondproject.models.*;
import java.util.ArrayList;
import java.io.FileReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class LoadJson {
    public ArrayList<ArrayList<Allele>> alleles = new ArrayList<ArrayList<Allele>>();

    public ArrayList<ArrayList<Allele>> getAlleles() {
        return alleles;
    }

    private void loadJson() {
        for (int i = 0; i <= 5; i++) {
            String file = "data/data" + i + ".json";
            // Load Json file like the example above
            // Create Professors and Courses
            // Next create an Allele object with the Professors, Courses and grades
            // Add the Allele object to the alleles ArrayList
            JSONParser parser = new JSONParser();
            try {
                Object obj = parser.parse(new FileReader(file));
                JSONObject jsonObject = (JSONObject) obj;
                JSONArray professors = (JSONArray) jsonObject.get("professors");
                int courses = Integer.parseInt(jsonObject.get("courses").toString());
                int population = Integer.parseInt(jsonObject.get("population").toString());
                for (int j = 0; j < professors.size(); j++) {
                    JSONObject professor = (JSONObject) professors.get(j);
                    String name = professor.get("name").toString();
                    JSONArray grades = (JSONArray) professor.get("grades");
                    ArrayList<Integer> gradesList = new ArrayList<Integer>();
                    for (int k = 0; k < grades.size(); k++) {
                        gradesList.add(Integer.parseInt(grades.get(k).toString()));
                    }
                    Professor professorObj = new Professor(name);
                    Course course = new Course(courses);
                    Allele allele = new Allele(professorObj, course, gradesList);
                    alleles.add(allele);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
}

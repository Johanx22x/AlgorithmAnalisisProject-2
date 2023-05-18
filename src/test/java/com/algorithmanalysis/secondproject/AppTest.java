package com.algorithmanalysis.secondproject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.algorithmanalysis.secondproject.models.Allele;
import com.algorithmanalysis.secondproject.models.Course;
import com.algorithmanalysis.secondproject.models.Professor;
import com.algorithmanalysis.secondproject.storage.DataStore;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest
        extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() throws FileNotFoundException, IOException, ClassNotFoundException {

        ArrayList<Allele> alelos = new ArrayList<>();
        alelos.add(new Allele(new Professor("Nigger"), new Course("Semen"), 90));
        DataStore.dumpToFile("Semen", alelos);

        ArrayList<Allele> alelos2 = DataStore.loadFromFile("Semen");
        System.out.println(alelos2.toString());
        assertTrue(true);
    }
}

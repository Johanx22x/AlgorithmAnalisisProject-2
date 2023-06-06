package com.algorithmanalysis.secondproject;

import java.io.FileNotFoundException;
import java.io.IOException;

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

        // ArrayList<Allele> alelos = new ArrayList<>();
        // alelos.add(new Allele(new Professor("Damn"), new Course("Prueba"), 90));
        // DataStore.dumpToFile("Damn", alelos);

        // ArrayList<Allele> alelos2 = DataStore.loadFromFile("Prueba");
        // System.out.println(alelos2.toString());
        // assertTrue(true);

    }
}

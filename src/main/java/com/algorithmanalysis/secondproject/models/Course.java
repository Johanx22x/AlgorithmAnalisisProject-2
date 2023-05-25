package com.algorithmanalysis.secondproject.models;

/**
 * Course class
 *
 * This class is used to represent a course in the algorithm.
 * 
 * @author Johan Rodríguez
 * @author Aaron González
 * @version 1.0
 */
public class Course {
    private String name; // The name of the course

    /**
     * Constructor
     *
     * @param name The name of the course
     */
    public Course(String name) {
        this.name = name;
    }

    /**
     * Get the name of the course
     *
     * @return The name of the course
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set the name of the course
     *
     * @param name The new name of the course
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * toString method
     *
     * @return The name of the course
     */
    public String toString() {
        return this.name;
    }
}

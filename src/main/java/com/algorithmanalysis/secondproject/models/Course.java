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
    private int index; // The index of the course

    /**
     * Constructor
     *
     * @param name The name of the course
     */
    public Course(String name) {
        this.name = name;
        // The index is the last character of the name 
        this.index = Integer.parseInt(name.substring(name.length() - 1));
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
     * Get the course index 
     *
     * @return The course index
     */
    public int getIndex() {
        return this.index;
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

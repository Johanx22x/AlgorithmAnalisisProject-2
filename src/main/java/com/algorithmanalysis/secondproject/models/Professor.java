package com.algorithmanalysis.secondproject.models;

import java.io.Serializable;

/**
 * Professor class
 *
 * This class is used to represent a professor in the algorithm.
 * @author Johan Rodríguez
 * @author Aaron González
 */
public class Professor implements Serializable {
    private String name; // The name of the professor

    /**
     * Constructor
     *
     * @param name The name of the professor
     */
    public Professor(String name) {
        this.name = name;
    }

    /**
     * Get the name of the professor 
     *
     * @return The name of the professor
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the professor
     *
     * @param newName The new name of the professor
     */
    public void setName(String newName) {
        this.name = newName;
    }

    /**
     * toString method
     *
     * @return The name of the professor
     */
    public String toString() {
        return this.name;
    }
}

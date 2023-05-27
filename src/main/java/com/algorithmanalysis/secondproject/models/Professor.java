package com.algorithmanalysis.secondproject.models;

/**
 * Professor class
 *
 * This class is used to represent a professor in the algorithm.
 * @author Johan Rodríguez
 * @author Aaron González
 */
public class Professor {
    private String name; // The name of the professor
    private int index; // The index of the professor in the array

    /**
     * Constructor
     *
     * @param name The name of the professor
     */
    public Professor(String name) {
        this.name = name;
        // Get the index of the professor
        this.index = Integer.parseInt(name.substring(name.length() - 1));
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
     * Get the index of the professor
     *
     * @return The index of the professor
     */
    public int getIndex() {
        return index;
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

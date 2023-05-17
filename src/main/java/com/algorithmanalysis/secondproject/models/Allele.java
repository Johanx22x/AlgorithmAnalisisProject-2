package com.algorithmanalysis.secondproject.models;

/**
 * Allele
 *
 * This class represents an allele in the genetic algorithm.
 * An allele is a pair of a professor, a course and a grade.
 *
 * @author: Johan Rodriguez
 * @version: 1.0
 */
public class Allele {
    private Professor professor; // The professor
    private Course course; // The course
    private int grade; // Professor's grade for the course

    /**
     * Constructor
     *
     * @param professor The professor
     * @param course The course
     * @param grade The professor's grade for the course
     */
    public Allele(Professor professor, Course course, int grade) {
        this.professor = professor;
        this.course = course;
        this.grade = grade;
    }

    /**
     * Get the Allele 
     *
     * @return The Allele
     */
    public Allele getAllele() {
        return this;
    }

    /**
     * Set the Allele
     *
     * @param allele The new Allele
     */
    public void setAllele(Allele allele) {
        this.professor = allele.getProfessor();
        this.course = allele.getCourse();
        this.grade = allele.getGrade();
    }

    /**
     * Get the professor 
     * 
     * @return The professor
     */
    public Professor getProfessor() {
        return this.professor;
    }

    /**
     * Set the professor
     *
     * @param professor The new professor
     */
    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    /**
     * Get the course
     *
     * @return The course
     */
    public Course getCourse() {
        return this.course;
    }

    /**
     * Set the course
     *
     * @param course The new course
     */
    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     * Get the grade
     *
     * @return The grade
     */
    public int getGrade() {
        return this.grade;
    }

    /**
     * Set the grade
     *
     * @param grade The new grade
     */
    public void setGrade(int grade) {
        this.grade = grade;
    }

    /**
     * Get the string representation of the Allele
     *
     * @return The string representation of the Allele
     */
    public String toString() {
        return "Professor: " + this.professor.getName() + " Course: " + this.course.getName() + " Grade: " + this.grade;
    }
}

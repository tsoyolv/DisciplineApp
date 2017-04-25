package com.olts.discipline.model;

/**
 * o.tsoy
 * 25.04.2017
 */

public class Habit {


    private long id;

    private String name;
    private int difficulty;

    @Override
    public String toString() {
        return "Habit{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", difficulty=" + difficulty +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
}

package com.example.alias;

import java.io.Serializable;

public class Team implements Serializable {
    protected String name;
    protected int points;

    public Team(String nextTeam){
    }

    public Team(String name, int points) {
        this.name = name;
        this.points = points;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setPoints(int points){
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public String toString() {
        return "Team{" +
                "name='" + name + '\'' +
                ", points=" + points +
                '}';
    }
}
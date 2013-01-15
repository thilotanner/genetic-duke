package models;

import play.db.jpa.Model;

import javax.persistence.Entity;

@Entity
public class Evolution extends Model
{
    public Integer numberOfConfigurations;

    public Integer numberOfGenerations;

    public String fitnessFunction;

    public String selectionAlgorithm;
}

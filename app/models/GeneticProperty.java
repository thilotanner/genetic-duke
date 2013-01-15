package models;

import play.db.jpa.Model;

import javax.persistence.ManyToOne;

public class GeneticProperty extends Model
{
    @ManyToOne
    public GeneticConfiguration geneticConfiguration;

    public String name;

    public String comparator;

    public Double lowProbability;

    public Double highProbability;
}

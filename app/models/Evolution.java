package models;

import genetic.Population;
import play.db.jpa.Model;

import javax.persistence.OneToMany;
import java.util.List;

public class Evolution extends Model
{
    public Integer numberOfConfigurations;

    public Integer numberOfGenerations;

    @OneToMany
    public List<Population> populations;
}

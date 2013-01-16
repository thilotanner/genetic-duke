package models;

import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import java.util.List;

@Entity
public class Evolution extends Model
{
    public Integer numberOfConfigurations;

    public Integer numberOfGenerations;

    public String fitnessFunction;

    public String selectionAlgorithm;

    @OneToMany(mappedBy = "evolution")
    @OrderColumn(name = "number")
    public List<Generation> generations;
}

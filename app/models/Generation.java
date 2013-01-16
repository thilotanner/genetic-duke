package models;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Generation extends Model
{
    @ManyToOne
    public Evolution evolution;

    public Integer number;

    @OneToMany(mappedBy = "generation")
    public List<GeneticConfiguration> geneticConfigurations;
}

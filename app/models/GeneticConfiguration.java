package models;

import play.db.jpa.Model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class GeneticConfiguration extends Model
{
    @ManyToOne
    public Generation generation;

    public Double threshold;

    @OneToMany(mappedBy = "geneticConfiguration", cascade = CascadeType.PERSIST)
    public List<GeneticProperty> geneticProperties = new ArrayList<GeneticProperty>();

    public Integer totalRecords = 0;

    public Integer truePositives = 0;

    public Integer falsePositives = 0;

    public Integer falseNegatives = 0;

    public Integer trueNegatives = 0;

    public double fitness;

    public GeneticConfiguration duplicate()
    {
        GeneticConfiguration configuration = new GeneticConfiguration();
        configuration.threshold = threshold;

        List<GeneticProperty> geneticProperties = new ArrayList<GeneticProperty>();
        for(GeneticProperty property : geneticProperties) {
            GeneticProperty duplicateProperty = new GeneticProperty();
            duplicateProperty.geneticConfiguration = configuration;
            duplicateProperty.name = property.name;
            duplicateProperty.comparator = property.comparator;
            duplicateProperty.lowProbability = property.lowProbability;
            duplicateProperty.highProbability = property.highProbability;
            geneticProperties.add(duplicateProperty);
        }
        configuration.geneticProperties = geneticProperties;

        return configuration;
    }
}

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

    @OneToMany(mappedBy = "geneticConfiguration", cascade = CascadeType.ALL)
    public List<GeneticProperty> geneticProperties = new ArrayList<GeneticProperty>();

    public int totalRecords = 0;

    public int truePositives = 0;

    public int falsePositives = 0;

    public int falseNegatives = 0;

    public int trueNegatives = 0;

    public double fitness = 0.0d;

    public boolean failed = false;

    public GeneticConfiguration duplicate()
    {
        GeneticConfiguration configuration = new GeneticConfiguration();
        configuration.threshold = threshold;

        List<GeneticProperty> duplicateProperties = new ArrayList<GeneticProperty>();
        for(GeneticProperty property : geneticProperties) {
            GeneticProperty duplicateProperty = new GeneticProperty();
            duplicateProperty.geneticConfiguration = configuration;
            duplicateProperty.name = property.name;
            duplicateProperty.comparator = property.comparator;
            duplicateProperty.lowProbability = property.lowProbability;
            duplicateProperty.highProbability = property.highProbability;
            duplicateProperties.add(duplicateProperty);
        }
        configuration.geneticProperties = duplicateProperties;

        return configuration;
    }
}

package models;

import play.db.jpa.Model;

import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

public class GeneticConfiguration extends Model
{
    public Double threshold;

    @OneToMany(mappedBy = "geneticConfiguration")
    public List<GeneticProperty> geneticProperties = new ArrayList<GeneticProperty>();

    public Integer totalRecords = 0;

    public Integer truePositives = 0;

    public Integer falsePositives = 0;

    public Integer falseNegatives = 0;

    public Integer trueNegatives = 0;

    public double getPrecision() {
        if(truePositives == 0) {
            return 0d;
        }

        return ((double) truePositives) / ((double) truePositives + falsePositives);
    }

    public double getRecall() {
        if(truePositives == 0) {
            return 0d;
        }

        return ((double) truePositives) / ((double) truePositives + falseNegatives);
    }
}

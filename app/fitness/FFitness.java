package fitness;

import models.GeneticConfiguration;

public class FFitness implements FitnessFunction
{
    @Override
    public double calculateFitness(GeneticConfiguration geneticConfiguration)
    {
        return getFMeasure(geneticConfiguration);
    }

    public double getPrecision(GeneticConfiguration geneticConfiguration) {
        if(geneticConfiguration.truePositives == 0) {
            return 0d;
        }

        return ((double) geneticConfiguration.truePositives) / ((double) geneticConfiguration.truePositives + geneticConfiguration.falsePositives);
    }

    public double getRecall(GeneticConfiguration geneticConfiguration) {
        if(geneticConfiguration.truePositives == 0) {
            return 0d;
        }

        return ((double) geneticConfiguration.truePositives) / ((double) geneticConfiguration.truePositives + geneticConfiguration.falseNegatives);
    }

    public double getFMeasure(GeneticConfiguration geneticConfiguration) {
        double precision = getPrecision(geneticConfiguration);
        double recall = getRecall(geneticConfiguration);

        if(precision == 0d || recall == 0d) {
            return 0d;
        }

        return 2d * ((precision * recall) / (precision + recall));
    }
}

package fitness;

import models.GeneticConfiguration;

public class FFitness implements FitnessFunction
{
    @Override
    public double calculateFitness(GeneticConfiguration configuration)
    {
        return getFMeasure(configuration);
    }

    public double getPrecision(GeneticConfiguration configuration) {
        if(configuration.truePositives == 0) {
            return 0d;
        }

        return ((double) configuration.truePositives) / ((double) configuration.truePositives + configuration.falsePositives);
    }

    public double getRecall(GeneticConfiguration configuration) {
        if(configuration.truePositives == 0) {
            return 0d;
        }

        return ((double) configuration.truePositives) / ((double) configuration.truePositives + configuration.falseNegatives);
    }

    public double getFMeasure(GeneticConfiguration configuration) {
        double precision = getPrecision(configuration);
        double recall = getRecall(configuration);

        if(precision == 0d || recall == 0d) {
            return 0d;
        }

        return 2d * ((precision * recall) / (precision + recall));
    }
}

package fitness;

import models.GeneticConfiguration;

public class GFitness implements FitnessFunction
{
    @Override
    public double calculateFitness(GeneticConfiguration configuration)
    {
        return Math.sqrt(getSensitivity(configuration) * getSpecificity(configuration));
    }

    public double getSensitivity(GeneticConfiguration configuration)
    {
        return ((double) configuration.truePositives) / ((double) configuration.truePositives + configuration.falseNegatives);
    }

    public double getSpecificity(GeneticConfiguration configuration)
    {
        return ((double) configuration.trueNegatives) / ((double) configuration.trueNegatives + configuration.falsePositives);
    }
}

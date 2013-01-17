package fitness;

import models.GeneticConfiguration;

public class MCCFitness implements FitnessFunction
{
    @Override
    public double calculateFitness(GeneticConfiguration configuration)
    {
        double tp = configuration.truePositives;
        double tn = configuration.trueNegatives;
        double fp = configuration.falsePositives;
        double fn = configuration.falseNegatives;

        return (tp * tn - fp * fn) / (Math.sqrt((tp + fp) * (tp + fn) * (tn + fp) * (tn + fn)));
    }
}

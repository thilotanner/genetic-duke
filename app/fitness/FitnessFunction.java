package fitness;

import models.GeneticConfiguration;

public interface FitnessFunction
{
    public double calculateFitness(GeneticConfiguration geneticConfiguration);
}

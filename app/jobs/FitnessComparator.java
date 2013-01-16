package jobs;

import models.GeneticConfiguration;

import java.util.Comparator;

public class FitnessComparator implements Comparator<GeneticConfiguration>
{
    @Override
    public int compare(GeneticConfiguration geneticConfiguration1,
                       GeneticConfiguration geneticConfiguration2)
    {
        Double f1 = geneticConfiguration1.fitness;
        if (f1 == null) {
            f1 = 0.0d;
        }
        Double f2 = geneticConfiguration2.fitness;
        if (f2 == null) {
            f2 = 0.0d;
        }

        return -1 * f1.compareTo(f2);
    }
}

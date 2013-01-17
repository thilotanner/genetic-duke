package jobs;

import models.GeneticConfiguration;
import models.GeneticProperty;
import no.priv.garshol.duke.comparators.DiceCoefficientComparator;
import no.priv.garshol.duke.comparators.DifferentComparator;
import no.priv.garshol.duke.comparators.ExactComparator;
import no.priv.garshol.duke.comparators.JaroWinkler;
import no.priv.garshol.duke.comparators.JaroWinklerTokenized;
import no.priv.garshol.duke.comparators.Levenshtein;
import no.priv.garshol.duke.comparators.MetaphoneComparator;
import no.priv.garshol.duke.comparators.NorphoneComparator;
import no.priv.garshol.duke.comparators.PersonNameComparator;
import no.priv.garshol.duke.comparators.SoundexComparator;
import no.priv.garshol.duke.comparators.WeightedLevenshtein;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GeneticHelper
{
    private static Random random = new Random();
    private static List<String> comparatorClasses;

    public static GeneticProperty getRandomProperty(GeneticConfiguration geneticConfiguration, String name) {
        GeneticProperty geneticProperty = new GeneticProperty();
        geneticProperty.geneticConfiguration = geneticConfiguration;
        geneticProperty.name = name;
        geneticProperty.lowProbability = random.nextDouble() * 0.5d;
        geneticProperty.highProbability = random.nextDouble() * 0.5d + 0.5d;
        geneticProperty.comparator = getRandomComparator();
        return geneticProperty;
    }

    private static String getRandomComparator() {
        if(comparatorClasses == null) {
            comparatorClasses = new ArrayList<String>();
            comparatorClasses.add(DiceCoefficientComparator.class.getName());
            comparatorClasses.add(DifferentComparator.class.getName());
            comparatorClasses.add(ExactComparator.class.getName());
            comparatorClasses.add(JaroWinkler.class.getName());
            comparatorClasses.add(JaroWinklerTokenized.class.getName());
            comparatorClasses.add(Levenshtein.class.getName());
            comparatorClasses.add(PersonNameComparator.class.getName());
            comparatorClasses.add(SoundexComparator.class.getName());
            comparatorClasses.add(WeightedLevenshtein.class.getName());
            comparatorClasses.add(NorphoneComparator.class.getName());
            comparatorClasses.add(MetaphoneComparator.class.getName());
        }

        Collections.shuffle(comparatorClasses);
        return comparatorClasses.get(0);
    }
}

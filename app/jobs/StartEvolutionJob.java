package jobs;

import models.Evolution;
import models.Generation;
import models.GeneticConfiguration;
import models.GeneticProperty;
import no.priv.garshol.duke.Comparator;
import no.priv.garshol.duke.comparators.DiceCoefficientComparator;
import no.priv.garshol.duke.comparators.DifferentComparator;
import no.priv.garshol.duke.comparators.ExactComparator;
import no.priv.garshol.duke.comparators.JaroWinkler;
import no.priv.garshol.duke.comparators.JaroWinklerTokenized;
import no.priv.garshol.duke.comparators.Levenshtein;
import no.priv.garshol.duke.comparators.PersonNameComparator;
import no.priv.garshol.duke.comparators.SoundexComparator;
import play.db.jpa.JPA;
import play.jobs.Job;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class StartEvolutionJob extends Job
{
    private Long evolutionId;

    private Random random = new Random();
    private ArrayList<Class<? extends Comparator>> comparatorClasses;

    public StartEvolutionJob(Long evolutionId)
    {
        this.evolutionId = evolutionId;
    }

    @Override
    public void doJob() throws Exception
    {
        Evolution evolution = Evolution.findById(evolutionId);

        Generation generation = new Generation();
        generation.evolution = evolution;
        generation.number = 1;
        generation.save();

        // create first generation
        for(int i = 0; i < evolution.numberOfConfigurations; i++) {
            GeneticConfiguration geneticConfiguration = createConfiguration(generation);
            geneticConfiguration.save();
        }

        JPA.em().getTransaction().commit();
        JPA.em().getTransaction().begin();

        EvaluateGenerationJob evaluateGenerationJob = new EvaluateGenerationJob(generation.getId());
        evaluateGenerationJob.now();
    }

    private GeneticConfiguration createConfiguration(Generation generation)
    {
        GeneticConfiguration geneticConfiguration = new GeneticConfiguration();
        geneticConfiguration.generation = generation;
        geneticConfiguration.threshold = random.nextDouble() * 0.5d + 0.5d;

        List<GeneticProperty> geneticProperties = new ArrayList<GeneticProperty>();
        geneticProperties.add(getRandomProperty(geneticConfiguration, "company"));
        geneticProperties.add(getRandomProperty(geneticConfiguration, "firstName"));
        geneticProperties.add(getRandomProperty(geneticConfiguration, "lastName"));
        geneticProperties.add(getRandomProperty(geneticConfiguration, "street"));
        geneticProperties.add(getRandomProperty(geneticConfiguration, "city"));
        geneticConfiguration.geneticProperties = geneticProperties;

        return geneticConfiguration;
    }

    private GeneticProperty getRandomProperty(GeneticConfiguration geneticConfiguration, String name) {
        GeneticProperty geneticProperty = new GeneticProperty();
        geneticProperty.geneticConfiguration = geneticConfiguration;
        geneticProperty.name = name;
        geneticProperty.lowProbability = random.nextDouble() * 0.5d;
        geneticProperty.highProbability = random.nextDouble() * 0.5d + 0.5d;
        geneticProperty.comparator = getRandomComparator().getClass().getName();
        return geneticProperty;
    }

    private Comparator getRandomComparator() {
        if(comparatorClasses == null) {
            comparatorClasses = new ArrayList<Class<? extends Comparator>>();
            comparatorClasses.add(DiceCoefficientComparator.class);
            comparatorClasses.add(DifferentComparator.class);
            comparatorClasses.add(ExactComparator.class);
            comparatorClasses.add(JaroWinkler.class);
            comparatorClasses.add(JaroWinklerTokenized.class);
            comparatorClasses.add(Levenshtein.class);
            comparatorClasses.add(PersonNameComparator.class);
            comparatorClasses.add(SoundexComparator.class);
        }

        Collections.shuffle(comparatorClasses);
        try {
            return comparatorClasses.get(0).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

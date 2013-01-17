package jobs;

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
import play.jobs.Job;
import selection.SelectionAlgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class EvolveGenerationJob extends Job
{
    private Long generationId;

    private Random random = new Random();

    private ArrayList<Class<? extends Comparator>> comparatorClasses;

    public EvolveGenerationJob(Long generationId)
    {
        this.generationId = generationId;
    }

    @Override
    public void doJob() throws Exception
    {
        Generation generation = Generation.findById(generationId);

        List<GeneticConfiguration> configurations = new ArrayList<GeneticConfiguration>(generation.geneticConfigurations);

        Collections.sort(configurations, new FitnessComparator());

        Class<SelectionAlgorithm> selectionAlgorithmClass =
                (Class<SelectionAlgorithm>) Class.forName(generation.evolution.selectionAlgorithm);
        SelectionAlgorithm selectionAlgorithm = selectionAlgorithmClass.newInstance();

        List<GeneticConfiguration> selectedConfigurations = selectionAlgorithm.select(configurations);

        // clone
        List<GeneticConfiguration> newConfigurations = new ArrayList<GeneticConfiguration>();
        for(GeneticConfiguration geneticConfiguration : selectedConfigurations) {
            newConfigurations.add(geneticConfiguration.duplicate());
        }

        // mutate
        mutate(newConfigurations);

        // save and start evaluation
        Generation newGeneration = new Generation();
        newGeneration.evolution = generation.evolution;
        newGeneration.number = generation.number + 1;
        newGeneration.save();

        for(GeneticConfiguration configuration : newConfigurations) {
            configuration.generation = newGeneration;
            configuration.save();
        }

        EvaluateGenerationJob evaluateGenerationJob = new EvaluateGenerationJob(newGeneration.getId());
        evaluateGenerationJob.now();
    }

    private void mutate(List<GeneticConfiguration> configurations) {
        for(GeneticConfiguration configuration : configurations) {
            int choice = Math.abs(random.nextInt() % 4);
            if(choice == 0) {
                mutate(configuration);
            } else {
                int randPos = Math.abs(random.nextInt() % configurations.size());
                GeneticConfiguration mate = configurations.get(randPos);
                mate(configuration, mate);
            }
        }
    }

    private void mutate(GeneticConfiguration configuration) {
        int propertiesToChange = Math.abs(random.nextInt() % 3 + 1);
        for(int i = 0; i < propertiesToChange; i++) {
            int randPos = Math.abs(random.nextInt() % configuration.geneticProperties.size());
            if(randPos == 0) {
                configuration.threshold = random.nextDouble() * 0.5d + 0.5d;
            }
            GeneticProperty property = configuration.geneticProperties.get(randPos);
            configuration.geneticProperties.set(randPos, GeneticHelper.getRandomProperty(configuration, property.name));
        }
    }

    private void mate(GeneticConfiguration geneticConfiguration,
                      GeneticConfiguration mate) {
        for(int i = 0; i < geneticConfiguration.geneticProperties.size(); i++) {
            if(random.nextBoolean()) {
                GeneticProperty property = geneticConfiguration.geneticProperties.get(i);
                GeneticProperty mateProperty = mate.geneticProperties.get(i);
                property.comparator = mateProperty.comparator;
                property.lowProbability = mateProperty.lowProbability;
                property.highProbability = mateProperty.highProbability;
            }
        }
    }
}

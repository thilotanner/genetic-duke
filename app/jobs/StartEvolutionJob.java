package jobs;

import models.Evolution;
import models.Generation;
import models.GeneticConfiguration;
import models.GeneticProperty;
import no.priv.garshol.duke.Comparator;
import play.db.jpa.JPA;
import play.jobs.Job;

import java.util.ArrayList;
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
        generation.number = 0;
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
        geneticProperties.add(GeneticHelper.getRandomProperty(geneticConfiguration, "company"));
        geneticProperties.add(GeneticHelper.getRandomProperty(geneticConfiguration, "firstName"));
        geneticProperties.add(GeneticHelper.getRandomProperty(geneticConfiguration, "lastName"));
        geneticProperties.add(GeneticHelper.getRandomProperty(geneticConfiguration, "street"));
        geneticProperties.add(GeneticHelper.getRandomProperty(geneticConfiguration, "city"));
        geneticConfiguration.geneticProperties = geneticProperties;

        return geneticConfiguration;
    }


}

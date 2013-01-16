package jobs;

import duplicates.EvaluationListener;
import fitness.FitnessFunction;
import models.GeneticConfiguration;
import models.GeneticProperty;
import no.priv.garshol.duke.Comparator;
import no.priv.garshol.duke.Configuration;
import no.priv.garshol.duke.Processor;
import no.priv.garshol.duke.Property;
import play.Play;
import play.jobs.Job;

import java.util.ArrayList;
import java.util.List;

public class EvaluateConfigJob extends Job
{
    private Long geneticConfigurationId;

    private GeneticConfiguration geneticConfiguration;

    public EvaluateConfigJob(Long geneticConfigurationId)
    {
        this.geneticConfigurationId = geneticConfigurationId;
    }

    @Override
    public void doJob() throws Exception
    {
        geneticConfiguration = GeneticConfiguration.findById(geneticConfigurationId);
        if(geneticConfiguration == null) {
            throw new IllegalStateException("Unkown genetic configuration");
        }

        Configuration configuration = new Configuration();
        configuration.addDataSource(0, new ContactDataSource());

        configuration.setThreshold(geneticConfiguration.threshold);
        configuration.setMaybeThreshold(geneticConfiguration.threshold);

        List<Property> properties = new ArrayList<Property>();
        properties.add(new Property("id"));
        for(GeneticProperty geneticProperty : geneticConfiguration.geneticProperties) {
            Class<Comparator> comparatorClass = (Class<Comparator>) Class.forName(geneticProperty.comparator);
            Comparator comparator = comparatorClass.newInstance();
            Property property =
                    new Property(geneticProperty.name, comparator, geneticProperty.lowProbability, geneticProperty.highProbability);
            properties.add(property);
        }
        configuration.setProperties(properties);

        Processor processor = new Processor(configuration, false);
        EvaluationListener listener = new EvaluationListener(Play.getFile("conf/duplicates.txt"));
        processor.addMatchListener(listener);
        processor.deduplicate();
        processor.close();

        geneticConfiguration.totalRecords = listener.getTotalRecords();
        geneticConfiguration.truePositives = listener.getTruePositives();
        geneticConfiguration.trueNegatives = listener.getTrueNegatives();
        geneticConfiguration.falsePositives = listener.getFalsePositives();
        geneticConfiguration.falseNegatives = listener.getFalseNegatives();

        // calculate fitness
        Class<FitnessFunction> fitnessFunctionClass =
                (Class<FitnessFunction>) Class.forName(geneticConfiguration.generation.evolution.fitnessFunction);
        FitnessFunction fitnessFunction = fitnessFunctionClass.newInstance();
        geneticConfiguration.fitness = fitnessFunction.calculateFitness(geneticConfiguration);

        geneticConfiguration.save();
    }
}

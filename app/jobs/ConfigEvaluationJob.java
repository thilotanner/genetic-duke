package jobs;

import duplicates.EvaluationListener;
import models.GeneticConfiguration;
import no.priv.garshol.duke.Configuration;
import no.priv.garshol.duke.DataSource;
import no.priv.garshol.duke.Processor;
import no.priv.garshol.duke.Property;
import play.Play;
import play.jobs.Job;

import java.util.ArrayList;
import java.util.List;

public class ConfigEvaluationJob extends Job
{
    private Long geneticConfigurationId;

    private GeneticConfiguration geneticConfiguration;

    @Override
    public void doJob() throws Exception
    {
        geneticConfiguration = GeneticConfiguration.findById(geneticConfigurationId);
        if(geneticConfiguration == null) {
            throw new IllegalStateException("Unkown genetic configuration");
        }


        Configuration configuration = new Configuration();
        int i = 0;
        for (DataSource dataSource : seedConfiguration.getDataSources()) {
            configuration.addDataSource(i, dataSource);
            i++;
        }

        configuration.setThreshold(geneticConfiguration.threshold);
        configuration.setMaybeThreshold(geneticConfiguration.threshold);

        List<Property> properties = new ArrayList<Property>();
        properties.add(new Property("id"));
        properties.addAll(geneticConfiguration.getProperties());
        configuration.setProperties(properties);

        Processor processor = new Processor(configuration, false);
        EvaluationListener listener = new EvaluationListener(Play.getFile("conf/duplicates.txt"));
        processor.addMatchListener(listener);
        processor.deduplicate();
        processor.close();



        geneticConfiguration.setFMeasure(listener.getFMeasure());
    }
}

package duplicates;

import jobs.ContactDataSource;
import models.Contact;
import models.GeneticConfiguration;
import models.GeneticProperty;
import no.priv.garshol.duke.Comparator;
import no.priv.garshol.duke.Configuration;
import no.priv.garshol.duke.Processor;
import no.priv.garshol.duke.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ContactDeduplicator
{
    private GeneticConfiguration geneticConfiguration;

    public ContactDeduplicator(GeneticConfiguration geneticConfiguration)
    {
        this.geneticConfiguration = geneticConfiguration;
    }

    public List<Map<Long,Contact>> deduplicate() throws Exception {
        Configuration configuration = new Configuration();

        configuration.setThreshold(geneticConfiguration.threshold);
        configuration.setMaybeThreshold(geneticConfiguration.threshold);

        configuration.addDataSource(0, new ContactDataSource());

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

        Processor processor = new Processor(configuration);
        MapMatchListener<Contact> listener = new MapMatchListener<Contact>(Contact.class);
        processor.addMatchListener(listener);
        processor.deduplicate();
        processor.close();

        return listener.getDuplicates();
    }
}

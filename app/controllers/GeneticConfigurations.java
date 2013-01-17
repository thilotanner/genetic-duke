package controllers;

import duplicates.ContactDeduplicator;
import models.Contact;
import models.GeneticConfiguration;
import play.mvc.Controller;

import java.util.List;
import java.util.Map;

public class GeneticConfigurations extends Controller
{
    public static void show(Long id)
    {
        notFoundIfNull(id);
        GeneticConfiguration geneticConfiguration = GeneticConfiguration.findById(id);
        notFoundIfNull(geneticConfiguration);
        render(geneticConfiguration);
    }

    public static void deduplicateContacts(Long id) throws Exception
    {
        notFoundIfNull(id);
        GeneticConfiguration geneticConfiguration = GeneticConfiguration.findById(id);
        notFoundIfNull(geneticConfiguration);

        ContactDeduplicator deduplicator = new ContactDeduplicator(geneticConfiguration);
        List<Map<Long, Contact>> duplicates = deduplicator.deduplicate();
        render(duplicates);
    }
}

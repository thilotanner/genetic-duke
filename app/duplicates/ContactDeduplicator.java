package duplicates;

import no.priv.garshol.duke.ConfigLoader;
import no.priv.garshol.duke.Configuration;
import no.priv.garshol.duke.Processor;
import play.Play;

public class ContactDeduplicator {
    public void deduplicate() throws Exception {
        Configuration config = ConfigLoader.load(Play.getFile("conf/duke-contacts.xml").getAbsolutePath());
        Processor processor = new Processor(config);
        EvaluationListener listener = new EvaluationListener(Play.getFile("conf/duplicates.txt"));
        processor.addMatchListener(listener);
        processor.deduplicate();
        processor.close();
    }
}

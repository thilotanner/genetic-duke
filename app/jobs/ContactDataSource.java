package jobs;

import no.priv.garshol.duke.DataSource;
import no.priv.garshol.duke.Logger;
import no.priv.garshol.duke.RecordIterator;

public class ContactDataSource implements DataSource
{
    @Override
    public RecordIterator getRecords()
    {
        return new ContactRecordIterator();
    }

    @Override
    public void setLogger(Logger logger)
    {
        // do nothing
    }
}

package jobs;

import models.Contact;
import no.priv.garshol.duke.Record;
import no.priv.garshol.duke.RecordIterator;

import java.util.ListIterator;

public class ContactRecordIterator extends RecordIterator
{
    private ListIterator<Contact> iterator;

    public ContactRecordIterator()
    {
        iterator = Contact
    }

    @Override
    public boolean hasNext()
    {
        return iterator.hasNext();
    }

    @Override
    public Record next()
    {
        Contact contact = iterator.next();

        new Record()
    }
}

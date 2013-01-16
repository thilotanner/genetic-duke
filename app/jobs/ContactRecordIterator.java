package jobs;

import models.Contact;
import no.priv.garshol.duke.Record;
import no.priv.garshol.duke.RecordImpl;
import no.priv.garshol.duke.RecordIterator;

import java.util.ListIterator;

public class ContactRecordIterator extends RecordIterator
{
    private ListIterator iterator;

    public ContactRecordIterator()
    {
        iterator = Contact.findAll().listIterator();
    }

    @Override
    public boolean hasNext()
    {
        return iterator.hasNext();
    }

    @Override
    public Record next()
    {
        Contact contact = (Contact) iterator.next();

        RecordImpl record = new RecordImpl();
        record.addValue("id", contact.getId().toString());
        record.addValue("company", contact.company != null ? contact.company : "");
        record.addValue("firstName", contact.firstName != null ? contact.firstName : "");
        record.addValue("lastName", contact.lastName != null ? contact.lastName : "");
        record.addValue("street", contact.street != null ? contact.street : "");
        record.addValue("city", contact.city != null ? contact.city : "");

        return record;
    }
}

package controllers;

import models.Contact;
import play.mvc.Controller;

import java.util.List;

public class Contacts extends Controller
{
    private static final int PAGE_SIZE = 10;

    public static void index(int page)
    {
        if (page < 1) {
            page = 1;
        }

        List<Contact> contacts = Contact.all().fetch(page, PAGE_SIZE);
        Long count = Contact.count();

        renderArgs.put("pageSize", PAGE_SIZE);
        render(contacts, count);
    }

    public static void show(Long id)
    {
        notFoundIfNull(id);
        Contact contact = Contact.findById(id);
        notFoundIfNull(contact);
        render(contact);
    }
}

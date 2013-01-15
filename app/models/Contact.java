package models;

import play.data.validation.Email;
import play.data.validation.Required;
import play.data.validation.URL;
import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;

@Entity
public class Contact extends Model
{
    public String company;

    @Enumerated(EnumType.STRING)
    public Title title;

    public String firstName;

    public String lastName;

    public String street;

    public String postOfficeBox;

    public String postalCode;

    @Required
    public String city;

    public String countryCode;

    public String phone;

    public String fax;

    public String mobile;

    @Email
    public String email;

    @URL
    public String website;

    @Lob
    public String comments;
}

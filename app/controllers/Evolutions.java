package controllers;

import jobs.StartEvolutionJob;
import models.Evolution;
import play.data.validation.Valid;
import play.data.validation.Validation;
import play.db.jpa.JPA;
import play.mvc.Controller;

import java.util.List;

public class Evolutions extends Controller
{
    public static void index()
    {
        List<Evolution> evolutions = Evolution.findAll();

        render(evolutions);
    }

    public static void show(Long id)
    {
        notFoundIfNull(id);
        Evolution evolution = Evolution.findById(id);
        notFoundIfNull(evolution);
        render(evolution);
    }

    public static void form()
    {
        render();
    }

    public static void save(@Valid Evolution evolution)
    {
        if(Validation.hasErrors()) {
            render("@form", evolution);
        }

        evolution.save();

        JPA.em().getTransaction().commit();
        JPA.em().getTransaction().begin();

        StartEvolutionJob startEvolutionJob = new StartEvolutionJob(evolution.getId());
        startEvolutionJob.now();

        flash.success("Evolution successfully started");
        index();
    }

    public static void delete(Long id)
    {
        notFoundIfNull(id);
        Evolution evolution = Evolution.findById(id);
        notFoundIfNull(evolution);

        evolution.delete();

        flash.success("Evolution successfully deleted");
        index();
    }
}

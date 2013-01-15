package controllers;

import models.Evolution;
import play.data.validation.Valid;
import play.data.validation.Validation;
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
        flash.success("Evolution successfully started");
        index();
    }
}

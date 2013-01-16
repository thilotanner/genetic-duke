package controllers;

import models.GeneticConfiguration;
import play.mvc.Controller;

public class GeneticConfigurations extends Controller
{
    public static void show(Long id)
    {
        notFoundIfNull(id);
        GeneticConfiguration geneticConfiguration = GeneticConfiguration.findById(id);
        notFoundIfNull(geneticConfiguration);
        render(geneticConfiguration);
    }
}

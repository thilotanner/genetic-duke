package genetic;

import no.priv.garshol.duke.ConfigLoader;
import no.priv.garshol.duke.Configuration;

import java.io.File;

public class Algorithm {

    private static final int NUMBER_OF_CONFIGURATIONS = 100;

    private static final int NUMBER_OF_GENERATIONS = 100;

    private Configuration configuration;

    public Algorithm(File configurationFile) {
        try {
            configuration = ConfigLoader.load(configurationFile.getAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        Population population = new Population(configuration, NUMBER_OF_CONFIGURATIONS);

        for(int i = 0; i < NUMBER_OF_GENERATIONS; i++) {
            population = population.evolve();
        }
    }
}

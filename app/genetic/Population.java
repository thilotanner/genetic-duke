package genetic;

import duplicates.EvaluationListener;
import no.priv.garshol.duke.Comparator;
import no.priv.garshol.duke.Configuration;
import no.priv.garshol.duke.DataSource;
import no.priv.garshol.duke.Processor;
import no.priv.garshol.duke.Property;
import no.priv.garshol.duke.comparators.DiceCoefficientComparator;
import no.priv.garshol.duke.comparators.DifferentComparator;
import no.priv.garshol.duke.comparators.ExactComparator;
import no.priv.garshol.duke.comparators.JaroWinkler;
import no.priv.garshol.duke.comparators.JaroWinklerTokenized;
import no.priv.garshol.duke.comparators.Levenshtein;
import no.priv.garshol.duke.comparators.PersonNameComparator;
import no.priv.garshol.duke.comparators.SoundexComparator;
import play.Logger;
import play.Play;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Population {

    private static List<Class<? extends Comparator>> comparatorClasses;

    private Configuration seedConfiguration;

    private Random random = new Random();

    List<GeneticConfiguration> configurations;

    public Population(Configuration seedConfiguration, int size) {
        this.seedConfiguration = seedConfiguration;
        configurations = new ArrayList<GeneticConfiguration>();
        for(int i = 0; i < size; i++) {
            createRandomConfiguration(seedConfiguration);
        }
    }

    public Population(Configuration seedConfiguration, List<GeneticConfiguration> configurations) {
        this.seedConfiguration = seedConfiguration;
        this.configurations = configurations;
    }

    public void evaluate() {
        for(GeneticConfiguration configuration : configurations) {
            try {
                evaluate(configuration);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Population evolve() {
        if(!isEvaluated()) {
            evaluate();
        }

        Collections.sort(configurations, new java.util.Comparator<GeneticConfiguration>() {
            public int compare(GeneticConfiguration geneticConfiguration1,
                               GeneticConfiguration geneticConfiguration2) {
                Double f1 = geneticConfiguration1.getFMeasure();
                if(f1 == null) {
                    f1 = 0.0d;
                }
                Double f2 = geneticConfiguration2.getFMeasure();
                if(f2 == null) {
                    f2 = 0.0d;
                }

                return -1 * f1.compareTo(f2);
            }
        });

        int highIndex = (int) (configurations.size() * 0.25);
        int lowIndex = (int) (configurations.size() * 0.70);

        List<GeneticConfiguration> newConfigurations = new ArrayList<GeneticConfiguration>();

        Logger.info("!!!!!!");
        Logger.info("Best configuration: " + configurations.get(0).toString());
        Logger.info("!!!!!!");

        for(int i = 0; i < configurations.size(); i++) {
            if(i < highIndex) {
                // clone best configs twice
                newConfigurations.add(configurations.get(i).clone());
                newConfigurations.add(configurations.get(i).clone());
            } else if(i >= highIndex && i < lowIndex) {
                newConfigurations.add(configurations.get(i).clone()); // clone once
            } else {
                // throw away worst configurations;
            }
        }

        // fill with very best configurations
        while (newConfigurations.size() < configurations.size()) {
            int randomPos = Math.abs(random.nextInt() % 3);
            newConfigurations.add(configurations.get(randomPos).clone());
        }

        mutate(newConfigurations);

        return new Population(seedConfiguration, newConfigurations);
    }

    private void mutate(List<GeneticConfiguration> configurations) {
        for(GeneticConfiguration configuration : configurations) {
            int choice = Math.abs(random.nextInt() % 4);
            if(choice == 0) {
                mutate(configuration);
            } else {
                int randPos = Math.abs(random.nextInt() % configurations.size());
                GeneticConfiguration mate = configurations.get(randPos);
                mate(configuration, mate);
            }
        }
    }

    private void mutate(GeneticConfiguration configuration) {
        int propertiesToChange = Math.abs(random.nextInt() % 3 + 1);
        for(int i = 0; i < propertiesToChange; i++) {
            int randPos = Math.abs(random.nextInt() % (configuration.getProperties().size() + 1));
            if(randPos == configuration.getProperties().size()) {
                configuration.setThreshold(random.nextDouble() * 0.5d + 0.5d);
            }
            Property property = configuration.getProperties().get(i);
            configuration.getProperties().set(i, getRandomProperty(property.getName()));
        }
    }

    private void mate(GeneticConfiguration geneticConfiguration,
                      GeneticConfiguration mate) {
        for(int i = 0; i < geneticConfiguration.getProperties().size(); i++) {
            if(random.nextBoolean()) {
                Property property = geneticConfiguration.getProperties().get(i);
                Property mateProperty = mate.getProperties().get(i);
                property.setComparator(mateProperty.getComparator());
                property.setLowProbability(mateProperty.getLowProbability());
                property.setHighProbability(mateProperty.getHighProbability());
            }
        }
    }

    public boolean isEvaluated() {
        // lastest f measure is available
        return configurations.get(configurations.size() - 1).getFMeasure() != null;
    }

    private void evaluate(GeneticConfiguration geneticConfiguration) throws Exception {
        Logger.info("-----");
        Logger.info("Evaluate config: \n" + geneticConfiguration.toString());

        // merge configs
        Configuration configuration = new Configuration();
        int i = 0;
        for (DataSource dataSource : seedConfiguration.getDataSources()) {
            configuration.addDataSource(i, dataSource);
            i++;
        }

        configuration.setThreshold(geneticConfiguration.getThreshold());
        configuration.setMaybeThreshold(geneticConfiguration.getThreshold());

        List<Property> properties = new ArrayList<Property>();
        properties.add(seedConfiguration.getPropertyByName("id"));
        properties.addAll(geneticConfiguration.getProperties());
        configuration.setProperties(properties);

        Processor processor = new Processor(configuration, false);
        EvaluationListener listener = new EvaluationListener(Play.getFile("conf/duplicates.txt"));
        processor.addMatchListener(listener);
        processor.deduplicate();
        processor.close();

        geneticConfiguration.setFMeasure(listener.getFMeasure());
    }

    private void createRandomConfiguration(Configuration seedConfiguration) {
        GeneticConfiguration configuration = new GeneticConfiguration();
        configuration.setThreshold(random.nextDouble() * 0.5d + 0.5d);

        for(Property property : seedConfiguration.getProperties()) {
            if(!property.getName().toLowerCase().equals("id")) {
                // all properties other than id
                configuration.addProperty(getRandomProperty(property.getName()));
            }
        }

        configurations.add(configuration);
    }

    private Property getRandomProperty(String name) {
        double low = random.nextDouble() * 0.5d;
        double high = random.nextDouble() * 0.5d + 0.5d;

        return new Property(name, getRandomComparator(), low, high);
    }

    private Comparator getRandomComparator() {
        if(comparatorClasses == null) {
            comparatorClasses = new ArrayList<Class<? extends Comparator>>();
            comparatorClasses.add(DiceCoefficientComparator.class);
            comparatorClasses.add(DifferentComparator.class);
            comparatorClasses.add(ExactComparator.class);
            comparatorClasses.add(JaroWinkler.class);
            comparatorClasses.add(JaroWinklerTokenized.class);
            comparatorClasses.add(Levenshtein.class);
            comparatorClasses.add(PersonNameComparator.class);
            comparatorClasses.add(SoundexComparator.class);
        }

        Collections.shuffle(comparatorClasses);
        try {
            return comparatorClasses.get(0).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

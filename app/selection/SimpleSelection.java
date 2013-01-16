package selection;

import models.GeneticConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimpleSelection implements SelectionAlgorithm
{
    private Random random = new Random();

    @Override
    public List<GeneticConfiguration> select(List<GeneticConfiguration> configurations)
    {
        int highIndex = (int) (configurations.size() * 0.25);
        int lowIndex = (int) (configurations.size() * 0.70);

        List<GeneticConfiguration> newConfigurations = new ArrayList<GeneticConfiguration>();

        for(int i = 0; i < configurations.size(); i++) {
            if(i < highIndex) {
                // clone best configs twice
                newConfigurations.add(configurations.get(i));
                newConfigurations.add(configurations.get(i));
            } else if(i >= highIndex && i < lowIndex) {
                newConfigurations.add(configurations.get(i)); // clone once
            } else {
                // throw away worst configurations;
            }
        }

        // fill with very best configurations
        while (newConfigurations.size() < configurations.size()) {
            int randomPos = Math.abs(random.nextInt() % 3);
            newConfigurations.add(configurations.get(randomPos));
        }

        return newConfigurations;
    }
}

package selection;

import models.GeneticConfiguration;

import java.util.List;

public interface SelectionAlgorithm
{
    public List<GeneticConfiguration> select(List<GeneticConfiguration> configurations);
}

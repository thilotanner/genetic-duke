package genetic;

import no.priv.garshol.duke.Property;

import java.util.ArrayList;
import java.util.List;

public class GeneticConfiguration implements Cloneable {

    private double threshold;

    private List<Property> properties;

    private Double fMeasure;

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public void addProperty(Property property) {
        if(properties == null) {
            properties = new ArrayList<Property>();
        }
        properties.add(property);
    }

    public List<Property> getProperties() {
        return properties;
    }

    public Double getFMeasure() {
        return fMeasure;
    }

    public void setFMeasure(Double fMeasure) {
        this.fMeasure = fMeasure;
    }

    public GeneticConfiguration clone() {
        GeneticConfiguration configuration = new GeneticConfiguration();
        configuration.setThreshold(threshold);
        for(Property currentProperty : properties) {
            Property property = new Property(
                    currentProperty.getName(),
                    currentProperty.getComparator(),
                    currentProperty.getLowProbability(),
                    currentProperty.getHighProbability());
            configuration.addProperty(property);
        }
        return configuration;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Threshold: ").append(threshold).append("\n");
        for(Property property : properties) {
            sb.append("Property: ").append(property.getName())
                    .append(" [").append(property.getComparator().getClass().getSimpleName()).append("] ")
                    .append(property.getLowProbability()).append(", ").append(property.getHighProbability()).append("\n");
        }
        return sb.toString();
    }
}

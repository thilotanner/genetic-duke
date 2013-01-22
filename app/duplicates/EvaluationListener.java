package duplicates;

import no.priv.garshol.duke.Record;
import no.priv.garshol.duke.matchers.AbstractMatchListener;
import no.priv.garshol.duke.matchers.PrintMatchListener;
import play.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EvaluationListener extends AbstractMatchListener {

    private Map<String, Set<String>> links;

    private Record currentRecord;
    private boolean matchFound;

    int totalRecords = 0;
    int truePositives = 0;
    int falsePositives = 0;
    int falseNegatives = 0;
    int trueNegatives = 0;

    public EvaluationListener(File testFile) {
        try {
            buildLinks(testFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void buildLinks(File testFile) throws IOException {

        links = new HashMap<String, Set<String>>();

        BufferedReader reader = new BufferedReader(new FileReader(testFile));
        String line = reader.readLine();
        while (line != null) {
            String[] ids = line.split(",");
            if(ids.length != 2) {
                Logger.warn(String.format("Malformed test line: '%s'", line));
                continue;
            }

            String id1 = ids[0];
            String id2 = ids[1];

            // build link1
            if(!links.containsKey(id1)) {
                links.put(id1, new HashSet<String>());
            }
            links.get(id1).add(id2);

            // build link2
            if(!links.containsKey(id2)) {
                links.put(id2, new HashSet<String>());
            }
            links.get(id2).add(id1);

            line = reader.readLine();
        }

        reader.close();
    }

    @Override
    public void startRecord(Record record) {
        currentRecord = record;
        totalRecords++;
    }

    public void matches(Record record1, Record record2, double v) {
        matchFound = true;
        String id1 = record1.getValue("id");
        String id2 = record2.getValue("id");
        if(links.containsKey(id1)) {
            // check link
            if(links.get(id1).contains(id2)) {
                // match found -> true positive
                truePositives++;
                Logger.debug("True positive found: %s | %s", id1, id2);
                Logger.debug(PrintMatchListener.toString(record1));
                Logger.debug(PrintMatchListener.toString(record2));
                Logger.debug("-----");
            } else {
                // match found, but no link -> false positive
                falsePositives++;
                Logger.debug("False positive found: %s | %s", id1, id2);
                Logger.debug(PrintMatchListener.toString(record1));
                Logger.debug(PrintMatchListener.toString(record2));
                Logger.debug("-----");
            }
        }
    }

    @Override
    public void endRecord() {
        // check if there is a link
        String id = currentRecord.getValue("id");
        if(!matchFound && links.containsKey(id)) {
            // not match found, but there is a match -> false negatives
            falseNegatives++;
            Logger.debug("False negative found: %s", id);
            Logger.debug(PrintMatchListener.toString(currentRecord));
            Logger.debug("-----");
        } else {
            trueNegatives++;
        }

        matchFound = false;
    }

    public int getTotalRecords()
    {
        return totalRecords;
    }

    public int getTruePositives()
    {
        return truePositives;
    }

    public int getFalsePositives()
    {
        return falsePositives;
    }

    public int getFalseNegatives()
    {
        return falseNegatives;
    }

    public int getTrueNegatives()
    {
        return trueNegatives;
    }

    public double getPrecision() {
        if(truePositives == 0) {
            return 0d;
        }

        return ((double) truePositives) / ((double) truePositives + falsePositives);
    }

    public double getRecall() {
        if(truePositives == 0) {
            return 0d;
        }

        return ((double) truePositives) / ((double) truePositives + falseNegatives);
    }

    public double getFMeasure() {
        double precision = getPrecision();
        double recall = getRecall();

        if(precision == 0d || recall == 0d) {
            return 0d;
        }

        return 2d * ((precision * recall) / (precision + recall));
    }

    @Override
    public void endProcessing() {
        Logger.info(String.format("Total records:   %d", totalRecords));
        Logger.info(String.format("True positives:  %d", truePositives));
        Logger.info(String.format("False positives: %d", falsePositives));
        Logger.info(String.format("False negatives: %d", falseNegatives));
        Logger.info(String.format("True negatives:  %d", trueNegatives));
        Logger.info(String.format("Precision:       %s", getPrecision()));
        Logger.info(String.format("Recall:          %s", getRecall()));
        Logger.info(String.format("Fitness:         %s", getFMeasure()));
    }
}

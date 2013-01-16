package jobs;

import models.Generation;
import models.GeneticConfiguration;
import play.jobs.Job;
import play.libs.F;

import java.util.ArrayList;
import java.util.List;

public class EvaluateGenerationJob extends Job
{
    private Long generationId;

    public EvaluateGenerationJob(Long generationId)
    {
        this.generationId = generationId;
    }

    @Override
    public void doJob() throws Exception
    {
        Generation generation = Generation.findById(generationId);

        List<F.Promise<Void>> promises = new ArrayList<F.Promise<Void>>();
        for(GeneticConfiguration configuration : generation.geneticConfigurations) {
            EvaluateConfigJob evaluateConfigJob = new EvaluateConfigJob(configuration.getId());
            promises.add(evaluateConfigJob.now());
        }

        if(generation.number >= generation.evolution.numberOfGenerations) {
            return; // stop evolution
        }

        F.Promise<List<Void>> promise = F.Promise.waitAll(promises);
        promise.onRedeem(new F.Action<F.Promise<List<Void>>>() {
            public void invoke(F.Promise<List<Void>> result) {
                EvolveGenerationJob evolveGenerationJob = new EvolveGenerationJob(generationId);
                evolveGenerationJob.now();
            }
        });
    }
}

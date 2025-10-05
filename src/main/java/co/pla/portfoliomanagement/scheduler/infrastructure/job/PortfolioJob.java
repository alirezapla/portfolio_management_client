package co.pla.portfoliomanagement.scheduler.infrastructure.job;

import co.pla.portfoliomanagement.scheduler.application.service.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.StatefulJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class PortfolioJob implements StatefulJob {

    private static final Logger logger = LoggerFactory.getLogger(PortfolioJob.class);

    private final SchedulerService schedulerService;

    public PortfolioJob(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        var traceId = UUID.randomUUID().toString();
        logger.info("PortfolioJob Started");
        try {
            schedulerService.getAllJobs();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
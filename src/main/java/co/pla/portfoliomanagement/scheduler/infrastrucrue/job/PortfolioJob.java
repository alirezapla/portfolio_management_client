package co.pla.portfoliomanagement.scheduler.infrastrucrue.job;

import co.pla.portfoliomanagement.scheduler.service.PortfolioJobService;
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

    private final PortfolioJobService portfolioJobService;

    public PortfolioJob(PortfolioJobService portfolioJobService) {
        this.portfolioJobService = portfolioJobService;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        var traceId = UUID.randomUUID().toString();
        logger.info("PortfolioJob Started");
        try {
            portfolioJobService.processPortfolios();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
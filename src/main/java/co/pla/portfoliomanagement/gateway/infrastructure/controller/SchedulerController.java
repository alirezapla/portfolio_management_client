package co.pla.portfoliomanagement.gateway.infrastructure.controller;

import co.pla.portfoliomanagement.common.dto.SchedulerDto;
import co.pla.portfoliomanagement.gateway.infrastructure.util.response.SuccessfulResponseEntity;
import co.pla.portfoliomanagement.scheduler.application.facade.SchedulerFacade;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping(value = "/scheduler")
public class SchedulerController {
    private final SchedulerFacade schedulerFacade;

    public SchedulerController(SchedulerFacade schedulerFacade) {
        this.schedulerFacade = schedulerFacade;
    }

    @PostMapping
    public ResponseEntity<Object> scheduleTask(@Valid @RequestBody SchedulerDto schedulerDto) {
        return ResponseEntity.ok(new SuccessfulResponseEntity<>(schedulerFacade.createScheduler(schedulerDto)));
    }

    @PutMapping("/job/{id}")
    public ResponseEntity<Object> updateTask(@RequestBody SchedulerDto schedulerDto, @PathVariable String id) {
        return ResponseEntity.ok(new SuccessfulResponseEntity<>(schedulerFacade.updateScheduler(schedulerDto, id)));
    }

    @GetMapping("/jobs")
    public ResponseEntity<Object> getJobs() {
        return ResponseEntity.ok(new SuccessfulResponseEntity<>(schedulerFacade.getAllJobs()));
    }

    @GetMapping("/job/{id}")
    public ResponseEntity<Object> getJob(@PathVariable String id) {
        return ResponseEntity.ok(new SuccessfulResponseEntity<>(schedulerFacade.getJob(id)));
    }

    @DeleteMapping("/job/{id}")
    public ResponseEntity<Void> removeJob(@PathVariable String id) {
        var res = schedulerFacade.removeJob(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/job/enum")
    public ResponseEntity<Object> getJobTypes() {
        return ResponseEntity.ok(new SuccessfulResponseEntity<>(schedulerFacade.getEnum()));
    }
}

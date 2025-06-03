package com.maybank.api.transactionservice.controller;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jobs")
public class JobMonitoringController {

    private final JobExplorer jobExplorer;

    public JobMonitoringController(JobExplorer jobExplorer) {
        this.jobExplorer = jobExplorer;
    }

    @GetMapping
    public ResponseEntity<List<String>> getJobNames() {
        return ResponseEntity.ok(jobExplorer.getJobNames());
    }

    @GetMapping("/{jobName}/executions")
    public ResponseEntity<List<Map<String, Object>>> getJobExecutions(@PathVariable String jobName) {
        List<JobInstance> instances = jobExplorer.getJobInstances(jobName, 0, 10);
        List<Map<String, Object>> result = new ArrayList<>();

        for (JobInstance instance : instances) {
            List<JobExecution> executions = jobExplorer.getJobExecutions(instance);
            for (JobExecution execution : executions) {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("jobId", execution.getJobId());
                map.put("startTime", execution.getStartTime());
                map.put("endTime", execution.getEndTime());
                map.put("status", execution.getStatus());
                map.put("exitStatus", execution.getExitStatus().getExitCode());
                result.add(map);
            }
        }
        return ResponseEntity.ok(result);
    }
}

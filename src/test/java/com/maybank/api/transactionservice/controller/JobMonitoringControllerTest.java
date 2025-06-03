package com.maybank.api.transactionservice.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobMonitoringControllerTest {

    @InjectMocks
    private JobMonitoringController jobMonitoringController;

    @Mock
    private JobExplorer jobExplorer;

    @Test
    void testGetJobNames() {
        when(jobExplorer.getJobNames()).thenReturn(List.of("job1", "job2"));
        ResponseEntity<List<String>> names = jobMonitoringController.getJobNames();
        Assertions.assertNotNull(names.getBody());
        assertEquals(2, names.getBody().size());
        assertTrue(names.getBody().contains("job1"));
    }

    @Test
    void testGetJobExecutions() {
        JobInstance instance = new JobInstance(1L, "job1");
        JobExecution execution = new JobExecution(1L);
        execution.setStartTime(LocalDateTime.now());
        execution.setEndTime(LocalDateTime.now());
        execution.setStatus(BatchStatus.COMPLETED);
        execution.setExitStatus(new ExitStatus("COMPLETED"));

        when(jobExplorer.getJobInstances("job1", 0, 10)).thenReturn(List.of(instance));
        when(jobExplorer.getJobExecutions(instance)).thenReturn(List.of(execution));

        ResponseEntity<List<Map<String, Object>>> result = jobMonitoringController.getJobExecutions("job1");
        Assertions.assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        assertEquals(BatchStatus.COMPLETED, result.getBody().get(0).get("status"));
    }
}
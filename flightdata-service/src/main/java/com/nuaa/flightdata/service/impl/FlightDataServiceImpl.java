package com.nuaa.flightdata.service.impl;

import com.nuaa.flightdata.model.TaskStatus;
import com.nuaa.flightdata.model.AnalysisResults;
import com.nuaa.flightdata.service.FlightDataService;
import javax.jws.WebService;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@WebService(
        serviceName = "FlightDataServiceImplService",
        targetNamespace = "http://example.com/flightdata",
        endpointInterface = "com.nuaa.flightdata.service.FlightDataService"
)
public class FlightDataServiceImpl implements FlightDataService {

    // In-memory storage for demo purposes (use database in production)
    private final Map<String, AnalysisResults> analysisStore = new ConcurrentHashMap<>();
    private final Map<String, TaskStatus> taskStore = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newCachedThreadPool();

    @Override
    public TaskStatus readFlightData(String missionId) {
        String taskId = "TASK_" + missionId + "_" + System.currentTimeMillis();
        TaskStatus status = createNewTask(taskId, "Reading flight data for mission: " + missionId);
        // Create analysis ID based on mission ID and timestamp
        String analysisId = "ANALYSIS_" + missionId + "_" + System.currentTimeMillis();
        status.setAnalysisId(analysisId);
        taskStore.put(taskId, status);

        executor.submit(() -> {
            try {
                status.setStatus("IN_PROGRESS");
                status.setProgress(30);

                // Simulate reading flight data (replace with actual implementation)
                Thread.sleep(5000);

                // Store some sample results
                AnalysisResults results = new AnalysisResults();
                results.setAnalysisId(analysisId);
                results.setTimestamp(new Date());
                results.setParameters(Arrays.asList("Altitude", "Speed", "Temperature"));
                results.setMetrics(Arrays.asList(1024.5, 856.3, -45.7));
                results.setStatus("COMPLETED");

                analysisStore.put(analysisId, results);

                status.setStatus("COMPLETED");
                status.setProgress(100);
                status.setMessage("Successfully processed flight data for mission: " + missionId);
            } catch (Exception e) {
                status.setStatus("FAILED");
                status.setMessage("Error processing flight data: " + e.getMessage());
            } finally {
                status.setEndTime(new Date());
            }
        });

        return status;
    }

    @Override
    public TaskStatus saveAnalysisResults(String analysisId, AnalysisResults results) {
        String taskId = UUID.randomUUID().toString();
        TaskStatus status = createNewTask(taskId, "Saving analysis results: " + analysisId);
        status.setAnalysisId(analysisId);
        taskStore.put(taskId, status);

        executor.submit(() -> {
            try {
                status.setStatus("IN_PROGRESS");
                status.setProgress(30);

                // Simulate saving process
                Thread.sleep(3000);

                // Update with provided results
                results.setAnalysisId(analysisId);
                analysisStore.put(analysisId, results);

                status.setStatus("COMPLETED");
                status.setProgress(100);
                status.setMessage("Successfully saved analysis results");
            } catch (Exception e) {
                status.setStatus("FAILED");
                status.setMessage("Error saving analysis results: " + e.getMessage());
            } finally {
                status.setEndTime(new Date());
            }
        });

        return status;
    }

    @Override
    public AnalysisResults getAnalysisResults(String analysisId) {
        return analysisStore.get(analysisId);
    }

    @Override
    public TaskStatus checkTaskStatus(String taskId) {
        TaskStatus status = taskStore.get(taskId);

        // If we have info about this task, return it
        // Otherwise just indicate we don't know about this task
        if (status == null) {
            status = new TaskStatus();
            status.setTaskId(taskId);
            status.setStatus("UNKNOWN");
            status.setProgress(0);
            status.setMessage("No information available for this task ID");
        }
        return status;
    }

    private TaskStatus createNewTask(String taskId, String message) {
        TaskStatus status = new TaskStatus();
        status.setTaskId(taskId);
        status.setStatus("PENDING");
        status.setProgress(0);
        status.setMessage(message);
        status.setStartTime(new Date());
        return status;
    }

    // Cleanup method
    public void cleanup() {
        executor.shutdownNow();
    }
}

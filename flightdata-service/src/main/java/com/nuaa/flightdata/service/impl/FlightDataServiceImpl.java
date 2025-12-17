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
        targetNamespace = "http://example.com/",
        endpointInterface = "com.nuaa.flightdata.service.FlightDataService"
)
public class FlightDataServiceImpl implements FlightDataService {

    // In-memory storage for demo purposes (use database in production)
    private final Map<String, AnalysisResults> analysisStore = new ConcurrentHashMap<>();
    private final Map<String, Boolean> taskMap = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newCachedThreadPool();

    @Override
    public TaskStatus readFlightData(String missionId) {
        String taskId = UUID.randomUUID().toString();
        TaskStatus status = createNewTask(taskId, "Reading flight data for mission: " + missionId);

        executor.submit(() -> {
            try {
                status.setStatus("IN_PROGRESS");
                status.setProgress(30);

                // Simulate reading flight data (replace with actual implementation)
                Thread.sleep(5000);

                // Create analysis ID based on mission ID and timestamp
                String analysisId = "ANALYSIS_" + missionId + "_" + System.currentTimeMillis();

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
                taskMap.remove(taskId);
            }
        });

        taskMap.put(taskId, true); // We only need the key for tracking
        return status;
    }

    @Override
    public TaskStatus saveAnalysisResults(String analysisId, AnalysisResults results) {
        String taskId = UUID.randomUUID().toString();
        TaskStatus status = createNewTask(taskId, "Saving analysis results: " + analysisId);

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
                taskMap.remove(taskId);
            }
        });

        taskMap.put(taskId, null);
        return status;
    }

    @Override
    public AnalysisResults getAnalysisResults(String analysisId) {
        return analysisStore.get(analysisId);
    }

    @Override
    public TaskStatus checkTaskStatus(String taskId) {
        TaskStatus status = new TaskStatus();
        status.setTaskId(taskId);
        status.setStartTime(new Date());

        // If we have info about this task, return it
        // Otherwise just indicate we don't know about this task
        if (taskMap.containsKey(taskId)) {
            status.setStatus("IN_PROGRESS");
            status.setProgress(50);
            status.setMessage("Task is still processing");
        } else {
            // Try to find completed tasks (in a real system you'd query a database)
            status.setStatus("UNKNOWN");
            status.setMessage("No information available for this task ID");
        }

        status.setEndTime(new Date());
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

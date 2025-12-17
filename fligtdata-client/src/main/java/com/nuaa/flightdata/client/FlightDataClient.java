package com.nuaa.flightdata.client;

import com.nuaa.flightdata.model.AnalysisResults;
import com.nuaa.flightdata.model.TaskStatus;
import com.nuaa.flightdata.service.FlightDataService;

import javax.xml.namespace.QName;
import java.net.URL;

public class FlightDataClient {

    public static void main(String[] args) throws Exception {
        // Define service location
        URL wsdlURL = new URL("http://localhost:8080/FlightDataService?wsdl");
        QName SERVICE_NAME = new QName("http://example.com/", "FlightDataServiceImplService");

        // Create service instance
        javax.xml.ws.Service service = javax.xml.ws.Service.create(wsdlURL, SERVICE_NAME);
        FlightDataService port = service.getPort(FlightDataService.class);

        // Example usage
        String missionId = "MISSION_001";

        // Step 1: Read flight data
        System.out.println("Reading flight data...");
        TaskStatus readStatus = port.readFlightData(missionId);
        System.out.println("Task ID: " + readStatus.getTaskId());
        System.out.println("Status: " + readStatus.getStatus());

        // Poll until task completes
        while (!readStatus.getStatus().equals("COMPLETED") && !readStatus.getStatus().equals("FAILED")) {
            Thread.sleep(2000);
            readStatus = port.checkTaskStatus(readStatus.getTaskId());
            System.out.println("Progress: " + readStatus.getProgress() + "%");
        }

        if (readStatus.getStatus().equals("COMPLETED")) {
            // Get analysis ID from the response or logs
            String analysisId = "ANALYSIS_MISSION_001_" + System.currentTimeMillis();

            // Step 2: Retrieve and display results
            System.out.println("\nRetrieving analysis results...");
            AnalysisResults results = port.getAnalysisResults(analysisId);

            System.out.println("Analysis ID: " + results.getAnalysisId());
            System.out.println("Timestamp: " + results.getTimestamp());
            System.out.println("Parameters analyzed:");
            for (String param : results.getParameters()) {
                System.out.println("- " + param);
            }
            System.out.println("Metric values:");
            for (Double metric : results.getMetrics()) {
                System.out.println("- " + metric);
            }

            // Step 3: Save updated results
            System.out.println("\nSaving updated results...");
            results.setStatus("VERIFIED");
            TaskStatus saveStatus = port.saveAnalysisResults(analysisId, results);

            // Wait for save to complete
            while (!saveStatus.getStatus().equals("COMPLETED") && !saveStatus.getStatus().equals("FAILED")) {
                Thread.sleep(2000);
                saveStatus = port.checkTaskStatus(saveStatus.getTaskId());
                System.out.println("Save progress: " + saveStatus.getProgress() + "%");
            }

            if (saveStatus.getStatus().equals("COMPLETED")) {
                System.out.println("Results successfully saved!");
            }
        } else {
            System.out.println("Failed to process flight data: " + readStatus.getMessage());
        }
    }
}

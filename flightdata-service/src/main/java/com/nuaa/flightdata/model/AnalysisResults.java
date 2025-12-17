package com.nuaa.flightdata.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;
import java.util.List;

@XmlRootElement(name = "AnalysisResults")
@XmlType(propOrder = {"analysisId", "timestamp", "parameters", "metrics", "status"})
public class AnalysisResults {
    private String analysisId;
    private Date timestamp;
    private List<String> parameters;
    private List<Double> metrics;
    private String status;

    // Default constructor
    public AnalysisResults() {
    }

    // Getters and setters
    public String getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(String analysisId) {
        this.analysisId = analysisId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

    public List<Double> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<Double> metrics) {
        this.metrics = metrics;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
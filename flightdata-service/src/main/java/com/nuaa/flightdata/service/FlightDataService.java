package com.nuaa.flightdata.service;

import com.nuaa.flightdata.model.AnalysisResults;
import com.nuaa.flightdata.model.TaskStatus;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.Action;

@WebService(targetNamespace = "http://example.com/flightdata")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface FlightDataService {

    /**
     * Reads flight test data from source
     *
     * @param missionId the unique identifier of the flight mission
     * @return TaskStatus containing operation result and status
     */
    @WebMethod(operationName = "ReadFlightData")
    @Action(input = "http://example.com/flightdata/ReadFlightData")
    @WebResult(name = "taskStatus")
    TaskStatus readFlightData(@WebParam(name = "missionId") String missionId);

    /**
     * Saves analysis results to persistent storage
     *
     * @param analysisId the unique identifier of the analysis
     * @param results    the analysis results to save
     * @return TaskStatus containing operation result and status
     */
    @WebMethod(operationName = "SaveAnalysisResults")
    @Action(input = "http://example.com/flightdata/SaveAnalysisResults")
    @WebResult(name = "taskStatus")
    TaskStatus saveAnalysisResults(
            @WebParam(name = "analysisId") String analysisId,
            @WebParam(name = "results") AnalysisResults results
    );

    /**
     * Retrieves saved analysis results
     *
     * @param analysisId the unique identifier of the analysis
     * @return AnalysisResults containing the requested data
     */
    @WebMethod(operationName = "GetAnalysisResults")
    @Action(input = "http://example.com/flightdata/GetAnalysisResults")
    @WebResult(name = "results")
    AnalysisResults getAnalysisResults(@WebParam(name = "analysisId") String analysisId);

    /**
     * Checks the status of an ongoing task
     *
     * @param taskId the unique identifier of the task
     * @return TaskStatus containing current status information
     */
    @WebMethod(operationName = "CheckTaskStatus")
    @Action(input = "http://example.com/flightdata/CheckTaskStatus")
    @WebResult(name = "taskStatus")
    TaskStatus checkTaskStatus(@WebParam(name = "taskId") String taskId);
}

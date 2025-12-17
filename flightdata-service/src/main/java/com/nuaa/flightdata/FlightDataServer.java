package com.nuaa.flightdata;

import com.nuaa.flightdata.service.impl.FlightDataServiceImpl;
import javax.xml.ws.Endpoint;

public class FlightDataServer {
    public static void main(String[] args) {
        System.setProperty("javax.xml.ws.spi.Provider", "com.sun.xml.ws.spi.ProviderImpl");

        FlightDataServiceImpl implementor = new FlightDataServiceImpl();

        // Auto-generate WSDL at http://localhost:8080/FlightDataService?wsdl
        Endpoint.publish("http://localhost:8080/FlightDataService", implementor);

        System.out.println("FlightDataService started successfully!");
        System.out.println("Access WSDL at: http://localhost:8080/FlightDataService?wsdl");
    }
}


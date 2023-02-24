/**
 * GlobalWeather_Port.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.capeclear.www.GlobalWeather_wsdl;

import java.rmi.Remote;
import java.rmi.RemoteException;
import com.capeclear.www.GlobalWeather_xsd.WeatherReport;

public interface GlobalWeather_Port extends Remote {
    public WeatherReport getWeatherReport(String code) throws RemoteException;
}

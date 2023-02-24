/**
 * GlobalWeather_Service.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.capeclear.www.GlobalWeather_wsdl;

import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

import java.net.URL;

public interface GlobalWeather_Service extends Service {

    // GlobalWeather
    public String getGlobalWeatherAddress();

    public GlobalWeather_Port getGlobalWeather() throws ServiceException;

    public GlobalWeather_Port getGlobalWeather(java.net.URL portAddress) throws ServiceException;
    public String getStationInfoAddress();

    public StationInfo getStationInfo() throws ServiceException;

    public StationInfo getStationInfo(URL portAddress) throws ServiceException;
}

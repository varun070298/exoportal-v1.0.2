/**
 * GlobalWeather_ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.capeclear.www.GlobalWeather_wsdl;

import javax.xml.rpc.ServiceException;
import javax.xml.namespace.QName;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Remote;

public class GlobalWeather_ServiceLocator extends org.apache.axis.client.Service implements GlobalWeather_Service {

    // GlobalWeather

    // Use to get a proxy class for GlobalWeather
    private final String GlobalWeather_address = "http://live.capescience.com:80/ccx/GlobalWeather";

    public String getGlobalWeatherAddress() {
        return GlobalWeather_address;
    }

    // The WSDD service name defaults to the port name.
    private String GlobalWeatherWSDDServiceName = "GlobalWeather";

    public String getGlobalWeatherWSDDServiceName() {
        return GlobalWeatherWSDDServiceName;
    }

    public void setGlobalWeatherWSDDServiceName(String name) {
        GlobalWeatherWSDDServiceName = name;
    }

    public GlobalWeather_Port getGlobalWeather() throws ServiceException {
       URL endpoint;
        try {
            endpoint = new java.net.URL(GlobalWeather_address);
        }
        catch (MalformedURLException e) {
            throw new ServiceException(e);
        }
        return getGlobalWeather(endpoint);
    }

    public GlobalWeather_Port getGlobalWeather(URL portAddress) throws ServiceException {
        try {
            GlobalWeatherStub _stub = new GlobalWeatherStub(portAddress, this);
            _stub.setPortName(getGlobalWeatherWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }


    // Use to get a proxy class for StationInfo
    private final String StationInfo_address = "http://live.capescience.com:80/ccx/GlobalWeather";

    public String getStationInfoAddress() {
        return StationInfo_address;
    }

    // The WSDD service name defaults to the port name.
    private String StationInfoWSDDServiceName = "StationInfo";

    public String getStationInfoWSDDServiceName() {
        return StationInfoWSDDServiceName;
    }

    public void setStationInfoWSDDServiceName(String name) {
        StationInfoWSDDServiceName = name;
    }

    public StationInfo getStationInfo() throws ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new URL(StationInfo_address);
        }
        catch (MalformedURLException e) {
            throw new ServiceException(e);
        }
        return getStationInfo(endpoint);
    }

    public StationInfo getStationInfo(URL portAddress) throws ServiceException {
        try {
            StationInfoStub _stub = new StationInfoStub(portAddress, this);
            _stub.setPortName(getStationInfoWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public Remote getPort(Class serviceEndpointInterface) throws ServiceException {
        try {
            if (GlobalWeather_Port.class.isAssignableFrom(serviceEndpointInterface)) {
                GlobalWeatherStub _stub = new GlobalWeatherStub(new URL(GlobalWeather_address), this);
                _stub.setPortName(getGlobalWeatherWSDDServiceName());
                return _stub;
            }
            if (StationInfo.class.isAssignableFrom(serviceEndpointInterface)) {
                StationInfoStub _stub = new StationInfoStub(new URL(StationInfo_address), this);
                _stub.setPortName(getStationInfoWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new ServiceException(t);
        }
        throw new ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public Remote getPort(QName portName, Class serviceEndpointInterface) throws ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        String inputPortName = portName.getLocalPart();
        if ("GlobalWeather".equals(inputPortName)) {
            return getGlobalWeather();
        }
        else if ("StationInfo".equals(inputPortName)) {
            return getStationInfo();
        }
        else  {
            Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public QName getServiceName() {
        return new QName("http://www.capeclear.com/GlobalWeather.wsdl", "GlobalWeather");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new QName("GlobalWeather"));
            ports.add(new QName("StationInfo"));
        }
        return ports.iterator();
    }

}

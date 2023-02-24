/**
 * StationInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.capeclear.www.GlobalWeather_wsdl;

import java.rmi.Remote;
import java.rmi.RemoteException;
import com.capeclear.www.GlobalWeather_xsd.Station;

public interface StationInfo extends Remote {
    public Station getStation(String code) throws RemoteException;
    public boolean isValidCode(String code) throws RemoteException;
    public String[] listCountries() throws RemoteException;
    public Station[] searchByCode(String code) throws RemoteException;
    public Station[] searchByCountry(String country) throws RemoteException;
    public Station[] searchByName(String name) throws RemoteException;
    public Station[] searchByRegion(String region) throws RemoteException;
}

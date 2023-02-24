/**
 * StationInfoStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.capeclear.www.GlobalWeather_wsdl;

import java.util.Enumeration;
import java.util.Vector;
import java.net.URL;
import java.rmi.RemoteException;
import javax.xml.namespace.QName;
import javax.xml.rpc.Service;

import com.capeclear.www.GlobalWeather_xsd.Station;

public class StationInfoStub extends org.apache.axis.client.Stub implements com.capeclear.www.GlobalWeather_wsdl.StationInfo {
    private Vector cachedSerClasses = new Vector();
    private Vector cachedSerQNames = new Vector();
    private Vector cachedSerFactories = new Vector();
    private Vector cachedDeserFactories = new Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[7];
        org.apache.axis.description.OperationDesc oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getStation");
        oper.addParameter(new QName("", "code"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new QName("http://www.capeclear.com/GlobalWeather.xsd", "Station"));
        oper.setReturnClass(Station.class);
        oper.setReturnQName(new QName("", "return"));
        oper.setStyle(org.apache.axis.enum.Style.RPC);
        oper.setUse(org.apache.axis.enum.Use.ENCODED);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("isValidCode");
        oper.addParameter(new QName("", "code"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        oper.setReturnClass(boolean.class);
        oper.setReturnQName(new QName("", "return"));
        oper.setStyle(org.apache.axis.enum.Style.RPC);
        oper.setUse(org.apache.axis.enum.Use.ENCODED);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("listCountries");
        oper.setReturnType(new QName("http://www.capeclear.com/GlobalWeather.xsd", "ArrayOfstring"));
        oper.setReturnClass(String[].class);
        oper.setReturnQName(new QName("", "return"));
        oper.setStyle(org.apache.axis.enum.Style.RPC);
        oper.setUse(org.apache.axis.enum.Use.ENCODED);
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("searchByCode");
        oper.addParameter(new QName("", "code"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new QName("http://www.capeclear.com/GlobalWeather.xsd", "ArrayOfStation"));
        oper.setReturnClass(Station[].class);
        oper.setReturnQName(new QName("", "return"));
        oper.setStyle(org.apache.axis.enum.Style.RPC);
        oper.setUse(org.apache.axis.enum.Use.ENCODED);
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("searchByCountry");
        oper.addParameter(new QName("", "country"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new QName("http://www.capeclear.com/GlobalWeather.xsd", "ArrayOfStation"));
        oper.setReturnClass(Station[].class);
        oper.setReturnQName(new QName("", "return"));
        oper.setStyle(org.apache.axis.enum.Style.RPC);
        oper.setUse(org.apache.axis.enum.Use.ENCODED);
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("searchByName");
        oper.addParameter(new QName("", "name"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new QName("http://www.capeclear.com/GlobalWeather.xsd", "ArrayOfStation"));
        oper.setReturnClass(Station[].class);
        oper.setReturnQName(new QName("", "return"));
        oper.setStyle(org.apache.axis.enum.Style.RPC);
        oper.setUse(org.apache.axis.enum.Use.ENCODED);
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("searchByRegion");
        oper.addParameter(new QName("", "region"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new QName("http://www.capeclear.com/GlobalWeather.xsd", "ArrayOfStation"));
        oper.setReturnClass(Station[].class);
        oper.setReturnQName(new QName("", "return"));
        oper.setStyle(org.apache.axis.enum.Style.RPC);
        oper.setUse(org.apache.axis.enum.Use.ENCODED);
        _operations[6] = oper;

    }

    public StationInfoStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public StationInfoStub(URL endpointURL, Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public StationInfoStub(Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
            Class cls;
            QName qName;
            Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            qName = new QName("http://www.capeclear.com/GlobalWeather.xsd", "ArrayOfstring");
            cachedSerQNames.add(qName);
            cls = String[].class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(arraysf);
            cachedDeserFactories.add(arraydf);

            qName = new QName("http://www.capeclear.com/GlobalWeather.xsd", "Station");
            cachedSerQNames.add(qName);
            cls = Station.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new QName("http://www.capeclear.com/GlobalWeather.xsd", "ArrayOfStation");
            cachedSerQNames.add(qName);
            cls = Station[].class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(arraysf);
            cachedDeserFactories.add(arraydf);

    }

    private org.apache.axis.client.Call createCall() throws RemoteException {
        try {
            org.apache.axis.client.Call _call =
                    (org.apache.axis.client.Call) super.service.createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
                    _call.setEncodingStyle(org.apache.axis.Constants.URI_SOAP11_ENC);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        Class cls = (Class) cachedSerClasses.get(i);
                        QName qName = (QName) cachedSerQNames.get(i);
                        Class sf = (Class) cachedSerFactories.get(i);
                        Class df = (Class) cachedDeserFactories.get(i);
                        _call.registerTypeMapping(cls, qName, sf, df, false);
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", t);
        }
    }

    public Station getStation(String code) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("capeconnect:GlobalWeather:StationInfo#getStation");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new QName("capeconnect:GlobalWeather:StationInfo", "getStation"));

        setRequestHeaders(_call);
        setAttachments(_call);
        Object _resp = _call.invoke(new Object[] {code});

        if (_resp instanceof RemoteException) {
            throw (RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (Station) _resp;
            } catch (java.lang.Exception _exception) {
                return (Station) org.apache.axis.utils.JavaUtils.convert(_resp, Station.class);
            }
        }
    }

    public boolean isValidCode(String code) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("capeconnect:GlobalWeather:StationInfo#isValidCode");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new QName("capeconnect:GlobalWeather:StationInfo", "isValidCode"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new Object[] {code});

        if (_resp instanceof RemoteException) {
            throw (RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return ((java.lang.Boolean) _resp).booleanValue();
            } catch (Exception _exception) {
                return ((java.lang.Boolean) org.apache.axis.utils.JavaUtils.convert(_resp, boolean.class)).booleanValue();
            }
        }
    }

    public String[] listCountries() throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("capeconnect:GlobalWeather:StationInfo#listCountries");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new QName("capeconnect:GlobalWeather:StationInfo", "listCountries"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new Object[] {});

        if (_resp instanceof RemoteException) {
            throw (RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (String[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (String[]) org.apache.axis.utils.JavaUtils.convert(_resp, String[].class);
            }
        }
    }

    public Station[] searchByCode(String code) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("capeconnect:GlobalWeather:StationInfo#searchByCode");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new QName("capeconnect:GlobalWeather:StationInfo", "searchByCode"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {code});

        if (_resp instanceof RemoteException) {
            throw (RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (Station[]) _resp;
            } catch (Exception _exception) {
                return (Station[]) org.apache.axis.utils.JavaUtils.convert(_resp, Station[].class);
            }
        }
    }

    public Station[] searchByCountry(String country) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("capeconnect:GlobalWeather:StationInfo#searchByCountry");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new QName("capeconnect:GlobalWeather:StationInfo", "searchByCountry"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {country});

        if (_resp instanceof RemoteException) {
            throw (RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (Station[]) _resp;
            } catch (Exception _exception) {
                return (Station[]) org.apache.axis.utils.JavaUtils.convert(_resp, Station[].class);
            }
        }
    }

    public Station[] searchByName(String name) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("capeconnect:GlobalWeather:StationInfo#searchByName");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new QName("capeconnect:GlobalWeather:StationInfo", "searchByName"));

        setRequestHeaders(_call);
        setAttachments(_call);
        Object _resp = _call.invoke(new Object[] {name});

        if (_resp instanceof RemoteException) {
            throw (RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (Station[]) _resp;
            } catch (Exception _exception) {
                return (Station[]) org.apache.axis.utils.JavaUtils.convert(_resp, Station[].class);
            }
        }
    }

    public Station[] searchByRegion(String region) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("capeconnect:GlobalWeather:StationInfo#searchByRegion");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new QName("capeconnect:GlobalWeather:StationInfo", "searchByRegion"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new Object[] {region});

        if (_resp instanceof RemoteException) {
            throw (RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (Station[]) _resp;
            } catch (Exception _exception) {
                return (Station[]) org.apache.axis.utils.JavaUtils.convert(_resp, Station[].class);
            }
        }
    }
}

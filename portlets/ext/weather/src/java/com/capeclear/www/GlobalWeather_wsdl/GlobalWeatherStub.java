/**
 * GlobalWeatherStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.capeclear.www.GlobalWeather_wsdl;

import java.util.Vector;
import java.rmi.RemoteException;

import javax.xml.namespace.QName;
import javax.xml.rpc.Service;

import com.capeclear.www.GlobalWeather_xsd.*;

public class GlobalWeatherStub extends org.apache.axis.client.Stub implements GlobalWeather_Port {
    private Vector cachedSerClasses = new Vector();
    private Vector cachedSerQNames = new Vector();
    private Vector cachedSerFactories = new Vector();
    private Vector cachedDeserFactories = new Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[1];
        org.apache.axis.description.OperationDesc oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getWeatherReport");
        oper.addParameter(new QName("", "code"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new QName("http://www.capeclear.com/GlobalWeather.xsd", "WeatherReport"));
        oper.setReturnClass(WeatherReport.class);
        oper.setReturnQName(new QName("", "return"));
        oper.setStyle(org.apache.axis.enum.Style.RPC);
        oper.setUse(org.apache.axis.enum.Use.ENCODED);
        _operations[0] = oper;

    }

    public GlobalWeatherStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public GlobalWeatherStub(java.net.URL endpointURL, Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public GlobalWeatherStub(Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
            java.lang.Class cls;
            QName qName;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            qName = new QName("http://www.capeclear.com/GlobalWeather.xsd", "Precipitation");
            cachedSerQNames.add(qName);
            cls = Precipitation.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new QName("http://www.capeclear.com/GlobalWeather.xsd", "WeatherReport");
            cachedSerQNames.add(qName);
            cls = WeatherReport.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new QName("http://www.capeclear.com/GlobalWeather.xsd", "PhenomenonType");
            cachedSerQNames.add(qName);
            cls = PhenomenonType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new QName("http://www.capeclear.com/GlobalWeather.xsd", "Pressure");
            cachedSerQNames.add(qName);
            cls = Pressure.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new QName("http://www.capeclear.com/GlobalWeather.xsd", "Phenomenon");
            cachedSerQNames.add(qName);
            cls = Phenomenon.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new QName("http://www.capeclear.com/GlobalWeather.xsd", "Direction");
            cachedSerQNames.add(qName);
            cls = Direction.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new QName("http://www.capeclear.com/GlobalWeather.xsd", "Station");
            cachedSerQNames.add(qName);
            cls = Station.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new QName("http://www.capeclear.com/GlobalWeather.xsd", "Visibility");
            cachedSerQNames.add(qName);
            cls = Visibility.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new QName("http://www.capeclear.com/GlobalWeather.xsd", "ArrayOfPhenomenon");
            cachedSerQNames.add(qName);
            cls = Phenomenon[].class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(arraysf);
            cachedDeserFactories.add(arraydf);

            qName = new QName("http://www.capeclear.com/GlobalWeather.xsd", "ExtremeType");
            cachedSerQNames.add(qName);
            cls = ExtremeType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new QName("http://www.capeclear.com/GlobalWeather.xsd", "ArrayOfLayer");
            cachedSerQNames.add(qName);
            cls = Layer[].class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(arraysf);
            cachedDeserFactories.add(arraydf);

            qName = new QName("http://www.capeclear.com/GlobalWeather.xsd", "ArrayOfPrecipitation");
            cachedSerQNames.add(qName);
            cls = Precipitation[].class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(arraysf);
            cachedDeserFactories.add(arraydf);

            qName = new QName("http://www.capeclear.com/GlobalWeather.xsd", "ArrayOfExtreme");
            cachedSerQNames.add(qName);
            cls = Extreme[].class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(arraysf);
            cachedDeserFactories.add(arraydf);

            qName = new QName("http://www.capeclear.com/GlobalWeather.xsd", "VisibilityQualifier");
            cachedSerQNames.add(qName);
            cls = VisibilityQualifier.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new QName("http://www.capeclear.com/GlobalWeather.xsd", "Sky");
            cachedSerQNames.add(qName);
            cls = Sky.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new QName("http://www.capeclear.com/GlobalWeather.xsd", "Temperature");
            cachedSerQNames.add(qName);
            cls = Temperature.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new QName("http://www.capeclear.com/GlobalWeather.xsd", "PhenomenonIntensity");
            cachedSerQNames.add(qName);
            cls = PhenomenonIntensity.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new QName("http://www.capeclear.com/GlobalWeather.xsd", "Extreme");
            cachedSerQNames.add(qName);
            cls = Extreme.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new QName("http://www.capeclear.com/GlobalWeather.xsd", "Layer");
            cachedSerQNames.add(qName);
            cls = Layer.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new QName("http://www.capeclear.com/GlobalWeather.xsd", "DirectionCompass");
            cachedSerQNames.add(qName);
            cls = DirectionCompass.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new QName("http://www.capeclear.com/GlobalWeather.xsd", "Wind");
            cachedSerQNames.add(qName);
            cls = Wind.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

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
            java.util.Enumeration keys = super.cachedProperties.keys();
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
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        QName qName = (QName) cachedSerQNames.get(i);
                        Class sf = (java.lang.Class) cachedSerFactories.get(i);
                        Class df = (java.lang.Class) cachedDeserFactories.get(i);
                        _call.registerTypeMapping(cls, qName, sf, df, false);
                    }
                }
            }
            return _call;
        }
        catch (Throwable t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", t);
        }
    }

    public WeatherReport getWeatherReport(String code) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("capeconnect:GlobalWeather:GlobalWeather#getWeatherReport");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new QName("capeconnect:GlobalWeather:GlobalWeather", "getWeatherReport"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new Object[] {code});

        if (_resp instanceof RemoteException) {
            throw (RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (WeatherReport) _resp;
            } catch (Exception _exception) {
                return (WeatherReport) org.apache.axis.utils.JavaUtils.convert(_resp, WeatherReport.class);
            }
        }
    }

}

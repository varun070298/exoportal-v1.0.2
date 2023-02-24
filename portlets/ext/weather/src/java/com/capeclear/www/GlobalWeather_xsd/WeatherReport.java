/**
 * WeatherReport.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.capeclear.www.GlobalWeather_xsd;

import java.io.Serializable;
import javax.xml.namespace.QName;
import java.util.Calendar;

public class WeatherReport  implements Serializable {
    private Calendar timestamp;
    private Station station;
    private Phenomenon[] phenomena;
    private Precipitation[] precipitation;
    private Extreme[] extremes;
    private Pressure pressure;
    private Sky sky;
    private Temperature temperature;
    private Visibility visibility;
    private Wind wind;

    public WeatherReport() {
    }

    public Calendar getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Calendar timestamp) {
        this.timestamp = timestamp;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public Phenomenon[] getPhenomena() {
        return phenomena;
    }

    public void setPhenomena(Phenomenon[] phenomena) {
        this.phenomena = phenomena;
    }

    public Precipitation[] getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(Precipitation[] precipitation) {
        this.precipitation = precipitation;
    }

    public Extreme[] getExtremes() {
        return extremes;
    }

    public void setExtremes(Extreme[] extremes) {
        this.extremes = extremes;
    }

    public Pressure getPressure() {
        return pressure;
    }

    public void setPressure(Pressure pressure) {
        this.pressure = pressure;
    }

    public Sky getSky() {
        return sky;
    }

    public void setSky(Sky sky) {
        this.sky = sky;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof WeatherReport)) return false;
        WeatherReport other = (WeatherReport) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.timestamp==null && other.getTimestamp()==null) || 
             (this.timestamp!=null &&
              this.timestamp.equals(other.getTimestamp()))) &&
            ((this.station==null && other.getStation()==null) || 
             (this.station!=null &&
              this.station.equals(other.getStation()))) &&
            ((this.phenomena==null && other.getPhenomena()==null) || 
             (this.phenomena!=null &&
              java.util.Arrays.equals(this.phenomena, other.getPhenomena()))) &&
            ((this.precipitation==null && other.getPrecipitation()==null) || 
             (this.precipitation!=null &&
              java.util.Arrays.equals(this.precipitation, other.getPrecipitation()))) &&
            ((this.extremes==null && other.getExtremes()==null) || 
             (this.extremes!=null &&
              java.util.Arrays.equals(this.extremes, other.getExtremes()))) &&
            ((this.pressure==null && other.getPressure()==null) || 
             (this.pressure!=null &&
              this.pressure.equals(other.getPressure()))) &&
            ((this.sky==null && other.getSky()==null) || 
             (this.sky!=null &&
              this.sky.equals(other.getSky()))) &&
            ((this.temperature==null && other.getTemperature()==null) || 
             (this.temperature!=null &&
              this.temperature.equals(other.getTemperature()))) &&
            ((this.visibility==null && other.getVisibility()==null) || 
             (this.visibility!=null &&
              this.visibility.equals(other.getVisibility()))) &&
            ((this.wind==null && other.getWind()==null) || 
             (this.wind!=null &&
              this.wind.equals(other.getWind())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getTimestamp() != null) {
            _hashCode += getTimestamp().hashCode();
        }
        if (getStation() != null) {
            _hashCode += getStation().hashCode();
        }
        if (getPhenomena() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getPhenomena());
                 i++) {
                Object obj = java.lang.reflect.Array.get(getPhenomena(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getPrecipitation() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getPrecipitation());
                 i++) {
                Object obj = java.lang.reflect.Array.get(getPrecipitation(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getExtremes() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getExtremes());
                 i++) {
                Object obj = java.lang.reflect.Array.get(getExtremes(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getPressure() != null) {
            _hashCode += getPressure().hashCode();
        }
        if (getSky() != null) {
            _hashCode += getSky().hashCode();
        }
        if (getTemperature() != null) {
            _hashCode += getTemperature().hashCode();
        }
        if (getVisibility() != null) {
            _hashCode += getVisibility().hashCode();
        }
        if (getWind() != null) {
            _hashCode += getWind().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(WeatherReport.class);

    static {
        typeDesc.setXmlType(new QName("http://www.capeclear.com/GlobalWeather.xsd", "WeatherReport"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timestamp");
        elemField.setXmlName(new QName("", "timestamp"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("station");
        elemField.setXmlName(new QName("", "station"));
        elemField.setXmlType(new QName("http://www.capeclear.com/GlobalWeather.xsd", "Station"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("phenomena");
        elemField.setXmlName(new QName("", "phenomena"));
        elemField.setXmlType(new QName("http://www.capeclear.com/GlobalWeather.xsd", "Phenomenon"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("precipitation");
        elemField.setXmlName(new QName("", "precipitation"));
        elemField.setXmlType(new QName("http://www.capeclear.com/GlobalWeather.xsd", "Precipitation"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("extremes");
        elemField.setXmlName(new QName("", "extremes"));
        elemField.setXmlType(new QName("http://www.capeclear.com/GlobalWeather.xsd", "Extreme"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pressure");
        elemField.setXmlName(new QName("", "pressure"));
        elemField.setXmlType(new QName("http://www.capeclear.com/GlobalWeather.xsd", "Pressure"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sky");
        elemField.setXmlName(new QName("", "sky"));
        elemField.setXmlType(new QName("http://www.capeclear.com/GlobalWeather.xsd", "Sky"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("temperature");
        elemField.setXmlName(new QName("", "temperature"));
        elemField.setXmlType(new QName("http://www.capeclear.com/GlobalWeather.xsd", "Temperature"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("visibility");
        elemField.setXmlName(new QName("", "visibility"));
        elemField.setXmlType(new QName("http://www.capeclear.com/GlobalWeather.xsd", "Visibility"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("wind");
        elemField.setXmlName(new QName("", "wind"));
        elemField.setXmlType(new QName("http://www.capeclear.com/GlobalWeather.xsd", "Wind"));
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           String mechType, 
           Class _javaType,  
           QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           String mechType, 
           Class _javaType,  
           QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}

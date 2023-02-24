/**
 * Wind.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.capeclear.www.GlobalWeather_xsd;

import java.io.Serializable;
import javax.xml.namespace.QName;

public class Wind  implements Serializable {
    private double prevailing_speed;
    private double gust_speed;
    private Direction prevailing_direction;
    private Direction varying_from_direction;
    private Direction varying_to_direction;
    private String string;

    public Wind() {
    }

    public double getPrevailing_speed() {
        return prevailing_speed;
    }

    public void setPrevailing_speed(double prevailing_speed) {
        this.prevailing_speed = prevailing_speed;
    }

    public double getGust_speed() {
        return gust_speed;
    }

    public void setGust_speed(double gust_speed) {
        this.gust_speed = gust_speed;
    }

    public Direction getPrevailing_direction() {
        return prevailing_direction;
    }

    public void setPrevailing_direction(Direction prevailing_direction) {
        this.prevailing_direction = prevailing_direction;
    }

    public Direction getVarying_from_direction() {
        return varying_from_direction;
    }

    public void setVarying_from_direction(Direction varying_from_direction) {
        this.varying_from_direction = varying_from_direction;
    }

    public Direction getVarying_to_direction() {
        return varying_to_direction;
    }

    public void setVarying_to_direction(Direction varying_to_direction) {
        this.varying_to_direction = varying_to_direction;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Wind)) return false;
        Wind other = (Wind) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.prevailing_speed == other.getPrevailing_speed() &&
            this.gust_speed == other.getGust_speed() &&
            ((this.prevailing_direction==null && other.getPrevailing_direction()==null) || 
             (this.prevailing_direction!=null &&
              this.prevailing_direction.equals(other.getPrevailing_direction()))) &&
            ((this.varying_from_direction==null && other.getVarying_from_direction()==null) || 
             (this.varying_from_direction!=null &&
              this.varying_from_direction.equals(other.getVarying_from_direction()))) &&
            ((this.varying_to_direction==null && other.getVarying_to_direction()==null) || 
             (this.varying_to_direction!=null &&
              this.varying_to_direction.equals(other.getVarying_to_direction()))) &&
            ((this.string==null && other.getString()==null) || 
             (this.string!=null &&
              this.string.equals(other.getString())));
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
        _hashCode += new Double(getPrevailing_speed()).hashCode();
        _hashCode += new Double(getGust_speed()).hashCode();
        if (getPrevailing_direction() != null) {
            _hashCode += getPrevailing_direction().hashCode();
        }
        if (getVarying_from_direction() != null) {
            _hashCode += getVarying_from_direction().hashCode();
        }
        if (getVarying_to_direction() != null) {
            _hashCode += getVarying_to_direction().hashCode();
        }
        if (getString() != null) {
            _hashCode += getString().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Wind.class);

    static {
        typeDesc.setXmlType(new QName("http://www.capeclear.com/GlobalWeather.xsd", "Wind"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("prevailing_speed");
        elemField.setXmlName(new QName("", "prevailing_speed"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gust_speed");
        elemField.setXmlName(new QName("", "gust_speed"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("prevailing_direction");
        elemField.setXmlName(new QName("", "prevailing_direction"));
        elemField.setXmlType(new QName("http://www.capeclear.com/GlobalWeather.xsd", "Direction"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("varying_from_direction");
        elemField.setXmlName(new QName("", "varying_from_direction"));
        elemField.setXmlType(new QName("http://www.capeclear.com/GlobalWeather.xsd", "Direction"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("varying_to_direction");
        elemField.setXmlName(new QName("", "varying_to_direction"));
        elemField.setXmlType(new QName("http://www.capeclear.com/GlobalWeather.xsd", "Direction"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("string");
        elemField.setXmlName(new QName("", "string"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
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

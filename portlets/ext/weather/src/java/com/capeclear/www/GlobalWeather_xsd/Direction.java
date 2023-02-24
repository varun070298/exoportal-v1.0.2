/**
 * Direction.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.capeclear.www.GlobalWeather_xsd;

import java.io.Serializable;
import javax.xml.namespace.QName;

public class Direction  implements Serializable {
    private DirectionCompass compass;
    private int degrees;
    private String string;

    public Direction() {
    }

    public DirectionCompass getCompass() {
        return compass;
    }

    public void setCompass(DirectionCompass compass) {
        this.compass = compass;
    }

    public int getDegrees() {
        return degrees;
    }

    public void setDegrees(int degrees) {
        this.degrees = degrees;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Direction)) return false;
        Direction other = (Direction) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.compass==null && other.getCompass()==null) || 
             (this.compass!=null &&
              this.compass.equals(other.getCompass()))) &&
            this.degrees == other.getDegrees() &&
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
        if (getCompass() != null) {
            _hashCode += getCompass().hashCode();
        }
        _hashCode += getDegrees();
        if (getString() != null) {
            _hashCode += getString().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Direction.class);

    static {
        typeDesc.setXmlType(new QName("http://www.capeclear.com/GlobalWeather.xsd", "Direction"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("compass");
        elemField.setXmlName(new QName("", "compass"));
        elemField.setXmlType(new QName("http://www.capeclear.com/GlobalWeather.xsd", "DirectionCompass"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("degrees");
        elemField.setXmlName(new QName("", "degrees"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
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

/**
 * Sky.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.capeclear.www.GlobalWeather_xsd;

import java.io.Serializable;
import javax.xml.namespace.QName;

public class Sky  implements Serializable {
    private double ceiling_altitude;
    private Layer[] layers;
    private String string;

    public Sky() {
    }

    public double getCeiling_altitude() {
        return ceiling_altitude;
    }

    public void setCeiling_altitude(double ceiling_altitude) {
        this.ceiling_altitude = ceiling_altitude;
    }

    public com.capeclear.www.GlobalWeather_xsd.Layer[] getLayers() {
        return layers;
    }

    public void setLayers(com.capeclear.www.GlobalWeather_xsd.Layer[] layers) {
        this.layers = layers;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Sky)) return false;
        Sky other = (Sky) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.ceiling_altitude == other.getCeiling_altitude() &&
            ((this.layers==null && other.getLayers()==null) || 
             (this.layers!=null &&
              java.util.Arrays.equals(this.layers, other.getLayers()))) &&
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
        _hashCode += new Double(getCeiling_altitude()).hashCode();
        if (getLayers() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getLayers());
                 i++) {
                Object obj = java.lang.reflect.Array.get(getLayers(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getString() != null) {
            _hashCode += getString().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Sky.class);

    static {
        typeDesc.setXmlType(new QName("http://www.capeclear.com/GlobalWeather.xsd", "Sky"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ceiling_altitude");
        elemField.setXmlName(new QName("", "ceiling_altitude"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("layers");
        elemField.setXmlName(new QName("", "layers"));
        elemField.setXmlType(new QName("http://www.capeclear.com/GlobalWeather.xsd", "Layer"));
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

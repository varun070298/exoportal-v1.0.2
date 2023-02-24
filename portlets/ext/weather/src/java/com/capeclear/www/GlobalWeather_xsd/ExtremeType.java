/**
 * ExtremeType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.capeclear.www.GlobalWeather_xsd;

import java.io.Serializable;
import java.util.HashMap;

public class ExtremeType implements Serializable {
    private String _value_;
    private static HashMap _table_ = new HashMap();

    // Constructor
    protected ExtremeType(String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final String _HIGH = "HIGH";
    public static final String _LOW = "LOW";
    public static final ExtremeType HIGH = new ExtremeType(_HIGH);
    public static final ExtremeType LOW = new ExtremeType(_LOW);
    public String getValue() { return _value_;}
    public static ExtremeType fromValue(String value)
          throws IllegalStateException {
        ExtremeType enum = (ExtremeType)
            _table_.get(value);
        if (enum==null) throw new IllegalStateException();
        return enum;
    }
    public static ExtremeType fromString(String value)
          throws IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public String toString() { return _value_;}
    public Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
}

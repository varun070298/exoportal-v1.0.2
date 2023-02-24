/**
 * VisibilityQualifier.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.capeclear.www.GlobalWeather_xsd;

import java.io.Serializable;
import java.util.HashMap;
import java.io.ObjectStreamException;

public class VisibilityQualifier implements Serializable {
    private String _value_;
    private static HashMap _table_ = new HashMap();

    // Constructor
    protected VisibilityQualifier(String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final String _AT = "AT";
    public static final String _BEYOND = "BEYOND";
    public static final String _BELOW = "BELOW";
    public static final VisibilityQualifier AT = new VisibilityQualifier(_AT);
    public static final VisibilityQualifier BEYOND = new VisibilityQualifier(_BEYOND);
    public static final VisibilityQualifier BELOW = new VisibilityQualifier(_BELOW);
    public String getValue() { return _value_;}
    public static VisibilityQualifier fromValue(String value)
          throws IllegalStateException {
        VisibilityQualifier enum = (VisibilityQualifier)
            _table_.get(value);
        if (enum==null) throw new IllegalStateException();
        return enum;
    }
    public static VisibilityQualifier fromString(String value)
          throws IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public String toString() { return _value_;}
    public Object readResolve() throws ObjectStreamException { return fromValue(_value_);}
}

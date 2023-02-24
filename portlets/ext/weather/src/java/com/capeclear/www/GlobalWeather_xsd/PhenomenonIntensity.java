/**
 * PhenomenonIntensity.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.capeclear.www.GlobalWeather_xsd;

import java.io.Serializable;
import java.util.HashMap;
import java.io.ObjectStreamException;

public class PhenomenonIntensity implements Serializable {
    private String _value_;
    private static HashMap _table_ = new HashMap();

    // Constructor
    protected PhenomenonIntensity(String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final String _DISTANT = "DISTANT";
    public static final String _NEARBY = "NEARBY";
    public static final String _LIGHT = "LIGHT";
    public static final String _MODERATE = "MODERATE";
    public static final String _HEAVY = "HEAVY";
    public static final PhenomenonIntensity DISTANT = new PhenomenonIntensity(_DISTANT);
    public static final PhenomenonIntensity NEARBY = new PhenomenonIntensity(_NEARBY);
    public static final PhenomenonIntensity LIGHT = new PhenomenonIntensity(_LIGHT);
    public static final PhenomenonIntensity MODERATE = new PhenomenonIntensity(_MODERATE);
    public static final PhenomenonIntensity HEAVY = new PhenomenonIntensity(_HEAVY);
    public String getValue() { return _value_;}
    public static PhenomenonIntensity fromValue(String value)
          throws IllegalStateException {
        PhenomenonIntensity enum = (PhenomenonIntensity)
            _table_.get(value);
        if (enum==null) throw new IllegalStateException();
        return enum;
    }
    public static PhenomenonIntensity fromString(String value)
          throws IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public String toString() { return _value_;}
    public Object readResolve() throws ObjectStreamException { return fromValue(_value_);}
}

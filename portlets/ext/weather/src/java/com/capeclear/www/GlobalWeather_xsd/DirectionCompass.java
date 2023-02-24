/**
 * DirectionCompass.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.capeclear.www.GlobalWeather_xsd;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.HashMap;

public class DirectionCompass implements Serializable {
    private String _value_;
    private static HashMap _table_ = new HashMap();

    // Constructor
    protected DirectionCompass(String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final String _N = "N";
    public static final String _NNE = "NNE";
    public static final String _NE = "NE";
    public static final String _ENE = "ENE";
    public static final String _E = "E";
    public static final String _ESE = "ESE";
    public static final String _SE = "SE";
    public static final String _SSE = "SSE";
    public static final String _S = "S";
    public static final String _SSW = "SSW";
    public static final String _SW = "SW";
    public static final String _WSW = "WSW";
    public static final String _W = "W";
    public static final String _WNW = "WNW";
    public static final String _NW = "NW";
    public static final String _NNW = "NNW";
    public static final DirectionCompass N = new DirectionCompass(_N);
    public static final DirectionCompass NNE = new DirectionCompass(_NNE);
    public static final DirectionCompass NE = new DirectionCompass(_NE);
    public static final DirectionCompass ENE = new DirectionCompass(_ENE);
    public static final DirectionCompass E = new DirectionCompass(_E);
    public static final DirectionCompass ESE = new DirectionCompass(_ESE);
    public static final DirectionCompass SE = new DirectionCompass(_SE);
    public static final DirectionCompass SSE = new DirectionCompass(_SSE);
    public static final DirectionCompass S = new DirectionCompass(_S);
    public static final DirectionCompass SSW = new DirectionCompass(_SSW);
    public static final DirectionCompass SW = new DirectionCompass(_SW);
    public static final DirectionCompass WSW = new DirectionCompass(_WSW);
    public static final DirectionCompass W = new DirectionCompass(_W);
    public static final DirectionCompass WNW = new DirectionCompass(_WNW);
    public static final DirectionCompass NW = new DirectionCompass(_NW);
    public static final DirectionCompass NNW = new DirectionCompass(_NNW);
    public String getValue() { return _value_;}
    public static DirectionCompass fromValue(String value)
          throws java.lang.IllegalStateException {
        DirectionCompass enum = (DirectionCompass)
            _table_.get(value);
        if (enum==null) throw new IllegalStateException();
        return enum;
    }
    public static DirectionCompass fromString(String value)
          throws IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public String toString() { return _value_;}
    public Object readResolve() throws ObjectStreamException { return fromValue(_value_);}
}

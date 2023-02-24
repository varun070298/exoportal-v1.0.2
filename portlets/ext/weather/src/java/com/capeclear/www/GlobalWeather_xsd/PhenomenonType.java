/**
 * PhenomenonType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.capeclear.www.GlobalWeather_xsd;

import java.io.Serializable;
import java.io.ObjectStreamException;

public class PhenomenonType implements Serializable {
    private String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected PhenomenonType(String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final String _MIST = "MIST";
    public static final String _FOG = "FOG";
    public static final String _CLOUD = "CLOUD";
    public static final String _TOWERING_CUMULUS = "TOWERING_CUMULUS";
    public static final String _CUMULONIMBUS = "CUMULONIMBUS";
    public static final String _PRECIPITATION = "PRECIPITATION";
    public static final String _SHOWERS = "SHOWERS";
    public static final String _DRIZZLE = "DRIZZLE";
    public static final String _RAIN = "RAIN";
    public static final String _SPRAY = "SPRAY";
    public static final String _DIAMOND_DUST = "DIAMOND_DUST";
    public static final String _SNOW = "SNOW";
    public static final String _SNOW_GRAINS = "SNOW_GRAINS";
    public static final String _ICE_PELLETS = "ICE_PELLETS";
    public static final String _SMALL_HAIL = "SMALL_HAIL";
    public static final String _LARGE_HAIL = "LARGE_HAIL";
    public static final String _HAZE = "HAZE";
    public static final String _SMOKE = "SMOKE";
    public static final String _DUST = "DUST";
    public static final String _SAND = "SAND";
    public static final String _VOLCANIC_ASH = "VOLCANIC_ASH";
    public static final String _WHIRLS = "WHIRLS";
    public static final String _SQUALLS = "SQUALLS";
    public static final String _LIGHTNING = "LIGHTNING";
    public static final String _DUSTSTORM = "DUSTSTORM";
    public static final String _SANDSTORM = "SANDSTORM";
    public static final String _THUNDERSTORM = "THUNDERSTORM";
    public static final String _TORNADIC = "TORNADIC";
    public static final String _UNKNOWN = "UNKNOWN";
    public static final PhenomenonType MIST = new PhenomenonType(_MIST);
    public static final PhenomenonType FOG = new PhenomenonType(_FOG);
    public static final PhenomenonType CLOUD = new PhenomenonType(_CLOUD);
    public static final PhenomenonType TOWERING_CUMULUS = new PhenomenonType(_TOWERING_CUMULUS);
    public static final PhenomenonType CUMULONIMBUS = new PhenomenonType(_CUMULONIMBUS);
    public static final PhenomenonType PRECIPITATION = new PhenomenonType(_PRECIPITATION);
    public static final PhenomenonType SHOWERS = new PhenomenonType(_SHOWERS);
    public static final PhenomenonType DRIZZLE = new PhenomenonType(_DRIZZLE);
    public static final PhenomenonType RAIN = new PhenomenonType(_RAIN);
    public static final PhenomenonType SPRAY = new PhenomenonType(_SPRAY);
    public static final PhenomenonType DIAMOND_DUST = new PhenomenonType(_DIAMOND_DUST);
    public static final PhenomenonType SNOW = new PhenomenonType(_SNOW);
    public static final PhenomenonType SNOW_GRAINS = new PhenomenonType(_SNOW_GRAINS);
    public static final PhenomenonType ICE_PELLETS = new PhenomenonType(_ICE_PELLETS);
    public static final PhenomenonType SMALL_HAIL = new PhenomenonType(_SMALL_HAIL);
    public static final PhenomenonType LARGE_HAIL = new PhenomenonType(_LARGE_HAIL);
    public static final PhenomenonType HAZE = new PhenomenonType(_HAZE);
    public static final PhenomenonType SMOKE = new PhenomenonType(_SMOKE);
    public static final PhenomenonType DUST = new PhenomenonType(_DUST);
    public static final PhenomenonType SAND = new PhenomenonType(_SAND);
    public static final PhenomenonType VOLCANIC_ASH = new PhenomenonType(_VOLCANIC_ASH);
    public static final PhenomenonType WHIRLS = new PhenomenonType(_WHIRLS);
    public static final PhenomenonType SQUALLS = new PhenomenonType(_SQUALLS);
    public static final PhenomenonType LIGHTNING = new PhenomenonType(_LIGHTNING);
    public static final PhenomenonType DUSTSTORM = new PhenomenonType(_DUSTSTORM);
    public static final PhenomenonType SANDSTORM = new PhenomenonType(_SANDSTORM);
    public static final PhenomenonType THUNDERSTORM = new PhenomenonType(_THUNDERSTORM);
    public static final PhenomenonType TORNADIC = new PhenomenonType(_TORNADIC);
    public static final PhenomenonType UNKNOWN = new PhenomenonType(_UNKNOWN);
    
    public String getValue() { 
    	return _value_;
    }
    public static PhenomenonType fromValue(String value) throws IllegalStateException {
        PhenomenonType enum = (PhenomenonType) _table_.get(value);
        if (enum==null) throw new IllegalStateException();
        return enum;
    }
    public static PhenomenonType fromString(String value)
          throws IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public String toString() { return _value_;}
    public Object readResolve() throws ObjectStreamException { return fromValue(_value_);}
}

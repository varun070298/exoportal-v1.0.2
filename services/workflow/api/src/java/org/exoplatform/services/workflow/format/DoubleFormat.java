package org.exoplatform.services.workflow.format;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;


public class DoubleFormat extends Format {
  
  DecimalFormat decimalFormat = new DecimalFormat();
  
  public DoubleFormat() {
    decimalFormat = new DecimalFormat("0.####################################");
  }

  public Object parseObject(String source, ParsePosition pos) {
    Double result = null;
    
    if ( source != null ) {
      try { 
        result = new Double(decimalFormat.parse(source).doubleValue());
        pos.setErrorIndex(-1);
        pos.setIndex(source.length());
      }
      catch(ParseException e) {
        pos.setErrorIndex(e.getErrorOffset());
      }
    }

    return result;
  }

  public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
    if ( obj != null ) {
      toAppendTo.append(decimalFormat.format(obj));
    }
    return toAppendTo;
  }

}

/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.workflow.format;

import java.text.*;
import java.util.Date;
import java.util.Locale;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 18 mai 2004
 */
public class BasicDateFormat extends Format{
  private SimpleDateFormat dateFormat;
  private String pattern = "dd/MM/yyyy";
  private Locale locale = new Locale("en");

  public BasicDateFormat() {
    updateFormat();
  }

  public void setPattern(String pattern){
    this.pattern = pattern;
    updateFormat();
  }

  public void setLocale(Locale locale){
    this.locale = locale;
    updateFormat();
  }

  public void updateFormat(){
    dateFormat = new SimpleDateFormat(pattern, locale);
  }

  public Object parseObject(String source, ParsePosition pos) {
    Date result = null;
    if ( source != null ) {
      try {
        result = dateFormat.parse(source);
        pos.setErrorIndex(-1);
        pos.setIndex(source.length());
      }
      catch(ParseException e) {
        pos.setErrorIndex(e.getErrorOffset());
        e.printStackTrace();
      }
    }
    return result;
  }

  public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
    if ( obj != null ) {
      toAppendTo.append(dateFormat.format(obj));
    }
    return toAppendTo;
  }

}

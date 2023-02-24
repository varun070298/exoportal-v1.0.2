 /***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.commons.utils;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Benjamin Mestrallet
 * benjamin.mestrallet@exoplatform.com
 */
public class MapResourceBundle extends ResourceBundle{
  
  private final static String REGEXP = "#\\{.*\\}";
  
  private Map props = new HashMap();
  private ResourceBundle rB;
  private Locale locale;
  
  public MapResourceBundle(Locale l) {    
    this.locale = l;
  }  

  public MapResourceBundle(ResourceBundle rB,  Locale l) {
    this.locale = l;
    this.rB = rB;
    initMap();
  }

  private void initMap() {
    Enumeration e = rB.getKeys();
    while (e.hasMoreElements()) {
      String s = (String) e.nextElement();
      Object value = rB.getObject(s);      
      try {        
        String[] newArray = rB.getStringArray(s);        
        if (props.get(s) == null) {
          props.put(s, newArray);
        }
      } catch (ClassCastException ex) {
        props.put(s, value);
      }
    }
  }

  protected Object handleGetObject(String key) {
    return props.get(key);
  }

  public Enumeration getKeys() {
    return new Vector(props.keySet()).elements();
  }

  public Locale getLocale() {
    return this.locale;
  }
  
  public void add(String key, Object value){
    props.put(key, value);
  }
  
  public void remove(String key){
    props.remove(key);
  }
    
  public void merge(ResourceBundle bundle){
    Enumeration e = bundle.getKeys();
    while (e.hasMoreElements()) {
      String s = (String) e.nextElement();
      Object value = bundle.getObject(s);      
      try {        
        String[] newArray = bundle.getStringArray(s);        
        if (props.get(s) == null) {
          props.put(s, newArray);
        }
      } catch (ClassCastException ex) {
        props.put(s, value);
      }
    }    
  }   
  
  public void resolveDependencies(){
    Map tempMap = new HashMap();
    Set keys = props.keySet();
    Pattern pattern = Pattern.compile(REGEXP);
    for (Iterator iter = keys.iterator(); iter.hasNext();) {
      String element = (String) iter.next();
      String value = lookupKey(element, pattern);        
      tempMap.put(element, value);
    }
    props = tempMap;
  }
  
  private String lookupKey(String key, Pattern pattern){
    String s = (String) props.get(key);     
    if(s == null) return key;
    Matcher matcher = pattern.matcher(s);         
    if(matcher.find()){      
      return recursivedResolving(s, pattern);        
    }  
    return s;                   
  }  
  
  private String recursivedResolving(String key, Pattern pattern){                  
    String resolved = key;
    StringBuffer sB = new StringBuffer();
    while(resolved.indexOf('#') != -1){  
      sB.setLength(0) ;
      int firstIndex = resolved.indexOf('#');      
      int lastIndex = resolved.indexOf('}', firstIndex);
      String realKey = resolved.substring(firstIndex + 2, lastIndex);      
      sB.append(resolved.substring(0, firstIndex));
      sB.append(lookupKey(realKey, pattern));
      sB.append(resolved.substring(lastIndex + 1));
      resolved = sB.toString();         
    }    
    return resolved;        
  }  
}
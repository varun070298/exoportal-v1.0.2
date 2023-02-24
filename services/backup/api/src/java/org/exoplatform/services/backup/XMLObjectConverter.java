/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.backup;

import java.util.Iterator;
import org.exoplatform.xml.object.*;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Apr 15, 2005
 * @version $Id$
 */
abstract public class XMLObjectConverter {
  private String name ;
  private String description ;
  private String dataVersion ;
  
  public String getName() { return name ; }
  public void   setName(String s) { name = s ;}
  
  public String getDescription() { return  description ; }
  public void   setDescription(String s) { description = s ; }
  
  public String getDataVersion() { return dataVersion ; }
  public void   setDataVersion(String s) { dataVersion = s ; }
  
  public void traverse(XMLObject xmlobject) {
    update(xmlobject) ;
    Iterator i = xmlobject.getFieldIterator() ;
    while(i.hasNext()) {
      XMLField xmlfield = (XMLField) i.next() ;
      Object obj = xmlfield.getValue() ;
      if(obj instanceof XMLObject) {
        traverse((XMLObject)obj) ;
      } else if(obj instanceof XMLCollection) {
        traverse((XMLCollection)obj) ;
      }
    }
  }
  
  public void traverse(XMLCollection xmlobject) {
    Iterator i = xmlobject.getIterator() ;
    while(i.hasNext()) {
      XMLValue xmlvalue = (XMLValue) i.next() ;
      Object obj = xmlvalue.getValue() ;
      if(obj instanceof XMLObject) {
        traverse((XMLObject)obj) ;
      }
    }
  }
  
  public void traverse(XMLMap xmlobject) {
    Iterator i = xmlobject.getEntryIterator() ;
    while(i.hasNext()) {
      XMLEntry xmlentry = (XMLEntry) i.next() ;
      Object key = xmlentry.getKey().getValue();
      if(key instanceof XMLObject) {
        traverse((XMLObject)key) ;
      }
      Object value = xmlentry.getKey().getValue();
      if(value instanceof XMLObject) {
        traverse((XMLObject)value) ;
      }
    }
  }
 
  abstract public  void update(XMLObject xmlobject)  ;
    
}

/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.backup;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.exoplatform.services.database.XResources;
import org.exoplatform.xml.object.XMLCollection;
import org.exoplatform.xml.object.XMLObject;
/**
 * May 27, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $ID$
 **/
abstract public class ImportExportPlugin  {
  final static public String DATA_VERSION = "1.0.1" ;
  
  private Map xmlObjectTransformer_ = new HashMap() ;
  private List dataTransformer_ = new ArrayList() ;
  
  private String name ;
  private String description ;
  private String dataVersion = DATA_VERSION ;
  
  public void init() throws Exception {
  }
  
  public String getName() { return name ; }
  public void   setName(String s) { name = s ; }
  
  public String getDescription() { return description ; }
  public void   setDescription(String s) { description =  s ; }
  
  public String getCurrentDataVersion() { return dataVersion ; }
  public void   setCurrentDataVersion(String s) { dataVersion = s ; }
  
  public boolean hasUserData()  { return true ;  }
  public boolean hasServiceData() { return true ; }
  
  
  public List getDataTransformer() { return dataTransformer_ ; }
  
  public void addXMLObjectConverter(List list) {
    for(int i = 0; i < list.size() ; i++) {
      XMLObjectConverter transformer = (XMLObjectConverter) list.get(i) ;
      xmlObjectTransformer_.put(transformer.getDataVersion(), transformer) ;
    }
  }
  
  public XMLObjectConverter getXMLObjectConverter(String dataVersion) {
    return (XMLObjectConverter) xmlObjectTransformer_.get(dataVersion) ;
  }
  
  abstract public void exportUserData(String user, XResources resources, ZipOutputStream os) throws Exception ;
  abstract public void importUserData(String user, XResources resources, ZipFile in) throws Exception ;
  
  
  abstract public void exportServiceData(XResources resources, ZipOutputStream os) throws Exception ;
  abstract public void importServiceData(XResources resources, ZipFile in) throws Exception ;
  
  public Metadata getMetadata(String name, ZipFile zipFile) throws Exception {
    Metadata mdata = (Metadata) XMLObject.getObject(getEntry(name, zipFile)) ;
    return mdata ;
  }
  
  protected InputStream getEntry(String name, ZipFile zipFile) throws Exception {
    ZipEntry entry = zipFile.getEntry(name);
    return zipFile.getInputStream(entry) ;
  }
  
  protected void createEntry(String name, ZipOutputStream os, Object o) throws Exception  {
    os.putNextEntry(new ZipEntry(name));
    if(o instanceof Collection) {
      XMLCollection xmlobject = new XMLCollection((Collection)o) ;
      os.write(xmlobject.toByteArray("UTF-8")) ;
    } else {
      XMLObject xmlobject = new XMLObject(o) ;
      os.write(xmlobject.toByteArray("UTF-8")) ;
    }
    os.closeEntry() ;
  }
}
/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.backup;

import java.util.Collection;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
/**
 * May 27, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $ID$
 **/
public interface ImportExportService  {
  public String getDirectory() ;
  public void setDirectory(String s) throws Exception ;
  public String getUserDataDirectory() ;
  public String getServiceDataDirectory() ;
  
  public void addPlugin(ImportExportPlugin plugin) ;
  public Collection getPlugins() ;
  
  public void exportUserData() throws Exception  ;
  public void exportUserData(String username) throws Exception ;
  public void exportUserData(String username, ZipOutputStream os) throws Exception ;
  
  public void importUserData() throws Exception  ;
  public void importUserData(String username) throws Exception ;
  public void importUserData(String username, ZipFile file) throws Exception ;
   
  public void exportServiceData() throws Exception ;
  public void exportServiceData(String serviceName) throws Exception ;
  public void exportServiceData(String serviceName, ZipOutputStream os) throws Exception ;
  
  public void importServiceData() throws Exception ;
  public void importServiceData(String serviceName) throws Exception ;
  public void importServiceData(String serviceName, ZipFile zip) throws Exception ;
  
  public ExportLogger getExportLogger() ;
  public ImportLogger getImportLogger() ;
}
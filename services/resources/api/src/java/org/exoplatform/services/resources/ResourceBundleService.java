/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.resources;

import java.util.* ;
import org.exoplatform.commons.utils.PageList;
/**
 * May 7, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: ResourceBundleService.java,v 1.2 2004/10/26 18:47:55 benjmestrallet Exp $
 **/
public interface ResourceBundleService {
	public ResourceBundle getResourceBundle(String name, Locale locale) ;
	public ResourceBundle getResourceBundle(String name, Locale locale, ClassLoader cl) ;  
  
  /**
   * This method will look call the method 
   * public ResourceBundle getResourceBundle(String[] name, Locale locale, ClassLoader cl)
   * and using the the classloader of the current thread 
   */
  public ResourceBundle getResourceBundle(String[] name, Locale locale) ;
  /**
   * This method will look for all the resources with the given names and merge into
   * one resource bundle, the properties in the later resource bundle name will have 
   * the higher priority than the previous one.
   */
  public ResourceBundle getResourceBundle(String[] name, Locale locale, ClassLoader cl) ;  
  
	public ResourceBundleData getResourceBundleData(String id) throws Exception ;
	public ResourceBundleData removeResourceBundleData(String id) throws Exception ;
	public void   saveResourceBundle(ResourceBundleData data) throws Exception ;
	
  public PageList  findResourceDescriptions(Query q) throws Exception ;
  
  public ResourceBundleData createResourceBundleDataInstance() ;
}
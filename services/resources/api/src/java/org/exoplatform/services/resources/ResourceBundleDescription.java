/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.resources;

/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: May 14, 2004
 * Time: 1:12:22 PM
 */
public interface ResourceBundleDescription {
  final static public String DEFAULT_LANGUAGE = "en" ;
  
  public String  getId() ;
   
  public String  getName()  ;
  public void    setName(String name) ; 
  
  public String  getLanguage()  ;
  public void    setLanguage(String s) ; 

  public String  getCountry()  ;
  public void    setCountry(String s) ; 
  
  public String  getVariant()  ;
  public void    setVariant(String s) ; 
  
  public String  getResourceType()  ;
  public void    setResourceType(String s) ;
}
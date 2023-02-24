/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.impl;

import java.util.* ;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Oct 27, 2004
 * @version $Id: NewPortalConfig.java,v 1.1 2004/10/28 15:39:41 tuan08 Exp $
 */
public class NewPortalConfig {
  private HashSet predefinedUser = new HashSet(5);
  private String templateUser ;
  private String templateLocation ;
  
  public HashSet getPredefinedUser() {   return predefinedUser; }
  public void setPredefinedUser(HashSet s) {  this.predefinedUser = s; }
  
  public String getTemplateLocation() {  return templateLocation; }
  public void setTemplateLocation(String s) { this.templateLocation = s; }
  
  public String getTemplateUser() {  return templateUser;}
  public void setTemplateUser(String s) {  this.templateUser = s; }
  
  public boolean isPredefinedUser(String user) { return predefinedUser.contains(user) ; }
}
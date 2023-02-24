/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.skin;

import java.util.*  ;
import java.io.InputStream  ;
import org.exoplatform.services.portal.skin.model.*;

/**
 * Created by The eXo Platform SARL.
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Jun 14, 2003
 * Time: 1:12:22 PM
 */
public interface SkinConfigService  {

  public void addConfiguration(InputStream is) throws Exception ;
  public void addConfiguration(String fileUrl) throws Exception ;

  public Collection getPortalDecorators() ;
  public Decorator  getPortalDecorator(String rendererType) ;
  public Style      getPortalDecoratorStyle(String rendererType, String styleName) ;
  
  public Collection getPageDecorators() ;
  public Decorator  getPageDecorator(String rendererType) ;
  public Style      getPageDecoratorStyle(String rendererType, String styleName) ;

  public Collection getContainerDecorators() ;
  public Decorator  getContainerDecorator(String rendererType) ;
  public Style      getContainerDecoratorStyle(String rendererType, String styleName) ;

  public Collection getPortletDecorators() ;
  public Decorator  getPortletDecorator(String rendererType) ;
  public Style      getPortletDecoratorStyle(String rendererType,String styleName) ;
  
  public List       getPortletStyles(String portletName) ;
  public Style      getPortletStyle(String portletName, String styleName) ;
}	
/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

/**
 * Jul 5, 2004, 2:29:15 PM
 * @author: F. MORON
 * @email: francois.moron@rd.francetelecom.com
 * 
 * */
package org.exoplatform.portal.faces.component;

public class UIMobilePage extends UISinglePage {
	  public static final String DEFAULT_RENDERER_TYPE = "MobilePageRenderer";
	  
	  public UIMobilePage() {
		super();
		setRendererType(getRendererType());
		setId("mobile-page");
	  }

	  public String getFamily() { return "org.exoplatform.portal.faces.component.UIMobilePage" ; }
	  public String getIdPrefix() { return "mp" ; }
	  protected String getDefaultRendererType() { return DEFAULT_RENDERER_TYPE ; }

	  public String getRendererType() {
	  		return DEFAULT_RENDERER_TYPE;
	  }
}

/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.test.mocks.portlet;

import javax.portlet.*  ;
import javax.faces.context.FacesContext  ;
import org.exoplatform.test.mocks.jsf.*;

/**
 * Apr 25, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: MockPortletFacesContext.java,v 1.1 2004/10/11 23:27:26 tuan08 Exp $
 **/
public class MockPortletFacesContext extends MockFacesContext {
	
    
	public MockPortletFacesContext() {
     externalContext_ = new MockPortletExternalContext();
  }
  
  public PortletPreferences getPortletPreferences() { 
  	return ((MockPortletExternalContext) externalContext_).getPortletPreferences() ;
  }
  
  public MockPortletConfig getMockPortletConfig() {
  	return ((MockPortletExternalContext) externalContext_).getMockPortletConfig() ;
  }
  
  public MockPortletContext getMockPortletContext() {
  	return ((MockPortletExternalContext) externalContext_).getMockPortletContext() ;
  }
  
  public static MockPortletFacesContext getMockPortletFacesContextCurrentInstance() {
    MockPortletFacesContext context = (MockPortletFacesContext)FacesContext.getCurrentInstance() ;
    if(context == null) {
      context= new MockPortletFacesContext() ;
      FacesContext.setCurrentInstance(context) ;
    }
    return context ;
  }
}

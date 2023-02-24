/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.portlet.faces.context;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
/**
 * This factory is part of JSR-127 chaper 6
 *
 * @author Ove Ranheim (oranheim@users.sourceforge.net)
 * @since Nov 6, 2003 4:09:23 PM
 *
 */
public class FacesPortletContextFactoryImpl extends FacesContextFactory {

  public FacesContext getFacesContext( Object config, Object request, Object response, Lifecycle lifecycle ) throws FacesException {
    try {
      return new FacesPortletContextImpl(config, request, response );
    } catch (Exception ex) {
      ex.printStackTrace() ;
      throw new FacesException("Cannot instantiate FacesContext ", ex) ;
    }
  }
}

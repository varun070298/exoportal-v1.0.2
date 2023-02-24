/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.resources.impl;

import java.io.InputStream ;
import java.io.IOException ;
import java.util.* ;

/**
 * May 7, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: ExoResourceBundle.java,v 1.1 2004/08/29 21:48:03 benjmestrallet Exp $
 **/
public class ExoResourceBundle extends PropertyResourceBundle {
  
  public ExoResourceBundle(InputStream is) throws IOException {
    super(is) ; 
  }
  
  public ExoResourceBundle(InputStream is, ResourceBundle parent) throws IOException {
    super(is) ; 
    setParent(parent);
  }
}
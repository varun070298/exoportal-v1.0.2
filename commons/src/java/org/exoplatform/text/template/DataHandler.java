/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.text.template;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Feb 1, 2005
 * @version $Id$
 */
public interface DataHandler {
  public Class getDataTypeHandler() ;
  public String getValueAsString(DataBindingValue value) ;
  public Object getValue(DataBindingValue value) ;
}

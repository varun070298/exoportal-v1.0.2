/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.xml.object;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Apr 11, 2005
 * @version $Id$
 */
public class XMLValue extends XMLBaseObject{
  public XMLValue() {  }
  
  public XMLValue(Class objecttype , Object val) throws Exception {
    super(objecttype , val) ;
  }
}
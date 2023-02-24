/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.model;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 17, 2004
 * @version $Id$
 */
public interface NodeVisitor {
  public void visit(Node node) ;
}

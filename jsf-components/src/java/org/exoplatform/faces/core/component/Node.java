/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.component;

import java.util.List ;

/**
 * Jun 2, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $ID$
 **/
public interface Node {
	public String getId() ;
	public String getName() ;
	public String getIcon() ;
	public String getDescription() ;
	public void setRendered(boolean b) ;
	public boolean isRendered() ;
	public List getChildren() ;
}

/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.container.jmx;

import javax.management.QueryExp;
import javax.management.MBeanServer ;
import javax.management.*  ;

/**
 * Jul 30, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: ExoQueryExp.java,v 1.1 2004/07/31 14:52:22 tuan08 Exp $
 */
public class ExoQueryExp implements QueryExp {
	
  private String domain_ ;
  
  public ExoQueryExp(String domain) {
    domain_ = domain ;
  }
  
  public void setMBeanServer(MBeanServer s) {
    
  }
  
  public boolean apply(ObjectName name) throws BadStringOperationException,
																				BadBinaryOpValueExpException,
																				BadAttributeValueExpException,
																				InvalidApplicationException {
		return domain_.equals(name.getDomain());
	}
}
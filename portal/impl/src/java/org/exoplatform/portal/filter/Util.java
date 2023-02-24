/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.filter;

import java.util.Enumeration;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest ;
import org.exoplatform.container.monitor.ActionData;
import org.exoplatform.portal.session.RequestInfo;
/**
 * Jul 13, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: Util.java,v 1.1 2004/07/14 14:02:54 tuan08 Exp $
 */
public class Util {
	static public void removeAttribute(HttpSession session) {
		Enumeration e = session.getAttributeNames() ;
		while(e.hasMoreElements()) {
			String key = (String) e.nextElement() ;
			session.removeAttribute(key);
		}
	}
}

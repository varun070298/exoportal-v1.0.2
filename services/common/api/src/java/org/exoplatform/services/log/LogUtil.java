/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.log;

import org.apache.commons.logging.Log;
import org.exoplatform.container.RootContainer;

/**
 * Jun 28, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: LogUtil.java,v 1.4 2004/07/24 16:20:09 tuan08 Exp $
 */
public class LogUtil {
	static private Log defaultInstance_ = null ;
	
	public static Log getLog(String category) {
		RootContainer manager  = RootContainer.getInstance();
		LogService service = (LogService) manager.getComponentInstanceOfType(LogService.class) ;
		Log log = service.getLog(category) ;
		return log ;
	}
	
  public static Log getLog(Class clazz) {
    RootContainer manager  = RootContainer.getInstance();
    LogService service = (LogService) manager.getComponentInstanceOfType(LogService.class) ;
    Log log = service.getLog(clazz) ;
    return log ;
  }
  
	public static void setLevel(String category, int level, boolean recursive) throws Exception {
		RootContainer manager  = RootContainer.getInstance();
		LogService service = (LogService) manager.getComponentInstanceOfType(LogService.class) ;
		service.setLogLevel(category, level, recursive) ;
	}
	
	static public void debug(String msg) {	getDefault().debug(msg) ;	}
	static public void debug(String msg, Throwable t) {	getDefault().debug(msg, t) ;	}
	
	static public void info(String msg) {	getDefault().info(msg) ;	}
	static public void info(String msg, Throwable t) {	getDefault().info(msg, t) ;	}
	
	static public void warn(String msg) {	getDefault().warn(msg) ;	}
	static public void warn(String msg, Throwable t) {	getDefault().warn(msg, t) ;	}
	
	static public void error(String msg) {	getDefault().error(msg) ;	}
	static public void error(String msg, Throwable t) {	getDefault().error(msg, t) ;	}
	
	final static public Log getDefault() {
		if(defaultInstance_ == null) {
			defaultInstance_ = getLog("default") ;
		}
		return defaultInstance_ ;
	}
}
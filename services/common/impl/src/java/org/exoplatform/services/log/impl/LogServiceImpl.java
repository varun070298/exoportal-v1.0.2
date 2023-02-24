package org.exoplatform.services.log.impl;

import java.util.*;
import org.apache.commons.logging.Log;
import org.exoplatform.services.log.LogService;

/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by the Exo Development team. Author : Mestrallet Benjamin
 * benjmestrallet@users.sourceforge.net Date: 14 nov. 2003 Time: 20:40:57
 */
public class LogServiceImpl implements LogService {

	private HashMap logs_;

	private HashMap configure_;

	public LogServiceImpl() {
		logs_ = new HashMap();
		configure_ = new HashMap();
		configure_.put("org.exoplatform.portal", new Integer(ExoLog.INFO));
		configure_.put("org.exoplatform.services.portletcontainer",
				new Integer(ExoLog.INFO));
	}

	public void start() {
	}

	public void stop() {

	}

	public Log getLog(String name) {
		Log log = (Log) logs_.get(name);
		if (log == null) {
			synchronized (logs_) {
				int level = ExoLog.INFO;
				try {
					level = getDefaultLogLevel(name);
				} catch (Exception ex) {
				}
				log = new ExoLog(name, level);
				logs_.put(name, log);
			}
		}
		return log;
	}

	public Log getLog(Class clazz) {
		String name = clazz.getName();
		int idx = name.lastIndexOf(".");
		name = name.substring(0, idx);
		return getLog(name);
	}

	public Collection getLogs() {
		return logs_.values();
	}

	public int getLogLevel(String name) throws Exception {
		ExoLog log = (ExoLog) logs_.get(name);
		if (log != null)
			return log.getLevel();
		return INFO;
	}

	public void setLogLevel(String name, int level, boolean recursive)
			throws Exception {
		if (recursive) {
			Iterator i = logs_.values().iterator();
			while (i.hasNext()) {
				ExoLog log = (ExoLog) i.next();
				if (log.getLogCategory().startsWith(name)) {
					log.setLevel(level);
				}
			}
		} else {
			ExoLog log = (ExoLog) logs_.get(name);
			if (log != null) {
				log.setLevel(level);
			}
		}
	}

	public List getLogBuffer() {
		return ExoLog.getLogBuffer();
	}

	public List getErrorBuffer() {
		return ExoLog.getErrorBuffer();
	}

	private int getDefaultLogLevel(String name) throws Exception {
		while (name != null) {
			Integer level = (Integer) configure_.get(name);
			if (level != null)
				return level.intValue();
			int index = name.lastIndexOf(".");
			if (index > 0)
				name = name.substring(0, index);
			else
				name = null;
		}
		return ExoLog.INFO;
	}
}
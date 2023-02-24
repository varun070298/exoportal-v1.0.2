/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.storage.filesystem.nodedata;

import org.apache.commons.logging.Log;
import org.exoplatform.services.jcr.core.NodeData;
import org.exoplatform.services.jcr.impl.core.NodeImpl;
import org.exoplatform.services.jcr.impl.core.PropertyImpl;
import org.exoplatform.services.jcr.util.PathUtil;
import org.exoplatform.services.log.LogUtil;

import javax.jcr.DateValue;
import javax.jcr.PathNotFoundException;
import javax.jcr.PropertyType;
import javax.jcr.StringValue;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: BaseNodeContainer.java,v 1.7 2004/09/16 15:26:54 geaz Exp $
 */

public abstract class BaseNodeContainer implements NodeContainer {

  protected static NodeContainerResolver resolver = new NodeContainerResolver();

  protected File storage;
  protected String containerPath;
  protected String nodeType;
  protected ArrayList rootProperties;
  protected Log log;

  BaseNodeContainer() {
    this.log = LogUtil.getLog("org.exoplatform.services.jcr");
  }

  // jcrPath of container!!!
  BaseNodeContainer(String jcrPath, String type, File storage) {
    this();
    this.containerPath = jcrPath;
    this.storage = storage;
    this.nodeType = type;
  }

  public NodeData getRootNode() {
    try {
      return new NodeImpl(containerPath, getRootProps());
    } catch (Exception e) {
      throw new RuntimeException("getRootNode failed. Reason: " + e.getMessage());
    }
  }

  public NodeData getNodeById(String UUID) {
    throw new RuntimeException("getNodeById does not supported ! ");
  }


  public final String getNodeType() {
    return this.nodeType;
  }

  public final String getContainerPath() {
    return containerPath;
  }

  public final File getStorage() {
    return storage;
  }

  public final String parseRelPath(String jcrPath) throws PathNotFoundException {
    if (jcrPath.length() < containerPath.length())
      throw new PathNotFoundException("ParseRelPath failed. Path <" + jcrPath + ">. is too short.");
    else if (jcrPath.length() == containerPath.length())
      return "";
    if (jcrPath.startsWith(containerPath))
      return jcrPath.substring(containerPath.length(), jcrPath.length());
    throw new PathNotFoundException("ParseRelPath failed. Unexpected Path <" + jcrPath + ">.");
  }

  protected String getJcrPath(String relPath) {
    return containerPath + relPath;
  }

  protected String getParentRelPath(String relPath) {

    try {

      if (PathUtil.getDepth(relPath) <= 1)
        return "";
      else
        return PathUtil.getAncestorPath(relPath, 1);
    } catch (Exception e) {
      return null;
    }

  }

  protected PropertyImpl getRootProperty(String relPath) {
//System.out.println("Prop relPath -- "+relPath+" '"+getParentRelPath(relPath)+"'");
    try {

//            String name = PathUtil.getName(jcrPath);

      if (relPath.equals("/jcr:primaryType"))
        return new PropertyImpl(getJcrPath("/jcr:primaryType"), new StringValue(nodeType),
            PropertyType.STRING);
      if (relPath.equals("/jcr:created") || relPath.equals("/jcr:lastModified")) {
        long cr = storage.lastModified();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(cr));
        return new PropertyImpl(getJcrPath(relPath), new DateValue(cal),
            PropertyType.DATE);
      }

    } catch (Exception e) {
      return null;
    }

    return null;
  }

  protected List getRootProps() {
    ArrayList properties = new ArrayList();
    properties.add(getRootProperty("/jcr:primaryType"));
    properties.add(getRootProperty("/jcr:lastModified"));
    properties.add(getRootProperty("/jcr:created"));
    return properties;
  }

}

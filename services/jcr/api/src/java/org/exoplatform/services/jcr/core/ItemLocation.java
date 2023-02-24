/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.core;


import javax.jcr.PathNotFoundException;
import org.exoplatform.services.jcr.util.PathUtil;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: ItemLocation.java,v 1.1 2004/09/16 15:26:54 geaz Exp $
 */

public class ItemLocation {

  public static final String ROOT_PATH = "/";
  private String path;

  public ItemLocation(String absPath) throws PathNotFoundException {
    this.path = PathUtil.makeCanonicalPath(absPath);
  }

  public ItemLocation(String parentAbsPath, String path) throws PathNotFoundException {
    this.path = PathUtil.makeCanonicalPath(parentAbsPath, path);
  }

  public String getName() {
    int pos = path.lastIndexOf("/");
    if (pos < 0) {
      return path;
    } else if (pos == path.length()) {
      return "";
    }
    return path.substring(pos + 1);
  }

  public String getPath() {
    return path;
  }

  public String getParentPath() throws PathNotFoundException {
    String parent;
    try {
      return getAncestorPath(1);
    } catch (PathNotFoundException e) {
      return ROOT_PATH;
    }
  }

  public String getAncestorPath(int relativeDegree) throws PathNotFoundException {
    int pos = path.length();
    int cnt = relativeDegree;
    while (cnt-- > 0) {
      pos = path.lastIndexOf('/', pos - 1);
      if (pos < 0) {
        throw new PathNotFoundException(relativeDegree + "nth ancestor of " + path);
      }
    }
    String ancestorPath = path.substring(0, pos);
    return ancestorPath.equals("") ? "/" : ancestorPath;
  }

  public int getDepth() {
    int cnt = 0;
    // Except trailing / - for ex. '/test/'
    for (int i = 0; i < path.length() - 1; i++)
      if (path.charAt(i) == '/')
        cnt++;
    return cnt;
  }

  public boolean equals(Object obj) {
    if (!(obj instanceof ItemLocation))
      return false;
    return ((ItemLocation) obj).getPath() == this.getPath();
  }
}

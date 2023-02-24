/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.core;

import javax.jcr.PathNotFoundException;
import java.util.Set;
import java.util.HashSet;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: ReferenceableNodeLocation.java,v 1.2 2004/11/02 18:29:51 geaz Exp $
 */

public class ReferenceableNodeLocation extends ItemLocation {

  private String UUID;
  private String realPath;
  private Set referencedPaths = new HashSet();

  public ReferenceableNodeLocation(String absPath, String UUID, String realPath) throws PathNotFoundException {
//     this(absPath, UUID, new HashSet());
     super(absPath);
     this.UUID = UUID;
     this.realPath = realPath;

  }

  public ReferenceableNodeLocation(String absPath, String UUID, String realPath, Set referencedPaths) throws PathNotFoundException {
     super(absPath);
     this.UUID = UUID;
     this.realPath = realPath;
     this.referencedPaths = referencedPaths;
  }

  public String getUUID() {
    return UUID;
  }

  public String getRealPath() {
    return realPath;
  }

  public void addReferencedPath(String path) {
    referencedPaths.add(path);
  }

  public Set getReferencedPaths() {
    return referencedPaths;
  }

  public String toString() {

    return "RefereanceableNodeLocation "+UUID+" path:"+getPath()+" real path:"+realPath+" references:"+referencedPaths;
  }

}

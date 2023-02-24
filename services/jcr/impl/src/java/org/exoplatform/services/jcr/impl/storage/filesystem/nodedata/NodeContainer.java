/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.storage.filesystem.nodedata;

import java.io.File;


import javax.jcr.PathNotFoundException;
import org.exoplatform.services.jcr.storage.Container;

import java.util.List;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: NodeContainer.java,v 1.3 2004/08/10 16:32:06 geaz Exp $
 */

// General contract: storage for node should exist!
public interface NodeContainer extends Container {

  String getNodeType();

  String getContainerPath();

  String parseRelPath(String path) throws PathNotFoundException;

  File getStorage();

}

/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.core;

import javax.jcr.Node;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: NodesAccessor.java,v 1.2 2004/07/24 00:16:21 benjmestrallet Exp $
 */

// one per ticket
public interface NodesAccessor {
  Node getNode(String absPath, boolean shallow);

  void addNode(Node node);

  void updateNode(Node node);

  void deleteNode(String absPath);
}

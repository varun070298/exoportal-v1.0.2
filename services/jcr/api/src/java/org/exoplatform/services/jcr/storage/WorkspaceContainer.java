/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.storage;

import javax.jcr.query.QueryResult;
import javax.jcr.query.Query;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: WorkspaceContainer.java,v 1.1 2004/08/23 10:28:19 geaz Exp $
 */

public interface WorkspaceContainer extends Container {

  public static final String ROOT_PATH = "/";

  QueryResult getQueryResult(Query query);

  String getName();

//  void setRepositoryManager(RepositoryManager manager);

}

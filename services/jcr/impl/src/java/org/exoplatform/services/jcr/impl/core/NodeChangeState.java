/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.core;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: NodeChangeState.java,v 1.5 2004/09/16 15:26:53 geaz Exp $
 */

public class NodeChangeState {
  public static final int UNDEFINED = 0;
  public static final int ADDED = 1;
  public static final int UPDATED = 2;
  public static final int DELETED = 3;
  public static final int UNCHANGED = 4;
  public static final int REF_ADDED = 5;


  public static String getStateName(int state) {
    if (state == ADDED)
      return "ADDED";
    else if (state == UPDATED)
      return "UPDATED";
    else if (state == DELETED)
      return "DELETED";
    else if (state == UNCHANGED)
      return "UNCHANGED";
    else if (state == REF_ADDED)
      return "REFERENCE ADDED";
    else
      return "UNDEFINED";
  }
}

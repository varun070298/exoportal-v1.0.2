/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.core.itemfilters;

import javax.jcr.Item;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: DepthHintFilter.java,v 1.2 2004/07/08 23:36:47 benjmestrallet Exp $
 */

public class DepthHintFilter implements ItemFilter {

  public DepthHintFilter(int depthHint) {
  }

  public boolean accept(Item item) {
    return true;
  }
}

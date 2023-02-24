/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.event;

import org.exoplatform.faces.core.component.UIExoComponent;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Dec 6, 2004
 * @version $Id$
 */
abstract public class UIComponentObserver {
  abstract public void onChange(UIExoComponent target) throws Exception ;
} 
/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.grammar.wiki;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Dec 17, 2004
 * @version $Id$
 */
public interface WikiEngineService {
  public String toXHTML(String wikiText);
}
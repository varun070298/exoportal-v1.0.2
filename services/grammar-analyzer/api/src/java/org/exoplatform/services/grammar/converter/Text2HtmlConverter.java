/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.grammar.converter;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Jan 4, 2005
 * @version $Id$
 */
public interface Text2HtmlConverter {
  public String toHtml(String javaCode) ;
  public String toHtml(char[] javaCodeBuf) ;
  public void   toHtml(char[] javaCodeBuf, StringBuffer out) ;
}
/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.util;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: StringConverter.java,v 1.2 2004/07/08 23:36:50 benjmestrallet Exp $
 */

public class StringConverter {

  /**
   * Normalizes and prints the given string.
   */
  public static String normalizeString(String s, boolean canonical) {

    StringBuffer strBuf = new StringBuffer();
    int len = (s != null) ? s.length() : 0;
    for (int i = 0; i < len; i++) {
      char c = s.charAt(i);
      strBuf.append(normalizeChar(c, canonical));
    }
    return new String(strBuf);

  } // normalizeAndPrint(String)

  /**
   * Normalizes and print the given character.
   */
  public static String normalizeChar(char c, boolean canonical) {

    switch (c) {
      case '<':
        return "&lt;";
      case '>':
        return "&gt;";
      case '&':
        return "&amp;";
      case '"':
        return "&quot;";
      case '\r':
      case '\n':
        if (canonical) {
          return "&#" + Integer.toString(c) + ";";
        }
        // else, default print char
      default:
        return "" + c;
    }

  }


}

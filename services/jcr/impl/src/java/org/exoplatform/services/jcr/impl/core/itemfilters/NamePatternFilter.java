/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.core.itemfilters;

import org.apache.commons.lang.StringUtils;

import javax.jcr.Item;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: NamePatternFilter.java,v 1.6 2004/08/06 18:51:24 benjmestrallet Exp $
 */

public class NamePatternFilter implements ItemFilter {

  private ArrayList expressions;

  public NamePatternFilter(String namePattern) {
    expressions = new ArrayList();
    StringTokenizer parser = new StringTokenizer(namePattern, "|");
    while (parser.hasMoreTokens()) {
      String token = parser.nextToken();
      expressions.add(token.trim());
    }
  }

  public boolean accept(Item item) {
    String name = item.getName();
    boolean result = false;
    for (int i = 0; i < expressions.size(); i++) {
      String expr = (String) expressions.get(i);
      if(estimate(name, expr))
        return true;
    }
    return false;
  }

  private boolean estimate(String name, String expr) {
    if (expr.indexOf("*") == -1)
      return name.equals(expr);
    else {
      String regexp = StringUtils.replace(expr,"*", ".*");
      Matcher matcher = Pattern.compile(regexp).matcher(name);
      return matcher.matches();
    }
  }

}

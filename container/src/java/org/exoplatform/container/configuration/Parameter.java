/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.container.configuration;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Oct 20, 2004
 * @version $Id: Parameter.java,v 1.1 2004/10/20 20:54:13 tuan08 Exp $
 */
abstract public class Parameter {
  private String  description;
  private String  name;

  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
}

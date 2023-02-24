/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.config;

import java.util.Properties;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: ContainerEntry.java,v 1.2 2004/08/23 14:54:11 geaz Exp $
 */

public class ContainerEntry {
  public ContainerEntry(String name, Class type, Properties params) {
    this.name = name;
    this.type = type;
    this.parameters = params;
  }

  private String name;
  private Class type;
  private Properties parameters;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Class getType() {
    return type;
  }

  public void setType(Class type) {
    this.type = type;
  }

  public Properties getParameters() {
    return parameters;
  }

  public void setParameters(Properties parameters) {
    this.parameters = parameters;
  }


}

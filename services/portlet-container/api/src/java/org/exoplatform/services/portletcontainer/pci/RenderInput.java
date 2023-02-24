/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/


package org.exoplatform.services.portletcontainer.pci;

import java.util.Map;
/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Jul 30, 2003
 * Time: 9:08:20 PM
 */
public class RenderInput extends Input {

  private Map renderParameters;
	private String title;
  private boolean updateCache;

  public Map getRenderParameters() {
    return renderParameters;
  }

  public void setRenderParameters(Map renderParameters) {
    this.renderParameters = renderParameters;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public boolean isUpdateCache() {
    return updateCache;
  }

  public void setUpdateCache(boolean updateCache) {
    this.updateCache = updateCache;
  }

}

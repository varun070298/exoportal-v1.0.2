/*************************************************************************** * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  * * Please look at license.txt in info directory for more license detail.  * **************************************************************************/package org.exoplatform.faces.core.renderer.html;public  class SimpleFormButtonRenderer extends SimpleFormRenderer {    protected String formatText(String text) {    StringBuffer b = new StringBuffer() ;    b.append("<span class='br'>");    b.append("<span class='tl'>");        b.append("<span class='outer'>");    b.append("<span class='inner'>");    b.append(text);    b.append("</span>");    b.append("</span>");    b.append("</span>");    b.append("</span>");      return b.toString() ;  }    protected String getDefaultClass() { return "ic3-button" ; }}
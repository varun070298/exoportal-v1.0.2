/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.util;

import org.w3c.dom.Document;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import java.io.InputStream;
import java.io.IOException;

import org.xml.sax.SAXException;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: NodeTypeRecognizer.java,v 1.3 2004/08/18 17:30:51 benjmestrallet Exp $
 */

public class NodeTypeRecognizer {

  public static final int SYS = 1;
  public static final int DOC = 2;

  public static int recognize(InputStream is)
      throws IOException, SAXException, ParserConfigurationException {
    DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
    dfactory.setNamespaceAware(true);
    DocumentBuilder docBuilder = dfactory.newDocumentBuilder();
    Document doc = docBuilder.parse(is);
    String name = doc.getDocumentElement().getNodeName();
    if (name.substring(0, 2).equalsIgnoreCase("sv"))
      return SYS;
    else
      return DOC;
  }


}

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
 * @version $Id: DOMBuilder.java,v 1.2 2004/07/08 23:36:50 benjmestrallet Exp $
 */

public class DOMBuilder {

  public static Document createDocument() throws ParserConfigurationException {
    DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
    dfactory.setNamespaceAware(true);
    DocumentBuilder docBuilder = dfactory.newDocumentBuilder();
    return docBuilder.newDocument();
  }

  public static Document createDocument(InputStream is)
      throws IOException, SAXException, ParserConfigurationException {
    DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
    dfactory.setNamespaceAware(true);
    DocumentBuilder docBuilder = dfactory.newDocumentBuilder();
    return docBuilder.parse(is);
  }


}

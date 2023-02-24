/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.portlet.cocoon;


import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.exoplatform.container.PortalContainer;

import org.exoplatform.services.xml.transform.html.HTMLTransformer;
import org.exoplatform.services.xml.transform.html.HTMLTransformerService;
import org.exoplatform.services.xml.transform.trax.TRAXTransformer;
import org.exoplatform.services.xml.transform.trax.TRAXTemplates;
import org.exoplatform.services.xml.transform.trax.TRAXTransformerService;
import org.exoplatform.services.xml.transform.trax.Constants;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import org.exoplatform.services.xml.transform.NotSupportedIOTypeException;
import org.exoplatform.services.log.LogService;
import org.apache.commons.logging.Log;

/**
 * Created by The eXo Platform SARL        .
 * <p/>
 * Url rewriter for XHTML script
 * href="original-url" will be rewrited as
 * href="<portlet-url>?<href-param>=<original-url>"
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: URLRewriter.java 596 2005-01-28 15:45:37Z kravchuk $
 */

public class URLRewriter {
    public static final String PARAM_NAME_VALUE = "url";
    //xsl param names
    public static final String BASE_URI = "baseURI"; //PREFIX_URL = "portlet-url";
    public static final String PARAM_NAME = "param-name"; //HREF_PARAM = //"href-param";
    public static final String LINK_PREFIX = "link-prefix"; //HREF_CONTEXT = "href-context";

    public static final String HTML_URL_REWRITE_STYLE =
            Constants.XSLT_DIR + "/html-url-rewite.xsl";

    private static TRAXTemplates templates;
    private Log log;

    public URLRewriter() {
        LogService logService = (LogService) PortalContainer.getInstance().
                                getComponentInstanceOfType(LogService.class);
        log = logService.getLog(this.getClass());
    }


    protected TRAXTemplates getTemplates() throws
            TransformerException, IOException {
        if (templates == null) {
            InputStream inputStream =
                    Thread.currentThread().getContextClassLoader().
                    getResource(HTML_URL_REWRITE_STYLE).openStream();
            TRAXTransformerService traxService =
                    (TRAXTransformerService) PortalContainer.getInstance().
                    getComponentInstanceOfType(TRAXTransformerService.class);
            try {
                templates = traxService.getTemplates(
                        new StreamSource(inputStream));
            } catch (NotSupportedIOTypeException ex) {
                new IOException(ex.getMessage());
            }
        }
        return templates;
    }


    public void rewrite(InputStream input, OutputStream output,
                        String baseURI, String linkPrefix) throws
            TransformerException,
            TransformerConfigurationException, InstantiationException,
            IOException {

//    System.out.println("Content to transform : " + new String(buffer));
//    log.debug("linkPrefix=["+currentPageURI+"]");
//    log.debug("urlPrefix=["+urlPrefix+"]");

        HTMLTransformerService htmlService =
                (HTMLTransformerService) PortalContainer.getInstance().
                getComponentInstanceOfType(HTMLTransformerService.class);

        log.debug("URLRewriterNew.rewrite htmlService is null? - " +
                  (htmlService == null));

        HTMLTransformer htmlTransformer = htmlService.getTransformer();
        TRAXTransformer traxTransformer = getTemplates().newTransformer();

        traxTransformer.setParameter(BASE_URI, baseURI);
        traxTransformer.setParameter(LINK_PREFIX, linkPrefix);
        traxTransformer.setParameter(PARAM_NAME, PARAM_NAME_VALUE);

        try {
            htmlTransformer.initResult(traxTransformer.getTransformerAsResult());
            traxTransformer.initResult(new StreamResult(output));
            htmlTransformer.transform(new StreamSource(input));
        } catch (NotSupportedIOTypeException ex) {
            new IOException(ex.getMessage());
        }

    }
}

/*
* Copyright 2001-2004 The eXo platform SARL All rights reserved.
* Please look at license.txt in info directory for more license detail.
*/

package org.exoplatform.services.wsrp.testConsumer;


import java.net.URLEncoder;
import java.net.URLDecoder;
import org.exoplatform.services.wsrp.WSRPConstants;
import org.exoplatform.services.wsrp.exceptions.WSRPException;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 6 févr. 2004
 * Time: 17:17:29
 */

public class TestURLRewriter extends BaseTest {

    String s = "wsrp_rewrite?wsrp-urlType=render&amp;wsrp-mode=wsrp:help&amp;" +
        "wsrp-navigationalState=rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAh3CAAAAAsAAAAAeA**" +
        "&amp;wsrp-windowState=wsrp:normal&amp;amp;wsrp-secureURL=false/wsrp_rewrite";

  public void testRewrite() throws WSRPException {
    System.out.println("Rewritten : " + urlRewriter.rewriteURLs("baseURL", s));
    //assertEquals(returned, urlRewriter.rewriteURLs(s));
  }

}
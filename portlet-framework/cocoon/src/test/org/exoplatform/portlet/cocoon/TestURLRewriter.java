/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.portlet.cocoon;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;
import org.exoplatform.portlet.cocoon.URLRewriter;
import org.exoplatform.services.log.LogService;
import java.io.*;
import org.apache.commons.logging.Log;
import junit.framework.TestCase;

/**
 * Created by the Exo Development team.
 */
public class TestURLRewriter extends TestCase {

   private Log log;
   public void setUp() throws Exception
   {

       LogService logService = (LogService) RootContainer.getInstance().
                            getComponentInstanceOfType(LogService.class);

    log = logService.getLog("org.exoplatform.services.xml");
    logService.setLogLevel("org.exoplatform.services.xml", LogService.DEBUG, true);

    log = logService.getLog("org.exoplatform.portlet.cocoon");
    logService.setLogLevel("org.exoplatform.portlet.cocoon", LogService.DEBUG, true);

    log = logService.getLog(this.getClass());


   }

  public void testRewrite() throws Exception {
//    byte[] buffer = "<a href=\"http://test/test.jsp\">test</a>".getBytes();
//    String testStr = "<a xmlns=\"\" href=\"page.jsp?portal:ctx=exo&portal:componentId=window-Cocoon&url=cocoon/samples/\">test</a>";
//    assertEquals(testStr, URLRewriter.rewrite(buffer, "cocoon/", "page.jsp?portal:ctx=exo&portal:componentId=window-Cocoon") );

//    byte[] buffer = "<a href=\"samples/\">test</a>".getBytes();
//    System.out.println("OUT----"+URLRewriter.rewrite(buffer, "cocoon/", "portal/faces/public/page.jsp?portal:ctx=exo&portal:componentId=window-Cocoon"));



      String test_url = "<a href='samples/'>test</a> "+
                     " <a href='?cocoon-view=pretty-content'>Pretty content</a>";

      ByteArrayInputStream inputStream =
              new ByteArrayInputStream(test_url.getBytes());


      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      URLRewriter urlRewriter = new URLRewriter();

      urlRewriter.rewrite(inputStream, outputStream,
              "portal/faces/public/page.jsp?portal:ctx=exo&portal:componentId=window-Cocoon",
              "cocoon/");

    assertTrue("empty output",outputStream.size()>0);
    log.debug("Out 1 ["+outputStream+"]");
    System.out.println("Out 1 ["+outputStream+"]");

      String prospective_url =
            "portal/faces/public/page.jsp?portal:ctx=exo&amp;" +
            "portal:componentId=window-Cocoon&amp;url=cocoon/samples/";
      assertTrue("Wrong result 1",outputStream.toString().indexOf(prospective_url) >= 0);

      inputStream.reset();
      outputStream.close();
      urlRewriter.rewrite(inputStream, outputStream,
                          "portal/faces/public/page.jsp",
                          "cocoon/");
      log.debug("Out 1 ["+outputStream+"]");
      assertTrue("empty output",outputStream.size()>0);


      prospective_url =
            "portal/faces/public/page.jsp?url=cocoon/samples/";
      assertTrue("Wrong result 2",outputStream.toString().indexOf(prospective_url) >= 0);


  }
}

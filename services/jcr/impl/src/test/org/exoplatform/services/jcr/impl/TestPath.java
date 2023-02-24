/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

package org.exoplatform.services.jcr.impl;

import org.exoplatform.services.jcr.BaseTest;
import org.exoplatform.services.jcr.core.ItemLocation;
import org.exoplatform.services.jcr.util.PathUtil;


/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: TestPath.java,v 1.2 2004/09/16 15:28:52 geaz Exp $
 */

public class TestPath extends BaseTest {


  public void testPathUtil() throws Exception {
    int test = PathUtil.getDepth("/");
    assertTrue(test == 0);
    test = PathUtil.getDepth("/node1");
    assertTrue(test == 1);
    test = PathUtil.getDepth("/node1/");
    assertTrue(test == 1);
    test = PathUtil.getDepth("/node1/jcr:uuid");
    assertTrue(test == 2);
    assertFalse(PathUtil.isDescendant("/node1/jcr:uuid", "/", true));
    assertTrue(PathUtil.isDescendant("/node1/jcr:uuid", "/", false));
    assertTrue(PathUtil.isDescendant("/node1/jcr:uuid", "/node1", false));
    assertTrue(PathUtil.isDescendant("/node1/jcr:uuid", "/node1", true));

    assertFalse(PathUtil.isDescendant("/node11", "/node1", true));
    assertFalse(PathUtil.isDescendant("/node11", "/node1", false));

    assertFalse(PathUtil.isDescendant("/testNode1/node11", "/testNode", true));
    assertFalse(PathUtil.isDescendant("/testNode1/node11", "/testNode", false));


  }

  public void testItemLocation() throws Exception {
    ItemLocation loc = new ItemLocation("/root/node1", "node2");
    assertEquals("/root/node1/node2", loc.getPath());
    assertEquals("node2", loc.getName());
    assertEquals("/root/node1", loc.getParentPath());
    assertEquals("/", loc.getAncestorPath(3));
    assertTrue(loc.getDepth() == 3);
  }
}

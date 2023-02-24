/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.jcr.api.visiting;


import javax.jcr.*;
import javax.jcr.util.TraversingItemVisitor;
import org.exoplatform.services.jcr.BaseTest;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 14 ao�t 2004
 */
public class ItemVisitorTest extends BaseTest {

  public void testItemVisiting() throws RepositoryException {
    Node root = ticket.getRootNode();
    Node file = root.addNode("childNode", "nt:folder").addNode("childNode2", "nt:file");
    Node contentNode = file.getNode("jcr:content");
    contentNode.setProperty("exo:content", new StringValue("this is the content"));
    ticket.save();

    ItemVisitor visitor = new MockVisitor();
    contentNode.accept(visitor);
    contentNode.getProperty("exo:content").accept(visitor);

    visitor = new MockVisitor2();
    root.getNode("childNode").accept(visitor);
    assertEquals(((MockVisitor2)visitor).getI(),((MockVisitor2)visitor).getJ());
    assertEquals(3, ((MockVisitor2)visitor).getI());
    assertTrue(((MockVisitor2)visitor).isReached());
  }

  public class MockVisitor implements ItemVisitor {

    public void visit(Property property) throws RepositoryException {
      assertEquals(property.getName(), "exo:content");
    }

    public void visit(Node node) throws RepositoryException {
      assertEquals(node.getName(), "jcr:content");
    }

  }


  public class MockVisitor2 extends TraversingItemVisitor {

    private boolean reached = false;
    private int i = 0;
    private int j = 0;

    protected void entering(Property property, int level) throws RepositoryException {
      if("exo:content".equals(property.getName()))
        reached = true;
    }

    protected void entering(Node node, int level) throws RepositoryException {
      i++;
      assertTrue(isInList(node.getName()));
    }

    protected void leaving(Property property, int level) throws RepositoryException {
    }

    protected void leaving(Node node, int level) throws RepositoryException {
      j++;
    }

    private boolean isInList(String name){
      if("childNode".equals(name) || "childNode2".equals(name) || "jcr:content".equals(name) ||
          "exo:content".equals(name)){
        return true;
      }
      return false;
    }

    public int getI() {
      return i;
    }

    public int getJ() {
      return j;
    }

    public boolean isReached() {
      return reached;
    }
  }

}

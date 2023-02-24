/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.jcr.api.writing;


import javax.jcr.*;
import org.exoplatform.services.jcr.BaseTest;
import java.io.ByteArrayInputStream;
import java.util.GregorianCalendar;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 2 ao�t 2004
 */
public class ValueTest extends BaseTest{

  public void initRepository() throws RepositoryException {
    Node root = ticket.getRootNode();
    Node propDef = root.addNode("propertyDefNode", "nt:propertyDef");
    // Unknown Property Type. Should set something!
    propDef.setProperty("jcr:defaultValue", "testString");

    root.addNode("childNodeDefNode", "nt:childNodeDef");
    ticket.save();
  }

  public void tearDown() throws RepositoryException {
    ticket = repository.login(credentials, WORKSPACE);
    Node root = ticket.getRootNode();
    root.remove("propertyDefNode");
    root.remove("childNodeDefNode");
    ticket.save();
  }

  public void testSetValue() throws RepositoryException {
    Node root = ticket.getRootNode();
    Property property = root.getNode("propertyDefNode").getProperty("jcr:defaultValue");

    property.setValue(new StringValue("haha"));
    property.setValue("default");
    property.setValue(new ByteArrayInputStream(new String("default").getBytes()));
    property.setValue(true);
    property.setValue(new GregorianCalendar());
    property.setValue(20D);
    property.setValue(20L);

    Value[] values = {new StringValue("not"), new StringValue("in")};
    try {
      property.setValue(values);
      fail("exception should have been thrown");
    } catch (ValueFormatException e) {
    }

    property = root.getNode("propertyDefNode").getProperty("jcr:multiple");
    try {
      property.setValue(20D);
      fail("ValueFormatException should have been thrown");
    } catch (ValueFormatException e) {
    }

    property = root.getNode("childNodeDefNode").getProperty("jcr:requiredPrimaryTypes");
    property.setValue(values);

    root.save(false);

    ticket = repository.login(credentials, WORKSPACE);
    Node node = ticket.getRootNode().getNode("childNodeDefNode");
    assertEquals(2, node.getProperty("jcr:requiredPrimaryTypes").getValues().length);

  }

}

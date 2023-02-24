/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.jcr.api.reading;


import javax.jcr.*;
import org.exoplatform.services.jcr.BaseTest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.GregorianCalendar;
import java.util.Calendar;


/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 24 juil. 2004
 */
public class PropertyTest extends BaseTest{

  public void initRepository() throws RepositoryException {
    Node root = ticket.getRootNode();
    Node file = root.addNode("childNode", "nt:file");
    Node contentNode = file.getNode("jcr:content");
    contentNode.setProperty("exo:content", new StringValue("this is the content"));
    Value[] values = new Value[3];
    values[0] = new StringValue("stringValue");
    values[1] = new BooleanValue(true);
    values[2] = new LongValue(12);
    contentNode.setProperty("multi", values, PropertyType.STRING);
//    ticket.save();
  }

  public void tearDown() throws RepositoryException {
    Node root = ticket.getRootNode();
    root.remove("childNode");
//    ticket.save();
  }

  public void testGetValue() throws RepositoryException {
    Node root = ticket.getRootNode();
    Property property = root.getNode("childNode/jcr:content").getProperty("exo:content");
    assertTrue(property.getValue() instanceof Value);

    Node node = root.getNode("childNode/jcr:content");
    assertEquals("stringValue", node.getProperty("multi").getValue().getString());

    node.setProperty("dummy", new Value[0], PropertyType.STRING);
    assertNull(node.getProperty("dummy").getValue());
  }

  public void testGetValues() throws RepositoryException {
    Node root = ticket.getRootNode();
    Node node = root.getNode("childNode/jcr:content");
    Value[] values = node.getProperty("multi").getValues();
    for (int i = 0; i < values.length; i++) {
      Value value = values[i];
      if(!("stringValue".equals(value.getString()) || "true".equals(value.getString()) ||
          "12".equals(value.getString()) )){
        fail("returned non expected value");
      }
    }
  }

  public void testGetString() throws RepositoryException {
    Node root = ticket.getRootNode();
    Node node = root.getNode("childNode/jcr:content");
    String stringValue = node.getProperty("multi").getString();
    assertEquals("stringValue", stringValue);
    assertEquals("stringValue", node.getProperty("multi").getValue().getString());
  }

  public void testDouble() throws RepositoryException {
    Node root = ticket.getRootNode();
    Node node = root.getNode("childNode/jcr:content");
    node.setProperty("double", new DoubleValue(15));
    assertEquals(15, (int)node.getProperty("double").getDouble());
    assertEquals(15, (int)node.getProperty("double").getValue().getDouble());
  }

  public void testLong() throws RepositoryException {
    Node root = ticket.getRootNode();
    Node node = root.getNode("childNode/jcr:content");
    node.setProperty("long", new LongValue(15));
    assertEquals(15, node.getProperty("long").getLong());
    assertEquals(15, node.getProperty("long").getValue().getLong());
  }

  public void testStream() throws RepositoryException, IOException {
    Node root = ticket.getRootNode();
    Node node = root.getNode("childNode/jcr:content");
//    node.setProperty("stream", new BinaryValue(new String("inputStream").getBytes()));
    node.setProperty("stream", new BinaryValue("inputStream"));

System.out.println("STTTTTTTTREAM ---- "+node.getProperty("stream"));
    InputStream iS = node.getProperty("stream").getStream();
System.out.println("IIIIIIString ---- "+node.getProperty("stream").getString());
System.out.println("IIIIIISREAM ---- "+iS.available());
    byte[] bytes = new byte[iS.available()];
    iS.read(bytes);
    assertEquals("inputStream", new String(bytes));
    iS.reset();
    iS = node.getProperty("stream").getValue().getStream();
    bytes = new byte[iS.available()];
    iS.read(bytes);
    assertEquals("inputStream", new String(bytes));
  }

  public void testDate() throws RepositoryException {
    Node root = ticket.getRootNode();
    Node node = root.getNode("childNode/jcr:content");
    Calendar calendar = new GregorianCalendar();
    node.setProperty("date", new DateValue(calendar));
//    System.out.println("MILLIS ----- "+ calendar.getTimeInMillis());
    assertEquals(calendar.getTimeInMillis(), node.getProperty("date").getDate().getTimeInMillis());
    assertEquals(calendar.getTimeInMillis(), node.getProperty("date").getValue().getDate().getTimeInMillis());
  }

  public void tesGetBoolean() throws RepositoryException {
    Node root = ticket.getRootNode();
    Node node = root.getNode("childNode/jcr:content");
    node.setProperty("boolean", new BooleanValue(true));
    assertEquals(true, node.getProperty("boolean").getBoolean());
    assertEquals(true, node.getProperty("boolean").getValue().getBoolean());
  }

  public void testHasValues() throws RepositoryException {
    Node root = ticket.getRootNode();
    Node node = root.getNode("childNode/jcr:content");
    node.setProperty("dummy", new Value[0], PropertyType.STRING);
    assertFalse(node.getProperty("dummy").hasValue());
  }

  public void testGetLength() throws RepositoryException, IOException {
    Node root = ticket.getRootNode();
    Node node = root.getNode("childNode/jcr:content");
    long length = node.getProperty("multi").getLength();
    assertEquals(11, length);

    InputStream iS = new ByteArrayInputStream(new String("inputStream").getBytes());
    node.setProperty("stream", new BinaryValue(iS));
    assertEquals(11, node.getProperty("stream").getLength());
  }

}

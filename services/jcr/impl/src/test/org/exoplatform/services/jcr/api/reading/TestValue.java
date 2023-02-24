/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.jcr.api.reading;


import javax.jcr.*;
import org.exoplatform.services.jcr.BaseTest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 27 juil. 2004
 */
public class TestValue extends BaseTest{

  public void testGetString() throws RepositoryException {
    Value value = new StringValue("text");
    assertEquals("text", value.getString());
  }

  public void testGetDouble() throws RepositoryException {
    Value value = new StringValue("text");
    try {
      value.getDouble();
      fail("exception should have been thrown");
    } catch (ValueFormatException e) {
    } catch (RepositoryException e) {
      fail("not good exception thrown");
    }

    value = new StringValue("20");
    assertEquals(20, (int)value.getDouble());

    try {
      value.getStream();
      fail("exception should have been thrown");
    } catch (IllegalStateException e) {
    } catch (RepositoryException e) {
      fail("not good exception thrown");
    }
  }

  public void testGetLong() throws RepositoryException {
    Value value = new StringValue("text");
    try {
      value.getDouble();
      fail("exception should have been thrown");
    } catch (ValueFormatException e) {
    } catch (RepositoryException e) {
      fail("not good exception thrown");
    }

    value = new StringValue("15");
    assertEquals(15, value.getLong());

    try {
      value.getStream();
      fail("exception should have been thrown");
    } catch (IllegalStateException e) {
    } catch (RepositoryException e) {
      fail("not good exception thrown");
    }
  }

  public void testGetStream() throws RepositoryException, IOException {
    Value value = new BinaryValue(new String("inputStream").getBytes());
    InputStream iS = value.getStream();
    byte[] bytes = new byte[iS.available()];
    iS.read(bytes);
    assertEquals("inputStream", new String(bytes));

    assertEquals(iS, value.getStream());

    value = new StringValue("text");
    //TODO JCR API BUG
    iS = value.getStream();
    bytes = new byte[iS.available()];
    iS.read(bytes);
    assertEquals("text", new String(bytes));

    try {
      value.getStream();
      fail("exception should have been thrown");
    } catch (IllegalStateException e) {
    } catch (RepositoryException e) {
      fail("not good exception thrown");
    }
  }

  public void testGetDate() throws RepositoryException {
    Value value = new StringValue("text");
    try {
      value.getDate();
      fail("exception should have been thrown");
    } catch (ValueFormatException e) {
    } catch (RepositoryException e) {
      fail("not good exception thrown");
    }

    Calendar calendar = new GregorianCalendar();
    value = new DateValue(calendar);
    assertEquals(calendar, value.getDate());

    try {
      value.getStream();
      fail("exception should have been thrown");
    } catch (IllegalStateException e) {
    } catch (RepositoryException e) {
      fail("not good exception thrown");
    }
  }

  public void testGetBoolean() throws RepositoryException {
    Value value = new LongValue(10);
    try {
      value.getBoolean();
      fail("exception should have been thrown");
    } catch (ValueFormatException e) {
    } catch (RepositoryException e) {
      fail("not good exception thrown");
    }

    value = new BooleanValue(true);
    assertTrue(value.getBoolean());

    try {
      value.getStream();
      fail("exception should have been thrown");
    } catch (IllegalStateException e) {
    } catch (RepositoryException e) {
      fail("not good exception thrown");
    }
  }

  public void tesGetType(){
    Value value = new StringValue("");
    assertEquals(PropertyType.STRING, value.getType());
    value = new LongValue(10);
    assertEquals(PropertyType.LONG, value.getType());
    value = new DoubleValue(10);
    assertEquals(PropertyType.DOUBLE, value.getType());
    value = new BooleanValue(true);
    assertEquals(PropertyType.BOOLEAN, value.getType());
    value = new DateValue(new GregorianCalendar());
    assertEquals(PropertyType.DATE, value.getType());
    value = new BinaryValue("");
    assertEquals(PropertyType.BINARY, value.getType());
    value = new ReferenceValue("uuid");
    assertEquals(PropertyType.REFERENCE, value.getType());
    value = new SoftLinkValue("");
    assertEquals(PropertyType.SOFTLINK, value.getType());
  }

}

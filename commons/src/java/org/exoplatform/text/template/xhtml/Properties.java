/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.text.template.xhtml;

import org.exoplatform.text.template.ObjectFormater ;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Feb 3, 2005
 * @version $Id$
 */
public class Properties extends Table {
  public Properties() {
    setCssClass("UIProperties") ;
  }
  
  public Properties addHeader(String header) {
    add(new Row().
        add(new HeaderCell(header).addArribute("colspan", "2"))) ;
    return this ;
  }
  
  public Properties add(String label, String value) {
    add(new Row().
        add(new Cell(label).setCssClass("key")).add(new Cell(value).setCssClass("value"))) ;
    return this ;
  }
  
  public Properties add(String label, String value, ObjectFormater formater) {
    add(new Row().
        add(new Cell(label).setCssClass("key")).
        add(new Cell(value).setCssClass("value").setFomater(formater))) ;
    return this ;
  }
  
  public Properties add(Element label, Element value) {
    add(new Row().add(label.setCssClass("key")).add(value.setCssClass("value"))) ;
    return this ;
  }
}
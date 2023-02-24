/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.text.template.xhtml;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.exoplatform.text.template.*;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Feb 1, 2005
 * @version $Id$
 */
abstract public class Element { 
  private List elements_ = new ArrayList() ;
  protected Element[] children_ ;
  private boolean locked_ = false ;
  protected String cssClass_ ;
  protected String cssStyle_ ;
  protected Class dataHandlerType_ ;
  protected ObjectFormater formater_ ;
  
  public Class    getDataHandlerType() { return dataHandlerType_ ; }
  public Element  setDataHandlerType(Class type) {
    dataHandlerType_ = type ;
    return this ;
  }
  
  public Element  setFomater(ObjectFormater formater) {
    formater_ = formater ;
    return this ;
  }
  
  public String getCssClass() { return cssClass_ ; }
  public Element  setCssClass(String css) {
    cssClass_ = css ;
    return this ;
  }
  
  public String getStyle() { return cssStyle_ ; }
  public Element  setStyle(String style) {
    cssStyle_ = style ;
    return this ;
  }
  
  public void addElement(Element element) {
    if(locked_) 
      throw new RuntimeException("The element has been optimized, rebuild the tree") ;
    elements_.add(element) ;
  }
  
  public Element add(Element element) {
    if(locked_) 
      throw new RuntimeException("The element has been optimized, rebuild the tree") ;
    elements_.add(element) ;
    return this ;
  }
  
  final public Element optimize() {
    optimize(null) ;
    return this ;
  }
  
  final public Element optimize(Element parent) {
    children_ = new Element[elements_.size()] ;
    if(dataHandlerType_ == null && parent != null) {
      dataHandlerType_ = parent.getDataHandlerType() ;
    }
    
    for(int i = 0; i < children_.length; i++) {
      children_[i] = (Element) elements_.get(i) ;
      children_[i].optimize(this) ;
    }
    locked_ = true ;
    elements_ = null ;
    return this ;
  }
  
  static protected String resolveValueAsString(Value value, DataHandler handler , ResourceBundle res ) {
    if(value instanceof ResourceBindingValue) {
      ResourceBindingValue rv = (ResourceBindingValue) value ;
      try {
        return res.getString(rv.getKey()) ;
      } catch (MissingResourceException ex) {
        return rv.getExpression() ;
      }
    } else if(value instanceof DataBindingValue) { 
      return handler.getValueAsString((DataBindingValue) value) ;
    } 
    return ((StringValue)value).getValue() ;
  }
  
  abstract public void render(XhtmlDataHandlerManager manager, 
                              ResourceBundle res, Writer w) throws IOException ;
}
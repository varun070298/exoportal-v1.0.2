/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.text.template;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Feb 2, 2005
 * @version $Id$
 */
public class ListBeanDataHandler extends BeanDataHandler  implements CollectionDataHandler {
  protected List beans_;
  protected int currentRow_;
  
  public ListBeanDataHandler(Class type) {
    super(type) ;
  }
  
  public ListBeanDataHandler setBeans(List beans) {
    beans_ = beans;
    return this;
  }
  
  public ListBeanDataHandler setBeans(Object[] beans) {
    beans_ = new ArrayList(beans.length + 3);
    for(int i = 0 ; i < beans.length; i++) {
      beans_.add(beans[i]);
    }
    return this;
  }
  
  public void begin() {
    currentRow_ = -1;
  }
  
  public void end() {
  }
  
  public boolean nextRow() {
    currentRow_++;
    if (beans_ != null) {
      if (currentRow_ < beans_.size()) {
        setBean(beans_.get(currentRow_));
        return true;
      }
    }
    return false;
  }
  
  public int getCurrentRow() { return currentRow_; }
  
  public Object getCurrentBean() {  return beans_.get(currentRow_); }
  
  public Object getBean(int index) {  return beans_.get(index); }
  
  public List getBeans() { return beans_ ; }
}

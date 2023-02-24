package org.exoplatform.services.jcr.impl.core;

import javax.jcr.Item;
import javax.jcr.RangeIterator;

public interface ItemIterator extends RangeIterator {

  /**
   * Returns the next <code>Element</code> in the iteration.
   *
   * @return the next <code>Element</code> in the iteration.
   * @throws java.util.NoSuchElementException
   *          if iteration has no more elements.
   */
  public Item nextItem();
}

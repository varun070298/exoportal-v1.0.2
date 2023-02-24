package org.exoplatform.services.jcr.impl.core.itemfilters;

import javax.jcr.Item;

public interface ItemFilter {

  /**
   * Returns <code>true</code> if the specified element is to be
   * included in the set of child elements returbned by
   * <code>{@link Node#getElements(ElementFilter filter)}</code>.
   *
   * @param element The element to be tested for inclusion in the returned set.
   * @return a <code>boolean</code>.
   */
  public boolean accept(Item item);
}

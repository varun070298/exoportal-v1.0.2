/*
 * $Id: RangeIterator.java,v 1.2 2004/07/24 00:16:21 benjmestrallet Exp $
 *
 * Copyright 2002-2004 Day Management AG, Switzerland.
 *
 * Licensed under the Day RI License, Version 2.0 (the "License"),
 * as a reference implementation of the following specification:
 *
 *   Content Repository API for Java Technology, revision 0.12
 *        <http://www.jcp.org/en/jsr/detail?id=170>
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License files at
 *
 *     http://www.day.com/content/en/licenses/day-ri-license-2.0
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package javax.jcr;

import java.util.Iterator;

/**
 * Extends <code>Iterator</code> with the <code>skip</code>, <code>getSize</code>
 * and <code>getPos</code> methods. The base interface of all type-specific
 * iterators in the <code>javax.jcr</code> and its subpackages.
 *
 * @author Peeter Piegaze
 * @author Stefan Guggisberg
 */
public interface RangeIterator extends Iterator {

  /**
   * Skip a number of elements in the iterator.
   *
   * @param skipNum the non-negative number of elements to skip
   * @throws java.util.NoSuchElementException
   *          if skipped past the last element in the iterator.
   */
  public void skip(int skipNum);

  /**
   * Returns the number of elements in the iterator.
   * If this information is unavailable, returns -1.
   *
   * @return a long
   */
  public long getSize();

  /**
   * Returns the current position within the iterator. The number
   * returned is the 0-based index of the next element in the iterator,
   * i.e. the one that will be returned on the subsequent <code>next</code> call.
   * <p/>
   * Note that this method does not check if there is a next element,
   * i.e. an empty iterator will always return 0.
   *
   * @return a long
   */
  public long getPos();
}

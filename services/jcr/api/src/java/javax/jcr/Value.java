/*
 * $Id: Value.java,v 1.3 2004/08/07 16:45:05 benjmestrallet Exp $
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

import java.io.InputStream;
import java.util.Calendar;

/**
 * A generic holder for the value of a <code>Property</code>
 * can be used whithout knowing the exact type (string, double, stream etc.)
 *
 * @author Peeter Piegaze
 * @author Stefan Guggisberg
 */
public interface Value {

  /**
   * Returns the <code>type</code> of this <code>Value</code>.
   * One of:
   * <ul>
   * <li><code>PropertyType.STRING</code></li>
   * <li><code>PropertyType.DATE</code></li>
   * <li><code>PropertyType.BINARY</code></li>
   * <li><code>PropertyType.DOUBLE</code></li>
   * <li><code>PropertyType.LONG</code></li>
   * <li><code>PropertyType.BOOLEAN</code></li>
   * <li><code>PropertyType.SOFTLINK</code></li>
   * <li><code>PropertyType.REFERENCE</code></li>
   * </ul>
   * See <code>{@link PropertyType}</code>.
   * <p/>
   * The type returned is that which was set at property creation.
   */
  public int getType();

  /**
   * Returns the <code>String</code> representation of this value.
   * Unlike <code>{@link Property#getString()}</code> this method throws a
   * <code>ValueFormatException</code> if conversion is not possible.
   *
   * @return a <code>String</code> representation of this value
   * @throws ValueFormatException  If, due to its type or format, this value
   *                               cannot be converted to <code>String</code> (since everything can be
   *                               converted to string, this should never happen) or the value is null
   *                               (i.e., empty).
   * @throws IllegalStateException if <code>getStream()</code> has been
   *                               called previously at least once.
   * @throws RepositoryException   If another error occurs.
   */
  public String getString() throws ValueFormatException, IllegalStateException, RepositoryException;

  /**
   * Returns the <code>double</code> representation of this value.
   * Unlike
   * <code>{@link Property#getDouble()}</code> this method throws a
   * <code>ValueFormatException</code> if conversion is not possible.
   *
   * @return a <code>double</code> representation of this value
   * @throws ValueFormatException  If, due to its type or format, this value
   *                               cannot be converted to <code>double</code> or the value is null
   *                               (i.e., empty).
   * @throws IllegalStateException if <code>getStream()</code> has been
   *                               called previously at least once.
   * @throws RepositoryException   If another error occurs.
   */
  public double getDouble() throws ValueFormatException, IllegalStateException, RepositoryException;

  /**
   * Returns the <code>InputStream</code> representation of this value.
   * Unlike
   * <code>{@link Property#getStream()}</code> this method throws a
   * <code>ValueFormatException</code> if conversion is not possible.
   * Please note that calling <code>getStream()</code> repeatedly will always
   * return the <i>same</i> <code>InputStream</code> object.
   *
   * @return a <code>InputStream</code> representation of this value
   * @throws ValueFormatException  If, due to its type or format, this value
   *                               cannot be converted to <code>InputStream</code> (since everything can be
   *                               converted to string, this should never happen) or the value is null
   *                               (i.e., empty).
   * @throws IllegalStateException if any getter methods other than
   *                               <code>getStream()</code> and <code>getState()</code> have been
   *                               called previously at least once.
   * @throws RepositoryException   If another error occurs.
   */
  public InputStream getStream() throws ValueFormatException, IllegalStateException, RepositoryException;

  /**
   * Returns the <code>Calendar</code> representation of this value.
   * Unlike
   * <code>{@link Property#getDate()}</code> this method throws a
   * <code>ValueFormatException</code> if conversion is not possible.
   *
   * @return a <code>Calendar</code> representation of this value
   * @throws ValueFormatException  If, due to its type or format, this value
   *                               cannot be converted to <code>Calendar</code> or the value is null
   *                               (i.e., empty).
   * @throws IllegalStateException if <code>getStream()</code> has been
   *                               called previously at least once.
   * @throws RepositoryException   If another error occurs.
   */
  public Calendar getDate() throws ValueFormatException, IllegalStateException, RepositoryException;

  /**
   * Returns the <code>long</code> representation of this value.
   * Unlike
   * <code>{@link Property#getLong()}</code> this method throws a
   * <code>ValueFormatException</code> if conversion is not possible.
   *
   * @return a <code>long</code> representation of this value
   * @throws ValueFormatException  If, due to its type or format, this value
   *                               cannot be converted to <code>long</code> or the value is null
   *                               (i.e., empty).
   * @throws IllegalStateException if <code>getStream()</code> has been
   *                               called previously at least once.
   * @throws RepositoryException   If another error occurs.
   */
  public long getLong() throws ValueFormatException, IllegalStateException, RepositoryException;

  /**
   * Returns the <code>boolean</code> representation of this value.
   * Unlike
   * <code>{@link Property#getBoolean()}</code> this method throws a
   * <code>ValueFormatException</code> if conversion is not possible.
   * Same as level 1.
   *
   * @return a <code>boolean</code> representation of this value
   * @throws ValueFormatException  If, due to its type or format, this value
   *                               cannot be converted to <code>boolean</code> or the value is null
   *                               (i.e., empty).
   * @throws IllegalStateException if <code>getStream()</code> has been
   *                               called previously at least once.
   * @throws RepositoryException   If another error occurs.
   */
  public boolean getBoolean() throws ValueFormatException, IllegalStateException, RepositoryException;
}
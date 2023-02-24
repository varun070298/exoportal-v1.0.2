/*
 * $Id: SoftLinkValue.java,v 1.2 2004/07/24 00:16:21 benjmestrallet Exp $
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

import java.util.Calendar;

/**
 * A <code>SoftLinkValue</code> provides an implementation
 * of the <code>Value</code> interface representing a soft link value
 * (i.e. an absolute or relative repository path).
 *
 * @author Stefan Guggisberg
 */
public class SoftLinkValue extends BaseValue {

  public static final int TYPE = PropertyType.SOFTLINK;

  private final String path;

  /**
   * Constructs a <code>SoftLinkValue</code> object representing an absolute
   * or relative path.
   *
   * @param path the link this <code>SoftLinkValue</code> should represent
   */
  public SoftLinkValue(String path) {
    super(TYPE);
// @todo validate path syntax
    this.path = path;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   * <p/>
   * The result is <code>true</code> if and only if the argument is not
   * <code>null</code> and is a <code>SoftLinkValue</code> object that
   * represents the same value as this object.
   *
   * @param obj the reference object with which to compare.
   * @return <code>true</code> if this object is the same as the obj
   *         argument; <code>false</code> otherwise.
   */
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj instanceof SoftLinkValue) {
      SoftLinkValue other = (SoftLinkValue) obj;
      if (path == other.path) {
        return true;
      } else if (path != null && other.path != null) {
        return path.equals(other.path);
      }
    }
    return false;
  }

  //----------------------------------------------------------------< Value >
  /**
   * @see Value#getDate
   */
  public Calendar getDate() throws ValueFormatException, IllegalStateException, RepositoryException {
    setValueConsumed();

    throw new ValueFormatException("conversion to date failed: inconvertible types");
  }

  /**
   * @see Value#getLong
   */
  public long getLong() throws ValueFormatException, IllegalStateException, RepositoryException {
    setValueConsumed();

    throw new ValueFormatException("conversion to long failed: inconvertible types");
  }

  /**
   * @see Value#getBoolean
   */
  public boolean getBoolean() throws ValueFormatException, IllegalStateException, RepositoryException {
    setValueConsumed();

    throw new ValueFormatException("conversion to boolean failed: inconvertible types");
  }

  /**
   * @see Value#getDouble
   */
  public double getDouble() throws ValueFormatException, IllegalStateException, RepositoryException {
    setValueConsumed();

    throw new ValueFormatException("conversion to double failed: inconvertible types");
  }

  /**
   * @see Value#getString
   */
  public String getString() throws ValueFormatException, IllegalStateException, RepositoryException {
    setValueConsumed();

    if (path != null) {
      return path;
    } else {
      throw new ValueFormatException("empty value");
    }
  }
}

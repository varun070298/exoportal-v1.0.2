/*
 * $Id: LongValue.java,v 1.2 2004/07/24 00:16:21 benjmestrallet Exp $
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
import java.util.Date;

/**
 * A <code>LongValue</code> provides an implementation
 * of the <code>Value</code> interface representing a long value.
 *
 * @author Stefan Guggisberg
 */
public class LongValue extends BaseValue {

  public static final int TYPE = PropertyType.LONG;

  private final Long lNumber;

  /**
   * Constructs a <code>LongValue</code> object representing a long.
   *
   * @param lNumber the long this <code>LongValue</code> should represent
   */
  public LongValue(Long lNumber) {
    super(TYPE);
    this.lNumber = lNumber;
  }

  /**
   * Constructs a <code>LongValue</code> object representing a long.
   *
   * @param l the long this <code>LongValue</code> should represent
   */
  public LongValue(long l) {
    super(TYPE);
    this.lNumber = new Long(l);
  }

  /**
   * Returns a new <code>LongValue</code> initialized to the value
   * represented by the specified <code>String</code>.
   *
   * @param s the string to be parsed.
   * @return a newly constructed <code>LongValue</code> representing the
   *         the specified value.
   * @throws ValueFormatException If the <code>String</code> does not
   *                              contain a parsable <code>long</code>.
   */
  public static LongValue valueOf(String s) throws ValueFormatException {
    try {
      return new LongValue(Long.parseLong(s));
    } catch (NumberFormatException e) {
      throw new ValueFormatException("invalid format", e);
    }
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   * <p/>
   * The result is <code>true</code> if and only if the argument is not
   * <code>null</code> and is a <code>LongValue</code> object that
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
    if (obj instanceof LongValue) {
      LongValue other = (LongValue) obj;
      if (lNumber == other.lNumber) {
        return true;
      } else if (lNumber != null && other.lNumber != null) {
        return lNumber.equals(other.lNumber);
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

    if (lNumber != null) {
      // loosing timezone information...
      Calendar cal = Calendar.getInstance();
      cal.setTime(new Date(lNumber.longValue()));
      return cal;
    } else {
      throw new ValueFormatException("empty value");
    }
  }

  /**
   * @see Value#getLong
   */
  public long getLong() throws ValueFormatException, IllegalStateException, RepositoryException {
    setValueConsumed();

    if (lNumber != null) {
      return lNumber.longValue();
    } else {
      throw new ValueFormatException("empty value");
    }
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

    if (lNumber != null) {
      return lNumber.doubleValue();
    } else {
      throw new ValueFormatException("empty value");
    }
  }

  /**
   * @see Value#getString
   */
  public String getString() throws ValueFormatException, IllegalStateException, RepositoryException {
    setValueConsumed();

    if (lNumber != null) {
      return lNumber.toString();
    } else {
      throw new ValueFormatException("empty value");
    }
  }
}

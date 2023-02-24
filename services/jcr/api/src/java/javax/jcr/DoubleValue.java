/*
 * $Id: DoubleValue.java,v 1.2 2004/07/24 00:16:21 benjmestrallet Exp $
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
 * A <code>DoubleValue</code> provides an implementation
 * of the <code>Value</code> interface representing a double value.
 *
 * @author Stefan Guggisberg
 */
public class DoubleValue extends BaseValue {

  public static final int TYPE = PropertyType.DOUBLE;

  private final Double dblNumber;

  /**
   * Constructs a <code>DoubleValue</code> object representing a double.
   *
   * @param dblNumber the double this <code>DoubleValue</code> should represent
   */
  public DoubleValue(Double dblNumber) {
    super(TYPE);
    this.dblNumber = dblNumber;
  }

  /**
   * Constructs a <code>DoubleValue</code> object representing a double.
   *
   * @param dbl the double this <code>DoubleValue</code> should represent
   */
  public DoubleValue(double dbl) {
    super(TYPE);
    this.dblNumber = new Double(dbl);
  }

  /**
   * Returns a new <code>DoubleValue</code> initialized to the value
   * represented by the specified <code>String</code>.
   *
   * @param s the string to be parsed.
   * @return a newly constructed <code>DoubleValue</code> representing the
   *         the specified value.
   * @throws ValueFormatException If the <code>String</code> does not
   *                              contain a parsable <code>double</code>.
   */
  public static DoubleValue valueOf(String s) throws ValueFormatException {
    try {
      return new DoubleValue(Double.parseDouble(s));
    } catch (NumberFormatException e) {
      throw new ValueFormatException("invalid format", e);
    }
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   * <p/>
   * The result is <code>true</code> if and only if the argument is not
   * <code>null</code> and is a <code>DoubleValue</code> object that
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
    if (obj instanceof DoubleValue) {
      DoubleValue other = (DoubleValue) obj;
      if (dblNumber == other.dblNumber) {
        return true;
      } else if (dblNumber != null && other.dblNumber != null) {
        return dblNumber.equals(other.dblNumber);
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

    if (dblNumber != null) {
      // loosing timezone information...
      Calendar cal = Calendar.getInstance();
      cal.setTime(new Date(dblNumber.longValue()));
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

    if (dblNumber != null) {
      return dblNumber.longValue();
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

    if (dblNumber != null) {
      return dblNumber.doubleValue();
    } else {
      throw new ValueFormatException("empty value");
    }
  }

  /**
   * @see Value#getString
   */
  public String getString() throws ValueFormatException, IllegalStateException, RepositoryException {
    setValueConsumed();

    if (dblNumber != null) {
      return dblNumber.toString();
    } else {
      throw new ValueFormatException("empty value");
    }
  }
}

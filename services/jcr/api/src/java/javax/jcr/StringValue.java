/*
 * $Id: StringValue.java,v 1.2 2004/07/24 00:16:21 benjmestrallet Exp $
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

import javax.jcr.util.ISO8601;
import java.util.Calendar;

/**
 * A <code>StringValue</code> provides an implementation
 * of the <code>Value</code> interface representing a string value.
 *
 * @author Stefan Guggisberg
 */
public class StringValue extends BaseValue {

  public static final int TYPE = PropertyType.STRING;

  private final String text;

  /**
   * Constructs a <code>StringValue</code> object representing a string.
   *
   * @param text the string this <code>StringValue</code> should represent
   */
  public StringValue(String text) {
    super(TYPE);
    this.text = text;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   * <p/>
   * The result is <code>true</code> if and only if the argument is not
   * <code>null</code> and is a <code>StringValue</code> object that
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
    if (obj instanceof StringValue) {
      StringValue other = (StringValue) obj;
      if (text == other.text) {
        return true;
      } else if (text != null && other.text != null) {
        return text.equals(other.text);
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

    if (text != null) {
      Calendar cal = ISO8601.parse(text);
      if (cal == null) {
        throw new ValueFormatException("not a valid date format");
      } else {
        return cal;
      }
    } else {
      throw new ValueFormatException("empty value");
    }
  }

  /**
   * @see Value#getLong
   */
  public long getLong() throws ValueFormatException, IllegalStateException, RepositoryException {
    setValueConsumed();

    if (text != null) {
      try {
        return Long.parseLong(text);
      } catch (NumberFormatException e) {
        throw new ValueFormatException("conversion to long failed", e);
      }
    } else {
      throw new ValueFormatException("empty value");
    }
  }

  /**
   * @see Value#getBoolean
   */
  public boolean getBoolean() throws ValueFormatException, IllegalStateException, RepositoryException {
    setValueConsumed();

    if (text != null) {
      return Boolean.valueOf(text).booleanValue();
    } else {
      throw new ValueFormatException("empty value");
    }
  }

  /**
   * @see Value#getDouble
   */
  public double getDouble() throws ValueFormatException, IllegalStateException, RepositoryException {
    setValueConsumed();

    if (text != null) {
      try {
        return Double.parseDouble(text);
      } catch (NumberFormatException e) {
        throw new ValueFormatException("conversion to double failed", e);
      }
    } else {
      throw new ValueFormatException("empty value");
    }
  }

  /**
   * @see Value#getString
   */
  public String getString() throws ValueFormatException, IllegalStateException, RepositoryException {
    setValueConsumed();

    if (text != null) {
      return text;
    } else {
      throw new ValueFormatException("empty value");
    }
  }
}

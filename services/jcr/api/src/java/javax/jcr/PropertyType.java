/*
 * $Id: PropertyType.java,v 1.2 2004/07/24 00:16:21 benjmestrallet Exp $
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

/**
 * The property types supported by the JCR standard.
 * <p/>
 * <p>This interface defines following property types:
 * <UL>
 * <LI>STRING
 * <LI>BINARY
 * <LI>LONG
 * <LI>DOUBLE
 * <LI>DATE
 * <LI>BOOLEAN
 * <LI>SOFTLINK
 * <LI>REFERENCE
 * </UL>
 *
 * @author Stefan Guggisberg
 */
public final class PropertyType {
  /**
   * The supported property types.
   */
  public static final int STRING = 1;
  public static final int BINARY = 2;
  public static final int LONG = 3;
  public static final int DOUBLE = 4;
  public static final int DATE = 5;
  public static final int BOOLEAN = 6;
  public static final int SOFTLINK = 7;
  public static final int REFERENCE = 8;

  /**
   * Undefined type.
   */
  public static final int UNDEFINED = 0;

  /**
   * The names of the supported property types,
   * as used in serialization.
   */
  public static final String TYPENAME_STRING = "String";
  public static final String TYPENAME_BINARY = "Binary";
  public static final String TYPENAME_LONG = "Long";
  public static final String TYPENAME_DOUBLE = "Double";
  public static final String TYPENAME_DATE = "Date";
  public static final String TYPENAME_BOOLEAN = "Boolean";
  public static final String TYPENAME_SOFTLINK = "SoftLink";
  public static final String TYPENAME_REFERENCE = "Reference";

  /**
   * Returns the name of the specified <code>type</code>,
   * as used in serialization.
   *
   * @param type the property type
   * @return the name of the specified <code>type</code>
   * @throws IllegalArgumentException if <code>type</code>
   *                                  is not a valid property type.
   */
  public static String nameFromValue(int type) {
    switch (type) {
      case STRING:
        return TYPENAME_STRING;
      case BINARY:
        return TYPENAME_BINARY;
      case BOOLEAN:
        return TYPENAME_BOOLEAN;
      case LONG:
        return TYPENAME_LONG;
      case DOUBLE:
        return TYPENAME_DOUBLE;
      case DATE:
        return TYPENAME_DATE;
      case SOFTLINK:
        return TYPENAME_SOFTLINK;
      case REFERENCE:
        return TYPENAME_REFERENCE;
      default:
        throw new IllegalArgumentException("unknown type: " + type);
    }
  }

  /**
   * Returns the numeric constant value of the type with the specified name.
   *
   * @param name the name of the property type
   * @return the numeric constant value
   * @throws IllegalArgumentException if <code>name</code>
   *                                  is not a valid property type name.
   */
  public static int valueFromName(String name) {
    if (name.equals(TYPENAME_STRING)) {
      return STRING;
    } else if (name.equals(TYPENAME_BINARY)) {
      return BINARY;
    } else if (name.equals(TYPENAME_BOOLEAN)) {
      return BOOLEAN;
    } else if (name.equals(TYPENAME_LONG)) {
      return LONG;
    } else if (name.equals(TYPENAME_DOUBLE)) {
      return DOUBLE;
    } else if (name.equals(TYPENAME_DATE)) {
      return DATE;
    } else if (name.equals(TYPENAME_SOFTLINK)) {
      return SOFTLINK;
    } else if (name.equals(TYPENAME_REFERENCE)) {
      return REFERENCE;
    } else {
      throw new IllegalArgumentException("unknown type: " + name);
    }
  }

  /**
   * private constructor to prevent instantiation
   */
  private PropertyType() {
  }
}


/*
 * $Id: BinaryValue.java,v 1.2 2004/07/24 00:16:21 benjmestrallet Exp $
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
import java.io.*;
import java.util.Calendar;

/**
 * A <code>BinaryValue</code> provides an implementation
 * of the <code>Value</code> interface representing a binary value.
 *
 * @author Stefan Guggisberg
 */
public class BinaryValue extends BaseValue {

  public static final int TYPE = PropertyType.BINARY;

  // those fields are mutually exclusive, i.e. only one can be non-null
  private byte[] streamData = null;
  private String text = null;

  /**
   * Constructs a <code>BinaryValue</code> object based on a string.
   *
   * @param text the string this <code>BinaryValue</code> should represent
   */
  public BinaryValue(String text) {
    super(TYPE);
    this.text = text;
  }

  /**
   * Constructs a <code>BinaryValue</code> object based on a stream.
   *
   * @param stream the stream this <code>BinaryValue</code> should represent
   */
  public BinaryValue(InputStream stream) {
    super(TYPE);
    this.stream = stream;
  }

  /**
   * Constructs a <code>BinaryValue</code> object based on a stream.
   *
   * @param data the stream this <code>BinaryValue</code> should represent
   */
  public BinaryValue(byte[] data) {
    super(TYPE);
    streamData = data;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   * <p/>
   * The result is <code>true</code> if and only if the argument is not
   * <code>null</code> and is a <code>BinaryValue</code> object that
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
    if (obj instanceof BinaryValue) {
      BinaryValue other = (BinaryValue) obj;
      if (text == other.text && stream == other.stream &&
          streamData == other.streamData) {
        return true;
      }
      // stream, streamData and text are mutually exclusive,
      // i.e. only one of them can be non-null
      if (stream != null) {
        return stream.equals(other.stream);
      } else if (streamData != null) {
        return streamData.equals(other.streamData);
      } else {
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

    Calendar cal = ISO8601.parse(getString());
    if (cal != null) {
      return cal;
    } else {
      throw new ValueFormatException("not a valid date format");
    }
  }

  /**
   * @see Value#getLong
   */
  public long getLong() throws ValueFormatException, IllegalStateException, RepositoryException {
    setValueConsumed();

    try {
      return Long.parseLong(getString());
    } catch (NumberFormatException e) {
      throw new ValueFormatException("conversion to long failed", e);
    }
  }

  /**
   * @see Value#getBoolean
   */
  public boolean getBoolean() throws ValueFormatException, IllegalStateException, RepositoryException {
    setValueConsumed();

    return Boolean.valueOf(getString()).booleanValue();
  }

  /**
   * @see Value#getDouble
   */
  public double getDouble() throws ValueFormatException, IllegalStateException, RepositoryException {
    setValueConsumed();

    try {
      return Double.parseDouble(getString());
    } catch (NumberFormatException e) {
      throw new ValueFormatException("conversion to double failed", e);
    }
  }

  /**
   * @see Value#getStream
   */
  public InputStream getStream() throws ValueFormatException, IllegalStateException, RepositoryException {
    setStreamConsumed();

    // build stream value if necessary
    if (streamData != null) {
      stream = new ByteArrayInputStream(streamData);
      streamData = null;
    } else if (text != null) {
      try {
        stream = new ByteArrayInputStream(text.getBytes(DEFAULT_ENCODING));
      } catch (UnsupportedEncodingException e) {
        throw new RepositoryException(DEFAULT_ENCODING + " not supported on this platform", e);
      }
      text = null;
    }

    return super.getStream();
  }

  /**
   * @see Value#getString
   */
  public String getString() throws ValueFormatException, IllegalStateException, RepositoryException {
    setValueConsumed();

    // build text value if necessary
    if (streamData != null) {
      try {
        text = new String(streamData, DEFAULT_ENCODING);
      } catch (UnsupportedEncodingException e) {
        throw new RepositoryException(DEFAULT_ENCODING + " not supported on this platform", e);
      }
      streamData = null;
    } else if (stream != null) {
      try {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int read;
        while ((read = stream.read(buffer)) > 0) {
          out.write(buffer, 0, read);
        }
        byte[] data = out.toByteArray();
        text = new String(data, DEFAULT_ENCODING);
      } catch (UnsupportedEncodingException e) {
        throw new RepositoryException(DEFAULT_ENCODING + " not supported on this platform", e);
      } catch (IOException e) {
        throw new ValueFormatException("conversion from stream to string failed", e);
      } finally {
        try {
          if (stream != null) {
            stream.close();
          }
        } catch (IOException e) {
          // ignore
        }
      }
      stream = null;
    }

    if (text != null) {
      return text;
    } else {
      throw new ValueFormatException("empty value");
    }
  }
}

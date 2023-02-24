/*
 * $Id: BaseValue.java,v 1.3 2004/08/07 16:45:05 benjmestrallet Exp $
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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * This class is the superclass of the type-specific
 * classes implementing the <code>Value</code> interfaces.
 *
 * @author Stefan Guggisberg
 * @see javax.jcr.Value
 * @see javax.jcr.StringValue
 * @see javax.jcr.LongValue
 * @see javax.jcr.DoubleValue
 * @see javax.jcr.BooleanValue
 * @see javax.jcr.DateValue
 * @see javax.jcr.BinaryValue
 * @see javax.jcr.SoftLinkValue
 * @see javax.jcr.ReferenceValue
 */
public abstract class BaseValue implements Value {

  protected static final String DEFAULT_ENCODING = "UTF-8";

  private static final short STATE_UNDEFINED = 0;
  private static final short STATE_VALUE_CONSUMED = 1;
  private static final short STATE_STREAM_CONSUMED = 2;

  private short state = STATE_UNDEFINED;

  protected final int type;

  protected InputStream stream = null;

  /**
   * Package-private default constructor.
   *
   * @param type The type of this value.
   */
  BaseValue(int type) {
    this.type = type;
  }

  /**
   * Checks if the non-stream value of this instance has already been
   * consumed (i.e. if any getter methods except <code>{@link #getStream()}</code> and
   * <code>{@link #getType()}</code> have been previously called at least once) and
   * sets the state to <code>STATE_STREAM_CONSUMED</code>.
   *
   * @throws IllegalStateException if any getter methods other than
   *                               <code>getStream()</code> and <code>getState()</code> have been
   *                               previously called at least once.
   */
  protected void setStreamConsumed() throws IllegalStateException {
    if (state == STATE_VALUE_CONSUMED) {
      throw new IllegalStateException("non-stream value has already been consumed");
    }
    state = STATE_STREAM_CONSUMED;
  }

  /**
   * Checks if the stream value of this instance has already been
   * consumed (i.e. if <code>{@link #getStream()}/<code> has been previously called
   * at least once) and sets the state to <code>STATE_VALUE_CONSUMED</code>.
   *
   * @throws IllegalStateException if <code>getStream()</code> has been
   *                               previously called at least once.
   */
  protected void setValueConsumed() throws IllegalStateException {
    if (state == STATE_STREAM_CONSUMED) {
      throw new IllegalStateException("stream value has already been consumed");
    }
    state = STATE_VALUE_CONSUMED;
  }

  //----------------------------------------------------------------< Value >
  /**
   * @see Value#getType
   */
  public int getType() {
    return type;
  }

  /**
   * @see Value#getStream
   */
  public InputStream getStream() throws ValueFormatException, RepositoryException {
    setStreamConsumed();

    if (stream != null) {
      return stream;
    }

    try {
      // convert via string
      stream = new ByteArrayInputStream(getString().getBytes(DEFAULT_ENCODING));
      return stream;
    } catch (UnsupportedEncodingException e) {
      throw new RepositoryException(DEFAULT_ENCODING + " not supported on this platform", e);
    }
  }
}

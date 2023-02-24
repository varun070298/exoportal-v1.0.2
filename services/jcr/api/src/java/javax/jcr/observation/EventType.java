/*
 * $Id: EventType.java,v 1.2 2004/07/24 00:16:23 benjmestrallet Exp $
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
package javax.jcr.observation;

/**
 * The event types used by the <code>ObservationManager</code>.
 * <p/>
 * <p>This interface defines the following event types:
 * <UL>
 * <LI>ITEM_ADDED
 * <LI>ITEM_CHANGED
 * <LI>ITEM_REMOVED
 * <LI>ITEM_VERSIONED
 * <LI>ITEM_RESTORED
 * <LI>LABEL_ADDED
 * <LI>LABEL_REMOVED
 * <LI>ITEM_LOCKED
 * <LI>ITEM_UNLOCKED
 * <LI>LOCK_EXPIRED
 * </UL>
 * <b>Level 2 only</b></code>
 * <p/>
 *
 * @author Peeter Piegaze
 * @author Stefan Guggisberg
 */
public interface EventType {

  /**
   * The event types defined by the JCR standard. Each constant is a
   * power of 2 (i.e. sets of event types can be encoded as a bitmask
   * in a <code>long</code> value).
   */
  public static final long ITEM_ADDED = 1;
  public static final long ITEM_CHANGED = 2;
  public static final long ITEM_REMOVED = 4;
  public static final long ITEM_VERSIONED = 8;
  public static final long ITEM_RESTORED = 16;
  public static final long LABEL_ADDED = 32;
  public static final long LABEL_REMOVED = 64;
  public static final long ITEM_LOCKED = 128;
  public static final long ITEM_UNLOCKED = 256;
  public static final long LOCK_EXPIRED = 512;

  /**
   * Returns the numerical constant identifying this event type. This value
   * is always a power of 2 ((i.e. sets of event types can be encoded as
   * a bitmask in a <code>long</code> value).
   *
   * @return the numerical value
   * @see ObservationManager
   */
  public long getValue();

  /**
   * Returns the descriptive name of this event type.
   *
   * @return the name
   * @see ObservationManager
   */
  public String getName();
}
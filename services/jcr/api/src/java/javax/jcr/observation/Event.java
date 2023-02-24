/*
 * $Id: Event.java,v 1.2 2004/07/24 00:16:23 benjmestrallet Exp $
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

import javax.jcr.Item;
import javax.jcr.Ticket;

/**
 * All events used by the ObservationManager system are subclassed from this interface
 * <p/>
 * <b>Level 2 only</b>
 * <p/>
 * For details see the <i>ObservationManager</i> section of the JCR standard document.
 *
 * @author Tim Anderson
 * @author Peeter Piegaze
 */
public interface Event {

  /**
   * Returns the type of this event. One of:
   * <ul>
   * <li>EventType.ITEM_ADDED
   * <li>EventType.ITEM_CHANGED
   * <li>EventType.ITEM_REMOVED
   * <li>EventType.ITEM_VERSIONED
   * <li>EventType.LABEL_ADDED
   * <li>EventType.LABEL_REMOVED
   * <li>EventType.ITEM_LOCKED
   * <li>EventType.ITEM_UNLOCKED
   * <li>EventType.LOCK_EXPIRED
   * </ul>
   *
   * @return the type of this event.
   */
  public long getType();

  /**
   * Returns the item connected with this event.
   *
   * @return an <code>Item</code>.
   */
  public Item getItem();

  /**
   * Returns the Ticket connected with this event
   *
   * @return an <code>Ticket</code>.
   */
  public Ticket getTicket();
}

/*
 * $Id: EventListener.java,v 1.2 2004/07/24 00:16:23 benjmestrallet Exp $
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
 * An event listener.
 * <p/>
 * <b>Level 2 only</b>
 * <p/>
 * An <code>EventListener</code> can be registered via the
 * <code>{@link javax.jcr.observation.ObservationManager}</code> object. Event listeners are
 * notified asynchronously, and see events after they occur and the transaction
 * is committed. An event listener only sees events for which the ticket that
 * registered it has sufficient access rights.
 *
 * @author Tim Anderson
 * @author Peeter Piegaze
 */
public interface EventListener {

  /**
   * Gets called when an event occurs.
   *
   * @param event The event recieved.
   */
  public void onEvent(Event event);
}

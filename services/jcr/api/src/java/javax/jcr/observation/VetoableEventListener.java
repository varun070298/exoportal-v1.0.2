/*
 * $Id: VetoableEventListener.java,v 1.2 2004/07/24 00:16:23 benjmestrallet Exp $
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
 * A vetoable event listener.
 * <p/>
 * <b>Level 2 only</b>
 * <p/>
 * A <code>VetoableEventListener</code> can be
 * registered via the <code>{@link javax.jcr.observation.ObservationManager}</code> object.
 * Vetoable event listeners may veto the action that the events represent. If an
 * event is vetoed, the corresponding action is forbidden by the repository.
 * Vetoable listeners are notified synchronously, and see events prior to the
 * repository acting upon them. A vetoable listener only sees events for which
 * the ticket that registered it has sufficient access rights.
 * <p/>
 * To veto an event, a class implementing the <code>VetoableEventListener</code>
 * interface returns true from the <code>onEvent</code> method. If this happens
 * during a transaction, the transaction is rolled back. If multiple vetoable
 * listeners are registered, the first listener to veto an event causes the
 * original operation to fail. No other vetoable listener is invoked.
 *
 * @author Tim Anderson
 * @author Peeter Piegaze
 */
public interface VetoableEventListener {

  /**
   * Performs some action upon receipt of the event. Returning
   * <code>false</code> will cause the operation in question to proceed,,
   * returning false will veto the operation in quetion.
   *
   * @param event
   * @return <code>true</code> to veto, <code>false</code> to proceed
   */
  public boolean onEvent(Event event);
}

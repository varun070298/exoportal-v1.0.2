/*
 * $Id: XATicket.java,v 1.2 2004/07/24 00:16:24 benjmestrallet Exp $
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
package javax.jcr.xa;

import javax.jcr.Ticket;
import javax.transaction.xa.XAResource;

/**
 * The <code>XATicket</code> interface extends the capability of
 * <code>Ticket</code> by adding access to a JCR repository's support for
 * the Java Transaction API (JTA).
 * <p/>
 * <b>Level 2 only</b>
 * <p/>
 * This support takes the form of a <code>javax.transaction.xa.XAResource</code>
 * object. The functionality of this object closely resembles that defined by
 * the standard X/Open XA Resource interface.
 * <p/>
 * This interface is used by the transaction manager; an application does not
 * use it directly.
 *
 * @author Stefan Guggisberg
 */
public interface XATicket extends Ticket {

  /**
   * Retrieves an <code>XAResource</code> object that the transaction manager
   * will use to manage this <code>XATicket</code> object's participation in
   * a distributed transaction.
   *
   * @return the <code>XAResource</code> object.
   */
  public XAResource getXAResource();
}
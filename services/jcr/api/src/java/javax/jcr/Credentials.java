/*
 * $Id: Credentials.java,v 1.2 2004/07/24 00:16:21 benjmestrallet Exp $
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

import java.io.Serializable;

/**
 * Base interface for all credentials that may be passed to the
 * {@link Repository#login(Credentials, String)
 * Repository.login(Credentials credentials, String workspaceName)} method.
 * Provides a minimal standard method for authenticating against a
 * repository (i.e., using a user ID and password). Additional attributes may
 * be used by the repository to, for example, set a token that can then be
 * passed back and forth once authentication has been completed (thus enabling
 * later authorization without re-authentication). In addition, any class that
 * implements this interface can also, of course, add its own methods to deal
 * with more advanced authentication/authorization mechanisms.
 *
 * @author Peeter Piegaze
 * @author Stefan Guggisberg
 */
public interface Credentials extends Serializable {

  /**
   * Returns the user ID.
   *
   * @return the user ID.
   */
  public String getUserId();

  /**
   * Returns the password.
   * <p/>
   * Note that this method returns a reference to the password.
   * It is the caller's responsibility to zero out the password information
   * after it is no longer needed.
   *
   * @return the password.
   */
  public char[] getPassword();

  /**
   * Stores an attribute in this credentials instance.
   *
   * @param name  a <code>String</code> specifying the name of the attribute
   * @param value the <code>Object</code> to be stored
   */
  public void setAttribute(String name, Object value);

  /**
   * Removes an attribute from this credentials instance.
   *
   * @param name a <code>String</code> specifying the name of the attribute
   *             to remove
   */
  public void removeAttribute(String name);

  /**
   * Returns the value of the named attribute as an <code>Object</code>,
   * or <code>null</code> if no attribute of the given name exists.
   *
   * @param name a <code>String</code> specifying the name of
   *             the attribute
   * @return	an <code>Object</code> containing the value of the attribute,
   * or <code>null</code> if the attribute does not exist
   */
  public Object getAttribute(String name);

  /**
   * Returns the names of the attributes available to this
   * credentials instance. This method returns an empty array
   * if the credentials instance has no attributes available to it.
   *
   * @return a string array containing the names of the stored attributes
   */
  public String[] getAttributeNames();
}
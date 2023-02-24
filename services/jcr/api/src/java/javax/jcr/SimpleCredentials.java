/*
 * $Id: SimpleCredentials.java,v 1.2 2004/07/24 00:16:21 benjmestrallet Exp $
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
import java.util.HashMap;

/**
 * <code>SimpleCredentials</code> implements the <code>Credentials</code>
 * interface and represents simple user ID/password credentials.
 *
 * @author Stefan Guggisberg
 */
public final class SimpleCredentials implements Credentials, Serializable {

  private final String userId;
  private final char[] password;
  private final HashMap attributes = new HashMap();

  /**
   * Create a new <code>SimpleCredentials</code> object, given a user ID
   * and password.
   * <p/>
   * Note that the given user password is cloned before it is stored
   * in the new <code>SimpleCredentials</code> object. This should
   * avoid the risk of having unnecessary references to password data
   * lying around in memory.
   * <p/>
   *
   * @param userId   the user ID
   * @param password the user's password
   */
  public SimpleCredentials(String userId, char[] password) {
    this.userId = userId;
    this.password = (char[]) password.clone();
  }

  /**
   * Returns the user password.
   * <p/>
   * Note that this method returns a reference to the password.
   * It is the caller's responsibility to zero out the password information
   * after it is no longer needed.
   *
   * @return the password
   */
  public char[] getPassword() {
    return password;
  }

  /**
   * Returns the user ID.
   *
   * @return the user ID.
   */
  public String getUserId() {
    return userId;
  }

  /**
   * Stores an attribute in this credentials instance.
   *
   * @param name  a <code>String</code> specifying the name of the attribute
   * @param value the <code>Object</code> to be stored
   */
  public void setAttribute(String name, Object value) {
// name cannot be null
    if (name == null) {
      throw new IllegalArgumentException("name cannot be null");
    }

// null value is the same as removeAttribute()
    if (value == null) {
      removeAttribute(name);
      return;
    }

    synchronized (attributes) {
      attributes.put(name, value);
    }
  }

  /**
   * Returns the value of the named attribute as an <code>Object</code>,
   * or <code>null</code> if no attribute of the given name exists.
   *
   * @param name a <code>String</code> specifying the name of the attribute
   * @return	an <code>Object</code> containing the value of the attribute,
   * or <code>null</code> if the attribute does not exist
   */
  public Object getAttribute(String name) {
    synchronized (attributes) {
      return (attributes.get(name));
    }
  }

  /**
   * Removes an attribute from this credentials instance.
   *
   * @param name a <code>String</code> specifying the name of the attribute
   *             to remove
   */
  public void removeAttribute(String name) {
    synchronized (attributes) {
      attributes.remove(name);
    }
  }

  /**
   * Returns the names of the attributes available to this
   * credentials instance. This method returns an empty array
   * if the credentials instance has no attributes available to it.
   * <p/>
   * <b>Level 1 and 2</b>
   * <p/>
   *
   * @return a string array containing the names of the stored attributes
   */
  public String[] getAttributeNames() {
    synchronized (attributes) {
      return (String[]) attributes.keySet().toArray(new String[attributes.keySet().size()]);
    }
  }
}

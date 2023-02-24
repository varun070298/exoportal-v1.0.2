/*
 * $Id: Repository.java,v 1.2 2004/07/24 00:16:21 benjmestrallet Exp $
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
 * The entry point into the content repository.
 * Represents the entry point into the content repository. Typically the object
 * implementing this interface will be acquired from a JNDI-compatible
 * naming and directory service.
 *
 * @author Peeter Piegaze
 * @author Stefan Guggisberg
 */
public interface Repository {

  /**
   * Login, creating a new ticket for the given credentials and specified
   * workspace. If login fails, a <code>LoginException</code> is thrown and
   * no valid ticket is generated. How the repository treats an attempt to
   * connect with <code>null</code> <code>credentials</code> and or a
   * <code>null</code> <code>workspaceName</code> is a matter of
   * implementation. Possibilities include allowing an anonymous connection in
   * cases of <code>null credentials</code> and connecting to a default
   * workspace in cases of a <code>null workspaceName</code>.
   * <p/>
   * <b>Level 2:</b>
   * <p/>
   * Returns a <code>{@link javax.jcr.xa.XATicket}</code> object in order to
   * support transactions.
   * <p/>
   *
   * @param credentials   The credentials of the user
   * @param workspaceName the name of a workspace.
   * @return a valid ticket for the user to access the repository.
   * @throws LoginException           If the login authentication fails.
   * @throws NoSuchWorkspaceException If the specified <code>workspaceName</code>
   *                                  is not recognized.
   */
  public Ticket login(Credentials credentials, String workspaceName)
      throws LoginException, NoSuchWorkspaceException;
}

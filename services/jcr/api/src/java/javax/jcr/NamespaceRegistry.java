/*
 * $Id: NamespaceRegistry.java,v 1.2 2004/07/24 00:16:21 benjmestrallet Exp $
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
 * The <code>Item</code> is the base interface of <code>{@link Node}</code>
 * and <code>{@link Property}</code>.
 *
 * @author Peeter Piegaze
 */
public interface NamespaceRegistry {

  /**
   * Set a mapping from prefix to URI. To remove an existing mapping, set its
   * prefix to null. Mappings are one-to-one: remapping an existing URI to a
   * new prefix will erase the old prefix. Similarly, remapping an existing
   * prefix to a new URI will erase the old URI. An attempt to change a
   * built-in mapping (jcr, nt, mix, pt, sv) or to remove such a mapping will
   * throw an NamespaceException.  An implementation may similarly prevent
   * the removal or alteration of additional implementation-specific,
   * built-in namespaces.
   *
   * @param prefix The prefix to be mapped
   * @param uri    The URI to be mapped
   * @throws NamespaceException  if an attempt is madeto change or delete a
   *                             built-in mapping
   * @throws RepositoryException if another error occurs
   */
  public void setMapping(String prefix, String uri) throws NamespaceException, RepositoryException;

  /**
   * Returns an array holding all currently registered prefixes.
   *
   * @return a string array
   */
  public String[] getPrefixes();

  /**
   * Returns an array holding all currently registered URIs.
   *
   * @return a string array
   */
  public String[] getURIs();

  /**
   * Returns the URI to which the given prefix is mapped.
   *
   * @param prefix a string
   * @return a string
   */
  public String getURI(String prefix) throws NamespaceException, RepositoryException;

  /**
   * Returns the prefix to which the given uri is mapped
   *
   * @param uri a string
   * @return a string
   */
  public String getPrefix(String uri) throws NamespaceException, RepositoryException;
}

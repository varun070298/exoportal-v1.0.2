/*
 * $Id: NodeTypeManager.java,v 1.2 2004/07/24 00:16:23 benjmestrallet Exp $
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
package javax.jcr.nodetype;

import javax.jcr.*;

/**
 * Allows for the retrieval of node types.
 * Accessed via {@link Workspace#getNodeTypeManager}.
 *
 * @author Peeter Piegaze
 * @author Stefan Guggisberg
 */
public interface NodeTypeManager {

  /**
   * Returns the named node type. Throws a
   * <code>NoSuchNodeTypeException</code> if a node type by that name
   * does not exist.
   *
   * @param nodeTypeName the name of an existing node type.
   * @return A <code>NodeType</code> object.
   * @throws NoSuchNodeTypeException if no node type by the given name exists.
   */
  public NodeType getNodeType(String nodeTypeName) throws NoSuchNodeTypeException;

  /**
   * Returns an iterator over all available node types (primary and mixin).
   *
   * @return An <code>NodeTypeIterator</code>.
   * @throws RepositoryException if an error occurs.
   */
  public NodeTypeIterator getAllNodeTypes() throws RepositoryException;

  /**
   * Returns an iterator over all available primary node types.
   *
   * @return An <code>NodeTypeIterator</code>.
   * @throws RepositoryException if an error occurs.
   */
  public NodeTypeIterator getPrimaryNodeTypes() throws RepositoryException;

  /**
   * Returns an iterator over all available mixin node types.
   *
   * @return An <code>NodeTypeIterator</code>.
   * @throws RepositoryException if an error occurs.
   */
  public NodeTypeIterator getMixinNodeTypes() throws RepositoryException;
}
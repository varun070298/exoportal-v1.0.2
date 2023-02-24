/*
 * $Id: Query.java,v 1.2 2004/07/24 00:16:24 benjmestrallet Exp $
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
package javax.jcr.query;

import javax.jcr.ItemExistsException;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.nodetype.ConstraintViolationException;

/**
 * A <code>Query</code> object.
 *
 * @author Peeter Piegaze
 */
public interface Query {

  /**
   * Executes this search and returns a
   * <code>{@link QueryResultIterator}</code>.
   *
   * @return a <code>QueryResultIterator</code>
   */
  public QueryResultIterator execute();

  /**
   * Returns the statement set for this query. Returns <code>null</code>
   * if no statement is currently set.
   *
   * @return the query statement.
   */
  public String getStatement();

  /**
   * Returns the language set for this query. This will be one of the
   * QueryLanguage constants returned by
   * QueryManager.getSupportedQueryLanguages(). If the query was created
   * using a mechanism outside the specification, this method may return 0.
   *
   * @return the query language.
   */
  public int getLanguage();

  /**
   * If this is a persistent query (i.e., it has been saved), returns the path of the
   * node that persists it. If this query has not been saved, it returns null.
   *
   * @return path of persisted node representing this query in content.
   */
  public String getPersistentQueryPath();

  /**
   * Creates a persistent query. The persisted query will be created as a
   * node of node type <code>nt:query</code>. This method persists the new node
   * immediately; there is no need to call <code>Node.save</code>.
   *
   * @param absPath path at which to persist this query.
   * @throws ItemExistsException          If an item already exists at the indicated position
   * @throws PathNotFoundException        If the path cannot be found
   * @throws ConstraintViolationException If creating the node would violate a
   *                                      node type (or other implementation specific) constraint.
   * @throws RepositoryException          If another error occurs.
   */
  public void save(String absPath) throws ItemExistsException, PathNotFoundException, ConstraintViolationException, RepositoryException;
}

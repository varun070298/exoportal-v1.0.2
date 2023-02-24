/*
 * $Id: QueryResult.java,v 1.2 2004/07/24 00:16:24 benjmestrallet Exp $
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

import javax.jcr.*;

/**
 * A QueryResult object. Returned in an iterator by {@link javax.jcr.query.Query#execute()
 * Query.execute()}
 *
 * @author Peeter Piegaze
 */
public interface QueryResult {

  /**
   * Returns a node meeting the search criteria. If the query executed
   * included a full text SEARCH clause then this method returns the parent
   * node of the property returned by getProperty, below.
   *
   * @return a Node
   */
  public Node getNode();

  /**
   * Returns the paths (there may be more than one) of the matching node.
   * This method may be used to avoid the overhead of accessing the matching
   * node itself, if only the paths are required.
   *
   * @return a StringIterator
   */
  public StringIterator getNodePaths();

  /**
   * Returns the UUID of the matching node. If the node does not have a UUID,
   * it returns null.
   *
   * @return a String
   */
  public String getUUID();

  /**
   * If the query executed included a full text SEARCH clause then this
   * method returns a property whose value matched the text search. If this
   * method returns a property, then the getNode method (above) must return
   * that property's parent. If the query did not include a SEARCH clause
   * then this method returns null.
   *
   * @return a Property
   */
  public Property getProperty();

  /**
   * If the query executed included a full text SEARCH clause then this
   * method returns the name of the property returned by getProperty, above.
   * This method may be used to avoid the overhead of accessing the matching
   * property itself, if only the name is required. If the query did not
   * include a SEARCH clause then this method returns null.
   *
   * @return a String
   */
  public String getPropertyName();

  /**
   * The relevance of this QueryResult. The details of this metric are
   * implementation specific. The only requirement is that the returned
   * integer must be equal to or greater than zero (i >= 0) and that higher
   * score number means “more relevant”. Implementations that do not support
   * this metric are free to return a score of zero (0) for every QueryResult.
   * <p/>
   * If the JCRQL clause ORDER BY SCORE was invoked (or a similar directive
   * in another language) then the QueryResultIterator returned by
   * Query.execute must order its returned QueryResult objects accordingly,
   * using this score value.
   *
   * @return an int
   */
  public int getScore();
}


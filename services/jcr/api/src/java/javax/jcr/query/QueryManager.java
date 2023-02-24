/*
 * $Id: QueryManager.java,v 1.2 2004/07/24 00:16:24 benjmestrallet Exp $
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

import javax.jcr.RepositoryException;

/**
 * This interface encapsulates methods for the management of search queries.
 * Provides methods for the creation and retrieval of search queries.
 *
 * @author Peeter Piegaze
 */
public interface QueryManager {

  /**
   * Creates a new query by specifying the query statement itself and the
   * language in which the query is stated.  If the query statement is
   * syntactically invalid, given the language specified, an
   * InvalidQueryException is thrown. language must specify a query language
   * from among those returned by QueryManager.getSupportedQueryLanguages(); if it is not
   * then an InvalidQueryException is thrown.
   *
   * @return A <code>Query</code> object.
   * @throws InvalidQueryException if statement is invalid or language is unsupported.
   * @throws RepositoryException   if another error occurs
   */
  public Query createQuery(String statement, int language) throws InvalidQueryException, RepositoryException;

  /**
   * Retrieves an existing persistent query. If absPath is not the path of a
   * valid persisted query an InvalidQueryException is thrown.
   *
   * @param absPath the absolute path of a persisted query.
   * @return a <code>Query</code> object.
   * @throws InvalidQueryException If <code>absPath</code> is not the path of
   *                               a valid persisted query or if another error occurs.
   * @throws RepositoryException   if another error occurs
   */
  public Query getQuery(String absPath) throws InvalidQueryException, RepositoryException;

  /**
   * Returns an array of integers identifying the supported query languages.
   * See {@link QueryLanguage}.
   *
   * @return An int array.
   */
  public int[] getSupportedQueryLanguages();
}

/*
 * $Id: QueryLanguage.java,v 1.2 2004/07/24 00:16:24 benjmestrallet Exp $
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

/**
 * The query languages supported by the JCR.
 * <p/>
 * This interface defines numeric constants for the following query languages:
 * <ul>
 * <li>JCRQL_SSES: A SQL-like query language adapted for use in the JCR and
 * providing a "Simple Search Engine Syntax" (SSES) sub-language for
 * full-text search which may be embedded in the SEARCH clause within
 * the JCRQL query string.
 * <li>XPath: An implementation of the XPath language used for element
 * selection in XML documents. It works by seeing the JCR repository as
 * an XML hierarchy. See the <i>XML Views</i> section of the
 * specification.
 * </ul>
 * <p/>
 * The JCR specification requires that at least one of the above query languages
 * be supported.
 * <p/>
 * Discovery of supported query languages is done through
 * <code>QueryManager.getSupportedQueryLanguages</code> which returns an array
 * of object implementing this interface, <code>QueryLanguage</code>.
 * <p/>
 * Implementations may support additional query languages by providing an
 * implementation of this interface that extends the number of languages.
 * <p/>
 * In particular, an implementation may choose to support a variant of JCRQL that
 * embeds a full-text search language in the SEARCH clause other than SSES.
 * In such cases, the implementor should adhere to the convention of designating
 * the language "JCRQL_XYZ", where "XYZ" is the name of the embedded full-text
 * search language.
 *
 * @author Peeter Piegaze
 * @author Stefan Guggisberg
 */
public interface QueryLanguage {

  /**
   * The query languages defined by the JCR standard.
   */
  public static final int JCRQL_SSES = 1;
  public static final int XPATH = 2;

  /**
   * Returns the numerical constant identifying this query language.
   *
   * @return the numerical value
   */
  public int getValue();

  /**
   * Returns the descriptive name of query language.
   *
   * @return the name
   */
  public String getName();
}


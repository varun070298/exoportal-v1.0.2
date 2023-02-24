/*
 * $Id: VersionHistory.java,v 1.2 2004/07/24 00:16:24 benjmestrallet Exp $
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
package javax.jcr.version;

import javax.jcr.*;
import java.util.*;

/**
 * A <code>VersionHistory</code> object wraps an <code>nt:versionHistory</code>
 * node. It provides convenient access to version history information.
 */
public interface VersionHistory {

  /**
   * Returns the root version of this version history.
   *
   * @return a <code>Version</code> object.
   * @throws RepositoryException if an error occurs.
   */
  public Version getRootVersion() throws RepositoryException;

  /**
   * Returns an iterator over all the versions within this version history.
   * The order of the returned objects will not necessarily correspond to the
   * order of versions in terms of the successor relation. To traverse the
   * version graph one must traverse the <code>jcr:successor REFERENCE</code>
   * properties starting with the root version.
   *
   * @return a <code>VersionIterator</code> object.
   * @throws RepositoryException if an error occurs.
   */
  public VersionIterator getAllVersions() throws RepositoryException;

  /**
   * Retrieves a particular version from this version history by version name.
   *
   * @param versionName a version name
   * @return a <code>Version</code> object.
   * @throws RepositoryException if an error occurs.
   */
  public Version getVersion(String versionName) throws RepositoryException;

  /**
   * Retrieves a particular version from this version history by version date.
   *
   * @param date a <code>Calendar</code> object
   * @return a <code>Version</code> object.
   * @throws RepositoryException if an error occurs.
   */
  public Version getVersion(Calendar date) throws RepositoryException;


  /**
   * Retrieves a particular version from this version history by version label.
   *
   * @param label a version label
   * @return a <code>Version</code> object.
   * @throws RepositoryException if an error occurs.
   */
  public Version getVersionByLabel(String label) throws RepositoryException;
}

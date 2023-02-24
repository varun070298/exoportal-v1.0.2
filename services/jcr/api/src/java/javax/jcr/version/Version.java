/*
 * $Id: Version.java,v 1.2 2004/07/24 00:16:24 benjmestrallet Exp $
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
 * A <code>Version</code> object wraps an <code>nt:version</code> node. It
 * provides convenient access to version information.
 */
public interface Version {

  /**
   * Returns the version name of this version. This corresponds to the value
   * of the <code>jcr:versionName</code> property in the <code>nt:version</code>
   * node that represents this version.
   *
   * @return the version name
   * @throws RepositoryException if an error occurs.
   */
  public String getVersionName() throws RepositoryException;

  /**
   * Returns the version date of this version. This corresponds to the value
   * of the <code>jcr:versionDate</code> property in the <code>nt:version</code>
   * node that represents this version.
   *
   * @return a <code>Calendar</code> object
   * @throws RepositoryException if an error occurs.
   */
  public Calendar getVersionDate() throws RepositoryException;

  /**
   * Returns the version labels of this version. This corresponds to the
   * values of the <code>jcr:versionName</code> property in the
   * <code>nt:version<code> node that represents this version.
   *
   * @return a string array
   * @throws RepositoryException if an error occurs.
   */
  public String[] getVersionLabels() throws RepositoryException;

  /**
   * Returns the version labels of this version. This corresponds to the
   * values of the <code>jcr:versionLabels</code> multi-value property in the
   * <code>nt:version</code> node that represents this version.
   *
   * @param label a version label
   * @throws RepositoryException if an error occurs.
   */
  public void addVersionLabel(String label) throws RepositoryException;

  /**
   * Removes the specified label from among the labels of this version. This
   * corresponds to removing a value from the <code>jcr:versionLabels</code>
   * multi-value property in the <code>nt:version</code> node that represents
   * this version.
   *
   * @param label a version label
   * @throws RepositoryException if an error occurs.
   */
  public void removeVersionLabel(String label) throws RepositoryException;

  /**
   * Returns the successor versions of this version. This corresponds to
   * returning all the <code>nt:version</code> nodes referenced by the
   * <code>jcr:successors</code> multi-value property in the
   * <code>nt:version</code> node that represents this version.
   *
   * @return a <code>Version</code> array.
   * @throws RepositoryException if an error occurs.
   */
  public Version[] getSuccessors() throws RepositoryException;

  /**
   * Returns the predecessor versions of this version. This corresponds to
   * returning all the <code>nt:version</code> nodes whose
   * <code>jcr:successors</code> property includes a reference to the
   * <code>nt:version</code> node that represents this version.
   *
   * @return a <code>Version</code> array.
   * @throws RepositoryException if an error occurs.
   */
  public Version[] getPredecessors() throws RepositoryException;

  /**
   * Adds the specified <code>v</code> as a successor of this version.
   * This is used to create a “merge” within the version graph (not to be
   * confused with the <code>Node.merge</code> method which operates on
   * workspace nodes). A workspace <code>Node.merge<code> may be used to
   * produce the appropriate node to be checked-in and then added as a
   * successor to more than one existing version, using this <code>addSuccessor</code> method,
   * thus performing both the semantic and the version graph parts of the
   * full “merge” operation.
   * <p/>
   * This method corresponds to adding a reference to an <code>nt:version</code>
   * node to the <code>jcr:successors</code> multi-value property of the
   * <code>nt:version</code> node that represents this version.
   * If <code>v<code> is not already in the same version history as
   * this node or if adding <code>v<code> as a successor would create a
   * cycle in the version history then an <code>VersionException</code> is thrown.
   *
   * @param v a <code>Version</code> object.
   * @throws RepositoryException if an error occurs.
   */
  public void addSuccessor(Version v) throws VersionException, RepositoryException;

  /**
   * Removes <code>v</code> from the successors of this version. This
   * method corresponds to removing a reference to an <code>nt:version</code>
   * node from the <code>jcr:successors</code> multi-value property of the
   * <code>nt:version</code> node that represents this version. If
   * <code>v</code> is not currently a direct successor of this node then an
   * <code>VersionException</code> is thrown.
   *
   * @param v a <code>Version</code> object.
   * @throws RepositoryException if an error occurs.
   */
  public void removeSuccessor(Version v) throws VersionException, RepositoryException;
}

package org.exoplatform.services.jcr.impl.core.version;


import javax.jcr.version.VersionException;
import javax.jcr.RepositoryException;
import java.util.Calendar;
import javax.jcr.version.Version;

/**
 * A <code>Version</code> object wraps an <code>nt:version</code> node. It
 * provides convenient access to version information.
 */
public class VersionImpl implements Version {

  /**
   * Returns the version name of this version. This corresponds to the value
   * of the <code>jcr:versionName</code> property in the <code>nt:version</code>
   * node that represents this version.
   *
   * @return the version name
   * @throws RepositoryException if an error occurs.
   */
  public String getVersionName() throws RepositoryException {
    throw new RepositoryException("Version is not supported yet!");
  }

  /**
   * Returns the version date of this version. This corresponds to the value
   * of the <code>jcr:versionDate</code> property in the <code>nt:version</code>
   * node that represents this version.
   *
   * @return a <code>Calendar</code> object
   * @throws RepositoryException if an error occurs.
   */
  public Calendar getVersionDate() throws RepositoryException {
    throw new RepositoryException("Version is not supported yet!");
  }

  /**
   * Returns the version labels of this version. This corresponds to the
   * values of the <code>jcr:versionName</code> property in the
   * <code>nt:version<code> node that represents this version.
   *
   * @return a string array
   * @throws RepositoryException if an error occurs.
   */
  public String[] getVersionLabels() throws RepositoryException {
    throw new RepositoryException("Version is not supported yet!");
  }

  /**
   * Returns the version labels of this version. This corresponds to the
   * values of the <code>jcr:versionLabels</code> multi-value property in the
   * <code>nt:version</code> node that represents this version.
   *
   * @param label a version label
   * @throws RepositoryException if an error occurs.
   */
  public void addVersionLabel(String label) throws RepositoryException {
    throw new RepositoryException("Version is not supported yet!");
  }

  /**
   * Removes the specified label from among the labels of this version. This
   * corresponds to removing a value from the <code>jcr:versionLabels</code>
   * multi-value property in the <code>nt:version</code> node that represents
   * this version.
   *
   * @param label a version label
   * @throws RepositoryException if an error occurs.
   */
  public void removeVersionLabel(String label) throws RepositoryException {
    throw new RepositoryException("Version is not supported yet!");
  }

  /**
   * Returns the successor versions of this version. This corresponds to
   * returning all the <code>nt:version</code> nodes referenced by the
   * <code>jcr:successors</code> multi-value property in the
   * <code>nt:version</code> node that represents this version.
   *
   * @return a <code>Version</code> array.
   * @throws RepositoryException if an error occurs.
   */
  public Version[] getSuccessors() throws RepositoryException {
    throw new RepositoryException("Version is not supported yet!");
  }

  /**
   * Returns the predecessor versions of this version. This corresponds to
   * returning all the <code>nt:version</code> nodes whose
   * <code>jcr:successors</code> property includes a reference to the
   * <code>nt:version</code> node that represents this version.
   *
   * @return a <code>Version</code> array.
   * @throws RepositoryException if an error occurs.
   */
  public Version[] getPredecessors() throws RepositoryException {
    throw new RepositoryException("Version is not supported yet!");
  }

  /**
   * Adds the specified <code>v</code> as a successor of this version.
   * This is used to create a �merge� within the version graph (not to be
   * confused with the <code>Node.merge</code> method which operates on
   * workspace nodes). A workspace <code>Node.merge<code> may be used to
   * produce the appropriate node to be checked-in and then added as a
   * successor to more than one existing version, using this <code>addSuccessor</code> method,
   * thus performing both the semantic and the version graph parts of the
   * full �merge� operation.
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
  public void addSuccessor(Version v) throws VersionException, RepositoryException {
    throw new RepositoryException("Version is not supported yet!");
  }

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
  public void removeSuccessor(Version v) throws VersionException, RepositoryException {
    throw new RepositoryException("Version is not supported yet!");
  }
}

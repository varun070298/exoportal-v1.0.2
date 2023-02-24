package org.exoplatform.services.jcr.impl.core.version;

import javax.jcr.version.Version;
import javax.jcr.version.VersionIterator;
import javax.jcr.RepositoryException;
import java.util.Calendar;
import javax.jcr.version.VersionHistory;

public class VersionHistoryImpl implements VersionHistory {

  /**
   * Returns the root version of this version history.
   *
   * @return a <code>Version</code> object.
   * @throws RepositoryException if an error occurs.
   */
  public Version getRootVersion() throws RepositoryException {
    throw new RepositoryException("Version is not supported yet!");
  }

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
  public VersionIterator getAllVersions() throws RepositoryException {
    throw new RepositoryException("Version is not supported yet!");
  }

  /**
   * Retrieves a particular version from this version history by version name.
   *
   * @param versionName a version name
   * @return a <code>Version</code> object.
   * @throws RepositoryException if an error occurs.
   */
  public Version getVersion(String versionName) throws RepositoryException {
    throw new RepositoryException("Version is not supported yet!");
  }

  /**
   * Retrieves a particular version from this version history by version date.
   *
   * @param date a <code>Calendar</code> object
   * @return a <code>Version</code> object.
   * @throws RepositoryException if an error occurs.
   */
  public Version getVersion(Calendar date) throws RepositoryException {
    throw new RepositoryException("Version is not supported yet!");
  }


  /**
   * Retrieves a particular version from this version history by version label.
   *
   * @param label a version label
   * @return a <code>Version</code> object.
   * @throws RepositoryException if an error occurs.
   */
  public Version getVersionByLabel(String label) throws RepositoryException {
    throw new RepositoryException("Version is not supported yet!");
  }
}

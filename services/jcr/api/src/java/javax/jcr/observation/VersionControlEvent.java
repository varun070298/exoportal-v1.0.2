/*
 * $Id: VersionControlEvent.java,v 1.2 2004/07/24 00:16:23 benjmestrallet Exp $
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
package javax.jcr.observation;

import javax.jcr.version.*;

/**
 * A subclass of <code>Event</code> representing a "version control" event.
 * <p/>
 * <b>Level 2 only</b>
 * <p/>
 * Will always be one of:
 * <ul>
 * <li><code>EventType.ITEM_VERSIONED</code>.
 * <li><code>EventType.LABEL_ADDED</code>.
 * <li><code>EventType.LABEL_REMOVED</code>.
 * </ul>
 * For details see the <i>ObservationManager</i> section of the JCR standard document.
 *
 * @author Tim Anderson
 * @author Peeter Piegaze
 */
public interface VersionControlEvent extends Event {

  /**
   * Returns the <code>VersionHistory</code> involved in the versioning event.
   *
   * @return a <code>VersionHistory</code> object.
   */
  public VersionHistory getVersionHistory();

  /**
   * Returns the <code>Version</code> involved in the versioning event.
   *
   * @return a <code>Version</code> object.
   */
  public Version getVersion();
}

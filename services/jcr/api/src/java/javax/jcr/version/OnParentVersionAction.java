/*
 * $Id: OnParentVersionAction.java,v 1.3 2004/07/31 11:49:26 benjmestrallet Exp $
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

/**
 * The possible actions specified by the <code>onParentVersion</code> attribute
 * in a property definition within a node type definition.
 * <p/>
 * <b>Level 2 only</b>
 * <p/>
 * This interface defines the following actions:
 * <UL>
 * <LI>COPY
 * <LI>VERSION
 * <LI>INITIALIZE
 * <LI>COMPUTE
 * <LI>IGNORE
 * <LI>ABORT
 * </UL>
 * <p/>
 * Every item (node or property) in the repository has a status indicator that
 * governs what happens to that item when its parent node is versioned. This
 * status is defined by the onParentVersion attribute in the PropertyDefNT or
 * NodeDef that applies to the item in question. This PropertyDefNT or NodeDef is
 * part of the NodeType of the parent node of the item in question.
 * <p/>
 * For example, let N be a versionable node in workspace W of repository R.
 * Furthermore, let N be of node type T, where T is a sub-type of
 * nt:versionable and T allows N to have one property called P and one child
 * node called C.
 * <p/>
 * What happens to P and C when N is versioned depends on their respective
 * OnParentVersion attribute as defined in the PropertyDefNT for P and the
 * NodeDef for C, found in the definition of node type T.
 * <p/>
 * The possible values for the OnParentVersion attribute are: COPY, VERSION,
 * INITIALIZE, COMPUTE, NOTHING and FORBIDDEN.
 * <p/>
 * The sections below describe, for each possible value of the OnParentVersion
 * attribute, what happens to C and P when,
 * <ul>
 * <li>N.checkin() is performed, creating the new version VN and adding to the
 * version history.
 * <li>N.restore(VN)  is performed, restoring the version VN.
 * </ul>
 * <p/>
 * COPY
 * <p/>
 * Child Node
 * <p/>
 * On checkin of N, C and all its descendent items, down to the leaves of the
 * subtree, will be copied to the version storage as a child subtree of VN. The
 * copy of C and its subtree will not have its own version history but will be
 * part of the state preserved in VN. C itself need not be versionable.
 * <p/>
 * On restore of VN, the copy of C and its subtree stored will be restored as
 * well, replacing the current C and its subtree in the workspace.
 * <p/>
 * Property
 * <p/>
 * On checkin of N, P will be copied to the version storage as a child of VN.
 * This copy of P is part of the state preserved in VN.
 * <p/>
 * On restore of VN, the copy of P stored as its child will be restored as
 * well, replacing the current P in the workspace.
 * <p/>
 * VERSION
 * <p/>
 * Child Node
 * <p/>
 * On checkin of N, the node VN will get a child reference to the version
 * history of C (not to C or any actual version of C). In practice, this means
 * that the root version of C's version history becomes the child of VN
 * because, as mentioned before, the root version is used as the referent when
 * a reference to the version history as a whole is required. This also
 * requires that C itself be versionable (otherwise it would not have a version
 * history). If C is not versionable then behavior of IGNORE applies on checkin
 * (see below).
 * <p/>
 * On restore of VN, if the workspace currently has an already existing node
 * corresponding to C's version history, then that instance of C becomes the
 * child of the restored N. If the workspace does not have an instance of C
 * then one is restored from C's version history. The workspace in which the
 * restore is being performed will determine which particular version of C will
 * be restored. This determination depends on the configuration of the
 * workspace and is outside the scope of this specification.
 * <p/>
 * Property
 * <p/>
 * In the case of properties, an OnParentVersion attribute of VERSION has the
 * same effect as COPY.
 * <p/>
 * INITIALIZE
 * <p/>
 * Child Node
 * <p/>
 * On checkin of N, a new node C will be created and placed in version storage
 * as a child of VN. This new C will be initialized just as it would be if
 * created normally in a workspace. No state information of the current C in
 * the workspace is preserved. The new C will not have its own version history
 * but will be part of the state preserved in VN. C itself need not be
 * versionable.
 * <p/>
 * On restore of VN, the C stored as its child will be restored as well,
 * replacing the current C in the workspace.
 * <p/>
 * Property
 * <p/>
 * On checkin of N, a new P will be created and placed in version storage as a
 * child of VN. The new P will be initialized just as it would be if created
 * normally in a workspace. The new P is part of the state preserved in VN.
 * <p/>
 * On restore of VN, the P stored as its child will be restored as well,
 * replacing the current P in the workspace.
 * <p/>
 * COMPUTE
 * <p/>
 * Child Node
 * <p/>
 * On checkin of N, a new node C will be created and placed in version storage
 * as a child of VN. This new C will be initialized according to some
 * configuration-specific procedure beyond the scope of this specification. The
 * new C will not have its own version history but will be part of the state
 * preserved in VN. C itself need not be versionable.
 * <p/>
 * On restore of VN, the C stored as its child will be restored as well,
 * replacing the current C in the workspace.
 * <p/>
 * Property
 * <p/>
 * On checkin of N, a new P will be created and placed in version storage as a
 * child of VN. The new P will be initialized according to some
 * configuration-specific procedure beyond the scope of this specification. The
 * new P is part of the state preserved in VN.
 * <p/>
 * On restore of VN, the P stored as its child will be restored as well,
 * replacing the current P in the workspace.
 * <p/>
 * IGNORE
 * <p/>
 * Child Node
 * <p/>
 * On checkin of N, no state information about C will be stored in VN.
 * <p/>
 * On restore of VN, the child node C of the current N will remain and not be
 * removed, despite not being included in the state recorded in VN, since its
 * IGNORE status tells the system to leave it alone.
 * <p/>
 * Property
 * <p/>
 * On checkin of N, no state information about P will be stored in VN.
 * <p/>
 * On restore of VN, the property P of the current N will remain and not be
 * removed, despite not being included in the state of recorded in VN, since
 * its IGNORE status tells the system to leave it alone.
 * <p/>
 * ABORT
 * <p/>
 * Child Node or Property
 * <p/>
 * On checkin of N an exception will be thrown. Having a child node or property
 * with an OnParentVersion attribute of ABORT prevents the parent node from
 * being checked-in.
 *
 * @author Peeter Piegaze
 * @author Stefan Guggisberg
 */
public final class OnParentVersionAction {

  /**
   * The action constants.
   */
  public static final int COPY = 1;
  public static final int VERSION = 2;
  public static final int INITIALIZE = 3;
  public static final int COMPUTE = 4;
  public static final int IGNORE = 5;
  public static final int ABORT = 6;

  /**
   * The names of the defined on-version actions,
   * as used in serialization.
   */
  public static final String ACTIONNAME_COPY = "COPY";
  public static final String ACTIONNAME_VERSION = "VERSION";
  public static final String ACTIONNAME_INITIALIZE = "INITIALIZE";
  public static final String ACTIONNAME_COMPUTE = "COMPUTE";
  public static final String ACTIONNAME_IGNORE = "IGNORE";
  public static final String ACTIONNAME_ABORT = "ABORT";

  /**
   * Returns the name of the specified <code>action</code>,
   * as used in serialization.
   *
   * @param action the on-version action
   * @return the name of the specified <code>action</code>
   * @throws IllegalArgumentException if <code>action</code>
   *                                  is not a valid on-version action.
   */
  public static String nameFromValue(int action) {
    switch (action) {
      case COPY:
        return ACTIONNAME_COPY;
      case VERSION:
        return ACTIONNAME_VERSION;
      case INITIALIZE:
        return ACTIONNAME_INITIALIZE;
      case COMPUTE:
        return ACTIONNAME_COMPUTE;
      case IGNORE:
        return ACTIONNAME_IGNORE;
      case ABORT:
        return ACTIONNAME_ABORT;
      default:
        throw new IllegalArgumentException("unknown on-version action: " + action);
    }
  }

  /**
   * Returns the numeric constant value of the on-version action with the
   * specified name.
   *
   * @param name the name of the on-version action
   * @return the numeric constant value
   * @throws IllegalArgumentException if <code>name</code>
   *                                  is not a valid on-version action name.
   */
  public static int valueFromName(String name) {
    if (name.equals(ACTIONNAME_COPY)) {
      return COPY;
    } else if (name.equals(ACTIONNAME_VERSION)) {
      return VERSION;
    } else if (name.equals(ACTIONNAME_INITIALIZE)) {
      return INITIALIZE;
    } else if (name.equals(ACTIONNAME_COMPUTE)) {
      return COMPUTE;
    } else if (name.equals(ACTIONNAME_IGNORE)) {
      return IGNORE;
    } else if (name.equals(ACTIONNAME_ABORT)) {
      return ABORT;
    } else {
      throw new IllegalArgumentException("unknown on-version action: " + name);
    }
  }

  /**
   * private constructor to prevent instantiation
   */
  private OnParentVersionAction() {
  }
}
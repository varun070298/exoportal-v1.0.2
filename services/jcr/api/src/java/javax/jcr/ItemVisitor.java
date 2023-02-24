/*
 * $Id: ItemVisitor.java,v 1.2 2004/07/24 00:16:21 benjmestrallet Exp $
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
package javax.jcr;

/**
 * The <code>ItemVisitor</code> defines an interface for the
 * <i>Visitor</i> design pattern (see, for example, <i>Design Patterns</i>,
 * Gamma <i>et al.</i>, 1995).
 * This interface defines two signatures of the
 * <code>visit</code> method; one taking a <code>Node</code>, the other a
 * <code>Property</code>. When an object implementing this interface is passed
 * to <code>{@link Item#accept(ItemVisitor visitor)}</code> the appropriate
 * <code>visit</code> method is automatically called, depending on whether the
 * <code>Item</code> in question is a <code>Node</code> or a
 * <code>Property</code>. Different implementations of this interface can be
 * written for different purposes. It is, for example, possible for the
 * <code>{@link #visit(Node node)}</code> method to call <code>accept</code> on the
 * children of the passed node and thus recurse through the tree performing some
 * operation on each <code>Item</code>.
 *
 * @author Peeter Piegaze
 */
public interface ItemVisitor {

  /**
   * This method is called when the <code>ItemVisitor</code> is
   * passed to the <code>accept</code> method of a <code>Property</code>.
   * If this method throws an exception the visiting process is aborted.
   *
   * @param property The <code>Property</code> that is accepting this visitor.
   * @throws RepositoryException if an error occurrs
   */
  public void visit(Property property) throws RepositoryException;

  /**
   * This method is called when the <code>ItemVisitor</code> is
   * passed to the <code>accept</code> method of a <code>Node</code>.
   * If this method throws an exception the visiting process is aborted.
   *
   * @param node The <code>Node</code that is accepting this visitor.
   * @throws RepositoryException if an error occurrs
   */
  public void visit(Node node) throws RepositoryException;
}
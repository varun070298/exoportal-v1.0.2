/*
 * $Id: TraversingItemVisitor.java,v 1.2 2004/07/24 00:16:24 benjmestrallet Exp $
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
package javax.jcr.util;

import javax.jcr.*;
import java.util.LinkedList;

/**
 * An implementaion of <code>ItemVisitor</code>.
 * <p/>
 * <b>Level 1 and 2</b>
 * <p/>
 * <code>TraversingItemVisitor</code> is an abstract utility class
 * which allows to easily traverse an <code>Item</code> hierarchy.
 * <p/>
 * <p><code>TraversingItemVisitor</code> makes use of the Visitor Pattern
 * as described in the book 'Design Patterns' by the Gang Of Four
 * (Gamma et al.).
 * <p/>
 * <p>Tree traversal is done observing the left-to-right order of
 * child <code>Item</code>s if such an order is supported and exists.
 *
 * @author Stefan Guggisberg
 */
public abstract class TraversingItemVisitor implements ItemVisitor {

  /**
   * indicates if traversal should be done in a breadth-first
   * manner rather than depth-first (which is the default)
   */
  final protected boolean breadthFirst;

  /**
   * the 0-based level up to which the hierarchy should be traversed
   * (if it's -1, the hierarchy will be traversed until there are no
   * more children of the current item)
   */
  final protected int maxLevel;

  /**
   * queues used to implement breadth-first traversal
   */
  private LinkedList currentQueue;
  private LinkedList nextQueue;

  /**
   * used to track hierarchy level of item currently being processed
   */
  private int currentLevel;

  /**
   * Constructs a new instance of this class.
   * <p/>
   * The tree of <code>Item</code>s will be traversed in a
   * depth-first manner (default behaviour).
   */
  public TraversingItemVisitor() {
    this(false, -1);
  }

  /**
   * Constructs a new instance of this class.
   *
   * @param breadthFirst if <code>breadthFirst</code> is true then traversal
   *                     is done in a breadth-first manner; otherwise it is done in a
   *                     depth-first manner (which is the default behaviour).
   */
  public TraversingItemVisitor(boolean breadthFirst) {
    this(breadthFirst, -1);
  }

  /**
   * Constructs a new instance of this class.
   *
   * @param breadthFirst if <code>breadthFirst</code> is true then traversal
   *                     is done in a breadth-first manner; otherwise it is
   *                     done in a depth-first manner (which is the default
   *                     behaviour).
   * @param maxLevel     the 0-based level up to which the hierarchy should be
   *                     traversed (if it's -1, the hierarchy will be traversed
   *                     until there are no more children of the current item)
   */
  public TraversingItemVisitor(boolean breadthFirst, int maxLevel) {
    this.breadthFirst = breadthFirst;
    if (breadthFirst) {
      currentQueue = new LinkedList();
      nextQueue = new LinkedList();
    }
    currentLevel = 0;
    this.maxLevel = maxLevel;
  }

  /**
   * Implement this method to add behaviour performed before a
   * <code>Property</code> is visited.
   *
   * @param property the <code>Property</code> that is accepting this visitor.
   * @param level    hierarchy level of this property (the root node starts at level 0)
   * @throws RepositoryException if an error occurrs
   */
  protected abstract void entering(Property property, int level)
      throws RepositoryException;

  /**
   * Implement this method to add behaviour performed before a
   * <code>Node</code> is visited.
   *
   * @param node  the <code>Node</code> that is accepting this visitor.
   * @param level hierarchy level of this node (the root node starts at level 0)
   * @throws RepositoryException if an error occurrs
   */
  protected abstract void entering(Node node, int level)
      throws RepositoryException;

  /**
   * Implement this method to add behaviour performed after a
   * <code>Property</code> is visited.
   *
   * @param property the <code>Property</code> that is accepting this visitor.
   * @param level    hierarchy level of this property (the root node starts at level 0)
   * @throws RepositoryException if an error occurrs
   */
  protected abstract void leaving(Property property, int level)
      throws RepositoryException;

  /**
   * Implement this method to add behaviour performed after a
   * <code>Node</code> is visited.
   *
   * @param node  the <code>Node</code> that is accepting this visitor.
   * @param level hierarchy level of this node (the root node starts at level 0)
   * @throws RepositoryException if an error occurrs
   */
  protected abstract void leaving(Node node, int level)
      throws RepositoryException;

  /**
   * Called when the Visitor is passed to a <code>Property</code>.
   * <p/>
   * It calls <code>TraversingItemVisitor.entering(Property, int)</code> followed by
   * <code>TraversingItemVisitor.leaving(Property, int)</code>. Implement these abstract methods to
   * specify behaviour on 'arrival at' and 'after leaving' the <code>Property</code>.
   * <p/>
   * <p/>
   * If this method throws, the visiting process is aborted.
   *
   * @param property the <code>Property</code> that is accepting this visitor.
   * @throws RepositoryException if an error occurrs
   */
  public void visit(Property property) throws RepositoryException {
    entering(property, currentLevel);
    leaving(property, currentLevel);
  }

  /**
   * Called when the Visitor is passed to a <code>Node</code>.
   * <p/>
   * It calls <code>TraversingItemVisitor.entering(Node, int)</code> followed by
   * <code>TraversingItemVisitor.leaving(Node, int)</code>. Implement these abstract methods to
   * specify behaviour on 'arrival at' and 'after leaving' the <code>Node</code>.
   * <p/>
   * If this method throws, the visiting process is aborted.
   *
   * @param node the <code>Node</code> that is accepting this visitor.
   * @throws RepositoryException if an error occurrs
   */
  public void visit(Node node)
      throws RepositoryException {
    try {
      if (!breadthFirst) {
        // depth-first traversal
        entering(node, currentLevel);
        if (maxLevel == -1 || currentLevel < maxLevel) {
          currentLevel++;
          NodeIterator nodeIter = node.getNodes();
          while (nodeIter.hasNext()) {
            nodeIter.nextNode().accept(this);
          }
          PropertyIterator propIter = node.getProperties();
          while (propIter.hasNext()) {
            propIter.nextProperty().accept(this);
          }
          currentLevel--;
        }
        leaving(node, currentLevel);
      } else {
        // breadth-first traversal
        entering(node, currentLevel);
        leaving(node, currentLevel);

        if (maxLevel == -1 || currentLevel < maxLevel) {
          NodeIterator nodeIter = node.getNodes();
          while (nodeIter.hasNext()) {
            nextQueue.addLast(nodeIter.nextNode());
          }
          PropertyIterator propIter = node.getProperties();
          while (propIter.hasNext()) {
            nextQueue.addLast(propIter.nextProperty());
          }
        }

        while (!currentQueue.isEmpty() || !nextQueue.isEmpty()) {
          if (currentQueue.isEmpty()) {
            currentLevel++;
            currentQueue = nextQueue;
            nextQueue = new LinkedList();
          }
          Item e = (Item) currentQueue.removeFirst();
          e.accept(this);
        }
        currentLevel = 0;
      }
    } catch (RepositoryException re) {
      currentLevel = 0;
      throw re;
    }
  }

  /**
   * Convenience class providing default implementations of the abstract methods
   * of <code>TraversingItemVisitor</code>.
   */
  public static class Default extends TraversingItemVisitor {

    /**
     * @see TraversingItemVisitor#TraversingItemVisitor()
     */
    public Default() {
    }

    /**
     * @see TraversingItemVisitor#TraversingItemVisitor()
     */
    public Default(boolean breadthFirst) {
      super(breadthFirst);
    }

    /**
     * @see TraversingItemVisitor#TraversingItemVisitor(boolean, int)
     */
    public Default(boolean breadthFirst, int maxLevel) {
      super(breadthFirst, maxLevel);
    }

    /**
     * @see TraversingItemVisitor#entering(Node, int)
     */
    protected void entering(Node node, int level)
        throws RepositoryException {
    }

    /**
     * @see TraversingItemVisitor#entering(Property, int)
     */
    protected void entering(Property property, int level)
        throws RepositoryException {
    }

    /**
     * @see TraversingItemVisitor#leaving(Node, int)
     */
    protected void leaving(Node node, int level)
        throws RepositoryException {
    }

    /**
     * @see TraversingItemVisitor#leaving(Property, int)
     */
    protected void leaving(Property property, int level)
        throws RepositoryException {
    }
  }
}

/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.core;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.jcr.ActionVetoedException;
import javax.jcr.BinaryValue;
import javax.jcr.BooleanValue;
import javax.jcr.DateValue;
import javax.jcr.DoubleValue;
import javax.jcr.Item;
import javax.jcr.ItemExistsException;
import javax.jcr.ItemNotFoundException;
import javax.jcr.ItemVisitor;
import javax.jcr.LongValue;
import javax.jcr.MergeException;
import javax.jcr.NoSuchWorkspaceException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.StringIterator;
import javax.jcr.StringValue;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.Value;
import javax.jcr.ValueFormatException;
import javax.jcr.access.AccessDeniedException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.nodetype.NodeDef;
import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.PropertyDef;
import javax.jcr.version.Version;
import javax.jcr.version.VersionHistory;

import org.exoplatform.services.jcr.core.ItemLocation;
import org.exoplatform.services.jcr.core.NodeData;
import org.exoplatform.services.jcr.impl.core.itemfilters.AllAcceptedFilter;
import org.exoplatform.services.jcr.impl.core.itemfilters.ItemFilter;
import org.exoplatform.services.jcr.impl.core.itemfilters.NamePatternFilter;
import org.exoplatform.services.jcr.impl.core.nodetype.NodeDefImpl;
import org.exoplatform.services.jcr.impl.core.nodetype.NodeTypeImpl;
import org.exoplatform.services.jcr.impl.core.nodetype.NodeTypeManagerImpl;
import org.exoplatform.services.jcr.impl.util.EntityCollection;
import org.exoplatform.services.jcr.impl.util.PropertyConvertor;

/**
 * Created by The eXo Platform SARL.
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: NodeImpl.java,v 1.59 2004/11/02 18:36:32 geaz Exp $
 */

public class NodeImpl extends ItemImpl implements NodeData {

  private List properties;


  /**
   * Level1 constructor
   */
  public NodeImpl(String absPath) throws PathNotFoundException {
    this(absPath, "nt:default");
  }

  public NodeImpl(String absPath, String type) throws PathNotFoundException {
    super(absPath);
    properties = new ArrayList();
    try {
      properties.add(new PropertyImpl(location.getPath() + "/jcr:primaryType",
          new StringValue(type), PropertyType.STRING));
    } catch (Exception e) {
      throw new PathNotFoundException("NodeImpl() failed. Reason: " + e.getMessage());
    }

  }

  public NodeImpl(String absPath, List props) throws PathNotFoundException {
    super(absPath);
    this.properties = props;
  }

  // Deep copy
  public NodeImpl(NodeImpl node) throws PathNotFoundException, RepositoryException {
    super(node.getPath());
    refresh(node);

  }


  /**
   * Returns the node at <code>relPath</code> relative to <code>this</code> node.
   * The properties and child nodes of the returned node can then be read
   * (and if permissions allow) changed and written. However, any changes
   * made to this node, its properties or its child nodes
   * (and their properties and child nodes, etc.) will only be persisted to
   * the repository upon calling save. Within the scope of a single
   * <code>Ticket</code> object, if a node has been acquired with
   * <code>getNode</code>, any subsequent call of <code>getNode</code>
   * re-acquiring the same node will return a reference to same object,
   * not a new copy.
   *
   * @param relPath The relative path of the node to retrieve.
   * @return The node at <code>relPath</code>.
   * @throws PathNotFoundException If no node exists at the
   *                               specified path.
   * @throws RepositoryException   If another error occurs.
   */
  public Node getNode(String path) throws PathNotFoundException, RepositoryException {
    ItemLocation loc = new ItemLocation(this.location.getPath(), path);
    if (loc.equals(this.location))
      return this;
    else
      return ticket.getNodeByAbsPath(loc.getPath());
  }

  /**
   * 6.2.1
   * Gets all child nodes of this node. Does not include
   * properties of this node. The same save and re-acquisition
   * semantics apply as with getNode.
   */
  public NodeIterator getNodes() throws RepositoryException {
    log.debug("get nodes");
    return retrieveChildNodes(new AllAcceptedFilter());
  }

  /**
   * 6.2.1
   * Gets all child nodes of this node that match
   * namePattern. The pattern may be a full name or a
   * partial name with one or more wildcard characters
   * ("*"), or a disjunction (using the �|� character to
   * represent logical OR) of these. For example,
   * N.getNodes("jcr:* | myapp:report")
   * would return a NodeIterator holding all child nodes of
   * N that are either called myapp:report or begin with the
   * prefix jcr:. The pattern does not represent paths, i.e.,
   * it may not contain / characters.
   * The same save and re-acquisition semantics apply as with getNode.
   */
  public NodeIterator getNodes(String namePattern) throws RepositoryException {
    return retrieveChildNodes(new NamePatternFilter(namePattern));
  }

  /**
   * Returns the property at <code>relPath</code> relative to <code>this</code>
   * node. The same <code>save</code> and re-acquisition
   * semantics apply as with <code>{@link #getNode(String)}</code>.
   *
   * @param relPath The relative path of the property to retrieve.
   * @return The property at <code>relPath</code>.
   * @throws PathNotFoundException If no property exists at the
   *                               specified path.
   * @throws RepositoryException   If another error occurs.
   */
  public Property getProperty(String path) throws PathNotFoundException, RepositoryException {
    ItemLocation loc = new ItemLocation(this.location.getPath(), path);
    if (!this.location.getPath().equals(loc.getParentPath())) {
      Node node = ticket.getNodeByAbsPath(loc.getParentPath());
      return node.getProperty(loc.getName());
    }

    EntityCollection res = retrieveProperties(new NamePatternFilter(loc.getName()));
    if (res.hasNext()) {
      PropertyImpl property = (PropertyImpl) res.nextProperty();
      property.setTicket(ticket);
      return property;
    } else
      throw new PathNotFoundException("No property at the path '" + loc.getPath() + "'");
  }


  /**
   * 6.2.1
   * Gets all properties of this node. Does not include child
   * nodes of this node. The same save and re-acquisition
   * semantics apply as with getNode.
   */
  public PropertyIterator getProperties() {
    return retrieveProperties(new AllAcceptedFilter());
  }

  /**
   * 6.2.1
   * Gets all properties of this node that match
   * namePattern. The pattern may be a full name or a
   * partial name with one or more wildcard characters
   * ("*"), or a disjunction (using the �|� character to
   * represent logical OR) of these. For example,
   * N.getProperties("jcr:* | myapp:name")
   * would return a PropertyIterator holding all
   * properties of N that are either called myapp:name or
   * begin with the prefix jcr. The pattern does not
   * represent paths, i.e., it may not contain / characters.
   * The same save and re-acquisition semantics apply as with getNode.
   */
  public PropertyIterator getProperties(String namePattern) {
    return retrieveProperties(new NamePatternFilter(namePattern));
  }

  /**
   * 6.2.1
   * Returns the first property of this node found with the
   * specified value. What makes a particular property
   * "first" (that is, the search order) is implementation
   * dependent. If the specified value and the value of a
   * property are of different types then a conversion is
   * attempted before the equality test is made. Returns
   * null if no such property is found. The same save and
   * re-acquisition semantics apply as with getNode.
   */
  public Property findProperty(Value value) throws RepositoryException {
    PropertyIterator iterator = findProperties(value);
    if (iterator.hasNext())
      return iterator.nextProperty();
    return null;
  }

  /**
   * 6.2.1
   * Returns all properties of this node with the specified
   * value. If the specified value and the value of a
   * property are of different types then a conversion is
   * attempted before the equality test is made. Returns an
   * empty iterator if no such property could be found. The
   * same save and re-acquisition semantics apply as with getNode.
   */
  public PropertyIterator findProperties(Value value) throws RepositoryException {
    log.debug("find properties");
    if (value == null)
      throw new RepositoryException("Value must not be null");

    ArrayList props = new ArrayList();
    PropertyIterator propsI = retrieveProperties(new AllAcceptedFilter());
    while (propsI.hasNext()) {
      PropertyImpl prop = (PropertyImpl) propsI.nextProperty();
      Value convertedValue = null;
      try {
        convertedValue = PropertyConvertor.convert(prop.getValue(), value.getType());
        log.debug("find properties - test if "+value+" of "+prop+" equals "+convertedValue+" result="+value.equals(convertedValue));

        if (value.equals(convertedValue))
          props.add(prop);

      } catch (IllegalStateException e) {
      } catch (RepositoryException e) {
      }
    }
    return new EntityCollection(props);
  }

  public ItemIterator getItems(ItemFilter filter) {
    List items = retrieveChildNodes(filter).getList();
    items.addAll(retrieveProperties(filter).getList());
    return new EntityCollection(items);
  }

  /**
   * 6.2.1
   * A node's type can specify a
   * maximum of one of its child items (child node or
   * property) as its primary child item. This method
   * traverses the chain of primary child items of this node
   * until it either encounters a property or encounters a
   * node that does not have a primary child item. It then
   * returns that property or node. If this node itself (the
   * one that this method is being called on) has no
   * primary child item then this method throws an
   * ItemNotFoundException. The same save and re-acquisition
   * semantics apply as with getNode.
   */
  public Item getPrimaryItem() throws ItemNotFoundException, RepositoryException {
    NodeImpl node = this;

    // Traverse children...
    ItemImpl item = findPrimaryItem(node);

//    System.out.println("ITEM returned " + item.getPath());

    if (item.equals(this))
      throw new ItemNotFoundException("NodeImpl.getPrimaryItem() Node does not have the primary items!");
    else
      return item;
  }

  /**
   * Returns the UUID of this node as recorded in the node's jcr:UUID
   * property. This method only works on nodes of mixin node type
   * <code>mix:referenceable</code>. On nonreferenceable nodes, this method
   * throws an <code>UnsupportedRepositoryOperationException</code>.
   *
   * @return the UUID of this node
   * @throws UnsupportedRepositoryOperationException
   *                             If this node nonreferenceable.
   * @throws RepositoryException If another error occurs.
   */
  public String getUUID() throws UnsupportedRepositoryOperationException, RepositoryException {
    try {
      return getProperty("jcr:uuid").getString();
    } catch (PathNotFoundException e) {
      throw new UnsupportedRepositoryOperationException("that node should have mix:referenceable mixin type", e);
    }
  }


  /**
   * 6.2.1
   * Returns true if a node exists at path and false otherwise.
   */
  public boolean hasNode(String relPath) throws RepositoryException {
    try {
      getNode(relPath);
    } catch (PathNotFoundException e) {
//      System.out.println("PATHHHHHHHHHHHHHHHHHHH NOTTTTTTTTTTTTTT F "+relPath);
      return false;
    }
    return true;
  }

  /**
   * 6.2.1
   * Returns true if this node has one or more child nodes.
   * Returns false otherwise.
   */
  public boolean hasNodes() throws RepositoryException {
    return getNodes().hasNext();
  }

  /**
   * 6.2.1
   * Returns true if a property exists at path and false otherwise.
   */
  public boolean hasProperty(String relPath) throws RepositoryException {
    try {
      if (getProperty(relPath) != null)
        return true;
    } catch (RepositoryException e) {
    }
    return false;
  }

  /**
   * 6.2.1
   * Returns true if this node has one or more properties.Returns false otherwise.
   */
  public boolean hasProperties() throws RepositoryException {
    return getProperties().hasNext();
  }

  /**
   * 6.3.1
   * Creates a new node at path. The new node will only be
   * persisted to the workspace when save is called on the new
   * node's parent node or higher-order ancestor node
   * (parent's parent, etc.) and if the structure of the new node
   * (its child nodes and properties) meets the constraint
   * criteria of the parent node's node type.
   * If path implies intermediary nodes that do not exist, then
   * a PathNotFoundException is thrown immediately (i.e., not at save).
   * If an item already exists at path then an
   * ItemExistsException is thrown on save.
   * If an attempt is made to add a node as a child of a
   * property then a ConstraintViolationException is
   * thrown immediately (not on save).
   * Node type validation is done at save. At that time, the
   * new node's node type will be determined by the node type
   * of its parent. If that node type does not unambiguously
   * determine the child node's type then the system checks if
   * the built-in node type nt:default is acceptable. If so,
   * then it is assigned. If not, then a
   * ConstraintViolationException will be thrown on save.
   */
  public Node addNode(String path) throws ItemExistsException, PathNotFoundException,
      ConstraintViolationException, RepositoryException {

    ItemLocation loc = new ItemLocation(location.getPath(), path);
    String name = loc.getName();
    NodeImpl parent = getParentToAddNodeTo(loc.getParentPath());

    if (!parent.getPrimaryNodeType().canAddChildNode(name)) {
      ticket.getNodesManager().addValidationError(loc.getPath(),
          new ConstraintViolationException("Can't add node " + name) );
    }


    // Find node Type
    String nodeTypeName = findNodeType(parent, name);

    if (nodeTypeName == null)
      throw new ConstraintViolationException("Can not define node type : for <" + name + "> !");

    return addNode(path, nodeTypeName);
  }

  private String findNodeType(NodeImpl parent, String name) {
    String nodeTypeName = null;
    NodeDef[] nodeDefs = parent.getPrimaryNodeType().getChildNodeDefs();
    boolean residualFlag = false;
    if (nodeDefs != null) {
      for (int i = 0; i < nodeDefs.length; i++) {
        NodeDef nodeDef = nodeDefs[i];
        if (nodeDef.getName() != null) {
          if (nodeDef.getName().equals(name)) {
            return nodeDef.getDefaultPrimaryType().getName();
          }
        } else {
          residualFlag = true;
        }
      }
      //manage residual definitions
      if (residualFlag) {
        for (int i = 0; i < nodeDefs.length; i++) {
          NodeDef nodeDef = nodeDefs[i];
          if (nodeDef.getName() == null) {
            return nodeDef.getDefaultPrimaryType().getName();
          }
        }
      }
    }
    return nodeTypeName;
  }

  /**
   * 6.3.1
   * Creates a new node at path of the specified node type.
   * The same as addNode(String path) except that the type
   * of the new node is explicitly specified.
   * If the node type of the new node's parent is incompatible
   * with the specified node type then a
   * ConstraintViolationException is thrown on save().
   * If nodeTypeName is not recognized, then a
   * NoSuchNodeTypeException is thrown on save.
   */
  public Node addNode(String path, String nodeTypeName)
      throws ItemExistsException, PathNotFoundException, NoSuchNodeTypeException,
      ConstraintViolationException, RepositoryException {
    // Primary Node type Name!!!!
    NodeType type = null;

    ItemLocation loc = new ItemLocation(location.getPath(), path);
    String name = loc.getName();
    NodeImpl parent = getParentToAddNodeTo(loc.getParentPath());

    if( ticket.getNodesManager().getNodeByPath(loc.getPath()) != null )
      ticket.getNodesManager().addValidationError(loc.getPath(), new ItemExistsException("Can't add node " + path + ". The node "
          + loc.getPath() + " already exists") );

    NodeType parentNodeType = parent.getPrimaryNodeType();
    if (!parentNodeType.canAddChildNode(name, nodeTypeName)) {
      log.debug("Can't add node " + name + " type " + nodeTypeName+ " to parent type "+parentNodeType.getName());
      ticket.getNodesManager().addValidationError(loc.getPath(), new ConstraintViolationException("Can't add node " + name
          + " type " + nodeTypeName+ " to parent type "+parentNodeType.getName()));
    }

    // NodeType
    if (nodeTypeName != null) {
      type = NodeTypeManagerImpl.getInstance().getNodeType(nodeTypeName);
    } else {
      throw new ConstraintViolationException("Add Node failed: Node Type <" + nodeTypeName + "> not found!");
    }

    if (type.isMixin())
      throw new ConstraintViolationException("Add Node failed: Node Type <" + nodeTypeName + "> is MIXIN type!");

    log.debug("Add node. Parent: " + parent + " path: " + path + " type " + type);
    NodeImpl node = createNode(parent, name, type);

    return node;
  }

  private NodeImpl getParentToAddNodeTo(String parentPath) throws PathNotFoundException, ConstraintViolationException {
    try {
      return (NodeImpl) getNode(parentPath);
    } catch (RepositoryException e) {
      try {
        getProperty(parentPath);
      } catch (RepositoryException e1) {
        throw new PathNotFoundException("Add Node failed: parent node at <" + parentPath + "> not found!");
      }
      throw new ConstraintViolationException("Can not add a node to a property item");
    }
  }

  /**
   * Adds the existing node at <code>absPath</code> as child of this node, thus adding
   * <code>this</code> node as an addional parent of the node at <code>absPath</code>.
   * <p/>
   * This change will be persisted (if valid) on <code>save</code>.
   * <p/>
   * If the node at <code>absPath</code> is not of mixin type
   * <code>mix:referenceable</code>, a <code>ConstraintViolationException</code>
   * will be thrown on <code>save</code>.
   * <p/>
   * The name of the new child node as accessed from <code>this</code> node
   * will be the same as its current name in <code>absPath</code> (that is, the last path
   * segment in that <code>absPath</code>).
   *
   * @param absPath The absolute path of the new child node.
   * @return The new child node.
   * @throws PathNotFoundException If no node exists at <code>absPath</code>.
   * @throws RepositoryException   In level 2: If another error occurs.
   */
  public Node addExistingNode(String absPath) throws PathNotFoundException, RepositoryException {
    ItemLocation location = new ItemLocation(absPath);
    return addExistingNode(absPath, location.getName());
//    throw new RepositoryException("Add existing node is not supported yet!");
  }

  /**
   * The same as <code>addExistingNode(String absPath)</code> except that the
   * node at <code>absPath</code> adopts <code>newName</code> in the path
   * where this node is its parent.
   *
   * @param absPath The absolute path of the new child node.
   * @param newName The new name for this node when referenced as a child of this node.
   * @return The new child node.
   * @throws PathNotFoundException If no node exists at <code>absPath</code>.
   * @throws RepositoryException   If another error occurs.
   */
  public Node addExistingNode(String absPath, String newName) throws PathNotFoundException, RepositoryException {

    NodeImpl realNode = (NodeImpl)ticket.getNodesManager().getNodeByPath(absPath);
    if(realNode == null)
       throw new PathNotFoundException("There are no node at "+realNode);

    Property uuid = realNode.getPermanentProperty("jcr:uuid");
    if (uuid == null) {
        ticket.getNodesManager().addValidationError(absPath, new ConstraintViolationException("Can't add reference as " + realNode + " is not referenceable."));
        log.debug("Can't add reference as " + absPath + " is not referenceable.");
     }

    String refPath = new ItemLocation(getPath(), newName).getPath();
//    NodeImpl node = new NodeImpl(refPath, realNode.getPermanentProperties());

    ticket.getNodesManager().addReference(uuid.getString(), refPath);

    return realNode;

//    throw new RepositoryException("Add existing node is not supported yet!");
  }


  /**
   * 6.3.2
   * Sets the property called name to the specified value.
   * The property type of the property will be that specified
   * by the node type of this node.
   * If the property type of the supplied Value object is
   * different from that required then a best-effort
   * conversion is attempted. If the conversion fails, a
   * ConstraintViolationException is thrown on the next save.
   * If the node type of this node does not indicate a
   * specific property type, then the property type of the
   * supplied Value object is used.
   * If the property already exists (has previously been set)
   * it assumes both the new value and new property type.
   * To erase a property, use Node.remove.
   * To persist the addition, removal or change of a
   * property to the workspace, save must be called on
   * this node or a higher-order ancestor of the property.
   */
  public Property setProperty(String name, Value value) throws ValueFormatException, RepositoryException {
    if (value == null)
      throw new ValueFormatException("Set Property failed: value type could not be recognized!");

    return setProperty(name, value, value.getType());
  }

  /**
   * 6.3.2
   */
  public Property setProperty(String name, Value value, int type)
      throws ValueFormatException, RepositoryException {
    Value[] array = {value};
    return setProperty(name, array, type);
  }


  /**
   * Sets the specified property to the specified array of values (assuming it
   * is a multi-value property) and the specified type. Same as
   * <code>{@link #setProperty(String name, Value value, int type)}<code>
   * except that an array of <code>Value</code> objects is assigned instead
   * of a single <code>Value</code>. If an array of more than one element
   * is passed and the property is not  multi-valued then a
   * <code>ValueFormatException</code> is thrown.
   *
   * @param name   the name of the property to be set.
   * @param values an array of <code>Value</code> objects.
   * @param type   a <code>PropertyType</code> constant.
   * @return the updated <code>Property</code> object.
   * @throws ValueFormatException if the type or format of <code>values</code>
   *                              is incompatible with the type of the specified property or if
   *                              <code>values</code> is incompatible with (i.e. can not be converted to)
   *                              <code>type</code> or if an array of more than one value is being passed
   *                              and the property is not multi-valued.
   * @throws RepositoryException  if another error occurs.
   */
  public Property setProperty(String name, Value[] values, int type)
      throws ValueFormatException, RepositoryException {
    if (name == null)
      throw new IllegalArgumentException("Property name can not be null");

    if(values.length > 1){
      PropertyDef propertyDef = ((NodeTypeImpl)getPrimaryNodeType()).getPropertyDef(name);
      if(propertyDef != null && !propertyDef.isMultiple())
        throw new ValueFormatException("Can not add multiple value to this property");
    }

    values = PropertyConvertor.convert(values, type);

    ItemLocation loc = new ItemLocation(location.getPath(), name);

    if (values.length == 1) {
      if (!getPrimaryNodeType().canSetProperty(name, values[0]))
        ticket.getNodesManager().addValidationError(loc.getParentPath(),
            new ConstraintViolationException("Can't set prop " + name));
    }

    PropertyDef[] propertyDef = getPrimaryNodeType().getPropertyDefs();
    int residualNb = 0;
    Collection residuals = new ArrayList();
    for (int i = 0; i < propertyDef.length; i++) {
      PropertyDef def = propertyDef[i];
      if (def != null) {
        if (def.getName() == null) {
          //residual property definition
          residuals.add(def);
          residualNb++;
        } else if (name.equals(def.getName())) {
          int requiredType = def.getRequiredType();
          if (requiredType != type && requiredType != PropertyType.UNDEFINED) {
            return updateProperty(name, requiredType, PropertyConvertor.convert(values, requiredType));
          } else {
            return updateProperty(name, type, values);
          }
        }
      }
    }
    if (residualNb == 1) {
      int requiredType = ((PropertyDef) residuals.iterator().next()).getRequiredType();
      if (requiredType != type && requiredType != PropertyType.UNDEFINED) {
        return updateProperty(name, requiredType, PropertyConvertor.convert(values, requiredType));
      } else {
        return updateProperty(name, type, values);
      }
    } else if (residualNb > 1) {
      for (Iterator iterator = residuals.iterator(); iterator.hasNext();) {
        PropertyDef def = (PropertyDef) iterator.next();
        int requiredType = def.getRequiredType();
        if (requiredType == type) {
          return updateProperty(name, type, values);
        }
      }
      throw new RepositoryException("Can not resolve property type due to multi residuals");
    }
    PropertyImpl prop = updateProperty(name, type, values);
    return prop;
  }

  /**
   * 6.3.2
   */
  public Property setProperty(String name, String value, int type)
      throws ValueFormatException, RepositoryException {
    return setProperty(name, new StringValue(value), type);
  }

  /**
   * Sets the specified property to the specified array of values (assuming it
   * is a multi-value property) and the specified type. Same as
   * <code>{@link #setProperty(String name, String[] value, int type)}<code>
   * except that an array of <code>String</code> objects is assigned instead
   * of a single <code>String</code>. If an array of more than one element
   * is passed and the property is not multi-valued then a
   * <code>ValueFormatException</code> is thrown.
   *
   * @param name   the name of the property to be set.
   * @param values an array of <code>String</code> objects.
   * @param type   a <code>PropertyType</code> constant.
   * @return the updated <code>Property</code> object.
   * @throws ValueFormatException if the type or format of <code>values</code>
   *                              is incompatible with the type of the specified property or if
   *                              <code>values</code> is incompatible with (i.e. can not be converted to)
   *                              <code>type</code> or if an array of more than one value is being passed
   *                              and the property is not multi-valued.
   * @throws RepositoryException  if another error occurs.
   */
  public Property setProperty(String name, String[] values, int type)
      throws ValueFormatException, RepositoryException {
    Value[] convertedValues = new Value[values.length];
    for (int i = 0; i < values.length; i++) {
      String value = values[i];
      convertedValues[i] = new StringValue(value);
    }
    return setProperty(name, convertedValues, type);
  }

  /**
   * 6.3.2
   */
  public Property setProperty(String name, String value) throws ValueFormatException, RepositoryException {
    return setProperty(name, new StringValue(value), PropertyType.STRING);
  }

  /**
   * 6.3.2
   */
  public Property setProperty(String name, InputStream value) throws ValueFormatException, RepositoryException {
    return setProperty(name, new BinaryValue(value), PropertyType.BINARY);
  }

  /**
   * 6.3.2
   */
  public Property setProperty(String name, boolean value) throws ValueFormatException, RepositoryException {
    return setProperty(name, new BooleanValue(value), PropertyType.BOOLEAN);
  }

  /**
   * 6.3.2
   */
  public Property setProperty(String name, Calendar value) throws ValueFormatException, RepositoryException {
    return setProperty(name, new DateValue(value), PropertyType.DATE);
  }

  /**
   * 6.3.2
   */
  public Property setProperty(String name, double value) throws ValueFormatException, RepositoryException {
    return setProperty(name, new DoubleValue(value), PropertyType.DOUBLE);
  }

  /**
   * 6.3.2
   */
  public Property setProperty(String name, long value) throws ValueFormatException, RepositoryException {
    return setProperty(name, new LongValue(value), PropertyType.LONG);
  }

  /**
   * 6.3.3
   * Removes (that is, actually erases) the item at path and its
   * subtree. As with addNode, setProperty and so forth, the change
   * (in this case the removal of the item) is only persisted to the
   * workspace when save is called on the parent (or higher order
   * ancestor) of the removed item.
   * In level 2: Removes the reference along path to the item. If this
   * item is a node that has other references to it (due to hard links),
   * then all of these other references to the node are preserved. If
   * the item at path has no other references to it then it is actually
   * erased, along with any child items that also have no other
   * references and so on down the subtree. As in level 1, save is
   * required to persist and change to the workspace
   * then all of these other references to the node are preserved. If
   * the item at path has no other references to it then it is actually
   * erased, along with any child items that also have no other
   * references and so on down the subtree. As in level 1, save is
   * required to persist and change to the workspace
   */
  public void remove(String path) throws PathNotFoundException, RepositoryException {
    // TODO REL PATH only!
    log.debug("Node.remove(" + path + ")") ;
    ItemLocation loc = new ItemLocation(location.getPath(), path);
    String name = loc.getName();
    NodeImpl parent = (NodeImpl) getNode(loc.getParentPath());

    if (parent == null)
      throw new PathNotFoundException("Remove failed: parent node for <" + path + "> not found!");

    if (!parent.getPrimaryNodeType().checkRemoveItem(name))
      ticket.getNodesManager().addValidationError(loc.getPath(),
          new ConstraintViolationException("Can't remove item " + name));

    ItemIterator items = parent.getItems(new NamePatternFilter(name));
    if (!items.hasNext()) {
      log.debug("Can not find item : " + name + " with parent : " + parent.getPath());
      return;
    }

    ItemImpl item = (ItemImpl)items.nextItem();
    item.setTicket(ticket);
    if(item.isNode())
      ticket.getNodesManager().delete((NodeImpl)item);
    else {
      parent.removePermanentProperty(name);
      ticket.getNodesManager().update(parent);
    }

//    ticket.getNodesManager().delete(item);

  }


  /**
   * 6.3.4
   * Persists all changes to this node and its descendants made
   * since the last save or get of this node. The scope of a save
   * is the subtree rooted at this node.
   * Constraints mandated by node types are validated on
   * save. If validation fails all changes made since the last
   * save or get are rolled-back and a
   * ConstraintViolationException is thrown.
   * The save method allows a node to be in a temporarily
   * invalid state while it is being "built", that is, while it is
   * having properties and child nodes added to it. Once it is
   * "finished", save is called and the structure is validated.
   * An AccessDeniedException will be thrown if an attempt is
   * made to save changes for which the current user does not
   * have sufficient access rights.
   * A StaleItemException will be thrown if an attempt is
   * made to save a change to a node or property that has
   * been changed (and saved) by another user, in the
   * intervening period since this user acquired it.
   * intervening period since this user acquired it.
   */
  public void save(boolean shallow) throws AccessDeniedException, ConstraintViolationException,
      ActionVetoedException, RepositoryException {
    if(!ticket.getNodesManager().hasPersistedParent(this))
      throw new ConstraintViolationException("parent node does not exist or is not persisted yet");    
    ticket.getNodesManager().validate(this, shallow);    
    ticket.getNodesManager().commit(this, shallow);
  }


  /**
   * Returns the primary node type of this node.
   *
   * @return a <code>NodeType</code> object.
   */
  public NodeType getPrimaryNodeType() {
//    return getNodeType();
    PropertyImpl nodeTypeProp = (PropertyImpl) getPermanentProperty ("jcr:primaryType");
    NodeType type;
    if (nodeTypeProp != null) {
      try {
        type = NodeTypeManagerImpl.getInstance().getNodeType(nodeTypeProp.getString());
      } catch (NoSuchNodeTypeException e) {
        throw new RuntimeException("NodeImpl.getNodeType() error: NodeType <" + nodeTypeProp.toString() +
            "> not found in NodeTypeManager!", e);
      }
      return type;
    } else {
      log.warn("getNodeType() jcr:primaryType not found for " + getPath() + " nt:default is applied.");
      return new org.exoplatform.services.jcr.impl.core.nodetype.nt.Default();
    }
  }

  /**
   * Returns an array of NodeType objects representing the mixin node types
   * assigned to this node.
   *
   * @return a <code>NodeType</code> object.
   */
  public NodeType[] getMixinNodeTypes() {

    if(getPermanentProperty("jcr:mixinType") == null)
        return new NodeType[0];

    try {
       Value[] mixinNodeTypeNames = getPermanentProperty("jcr:mixinType").getValues();
       NodeType[] mixinTypes = new NodeType[mixinNodeTypeNames.length];
       for(int i=0; i<mixinNodeTypeNames.length; i++) {
           mixinTypes[i] = NodeTypeManagerImpl.getInstance().getNodeType(mixinNodeTypeNames[i].getString());

       }
       return mixinTypes;
    } catch (Exception e) {
       e.printStackTrace();
       log.error("NoSuchNodeTypeException (getMixinNodeTypes) "+e);
       return null;
    }

//    return getDefinition().getDefaultMixinTypes();
  }

  /**
   * 6.10.3
   * Returns true if this node is of the specified node type or a
   * subtype thereof. Returns false otherwise.
   */
  public boolean isNodeType(String nodeTypeName) throws RepositoryException {
    NodeType testNodeType;
    try {
        testNodeType = NodeTypeManagerImpl.getInstance().getNodeType(nodeTypeName);
     } catch (NoSuchNodeTypeException e) {
        log.error("Node.isNodeType() No such node: "+nodeTypeName);
        return false;
//         throw new RepositoryException("No such node: "+nodeTypeName);
    }

    if(NodeTypeImpl.isSameOrSubType(testNodeType, getPrimaryNodeType()))
        return true;
    else {
        for(int i=0; i<getMixinNodeTypes().length; i++) {
           if(NodeTypeImpl.isSameOrSubType(testNodeType, getMixinNodeTypes()[i]))
             return true;
        }
    }
    return false;
    // TODO MIXIN type
//    return getPrimaryNodeType().getName().equals(nodeTypeName);
  }

  /**
   * Adds the specified mixin node type to this node. If a conflict with
   * another assigned mixin or the main node type results, then an exception
   * is thrown on save. Adding a mixin node type to a node immediately adds
   * the name of that type to the list held in that node�s
   * <code>jcr:mixinTypes</code> property.
   *
   * @param mixinName
   */
  public void addMixin(String mixinName) {

    NodeType type = null;
    try {
      type = NodeTypeManagerImpl.getInstance().getNodeType(mixinName);
      log.debug("Node.addMixin "+type);
    } catch (NoSuchNodeTypeException e) {
//      e.printStackTrace();
      log.error("Node.addMixin failed: "+e);
    }

    if(type == null || !type.isMixin()) {
      ticket.getNodesManager().addValidationError(getPath(), new ConstraintViolationException("Node Type " + mixinName +
          " not found or not mixin type."));
      return;
    }

     Value[] values; 
     Property mixinProperty = getPermanentProperty("jcr:mixinType");
     try {
        if(mixinProperty == null) {
          values = new Value[1];
          values[0] = new StringValue(mixinName);
        } else {
          // if(validate mixin for conflict  
          if(hasSameOrSubtypeMixin((NodeTypeImpl)type)) {
            log.warn("The node has already exist mixin type "+ mixinName + " or its subtype. Adding of mixin ignored.");
            return;
          }
          values = new Value[mixinProperty.getValues().length+1];
          for(int i=0; i<values.length - 1; i++)
             values[i] = mixinProperty.getValues()[i];
          values[values.length - 1] = new StringValue(mixinName);
       }
       addPermanentProperty(new PropertyImpl(getPath()+"/"+"jcr:mixinType", values, PropertyType.STRING));
       log.debug("Node.addMixin  "+mixinName+ " added");
       addAutoCreatedItems(type);
    } catch (Exception e) {
       ticket.getNodesManager().addValidationError(getPath(), new ConstraintViolationException("Unexpected exception while adding mixin type " + mixinName +
          " Reason: "+e));
    }
//    throw new RuntimeException("Mixin node type is not supported yet!");
  }
  
  private boolean hasSameOrSubtypeMixin(NodeTypeImpl type) throws RepositoryException, ValueFormatException {

    Value[] mixinValues = getPermanentProperty("jcr:mixinType").getValues();
    for(int i=0; i<mixinValues.length; i++) {
        NodeType testNodeType;
        try {
           testNodeType = NodeTypeManagerImpl.getInstance().getNodeType(mixinValues[i].getString());
        } catch (NoSuchNodeTypeException e) {
          throw new RepositoryException("No such node: "+mixinValues[i].getString());
        }

        if(NodeTypeImpl.isSameOrSubType(type, testNodeType))
          return true;
    }
    return false;

  }

  /**
   * 6.10.14
   * Returns the definition of this Node. This method is
   * actually a shortcut to searching through this node's
   * parent's node type (and its supertypes) for the child node
   * definition applicable to this node.
   */
  public NodeDef getDefinition() {
    NodeTypeImpl parentNodeType;
    try {
      Node parent = getParent();
      if (parent != null)
        parentNodeType = (NodeTypeImpl) parent.getPrimaryNodeType();
      else
        throw new RuntimeException("Parent is NULL!");
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
    NodeDef def = parentNodeType.getChildNodeDef(getName());

    if (def == null)
      def = new NodeDefImpl();
    return def;
  }

  /**
   * 6.3.4
   * Analogous to save except that while save acts by
   * persisting changes from the transient layer to the
   * workspace, checkin acts by persisting changes from the
   * workspace layer to the repository layer.
   * The scope of the checkin is the subtree rooted at this
   * node. If this node or its subtree has changed without
   * those changes yet being saved to the workspace, then the
   * checkin automatically does the save, persisting the
   * changes first to the workspace and then on to the
   * repository.
   * In level 2: checkin causes a new version of this node to
   * be created in the repository layer.
   */
  public Version checkin() throws UnsupportedRepositoryOperationException, RepositoryException {
//     log.debug("Checkin called for "+this);
//     ((WorkspaceImpl)getWorkspace()).updateNode(this);
//     ((WorkspaceImpl)getWorkspace()).doUpdateNode(this);

    throw new UnsupportedRepositoryOperationException("Node.checkin() is not supported yet!");

//       workspace.doUpdateNode(this);
  }

  public void checkout() throws RepositoryException, UnsupportedRepositoryOperationException {
    throw new UnsupportedRepositoryOperationException("Node.checkout() is not supported by Level 1 of JCR.");
  }

  /**
   * Updates this node to reflect the state (i.e., have the same properties
   * and child nodes) of this node's corresponding node in srcWorkspace (that
   * is, the node in srcWorkspace with the same UUID as this node). If
   * shallow is set to false, then only this node is updated. If shallow is
   * set to true then every node with a UUID in the subtree rooted at this
   * node is updated. If the current ticket does not have sufficient rights
   * to perform the update or the specified workspace does not exist, a
   * <code>NoSuchWorkspaceException</code> is thrown. If another error occurs
   * then a RepositoryException is thrown. If the update succeeds the changes
   * made to this node are persisted immediately, there is no need to call
   * save.
   *
   * @param srcWorkspaceName the name of the source workspace.
   * @param shallow          a boolean
   * @throws RepositoryException If another error occurs.
   */
  public void update(String srcWorkspaceName, boolean shallow) throws NoSuchWorkspaceException, RepositoryException {
  }

  /**
   * Performs the same function as update (above) with one restriction:
   * merge only succeeds if the base version of the corresponding node in
   * <code>srcWorkspace</code> is a successor (or a successor of a successor, etc., to
   * any degree of separation) of the base version of this node. Otherwise,
   * the operation throws a <code>MergeException</code>. In repositories that
   * do not support versioning, <code>merge</code> throws an
   * <code>UnsupportedRepositoryOperationException</code>. If the current
   * ticket does not have sufficient rights to perform the <code>merge</code> or the
   * specified workspace does not exist, a <code>NoSuchWorkspaceException</code> is thrown.
   * If the <code>merge</code> succeeds, the changes made to this node are
   * persisted immediately, there is no need to call <code>save</code>.
   *
   * @param srcWorkspace the name of the source workspace.
   * @param shallow      a boolean
   * @throws UnsupportedRepositoryOperationException
   *                                  if versioning is not supported.
   * @throws MergeException           succeeds if the base version of the corresponding
   *                                  node in srcWorkspace is not a successor of the base version of this node.
   * @throws NoSuchWorkspaceException If the current
   *                                  ticket does not have sufficient rights to perform the <code>merge</code> or the
   *                                  specified workspace does not exist.
   * @throws RepositoryException      If another error occurs.
   */
  public void merge(String srcWorkspace, boolean shallow) throws UnsupportedRepositoryOperationException, NoSuchWorkspaceException, MergeException, RepositoryException {
    throw new UnsupportedRepositoryOperationException("Node.merge() is not supported by Level 1 of JCR.");
  }

  /**
   * Returns <code>true</code> if this node is currently checked-out and <code>false</code> otherwise.
   *
   * @return a boolean
   * @throws UnsupportedRepositoryOperationException
   *                             if versioning is not supported.
   * @throws RepositoryException If another error occurs.
   */
  public boolean isCheckedOut() throws UnsupportedRepositoryOperationException, RepositoryException {
    throw new UnsupportedRepositoryOperationException("Node.isCheckedOut() is not supported by Level 1 of JCR.");
  }

  /**
   * Restores this node to the state recorded in the specified version.
   *
   * @param versionName
   * @throws UnsupportedRepositoryOperationException
   *                             if versioning is not supported.
   * @throws RepositoryException If another error occurs.
   */
  public void restore(String versionName) throws UnsupportedRepositoryOperationException, RepositoryException {
    throw new UnsupportedRepositoryOperationException("Node.restore() is not supported by Level 1 of JCR.");
  }

  /**
   * Restores this node to the state recorded in the specified version.
   *
   * @param version a <code>Version</code> object
   * @throws UnsupportedRepositoryOperationException
   *                             if versioning is not supported.
   * @throws RepositoryException If another error occurs.
   */
  public void restore(Version version) throws UnsupportedRepositoryOperationException, RepositoryException {
    throw new UnsupportedRepositoryOperationException("Node.restore() is not supported by Level 1 of JCR.");
  }

  /**
   * Restores this node to the state recorded in the version corresponding to the specified date.
   *
   * @param date a <codeCalendar</code> object
   * @throws UnsupportedRepositoryOperationException
   *                             if versioning is not supported.
   * @throws RepositoryException If another error occurs.
   */
  public void restore(Calendar date) throws UnsupportedRepositoryOperationException, RepositoryException {
    throw new UnsupportedRepositoryOperationException("Node.restore() is not supported by Level 1 of JCR.");
  }

  /**
   * Restores this node to the state recorded in the version specified by
   * <code>versionLabel</code>.
   *
   * @param versionLabel a String
   * @throws UnsupportedRepositoryOperationException
   *                             if versioning is not supported.
   * @throws RepositoryException If another error occurs.
   */
  public void restoreByLabel(String versionLabel) throws UnsupportedRepositoryOperationException, RepositoryException {
    throw new UnsupportedRepositoryOperationException("Node.restore() is not supported by Level 1 of JCR.");
  }

  /**
   * Returns the <code>VersionHistory</code> object of this node. This object
   * is simply a wrapper for the <code>nt:versionHistory</code> node holding
   * this node's versions.
   *
   * @return a <code>VersionHistory</code> object
   * @throws UnsupportedRepositoryOperationException
   *                             if versioning is not supported.
   * @throws RepositoryException If another error occurs.
   */
  public VersionHistory getVersionHistory() throws UnsupportedRepositoryOperationException, RepositoryException {
    throw new UnsupportedRepositoryOperationException("Node.getVersionHistory() is not supported by Level 1 of JCR.");
  }

  /**
   * Returns the current base version of this versionable node.
   *
   * @return a <code>Version</code> object.
   * @throws UnsupportedRepositoryOperationException
   *                             if versioning is not supported.
   * @throws RepositoryException If another error occurs.
   */
  public Version getBaseVersion() throws UnsupportedRepositoryOperationException, RepositoryException {
    throw new UnsupportedRepositoryOperationException("Node.getBaseVersion() is not supported by Level 1 of JCR.");
  }

 //////////////////// Item implementation ////////////////////
  public void accept(ItemVisitor visitor) throws RepositoryException {
    visitor.visit(this);
  }

  public boolean isNode() {
    return true;
  }

  protected boolean isItemIdentical(Item otherItem) {
    try {
      NodeIterator nodes = getNodes();
      Node otherNode = (Node) otherItem;
      while (nodes.hasNext()) {
        if (otherNode.hasNode(nodes.nextNode().getName()))
          return false;
        }
      PropertyIterator props = this.getProperties();
        while (props.hasNext()) {
          Property prop1 = props.nextProperty();
          Property prop2 = null;
          try {
            prop2 = otherNode.getProperty(prop1.getName());
          } catch (PathNotFoundException e) {
            return false;
          }
          if (!prop1.isIdentical(prop2))
            return false;
        }
        return true;
    } catch (Exception e) {
        log.error("Node.isIdentical() failed. Reason: "+e);
        return false;
//      throw new RuntimeException(e.getMessage(), e);
    }

  }

  public StringIterator getPaths() {
    ArrayList list = new ArrayList();
    Property uuid = getPermanentProperty("jcr:uuid");
    if( uuid != null)
       list.addAll(ticket.getNodesManager().getPaths(uuid.getString()));
    else
        list.add(getPath());
    EntityCollection paths = new EntityCollection(list);
    return paths;
  }


  // NodeData implementation //////////////////////////
  public void addPermanentProperty(Property property) {
    removePermanentProperty(property.getName());
    properties.add(property);
    log.debug("Add permanent property "+property+" hash="+property.hashCode() );
  }

  public void removePermanentProperty(String name) {
    for (int i = 0; i < properties.size(); i++) {
      PropertyImpl prop = (PropertyImpl) properties.get(i);
      if (prop.getName().equals(name)) {
        properties.remove(i);
        log.debug("Remove permanent property "+prop+" hash="+prop.hashCode() );
      }
    }
  }

  public Property getPermanentProperty(String name) {
    for (int i = 0; i < properties.size(); i++) {
      PropertyImpl prop = (PropertyImpl) properties.get(i);
      if (prop.getName().equals(name))
        return prop;
    }
    return null;
  }

  public List getPermanentProperties() {
    return properties;
  }

  public void refresh(Node withNode) throws PathNotFoundException, RepositoryException {
    properties = new ArrayList();
    List props = ((NodeImpl)withNode).getPermanentProperties();
    for(int i=0; i<props.size(); i++)
       this.properties.add( new PropertyImpl((PropertyImpl)props.get(i)) );
  }


  ///////////////////////////////////////////////

  public String toString() {
    int psize = (properties == null) ? 0 : properties.size();
    NodeType ntype = getPrimaryNodeType();
    return "Node:( path=" + getPath() +
        " type=" + ntype +
        " properties=" + psize
        + ")";
  }

  PropertyImpl updateProperty(String name, int type, Value[] values) throws ConstraintViolationException {

    PropertyImpl property = null;
    try {
      ItemLocation loc = new ItemLocation(getPath(), name);
      property = new PropertyImpl(loc.getPath(), values, type);
      log.debug("NodeImpl.updateProperty-- "+this.getPermanentProperty(name)+" with "+property+" type = "+type);
      property.setTicket(ticket);

//      removePermanentProperty(property.getName());
      addPermanentProperty(property);
      ticket.getNodesManager().update(this);

//      ticket.getNodesManager().updateProperty(this, property);
    } catch (Exception e) {
      e.printStackTrace();
      throw new ConstraintViolationException("Node.updateProperty() failed for '" + name, e);
    }
    log.debug("createProperty (" + property + ") " + getPath());
    return property;
  }

  protected NodeImpl createNode(NodeImpl parent, String name, NodeType type) throws ConstraintViolationException {
    NodeImpl node = null;
    try {
      String nodePath = new ItemLocation(parent.getPath(), name).getPath();
      node = new NodeImpl(nodePath, type.getName());
    } catch (PathNotFoundException e) {
      throw new ConstraintViolationException("Node.createNode() failed Reason: " + e);
    }
    node.setTicket(ticket);
    log.debug("Create node " + node.getPath() + " type =" + type + " manager " + ticket);
    ticket.getNodesManager().add(node);
    node.addAutoCreatedItems(type);
    return node;
  }

  private EntityCollection retrieveChildNodes(ItemFilter filter) {
    ArrayList list = new ArrayList();
    Iterator children = ticket.getNodesManager().getChildren(getPath()).iterator();
    while(children.hasNext()) {
      String path = (String) children.next();
      NodeImpl item = (NodeImpl) ticket.getNodesManager().getNodeByPath(path);
      item.setTicket(ticket);
      log.debug("Retrieve item : " + item.getPath());
      if (filter.accept(item))
        list.add(item);
    }
    return new EntityCollection(list);
  }

  private EntityCollection retrieveProperties(ItemFilter filter) {

    log.debug("retrieve properties of node : " + this.getPath());
    ArrayList list = new ArrayList();
//    List properties = ticket.getNodesManager().getProperties(getPath());

    for (int i = 0; i < properties.size(); i++) {
      Property prop = (Property) properties.get(i);
      if (prop != null && filter.accept(prop)) {
        log.debug("retrieve filtered property " + prop.getName());
        list.add(prop);
      }
    }
    return new EntityCollection(list);
  }

  void addAutoCreatedItems(NodeType type) throws ConstraintViolationException {
    NodeDef[] nodeDefs = type.getChildNodeDefs();
    PropertyDef[] propDefs = type.getPropertyDefs();

    for (int i = 0; i < propDefs.length; i++) {
      if (propDefs[i].isAutoCreate()) {
        // Real Property value OR propDefs[i].getDefaultValue() is not found
        Value propVal = propDefs[i].getDefaultValue();
        String name = propDefs[i].getName();

        // SYSTEM PROP jcr:primaryType
        if (name.equals("jcr:primaryType"))
          propVal = new StringValue(type.getName());


        Value[] array = { propVal };
        log.debug("AutoCreate Property-> for " + this.getName() + " property name " + propDefs[i].getName() + " " +
            propDefs[i].getRequiredType()+ " "+ getPermanentProperty ("jcr:primaryType"));
        PropertyImpl prop = updateProperty(name, propDefs[i].getRequiredType(), array);
      }
    }

    if (nodeDefs == null)
      return;
    for (int i = 0; i < nodeDefs.length; i++) {
      log.debug("AutoCreate nodedef ->" + nodeDefs[i].getName());
      if (nodeDefs[i].isAutoCreate()) {
        createNode(this, nodeDefs[i].getName(), nodeDefs[i].getDefaultPrimaryType());
        log.debug("AutoCreate Node->" + this + " " + nodeDefs[i].getName());
      }
    }
  }

  private ItemImpl findPrimaryItem(NodeImpl parent)
      throws ItemNotFoundException, AccessDeniedException, RepositoryException {
    PropertyIterator properties = parent.getProperties();
    // HAS Primary property
    while (properties.hasNext()) {
      PropertyImpl prop = (PropertyImpl) properties.nextProperty();
      prop.setTicket(ticket);
      if (prop.getDefinition() != null && prop.getDefinition().isPrimaryItem()) {
        return (PropertyImpl) prop;
      }
    }

    //... OR HAS Primary Node
    NodeIterator nodes = parent.getNodes();

    NodeImpl primaryItem = null;
    while (nodes.hasNext()) {
      NodeImpl node = (NodeImpl) nodes.nextNode();
      node.setTicket(ticket);
      if (node.getDefinition() != null && node.getDefinition().isPrimaryItem()) {
        // Traverse if primary
        return findPrimaryItem((NodeImpl) node);
      }
    }
    return parent;
  }

}

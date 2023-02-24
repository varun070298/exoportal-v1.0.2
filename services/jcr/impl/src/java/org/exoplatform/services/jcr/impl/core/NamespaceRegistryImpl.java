/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.jcr.NamespaceException;
import javax.jcr.NamespaceRegistry;
import javax.jcr.RepositoryException;

import org.apache.commons.lang.ArrayUtils;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: NamespaceRegistryImpl.java,v 1.7 2004/08/15 12:01:51 benjmestrallet Exp $
 */

public class NamespaceRegistryImpl implements NamespaceRegistry {

  private HashMap namespaces;

  private static final String[] protectedNamespaces = {"jcr", "nt", "mix","pt", "sv", "exo"};

  public NamespaceRegistryImpl() {
    namespaces = new HashMap();
    namespaces.put("jcr", "http://www.jcp.org/jcr/1.0");
    namespaces.put("nt", "http://www.jcp.org/jcr/nt/1.0");
    namespaces.put("mix", "http://www.jcp.org/jcr/mix/1.0");
    namespaces.put("pt", "http://www.jcp.org/jcr/pt/1.0");
    namespaces.put("sv", "http://www.jcp.org/jcr/sv/1.0");
    namespaces.put("exo", "http://www.exoplatform.com/jcr/exo/1.0");
  }

  /**
   * 6.7
   * Get the URI to which the given prefix is mapped.
   */
  public String getURI(String prefix) {
    return (String) namespaces.get(prefix);
  }

  /**
   * 6.7
   * Set a mapping from prefix to URI. To remove an existing
   * mapping, set its prefix to null.
   */
  public void setMapping(String prefix, String uri) throws NamespaceException, RepositoryException {
    if(ArrayUtils.contains(protectedNamespaces, prefix)){
      if(uri == null)
        throw new NamespaceException("Can not remove built-in namespace");
      else
        throw new NamespaceException("Can not change built-in namespace");
    }
    if(uri == null)
      namespaces.remove(prefix);
    else {
      Collection values = namespaces.values();
      if(values.contains(uri)){
        String key2Remove = null;
        Set keys = namespaces.keySet();
        for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
          String key = (String) iterator.next();
          String value = (String) namespaces.get(key);
          if(value.equals(uri)){
            key2Remove = key;
            break;
          }
        }
        namespaces.remove(key2Remove);
      }
      namespaces.put(prefix, uri);
    }
  }

  /**
   * 6.7
   * Returns an array holding all currently registered prefixes.
   */
  public String[] getPrefixes() {
    return (String[]) namespaces.keySet().toArray(new String[namespaces.keySet().size()]);
  }

  public Map getURIMap() {
    return namespaces;
  }

  public String[] getURIs() {   
    return (String[]) namespaces.values().toArray(new String[namespaces.size()]);
  }


  /**
   * Returns the prefix to which the given uri is mapped
   *
   * @param uri a string
   * @return a string
   */
  public String getPrefix(String uri) throws NamespaceException, RepositoryException {
    String[] prefixes = getPrefixes();
    for (int i = 0; i < prefixes.length; i++) {
      if (getURI(prefixes[i]).equals(uri))
        return (String) namespaces.get(getPrefixes()[i]);
    }
    throw new NamespaceException("There is no uri <" + uri + "> in the repository!");
  }

}

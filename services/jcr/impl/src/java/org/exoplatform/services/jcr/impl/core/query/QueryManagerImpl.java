/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.core.query;

import javax.jcr.query.QueryManager;
import javax.jcr.query.Query;
import javax.jcr.query.InvalidQueryException;
import java.util.HashMap;


import javax.jcr.RepositoryException;
import org.exoplatform.services.jcr.impl.core.NodeImpl;
import org.exoplatform.services.jcr.impl.core.WorkspaceImpl;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: QueryManagerImpl.java,v 1.3 2004/07/08 23:36:48 benjmestrallet Exp $
 */

public class QueryManagerImpl implements QueryManager {

  private static QueryManagerImpl instance;
  private HashMap languages;
  private WorkspaceImpl workspace;

  private QueryManagerImpl() {
    languages = new HashMap();
    languages.put("JCRQL-SSES", "JCR query language");
//        new QueryLanguageDef(new org.exoplatform.services.jcr.impl.core.query.jcrql.token.Select()));
  }

  public static QueryManagerImpl getInstance() {
    if (instance == null)
      instance = new QueryManagerImpl();
    return instance;
  }

  public void init(WorkspaceImpl workspace) {
    this.workspace = workspace;
  }

  /**
   * 6.9.1
   * Creates a transient Query.
   */
  public Query createQuery(String statement, int language) throws InvalidQueryException, RepositoryException {
    if (workspace == null)
      throw new RepositoryException("Query Manager is not initialized!");

//        return new QueryImpl(workspace, null);

    return null;
  }

  /**
   * 6.9.1
   * Retrieves an existing persistent query. If absPath is not the
   * path of a persisted query (i.e., if it does not point to a node of
   * node type nt:query) then an InvalidPathException is
   * thrown. If the query exists but is invalid, an
   * InvalidQueryException is thrown.
   */
  public Query getQuery(String absPath) throws InvalidQueryException, RepositoryException {
/*
        if(workspace == null)
            throw new RepositoryException("Query Manager is not initialized!");

        NodeImpl node = workspace.getNodeByPath(absPath);
        if(node == null)
            throw new RepositoryException("Node not found at <"+absPath+"> !");

//        return new QueryImpl(node);

        return null;
*/
    throw new RepositoryException("Not implemented yet !");

  }

  /**
   * 6.9.1
   * Returns a string identifier for each supported query language.
   * The string should consist of two parts. The first identifying the
   * main query language, the second identifying the full-text
   * search language embedded in the SEARCH clause.
   */
  public int[] getSupportedQueryLanguages() {
//        return (String[])languages.keySet().toArray();

    int[] langs = new int[1];
    langs[1] = 0;
    return langs;
  }
/*
    public Token getFirstExpectedToken(String language) throws UnsupportedQueryLanguageException {
        QueryLanguageDef lang = (QueryLanguageDef)languages.get(language);
        if(lang == null)
            throw new UnsupportedQueryLanguageException("Language <"+language+"> is not supported!");
        return lang.getFirstExpectedToken();
    }

    public class QueryLanguageDef {
        private Token firstExpectedToken;
        public QueryLanguageDef(Token firstExpectedToken) {
            this.firstExpectedToken = firstExpectedToken;
        }

        public Token getFirstExpectedToken() {
            return firstExpectedToken;
        }
    }
*/
}

/*
 * $Header: /cvsroot/exo/exoplatform/exo-services/jcr-service/api/src/java/exo/services/jcr/util/PathUtil.java,v 1.1 2004/09/16 15:27:38 geaz Exp $
 * $Revision: 1.1 $
 * $Date: 2004/09/16 15:27:38 $
 *
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Tomcat", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 * [Additional notices, if required by prior licensing conditions]
 *
 */

package org.exoplatform.services.jcr.util;

import java.util.Iterator;
import java.util.LinkedList;
import javax.jcr.PathNotFoundException;

/**
 * The <code>PathUtil</code> utility class provides
 * misc. methods to resolve and nornalize JCR-style element paths.
 *
 * @author Stefan Guggisberg
 * @version $Revision: 1.1 $, $Date: 2004/09/16 15:27:38 $
 */
public class PathUtil {

  /**
   * Builds a canonical path of the specified path.
   * <p/>
   * A canonical path is both absolute and unique.
   * The special path elements "." and ".." are normalized.
   *
   * @param absolutePath absolute path which should be resolved as a canonical path
   * @return a canonical path
   * @throws MalformedPathException if the specified path is malformed
   *                                (e.g. "/../../foo") or if it is not an absolute path.
   */
  public static String makeCanonicalPath(String absolutePath)
      throws PathNotFoundException {
    if (!absolutePath.startsWith("/")) {
      throw new PathNotFoundException("'" + absolutePath + "' is not an absolute path");
    }
    // check for "." and ".." path elements
    // (just an optimization, not 100% accurate;
    //  e.g. "/foo/.foo1" would incorrectly pass it)
    if (absolutePath.indexOf("./") >= 0 || absolutePath.indexOf("/.") >= 0) {
      LinkedList queue = new LinkedList();
      int start = 0;
      while (start >= 0) {
        int end = absolutePath.indexOf('/', start + 1);
        String element = absolutePath.substring(start + 1, end == -1 ? absolutePath.length() : end);
        if (element.equals(".")) {
          // ignore
        } else if (element.equals("..")) {
          if (queue.isEmpty()) {
            throw new PathNotFoundException("'" + absolutePath + "' is not a valid path");
          }
          queue.removeLast();
        } else {
          queue.add(element);
        }
        start = end;
      }
      StringBuffer buf = new StringBuffer();
      Iterator iter = queue.iterator();
      while (iter.hasNext()) {
        buf.append('/');
        buf.append((String) iter.next());
      }
      return buf.toString();
    } else {
      return absolutePath;
    }
  }

  /**
   * Builds a canonical path of the specified paths.
   * <p/>
   * A canonical path is both absolute and unique.
   * The special path elements "." and ".." are normalized.
   * <p/>
   * If <code>somePath</code> specifies a relative path (i.e. one not
   * starting with  "/") then it is interpreted as being relative to
   * <code>parentPath</code>; otherwise <code>parentPath</code> is ignored.
   *
   * @param parentPath parent path used to resolve a relative path with.
   * @param somePath   either relative or absolute path.
   * @return a canonical path
   * @throws MalformedPathException if at least one of the specified paths
   *                                is malformed or if the resulting absolute path is malformed (e.g. "/../../foo")
   */
  public static String makeCanonicalPath(String parentPath, String somePath)
      throws PathNotFoundException {
    // @todo add additonal validations
    if (somePath.startsWith("/")) {
      return makeCanonicalPath(somePath);
    } else {
      // make absolute path
      String absPath = (parentPath.equals("/") ? "" : parentPath) + "/" + somePath;
      return makeCanonicalPath(absPath);
    }
  }

  /**
   * Returns the path to the ancestor of degree <code>degree</code> of the
   * <code>Element</code> pointed to by <code>descendantPath</code>.
   *
   * @param descendantPath absolute path to the descendant of the requested ancestor.
   * @param degree         the degree of the requested ancestor.
   * @return the path of the ancestor of the specified degree of the
   *         <code>Element</code> pointed to by <code>descendantPath</code>.
   * @throws MalformedPathException if there is no ancestor of the specified
   *                                degree or if the path is malformed (e.g. "/../../foo").
   */
  public static String getAncestorPath(String descendantPath, int degree)
      throws PathNotFoundException {
    descendantPath = makeCanonicalPath(descendantPath);
    int pos = descendantPath.length();
    int cnt = degree;
    while (cnt-- > 0) {
      pos = descendantPath.lastIndexOf('/', pos - 1);
      if (pos < 0) {
        throw new PathNotFoundException(degree + "nth ancestor of " + descendantPath);
      }
    }

    String ancestorPath = descendantPath.substring(0, pos);
    return ancestorPath.equals("") ? "/" : ancestorPath;
  }

  /**
   * Returns the name element (i.e. the last element) of the supplied path.
   *
   * @param path the path whise name element should be returned.
   * @return the name element of the path
   * @throws MalformedPathException if the path is malformed (e.g. "/../../foo").
   */
  public static String getName(String path) throws PathNotFoundException {
    path = makeCanonicalPath(path);

    int pos = path.lastIndexOf("/");
    if (pos < 0) {
      return path;
    } else if (pos == path.length()) {
      return "";
    }
    return path.substring(pos + 1);
  }

  public static String rewriteSuffix(String path, String from, String to) throws PathNotFoundException {
    path = makeCanonicalPath(path);

    int pos = from.length();
    if (pos == path.length()) {
      return to;
    } else {
      return to + path.substring(pos);
    }

  }


  public static boolean isDescendant(String testPath, String ancestorPath, boolean direct) /*throws PathNotFoundException*/ {

    try {

      int depth = getDepth(makeCanonicalPath(testPath));
      if(depth == 0)
         return false;

      if(direct)
         return getAncestorPath( makeCanonicalPath(testPath),1).equals(makeCanonicalPath(ancestorPath));
      else {
         for(int i=1; i<=depth; i++)
            if(getAncestorPath( makeCanonicalPath(testPath),i).equals(makeCanonicalPath(ancestorPath)))
               return true;
      }

    } catch (PathNotFoundException e) {throw new RuntimeException("isDescendanr failed "+e); }

    return false;

/*
    if (testPath.length() <= ancestorPath.length())
      return false;
    try {
      if (getDepth(makeCanonicalPath(testPath)) <= getDepth(makeCanonicalPath(ancestorPath)))
        return false;
    } catch (Exception e) {
      return false;
    }

    if(!testPath.startsWith(ancestorPath))
      return false;

    int pos = testPath.indexOf(ancestorPath);
    if (pos == -1)
      return false;

    String desc = testPath.substring(pos + ancestorPath.length());

//    System.out.println("DESC = "+desc+" "+ancestorPath+" "+desc.substring(0,desc.indexOf("/")));


    if (desc.equals("/"))
      return true;

    if (desc.startsWith("/"))
      desc = desc.substring(1);

    if (direct && desc.indexOf("/") != -1)
      return false;


    return true;
*/
  }

  public static int getDepth(String absolutePath) throws PathNotFoundException {
    int cnt = 0;
    String _path = makeCanonicalPath(absolutePath);
    // Except trailing / - for ex. '/test/'
    for (int i = 0; i < _path.length() - 1; i++)
      if (_path.charAt(i) == '/')
        cnt++;
    return cnt;
  }

}

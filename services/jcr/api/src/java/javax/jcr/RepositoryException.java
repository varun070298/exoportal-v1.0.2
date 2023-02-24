/*
 * $Id: RepositoryException.java,v 1.2 2004/07/24 00:16:21 benjmestrallet Exp $
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

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Main exception thrown by classes in this package. May either contain
 * an error message or another exception wrapped inside this exception.
 *
 * @author Peeter Piegaze
 */
public class RepositoryException extends Exception {

  /**
   * Root failure cause
   */
  protected Exception rootException;

  /**
   * Constructs a new instance of this class.
   */
  public RepositoryException() {
    super();
  }

  /**
   * Constructs a new instance of this class given a message describing the
   * failure cause.
   *
   * @param s description
   */
  public RepositoryException(String s) {
    super(s);
  }

  /**
   * Constructs a new instance of this class given a message describing the
   * failure and a root exception.
   *
   * @param s description
   * @param e root failure cause
   */
  public RepositoryException(String s, Exception e) {
    super(s);

/* don't create a proxy to a proxy */
    if (e instanceof RepositoryException) {
      RepositoryException e2 = (RepositoryException) e;
      rootException = e2.rootException;
    } else {
      rootException = e;
    }
  }

  /**
   * Constructs a new instance of this class given a root exception.
   *
   * @param e root failure cause
   */
  public RepositoryException(Exception e) {
    this(null, e);
  }

  /**
   * Returns the error message string of this exception.
   *
   * @return error message string of this exception.
   */
  public String getMessage() {
    String s = super.getMessage();
    if (rootException == null) {
      return s;
    } else {
      String s2 = rootException.getMessage();
      return s == null ? s2 : s + ": " + s2;
    }
  }

  /**
   * Returns the root exception of this exception.
   *
   * @return the root exception of this exception
   */
  public Exception getRootException() {
    return rootException;
  }

  /**
   * Prints this <code>RepositoryException</code> and its backtrace to the
   * standard error stream.
   */
  public void printStackTrace() {
    synchronized (System.err) {
      super.printStackTrace();
      if (rootException != null) {
        rootException.printStackTrace();
      }
    }
  }

  /**
   * Prints this <code>RepositoryException</code> and its backtrace to the
   * specified print stream.
   *
   * @param s <code>PrintStream</code> to use for output
   */
  public void printStackTrace(PrintStream s) {
    synchronized (s) {
      super.printStackTrace(s);
      if (rootException != null) {
        rootException.printStackTrace(s);
      }
    }
  }

  /**
   * Prints this <code>RepositoryException</code> and its backtrace to
   * the specified print writer.
   *
   * @param s <code>PrintWriter</code> to use for output
   * @since JDK1.1
   */
  public void printStackTrace(PrintWriter s) {
    synchronized (s) {
      super.printStackTrace(s);
      if (rootException != null) {
        rootException.printStackTrace(s);
      }
    }
  }
}

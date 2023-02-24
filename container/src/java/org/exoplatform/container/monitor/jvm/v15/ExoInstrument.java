/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.container.monitor.jvm.v15;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Feb 8, 2005
 * @version $Id$
 * You should pass -javaagent:org.exoplatform.container.monitor.jvm.v15.ExoInstrument
 * to JAVA_OPTS
 */
public class ExoInstrument {
  static private Instrumentation ins_ ;
  
  public static void main(String args[] ) {
    System.out.println("Hello World" );
  }
  
  public static void premain(String options, Instrumentation ins) {
    //ins.addTransformer(new Logger() );
    ins_ = ins ;
    System.out.println("==========================> call premain") ;
  }
  
  public static Instrumentation getInstrumentation() { return ins_ ; }
  
  public void printObjectSize(Object object) {
    System.out.println("size of " + object.getClass().getName() + " = "+ ins_.getObjectSize(object)) ;
  }
  
  /*Sample transformer , this class just print the loaded classes*/
  public static class Logger implements ClassFileTransformer {
    public byte[] transform(java.lang.ClassLoader loader,
                            java.lang.String className,
                            java.lang.Class classBeingRedefined,
                            java.security.ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException   {
      System.out.println(className );
      return null;
    }
  }
}
package org.exoplatform.test;

import junit.framework.TestCase;

public  class BasicTestCase extends TestCase {
  private static int testNumber_ = 1 ;
  protected int counter_ ;

  public BasicTestCase() {
  }
  
  public BasicTestCase(String name) {
    super(name);
  }
  
  protected int getTestNumber() {
    return testNumber_ ;
  }

  protected void setTestNumber(int num) {
    testNumber_ = num ;
  }
  
  protected int getCounter() {
    return counter_  + 1;
  }

  protected void runTest() throws Throwable {
    long t = System.currentTimeMillis() ;
    long firstRun = 0  ;
    int testNum = getTestNumber() ;
    System.out.println(getDescription());
    System.out.println("Test Case: " + getName() + "()" );
    for(counter_= 0; counter_ < testNum; counter_++) {
      super.runTest() ;
      if (counter_ == 0) {
        firstRun = System.currentTimeMillis() - t;
      }
    }
    t = System.currentTimeMillis() - t;
    System.out.println("End Test Case: run " + getName() + "() " + 
                       getTestNumber() + " time in " + t + 
                       "ms, first run: " + firstRun + "ms, " +
                       "average: " + t/testNum+ "ms\n");
  }
  
  protected  static void info(String s) {
    System.out.println("  INFO: " + s);
  }

  protected  static void error(String s) {
    System.out.println("ERROR: " + s);
  }

  protected String getDescription()  {
  	return "Run test " + getClass().getName() ;
  }
}

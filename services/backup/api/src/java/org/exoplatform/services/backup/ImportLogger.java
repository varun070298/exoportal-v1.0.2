/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.backup;
/**
 * May 27, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $ID$
 **/
public class ImportLogger {
	private int numberOfEntry_ ;
	private int numberOfFail_ ;
	private StringBuffer buffer_ ;
	private StringBuffer errorBuffer_ ;
	
	public ImportLogger() {
		numberOfEntry_ = 0;
		numberOfFail_ = 0;
		buffer_ = new StringBuffer(5000) ;
		errorBuffer_ = new StringBuffer(1000) ;
	}
	
	public void log(String record) {
		numberOfEntry_++ ;
		buffer_.append("import record ").append(record).append(" successfully\n");
	}
	
	public void log(String record, Throwable t) {
		numberOfEntry_++ ;
		numberOfFail_++ ;
		errorBuffer_.append("import record ").append(record).append(" fail\n");
	}
	
	public String getTextSummary() {
		StringBuffer b = new StringBuffer() ;
		b.append("Number of entry: ").append(numberOfEntry_).append("\n") ;
		b.append("Number of fail entry: ").append(numberOfFail_).append("\n") ;
		b.append(buffer_);
		b.append(errorBuffer_);
		return b.toString() ;
	}
}
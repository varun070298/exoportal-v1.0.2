/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.test.web;

import com.meterware.httpunit.*;

/**
 * Jun 27, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: Util.java,v 1.1 2004/10/11 23:36:03 tuan08 Exp $
 */
public class Util {
	public static WebLink findLink(WebResponse response, WebTable block, 
                                 String text) throws Exception {
		WebLink link = null ;
  	if(block != null) {
  		for(int i = 0 ; i < block.getRowCount(); i++) {
  			for(int j = 0; j < block.getColumnCount(); j++) {
  				TableCell cell = block.getTableCell(i, j) ;
  				link = cell.getFirstMatchingLink( WebLink.MATCH_TEXT, text);
  				if(link != null) return link ;
  			}
  		}
  		
  	} else {
  		link = response.getFirstMatchingLink(WebLink.MATCH_TEXT, text);
  		if(link != null) return link ;
  	}
  	return null ;
	}
	
	public static WebLink[] findLinks(WebResponse response, WebTable block, 
                                    String text) throws Exception {
		WebLink[] links = null ;
  	if(block != null) {
  		for(int i = 0 ; i < block.getRowCount(); i++) {
  			for(int j = 0; j < block.getColumnCount(); j++) {
  				TableCell cell = block.getTableCell(i, j) ;
  				links = cell.getMatchingLinks(WebLink.MATCH_TEXT , text);
  				if(links.length > 0) return links ;
  			}
  		}
  		
  	} else {
  		return response.getMatchingLinks(WebLink.MATCH_TEXT , text);
  	}
  	return new WebLink[0] ;
		
	}
  //==============================================================================
	public static WebLink findLinkWithText(WebResponse response, WebTable block, 
			                                   String text) throws Exception{
		WebLink link = null ;
  	if(block != null) {
  		for(int i = 0 ; i < block.getRowCount(); i++) {
  			for(int j = 0; j < block.getColumnCount(); j++) {
  				TableCell cell = block.getTableCell(i, j) ;
  				link = cell.getFirstMatchingLink( WebLink.MATCH_CONTAINED_TEXT, text);
  				if(link != null) return link ;
  			}
  		}
  		
  	} else {
  		link = response.getFirstMatchingLink( WebLink.MATCH_CONTAINED_TEXT, text);
  		if(link != null) return link ;
  	}
  	return null ;
	}
	
	public static WebLink[] findLinksWithText(WebResponse response, WebTable block, 
                                            String text) throws Exception{
		WebLink[] links = null ;
  	if(block != null) {
  		for(int i = 0 ; i < block.getRowCount(); i++) {
  			for(int j = 0; j < block.getColumnCount(); j++) {
  				TableCell cell = block.getTableCell(i, j) ;
  				links = cell.getMatchingLinks(WebLink.MATCH_CONTAINED_TEXT , text);
  				if(links.length > 0) return links ;
  			}
  		}
  		
  	} else {
  		return response.getMatchingLinks(WebLink.MATCH_CONTAINED_TEXT , text);
  	}
  	return new WebLink[0] ;
	}
	//==============================================================================
	public static WebLink findLinkWithURL(WebResponse response, WebTable block, 
                                        String url) throws Exception{	
		WebLink link = null ;
		if(block != null) {
			for(int i = 0 ; i < block.getRowCount(); i++) {
				for(int j = 0; j < block.getColumnCount(); j++) {
					TableCell cell = block.getTableCell(i, j) ;
					link = cell.getFirstMatchingLink( WebLink.MATCH_URL_STRING, url);
					if(link != null) return link ;
				}
			}

		} else {
			link = response.getFirstMatchingLink( WebLink.MATCH_URL_STRING, url);
			if(link != null) return link ;
		}
		return null ;
	}
	
	public static WebLink[] findLinksWithURL(WebResponse response, WebTable block, 
                                           String partOfURL) throws Exception{
		WebLink[] links = null ;
		if(block != null) {
			for(int i = 0 ; i < block.getRowCount(); i++) {
				for(int j = 0; j < block.getColumnCount(); j++) {
					TableCell cell = block.getTableCell(i, j) ;
					links = cell.getMatchingLinks(WebLink.MATCH_URL_STRING, partOfURL) ;
					if(links.length > 0) return links ;
				}
			}

		} else {
			return response.getMatchingLinks(WebLink.MATCH_URL_STRING, partOfURL) ;
		}
		return new WebLink[0] ;
	}
	
	public static WebForm findFormWithName(WebResponse response, WebTable block, 
                                         String name) throws Exception{
		WebForm form = null ;
		if(block != null) {
			for(int i = 0 ; i < block.getRowCount(); i++) {
				for(int j = 0; j < block.getColumnCount(); j++) {
					TableCell cell = block.getTableCell(i, j) ;
					form = cell.getFormWithName(name) ;
					if(form != null) return form ;
				}
			}

		} else {
			form = response.getFormWithName(name) ;
			if(form != null) return form ;
		}
		return null ;
	}
}
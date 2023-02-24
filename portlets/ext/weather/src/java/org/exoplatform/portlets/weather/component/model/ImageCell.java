/**
 * Jun 17, 2004, 11:14:24 AM
 * @author: Fran�ois MORON
 * @email: francois.moron@rd.francetelecom.com
 * 
 * */

package org.exoplatform.portlets.weather.component.model;

import java.io.IOException;

import javax.faces.context.ResponseWriter;
import org.exoplatform.faces.core.component.UIGrid;
import org.exoplatform.faces.core.component.model.Cell;



public class ImageCell extends Cell {
  private String src_ ;
  private String alt_ ;
  
  public ImageCell(String src) {
    src_ = src ;
    alt_ = "";
  }

  public ImageCell(String src, String alt) {
   src_ = src ;
   alt_ = alt;
 }
  public ImageCell setSrc(String src) {
  	src_ = src ;
    return this ;
  }

  public ImageCell setAlt(String alt) {
  	alt_ = alt ;
    return this ;
  }

  public void render(ResponseWriter w, UIGrid uiGrid, String cellTag) throws IOException { 
    w.write('<'); w.write(cellTag) ;
    w.write('>') ;
    w.write("<img src=\""); w.write(src_); w.write("\" alt=\""); w.write(alt_); w.write("\"/>");
    w.write("</") ; w.write(cellTag) ; w.write(">\n") ;
  }
  
  public void renderXhtmlMP(ResponseWriter w,UIGrid uiGrid, String cellTag) throws IOException {
  	render(w, uiGrid,  cellTag);
  }
}
/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.portlets.communication.sms.component;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.exoplatform.faces.core.component.UIExoComponentBase;



/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jun 24, 2004 11:26:04 AM
 */
public class UISmsHelpContent extends UIExoComponentBase {
    
    final static private String _frameSource = "https://secure.pswin.com/pswincom/gwsignup.asp?accountType=business";
    final static private String _frameWidth = "100%";
    final static private String _frameHeight = "500px";
    
    public UISmsHelpContent() {
        setId("UISmsHelpContent");
    }
    
    public void encodeBegin( FacesContext context ) throws IOException {
        ResponseWriter w = context.getResponseWriter() ;
        w.write("<iframe style='border: none' src='"+ _frameSource + "'" +
                       " width='"+ _frameWidth + "'" + 
                       " height='"+ _frameHeight +"'" +
                       " scrolling='auto' frameborder='0'>") ;
        w.  write("[Your user agent does not support frames or is currently configured " +
                  "not to display frames. However, you may visit") ;
        w.write("</iframe>");
      }
   

}

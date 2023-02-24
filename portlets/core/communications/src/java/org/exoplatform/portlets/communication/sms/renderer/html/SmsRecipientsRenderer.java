/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.portlets.communication.sms.renderer.html;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;



/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jun 21, 2004 5:14:44 PM
 */
public class SmsRecipientsRenderer extends HtmlBasicRenderer {

    
    
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);
    }
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        super.encodeChildren(context, component);
        
    }
}

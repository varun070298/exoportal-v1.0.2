package org.exoplatform.portlets.nav.renderer.html;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import org.exoplatform.faces.FacesConstants;
import org.exoplatform.portlets.nav.component.UINavigation;


/**
 * 
 * @version $Revision: 1.1.1.1 $ $Date: 2004/08/05 13:11:14 $
 * @author Fahrid Djebbari
 * 
 */

public class NavigationEditRenderer extends Renderer  
{
	private static String FACE_FORM = "navigationEdit";
	private static String FACE_ACTION_SAVE = "faceActionSave";
	private static String FACE_ACTION_CANCEL = "faceActionCancel";
	
	public void encodeBegin(FacesContext context,UIComponent component) throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();
		
		String actionURL = context.getExternalContext().encodeActionURL(null);
		
		writer.write("<form name=\"" + FACE_FORM + "\" method=\"post\" action=\"" + actionURL + "\">");
		writer.write("<input type=\"hidden\" name=\"" + FacesConstants.ACTION + "\" value=\"" + FACE_ACTION_CANCEL + "\">");
		writer.write("<input type=\"hidden\" name=\"" + FacesConstants.COMPONENT + "\" value=\"" + UINavigation.DEFAULT_RENDERER_TYPE + "\">");		
		
		writer.write("<table>");
		
		writer.write("<tr>");
		writer.write("<td><label>Menu</label></td>");
		writer.write("<td>");
		writer.write("<select name=\"renderer\">");
		
		writer.write("<option value=\"HorizontalMenuRenderer\">Horizontal Menu</option>");
		writer.write("<option value=\"BreadcrumbsRenderer\">Breadcrumbs</option>");
		
		writer.write("</select>");
		writer.write("</td>");
		writer.write("</tr>");
/*		
		writer.write("<tr>");
		writer.write("<td><label>Level</label></td>");
		writer.write("<td>");
		writer.write("<input name=\"level\" type=\"text\" value=\"\" size=\"2\" maxlength=\"2\">");
		writer.write("</td>");
		writer.write("</tr>");
		
		writer.write("<tr>");
		writer.write("<td><label>Depth</label></td>");
		writer.write("<td>");
		writer.write("<input name=\"depth\" type=\"text\" value=\"\" size=\"2\" maxlength=\"2\">");
		writer.write("</td>");
		writer.write("</tr>");		
*/		
		writer.write("<tr>");
		writer.write("<td colspan=\"2\">");
		writer.write("<a href=\"javascript: document." + FACE_FORM + ".elements['" + FacesConstants.ACTION + "'].value = '" + FACE_ACTION_SAVE + "';document." + FACE_FORM + ".submit();\">Save</a>");
		writer.write("<a href=\"javascript: document." + FACE_FORM + ".elements['" + FacesConstants.ACTION + "'].value = '" + FACE_ACTION_CANCEL + "';document." + FACE_FORM + ".submit();\">Cancel</a>");
		writer.write("</td>");
		writer.write("</tr>");
		
		writer.write("</table>");
		
		writer.write("</form>");			
	}

	public void encodeChildren(FacesContext context,UIComponent component) throws IOException
	{
	}

	public void encodeEnd(FacesContext context,UIComponent component) throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();
		
		writer.write("</form>");
	}
}

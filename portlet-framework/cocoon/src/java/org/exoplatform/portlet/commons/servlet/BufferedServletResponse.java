/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.portlet.commons.servlet;

import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletOutputStream;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.RenderResponseImp;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: BufferedServletResponse.java 567 2005-01-25 12:51:30Z kravchuk $
 */


public class BufferedServletResponse extends HttpServletResponseWrapper {

    private RenderOutputStream outStream;

    public BufferedServletResponse(RenderResponseImp renderResponse) {
        super(renderResponse);
        outStream = new RenderOutputStream(new ByteArrayOutputStream());
    }

    public void flushBuffer() throws IOException  {
        outStream.flushBuffer();
    }

    public void closeBuffer() throws IOException {
        outStream.closeBuffer();
    }

    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(outStream.baos);
//    return super.getWriter();
    }

    public ServletOutputStream getOutputStream() throws IOException {

        return outStream;
    }

    public byte[] toByteArray() {
        return outStream.baos.toByteArray();
    }

    public void reset() {
        super.reset();
    }

    private class RenderOutputStream extends ServletOutputStream
    {
        public ByteArrayOutputStream baos;

        public RenderOutputStream(ByteArrayOutputStream baos) {
            this.baos = baos;
        }

        public void write(int i) throws IOException {
            baos.write(i);
        }

        public void flush()
        {}

        public void close()
        {}

        public void flushBuffer() throws IOException {
           super.flush();
        }

        public void closeBuffer() throws IOException {
           super.close();
        }


    }

}

/**
 **************************************************************************
 * Copyright 2001-2005 The eXo Platform SARL         All rights reserved. *
 * Please look at license.txt in info directory for more license detail.  *
 **************************************************************************
 */
package org.exoplatform.services.xml.transform.impl;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.transform.TransformerException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.SAXException;

import org.apache.commons.logging.Log;

import org.exoplatform.services.xml.transform.AbstractTransformer;
import org.exoplatform.services.xml.transform.NotSupportedIOTypeException;
import org.exoplatform.services.log.LogService;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.xml.resolving.XMLResolvingService;



/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:alex.kravchuk@gmail.com">Alexander Kravchuk</a>
 * @version $Id: TransformerBase.java 566 2005-01-25 12:50:49Z kravchuk $
 */
public abstract class TransformerBase implements AbstractTransformer {
    private Result result = null;

    protected Log log;
    protected SAXTransformerFactory tSAXFactory;

    public XMLResolvingService resolvingService;

    public TransformerBase() {
        LogService logService = (LogService) PortalContainer.getInstance().
                                getComponentInstanceOfType(LogService.class);
        log = logService.getLog(this.getClass());

        log.debug("Current javax.xml.parsers.SAXParserFactory sys property [ " +
                  System.getProperty("javax.xml.parsers.SAXParserFactory",
                                     "-Not set-") + "]");

        tSAXFactory = (SAXTransformerFactory)
           SAXTransformerFactory.newInstance();
    }

    /**
     * @todo change. Must no use explicit parser class name
     */
    static public XMLReader getXMLReader() throws SAXException {
       return XMLReaderFactory.
               createXMLReader("org.apache.xerces.parsers.SAXParser");
    }

    /**
     * override when need some operation after initialization result
     * in transformer
     */
    protected void afterInitResult(){
        log.debug("Result is set");
    }

    final public void initResult(Result result) throws NotSupportedIOTypeException {
        if (!isResultSupported(result)) {
            throw new NotSupportedIOTypeException(result);
        }
        this.result = result;
        afterInitResult();
    }

    protected Result getResult() {
        return this.result;
    }

    protected boolean isSourceSupported(Source source) {
        return true;
    }

    protected boolean isResultSupported(Result result) {
        return true;
    }

    protected abstract void internalTransform(Source src) throws
            NotSupportedIOTypeException,
            TransformerException, IllegalStateException;


    final public void transform(Source source) throws
            NotSupportedIOTypeException,
            TransformerException, IllegalStateException {

        if (!isSourceSupported(source)) {
            log.error("source of type "+source.getClass().getName()+" not supported");
            throw new NotSupportedIOTypeException(source);
        }

        if (this.result == null) {
            log.error("Result not set");
            throw new IllegalStateException(
                    "Result not specified. See initResult(Result)");
        }

        internalTransform(source);
    }

    /**
     * Tranform InputStream to specified result, according to type of result
     * @param input InputStream
     * @param result Result
     * @throws TransformerException
     */

    public void transformInputStream2Result(InputStream input, Result result) throws
            TransformerException {
        log.debug("Transform InputStream to result of type "+result.getClass().getName());

        //StreamResult - write data from InputStream to OutputStream
        if (result instanceof StreamResult) {
            OutputStream outputStream =
            ((StreamResult) result).getOutputStream();
            try {
                int counter = 0;
                while (input.available() > 0) {
                    byte[] byteArray = new byte[input.available()];
                    int readBytes = input.read(byteArray);
                    counter += readBytes;
                    outputStream.write(byteArray, 0, readBytes);
                }
                log.debug("Write "+counter+" bytes to ouput stream");
            } catch (IOException ex) {
                log.error("Error on read/write ",ex);
                throw new TransformerException(ex);
            }
        }
        //not StreamResult
        else {
            XMLReader xmlReader = null;
            try {
                xmlReader = getXMLReader();
                log.debug("xmlReader class is "+xmlReader.getClass().getName());

                //set default resolver
                if (resolvingService != null) {
                    xmlReader.setEntityResolver(
                            resolvingService.getEntityResolver());
                    log.debug("Set entity resolver");
                }

                //SAXResult use XMLReader to parce InputStream to SAXEvents
                if (result instanceof SAXResult) {
                    SAXResult saxResult = (SAXResult) result;
                    xmlReader.setContentHandler(saxResult.getHandler());
                    log.debug("Parse direct to result");
                }

                //not StreamResult, not SAXResult - create empty transformation
                else {
                    log.debug("Create empty transformation");
                    TransformerHandler transformerHandler =
                            tSAXFactory.newTransformerHandler();
                    transformerHandler.setResult(result);
                    xmlReader.setContentHandler(transformerHandler);
                    log.debug("Parse to result throw empty transformer");
                }
                xmlReader.parse(new InputSource(input));
                log.debug("Parse complete");
            } catch (SAXException ex) {
                throw new TransformerException(ex);
            } catch (IOException ex) {
                throw new TransformerException(ex);
            }
        }
    }

    /**
     * Transform javax.xml.transform.Source to java.io.InputStream
     *  if can't transform throw exception
     * @param source Source
     * @return InputStream
     * @throws NotSupportedIOTypeException
     */
    protected InputStream sourceAsInputStream(Source source) throws
            NotSupportedIOTypeException {
        InputSource inputSource = SAXSource.sourceToInputSource(source);
        if (inputSource == null) {
            throw new NotSupportedIOTypeException(source);
        }
        return inputSource.getByteStream();
    }

    /**
     * @todo REMOVE!!!! For debug only!!!
     * @deprecated see Warning
     */
    protected void writeTofile( byte[] bytes,
                                String postfix){
        String POSTFIX =  new java.text.SimpleDateFormat("yy-MM-DD_HH-mm-ss_").
                          format(new java.util.Date());
        try {

            java.io.FileOutputStream fileLog =
                    new java.io.FileOutputStream(
                            "c:/tmp/transf" + POSTFIX+postfix+".xhtml");
            fileLog.write(bytes);
            fileLog.flush();
            fileLog.close();

        } catch (java.io.FileNotFoundException ex) {
        } catch (IOException ex) {
        }
    }

}





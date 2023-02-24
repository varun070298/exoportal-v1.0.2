/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.sms.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import org.exoplatform.services.communication.sms.common.CommunicationError;


/**
 * @author: Ove Ranheim
 * @email: oranheim@users.sourceforge.net
 */
public class ClientSocket {

    private static final int SOCKET_TIMEOUT_IN_MILLIS = 10 * 1000;
    private static final int SOCKET_BUFFER_SIZE = 8 * 1024;
    private String _ipAddress = "";
    private int _port = 0;
    private Charset _charset;
    private CharsetEncoder _charSetEncoder;
    private CharsetDecoder _charSetDecoder;

    public ClientSocket(String host, int port) {
        _ipAddress = host;
        _port = port;

        _charset = Charset.forName("ISO-8859-1");
        _charSetEncoder = _charset.newEncoder();
        _charSetDecoder = _charset.newDecoder();
    }

    private byte[] charArrayToByteArray(char[] chars, int offset, int len) throws CharacterCodingException {
        CharBuffer charBuf = CharBuffer.wrap(chars, offset, len);
        return _charSetEncoder.encode(charBuf).array();
    }

    private char[] byteArrayToCharArray(byte[] bytes, int offset, int len) throws CharacterCodingException {
        ByteBuffer byteBuf = ByteBuffer.wrap(bytes, offset, len);
        return _charSetDecoder.decode(byteBuf).array();
    }

    public String sendMessage(String buffer) throws CommunicationError {
        StringBuffer inputBuffer = new StringBuffer();
        PrintWriter out = null;
        BufferedReader in = null;
        Socket writeSocket = new Socket();
        try {
            try {
                SocketAddress remote = new InetSocketAddress(_ipAddress, _port);
                writeSocket.connect(remote, SOCKET_TIMEOUT_IN_MILLIS);
                OutputStream os = writeSocket.getOutputStream();
                InputStream is = writeSocket.getInputStream();

                out = new PrintWriter(os, true);
                try {
                    in = new BufferedReader(new InputStreamReader(is));
                    try {
                        // Send payload
                        byte[] b_out = buffer.getBytes();
                        int p = 0;
                        int n = b_out.length;
                        while (n > 0) {
                            int m = SOCKET_BUFFER_SIZE;
                            if (n < SOCKET_BUFFER_SIZE) m = n;
                            char[] a = byteArrayToCharArray(b_out, p, m);
                            out.write(a);
                            p = p + m; // inc next read position
                            n = n - m; // dec remaning buffer
                        }
                        out.println();

                        // Read resposne
                        char[] b_in = new char[SOCKET_BUFFER_SIZE];
                        while ((n = in.read(b_in)) > 0) {
                            inputBuffer.append(b_in, 0, n);
                        }
                    } finally {
                        in.close();
                    }
                } finally {
                    out.close();
                }
            } catch (UnknownHostException e) {
                throw new CommunicationError("Unknown host: " + _ipAddress);
            } finally {
                writeSocket.close();
            }
        } catch (IOException ioe) {
            throw new CommunicationError(ioe);
        }
        return inputBuffer.toString();
    }
}


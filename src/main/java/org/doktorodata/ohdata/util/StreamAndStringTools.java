package org.doktorodata.ohdata.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import org.apache.cxf.common.util.Base64Utility;

public class StreamAndStringTools {

	public static String toString(InputStream is, String charset) throws IOException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(is, charset));
		StringBuffer content = new StringBuffer();
		String line;
		
		while((line = br.readLine()) != null){
			content.append(line);
		}
		
		return content.toString();
	}

	public static void write(String content, OutputStream os, String charset) throws IOException {
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, charset));
		bw.write(content);		
		bw.flush();
	}

	public static InputStream toInputStream(String content, String charset) throws UnsupportedEncodingException {	
		return new ByteArrayInputStream(content.getBytes(charset));
	}

	public static void copy(InputStream is, OutputStream os) throws IOException {
		ReadableByteChannel channelIn = Channels.newChannel(is);
		WritableByteChannel channelOut = Channels.newChannel(os);
		
		ByteBuffer buffer = ByteBuffer.allocate(65536);
		while (channelIn.read(buffer) != -1) {
			buffer.flip( );
			channelOut.write(buffer);
			buffer.clear( );
		}

	}
	
	public static String encodeBase64(byte[] bytes) throws UnsupportedEncodingException {
		return Base64Utility.encode(bytes);
	}


}

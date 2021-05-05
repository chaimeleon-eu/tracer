package es.upv.grycap.tracer.model;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

public class LogEntryMetadataBase {
	
	public final static Charset CHARSET_STRING_FIELDS = Charset.forName("ASCII");

	// 1B
	protected byte action;
	// MongoDB object _id
	protected String userId;
	// MongoDB dataset _id
	protected String datasetId;
	
	/**
	 * String fields are considered 
	 * @return the byte array representation of the object
	 * @throws IOException 
	 */
	public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        dos.write(action);
        dos.write(userId.getBytes(CHARSET_STRING_FIELDS));
        dos.write(datasetId.getBytes(CHARSET_STRING_FIELDS));
        dos.flush();
		dos.close();
		byte[] res = bos.toByteArray();
		bos.close();
		return res;
	}
	
}

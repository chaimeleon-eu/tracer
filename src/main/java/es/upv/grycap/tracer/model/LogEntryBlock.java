package es.upv.grycap.tracer.model;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.UUID;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogEntryBlock {
	
	public static final int BLOCK_VERSION = 1;

	// UUID v4 16B
	protected byte[] id;
	// 8B
	protected long timestamp;
	// 4B
	protected int version;
	// 1B
	protected byte action;
	// UUID v4 16B
	protected byte[] userId;
	// 64B
	/**
	 * SHA512
	 */
	protected byte[] prevHash;
	// variable
	protected byte[] hashClinicalMetadata;
	// variable
	protected byte[] hashClinicalImages;
	
	public LogEntryBlock(UUID id, long timestamp, UserAction action, UUID userId, 
			byte[] prevHash,
			byte[] hashClinicalMetadata, byte[] hashClinicalImages) {
		this.id = this.getBytesFromUUID(id);
		this.timestamp = timestamp;
		this.action = (byte)action.id;
		this.userId = this.getBytesFromUUID(userId);
		this.prevHash = Arrays.copyOf(prevHash, prevHash.length);
		this.hashClinicalMetadata = 
				Arrays.copyOf(hashClinicalMetadata, hashClinicalMetadata.length);
		this.hashClinicalImages = 
				Arrays.copyOf(hashClinicalImages, hashClinicalImages.length);
	}
	
	public byte[] blockHash() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        dos.write(id);
        dos.writeLong(timestamp);
        dos.writeInt(version);
        dos.writeByte(action);
        dos.write(userId);
        dos.write(prevHash);
        dos.write(hashClinicalMetadata);
        dos.write(hashClinicalImages);
        dos.flush();
		byte[] sha512 = DigestUtils.sha512(bos.toByteArray());
		dos.close();
		bos.close();
		 
		return sha512;
	}
	
	public String blockHashHexStr() throws IOException {
		byte[] sha512 = blockHash();
		return Hex.encodeHexString(sha512);
	}
	
	protected byte[] getBytesFromUUID(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());

        return bb.array();
    }

	protected UUID getUUIDFromBytes(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        Long high = byteBuffer.getLong();
        Long low = byteBuffer.getLong();

        return new UUID(high, low);
    }
	
}

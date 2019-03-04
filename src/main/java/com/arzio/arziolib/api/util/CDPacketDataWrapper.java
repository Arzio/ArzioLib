package com.arzio.arziolib.api.util;

public class CDPacketDataWrapper {
	
	private byte packetId;
	private byte[] innerPacketData;
	
	public CDPacketDataWrapper(byte[] customPayloadData) {
		this.packetId = customPayloadData[0];
		this.innerPacketData = new byte[customPayloadData.length - 1];
		
		System.arraycopy(customPayloadData, 1, innerPacketData, 0, innerPacketData.length);
	}
	
	public byte[] buildCustomPayloadData() {
		byte[] customPayloadData = new byte[innerPacketData.length + 1];
		customPayloadData[0] = this.packetId;
		
		System.arraycopy(this.innerPacketData, 0, customPayloadData, 1, innerPacketData.length);
		
		return customPayloadData;
	}
	
	public int getInnerPacketId() {
		return this.packetId;
	}
	
	public byte[] getInnerPacketData() {
		return this.innerPacketData;
	}
	
	public void setInnerPacketData(byte[] data) {
		this.innerPacketData = data;
	}
}
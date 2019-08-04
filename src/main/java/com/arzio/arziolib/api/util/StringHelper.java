package com.arzio.arziolib.api.util;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class StringHelper {

	public static void writeString(DataOutput buf, String text) throws IOException {
		byte[] convertedText = text.getBytes(StandardCharsets.UTF_8);
		buf.writeShort(convertedText.length);
		buf.write(convertedText);
	}
	
	public static String appendStringFromIndex(String[] str, int index, int maxIndex) {
		StringBuilder sb = new StringBuilder();
		
		for (int i = index; i < maxIndex; i++) {
			sb.append(str[i]);
			if (i != maxIndex - 1) {
				sb.append(" ");
			}
		}
		
		return sb.toString();
	}

}

package com.royvandewater.rainman.util;

import java.io.IOException;
import java.io.InputStream;

public class StringFunctions {
	public static String streamToString(InputStream in) throws IOException {
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;) {
			out.append(new String(b, 0, n));
		}
		return out.toString();
	}
}

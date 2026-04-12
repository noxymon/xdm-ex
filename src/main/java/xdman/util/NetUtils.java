package xdman.util;

import java.io.*;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

import xdman.network.http.*;

public class NetUtils {
	public static byte[] getBytes(String str) {
		return str.getBytes();
	}

	public static final String readLine(InputStream in) throws IOException {
		StringBuffer buf = new StringBuffer();
		while (true) {
			int x = in.read();
			if (x == -1)
				throw new IOException(
						"Unexpected EOF while reading header line");
			if (x == '\n')
				return buf.toString();
			if (x != '\r')
				buf.append((char) x);
		}
	}

	public static final int getResponseCode(String statusLine) {
		String arr[] = statusLine.split(" ");
		if (arr.length < 2)
			return 400;
		return Integer.parseInt(arr[1]);
	}

	public static long getContentLength(HeaderCollection headers) {
		try {
			String clen = headers.getValue("content-length");
			if (clen != null) {
				return Long.parseLong(clen);
			} else {
				clen = headers.getValue("content-range");
				if (clen != null) {
					String str = clen.split(" ")[1];
					str = str.split("/")[0];
					String arr[] = str.split("-");
					return Long.parseLong(arr[1]) - Long.parseLong(arr[0]) + 1;
				} else {
					return -1;
				}
			}
		} catch (Exception e) {
			return -1;
		}
	}

	public static InputStream getInputStream(HeaderCollection respHeaders,
			InputStream inStream) throws IOException {
		String transferEncoding = respHeaders.getValue("transfer-encoding");
		if (!StringUtils.isNullOrEmptyOrBlank(transferEncoding)) {
			inStream = new ChunkedInputStream(inStream);
		}
		String contentEncoding = respHeaders.getValue("content-encoding");
		Logger.log("Content-Encoding: " + contentEncoding);
		if (!StringUtils.isNullOrEmptyOrBlank(contentEncoding)) {
			if (contentEncoding.equalsIgnoreCase("gzip")) {
				inStream = new GZIPInputStream(inStream);
			} else if (!(contentEncoding.equalsIgnoreCase("none")
					|| contentEncoding.equalsIgnoreCase("identity"))) {
				throw new IOException(
						"Content Encoding not supported: " + contentEncoding);
			}
		}
		return inStream;
	}

	public static void skipRemainingStream(HeaderCollection respHeaders,
			InputStream inStream) throws IOException {
		inStream = getInputStream(respHeaders, inStream);
		long length = getContentLength(respHeaders);
		skipRemainingStream(inStream, length);
	}

	public static void skipRemainingStream(InputStream inStream, long length)
			throws IOException {
		byte buf[] = new byte[8192];
		if (length > 0) {
			while (length > 0) {
				int r = (int) (length > buf.length ? buf.length : length);
				int x = inStream.read(buf, 0, r);
				if (x == -1)
					break;
				length -= x;
			}
		} else {
			while (true) {
				int x = inStream.read(buf);
				if (x == -1)
					break;
			}
		}
	}

	private static String getExtendedContentDisposition(String header) {
		try {
			String arr[] = header.split(";");
			for (String str : arr) {
				str = str.trim();
				if (str.toLowerCase().startsWith("filename*")) {
					int eqIdx = str.indexOf('=');
					if (eqIdx < 0) continue;
					String rfc5987 = str.substring(eqIdx + 1).trim();
					// Format: charset'language'encoded-value
					int firstQuote = rfc5987.indexOf('\'');
					int secondQuote = firstQuote >= 0 ? rfc5987.indexOf('\'', firstQuote + 1) : -1;
					if (firstQuote >= 0 && secondQuote > firstQuote) {
						String charset = rfc5987.substring(0, firstQuote).trim();
						String encodedValue = rfc5987.substring(secondQuote + 1);
						if (charset.isEmpty()) charset = "UTF-8";
						try {
							return URLDecoder.decode(encodedValue, charset);
						} catch (Exception e) {
							return URLDecoder.decode(encodedValue, StandardCharsets.UTF_8);
						}
					} else {
						// Fallback: no charset/language prefix, try last quote approach
						int index = rfc5987.lastIndexOf("'");
						if (index >= 0) {
							String st = rfc5987.substring(index + 1);
							return XDMUtils.decodeFileName(st);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getNameFromContentDisposition(String header) {
		try {
			if (header == null)
				return null;
			String headerLow = header.toLowerCase();
			if (headerLow.startsWith("attachment")
					|| headerLow.startsWith("inline")) {
				String name = getExtendedContentDisposition(header);
				if (name != null)
					return name;
				String arr[] = header.split(";");
				for (int i = 0; i < arr.length; i++) {
					String str = arr[i].trim();
					if (str.toLowerCase().startsWith("filename")) {
						int index = str.indexOf('=');
						String file = str.substring(index + 1).replace("\"", "")
								.trim();
						try {
							String decoded = XDMUtils.decodeFileName(file);
							// Heuristic: try UTF-8 re-decode of ISO-8859-1 interpreted string
							try {
								byte[] isoBytes = decoded.getBytes(StandardCharsets.ISO_8859_1);
								// Validate that isoBytes is actually valid UTF-8 before re-interpreting.
								// Must decode isoBytes directly — not a re-encoded Java String —
								// so that CodingErrorAction.REPORT can detect invalid sequences.
								StandardCharsets.UTF_8.newDecoder()
									.onMalformedInput(CodingErrorAction.REPORT)
									.onUnmappableCharacter(CodingErrorAction.REPORT)
									.decode(ByteBuffer.wrap(isoBytes));
								// isoBytes is valid UTF-8 — apply the re-interpretation
								decoded = new String(isoBytes, StandardCharsets.UTF_8);
							} catch (Exception e) {
								// Not valid UTF-8 — keep original decoded value
							}
							return decoded;
						} catch (Exception e) {
							return file;
						}

					}
				}
			}
		} catch (Exception e) {
		}
		return null;
	}

	public static String getCleanContentType(String contentType) {
		if (contentType == null || contentType.length() < 1)
			return contentType;
		int index = contentType.indexOf(";");
		if (index > 0) {
			contentType = contentType.substring(0, index).trim().toLowerCase();
		}
		return contentType;
	}
}

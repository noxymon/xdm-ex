package xdman.util;

import xdman.network.http.ChunkedInputStream;
import xdman.network.http.HeaderCollection;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

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
				if (str.toLowerCase().contains("filename*")) {
					// RFC 5987 format: filename*=charset'language'encoded-value
					int eqIndex = str.indexOf('=');
					if (eqIndex < 0) continue;
					String value = str.substring(eqIndex + 1).trim();
					// Remove surrounding quotes if present
					if (value.startsWith("\"") && value.endsWith("\"")) {
						value = value.substring(1, value.length() - 1);
					}
					// Extract charset from before first '
					int firstQuote = value.indexOf('\'');
					int secondQuote = firstQuote >= 0 ? value.indexOf('\'', firstQuote + 1) : -1;
					String charset = "UTF-8";
					String encodedValue = value;
					if (firstQuote >= 0 && secondQuote > firstQuote) {
						String extractedCharset = value.substring(0, firstQuote).trim();
						if (!extractedCharset.isEmpty()) {
							charset = extractedCharset;
						}
						encodedValue = value.substring(secondQuote + 1);
					}
					return java.net.URLDecoder.decode(encodedValue, java.nio.charset.Charset.forName(charset));
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
							file = XDMUtils.decodeFileName(file);
						} catch (Exception e) {
							// keep original value
						}
						// Apply UTF-8 re-decode heuristic for raw non-ASCII filenames
						try {
							String utf8Attempt = new String(file.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
							java.nio.charset.CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
								.onMalformedInput(CodingErrorAction.REPORT)
								.onUnmappableCharacter(CodingErrorAction.REPORT);
							decoder.decode(java.nio.ByteBuffer.wrap(utf8Attempt.getBytes(StandardCharsets.UTF_8)));
							file = utf8Attempt;
						} catch (Exception e) {
							// keep original value
						}
						return file;
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

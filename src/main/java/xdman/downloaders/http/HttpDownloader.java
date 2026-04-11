package xdman.downloaders.http;

import xdman.XDMConstants;
import xdman.downloaders.AbstractChannel;
import xdman.downloaders.Segment;
import xdman.downloaders.SegmentDownloader;
import xdman.downloaders.metadata.HttpMetadata;
import xdman.util.*;

public class HttpDownloader extends SegmentDownloader {
	private HttpMetadata metadata;
	private String newFileName;
	private boolean isJavaClientRequired;

	public HttpDownloader(String id, String folder, HttpMetadata metadata) {
		super(id, folder);
		this.metadata = metadata;
	}

	private static boolean isSignedUrl(String url) {
		if (url == null) return false;
		int queryIndex = url.indexOf('?');
		if (queryIndex < 0) return false;
		String query = url.substring(queryIndex + 1).toLowerCase();
		return query.contains("x-amz-signature") || query.contains("x-amz-expires")
				|| query.contains("signature=") || query.contains("token=");
	}

	@Override
	public AbstractChannel createChannel(Segment segment) {
		StringBuffer buf = new StringBuffer();
		metadata.getHeaders().appendToBuffer(buf);
		System.out.println("Headers all: " + buf);

		String urlToUse = metadata.getUrl();
		// Use cached resolved URL for signed URLs within expiry window
		if (isSignedUrl(urlToUse)) {
			String resolvedUrl = metadata.getResolvedUrl();
			long resolvedTs = metadata.getResolvedUrlTimestamp();
			if (resolvedUrl != null && resolvedTs > 0) {
				// Default expiry window: 5 minutes
				long expiryMs = 5 * 60 * 1000L;
				// Try to extract X-Amz-Expires from the resolved URL query
				try {
					int queryIdx = resolvedUrl.indexOf('?');
					if (queryIdx >= 0) {
						for (String param : resolvedUrl.substring(queryIdx + 1).split("&")) {
							if (param.toLowerCase().startsWith("x-amz-expires=")) {
								expiryMs = Long.parseLong(param.substring(param.indexOf('=') + 1)) * 1000L;
								break;
							}
						}
					}
				} catch (Exception ignored) {}
				if (System.currentTimeMillis() - resolvedTs < expiryMs) {
					urlToUse = resolvedUrl;
					Logger.log("Using cached resolved URL for signed URL");
				} else {
					Logger.log("Cached resolved URL expired, re-resolving");
					metadata.setResolvedUrl(null);
					metadata.setResolvedUrlTimestamp(0);
				}
			}
		}

		HttpChannel hc = new HttpChannel(segment, urlToUse, metadata.getHeaders(), length,
				isJavaClientRequired);
		return hc;
	}

	@Override
	public int getType() {
		return XDMConstants.HTTP;
	}

	@Override
	public boolean isFileNameChanged() {
		Logger.log("Checking for filename change " + (newFileName != null));
		return newFileName != null;
	}

	@Override
	public String getNewFile() {
		return newFileName;
	}

	@Override
	protected void chunkConfirmed(Segment c) {
		// logic
		// if the response has html content type and no attachment
		// no matter what is the target file extension, if any, will be changed to html.
		// If the download
		// has video conversion option, then conversion format will be removed.
		// in case of having an attachment, attachment extension will be used
		String oldFileName = getOutputFileName(false);
		HttpChannel hc = (HttpChannel) c.getChannel();
		this.isJavaClientRequired = hc.isJavaClientRequired();
		super.getLastModifiedDate(c);
		if (hc.isRedirected()) {
			metadata.setUrl(hc.getRedirectUrl());
			metadata.save();

			// newFileName = XDMUtils.getFileName(metadata.getUrl());
			//
			// if (outputFormat == 0) {
			// newFileName = XDMUtils.getFileName(metadata.getUrl());
			// Logger.log("set new filename: " + newFileName);
			// Logger.log("new file name: " + newFileName);
			// }
		}

		if ((hc.getHeader("content-type") + "").contains("text/html")) {
			if (hc.getHeader("content-disposition") == null) {
				newFileName = XDMUtils.getFileNameWithoutExtension(oldFileName) + ".html";
				outputFormat = 0;
			}
		}

		boolean nameSet = false;
		String contentDispositionHeader = hc.getHeader("content-disposition");
		if (contentDispositionHeader != null) {
			if (outputFormat == 0) {
				System.out.println("checking content disposition");
				String name = NetUtils.getNameFromContentDisposition(contentDispositionHeader);
				if (name != null) {
					this.newFileName = name;
					nameSet = true;
					Logger.log("set new filename: " + newFileName);
				}
			}
		}
		// if ((hc.getHeader("content-type") + "").contains("/html")) {
		// if (this.newFileName != null) {
		// String upperStr = this.newFileName.toUpperCase();
		// if (!(upperStr.endsWith(".HTML") || upperStr.endsWith(".HTM"))) {
		// outputFormat = 0;
		// this.newFileName += ".html";
		// Logger.log("set new filename: " + newFileName);
		// }
		// }
		// }
		if (!nameSet) {
			String ext = XDMUtils.getExtension(oldFileName);
			if (StringUtils.isNullOrEmptyOrBlank(ext)) {
				String newExt = MimeUtil.getFileExt(hc.getHeader("content-type"));
				if (newExt != null) {
					newFileName = oldFileName + "." + newExt;
				}
			}
			Logger.log("new filename: " + newFileName);
		}
	}

	@Override
	public HttpMetadata getMetadata() {
		return this.metadata;
	}

}

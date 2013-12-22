package at.wolfy.observer.streams;

import java.io.IOException;
import java.io.InputStream;

import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.services.Response;

/**
 * @see http://wiki.apache.org/tapestry/Tapestry5HowToStreamAnExistingBinaryFile
 */
public class AttachmentStreamResponse implements StreamResponse {
	
	public enum ContentType {
		WAV("audio/vnd.wave");
		private final String contentType;
		private ContentType(String contentType) {
			this.contentType = contentType;
		}
		public String getContentType() {
			return contentType;
		}
	}
	
	private InputStream is;
	private String filename;
	private ContentType contentType;
	
	public AttachmentStreamResponse(InputStream is, String filename, ContentType contentType) {
		this.is = is;
		this.filename = filename;
		this.contentType = contentType;
	}

	@Override
	public String getContentType() {
		return contentType.getContentType();
	}

	@Override
	public InputStream getStream() throws IOException {
		return is;
	}

	@Override
	public void prepareResponse(Response resp) {
		resp.setHeader("Content-Disposition", "attachment; filename=" + filename);
		resp.setHeader("Expires", "0");
		resp.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
		resp.setHeader("Pragma", "public");
	}
}

package com.automation.test.reader;

import com.automation.test.exception.TestIOException;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class GzipExtrator extends ArchiveExtractor {

	public GzipExtrator(Reader<byte[]> binaryReader) {
		super(binaryReader);
	}

	@Override
	public byte[] read() throws Exception {
		try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
			try (GZIPInputStream zis = new GZIPInputStream(new ByteArrayInputStream(parent.getData()))) {
				IOUtils.copy(zis, os);
			} catch (IOException e) {
				throw new TestIOException(
						"Could not extract files by GZIP, please check the permission or ensure it's not corrupt!", e);
			}

			return os.toByteArray();
		} catch (IOException e) {
			throw new TestIOException("Could not init output stream, please try again later!", e);
		}
	}

}

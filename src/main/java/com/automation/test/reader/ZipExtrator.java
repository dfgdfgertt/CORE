package com.automation.test.reader;

import com.automation.test.exception.TestIOException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;

public class ZipExtrator extends ArchiveExtractor {

	public ZipExtrator(Reader<byte[]> binaryReader) {
		super(binaryReader);
	}

	@Override
	public byte[] read() throws Exception {
		try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
			try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(parent.getData()))) {
				if (zis.getNextEntry() != null) {
					int len;
					byte[] buffer = new byte[1024];
					while ((len = zis.read(buffer)) > 0) {
						os.write(buffer, 0, len);
					}
					
					zis.closeEntry();
				} else {
					throw new ZipException("The zip data is corrupt!");
				}
			} catch (IOException e) {
				throw new TestIOException(
						"Could not extract files from ZIP, please check the permission or ensure it's not corrupt!", e);
			}

			return os.toByteArray();
		} catch (IOException e) {
			throw new TestIOException("Could not init output stream, please try again later!", e);
		}
	}

}

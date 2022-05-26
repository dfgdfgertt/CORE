package com.automation.test.core.unittest;

import com.automation.test.exception.TestIOException;
import com.automation.test.reader.*;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.testng.Assert.*;

public class DataReaderUnitTest {
	private Path tmpDirPath;

	@Test
	public void testBinaryReader() throws Exception {
		SimpleReader<String> simple = new SimpleReader<String>();
		simple.setData(Paths.get(tmpDirPath.toString(), "\\0.bin").toString());
		FileReader reader = new FileReader(simple);
		BinaryFileReader binReader = new BinaryFileReader(reader);
		assertEquals(binReader.read(), "0".getBytes());
	}

	@Test
	public void testNoBinaryFile() throws Exception {
		SimpleReader<String> simple = new SimpleReader<String>();
		simple.setData("@#%@#%#%");
		FileReader reader = new FileReader(simple);
		BinaryFileReader binReader = new BinaryFileReader(reader);
		assertThrows(TestIOException.class, () -> binReader.read());
	}

	@Test
	public void testGzipExtractor() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream("0".length());
		GZIPOutputStream gzip = new GZIPOutputStream(out);
		gzip.write("0".getBytes());
		gzip.close();

		SimpleReader<byte[]> simple = new SimpleReader<byte[]>();
		simple.setData(out.toByteArray());
		out.close();

		GzipExtrator reader = new GzipExtrator(simple);
		assertEquals(reader.read(), "0".getBytes());
		assertNotEquals(reader.read(), "0");
	}

	@Test
	public void testGzipExtractorCorruptedData() throws Exception {
		SimpleReader<byte[]> simple = new SimpleReader<byte[]>();
		simple.setData("#@$@#$@#$@$".getBytes());
		GzipExtrator reader = new GzipExtrator(simple);
		assertThrows(TestIOException.class, () -> reader.read());
	}

	@Test
	public void testZipExtractor() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream("0".length());
		ZipOutputStream zip = new ZipOutputStream(out);
		ZipEntry ze1 = new ZipEntry("ZipEntry1");
		zip.putNextEntry(ze1);
		zip.write("0".getBytes());
		zip.close();

		SimpleReader<byte[]> simple = new SimpleReader<byte[]>();
		simple.setData(out.toByteArray());
		out.close();

		ZipExtrator reader = new ZipExtrator(simple);
		assertEquals(reader.read(), "0".getBytes());
		assertNotEquals(reader.read(), "0");
	}

	@Test
	public void testZipExtractorCorruptedData() throws Exception {
		SimpleReader<byte[]> simple = new SimpleReader<byte[]>();
		simple.setData("#@$@#$@#$@$".getBytes());
		ZipExtrator reader = new ZipExtrator(simple);
		assertThrows(TestIOException.class, () -> reader.read());
	}

	@Test
	public void testBinaryToStringConverter() throws Exception {
		SimpleReader<byte[]> simple = new SimpleReader<byte[]>();
		simple.setData("0".getBytes());
		BinaryToStringConverter reader = new BinaryToStringConverter(simple);
		assertEquals(reader.read(), "0");
		assertNotEquals(reader.read(), "0".getBytes());
	}

	@Test
	public void testBinaryToStringConverterNoInput() throws Exception {
		SimpleReader<byte[]> simple = new SimpleReader<byte[]>();
		BinaryToStringConverter reader = new BinaryToStringConverter(simple);
		assertNull(reader.read());
	}

	@Test
	public void testFormatStringBuilder() throws Exception {
		SimpleReader<String> simple = new SimpleReader<String>();
		simple.setData("0.bin");
		FormatStringBuilder reader = new FormatStringBuilder(simple);
		reader.setFormat(tmpDirPath.toString() + "%s");
		assertEquals(reader.read(), tmpDirPath.toString() + "0.bin");
		assertNotEquals(reader.read(), tmpDirPath.toString() + "\\0.bin");
	}

	@Test
	public void testFormatStringBuilderNoFormat() throws Exception {
		SimpleReader<String> simple = new SimpleReader<String>();
		simple.setData("0.bin");
		FormatStringBuilder reader = new FormatStringBuilder(simple);
		assertEquals(reader.read(), "0.bin");
	}

	@BeforeClass
	public void init() throws IOException {
		tmpDirPath = Files.createTempDirectory("unittest_");
		com.google.common.io.Files.asByteSink(Paths.get(tmpDirPath.toString(), "\\0.bin").toFile())
				.write("0".getBytes());
	}

	@AfterClass
	public void cleanUp() throws IOException {
		FileUtils.deleteDirectory(tmpDirPath.toFile());
	}

}

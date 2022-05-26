package com.automation.test.helper;

import com.automation.test.exception.TestIOException;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFilenameFilter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;

/**
 * Network sharing helper
 * 
 * @author peter.tu.tran
 *
 */
public class SmbFileHelper {
	private NtlmPasswordAuthentication authentication;

	public SmbFileHelper(String domain, String username, String password) {
		super();
		this.authentication = new NtlmPasswordAuthentication(domain, username, password);
	}

	/**
	 * Delete all files in given folder
	 * 
	 * @param url Link to shared folder
	 * @throws MalformedURLException
	 * @throws SmbException
	 */
	public void cleanDirectory(String url) throws MalformedURLException, SmbException {
		SmbFile folder = new SmbFile(url, authentication);
		SmbFile[] files = folder.listFiles();
		for (SmbFile file : files) {
			file.delete();
		}
	}

	/**
	 * Create a folder if it does not exist
	 * 
	 * @param url Folder path
	 * @throws MalformedURLException
	 * @throws SmbException
	 */
	public void createDirectoryIfNotExist(String url) throws MalformedURLException, SmbException {
		createDirectoryIfNotExist(url, false);
	}

	/**
	 * Create a folder if it does not exist
	 * 
	 * @param url       Folder path
	 * @param recursive Create path if not exist
	 * @throws MalformedURLException
	 * @throws SmbException
	 */
	public void createDirectoryIfNotExist(String url, boolean recursive) throws MalformedURLException, SmbException {
		SmbFile folder = new SmbFile(url, authentication);
		if (!folder.exists()) {
			if (recursive) {
				folder.mkdirs();
			} else {
				folder.mkdir();
			}
		}
	}
	
	/**
	 * Create new smb file
	 * @param url Path of file to create
	 * @throws MalformedURLException
	 * @throws SmbException
	 */
	public void createFile(String url) throws MalformedURLException, SmbException {
		SmbFile file = new SmbFile(url, authentication);
		file.createNewFile();
	}

	/**
	 * Delete files matching give pattern
	 * 
	 * @param folderUrl   Link to shared folder
	 * @param filePattern File matching with this pattern will be removed
	 * @throws MalformedURLException
	 * @throws SmbException
	 */
	public void removeFiles(String folderUrl, String filePattern) throws MalformedURLException, SmbException {
		SmbFile folder = new SmbFile(folderUrl, authentication);
		SmbFile[] files = folder.listFiles(new SmbFilenameFilter() {
			@Override
			public boolean accept(SmbFile dir, String name) throws SmbException {
				return name.matches(filePattern);
			}
		});

		for (SmbFile file : files) {
			file.delete();
		}
	}

	/**
	 * Check if file exists
	 * 
	 * @param url Link to shared file
	 * @return true if exist
	 * @throws SmbException
	 * @throws MalformedURLException
	 */
	public boolean exists(String url) throws SmbException, MalformedURLException {
		try {
			return new SmbFile(url, authentication).exists();
		} catch (SmbException ex) { // Throw access denied when file not exist
			return false;
		}
	}

	/**
	 * List out files in given folder
	 * 
	 * @param url Link to shared folder
	 * @return Filenames in the folder
	 * @throws MalformedURLException
	 * @throws SmbException
	 */
	public String[] list(String url) throws MalformedURLException, SmbException {
		return new SmbFile(url, authentication).list();
	}

	public InputStream getInputStream(String url) throws TestIOException {
		try {
			return new SmbFile(url, authentication).getInputStream();
		} catch (MalformedURLException e) {
			throw new TestIOException(String.format("File path '%s' is not correct!", url), e);
		} catch (IOException e) {
			throw new TestIOException(String.format("Could not access file '%s', please check the permission!", url),
					e);
		}
	}

	public OutputStream getOutputStream(String url) throws MalformedURLException, IOException {
		return new SmbFile(url, authentication).getOutputStream();
	}
}

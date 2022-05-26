package com.automation.test;

import com.automation.test.reader.Reader;
import com.automation.test.verifier.Verifier;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Define a verification consisting of how to get and verify that data
 * @author peter.tu.tran
 * @param <T> Data type
 */
public class TestVerification<T> implements Verifiable {
	private Reader<T> reader;
	private Reader<T> nestedReader = null; // Actual reader to verify
	private Verifier<T, ?> verifier;
	private String verifiableInstruction;
	private String explanation;
	
	private int delayTime;
	
	/**
	 * Constructor
	 * @param reader How to get data
	 * @param verifier How to verify data
	 */
	public TestVerification(Reader<T> reader, Verifier<T, ?> verifier) {
		this.reader = reader;
		this.verifier = verifier;
		
		this.delayTime = 0;
	}
	
	/**
	 * Reader used to minimize actual before passing to verifier
	 * @return Same type nested reader
	 */
	public Reader<T> getNestedReader() {
		return nestedReader;
	}

	/**
	 * Set this if you need minimize actual before passing to verifier
	 * @param nestedReader Minimize actual data
	 */
	public void setNestedReader(Reader<T> nestedReader) {
		this.nestedReader = nestedReader;
	}


	/**
	 * Delay time before read and verify data
	 * @param delayTime
	 */
	public void setDelayTime(int delayTime) {
		this.delayTime = delayTime;
	}

	/**
	 * More description for verification
	 * @return Verifiable instruction
	 */
	public String getVerifiableInstruction() {
		return verifiableInstruction;
	}

	/**
	 * Give more description for verification
	 * @param verifiableInstruction Verifiable instruction
	 */
	public void setVerifiableInstruction(String verifiableInstruction) {
		this.verifiableInstruction = verifiableInstruction;
	}

	/**
	 * Get data from reader
	 * @return Actual data or null if reader is not set
	 * @throws Exception
	 */
	public T getActual() throws Exception {
		if (reader != null) {
			return reader.getData();
		}
		
		return null;
	}
	
	public Verifier<T, ?> getVerifier() {
		return verifier;
	}

	/**
	 * Get expected instruction and data
	 * @return Expected instruction and data
	 */
	public String getExpectedDescription() {
		StringBuilder sb = new StringBuilder();
		if (verifiableInstruction != null) {
			sb.append(verifiableInstruction);
			sb.append("\n");
		}
		
		if (verifier != null && verifier.getExpected() != null) {
			sb.append(String.format("%s", verifier.getExpected()));
		}
		
		return sb.toString();
	}

	public boolean isOk() throws Exception {
		try {
			if (delayTime > 0) {
				Thread.sleep(delayTime);
			}
			
			if (nestedReader != null) {
				return verifier.isOk(nestedReader.getData());
			} else {
				return verifier.isOk(getActual());
			}
		} catch (Throwable e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter();
		    e.printStackTrace(new PrintWriter(sw));
		    explanation = sw.toString();
		    throw e;
		}
	}
	
	/**
	 * Get info or error message during execution
	 * @return
	 */
	public String getMessage() {
		if (verifier != null) {
			return verifier.getMessage();
		} else {
			return "Verifier was not set";
		}
	}

	/**
	 * Get info how this verification passed or failed
	 * @return
	 */
	public String getExplanation() {
	    if (explanation != null) {
	        return explanation;
	    }
	    return verifier.getExplanation();
	}
	
	public void reset() {
		if (reader != null) {
			reader.resetCache();
		}
		if (verifier != null) {
			verifier.resetCache();
		}
	}
	
	@Override
	public String toString() {
		List<String> info = new ArrayList<String>();
		if (reader != null) {
			info.add(String.format("Reader: %s", reader.getClass()));
		}
		if (verifier != null) {
			info.add(String.format("Verifier: %s", verifier.getClass()));
		}
		
		return String.join("\n", info);
	}
}

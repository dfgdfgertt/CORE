package com.automation.test.verifier;

import com.automation.test.exception.TestIOException;
import org.apache.log4j.Logger;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/**
 * Verify if folder contains enough files with a given pattern name
 * @author peter.tu.tran
 *
 */
public class FilePatternVerifier extends AbstractFileVerifier<File, Long> {

	private Logger logger = Logger.getLogger(this.getClass());
	
    protected String folderPath;
    protected String pattern;
    protected CompareMethod compareMethod = CompareMethod.Equal;

    public FilePatternVerifier(String folderPath, String pattern, int timeout, CompareMethod compareMethod) {
        super(timeout);
        this.folderPath = folderPath;
        this.pattern = pattern;
        this.compareMethod = compareMethod;
    }

    public FilePatternVerifier(String folderPath, String pattern, int timeout) {
        super(timeout);
        this.folderPath = folderPath;
        this.pattern = pattern;
    }
    
    protected String[] list() throws TestIOException {
        File folder = new File(folderPath);
        return folder.list();
    }

    protected boolean isNumFilesOK(long[] actualFiles, long expectedFiles) throws TestIOException {
    	String dateTimeNow = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now());
		boolean ret = false;
		
		long nFiles = actualFiles[0] = Arrays.stream(list()).filter(f -> f.matches(pattern)).count();
		
		if (compareMethod == CompareMethod.Equal) {
			explanation = String.format("%s - Counting files matching '%s' in folder '%s'. Expected: %d, actual: %d", dateTimeNow,pattern, folderPath, expectedFiles, nFiles);
			ret = (nFiles == expectedFiles);
		} else if (compareMethod == CompareMethod.GreaterOrEqual) {
			explanation = String.format("%s - Counting files matching '%s' in folder '%s'. Expected >= %d, actual: %d", dateTimeNow,pattern, folderPath, expectedFiles, nFiles);
			ret = (nFiles >= expectedFiles);
		} else if (compareMethod == CompareMethod.Greater) {
			explanation = String.format("%s - Counting files matching '%s' in folder '%s'. Expected > %d, actual: %d", dateTimeNow,pattern, folderPath, expectedFiles, nFiles);
			ret = (nFiles > expectedFiles);
		} // else future compare type

		return ret;
    }

    @Override
    public boolean isOk(File actual) {
        long[] actualFiles = new long[1];
        
        long expectedFiles = 1; // Default
        Long expected = getExpected();
        if (expected != null) {
            expectedFiles = expected;
        }

        if (compareMethod == CompareMethod.Equal) {
            logger.debug(String.format("Waiting for %d file(s) in %dms", expectedFiles, timeout));
        } else if (compareMethod == CompareMethod.GreaterOrEqual) {
            logger.debug(String.format("Waiting for file(s) in %dms (number of files >= %d)", timeout, expectedFiles));
        } else if (compareMethod == CompareMethod.Greater) {
            logger.debug(String.format("Waiting for file(s) in %dms (number of files > %d)", timeout, expectedFiles));
        } // else future compare

        
        int nSleep = getNSleep();
        try {
	        while (!isNumFilesOK(actualFiles, expectedFiles)) {
	            if (nSleep-- > 0) {
	                try {
	                    Thread.sleep(SLEEP_PERIOD);
	                } catch (InterruptedException e) {
	                    msg = String.format("Counting files matching '%s' in folder '%s' threw exception: %s", pattern, folderPath, e.getMessage());
	                    return false;
	                }
	            } else {
	            	msg = String.format("Counting files matching '%s' in folder '%s' timed out (exceed %d ms). Expected: %d, actual: %d", pattern, folderPath, timeout, expectedFiles, actualFiles[0]);
	                return false;
	            }
	        }
        } catch (TestIOException e) {
        	msg = String.format("Counting files matching '%s' in folder '%s' threw exception: %s", pattern, folderPath, e.getMessage());
            return false;
        }

        boolean ret = false;

        if (compareMethod == CompareMethod.Equal) {
            ret = (actualFiles[0] == expectedFiles);
            msg = String.format("Counting files matching '%s' in folder '%s'. Expected: %d, actual: %d", pattern, folderPath, expectedFiles, actualFiles[0]);
        } else if (compareMethod == CompareMethod.GreaterOrEqual) {
            ret = (actualFiles[0] >= expectedFiles);
            msg = String.format("Counting files matching '%s' in folder '%s'. Expected: >= %d, actual: %d", pattern, folderPath, expectedFiles, actualFiles[0]);
        } else if (compareMethod == CompareMethod.Greater) {
            ret = (actualFiles[0] > expectedFiles);
            msg = String.format("Counting files matching '%s' in folder '%s'. Expected: > %d, actual: %d", pattern, folderPath, expectedFiles, actualFiles[0]);
        }// else future compare type

        return ret;
    }

}

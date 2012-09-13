package co.sf.install.test;

import java.util.ArrayList;

import co.sf.install.exception.InstallMgrException;
import co.sf.install.reader.InstallMgrReader;
import junit.framework.TestCase;

public class InstallMgrReaderTest extends TestCase {

	public void testProcessArguments() 
	{
		String fileName = "problema.in.txt";
		ArrayList<String> inputCmds = new ArrayList<String>();
		try {
			inputCmds = InstallMgrReader.processArguments(fileName);
			assertTrue(inputCmds.size() > 0);
			assertEquals(21, inputCmds.size());
		} catch(Exception e) {
			assertTrue(false); //should not come here
		}
	}
	
	public void testProcessArgumentsNoFileFound() 
	{
		String fileName = "medontexist.in.txt";
		ArrayList<String> inputCmds = new ArrayList<String>();
		try {
			inputCmds = InstallMgrReader.processArguments(fileName);
			assertTrue(false); //should not come here
		} catch(Exception e) {
			assertTrue(e instanceof InstallMgrException);
			String msg = String.format("The file %s is not found.\n", fileName);
			assertEquals(msg, e.getMessage());
		}
	}

	public void testProcessArgumentsEmptyFile() 
	{
		String fileName = "emptyTestFile.in.txt";
		ArrayList<String> inputCmds = new ArrayList<String>();
		try {
			inputCmds = InstallMgrReader.processArguments(fileName);
			assertTrue(false); //should not come here
		} catch(Exception e) {
			assertTrue(e instanceof InstallMgrException);
			String msg = String.format("The file %s is empty.\n", fileName);
			assertEquals(msg, e.getMessage());
		}
	}
}

package co.sf.install.reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;


import co.sf.install.exception.InstallMgrException;

public class InstallMgrReader {

	private InstallMgrReader() {} //non-instantiable outside the class
	
	/**
	 * Process the entries in the input file. Skip empty lines.
	 * 
	 * I used custom InstallMgrException which is a RuntimeException to handle
	 * either a checked or unchecked exception encounter at runtime. This will
	 * simplify the method definition and caller's code in handling exceptions.
	 * 
	 * @param fileName String name of input file
	 * @return ArrayList<String> array of strings representing non-empty entries in the input file
	 * @exception InstallMgrException
	 */
	public static ArrayList<String> processArguments(String fileName) {
		FileReader fr = null;
		BufferedReader br = null;
		ArrayList<String> inputCmds = new ArrayList<String>();
		try {
			fr = new FileReader(fileName);
			br = new BufferedReader(fr);
			String s = null;
			int count = 0;
			while ((s = br.readLine()) != null) {
				if (s.isEmpty()) {
					continue;
				}
				inputCmds.add(s);
				count++;
			}
//			for (String cmd : inputCmds) {
//				System.out.println(cmd);
//			}
			if (count == 0) {
				throw new NoSuchElementException("Input file cannot be empty");
			}
			return inputCmds;
		} catch (FileNotFoundException e) {
			String msg = String.format("The file %s is not found.\n", fileName);
			throw new InstallMgrException(msg, e);
		} catch (NoSuchElementException e) {
			String msg = String.format("The file %s is empty.\n", fileName);
			throw new InstallMgrException(msg, e);
		} catch (Exception e) {
			throw new InstallMgrException("Failed to process input areguemnts", e);
		} finally {
			try {
				if (br != null) br.close();
				if (fr != null) fr.close();				
			} catch(IOException e) {
				e.printStackTrace(); //don't throw anymore excpetion from finally. Just print out the stack trace.
			}
		}
	}
}

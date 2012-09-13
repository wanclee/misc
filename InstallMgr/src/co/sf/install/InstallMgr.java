package co.sf.install;

import java.util.ArrayList;

import co.sf.install.dependency.InstallMgrDependency;
import co.sf.install.reader.InstallMgrReader;

public class InstallMgr {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {			
			String option = args[0]; //don't care for now, just an option
			String inputFile = args[1]; //the input file to be processed
			ArrayList<String> list = InstallMgrReader.processArguments(inputFile);
			InstallMgrDependency dep = new InstallMgrDependency();
			dep.ProcessCmds(list);
			
			System.exit(0); //normal exit
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1); //abnormal exit, return 1
		}
	}

}

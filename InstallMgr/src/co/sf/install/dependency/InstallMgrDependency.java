package co.sf.install.dependency;

import java.util.ArrayList;
import java.util.HashMap;

import co.sf.install.constant.LimitUtil;
import co.sf.install.constant.MgrAction;
import co.sf.install.exception.InstallMgrException;
import co.sf.install.util.InstallMgrUtil;

/**
 * Manage the installation actions and dependencies
 * It uses three simple HashMaps to keep track of dependencies among components and installed components
 * compDependency is the dependency map from the consuming component's perspective
 * reverseCompDependency is the reverse perspective as seen from the dependent's perspective; i.e. who has depended on 'me'.
 * installedComps keeps a list of installed components.
 * 
 * @author wan
 *
 */
/**
 * @author wan
 *
 */
public class InstallMgrDependency {

	//Storing the dependency of components; the key is the component that depends on the list of other componeents
	private HashMap<String,String[]> compDependency = new HashMap<String, String[]>();
	//Storing the reverse dependency of components; this serves as reference check for consuming components as seen from dependent component perspective
	private HashMap<String,String[]> reverseCompDependency = new HashMap<String, String[]>();
	//storing installed components
	private HashMap<String, Boolean> installedComps = new HashMap<String, Boolean>();
	
	public InstallMgrDependency() {}
	
	public void ProcessCmds(ArrayList<String> inputLines) {
		String[] items = null;
		String action = null;
		String key = null;
		for (String lineItem : inputLines) {
			items = lineItem.split(" ");
			items = InstallMgrUtil.compactArray(items);
			if (items.length == 0) {
				throw new InstallMgrException("The command line must at least specify an action verb");
			}
			System.out.println("*****");
			System.out.println(lineItem); //echo the line of action and components
			action = items[0]; //index 0 is the action verb
			if (action.equals(MgrAction.DEPEND)) {
				key = items[1]; //index 1 is the first component
				doDepend(key, items); //for simplicity, we chose not to clear the action verb and first component before passing to doDepend
			} else if (action.equals(MgrAction.LIST)) {
				doList();
			} else if (action.equals(MgrAction.REMOVE)) {
				key = items[1];
				doRemove(key);
			} else if (action.equals(MgrAction.INSTALL)) {
				key = items[1];
				doInstall(key);
			} else if (action.equals(MgrAction.END)) {
				doEnd();
			} else {
				throw new InstallMgrException("Unsupported action");
			}
		}
	}
	
	/**
	 * Build both the dependency and reverse dependency maps
	 * @param key String name of component
	 * @param items String[] containing all items found in a single line entry 
	 * in the input file, including the action verb item.
	 * @exception InstallMgrException
	 */
	public void doDepend(String key, String[] items) {
		String[] values = null;
		int len = items.length;
		
		if (len < 3) {
			throw new InstallMgrException("There must be at least two components following the action verb");
		}
		values = new String[len-2]; //account for the action verb and first component
		System.arraycopy(items, 2, values, 0, len - 2);
		String[] v = compDependency.get(key);
		if (v == null) {
			compDependency.put(key, values);
		} else {
			String[] mergedArr = InstallMgrUtil.mergeArray(v, values);
			compDependency.put(key, mergedArr);
		}
	
		generateReverseDependency(key, items);
	}
	
	/**
	 * Display a list of installed components
	 */
	public void doList() {
		String[] comps = getInstalledComponents();
		for (int i = 0; i < comps.length; i++) {
			System.out.println("\t" + comps[i]);
		}
	}
		
	/**
	 * Remove installed component if it is indeed an installed component 
	 * and there is no usage reference of it found in the reverse dependency map
	 * 
	 * @param target String name of component to be removed
	 */
	public void doRemove(String target) {
		if (!isInstalled(target)){
			String msg = String.format("\tCannot remove component %s as it is not yet installed", target);
			System.out.println(msg);
			return;
		}
		String[] dependencies = null;
		dependencies = reverseCompDependency.get(target);
		if (dependencies == null || dependencies.length == 0) {
			installedComps.remove(target);
			System.out.printf("\t'%s' is removed\n", target);
		} else {
			boolean bOk = checkReverseDependency(dependencies, true);
			if (bOk) { //if there is any installed component still consuming the target, we can't remove the target component
				System.out.printf("\t Unable to remove installed component '%s' as it is still\n" +
						"\t\t being used by other components\n", target);
			} else {
				installedComps.remove(target);
				System.out.printf("\t'%s' is removed\n", target);
				
			}
		}
	}
	
	/**
	 * Verify if the component's dependencies are satisfied. Install the component and update the
	 * installedComp map.
	 * @param key String name of component to be installed
	 */
	public void doInstall(String key) {
		Boolean bInstalled = installedComps.get(key);
		if (bInstalled != null && bInstalled.booleanValue()) {
			String msg = String.format("\tThe component %s has already been installed", key);
			System.out.println(msg);
			return;
		}
		String[] v = compDependency.get(key);
		boolean bDependencyOk = false;
		System.out.println("\t" + "installing..." + key);
		if (v != null) {
			bDependencyOk = checkDependency(v, true);
			if (!bDependencyOk) {
				String msg = String.format("Failed to install component '%s', " +
						"not all the dependencies are available", key);
				System.out.println("\t\t" + msg);
				//throw new InstallMgrException(msg);
				return;
			}

		}
		installedComps.put(key, new Boolean(true));
		System.out.println("\t\t" + key + " is installed");
	}
	
	/**
	 * Halt the execution and exit 
	 */
	public void doEnd() {
		System.exit(0);
	}

	/**
	 * Get dependency for a component
	 * @param component String name of component
	 * @return String[] array of dependent components; empty string[] if no dependency is found
	 */
	public String[] getDependency(String component) {
		String[] v = compDependency.get(component);
		if (v == null) {
			return new String[0];
		}
		return v;
	}

	/**
	 * Get a list of installed components
	 * @return String[] array of installed components
	 */
	public String[] getInstalledComponents() {
		//iterate through the keys in installedComps to list out all the installed components
		String[] comps = new String[installedComps.size()];
		int i = 0;
		for (String key :installedComps.keySet()) {
			if (key != null && !key.isEmpty()) {
				comps[i++] = key;
			}
		}
		return InstallMgrUtil.compactArray(comps);
	}
	
	/**
	 * Get a list of reverse dependency
	 * @param component String name of component
	 * @return String[] array of components who have a reverse relationship with the input component
	 */
	public String[] getReverseDependency(String component) {
		String[] v = reverseCompDependency.get(component);
		if (v == null) {
			return new String[0];
		}
		return v;
	}
	
	/**
	 * @param component String name of component
	 * @return boolean true if component is an installed component; otherwise false
	 */
	public boolean isInstalled(String component) {
		Boolean bInstalled = installedComps.get(component);
		return (bInstalled != null);
	}
	
	/**
	 * Recursively traverse compDependency map and verify if dependency exists
	 * TODO: Check for cyclic dependency so we don't end up in infinity loop
	 * @param dependencies String[] array of dependent components
	 * @param boolean bOk true means dependency exists; otherwise non-existent
	 * @return boolean
	 */
	private boolean checkDependency(String[] dependencies, boolean bOk) {
		String[] v = null;
		for (int i = 0; i < dependencies.length; i++) {
			bOk = isInstalled(dependencies[i]);
			if (!bOk) {
				break;
			}
			v = compDependency.get(dependencies[i]);
			if (v != null) {
				return bOk &= checkDependency(v, bOk); //all dependency must be found; 
			}			
		}
		return bOk;
	}
	
	private boolean checkReverseDependency(String[] dependencies, boolean bOk) {
		String[] v = null;
		for (int i = 0; i < dependencies.length; i++) {
			bOk &= isInstalled(dependencies[i]);
			if (!bOk) {
				break;
			}
			v = reverseCompDependency.get(dependencies[i]);
			if (v != null && v.length > 0) {
				return bOk &= checkReverseDependency(v, bOk); //all dependency must be found; 
			}			
		}
		return bOk;
	}
	
	/**
	 * Generate the reverse dependency map for all the components that are being dependent upon
	 * @param key String name of component
	 * @param values String[] array of items obtained from the input file, including action and first component.
	 */
	private void generateReverseDependency(String key, String[] values) {
		String component = null;
		String[] dependencies = null;
		for (int i = 2; i < values.length; i++) { //dependent components start from index 2 and onward...
			component = values[i];
			dependencies = reverseCompDependency.get(component);
			if (dependencies == null) {
				String[] newDependencies = new String[LimitUtil.INCREMENT_SIZE];
				newDependencies[0] = key;
				reverseCompDependency.put(component, newDependencies);				
			} else {
				boolean bAssigned = false;
				int j = 0;
				for (; j < dependencies.length; j++) {
					if (dependencies[j] == key) {
						bAssigned = true;
						continue; //do nothing as the dependent component is already in the dependencies array
					} else if (dependencies[j] == null) {
						dependencies[j] = key;
						bAssigned = true;
						break;
					}
				}
				if (!bAssigned) { //the array doesn't have the entry that match
					String[] newDependencies = new String[LimitUtil.INCREMENT_SIZE];
					newDependencies[0] = key;
					String[] newArr = InstallMgrUtil.mergeArray(dependencies, newDependencies);
					reverseCompDependency.put(component, newArr);
				} else {
					reverseCompDependency.put(component, dependencies);					
				}
			}
			
		}
	}
	
}

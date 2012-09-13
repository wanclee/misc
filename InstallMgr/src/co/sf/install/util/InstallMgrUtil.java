package co.sf.install.util;

public class InstallMgrUtil {
	private InstallMgrUtil() {}

	/**
	 * Merge two String arrays
	 * @param arr1 String[]
	 * @param arr2 String[]
	 * @return String[] merged array
	 */
	public static String[] mergeArray(String[] arr1, String[] arr2) {
		int len1 = arr1.length;
	    int len2 = arr2.length;
		int len = len1 + len2;
		String[] newArr = new String[len]; //worse case: the array grows to take all unique elements
		//TODO: performance is still acceptable of the size of arrays are small; we will live with it for now
		//TODO: in the interest of time, we will simply merge two arrays without worrying about duplicates
		System.arraycopy(arr1, 0, newArr, 0, len1);
		System.arraycopy(arr2, 0, newArr, len1, len2);
		return newArr; 
	}
	
	/**
	 * Items contain empty string in the array. The method will purges the empty entries
	 * and return a compacted non-empty entries array
	 *  
	 * @param arr String[] array of items
	 * @return String[] array of non-empty items
	 */
	
	public static String[] compactArray(String[] arr) {
		String[] newArr = new String[arr.length];
		int j = 0;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == null || arr[i].isEmpty()) {
				continue;
			}
			newArr[j++] = arr[i];
		}
		String[] tempArr = new String[j];
		if (j > 0) {
			System.arraycopy(newArr, 0, tempArr, 0, j);
		}
		return tempArr;
	}
}

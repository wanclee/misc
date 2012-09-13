package co.sf.install.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Register the test classes as suites for batch testing from one place
 * @author wan
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
	InstallMgrDependencyTest.class,
	InstallMgrReaderTest.class})
public class InstallMgrTestAll {

}

package co.sf.install.test;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import co.sf.install.dependency.InstallMgrDependency;

import junit.framework.TestCase;

public class InstallMgrDependencyTest extends TestCase {
	protected InstallMgrDependency dep = null;
	protected ArrayList<String> list = null;
	
	@Before
	protected void setUp() throws Exception {
		super.setUp();
		dep = new InstallMgrDependency();
		list = new ArrayList<String>();
//		list.add("DEPEND   TELNET TCPIP NETCARD");
//		list.add("DEPEND TCPIP NETCARD");
//		list.add("DEPEND DNS TCPIP NETCARD");
//		list.add("DEPEND  BROWSER   TCPIP  HTML");
	}
	
	@After
	protected void tearDown() throws Exception {
		dep = null;
		list.clear();
		list = null;
		super.tearDown();
	}
	
	
	public void testDepend() {
		list.add("DEPEND TELNET TCPIP NETCARD");
		dep.ProcessCmds(list);
		String[] dependencies = dep.getDependency("TELNET");
		assertTrue(dependencies != null);
		assertEquals(2, dependencies.length);
		assertEquals("TCPIP", dependencies[0]);
		assertEquals("NETCARD", dependencies[1]);
	}
	
	public void testInstall() {
		list.add("INSTALL NETCARD");
		list.add("INSTALL HTML");
		dep.ProcessCmds(list);
		assertTrue(dep.isInstalled("NETCARD"));
		assertTrue(dep.isInstalled("HTML"));
	}
	
	public void testInstallNegative() {
		list.add("DEPEND TELNET TCPIP NETCARD");
		list.add("INSTALL TELNET");
		dep.ProcessCmds(list);
		assertTrue(!dep.isInstalled("TELNET"));
	}
	
	public void testList() {
		list.add("INSTALL NETCARD");
		list.add("INSTALL HTML");
		list.add("LIST");
		dep.ProcessCmds(list);
		String[] comps = dep.getInstalledComponents();
		assertTrue(comps != null);
		assertEquals(2, comps.length);
		assertTrue(dep.isInstalled("NETCARD"));
		assertTrue(dep.isInstalled("HTML"));
	}
	
	public void testRemove() {
		list.add("INSTALL NETCARD");
		list.add("INSTALL HTML");
		list.add("LIST");
		list.add("REMOVE HTML");
		dep.ProcessCmds(list);
		String[] comps = dep.getInstalledComponents();
		assertTrue(comps != null);
		assertEquals(1, comps.length);
		assertTrue(dep.isInstalled("NETCARD"));
		assertTrue(!dep.isInstalled("HTML"));
	}
	
	public void testRemoveNegative() {
		list.add("DEPEND   TELNET TCPIP NETCARD");
		list.add("INSTALL NETCARD");
		list.add("INSTALL TCPIP");
		list.add("INSTALL TELNET");
		dep.ProcessCmds(list);
		String[] comps = dep.getInstalledComponents();
		assertTrue(comps != null);
		assertEquals(3, comps.length);
		assertTrue(dep.isInstalled("TELNET"));
		assertTrue(dep.isInstalled("TCPIP"));
		assertTrue(dep.isInstalled("NETCARD")); //verify NETCARD is still around
	}
	
	public void testAll() {
		list.add("DEPEND   TELNET TCPIP NETCARD");
		list.add("DEPEND TCPIP NETCARD");
		list.add("DEPEND DNS TCPIP NETCARD");
		list.add("DEPEND  BROWSER   TCPIP  HTML");
		list.add("INSTALL NETCARD");
		list.add("INSTALL TELNET");
		list.add("INSTALL foo");
		list.add("REMOVE NETCARD");
		list.add("INSTALL BROWSER");
		list.add("INSTALL DNS");
		list.add("LIST");
		list.add("REMOVE TELNET");
		list.add("REMOVE NETCARD");
		list.add("REMOVE DNS");
		list.add("REMOVE NETCARD");
		list.add("INSTALL NETCARD");
		list.add("REMOVE TCPIP");
		list.add("REMOVE BROWSER");
		list.add("REMOVE TCPIP");
		list.add("LIST");
		dep.ProcessCmds(list);
		String[] comps = dep.getInstalledComponents();
		assertTrue(comps != null);
		assertEquals(2, comps.length);
		assertTrue(dep.isInstalled("NETCARD"));
		assertTrue(dep.isInstalled("foo"));
		
	}
	
}

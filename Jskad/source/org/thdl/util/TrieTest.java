package org.thdl.util;

import junit.framework.TestCase;

/**
 * @author David Chandler
 *
 * Tests {@link org.thdl.util.Trie} at the unit level.
 */
public class TrieTest extends TestCase {

	/**
	 * Constructor for TrieTest.
	 * @param arg0
	 */
	public TrieTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TrieTest.class);
	}

	/**
	 * Tests that the trie distinguishes between uppercase and
	 * lowercase.  */
	public void testCaseSupport() {
        Trie t = new Trie();
        t.put("S", "S value");
        assertTrue(t.get("S").equals("S value"));
        assertTrue(null == t.get("s"));
        t.put("Sh", "Sh value");
        assertTrue(t.get("Sh").equals("Sh value"));
        assertTrue(t.get("S").equals("S value"));
        assertTrue(null == t.get("s"));
        assertTrue(null == t.get("sh"));
        assertTrue(null == t.get("SH"));
        assertTrue(null == t.get("sH"));
    }

	/**
	 * Test for put(String, Object)
	 */
	public void testReplacement() {
        Trie t = new Trie();
        t.put("S", "S value 1");
        assertTrue(t.get("S").equals("S value 1"));
        t.put("S", "S value 2");
        assertTrue(t.get("S").equals("S value 2"));
    }

	/**
	 * Tests that put(*, null) throws a NullPointerException.
	 */
	public void testPuttingNull() {
        Trie t = new Trie();
        boolean threw = false;
        try {
            t.put("heya", null);
        } catch (NullPointerException e) {
            threw = true;
        }
        assertTrue(threw);
    }

	/**
	 * Test for put(String, Object)
	 */
	public void testPrefix() {
        Trie t = new Trie();
        t.put("t", "t value");
        t.put("ts", "ts value");
        t.put("tsh", "tsh value");
        assertTrue(t.get("t").equals("t value"));
        assertTrue(t.get("ts").equals("ts value"));
        assertTrue(t.get("tsh").equals("tsh value"));
        assertTrue(t.hasPrefix("t"));
        assertTrue(t.hasPrefix("ts"));
        assertTrue(!t.hasPrefix("tsh"));
	}
}

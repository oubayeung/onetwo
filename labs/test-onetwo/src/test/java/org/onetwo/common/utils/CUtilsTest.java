package org.onetwo.common.utils;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class CUtilsTest {

	@Test
	public void testAsArray(){
		String[] strs = new String[]{"aa", "bb"};
		Object[] objs = new Object[]{"aa", "bb"};
		
		String[] fields = (String[])CUtils.asArray(strs);
		Assert.assertTrue(strs==fields);
		
		try {
			fields = (String[])CUtils.asArray(objs);
			Assert.fail();
		} catch (Exception e) {
			Assert.assertTrue(ClassCastException.class==e.getClass());
		}
		
		fields = CUtils.asStringArray(strs);
		Assert.assertTrue(strs==fields);
		
		fields = CUtils.asStringArray(objs);
		Assert.assertTrue(strs!=fields);
		Assert.assertEquals(strs[0], fields[0]);
		Assert.assertEquals(strs[1], fields[1]);
		

		fields = CUtils.asStringArray("aa");
		Assert.assertEquals(strs[0], fields[0]);

		Set<String> formats = CUtils.asSet("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "HH:mm:ss", "yyyy-MM-dd HH:mm");
		Assert.assertEquals(formats.size(), 4);
	}
}

package org.onetwo.common.excel;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.onetwo.common.utils.CUtils;

import com.google.common.collect.Sets;

public class ExcelCompareTest {

	private String basePath = "org/onetwo/common/excel/";
	
	private String path1 = basePath + "bus_copy.xls";
	private String path2 = basePath + "bus_origin.xls";
	
	@Test
	public void test(){
		BeanRowMapper<HashMap> bm = BeanRowMapper.map(4, HashMap.class, 1, "carno", 2, "cardno");
		Map<String, List<?>> map1 = WorkbookReaderFactory.createWorkbookByMapper(bm).readData(path1, 2, 1);
		List<HashMap<String, Object>> copyData = (List<HashMap<String, Object>>)map1.entrySet().iterator().next().getValue();
		Map<String, List<?>> map2 = WorkbookReaderFactory.createWorkbookByMapper(bm).readData(path2, 2, 1);
		List<HashMap<String, Object>> originData = (List<HashMap<String, Object>>)map2.entrySet().iterator().next().getValue();
		System.out.println("size1: " + copyData.size());
		System.out.println("size2: " + originData.size());
		
//		Set<HashMap<String, Object>> diff = Sets.difference(new HashSet<HashMap<String, Object>>(data1), new HashSet<HashMap<String, Object>>(data2));
//		Map<String, List<?>> data2 = WorkbookReaderFactory.getWorkbookReader(HashMap.class).readData(path2, 2, 1);
		Collection<HashMap<String, Object>> diff = CUtils.difference(copyData, originData, "carno", "cardno");
		System.out.println("copyData diff originData: " + diff.size());
		System.out.println("copyData diff originData: " + diff);
		Collection<HashMap<String, Object>> diff2 = CUtils.difference(originData, copyData, "carno", "cardno");
		System.out.println("originData diff copyData: " + diff2.size());
		System.out.println("originData diff copyData: " + diff2);
	}

}

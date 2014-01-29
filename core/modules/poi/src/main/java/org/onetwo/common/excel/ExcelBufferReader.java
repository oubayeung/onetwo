package org.onetwo.common.excel;

public interface ExcelBufferReader<T> {

	void initReader();
	T read();
	boolean isEnd();

}
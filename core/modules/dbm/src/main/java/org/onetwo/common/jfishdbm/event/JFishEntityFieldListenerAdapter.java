package org.onetwo.common.jfishdbm.event;

public class JFishEntityFieldListenerAdapter implements DbmEntityFieldListener {

	@Override
	public Object beforeFieldInsert(String fieldName, Object value) {
		return value;
	}

	@Override
	public Object beforeFieldUpdate(String fieldName, Object value) {
		return value;
	}

}

package org.onetwo.common.jfishdbm.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.onetwo.common.convert.Types;
import org.onetwo.common.date.Dates;
import org.onetwo.common.jfishdbm.annotation.DbmFieldListeners;
import org.onetwo.common.jfishdbm.event.DbmEntityFieldListener;
import org.onetwo.common.jfishdbm.exception.DbmException;
import org.onetwo.common.jfishdbm.mapping.SqlTypeFactory;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.JFishProperty;
import org.onetwo.common.utils.LangUtils;
import org.springframework.jdbc.core.SqlParameterValue;

final public class DbmUtils {
	private DbmUtils(){
	}
	public static List<DbmEntityFieldListener> initJFishEntityFieldListeners(DbmFieldListeners listenersAnntation){
		List<DbmEntityFieldListener> fieldListeners = Collections.EMPTY_LIST;
		if(listenersAnntation!=null){
			fieldListeners = LangUtils.newArrayList();
			Class<? extends DbmEntityFieldListener>[] flClasses = listenersAnntation.value();
			for(Class<? extends DbmEntityFieldListener> flClass : flClasses){
				if(flClass==null)
					continue;
				DbmEntityFieldListener fl = ReflectUtils.newInstance(flClass);
				fieldListeners.add(fl);
			}
		}
		return fieldListeners;
	}
	
	public static Object convertPropertyValue(JFishProperty propertyInfo, Object value){
		Object actualValue = value;
		Class<?> propType = propertyInfo.getType();
		if(Enum.class.isAssignableFrom(propType)){
			Enumerated enumerated = propertyInfo.getAnnotation(Enumerated.class);
			if(enumerated!=null){
				EnumType etype = enumerated.value();
				if(etype==EnumType.ORDINAL){
					actualValue = Types.asValue(Integer.valueOf(value.toString()), propType);
				}else if(etype==EnumType.STRING){
					actualValue = Types.asValue(value.toString(), propType);
				}else{
					throw new DbmException("error enum type: " + etype);
				}
			}
			
		}else if(Date.class.isInstance(value) && Temporal.class.isAssignableFrom(propType)){
			Date date = (Date) value;
			if(LocalDate.class.isAssignableFrom(propType)){
				actualValue = Dates.toLocalDate(date);
			}else if(LocalTime.class.isAssignableFrom(propType)){
				actualValue = Dates.toLocalTime(date);
			}else if(LocalDateTime.class.isAssignableFrom(propType)){
				actualValue = Dates.toLocalDateTime(date);
			}
		}
		return actualValue;
	}
	
	public static Object convertSqlParameterValue(JFishProperty propertyInfo, Object value){
		if(value==null)
			return null;
		int sqlType = SqlTypeFactory.getType(propertyInfo.getType());
		SqlParameterValue sqlValue = new SqlParameterValue(sqlType, value);
		return sqlValue;
	}
}
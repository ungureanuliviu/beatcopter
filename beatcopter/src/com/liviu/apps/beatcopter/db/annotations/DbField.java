package com.liviu.apps.beatcopter.db.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.liviu.apps.beatcopter.db.DBConstants;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DbField {	
	public String defaultValue() default DBConstants.DEFAULT_FIELD_NO_DEFAULT_VALUE;	
	public boolean unique() default DBConstants.DEFAULT_FIELD_UNICITY;
	public boolean autoincrement() default DBConstants.DEFAULT_FIELD_AUTOINCREMENT; // we have to check if it is a supportable type
	public boolean canBeNull() default DBConstants.DEFAULT_FIELD_CAN_BE_NULL;
	public boolean primaryKey() default DBConstants.DEFAULT_FIELD_PRIMARY_KEY;
}

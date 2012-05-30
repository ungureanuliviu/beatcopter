package com.liviu.apps.beatcopter.db.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

import com.liviu.apps.beatcopter.db.DBConstants;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DbTable {
	/** 
	 * Define the name of this table
	 * If a name is NOT specified, we will use the class's name.
	 */
    public String name() default DBConstants.TABLE_NO_NAME;    
}

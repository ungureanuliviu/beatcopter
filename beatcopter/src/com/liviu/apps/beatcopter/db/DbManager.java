package com.liviu.apps.beatcopter.db;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import android.R.array;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.liviu.apps.beatcopter.db.annotations.DbField;
import com.liviu.apps.beatcopter.db.annotations.DbTable;
import com.liviu.apps.beatcopter.utils.Console;
import com.liviu.apps.beatcopter.utils.Utils;

public class DbManager {
	// Constants
	private final String TAG = "DbManager";	
	private final String DB_NAME = "db_name";	
	private final String DB_PREFS_KEY_TABLE_HASHCODE = "key_hascode";
	
	private static DbManager mInstance;
	private Context mContext;
	private SQLiteDatabase mDb;
	private String mPrefsName;
	private SharedPreferences mPrefs;
	
	private DbManager(Context pContext) {
		Console.debug(TAG, "db init", Console.Liviu);
		mPrefsName = pContext.getPackageName();
		mPrefs = pContext.getSharedPreferences(mPrefsName, Context.MODE_PRIVATE);
		mContext = pContext;
	}
	
	public static DbManager getInstance(Context pContext){
		if(null == mInstance){
			return (mInstance = new DbManager(pContext));
		}else{
			return mInstance;
		}
	}
	
	public boolean registerTables(Class[] classes){
		try{
			String hashcode = Integer.toString(classes.length);
			for(Class<?> clazz : classes){
				hashcode += clazz.getName();
			}			
						
			if(mPrefs.getString(DB_PREFS_KEY_TABLE_HASHCODE, null) == null || !mPrefs.getString(DB_PREFS_KEY_TABLE_HASHCODE, "").equals(hashcode)){
				mDb = mContext.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
				for(Class<?> clazz : classes){
					String sql = getSqlForGenerateTable(clazz);
					Console.debug(TAG, "sql: " + sql, Console.Liviu);				
					if(null != sql){
						try{
							mDb.execSQL(sql);
						}catch (SQLException e) {
							e.printStackTrace();
							Console.error(TAG, "Create table eror: SQL = " + sql, Console.Liviu);						
						}
					}
				}			
				closeDatabase();
				
				// save the Hashcode
				SharedPreferences.Editor ed = mPrefs.edit();
				ed.putString(DB_PREFS_KEY_TABLE_HASHCODE, hashcode);
				ed.commit();
			}else{
				// we do not have to re-create the tables again
				Console.debug(TAG, "we don't have to re-create the tables again", Console.Liviu);
			}			
			return true;		
		} catch (IllegalStateException e) {
			e.printStackTrace();
			Console.error(TAG, "sql exec error", Console.Liviu);
			closeDatabase();
			return false;
		}				
	}		
	
	private String getSqlForGenerateTable(Class<?> pClazz){
		if(null == pClazz){
			return null;
		}
		
		String sql = null;
		DbTable dbTable = (DbTable) pClazz.getAnnotation(DbTable.class);
		
		Annotation[] annotations = pClazz.getAnnotations();
		if(annotations.length == 0){
			Console.error(TAG, "no @DbTable annotation for " + pClazz, Console.Liviu);
			return null;
		}
		
		if(null != dbTable){
			if(DBConstants.TABLE_NO_NAME.equals(dbTable.name())){
				sql = "create table if not exists " + computeTableName(pClazz.getName()) + " (";
			}else{
				sql = "create table if not exists " + computeTableName(dbTable.name()) + " (";
			}
		}else{
			Console.error(TAG, "no @DbTable annotation specified for class " + pClazz , Console.Liviu);
		}
		
		// now the fields
		Field[] fields = pClazz.getDeclaredFields();
		
		for(int i = 0; i < fields.length; i++){
			String tempSql = getFieldSql(fields[i]);
			if(null != tempSql){
				sql += tempSql;
				if(i + 1 < fields.length){
					sql += ",";
				}
			}
		}				
		
		sql += ");";		
		Console.error(TAG, "table for " + pClazz, Console.Liviu);
		return sql;
	}		
	
	private String getFieldSql(Field pField){
		if(null == pField){
			return null;
		}
		String sql = null;
		int modifiers;
		
		pField.setAccessible(true);
		modifiers = pField.getModifiers();
		if(!Modifier.isFinal(modifiers)){
			try {													
				if(pField.getType().equals(String.class))
					sql = pField.getName() + " text";
				else if(pField.getType().equals(int.class) || pField.getType().equals(Integer.class))
					sql = pField.getName() + " integer";
				else if(pField.getType().equals(long.class) || pField.getType().equals(Long.class))
					sql = pField.getName() + " integer";
				else if(pField.getType().equals(Double.class) || pField.getType().equals(double.class))
					sql = pField.getName() + " double";
				else if(pField.getType().equals(Boolean.class) || pField.getType().equals(boolean.class))
					sql = pField.getName() + " integer"; // this should boolean: TODO look at this problem when you read from DB		
			}
			catch (IllegalArgumentException e) {
				e.printStackTrace();
			}					
		}		
				
		if(null == sql){
			return null;
		}
		
		// check for annotations
		DbField dbFieldAnn = pField.getAnnotation(DbField.class);
		if(null != dbFieldAnn){			
			if(dbFieldAnn.primaryKey()){
				sql += " PRIMARY KEY";
			}
			
			if(dbFieldAnn.autoincrement()){
				sql += " autoincrement";
			}		
			
			if(dbFieldAnn.unique()){
				sql += " unique";
			}
			
			// this should be the last statement
			if(!dbFieldAnn.canBeNull()){
				sql += " NOT NULL";
			}
			
			if(null != dbFieldAnn.defaultValue()){
				if(!DBConstants.DEFAULT_FIELD_NO_DEFAULT_VALUE.equals(dbFieldAnn.defaultValue())){
					if(DBConstants.DEFAULT_NOW.equals(dbFieldAnn.defaultValue())){
						sql += " default " + Utils.now();
					}else{
						sql += " default " + dbFieldAnn.defaultValue();
					}
				}
			}					
		}
		return sql;
	}
	
	public long put(Object pItem){
		if(null == pItem){
			return DBConstants.INVALID_ID;
		}
		
		Console.debug(TAG, "Add a new " + pItem.getClass().getName(), Console.Liviu);
		String tableName = computeTableName(pItem.getClass().getName());
		ContentValues values = new ContentValues();
		
		// now the fields
		Field[] fields = pItem.getClass().getDeclaredFields();
		Field f;
		
		for(int i = 0; i < fields.length; i++){			
			f = fields[i];
			f.setAccessible(true);
			DbField dbFieldAnn = f.getAnnotation(DbField.class);			
			if(null != dbFieldAnn){
				// we should ignore the fields which have the autoincrement detail.
				if(!dbFieldAnn.autoincrement()){
					try{
						if(f.getType().equals(String.class))				
							values.put(f.getName(), (String)f.get(pItem));
						else if(f.getType().equals(int.class) || f.getType().equals(Integer.class))
							values.put(f.getName(), f.getInt(pItem));
						else if(f.getType().equals(long.class) || f.getType().equals(Long.class))
							values.put(f.getName(), f.getLong(pItem));
						else if(f.getType().equals(Double.class) || f.getType().equals(double.class))
							values.put(f.getName(), f.getDouble(pItem));
						else if(f.getType().equals(Boolean.class) || f.getType().equals(boolean.class))
							values.put(f.getName(), f.getBoolean(pItem));
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}else{
				Console.debug(TAG, "the field " + f.getName() + " is not an entry in database", Console.Liviu);
			}			
		}	
		
		if(values.size() != 0){
			Console.debug(TAG, "values size: " + values.size(), Console.Liviu);
			openOrCreateDatabase();
			try{
				long pNewId = mDb.insertOrThrow(tableName, null, values);
				return pNewId;
			}catch (SQLException e) {
				e.printStackTrace();
			}
			closeDatabase();
		}else{
			Console.debug(TAG, "db:add-> No value was added in ContentValues", Console.Liviu);
		}
		
		return DBConstants.INVALID_ID;
	}
	
	private String computeTableName(String pObj){
		if(null == pObj){
			return null;
		}else{		
			return pObj.replace(".", "_");
		}
	}
	
	private synchronized boolean openOrCreateDatabase(){
		try{
			mDb = mContext.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);						
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			Console.error(TAG, "OpenOrCreateDatabase erorr", Console.Liviu);
			return false;
		} catch (IllegalStateException e) {
			e.printStackTrace();
			Console.error(TAG, "sql exec error", Console.Liviu);
			return false;
		}
	}
	
	public synchronized <T> Object query(T objectsKindClass, String[] projection, String selection, String groupBy, String having, String orderBy){
		ArrayList<T> resultsList = new ArrayList<T>();
		Class objectsKind = (Class)objectsKindClass;
		
		openOrCreateDatabase();
		Cursor c = null;
		try{
			c = mDb.query(computeTableName(objectsKind.getName()), projection, selection, null, groupBy, having, orderBy);
		}catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		if(null == c){
			closeDatabase();
			return resultsList;
		}
		
		if(c.getCount() == 0){
			c.close();
			closeDatabase();
			return resultsList;
		}
		
		int numRows = c.getCount();
		c.moveToFirst();
		try {
			Constructor constructor = objectsKind.getConstructor();
			T tempObj = null;
						
			for(int i = 0; i < numRows; i++){
				tempObj = (T)constructor.newInstance();
				for(int prjIndex = 0; prjIndex < projection.length; prjIndex++){
					Field f = objectsKind.getDeclaredField(projection[prjIndex]);
					f.setAccessible(true);
					if(f.getType().equals(String.class))				
						f.set(tempObj, c.getString(prjIndex));
					else if(f.getType().equals(int.class) || f.getType().equals(Integer.class))
						f.setInt(tempObj, c.getInt(prjIndex));
					else if(f.getType().equals(long.class) || f.getType().equals(Long.class))
						f.setLong(tempObj, c.getLong(prjIndex));
					else if(f.getType().equals(Double.class) || f.getType().equals(double.class))
						f.setDouble(tempObj, c.getDouble(prjIndex));
					else if(f.getType().equals(Boolean.class) || f.getType().equals(boolean.class))
						f.setBoolean(tempObj, c.getInt(prjIndex) == 1 ? Boolean.TRUE : Boolean.FALSE);
				}
				
				resultsList.add(tempObj);
				c.moveToNext();
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		
		c.close();
		closeDatabase();
		
		return resultsList;
	}
	
	/**
	 * close the database (just if it is open)
	 */
	private synchronized void closeDatabase(){
		if(mDb.isOpen())
			mDb.close();		
	}	
}

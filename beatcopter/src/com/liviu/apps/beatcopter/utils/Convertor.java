package com.liviu.apps.beatcopter.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.json.JSONException;
import org.json.JSONObject;

public class Convertor {
	
	// Constants
	private static final String TAG = "Convertor";
	
	public Convertor() {}
	
	/**
	 * Print object's members values to a readable format
	 * <b>Note:</b> Any array based object won't be displayed for the moment (FIXME)
	 * 
	 * @param obj 	object to print
	 * @return		String version of the passed object 
	 * 
	 */
	public static String toString(Object obj){			
		if(obj == null)
			return "this object is null";
		String thisClass = obj.getClass().getSimpleName();
		Class superClass = obj.getClass().getSuperclass();
		
		
		String data		= "\n======================= " + thisClass + " ===========================";		
		Class<?> c 		= obj.getClass();				
		
		Field[] baseFields = obj.getClass().getDeclaredFields();
		Field[] superFields = obj.getClass().getSuperclass().getDeclaredFields();
		Field[] fields = new Field[baseFields.length + superFields.length];
		int modifiers;		
		
		System.arraycopy(superFields, 0, fields, 0, superFields.length);
		System.arraycopy(baseFields, 0, fields, superFields.length, baseFields.length);							
		
		for(Field f : fields){
			f.setAccessible(true);
			modifiers = f.getModifiers();
			if(!Modifier.isFinal(modifiers)){
				try {													
					if(f.getType().equals(String.class))
						data += "\n" + f.getName() + ": " + ((String)f.get(obj));
					else if(f.getType().equals(int.class) || f.getType().equals(Integer.class))
						data += "\n" + f.getName() + ": " + f.getInt(obj);
					else if(f.getType().equals(long.class) || f.getType().equals(Long.class))
						data += "\n" + f.getName() + ": " + f.getLong(obj);
					else if(f.getType().equals(Double.class) || f.getType().equals(double.class))
						data += "\n" + f.getName() + ": " + f.getDouble(obj);
					else if(f.getType().equals(Boolean.class) || f.getType().equals(boolean.class))
						data += "\n" + f.getName() + ": " + f.getBoolean(obj);
					else data += "\n" + f.getName() + ":" + Convertor.toString(f.get(obj), 5);
				}
				catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
				catch (IllegalAccessException e) {
					e.printStackTrace();
				}		
			}
		}
		data += "\nsuperclass:\n   " + superClass.toString();
		data += "\n================================================================\n";
		return data;
	}	
	
	public static String toString(Object obj, int spaces){			
		if(obj == null)
			return "this object is null";
		String thisClass = obj.getClass().getSimpleName();
		Class superClass = obj.getClass().getSuperclass();
		
		int clsNameLength = thisClass.length();
		String eqSign = "----------------------";
		
		String data = "";
		String strSpaces = "";
		for(int i = 0; i < spaces; i++){
			strSpaces+= " ";
		}
		
		strSpaces += "|";		
		data = "\n" + strSpaces + eqSign + " " + thisClass + " " + eqSign;		
		Class<?> c 		= obj.getClass();				
		Field[] baseFields = obj.getClass().getDeclaredFields();
		Field[] superFields = obj.getClass().getSuperclass().getDeclaredFields();
		Field[] fields = new Field[baseFields.length + superFields.length];
		
		System.arraycopy(superFields, 0, fields, 0, superFields.length);
		System.arraycopy(baseFields, 0, fields, superFields.length, baseFields.length);			
		int modifiers;		
		
		for(Field f : fields){
			f.setAccessible(true);
			modifiers = f.getModifiers();
			if(!Modifier.isFinal(modifiers)){
				try {													
					if(f.getType().equals(String.class))
						data += "\n" + strSpaces + f.getName() + ": " + ((String)f.get(obj));
					else if(f.getType().equals(int.class) || f.getType().equals(Integer.class))
						data += "\n" + strSpaces + f.getName() + ": " + f.getInt(obj);
					else if(f.getType().equals(long.class) || f.getType().equals(Long.class))
						data += "\n" + strSpaces + f.getName() + ": " + f.getLong(obj);
					else if(f.getType().equals(Double.class) || f.getType().equals(double.class))
						data += "\n" + strSpaces + f.getName() + ": " + f.getDouble(obj);
					else if(f.getType().equals(Boolean.class) || f.getType().equals(boolean.class))
						data += "\n" + strSpaces + f.getName() + ": " + f.getBoolean(obj);
					else data += "\n" + strSpaces + f.getName() + ":" + Convertor.toString(f.get(obj), spaces + 5);
				}
				catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
				catch (IllegalAccessException e) {
					e.printStackTrace();
				}		
			}
		}
		data +=  "\n" + strSpaces + "superclass:\n" + strSpaces + "    " + superClass.toString();		
		data += "\n" + strSpaces + eqSign;
		for(int i = 0; i < clsNameLength + 2; i++){
			data += "-";
		}
		data += eqSign;
		return data;
	}	
	
	/**
	 * Convert any object to a JSON object
	 * <b>Note:</b> The keys of the new JSON object will be the name of
	 * original object.<br />
	 * 
	 * If the param <b>obj</b> have 2 fields <b>mName</b> and <b>mType</b> the json object 
	 * will look like:
	 * <pre>
	 * {@code}
	 * {
	 * 	"mName": "value",
	 * 	"mType": "value"
	 * }
	 * </pre>
	 * 
	 * <b>Note:</b> This method do not add any array based object to final JSON object.
	 * @param obj 	Object to convert			
	 * @return		A JSON object which have all members of <b>obj</b> params as key-value entries
	 */
	public static JSONObject toJson(Object obj){				
		Class<?> c 		= obj.getClass();				
		Field[] fields 	= c.getDeclaredFields();
		JSONObject json = new JSONObject();
		
		
		for(Field f : fields){
			f.setAccessible(true);
			try {													
				if(f.getType().equals(String.class))
					json.put(f.getName(), (String)f.get(obj));							
				else if(f.getType().equals(int.class) || f.getType().equals(Integer.class))
					json.put(f.getName(), f.getInt(obj));
				else if(f.getType().equals(long.class) || f.getType().equals(Long.class))
					json.put(f.getName(), f.getLong(obj));
				else if(f.getType().equals(Double.class) || f.getType().equals(double.class))
					json.put(f.getName(), f.getDouble(obj));
				else if(f.getType().equals(Boolean.class) || f.getType().equals(boolean.class))
					json.put(f.getName(), f.getBoolean(obj));
				else {
					Type type = f.getGenericType();
					if(type instanceof ParameterizedType){
						 ParameterizedType pt = (ParameterizedType) type;  
			             System.out.println("raw type: " + pt.getRawType());  
			             /*
			             if(pt.getRawType().equals(java.util.ArrayList.class) && pConvertArraylists == true){
			            	 Console.debug(TAG, "We have an arraylist");
			            	 for (Type t : pt.getActualTypeArguments()) {  
				                 Console.debug(TAG, "    " + t + " session: " + Session.class);  
				                 if(t.equals(Session.class)){
				                	 ArrayList<Session>list = (ArrayList<Session>)f.get(obj);
				                	 
				                	 if(list != null){
					                	 JSONArray jsonArray = new JSONArray();
					                	 for(int i = 0; i < list.size(); i++){
					                		 jsonArray.put(Convertor.toJson(list.get(i), false));
					                	 }
					                	 json.put(f.getName(), jsonArray);
				                	 }
				                	 else
				                		 Console.debug(TAG, "list is null in Converter.toJson");
					                		
				                 }
				             }			            	 
			             }	
			             */
					}
				}
			}
			catch (IllegalArgumentException e) {
				e.printStackTrace();
				json = null;
			}
			catch (IllegalAccessException e) {
				e.printStackTrace();
				json = null;
			} catch (JSONException e) {
				e.printStackTrace();
				json = null;
			}													
		}
		
		return json;
	}		

	public static JSONObject alertToJson(Object obj){						
		Class<?> c 		= obj.getClass();				
		Field[] fields 	= c.getDeclaredFields();
		JSONObject json = new JSONObject();
		
		Field[] fs = obj.getClass().getSuperclass().getDeclaredFields();
		try {
			for(Field f : fs){
				f.setAccessible(true);																
				if(f.getType().equals(String.class))		
					json.put(f.getName(), (String)f.get(obj));					
				else if(f.getType().equals(int.class) || f.getType().equals(Integer.class))
					json.put(f.getName(), f.getInt(obj));
				else if(f.getType().equals(long.class) || f.getType().equals(Long.class))
					json.put(f.getName(), f.getLong(obj));
				else if(f.getType().equals(Double.class) || f.getType().equals(double.class))
					json.put(f.getName(), f.getDouble(obj));
				else if(f.getType().equals(Boolean.class) || f.getType().equals(boolean.class))
					json.put(f.getName(), f.getBoolean(obj));				
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
				
		for(Field f : fields){
			f.setAccessible(true);
			try {													
				if(f.getType().equals(String.class))
					json.put(f.getName(), (String)f.get(obj));							
				else if(f.getType().equals(int.class) || f.getType().equals(Integer.class))
					json.put(f.getName(), f.getInt(obj));
				else if(f.getType().equals(long.class) || f.getType().equals(Long.class))
					json.put(f.getName(), f.getLong(obj));
				else if(f.getType().equals(Double.class) || f.getType().equals(double.class))
					json.put(f.getName(), f.getDouble(obj));
				else if(f.getType().equals(Boolean.class) || f.getType().equals(boolean.class))
					json.put(f.getName(), f.getBoolean(obj));
				else {
					Type type = f.getGenericType();
					if(type instanceof ParameterizedType){
						 ParameterizedType pt = (ParameterizedType) type;  
			             System.out.println("raw type: " + pt.getRawType());  			             
			             if(pt.getRawType().equals(java.util.ArrayList.class)){
			            	 Console.debug(TAG, "We have an arraylist", Console.Liviu);
			            	 
//			            	 for (Type t : pt.getActualTypeArguments()) {  
//				                 
//				             }			            	 
			             }				             
					}
				}
			}
			catch (IllegalArgumentException e) {
				e.printStackTrace();
				json = null;
			}
			catch (IllegalAccessException e) {
				e.printStackTrace();
				json = null;
			} catch (JSONException e) {
				e.printStackTrace();
				json = null;
			}													
		}
		
		return json;
	}			
}

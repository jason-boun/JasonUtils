package com.jason.jasonutils.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

/**
 * 这是一个简易的Json-HashMap转换工具，可以将普通的json数据（字符串）
 * 转换为一个HashMap<Srting, Object>表格，也可以反过来操作。此外还支
 * 持将json数据格式化。
 */
public class JsonUtils {

	/**
	 * 将指定的json数据转成HashMap对象 
	 * @param jsonStr
	 * @return
	 */
	public HashMap<String, Object> fromJson(String jsonStr) {
		try {
			if (jsonStr.startsWith("[")&& jsonStr.endsWith("]")) {
				jsonStr = "{\"fakelist\":" + jsonStr + "}";
			}

			JSONObject json = new JSONObject(jsonStr);
			return fromJson(json);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return new HashMap<String, Object>();
	}

	/**
	 * JSON字符串转换为HashMap
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	private HashMap<String, Object> fromJson(JSONObject json) throws JSONException {
		HashMap<String, Object> map = new HashMap<String, Object>();
		@SuppressWarnings("unchecked")
		Iterator<String> iKey = json.keys();
		while(iKey.hasNext()) {
			String key = iKey.next();
			Object value = json.opt(key);
			if (JSONObject.NULL.equals(value)) {
				value = null;
			}
			if (value != null) {
				if (value instanceof JSONObject) {
					value = fromJson((JSONObject)value);
				}
				else if (value instanceof JSONArray) {
					value = fromJson((JSONArray)value);
				}
				map.put(key, value);
			}
		}
		return map;
	}

	/**
	 * JSON字符串转换为ArrayList集合
	 * @param array
	 * @return
	 * @throws JSONException
	 */
	private ArrayList<Object> fromJson(JSONArray array) throws JSONException {
		ArrayList<Object> list = new ArrayList<Object>();
		for (int i = 0, size = array.length(); i < size; i++) {
			Object value = array.opt(i);
			if (value instanceof JSONObject) {
				value = fromJson((JSONObject)value);
			}
			else if (value instanceof JSONArray) {
				value = fromJson((JSONArray)value);
			}
			list.add(value);
		}
		return list;
	}

	/**
	 * 将指定的HashMap对象转成json字符串
	 * @param map
	 * @return
	 */
	public String fromHashMap(HashMap<String, Object> map) {
		try {
			return getJSONObject(map).toString();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return "";
	}

	
	/**
	 * 将指定的HashMap对象转成JSONObject对象
	 * @param map
	 * @return
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	private JSONObject getJSONObject(HashMap<String, Object> map) throws JSONException {
		JSONObject json = new JSONObject();
		for (Entry<String, Object> entry : map.entrySet()) {
			Object value = entry.getValue();
			if (value instanceof HashMap<?, ?>) {
				value = getJSONObject((HashMap<String, Object>)value);
			}
			else if (value instanceof ArrayList<?>) {
				value = getJSONArray((ArrayList<Object>)value);
			}
			json.put(entry.getKey(), value);
		}
		return json;
	}

	/**
	 * 将指定的 HashMap对象转成JSONArray对象
	 * @param list
	 * @return
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	private JSONArray getJSONArray(ArrayList<Object> list) throws JSONException {
		JSONArray array = new JSONArray();
		for (Object value : list) {
			if (value instanceof HashMap<?, ?>) {
				value = getJSONObject((HashMap<String, Object>)value);
			}
			else if (value instanceof ArrayList<?>) {
				value = getJSONArray((ArrayList<Object>)value);
			}
			array.put(value);
		}
		return array;
	}

	/**
	 * 格式化一个json串
	 * @param jsonStr
	 * @return
	 */
	public String format(String jsonStr) {
		try {
			return format("", fromJson(jsonStr));
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return "";
	}

	@SuppressWarnings("unchecked")
	private String format(String sepStr, HashMap<String, Object> map) {
		StringBuffer sb = new StringBuffer();
		sb.append("{\n");
		String mySepStr = sepStr + "\t";
		int i = 0;
		for (Entry<String, Object> entry : map.entrySet()) {
			if (i > 0) {
				sb.append(",\n");
			}
			sb.append(mySepStr).append('\"').append(entry.getKey()).append("\":");
			Object value = entry.getValue();
			if (value instanceof HashMap<?, ?>) {
				sb.append(format(mySepStr, (HashMap<String, Object>)value));
			}
			else if (value instanceof ArrayList<?>) {
				sb.append(format(mySepStr, (ArrayList<Object>)value));
			}
			else if (value instanceof String) {
				sb.append('\"').append(value).append('\"');
			}
			else {
				sb.append(value);
			}
			i++;
		}
		sb.append('\n').append(sepStr).append('}');
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	private String format(String sepStr, ArrayList<Object> list) {
		StringBuffer sb = new StringBuffer();
		sb.append("[\n");
		String mySepStr = sepStr + "\t";
		int i = 0;
		for (Object value : list) {
			if (i > 0) {
				sb.append(",\n");
			}
			sb.append(mySepStr);
			if (value instanceof HashMap<?, ?>) {
				sb.append(format(mySepStr, (HashMap<String, Object>)value));
			}
			else if (value instanceof ArrayList<?>) {
				sb.append(format(mySepStr, (ArrayList<Object>)value));
			}
			else if (value instanceof String) {
				sb.append('\"').append(value).append('\"');
			}
			else {
				sb.append(value);
			}
			i++;
		}
		sb.append('\n').append(sepStr).append(']');
		return sb.toString();
	}
	
	/**
	 * 将HashMap对应的字符串重新封装到HashMap中
	 * @param mapStr
	 * @param personInfo
	 * @return
	 */
	public static HashMap<String, String> str2HashMap(String mapStr){
		System.out.println("---mapStr--:"+mapStr);
		if(TextUtils.isEmpty(mapStr)) return new HashMap(); 
		mapStr = mapStr.trim();
		if(mapStr.startsWith("{") && mapStr.endsWith("}")){
			HashMap<String, String> hashMap = new HashMap<String, String>();
			mapStr = mapStr.substring(1, mapStr.length()-1).trim();//去除大括号
			String[] infoArray = mapStr.split(",");//拆分元素
			if(infoArray.length>0){
				for(int i=0;i<infoArray.length;i++){
					String[] split = infoArray[i].trim().split("=");//拆分键值对
					if(split.length==2){
						hashMap.put(split[0], split[1]);
					}
				}
			}
			return hashMap;
		}
		return new HashMap();
	}
	
	/**
	 * 将HashMap格式的字符串转化为JSONObject对象
	 * @param personInfo
	 * @return
	 * @throws JSONException 
	 */
	public JSONObject getJSONObject(Map<String, String> personInfo) throws JSONException {
		if(personInfo.size()>0){
			JSONObject jsonObj = new JSONObject();
			Set<String> keySet = personInfo.keySet();
			for(String key :keySet){
				try {
					jsonObj.put(key, personInfo.get(key));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return jsonObj;
		}
		return null;
	}
	
	/**
	 * 将字符串转化为JSONObject对象
	 * @param jsonString
	 * @return
	 */
	public JSONObject getJSONObject(String jsonStr){
		try {
			return new JSONObject(jsonStr);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 将List字符串转换成List集合
	 * @param listStr
	 * @return
	 */
	public static List<String> string2List(String listStr){
		List<String> strList = new ArrayList<String>();
		if(listStr.contains("[")&&listStr.contains("]")){
			listStr = listStr.substring(1,listStr.indexOf("]"));
			String[] strs = listStr.split(",");
			for(String s:strs){
				strList.add(s.trim());
			}
		}
		return strList; 
	}
	
	/**
	 * 将Set字符串转换成Set集合
	 * @param setStr
	 * @return
	 */
	public static Set<String> string2Set(String setStr){
		if(!TextUtils.isEmpty(setStr)){
			Set<String> strSet = new TreeSet<String>();
			if(setStr.contains("[")&& setStr.contains("]")){
				setStr = setStr.substring(1,setStr.indexOf("]"));
				String[] strs = setStr.split(",");
				for(String s:strs){
					strSet.add(s.trim());
				}
			}
			return strSet; 
		}
		return null;
	}
	
	/**
	 * set字符串中，每个元素都是map字符串
	 * @param setStr
	 * @return
	 */
	public static Set<String> strMap2Set(String setStr){
		if(!TextUtils.isEmpty(setStr)&& setStr.contains("{") && setStr.contains("}")){
			System.out.println("----setStr original--:"+setStr);
			Set<String> strSet = new TreeSet<String>();
			if(setStr.contains("[")&& setStr.contains("]")){
				setStr = setStr.substring(1,setStr.indexOf("]"));
				System.out.println("----setStr--:"+setStr);
				setStr = setStr.replace("},", "}&&&");
				String[] strs = setStr.split("&&&");
				for(String s:strs){
					System.out.println("---s--:"+s);
					strSet.add(s.trim());
				}
			}
			System.out.println("---strSet final --:"+strSet);
			return strSet; 
		}
		return null;
	}
	
}

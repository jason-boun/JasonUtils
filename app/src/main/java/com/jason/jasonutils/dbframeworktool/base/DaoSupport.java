package com.jason.jasonutils.dbframeworktool.base;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jason.jasonutils.dbframeworktool.annotation.Coloumn;
import com.jason.jasonutils.dbframeworktool.annotation.ID;
import com.jason.jasonutils.dbframeworktool.annotation.TableName;
import com.jason.jasonutils.dbframeworktool.dao.DBHelper;

public abstract class DaoSupport<M> implements DAO<M> {

	//private static final String TAG = "DaoSupport";
	// 问题一：表名获取
	// 问题二：将数据库中列的信息设置到实体的对应属性当中
	// 问题三：实体中数据导入到ContentValues
	// 问题四：主键信息获取
	// 问题五：实体Bean对象的创建

	protected DBHelper helper;
	protected SQLiteDatabase db;
	protected Context context;

	public DaoSupport(Context context) {
		this.context = context;
		helper = new DBHelper(context);
		db = helper.getWritableDatabase();
	}

	@Override
	public int delete(Serializable id) {
		return db.delete(getTableName(), DBConstants.TABLE_ID + " =? ", new String[] { id.toString() });
	}

	@Override
	public List<M> findAll() {
		List<M> result = new ArrayList<M>();
		Cursor query = db.query(getTableName(), null, null, null, null, null, null);

		while (query.moveToNext()) {
			M m = getInstance();
			fillField(query, m);
			result.add(m);
		}
		query.close();
		return result;
	}

	public List<M> findByCondition(String[] columns, String selection, String[] selectionArgs, String limit) {
		return findByCondition(columns, selection, selectionArgs, null, null, null, limit);
	}

	public List<M> findByCondition(String[] columns, String selection, String[] selectionArgs, String groupBy, String having,
			String orderBy, String limit) {
		List<M> result = new ArrayList<M>();
		Cursor query = db.query(getTableName(), columns, selection, selectionArgs, groupBy, having, orderBy, limit);

		while (query.moveToNext()) {
			M m = getInstance();

			// int index = query.getColumnIndex(DBHelper.TABLE_NEWS_TITLE);
			// String title = query.getString(index);
			// news.setTitle(title);
			fillField(query, m);

			// 省略N行代码

			result.add(m);
		}
		query.close();

		return result;
	}

	@Override
	public long insert(M m) {
		ContentValues values = new ContentValues();
		fillContentValues(m, values);
		return db.insert(getTableName(), null, values);
	}

	@Override
	public int update(M m) {
		ContentValues values = new ContentValues();
		fillContentValues(m, values);
		return db.update(getTableName(), values, DBConstants.TABLE_ID + " =? ", new String[] { getId(m) });
	}

	/**
	 * 问题一：表名获取
	 * @return 表名
	 */
	private String getTableName() {
		M m = getInstance();
		TableName annotation = m.getClass().getAnnotation(TableName.class);
		if (annotation != null) {
			return annotation.value();
		}
		return "";
	}

	/**
	 * 问题二：将数据库中列的信息设置到实体的对应属性当中
	 */
	private void fillField(Cursor cursor, M m) {
		// 实体中的属性与表中列的对应关系
		Field[] fields = m.getClass().getDeclaredFields();
		for (Field item : fields) {
			item.setAccessible(true);

			Coloumn annotation = item.getAnnotation(Coloumn.class);
			if (annotation != null) {
				String key = annotation.value();
				int index = cursor.getColumnIndex(key);
				String value = cursor.getString(index);

				try {
					Class<?> type = item.getType();
					if (type == long.class) {
						item.set(m, Long.parseLong(value));
					} else {
						item.set(m, value);
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 问题三：实体中数据导入到ContentValues
	 */
	private void fillContentValues(M m, ContentValues values) {
		Field[] fields = m.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);

			Coloumn annotation = field.getAnnotation(Coloumn.class);
			if (annotation != null) {
				String key = annotation.value();
				try {
					String value = field.get(m).toString();
					ID id = field.getAnnotation(ID.class);//判断该列值是不是主键，如果是主键，并且是自增则不能添加
					if(id==null || !id.autoincrement()){
						values.put(key, value);
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 问题四：主键信息获取
	 */
	private String getId(M m) {
		Field[] fields = m.getClass().getDeclaredFields();
		for (Field item : fields) {
			item.setAccessible(true);
			ID annotation = item.getAnnotation(ID.class);
			if (annotation != null) {
				try {
					return item.get(m).toString();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 问题五：实体Bean对象的创建
	 */
	@SuppressWarnings("unchecked")
	public M getInstance() {
		Class<?> clazz = getClass();// ①获取运行时子类对应的字节码
		Type genericSuperclass = clazz.getGenericSuperclass();// ②获取该字节码对应的支持泛型的父类
		Type type = ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];// ③获取父类的参数信息
		Class<?> m = (Class<?>) type;// ④所需参数字节码
		try {
			return (M) m.newInstance();//JavaBean特性，通过字节码构造实例，并返回
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return null;
	}

}

package com.jason.jasonutils.dbframeworktool.base;

import java.io.Serializable;
import java.util.List;

/**
 * 数据库操作，使用说明：
 * ①创建表
 * ②实体
 * ③设置实体对应的表名
 * ④设置实体中属性对应的表中列的信息(注意：主键信息的配置)
 * ⑤写一个操作实体的接口，DAO的子类
 * ⑥写一个实现了该接口的类，作为DaoSupport的子类 
 * @param <M> 
 */
public interface DAO<M> {
	long insert(M m);
	int delete(Serializable id);// String、int----Object JPA
	int update(M m);
	List<M> findAll();

	public List<M> findByCondition(String[] columns, String selection, String[] selectionArgs, String limit);

}

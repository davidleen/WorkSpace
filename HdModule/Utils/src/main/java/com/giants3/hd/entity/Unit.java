package com.giants3.hd.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 单位 数据  单位 表
 */

@Entity(name="T_Unit")
public class Unit implements Serializable{

	/**
	 * 单位 id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long id;
	/**
	 * 单位 名称
	 */
	@Basic
	public String name;


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
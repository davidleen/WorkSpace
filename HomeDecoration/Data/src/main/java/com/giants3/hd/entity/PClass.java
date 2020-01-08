package com.giants3.hd.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 产品类别比表
 * 
 * 产品类别细分
 */

@Entity(name="T_PClass")
public class PClass  implements Serializable {


	/**
	 * 类别id
	 */
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	public long id			;
	/**
	 * 类别名称
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


	@Override
	public String toString() {
		return name;
	}
}
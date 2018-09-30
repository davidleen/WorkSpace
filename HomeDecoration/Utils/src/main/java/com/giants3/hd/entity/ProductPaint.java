package com.giants3.hd.entity;

import com.giants3.hd.logic.ProductAnalytics;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.interf.Valuable;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 产品油漆信息
 * 包括 材料工序
 */


@Entity(name="T_ProductPaint")
@Table(
		indexes = {@Index(name = "materialIdIndex",  columnList="materialId", unique = false),
				@Index(name = "productIdIndex", columnList="productId",     unique = false)
				,@Index(name = "flowIdIndex", columnList="flowId",     unique = false)}
)

public class ProductPaint  implements Serializable,Summariable,Valuable {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	public long id;
	public long productId;


	@Basic
	public int itemIndex;
	/**
	 * 工序id
	 */
	@Basic
	private long processId;
	/**
	 * 物料id
	 */
	@Basic
	public long materialId;
	/**
	 * 产品名称
	 */
	@Basic
	private String productName;

	/**
	 * 流程id
	 */
	@Basic
	public long flowId;
	@Basic
	public  String memo;




	public long getFlowId() {
		return flowId;
	}

	public void setFlowId(long flowId) {
		this.flowId = flowId;
	}

	public String getMaterialCode() {
		return materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}

	/**
	 * 物料编码
	 */
	@Basic
	public String materialCode;
	/**
	 * 物料名称
	 */
	@Basic
	public String materialName;


	/**
	 * 物料单位
	 */
	@Basic
	public String unitName;




	/**
	 * 工序编码
	 */
	@Basic
	public String processCode;

	/**
	 * 工序名称
	 */
	@Basic
	public String processName;


	/**
	 * 混合后的材料单价
	 */
	@Basic
	public  float price;
	/**
	 * 原材料单价
	 */
	@Basic
	public float materialPrice;
	/**
	 * 工序单价
	 */
	@Basic
	public float processPrice;
	/**
	 * 配料比例  值【0，】  配料都是稀释剂
	 */
	@Basic
	public float ingredientRatio;


	/**
	 * 混合材料的用量
	 */
	@Basic
	public float quantity;
	/**
	 * 原材料用量  
	 */
	@Basic
	public float materialQuantity;
	/**
	 * 材料费用=（materialPrice*materialQuantity）
	 */
	@Basic
	public float cost;

	/**
	 * 配料（稀释剂）的用量   =材料用量×（配料比例/（1+配料比例））
	 * 
	 * =materialQuantity*(ingredientRatio/(1+ingredientRatio))
	 */
	@Basic
	public float ingredientQuantity;


	/**
	 * 材料分类类型
	 */
	@Basic
	public int materialType;




//	/**
//	 * 副单位
//	 */
//	public String unit2;
//	/**
//	 * 副单价
//	 */
//	public float price2;




	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public long getProcessId() {
		return processId;
	}

	public void setProcessId(long processId) {
		this.processId = processId;
	}

	public long getMaterialId() {
		return materialId;
	}

	public void setMaterialId(long materialId) {
		this.materialId = materialId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}



	public float getProcessPrice() {
		return processPrice;
	}

	public void setProcessPrice(float processPrice) {
		this.processPrice = processPrice;
	}

	public float getIngredientRatio() {
		return ingredientRatio;
	}








	public String getProcessCode() {
		return processCode;
	}

	public void setProcessCode(String processCode) {
		this.processCode = processCode;
	}


	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}


	@Override
	public int getType() {
		return materialType;
	}

	@Override
	public float getAmount() {
		return cost;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getMemo() {
		return memo;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ProductPaint)) return false;

		ProductPaint that = (ProductPaint) o;

		if (id != that.id) return false;
		if (productId != that.productId) return false;
		if (processId != that.processId) return false;
		if (materialId != that.materialId) return false;
		if (flowId != that.flowId) return false;
		if (Float.compare(that.price, price) != 0) return false;
		if (Float.compare(that.materialPrice, materialPrice) != 0) return false;
		if (Float.compare(that.processPrice, processPrice) != 0) return false;
		if (Float.compare(that.ingredientRatio, ingredientRatio) != 0) return false;
		if (Float.compare(that.quantity, quantity) != 0) return false;
		if (Float.compare(that.itemIndex, itemIndex) != 0) return false;
		if (Float.compare(that.materialQuantity, materialQuantity) != 0) return false;
		if (Float.compare(that.cost, cost) != 0) return false;
		if (Float.compare(that.ingredientQuantity, ingredientQuantity) != 0) return false;
		if (materialType != that.materialType) return false;
		if (productName != null ? !productName.equals(that.productName) : that.productName != null) return false;
		if (memo != null ? !memo.equals(that.memo) : that.memo != null) return false;
		if (materialCode != null ? !materialCode.equals(that.materialCode) : that.materialCode != null) return false;
		if (materialName != null ? !materialName.equals(that.materialName) : that.materialName != null) return false;
		if (unitName != null ? !unitName.equals(that.unitName) : that.unitName != null) return false;
		if (processCode != null ? !processCode.equals(that.processCode) : that.processCode != null) return false;
		return !(processName != null ? !processName.equals(that.processName) : that.processName != null);

	}

	@Override
	public int hashCode() {
		int result = (int) (id ^ (id >>> 32));
		result = 31 * result + (int) (productId ^ (productId >>> 32));
		result = 31 * result + (int) (processId ^ (processId >>> 32));
		result = 31 * result + (int) (materialId ^ (materialId >>> 32));
		result = 31 * result + (productName != null ? productName.hashCode() : 0);
		result = 31 * result + (int) (flowId ^ (flowId >>> 32));
		result = 31 * result + (memo != null ? memo.hashCode() : 0);
		result = 31 * result + (materialCode != null ? materialCode.hashCode() : 0);
		result = 31 * result + (materialName != null ? materialName.hashCode() : 0);
		result = 31 * result + (unitName != null ? unitName.hashCode() : 0);
		result = 31 * result + (processCode != null ? processCode.hashCode() : 0);
		result = 31 * result + (processName != null ? processName.hashCode() : 0);
		result = 31 * result + (price != +0.0f ? Float.floatToIntBits(price) : 0);
		result = 31 * result + (materialPrice != +0.0f ? Float.floatToIntBits(materialPrice) : 0);
		result = 31 * result + (processPrice != +0.0f ? Float.floatToIntBits(processPrice) : 0);
		result = 31 * result + (ingredientRatio != +0.0f ? Float.floatToIntBits(ingredientRatio) : 0);
		result = 31 * result + (quantity != +0.0f ? Float.floatToIntBits(quantity) : 0);
		result = 31 * result +itemIndex;
		result = 31 * result + (materialQuantity != +0.0f ? Float.floatToIntBits(materialQuantity) : 0);
		result = 31 * result + (cost != +0.0f ? Float.floatToIntBits(cost) : 0);
		result = 31 * result + (ingredientQuantity != +0.0f ? Float.floatToIntBits(ingredientQuantity) : 0);
		result = 31 * result + materialType;
		return result;
	}

	@Override
	public boolean isEmpty() {

	return 	StringUtils.isEmpty(processName)&&StringUtils.isEmpty(processCode)&&materialId<=0;

	}




}
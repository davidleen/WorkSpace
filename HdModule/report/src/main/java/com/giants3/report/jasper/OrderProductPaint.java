package com.giants3.report.jasper;

import com.giants3.hd.entity.ProductPaint;


/**订单产品输出报表
 * Created by david on 2016/3/27.
 */
public class OrderProductPaint  {


    /**
     * 物料编码
     */

    public String materialCode;
    /**
     * 物料名称
     */

    public String materialName;


    /**
     * 物料单位
     */

    public String unitName;


    /**
     * 工序编码
     */

    public String processCode;

    /**
     * 工序名称
     */

    public String processName;


    /**
     * 混合后的材料单价
     */

    public float price;
    /**
     * 原材料单价
     */

    public float materialPrice;
    /**
     * 工序单价
     */

    public float processPrice;
    /**
     * 配料比例  值【0，】  配料都是稀释剂
     */

    public float ingredientRatio;


    /**
     * 混合材料的用量
     */

    public float quantity;
    /**
     * 原材料用量  
     */

    public float materialQuantity;
    /**
     * 材料费用=（materialPrice*materialQuantity）
     */

    public float cost;

    /**
     * 配料（稀释剂）的用量   =材料用量×（配料比例/（1+配料比例））
     * <p/>
     * =materialQuantity*(ingredientRatio/(1+ingredientRatio))
     */

    public float ingredientQuantity;


    /**
     * 总材料用量
     */
    public float totalQuantity;

    public float totalSalary;


    public float totalMaterialCost;

    public float totalIngredientQuantity;


    public void setProductPaint(ProductPaint productPaint)
    {

        materialCode=productPaint.materialCode;
        materialName=productPaint.materialName;
        unitName=productPaint.unitName;
        processCode=productPaint.processCode;
        processName=productPaint.processName;
        price=productPaint.price;
        materialPrice=productPaint.materialPrice;
        processPrice=productPaint.processPrice;
        ingredientRatio=productPaint.ingredientRatio;
        quantity=productPaint.quantity;
        materialQuantity=productPaint.materialQuantity;
        cost=productPaint.cost;
        ingredientQuantity=productPaint.ingredientQuantity;



    }


    /**
     * 设置订单数据， 同时计算总用量
     * @param qty
     */
    public void setQty(int qty)
    {

    }


    public String getMaterialCode() {
        return materialCode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public String getUnitName() {
        return unitName;
    }

    public String getProcessCode() {
        return processCode;
    }

    public String getProcessName() {
        return processName;
    }

    public float getPrice() {
        return price;
    }

    public float getMaterialPrice() {
        return materialPrice;
    }

    public float getProcessPrice() {
        return processPrice;
    }

    public float getIngredientRatio() {
        return ingredientRatio;
    }

    public float getQuantity() {
        return quantity;
    }

    public float getMaterialQuantity() {
        return materialQuantity;
    }

    public float getCost() {
        return cost;
    }

    public float getIngredientQuantity() {
        return ingredientQuantity;
    }

    public float getTotalQuantity() {
        return totalQuantity;
    }

    public float getTotalSalary() {
        return totalSalary;
    }

    public float getTotalMaterialCost() {
        return totalMaterialCost;
    }

    public float getTotalIngredientQuantity() {
        return totalIngredientQuantity;
    }




}

package com.giants3.hd.noEntity;

import com.giants3.hd.entity.Product;
import com.giants3.hd.utils.StringUtils;

/**产品信息基本展示帮助类
 * Created by davidleen29 on 2018/4/14.
 */
public class ProductAgent {

	/**
	 * 获取产品的包装全字符串
	 * @param product
	 * @return
     */
    public static String getProductFullPackageInfo(Product product)
    {
       return product.insideBoxQuantity+"/"+product.packQuantity+"/"+product.packLong+"*"+product.packWidth+"*"+product.packHeight;

    }

	/**
	 * 获取产品全名    当有pversion时候  用-连接
	 * @return
	 * @param product
     */
	public static String getFullName(Product product)
	{
		return product.name + (StringUtils.isEmpty(product.pVersion)?"":("-"+ product.pVersion));
	}
}

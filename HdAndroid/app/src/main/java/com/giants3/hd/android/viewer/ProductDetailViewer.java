package com.giants3.hd.android.viewer;

import com.giants3.hd.android.mvp.NewViewer;
import com.giants3.hd.android.presenter.ProductDetailPresenter;
import com.giants3.hd.entity.Pack;
import com.giants3.hd.entity.Product;
import com.giants3.hd.noEntity.ProductDetail;

import java.util.List;

/**
 * Created by david on 2016/4/12.
 */
public interface ProductDetailViewer extends NewViewer {


      void bindData(ProductDetail productDetail);



      void showConceptusMaterial(ProductDetail productDetail);

      void showConceptusWage(ProductDetail productDetail);

      void showAssembleMaterial(ProductDetail productDetail);

      void showAssembleWage(ProductDetail productDetail);

      void showPaintMaterialWage(ProductDetail productDetail);

      void showPackMaterial(ProductDetail productDetail);

      void showPackWage(ProductDetail productDetail);

      void showFieldValueEditDailog(String title, String field, String oldValue);

      void showFieldValueEditDailog(String title, String field, String oldValue,Class valueType);


        <T> void showPickDialog(String title , final String field, List<T> items, T preItem);

      void showPackRelateEditDialog(Product product);
}

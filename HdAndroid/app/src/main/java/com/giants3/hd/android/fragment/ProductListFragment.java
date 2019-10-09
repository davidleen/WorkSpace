package com.giants3.hd.android.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.giants3.hd.android.activity.ProductDetailActivity;
import com.giants3.android.adapter.AbstractAdapter;
import com.giants3.hd.android.adapter.ProductListAdapter;
import com.giants3.hd.android.entity.ProductDetailSingleton;
import com.giants3.hd.android.helper.AuthorityUtil;
import com.giants3.hd.android.helper.SharedPreferencesHelper;
import com.giants3.hd.android.presenter.ProductDetailPresenter;
import com.giants3.hd.data.interractor.UseCase;
import com.giants3.hd.data.interractor.UseCaseFactory;
import com.giants3.hd.data.utils.GsonUtils;
import com.giants3.hd.entity.app.AProduct;
import com.giants3.hd.noEntity.ProductDetail;
import com.giants3.hd.parser.ProductParser;
import com.giants3.hd.utils.ObjectUtils;
import com.google.zxing.client.result.ProductResultParser;

import java.util.List;

import anetwork.channel.cache.CacheManager;

import static com.giants3.hd.android.presenter.ProductDetailPresenter.REQUEST_PRODUCT_DETAIL_EDIT;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductListFragment extends ListFragment<AProduct> {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and parse types of parameters
    private String mParam1;
    private String mParam2;




    public ProductListFragment() {
        // Required empty public constructor
        setFragmentListener(new OnProductFragmentInteractionListener() {
            @Override
            public void onFragmentInteraction(AProduct data) {
                //调整act
                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                intent.putExtra(ProductDetailPresenter.ARG_ITEM, GsonUtils.toJson(data));
                startActivity(intent);
            }
        });
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductListFragment.
     */
    // TODO: Rename and parse types and number of parameters
    public static ProductListFragment newInstance(String param1, String param2) {
        ProductListFragment fragment = new ProductListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        adapter =new ProductListAdapter(getActivity());
    }

    @Override
    protected AbstractAdapter<AProduct> getAdapter() {
        return adapter;
    }

    @Override
    protected UseCase getUserCase(String key, int pageIndex, int pageSize) {
        return  UseCaseFactory.getInstance().createProductListCase(key,pageIndex,pageSize);
    }








    public  interface OnProductFragmentInteractionListener  extends OnFragmentInteractionListener<AProduct>
    {

    }


    @Override
    protected boolean supportAdd() {


        return AuthorityUtil.getInstance().editProduct() ;
    }


    @Override
    protected void addNewOne() {


        List<ProductDetail> demos = SharedPreferencesHelper.getInitData().demos;
        ProductDetail detail;
        if( demos !=null&& demos.size()>0)
        {

                  detail=demos.get(0);
                detail=  (ProductDetail) ObjectUtils.deepCopy(detail);
                detail.product.name="";


//            }else
//            {



//                ProductTemplateDialog dialog=    new ProductTemplateDialog(getWindow()) ;
//
//                dialog.setVisible(true);
//                ProductDetail detail=dialog.getResult();
//                if(detail!=null)
//                {
//                    detail=  (ProductDetail) ObjectUtils.deepCopy(detail);
//                    detail.product.name="";
//                    HdSwingUtils.showDetailPanel(SwingUtilities.getWindowAncestor(getRoot()),detail );
//                }

//            }



        }else
        {
            detail=new ProductDetail();

        }
        ProductDetailSingleton.getInstance().setProductDetail(detail);
        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
        intent.putExtra(ProductDetailPresenter.EXTRA_EDITABLE, true);

      startActivityForResult(intent,REQUEST_PRODUCT_DETAIL_EDIT);








    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK)
        {
            switch (requestCode)
            {
                case REQUEST_PRODUCT_DETAIL_EDIT:

                    ProductDetail productDetail=ProductDetailSingleton.getInstance().getProductDetail();
                    AProduct aProduct=new ProductParser().parse(productDetail.product);
                    getAdapter().getDatas().add(0,aProduct);
                    getAdapter().notifyDataSetChanged();

                    break;
            }
        }
    }
}

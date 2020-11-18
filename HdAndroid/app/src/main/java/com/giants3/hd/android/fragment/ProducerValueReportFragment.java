package com.giants3.hd.android.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.fragment.app.Fragment;

import com.giants3.android.adapter.AbstractAdapter;
import com.giants3.hd.android.R;
import com.giants3.hd.android.activity.FragmentWrapActivity;
import com.giants3.hd.android.adapter.ProducerValueReportAdapter;
import com.giants3.hd.data.interractor.UseCase;
import com.giants3.hd.data.interractor.UseCaseFactory;
import com.giants3.hd.data.net.HttpUrl;
import com.giants3.hd.entity.app.AProduct;
import com.giants3.hd.noEntity.ProducerValueReport;
import com.giants3.hd.utils.ArrayUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProducerValueReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProducerValueReportFragment extends ListFragment<ProducerValueReport> {


    private List<ProducerValueReport> datas;

    public ProducerValueReportFragment() {
//        // Required empty public constructor
//        setFragmentListener(new OnProductFragmentInteractionListener() {
//            @Override
//            public void onFragmentInteraction(AProduct data) {
//                //调整act
//                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
//                intent.putExtra(ProductDetailPresenter.ARG_ITEM, GsonUtils.toJson(data));
//                startActivityForResult(intent, REQUEST_PRODUCT_DETAIL_EDIT);
//            }
//        });
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductListFragment.
     */

    public static ProducerValueReportFragment newInstance(String param1, String param2) {
        ProducerValueReportFragment fragment = new ProducerValueReportFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hide_no_value.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                notifyAdapterDataChanged();

            }
        });

        sort_value_desc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                notifyAdapterDataChanged();

            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    @Override
    protected AbstractAdapter<ProducerValueReport> getAdapter() {
        return  new ProducerValueReportAdapter(getActivity());
    }

    @Override
    protected UseCase getUserCase(String key, int pageIndex, int pageSize) {
        return UseCaseFactory.getInstance().createGetUseCase(HttpUrl.getProducerValueReport(key), ProducerValueReport.class);
    }


    public interface OnProductFragmentInteractionListener extends OnFragmentInteractionListener<AProduct> {

    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {


            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onListItemClick(ProducerValueReport item) {

        Bundle bundle=new Bundle();
        bundle.putString(ProducerValueListFragment.PARA_JGH,item.dept);
        bundle.putString(ProducerValueListFragment.PARAM_NAME,item.name);

        FragmentWrapActivity.start(this,ProducerValueListFragment.class,bundle,0);



    }

    @BindView(R.id.hide_no_value)
    public CheckBox hide_no_value;
    @BindView(R.id.sort_value_desc)
    public CheckBox sort_value_desc;
    @Override
    protected void configCustomContainer(ViewGroup container) {


        View inflate = View.inflate(container.getContext(), R.layout.part_producer_report, null);
        container.addView(inflate);



    }




    @Override
    public List<ProducerValueReport> filterData(List<ProducerValueReport> datas) {




        ArrayList<ProducerValueReport> producerValueReports = new ArrayList<>();

        if(hide_no_value.isChecked())
        {

            for (ProducerValueReport report:datas)
            {
                if(report.workingValue>0)
                {
                    producerValueReports.add(report);
                }
            }


        }else
        {
            producerValueReports.addAll(datas);
        }


        if(sort_value_desc.isChecked())
        {
            ArrayUtils.sortList(producerValueReports, new Comparator<ProducerValueReport>() {
                @Override
                public int compare(ProducerValueReport o1, ProducerValueReport o2) {
                    return Double.compare(o2.workingValue,o1.workingValue);
                }
            });



        }

        return producerValueReports;

    }
}

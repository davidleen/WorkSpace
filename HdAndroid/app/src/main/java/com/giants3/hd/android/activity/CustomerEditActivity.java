package com.giants3.hd.android.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;



import com.giants3.android.frame.util.BitmapHelper;
import com.giants3.hd.android.R;
import com.giants3.hd.android.helper.AndroidUtils;
import com.giants3.hd.android.helper.CapturePictureHelper;
import com.giants3.hd.android.helper.ImageLoaderFactory;
import com.giants3.hd.android.helper.ImageViewerHelper;
import com.giants3.hd.android.mvp.AndroidRouter;
import com.giants3.hd.android.mvp.customer.CustomerEditMVP;
import com.giants3.hd.android.mvp.customer.PresenterImpl;
import com.giants3.hd.data.net.PictureUrl;
import com.giants3.hd.entity.Customer;
import com.giants3.hd.noEntity.NameCard;
import com.giants3.hd.utils.StringUtils;

import java.io.File;
import java.util.Calendar;

import butterknife.Bind;

//import com.giants3.android.namecardscan.activity.*;
//import com.giants3.android.namecardscan.activity.*;
//import cn.sharp.android.ncr.ocr.OCRItems;

public class CustomerEditActivity extends BaseHeadViewerActivity<CustomerEditMVP.Presenter> implements CustomerEditMVP.Viewer {


    private static final String KEY_CUSTOMER = "KEY_CUSTOMER";
    @Bind(R.id.save)
    TextView save;
    @Bind(R.id.namecard)
    TextView namecard;
    @Bind(R.id.nation)
    TextView nation;
    @Bind(R.id.address)
    TextView address;
    @Bind(R.id.email)
    TextView email;
    @Bind(R.id.fax)
    TextView fax;
    @Bind(R.id.tel)
    TextView tel;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.code)
    TextView code;
    @Bind(R.id.company)
    TextView company;
    @Bind(R.id.img_namecard)
    ImageView img_namecard;
    @Bind(R.id.lastRequestFailImg)
    ImageView lastRequestFailImg;
    @Bind(R.id.retry)
    View retry;

    @Bind(R.id.last)
    View last;

    @Bind(R.id.delete)
    View delete;


    CapturePictureHelper capturePictureHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String codeText = code.getText().toString().trim();
                String nameText = name.getText().toString().trim();
                String telText = tel.getText().toString().trim();
                String faxText = fax.getText().toString().trim();
                String addressText = address.getText().toString().trim();
                String nationText = nation.getText().toString().trim();
                String emailText = email.getText().toString().trim();

                getPresenter().updateValue(codeText, nameText, telText, faxText, emailText, addressText, nationText);
                getPresenter().save();
            }
        });
        code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessage("客户编号不允许修改");
            }
        });
        namecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                capturePictureHelper.pickFromCamera(false);

            }
        });

        namecard.setVisibility( View.VISIBLE );
        last.setVisibility( View.GONE );



        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                name.requestFocus();

            }
        });


        capturePictureHelper = new CapturePictureHelper(this, new CapturePictureHelper.OnPictureGetListener() {


            @Override
            public void onPictureFileGet(String filePath) {
                {
                    final File newPath = new File(AndroidUtils.getCacheDir(), Calendar.getInstance().getTimeInMillis() + "");
                    File tempFile = new File(filePath);
                    tempFile.renameTo(newPath);

                    filePath = newPath.getPath();
                }

                float destWidth=1024f;

                int[] size=    BitmapHelper.readSie(filePath);
                int degree=0;
                float sampleSize=size[0]/destWidth;
                if(size[0]<size[1]) {
                    degree = -90;
                      sampleSize=size[1]/destWidth;
                }


                if(degree==0&& sampleSize <2)
                {
                    getPresenter().scanNameCard( new File(filePath));
                }else {

                    showWaiting("进行图片旋转压缩处理...");
                    final int finalDegree = degree;
                    final float finalScaleSize = sampleSize;


//
                    final String finalFilePath = filePath;
                    new AsyncTask() {
                        @Override
                        protected Object doInBackground(Object[] objects) {
                            BitmapHelper.rotateBitmapFile(finalFilePath, finalDegree, finalScaleSize);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Object o) {
                            hideWaiting();

                            getPresenter().scanNameCard(new File(finalFilePath));

                        }
                    }.execute(AsyncTask.THREAD_POOL_EXECUTOR);

                }



            }
        });



        img_namecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String url = (String) v.getTag();
                ImageViewerHelper.view(CustomerEditActivity.this, url);





            }
        });

        lastRequestFailImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = (String) v.getTag();
                ImageViewerHelper.view(CustomerEditActivity.this, url);





            }
        });

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                getPresenter().retryLastRequest();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getPresenter().deleteCustomer();


            }
        });
    }



    @Override
    public void bindData(Customer customer,NameCard lastRequestNameCard) {

        bindLastRequestNameCard(lastRequestNameCard);



        delete.setVisibility(customer==null||customer.id==0?View.GONE:View.VISIBLE);
        img_namecard.setVisibility(customer!=null&&!StringUtils.isEmpty(customer.nameCardFileUrl)?View.VISIBLE:View.GONE);
        if (customer == null) return;
        code.setText(customer.code);
        company.setText(customer.company);
        name.setText(customer.name);
        tel.setText(customer.tel);
        fax.setText(customer.fax);
        address.setText(customer.addr);
        nation.setText(customer.nation);
        email.setText(customer.email);

        if (customer.nameCardFileUrl!=null) {

            String url = "";
            if (new File(customer.nameCardFileUrl).exists()) {
                url = customer.nameCardFileUrl;
            } else {
                url = PictureUrl.completeUrl(customer.nameCardFileUrl);
            }


            ImageLoaderFactory.getInstance().displayImage(url, img_namecard);
            img_namecard.setTag(url);
        }


    }


    /**
     * 上次请求失败的名片数据
     * @param nameCard
     */
    public  void bindLastRequestNameCard(NameCard nameCard)
    {
        boolean b = nameCard != null && !StringUtils.isEmpty(nameCard.pictureUrl);
        last.setVisibility(b ?View.VISIBLE:View.GONE);

        if(b) {
            String url = PictureUrl.completeUrl(nameCard.pictureUrl);
            ImageLoaderFactory.getInstance().displayImage(url, lastRequestFailImg);
            lastRequestFailImg.setTag(url);
        }
    }

    @Override
    protected View getContentView() {
        return getLayoutInflater().inflate(R.layout.activity_customer_edit, null);
    }

    @Override
    protected CustomerEditMVP.Presenter onLoadPresenter() {
        return new PresenterImpl();
    }

    @Override
    protected void initEventAndData(Bundle savedInstance) {


        setTitle(   "客户编辑");

        //现场恢复
        if(savedInstance!=null&&getPresenter().restoreInstance(savedInstance))
        {

            return;

        }

        {
            Customer customer = (Customer) getIntent().getSerializableExtra(KEY_CUSTOMER);
            getPresenter().initCustomer(customer);

        }

    }


    public static void start(AndroidRouter communicator, int requestCode) {
        start(communicator, requestCode, null);
    }


    public static void start(AndroidRouter communicator, int requestCode, Customer customer) {
        Intent intent = new Intent(communicator.getContext(), CustomerEditActivity.class);
        if (customer != null) {
            intent.putExtra(CustomerEditActivity.KEY_CUSTOMER, customer);
        }
        communicator.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getPresenter().saveInstance(outState);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) return;
        capturePictureHelper.onActivityResult(requestCode, resultCode, data);

    }


}

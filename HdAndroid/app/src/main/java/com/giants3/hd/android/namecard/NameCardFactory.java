package com.giants3.hd.android.namecard;

import android.app.Activity;
import android.content.Intent;

import com.giants3.android.api.namecard.NameCard;
import com.giants3.android.api.namecard.NameCardApi;
import com.giants3.android.frame.util.StorageUtils;
import com.giants3.hd.utils.StringUtils;

import java.io.File;

/**
 * Created by davidleen29 on 2018/9/10.
 */

public class NameCardFactory {
    static NameCardApi nameCardApi;


    public static final String NAMECARDIMPL = "com.giants3.android.namecardscan.NameCardApiImpl";


    public static synchronized NameCardApi getInstance() {
        if (nameCardApi == null) {

            try {
                nameCardApi = (NameCardApi) Class.forName(NAMECARDIMPL).newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (Throwable e) {
                e.printStackTrace();
            }

            if(nameCardApi==null)
            {
                nameCardApi=new NameCardApi() {
                    @Override
                    public void startScan(Activity activity, NameCardPickListener listener) {


                        if( listener!=null)
                        {
                            NameCard nameCard=new NameCard();
                            nameCard.name="test";
                            nameCard.vcf="xxxxxxxxxxxxxx";
                            nameCard.nameCardPitcurePath= StorageUtils.getFilePath("print/0.jpg");
                            listener.onNameCardPick(nameCard);
                        }
                    }

                    @Override
                    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {

                    }
                };
            }


        }
        return nameCardApi;

    }


}

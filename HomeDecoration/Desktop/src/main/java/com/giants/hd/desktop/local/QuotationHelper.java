package com.giants.hd.desktop.local;

import com.giants3.hd.domain.api.CacheManager;
import com.giants3.hd.entity.User;
import com.giants3.hd.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidleen29 on 2018/9/30.
 */
public class QuotationHelper {

    public static List<User>    getQuotationUsersOnLoginUser()
    {
        String relateSales = CacheManager.getInstance().bufferData.quoteAuth.relatedSales;
        List<User> users=new ArrayList<>();

        User all = new User();
        all.id = -1;
        all.code = "--";
        all.name = "--";
        all.chineseName = "所有人";
        users.add(all);
        String[] ids = StringUtils.isEmpty(relateSales) ? null : relateSales.split(",|，");
        if (ids != null) {
            for (User user : CacheManager.getInstance().bufferData.salesmans) {

                boolean find = false;
                for (String s : ids) {
                    if (String.valueOf(user.id).equals(s)) {
                        find = true;
                        break;
                    }
                }

                if (find)
                    users.add(user);
            }

        }
        return users;
    }

}

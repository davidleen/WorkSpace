package com.giants3.hd.server.parser;

import com.giants3.hd.app.AUser;
import com.giants3.hd.entity.User;
import org.springframework.stereotype.Component;

/**  桌面端User 转换成app 端User 数据
 *
 * Created by david on 2016/1/2.
 */
@Component("CustomImplName")
//  qualifier     CustomImplName
public class UserParser implements DataParser<User,AUser> {


    @Override
    public AUser parse(User data) {
        AUser aUser=new AUser();
        aUser.chineseName=data.chineseName;
        aUser.name=data.name;
        aUser.id=data.id;
        aUser.code=data.code;

        aUser.isSalesman=data.isSalesman;
        aUser.tel=data.tel;
        aUser.email=data.email;
        aUser.position=data.position;
        aUser.positionName=data.positionName;
        aUser.internet=data.internet;
        return aUser;
    }
}

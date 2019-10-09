package com.giants3.hd.parser;


import com.giants3.hd.entity.User;
import com.giants3.hd.entity.app.AUser;


/**  桌面端User 转换成app 端User 数据
 *
 * Created by david on 2016/1/2.
 */

//  qualifier     CustomImplName
public class UserParser implements DataParser<User, AUser> {


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

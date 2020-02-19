package com.giants3.reader.server.service;

import com.giants3.reader.entity.*;
import com.giants3.reader.noEntity.ComicChapterInfo;
import com.giants3.reader.noEntity.RemoteData;
import com.giants3.reader.server.repository.*;
import com.giants3.utils.Assets;
import com.giants3.utils.StringUtils;
import de.greenrobot.common.io.IoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by davidleen29 on 2018/5/12.
 */
@Service
public class AuthService extends AbstractService {

    @Autowired
    AuthCodesRepository authCodesRepository;
    @Autowired
    SettingsRepository settingsRepository;


    public RemoteData<AuthCodes> getAuthCodesByKey( String key,String platform)
    {
        Sort itemIndex = sortByParam(Sort.Direction.DESC, "itemIndex");
        List result ;
        if(StringUtils.isEmpty(platform))
        {

            result=authCodesRepository.findByCodeLike(StringUtils.sqlLike(key),itemIndex);
        }else
        {
            result=    authCodesRepository.findByCodeLikeAndPlatformEquals(StringUtils.sqlLike(key), platform, itemIndex);
        }



        return wrapData(result);
    }

    public void addNewCode(String code, String platform) {


        AuthCodes authCodes=new AuthCodes();
        authCodes.code=code;
        authCodes.platform=platform;
        authCodes.itemIndex= (int) authCodesRepository.count();
        authCodesRepository.save(authCodes);
    }

    public void deleteAuthCode(long id) {


        authCodesRepository.delete(id);
    }

    public void setRate(int rate) {


        Settings first = getSettings();
        if(first==null) {
            first=new Settings();
        }
        first.rate=rate;
        settingsRepository.save(first);

    }


    public Settings getSettings()
    {
        List<Settings> all = settingsRepository.findAll();
        return all==null||all.size()==0?null:all.get(0);
    }
}

package com.giants3.hd.server.service;


import com.giants3.hd.entity.*;
import com.giants3.hd.entity.app.AUser;
import com.giants3.hd.entity.app.AppQuoteAuth;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.parser.DataParser;
import com.giants3.hd.parser.ProductParser;
import com.giants3.hd.parser.RemoteDataParser;
import com.giants3.hd.parser.UserParser;
import com.giants3.hd.server.repository.*;
import com.giants3.hd.utils.DateFormats;
import com.giants3.hd.utils.DigestUtils;
import com.giants3.hd.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by davidleen29 on 2018/6/12.
 */
@Service
public class AuthorityService extends AbstractService {


    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private AppQuoteAuthRepository appQuoteAuthRepository;

    @Autowired

    private SessionRepository sessionRepository;


    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    AppVersionRepository appVersionRepository;

    @Autowired
    QuoteAuthRepository quoteAuthRepository;

    @Autowired
    StockOutAuthRepository stockOutAuthRepository;

    @Autowired
    OrderAuthRepository orderAuthRepository;

    @Autowired
    UserRepository userRepository;


    DataParser<User, AUser> dataParser;


    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        dataParser=new UserParser();
    }

    public List<Authority> getAuthoritiesForUser(@RequestParam(value = "userId") long userId) {
        User user = userRepository.findOne(userId);

        List<Module> modules = moduleRepository.findAll();
        List<Authority> authorities = authorityRepository.findByUser_IdEquals(userId);



        int moduleSize = modules.size();
        for (int i = 0; i < moduleSize; i++) {

            Module module = modules.get(i);
            boolean found = false;
            for (Authority authority : authorities) {
                if (authority.module.id == module.id) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                Authority authority = new Authority();
                authority.module = module;
                authority.user = user;

                authorityRepository.saveAndFlush(authority);

            }


        }


        return authorityRepository.findByUser_IdEquals(userId);
    }

    @Transactional
    public RemoteData<Authority> saveAuthorities(@RequestParam(value = "userId") long userId, @RequestBody List<Authority> authorities) {
        User user = userRepository.findOne(userId);
        if(user==null)

            return wrapError("用户不存在");
        List<Authority> newData = new ArrayList<>();



        for(Authority authority:authorities)
        {
            if(authority.id<=0)
                return wrapError("数据异常，权限数据未初始化");

            if(authority.user.id!=userId)
            {
                return wrapError("数据异常，用户id不匹配");
            }

        }
        newData.addAll( authorityRepository.save(authorities));
        authorityRepository.flush();
        return wrapData(newData);
    }


    @Transactional
    public RemoteData<User> doLogin2(long userId, String passwordMd5, String client, int version, String loginIp, String device_token) {
        User findUser = userRepository.findOne(userId);
        if (findUser == null)
            return wrapError("用户不存在");


        final RemoteData<User> userRemoteData = doLogin2(findUser, passwordMd5, client, version, loginIp, device_token);

        return userRemoteData;

    }

    /**
     * 登录逻辑
     *
     * @param findUser
     * @param passwordMd5
     * @param client
     * @param version
     * @param loginIp
     * @param device_token
     */
    public RemoteData<User> doLogin2(User findUser, String passwordMd5, String client, int version, String loginIp, String device_token) {


        if (!findUser.isCorrectPassword(passwordMd5)) {
            return wrapError("密码错误");
        }
        findUser = (User) ObjectUtils.deepCopy(findUser);

        findUser.password = null;
        findUser.passwordMD5 = null;

        RemoteData<User> data = wrapData(findUser);
        Date date = Calendar.getInstance().getTime();
        long loginTime = date.getTime();
        data.token = DigestUtils.md5(findUser.toString() + loginTime);
        AppVersion appVersion = getAppVersion();
        if (version > 0 && appVersion != null && appVersion.versionCode > version) {
            data.newVersionCode = appVersion.versionCode;
            data.newVersionName = appVersion.versionName;
        }


        //Session session= sessionRepository.findFirstByUser_IdEqualsEqualsOrderByLoginTimeDesc(findUser.id);

        Session session = new Session();
        session.user
                = findUser;

        session.loginTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM_SS.format(date);
        session.loginTime = loginTime;
        session.token = data.token;
        session.loginIp = loginIp;
        session.client = client;
        session.device_token = device_token;
        sessionRepository.save(session);


        return data;
    }

    public AppVersion getAppVersion() {
        return appVersionRepository.findFirstByAppNameLikeOrderByVersionCodeDescUpdateTimeDesc("%%");
    }

    public QuoteAuth getQuoteAuthForUser(@RequestParam(value = "userId") long userId) {
        return quoteAuthRepository.findFirstByUser_IdEquals(userId);
    }

    public List<QuoteAuth> getQuoteAuths() {



        return quoteAuthRepository.findAll();

    }

    public void initQuoteAuths() {
        List<User> users = userRepository.findByDeletedEqualsOrderByCode(false);


        List<QuoteAuth> quoteAuths = quoteAuthRepository.findAll();
        //移除user 为delete 的权限配置
        List<QuoteAuth> tempQuoteAuthList = new ArrayList<>();
        for (QuoteAuth quoteAuth : quoteAuths) {
            if (quoteAuth.user.deleted) tempQuoteAuthList.add(quoteAuth);
        }
        quoteAuths.removeAll(tempQuoteAuthList);


        tempQuoteAuthList.clear();


        int size = users.size();
        for (int i = 0; i < size; i++) {

            User user = users.get(i);
            if (user.deleted) continue;
            boolean found = false;
            for (QuoteAuth quoteAuth : quoteAuths) {
                if (user.id == quoteAuth.user.id) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                QuoteAuth authority = new QuoteAuth();
                authority.user = user;
                tempQuoteAuthList.add(authority);

            }


        }

        quoteAuthRepository.save(tempQuoteAuthList);



    }


    /**
     * 初始化广交会报价单权限数据 ，每个用户都自动配给一个数据
     * @return
     */
    public void initAppQuoteAuths() {
        List<User> users = userRepository.findByDeletedEqualsOrderByCode(false);


        List<AppQuoteAuth> quoteAuths = appQuoteAuthRepository.findAll();
        //移除user 为delete 的权限配置
        List<AppQuoteAuth> tempQuoteAuthList = new ArrayList<>();
        for (AppQuoteAuth quoteAuth : quoteAuths) {
            if (quoteAuth.user.deleted) tempQuoteAuthList.add(quoteAuth);
        }
        quoteAuths.removeAll(tempQuoteAuthList);


        tempQuoteAuthList.clear();


        int size = users.size();
        for (int i = 0; i < size; i++) {

            User user = users.get(i);
            if (user.deleted) continue;
            boolean found = false;
            for (AppQuoteAuth quoteAuth : quoteAuths) {
                if (user.id == quoteAuth.user.id) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                AppQuoteAuth authority = new AppQuoteAuth();
                authority.user = user;
                tempQuoteAuthList.add(authority);

            }


        }


        appQuoteAuthRepository.save(tempQuoteAuthList);


    }

    @Transactional
    public RemoteData<User> doLogin2(String userName, String passwordMd5, String client, int version, String loginIp, String device_token) {


        List<User> userList = userRepository.findByNameEquals(userName);

        int size = userList.size();
        if (size <= 0)
            return wrapError("用户账户不存在");
        if (size > 1)
            return wrapError("存在重名用户，请联系管理员");

        User findUser = userList.get(0);
        if(findUser.stopped)
        {
            return wrapError("当前账号已经停用，请联系管理员");
        }

        final RemoteData<User> userRemoteData = doLogin2(findUser, passwordMd5, client, version, loginIp, device_token);
        return userRemoteData;

    }

    @Transactional
    public RemoteData<AUser> doLogin2Service(String userName, String password, String client, int version, String device_token, String remoteAddr) {
        RemoteData<User> userRemoteData = doLogin2(userName, password, client, version, remoteAddr, device_token);
        RemoteData<AUser> result = RemoteDataParser.parse(userRemoteData, dataParser);


        if (result.isSuccess()) {

            AUser loginUser = result.datas.get(0);
            loginUser.token = result.token;
            List<Authority> authorities = authorityRepository.findByUser_IdEquals(loginUser.id);
            QuoteAuth quoteAuth = getQuoteAuthForUser(loginUser.id);
            loginUser.authorities = authorities;
            loginUser.quoteAuth = quoteAuth;

        }
        return result;
    }

    public List<Module> findAllModules() {
        return moduleRepository.findAll();
    }

    public List<OrderAuth> getOrderAuths() {
        final List<User> users1 = userRepository.findByDeletedEqualsOrderByCode(false);


        List<User> users = users1;


        List<OrderAuth> auths = orderAuthRepository.findAll();
        //移除user 为delete 的权限配置
        List<OrderAuth> tempAuthList = new ArrayList<>();
        for (OrderAuth aAuth : auths) {
            if (aAuth.user.deleted) tempAuthList.add(aAuth);
        }
        auths.removeAll(tempAuthList);


        tempAuthList.clear();


        int size = users.size();
        for (int i = 0; i < size; i++) {

            User user = users.get(i);
            if (user.deleted) continue;
            boolean found = false;
            for (OrderAuth quoteAuth : auths) {
                if (user.id == quoteAuth.user.id) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                OrderAuth authority = new OrderAuth();
                authority.user = user;
                tempAuthList.add(authority);

            }


        }

        auths.addAll(tempAuthList);
        return auths;
    }

    public List<StockOutAuth> getStockOutAuths() {
        List<User> users = userRepository.findByDeletedEqualsOrderByCode(false);


        List<StockOutAuth> auths = stockOutAuthRepository.findAll();
        //移除user 为delete 的权限配置
        List<StockOutAuth> tempAuthList = new ArrayList<>();
        for (StockOutAuth aAuth : auths) {
            if (aAuth.user.deleted) tempAuthList.add(aAuth);
        }
        auths.removeAll(tempAuthList);


        tempAuthList.clear();


        int size = users.size();
        for (int i = 0; i < size; i++) {

            User user = users.get(i);
            if (user.deleted) continue;
            boolean found = false;
            for (StockOutAuth quoteAuth : auths) {
                if (user.id == quoteAuth.user.id) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                StockOutAuth authority = new StockOutAuth();
                authority.user = user;
                tempAuthList.add(authority);

            }


        }

        auths.addAll(tempAuthList);
        return auths;
    }

    @Transactional
    public List<QuoteAuth> saveQuotes(List<QuoteAuth> authorities) {
        List<QuoteAuth> newData = new ArrayList<>();

        for (QuoteAuth authority : authorities) {

            newData.add(quoteAuthRepository.save(authority));
        }
        return newData;
    }

    @Transactional
    public List<OrderAuth> saveOrderAuthList(List<OrderAuth> authorities) {


        List<OrderAuth> newData = new ArrayList<>();

        for (OrderAuth authority : authorities) {

            newData.add(orderAuthRepository.save(authority));
        }

        return newData;
    }

    @Transactional
    public List<StockOutAuth> saveStockOutAuthList(List<StockOutAuth> authorities) {

        List<StockOutAuth> newData = new ArrayList<>();

        for (StockOutAuth authority : authorities) {

            newData.add(stockOutAuthRepository.save(authority));
        }
        return newData;
    }


    /**
     * 查登录过的session数据
     * @param token
     * @return
     */
    @Transactional
    public  Session checkSessionForToken(String token)
    {
        return sessionRepository.findFirstByTokenEquals(token);
    }

    public List<AppQuoteAuth> saveAppQuotes(List<AppQuoteAuth> authorities) {
        List<AppQuoteAuth> newData = new ArrayList<>();

        for (AppQuoteAuth authority : authorities) {

            newData.add(appQuoteAuthRepository.save(authority));
        }
        return newData;

    }

    public List<AppQuoteAuth> getAppQuoteAuths() {

        return appQuoteAuthRepository.findAll();
    }
}

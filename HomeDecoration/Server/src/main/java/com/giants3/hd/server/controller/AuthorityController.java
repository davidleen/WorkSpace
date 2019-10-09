package com.giants3.hd.server.controller;


import com.giants3.hd.entity.*;
import com.giants3.hd.entity.app.AUser;
import com.giants3.hd.entity.app.AppQuoteAuth;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.server.service.AuthorityService;
import com.giants3.hd.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 权限
 * Created by davidleen29 on 2014/9/18.
 */
@Controller

@RequestMapping("/authority")
public class AuthorityController extends BaseController {

    @Autowired
    AuthorityService authorityService;


    @RequestMapping(value = "/findByUser", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Authority> findByUser(@RequestParam(value = "userId") long userId) {


        List<Authority> authorities = authorityService.getAuthoritiesForUser(userId);


        return wrapData(authorities);


    }


    @RequestMapping(value = "/saveList", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<Authority> save(@RequestParam(value = "userId") long userId, @RequestBody List<Authority> authorities) {


        return authorityService.saveAuthorities(userId, authorities);

    }


    /**
     * 提供给移动端调用的接口
     *
     * @param request
     * @param params
     * @return
     */
    @RequestMapping(value = "/aLogin2", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<AUser> aLogin2(HttpServletRequest request, @RequestBody Map<String, String> params) {


        String userName = params.get("userName");
        String password = params.get("password");
        String client = params.get("client");
        int version = 0;
        try {
            version = Integer.valueOf(params.get("version"));
        } catch (Throwable t) {
        }

        String device_token = params.get("device_token");
        device_token = StringUtils.isEmpty(device_token) ? "" : device_token;
        String versionName = params.get("versionName");
        versionName = StringUtils.isEmpty(versionName) ? "" : versionName;

        final String remoteAddr = request.getRemoteAddr();
        RemoteData<AUser> result = authorityService.doLogin2Service(userName, password, client, version, device_token, remoteAddr);


        return result;


    }


    /**
     * 登录接口2  密码加密
     *
     * @param request
     * @param user
     * @param appVersion
     * @return
     */
    @RequestMapping(value = "/login2", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<User> login2(HttpServletRequest request, @RequestBody User user, @RequestParam(value = "appVersion", required = false, defaultValue = "0") int appVersion, @RequestParam(value = "client", required = false, defaultValue = "DESKTOP") String client) {

        return doLogin2(user.name, user.password, client, appVersion, request.getRemoteAddr());
    }

    private RemoteData<User> doLogin2(String userName, String passwordMd5, String client, int version, String loginIp) {


        return authorityService.doLogin2(userName, passwordMd5, client, version, loginIp, "");
    }


    /**
     * 登录接口   密码加密
     *
     * @param request
     * @param userId
     * @param appVersion
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<User> login(HttpServletRequest request,
                           @RequestParam(value = "userId", required = false, defaultValue = "0") long userId,
                           @RequestParam(value = "password", required = false, defaultValue = "") String password,
                           @RequestParam(value = "appVersion", required = false, defaultValue = "0") int appVersion
            , @RequestParam(value = "client", required = false, defaultValue = "DESKTOP") String client
            , @RequestParam(value = "device_token", required = false, defaultValue = "") String device_token

    ) {


        final RemoteData<User> userRemoteData = authorityService.doLogin2(userId, password, client, appVersion, request.getRemoteAddr(), device_token);
        return userRemoteData;
    }


    @RequestMapping(value = "/moduleList", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Module> moduleList() {


        return wrapData(authorityService.findAllModules());


    }


    @RequestMapping(value = "/loadAppVersion", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<AppVersion> loadAppVersion() {


        AppVersion appVersion = authorityService.getAppVersion();
        if (appVersion == null) {
            wrapError("无最新版本");
        }

        return wrapData(appVersion);


    }


    @RequestMapping(value = "/findQuoteAuth", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<QuoteAuth> findQuoteAuth(@RequestParam(value = "userId") long userId) {


        QuoteAuth quoteAuth = authorityService.getQuoteAuthForUser(userId);
        return wrapData(quoteAuth);


    }


    @RequestMapping(value = "/quoteAuthList", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<QuoteAuth> quoteAuthList() {


        List<QuoteAuth> quoteAuths = authorityService.getQuoteAuths();


        return wrapData(quoteAuths);
    }


    @RequestMapping(value = "/orderAuthList", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<OrderAuth> orderAuthList() {


        List<OrderAuth> auths = authorityService.getOrderAuths();


        return wrapData(auths);
    }


    @RequestMapping(value = "/stockOutAuthList", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<StockOutAuth> stockOutAuthList() {


        List<StockOutAuth> auths = authorityService.getStockOutAuths();


        return wrapData(auths);
    }

    @RequestMapping(value = "/saveQuoteList", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<QuoteAuth> saveQuoteList(@RequestBody List<QuoteAuth> authorities) {


        List<QuoteAuth> newData = authorityService.saveQuotes(authorities);


        return wrapData(newData);
    }


    @RequestMapping(value = "/saveStockOutList", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<StockOutAuth> saveStockOutList(@RequestBody List<StockOutAuth> authorities) {


        List<StockOutAuth> newData = authorityService.saveStockOutAuthList(authorities);


        return wrapData(newData);
    }


    @RequestMapping(value = "/saveOrderList", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<OrderAuth> saveOrderList(@RequestBody List<OrderAuth> authorities) {

        List<OrderAuth> newData = authorityService.saveOrderAuthList(authorities);


        return wrapData(newData);
    }

    @RequestMapping(value = "/saveAppQuoteList", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<AppQuoteAuth> saveAppQuoteList(@RequestBody List<AppQuoteAuth> authorities) {


        List<AppQuoteAuth> newData = authorityService.saveAppQuotes(authorities);


        return wrapData(newData);
    }


    @RequestMapping(value = "/appQuoteAuthList", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<AppQuoteAuth> getAppQuoteAuthList() {


        List<AppQuoteAuth> quoteAuths = authorityService.getAppQuoteAuths();


        return wrapData(quoteAuths);
    }
}

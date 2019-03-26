package com.giants3.hd.server.interceptor;


import com.giants3.hd.entity.Session;
import com.giants3.hd.noEntity.ConstantData;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.server.BootConfig;
import com.giants3.hd.server.repository.SessionRepository;
import com.giants3.hd.server.service.AuthorityService;
import com.giants3.hd.server.utils.Constraints;
import com.giants3.hd.utils.GsonUtils;
import com.giants3.hd.utils.UrlFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.logging.Logger;


public class AuthInterceptor extends HandlerInterceptorAdapter {

    private static final String TAG = "AuthInterceptor";
    private static final CharSequence WEIXIN = "weixin";
    public static final String TOKEN = "token";
    public String UNLOGIN = "api/authority/unLogin";

    public String UN_INTERCEPT_USERLIST = "/api/user/list";
    public String UN_INTERCEPT_ALOGIN = "/api/authority/aLogin";
    public String UN_INTERCEPT_LOGIN = "/api/authority/login";

    public String UN_INTERCEPT_FILE = "/api/file/download/";

    public long VALIDATE_TIME = 24l * 60 * 60 * 1000;


    @Autowired
    BootConfig bootConfig;

    @Autowired
    AuthorityService authorityService;

    @Autowired
    private SessionRepository sessionRepository;



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        Logger.getLogger(TAG).info("AuthInterceptor");


        String url = request.getRequestURI();
        //文件下载 不验证参数
        if( url.contains(UN_INTERCEPT_FILE))
        {
            return true;
        }

        //签名验证
        if(!bootConfig.isDebug()) {
            if ( !UrlFormatter.validateSign(request.getQueryString())) {
                writeErrorMessage(response.getOutputStream(), RemoteData.CODE_FAIL, "sign verify fail");
                return false;
            }
        }



        HttpServletRequestWrapper wrapper;
        //非过滤的url
        if (ConstantData.FOR_TEST || url.contains(UN_INTERCEPT_LOGIN) || url.contains(UN_INTERCEPT_ALOGIN) || url.contains(UN_INTERCEPT_USERLIST) || url.contains(UNLOGIN) || url.contains(WEIXIN))
            return true;
        else
//            if(url.contains("app/quotation/findDetails")||url.contains("api/customer/list"))//特殊接口，用来服务器之间同步。
//            {
//                return true;
//            }
//             else
            {
            String token = request.getParameter(TOKEN);

            String appVersion = request.getParameter("appVersion");
            int
                    integer = 0;
            try {
                integer = Integer.valueOf(appVersion).intValue();
            } catch (Throwable t) {

            }
            //  ConstantData.IS_CRYPT_JSON = integer >= ConstantData.CRYPE_JSON_FROM_VERSION;
            Session session = authorityService.checkSessionForToken(token);
            // Session session = sessionRepository.findFirstByTokenEquals(token);


            if (session != null) {

//                String ip=request.getRemoteAddr();
//                if(!session.user.internet&&!ip.startsWith("192.168."))
//                {
//                    //当前用户无外网访问权限
//
//
//                }


                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime - session.loginTime < VALIDATE_TIME) {
                    request.setAttribute(Constraints.ATTR_LOGIN_USER, session.user);
                    //  request.setAttribute(Constraints.ATTR_LOGIN_SESSION, session);
                    return true;
                }


            }
            //如果验证失败
            //返回到登录界面
            writeErrorMessage(response.getOutputStream(), RemoteData.CODE_UNLOGIN, "用户未登录，或者登录超时失效");

            return false;


        }

    }

    private void writeErrorMessage(OutputStream outputStream, int code, String message) throws IOException {
        RemoteData<Void> data = new RemoteData<>();
        data.code = code;
        data.message = message;
        byte[] bytes = null;
        try {
            bytes = GsonUtils.toJson(data).getBytes(GsonUtils.UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        outputStream.write(bytes);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }
}
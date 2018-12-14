package com.giants3.reader.server.interceptor;



import com.giants3.reader.noEntity.RemoteData;

import com.giants3.reader.server.utils.Constraints;
import com.giants3.utils.GsonUtils;
import com.giants3.utils.UrlFormatter;
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

//    @Autowired
//    SessionRepository sessionRepository;
    private boolean DEVELOP=true;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        if(DEVELOP) return true;

        Logger.getLogger(TAG).info("AuthInterceptor");


        String url = request.getRequestURI();


        //签名验证

        if (!UrlFormatter.validateSign(request.getQueryString())) {


            writeErrorMessage(response.getOutputStream(), RemoteData.CODE_FAIL, "签名效验失败");

            return false;

        }


        return true;



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
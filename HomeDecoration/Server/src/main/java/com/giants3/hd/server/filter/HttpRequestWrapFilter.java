package com.giants3.hd.server.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.logging.Logger;

/**
 * Created by david on 2016/2/11.
 */
@Deprecated
public class HttpRequestWrapFilter implements Filter {


    private static  final String TAG="HttpRequestWrapFilter";
    @Override
    public void destroy() {}
    @Override
    public void init(FilterConfig arg0) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpRequestWrapper    httpRequestWrapper=new HttpRequestWrapper((HttpServletRequest)request);
      //  HttpResponseWrapper  httpResponseWrapper=new HttpResponseWrapper((HttpServletResponse)response);

        chain.doFilter(httpRequestWrapper, response);




    }
}
package com.giants3.hd.server.datasource;

import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.PatternMatchUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 设置数据源KEY的拦截器
 * 
 */
public class DynamicDataSourceInterceptor implements MethodInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceInterceptor.class);
    /**
     * 方法和使用数据源key的对应关系
     */
    private List<String> attributeSource = new ArrayList<String>();

    public Object invoke(MethodInvocation invocation) throws Throwable {
        final String methodName = invocation.getMethod().getName();
        String key = null;
        for (String value : attributeSource) {
            String mappedName = value.split(",")[0];
            if (isMatch(methodName, mappedName)) {
                key = value.split(",")[1];
                break;
            }
        }
        logger.debug("methodName:" + methodName);
        if (null != key) {
            DynamicDataSourceKeyHolder.setKey(key);
        }

        return invocation.proceed();
    }

    private boolean isMatch(String methodName, String mappedName) {
        return PatternMatchUtils.simpleMatch(mappedName, methodName);
    }

    public List<String> getAttributeSource() {
        return attributeSource;
    }

    public void setAttributeSource(List<String> attributeSource) {
        this.attributeSource = attributeSource;
    }

}
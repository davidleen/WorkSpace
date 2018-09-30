package com.giants3.hd.server.service;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Created by david on 2016/1/16.
 */
@Service
public class WeixinService implements InitializingBean,DisposableBean {



    public   <T>  T handleWxTxt(String message,Class<T> tClass)
    {


        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(tClass);

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            StringReader reader = new StringReader(message);
            T person = (T) unmarshaller.unmarshal(reader);
            return person;

        } catch (JAXBException e) {
            e.printStackTrace();
        }



        return  null;
    }


    public   <T> String toXmlString(T object)
    {


        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(object.getClass());

            Marshaller unmarshaller = jaxbContext.createMarshaller();

            StringWriter writer = new StringWriter( );
             unmarshaller.marshal(object,writer);
            return writer.toString();

        } catch (JAXBException e) {
            e.printStackTrace();
        }



        return  null;
    }


    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}

package com.giants3.hd.server.converter;


import java.io.*;
import java.nio.charset.Charset;
import java.lang.reflect.Type;
import java.util.List;

import com.giants3.hd.noEntity.ConstantData;
import com.giants3.crypt.CryptUtils;
import com.google.gson.*;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;

import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;

/**
 * 自定义JSON 信息转换类。
 */

public class GsonMessageConverter  extends AbstractHttpMessageConverter<Object>
        implements GenericHttpMessageConverter<Object> {


        public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    /**
     * json 生产中过滤所有密码字段
     */
    private static final String PASSWORD = "password";
    /**
     * json 生产中过滤所有密码字段
     */
    private static final String PASSWORD_MD_5 = "passwordmd5";
    private Gson _gson;
        private Type _type = null;
        private boolean _prefixJson = false;
    private static ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();

    /**
         * Construct a new {@code GsonMessageConverter} with a default
         * {@link Gson#Gson() Gson}.
         */
        public GsonMessageConverter() {
            this(false);
        }

        /**
         * Construct a new {@code GsonMessageConverter}.
         *
         * @param serializeNulls
         *            true to generate json for null values
         */
        public GsonMessageConverter(boolean serializeNulls) {
            super(new MediaType("application", "json", DEFAULT_CHARSET));
            GsonBuilder builder=new GsonBuilder();
            builder.addSerializationExclusionStrategy(new ExclusionStrategy(){
                /**
                 * @param f the field object that is under test
                 * @return true if the field should be ignored; otherwise false
                 */
                @Override
                public boolean shouldSkipField(FieldAttributes f) {


                    final String fieldName = f.getName().toLowerCase();
                    if(fieldName.equals(PASSWORD)|| fieldName.equals(PASSWORD_MD_5))

                        return true;

                    return false;
                }

                /**
                 * @param clazz the class object that is under test
                 * @return true if the class should be ignored; otherwise false
                 */
                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }
            });
            if(serializeNulls)
                builder.serializeNulls();
            builder.registerTypeAdapter(List.class,new ListTypeAdapter());
            setGson(builder.create());
        }



        /**
         * Sets the {@code Gson} for this viewImpl. If not set, a default
         * {@link Gson#Gson() Gson} is used.
         * <p>
         * Setting a custom-configured {@code Gson} is one way to take further
         * control of the JSON serialization process.
         *
         * @throws IllegalArgumentException
         *             if gson is null
         */
        public void setGson(Gson gson) {
            Assert.notNull(gson, "'gson' must not be null");
            _gson = gson;
        }

        public void setType(Type type) {
            this._type = type;
        }

        public Type getType() {
            return _type;
        }

        /**
         * Indicates whether the JSON output by this viewImpl should be prefixed with
         * "{} &&". Default is false.
         * <p>
         * Prefixing the JSON string in this manner is used to help prevent JSON
         * Hijacking. The prefix renders the string syntactically invalid as a
         * script so that it cannot be hijacked. This prefix does not affect the
         * evaluation of JSON, but if JSON validation is performed on the string,
         * the prefix would need to be ignored.
         */
        public void setPrefixJson(boolean prefixJson) {
            this._prefixJson = prefixJson;
        }

        @Override
        public boolean canRead(Class<?> clazz, MediaType mediaType) {
            return canRead(mediaType);
        }

        @Override
        public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
            return canRead(mediaType);
        }

        @Override
        public boolean canWrite(Class<?> clazz, MediaType mediaType) {
            return canWrite(mediaType);
        }

        @Override
        protected boolean supports(Class<?> clazz) {
            // should not be called, since we override canRead/Write instead
            throw new UnsupportedOperationException();
        }

        /**
         * Generic types will be processed here.
         */
        @Override
        public Object read(Type type, Class<?> contextClass,
                           HttpInputMessage inputMessage)
                throws HttpMessageNotReadableException, IOException {
            setType(type);
            return readInternal(null, inputMessage);
        }

        @Override
        protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage)
                throws IOException, HttpMessageNotReadableException {




            InputStream inputStream=inputMessage.getBody();
            if(ConstantData.IS_CRYPT_JSON) {
                synchronized (byteArrayOutputStream) {
                    byteArrayOutputStream.reset();
                    IOUtils.copy(inputStream, byteArrayOutputStream);
                    inputStream.close();

                    byte[] data = byteArrayOutputStream.toByteArray();
                    byteArrayOutputStream.reset();
                    data=CryptUtils.decryptDES(data, ConstantData.DES_KEY);

                    inputStream = new ByteArrayInputStream(data);

                }
            }
                Reader json = new InputStreamReader(inputStream,
                        getCharset(inputMessage.getHeaders()));

                try {
                    Type typeOfT = getType();
                    if (typeOfT != null) {
                        return _gson.fromJson(json, typeOfT);
                    } else {
                        return _gson.fromJson(json, clazz);
                    }
                } catch (JsonSyntaxException ex) {
                    throw new HttpMessageNotReadableException("Could not read JSON: "
                            + ex.getMessage(), ex);
                } catch (JsonIOException ex) {
                    throw new HttpMessageNotReadableException("Could not read JSON: "
                            + ex.getMessage(), ex);
                } catch (JsonParseException ex) {
                    throw new HttpMessageNotReadableException("Could not read JSON: "
                            + ex.getMessage(), ex);
                } finally {
                    setType(null);
                }


        }

        @Override
        protected void writeInternal(Object o, HttpOutputMessage outputMessage)
                throws IOException, HttpMessageNotWritableException {




            synchronized (byteArrayOutputStream) {


                byteArrayOutputStream.reset();
                OutputStreamWriter writer = new OutputStreamWriter(
                        byteArrayOutputStream, getCharset(outputMessage.getHeaders()));

                try {
                    if (this._prefixJson) {
                        writer.append("{} && ");
                    }
                    Type typeOfSrc = getType();


                    if (typeOfSrc != null) {
                        _gson.toJson(o, typeOfSrc, writer);
                    } else {

                        _gson.toJson(o, writer);
                    }

                    writer.close();

                    byte[] data=byteArrayOutputStream.toByteArray();
                    byteArrayOutputStream.reset();
                    outputMessage.getBody().write(ConstantData.IS_CRYPT_JSON ?CryptUtils.encryptDES(data, ConstantData.DES_KEY):data);

                } catch (JsonIOException ex) {
                    throw new HttpMessageNotWritableException("Could not jxl.biff.biff JSON: "
                            + ex.getMessage(), ex);
                }
            }
        }

        // helpers

        private Charset getCharset(HttpHeaders headers) {
            if (headers != null && headers.getContentType() != null
                    && headers.getContentType().getCharSet() != null) {
                return headers.getContentType().getCharSet();
            }
            return DEFAULT_CHARSET;
        }



}

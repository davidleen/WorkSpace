package com.giants3.hd.server.service_third;

import com.giants3.hd.domain.api.Client;
import com.giants3.hd.noEntity.NameCard;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.server.service.AbstractService;
import com.giants3.hd.utils.DigestUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 名片解析，讯飞在线解析
 * Created by davidleen29 on 2018/9/20.
 */
@Service
public class NameCardService extends AbstractService {

    public static final String TAG = "XunfeiNameCardApi";
    private static final int REQUEST_CODE_RECOGNIZE = 0x1001;

    private static final String XUNFEI_API = "5baa3c46";
    private static final String XUNFEI_AAPIKey = "6dd084daeb110c42b7d6fc3c1c3f006b";
    public static final String WEB_API = "http://webapi.xfyun.cn/v1/service/v1/ocr/business_card";
    public static final String UTF_8 = "UTF-8";
    public static final String TYPE_BUSINESS_CARD = "business_card";


    public RemoteData<NameCard> requestScanNameCard(String pictureFilePath ) {

        Client client = new Client();


        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try {
            in = new FileInputStream(pictureFilePath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            String base64img = new String(Base64.encodeBase64(data), UTF_8);
            base64img = URLEncoder.encode(base64img, UTF_8);

            String url = WEB_API;

            String xTime = String.valueOf(System.currentTimeMillis() / 1000);

            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("engine_type", TYPE_BUSINESS_CARD);
            jsonObject.addProperty("osid", "Windows");

            String xParam = "";

            final String s = jsonObject.toString();
            logger.info(s);
            xParam = new String(Base64.encodeBase64(s.getBytes(UTF_8)));

            String x_checksum = "";

            x_checksum = DigestUtils.md5((XUNFEI_AAPIKey + xTime + xParam));


            Map<String, String> headers = new HashMap<>();
            headers.put("X-Appid", XUNFEI_API);
            headers.put("X-CurTime", xTime);
            headers.put("X-Param", xParam);
            headers.put("X-CheckSum",
                    x_checksum);
            headers.put("Content-Type",
                    "application/x-www-form-urlencoded; charset=utf-8");


            String result = client.post(url, headers, "image=" + base64img);//post body  要加上image= !!!!!
            logger.error(result);

            final JsonObject json = new JsonParser().parse(result).getAsJsonObject();

            int code = json.get("code").getAsInt();
            String desc = json.get("desc").getAsString();
            if (code != 0) {
                logger.error(desc);
                return wrapError(desc);

            }

            return wrapData(fromSource(result));

        } catch (Throwable t) {

            t.printStackTrace();
            return wrapError(t.getMessage());
        } finally {
            client.close();
        }

    }


    private NameCard fromSource(String result) {
        final JsonObject json = new JsonParser().parse(result).getAsJsonObject();
        NameCard nameCard = new NameCard();

        nameCard.orginData = result;
        JsonObject dataJson = json.getAsJsonObject("data");

        try {
            final JsonArray itemJson = dataJson.getAsJsonArray("url");
            String url = "";
            if (itemJson.size() > 0) {
                url = itemJson.get(0).getAsJsonObject().get("item").getAsString();
            }
            nameCard.url = url;
        } catch (Throwable t) {
            t.printStackTrace();
        }

        try {
            final JsonArray itemJson = dataJson.getAsJsonArray("title");
            String title = "";
            if (itemJson.size() > 0) {
                title = itemJson.get(0).getAsJsonObject().get("item").getAsString();
            }
            nameCard.title = title;
        } catch (Throwable t) {
            t.printStackTrace();
        }
        try {
            final JsonArray itemJson = dataJson.getAsJsonArray("email");
            String email = "";
            if (itemJson.size() > 0) {
                email = itemJson.get(0).getAsJsonObject().get("item").getAsString();
            }
            nameCard.email = email;
        } catch (Throwable t) {
            t.printStackTrace();
        }
        try {
            final JsonArray itemJson = dataJson.getAsJsonArray("formatted_name");
            String name = "";
            if (itemJson.size() > 0) {
                name = itemJson.get(0).getAsJsonObject().get("item").getAsString();
            }
            nameCard.name = name;
        } catch (Throwable t) {
            t.printStackTrace();
        }  try {
            final JsonArray itemJson = dataJson.getAsJsonArray("organization");
            String organization = "";
            if (itemJson.size() > 0) {
                organization = itemJson.get(0).getAsJsonObject().get("item").getAsJsonObject().get("name").getAsString();
            }
            nameCard.company = organization;
        } catch (Throwable t) {
            t.printStackTrace();
        }
        try {
            final JsonArray itemJson = dataJson.getAsJsonArray("telephone");
            String telephone = "";
            if (itemJson.size() > 0) {
                telephone = itemJson.get(0).getAsJsonObject().get("item").getAsJsonObject().get("number").getAsString();
            }
            nameCard.telephone = telephone;
        } catch (Throwable t) {
            t.printStackTrace();
        }
        try {
            final JsonArray itemJson = dataJson.getAsJsonArray("label");
            String address = "";
            if (itemJson.size() > 0) {
                address = itemJson.get(0).getAsJsonObject().get("item").getAsJsonObject().get("address").getAsString();
            }
            nameCard.address = address;
        } catch (Throwable t) {
            t.printStackTrace();
        }
try {
            final JsonArray itemJson = dataJson.getAsJsonArray("address");
            String nation = "";
            if (itemJson.size() > 0) {
                nation = itemJson.get(0).getAsJsonObject().get("item").getAsJsonObject().get("country").getAsString();
            }
            nameCard.nation = nation;
        } catch (Throwable t) {
            t.printStackTrace();
        }


        return nameCard;

    }

}

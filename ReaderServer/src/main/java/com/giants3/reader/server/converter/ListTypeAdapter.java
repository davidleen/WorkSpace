package com.giants3.reader.server.converter;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.List;

/**
 *   自定义集合 json 解析方式   使用集合中每个元素的具体类型
 */

public class ListTypeAdapter implements JsonSerializer<List<?>> {
    @Override
    public JsonElement serialize(List<?> src, Type typeOfSrc, JsonSerializationContext context) {


        JsonArray map = new JsonArray();


        for(Object o:src)
        {
            map.add(context.serialize(o, o.getClass()));
        }



        return map;
    }


}

package com.giants3.hd.server.repository_erp;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BlobType;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

/**
 * 数据库图片读取
 */

@Repository
public class ErpPhotoRepository  extends ErpRepository{




    public ErpPhotoRepository( ) {



    }


    public byte[] findProductPhotoByIdNo(String name) {


        Query query = getEntityManager().createNativeQuery("select photo_bom as photo from mf_bom where bom_no=:name");
        query.setParameter("name", name);
        List<BlobWrapper> list = query.unwrap(SQLQuery.class)
                .addScalar("photo", BlobType.INSTANCE).setResultTransformer(Transformers.aliasToBean(BlobWrapper.class)).list();

        if (list == null || list.size() == 0) return null;
        BlobWrapper object = list.get(0);


        final Blob photo = object.photo;
        byte[] bytes = null;
        try {
            bytes = photo.getBytes(1, (int) photo.length());

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                photo.free();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }


        return bytes;




    }


    public static class BlobWrapper {

        Blob photo;
    }
}

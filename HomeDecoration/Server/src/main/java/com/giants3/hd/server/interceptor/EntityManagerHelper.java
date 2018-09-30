package com.giants3.hd.server.interceptor;


import com.giants3.hd.server.repository.UserRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.logging.Level;

/**
 * Created by davidleen29 on 2015/8/24.
 */


public class EntityManagerHelper {


   private static EntityManagerHelper erp=null;

    public static final String PERSISTENCE_ERP="erpPersistenceUnit";
    public static final String PERSISTENCE_BACK="backPersistenceUnit";
    private static EntityManagerHelper back;
    // 实体化私有静态实体管理器变量emf
    private     EntityManagerFactory emf;

    // 实体化私有静态本地线程变量threadLocal
    private     ThreadLocal<EntityManager> threadLocal;
    // 用来给两个变量赋初值的静态块


    private EntityManagerHelper(String unitName)
    {

        emf = Persistence.createEntityManagerFactory(unitName);
        threadLocal = new ThreadLocal<EntityManager>();

    }


    public static EntityManagerHelper getErp()
    {

        if(erp==null)
        {

            erp=new EntityManagerHelper(PERSISTENCE_ERP);
        }
            return erp;

    }

    public static EntityManagerHelper getBack()
    {

        if(back==null)
        {

            back=new EntityManagerHelper(PERSISTENCE_BACK);
        }
        return back;

    }

    // 得到实体管理器的方法
    public   EntityManager getEntityManager() {
        EntityManager manager = threadLocal.get();
        if (manager == null || !manager.isOpen()) {
            manager = emf.createEntityManager();
            threadLocal.set(manager);
        }


        return manager;
    }
    // 关闭实体管理器的方法
    public   void closeEntityManager() {
        EntityManager em = threadLocal.get();
        threadLocal.set(null);
        if (em != null)
            em.close();
    }
    // 开始事务的方法
    public   void beginTransaction() {
        getEntityManager().getTransaction().begin();
    }
    // 提交事务的方法
    public   void commit() {
        getEntityManager().getTransaction().commit();
    }
    // 回滚事务的方法
    public   void rollback() {
        getEntityManager().getTransaction().rollback();
    }
    // 生成查找的方法
    public   Query createQuery(String query) {
        return getEntityManager().createQuery(query);
    }


    /**
     * how to use  entity manager
     */

    public void test()
    {
   //     EntityManagerHelper helper=EntityManagerHelper.getBack();
//
//
//
//
//
//        JpaRepositoryFactory jpaRepositoryFactory=new JpaRepositoryFactory(helper.getEntityManager());
//
//
//
//        PackRepository  backPackRepository=jpaRepositoryFactory.getRepository(PackRepository.class);
//
//        ProductRepository backProductRepository=jpaRepositoryFactory.getRepository(ProductRepository.class);
//        helper.beginTransaction();
//        Product deleteProduct= (Product) ObjectUtils.deepCopy(product);
//        deleteProduct.id=0;
//        deleteProduct.xiankang.setId(0);
//        Pack pack=deleteProduct.pack;
//        pack.id=0;
//        Pack newPack=backPackRepository.save(pack);
//        deleteProduct.pack=newPack;
//        Product newProduct=backProductRepository.save(deleteProduct);
//
//
//
//
//
//
//        helper.commit();
//
//        helper.closeEntityManager();
    }

}
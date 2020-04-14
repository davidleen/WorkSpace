package com.giants3.hd.server.repository_erp;

import com.giants3.hd.entity.Prdt;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.data.domain.Pageable;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
*  从第三方数据库读取prdt 数据相关
*/

@Repository
public   class ErpPrdtRepository  extends ErpRepository   {






    public   ErpPrdtRepository( ) {


    }




    //@Query(value = " SELECT  a    FROM PRDT a WHERE  a.prd_no like  :prd_no      " )
    public   List<Prdt> findByPrd_noEquals(@Param("prd_no") String prd_no,Pageable pageable)
    {

       CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
//
//
//        CriteriaQuery<Prdt> query = builder.createQuery(Prdt.class);
//        Root<Prdt> root = query.from(Prdt.class);
//
//        Join<Prdt, CST_STD>  joinTable=    root.join(CST_STD.class.getSimpleName(), JoinType.INNER);
//        joinTable.
//
//
//        Predicate hasBirthday = builder.equal(root.get("prd_no"), prd_no);
//        //Predicate isLongTermCustomer = builder.lessThan(root.get(Customer_.createdAt), today.minusYears(2);
//        query.where(builder.and(hasBirthday));//, isLongTermCustomer
//     return   em.createQuery(query.select(root)).getResultList();

// where e.prd_no = :prd_no   .setParameter(p,prd_no)
       // return em.createQuery("select  e  from PRDT e left join CST_STD  d fetch    e.prd_no = d.prd_no where e.prd_no=:prd_no ",Prdt.class) .setParameter("prd_no",prd_no).getResultList();
//        Specification<Prdt> prdtSpecification=new Prdt
//        listQuery()
//        TypedQuery query = em.createQuery("select a from Account a where a.customer = ?1", Account.class);
//        query.setParameter(1, customer);


     List result=   getEntityManager().createNativeQuery("select CAST(p.prd_no AS varchar) as prd_no  ,CAST(p.name AS varchar) as name ,CAST(p.ut AS varchar) as ut ,CAST(p.spc AS varchar) as  spec ,CAST(p.rem AS varchar) as rem ,cs.price , p.nouse_dd from (select * from  prdt  where prdt.knd='4') p inner join (select prd_no , up_std as price from CST_STD ) cs on p.prd_no=cs.prd_no  where p.prd_no = :prd_no " ).setParameter("prd_no",prd_no).getResultList();
     ///   return em.createQuery("select  e  from prdt e  ,(select f.prd_no,f.up_std from CST_STD f) d where e.prd_no=:prd_no ",Prdt.class) .setParameter("prd_no",prd_no).getResultList();
        return  convertToPojo(result);


    }




    public   List<Prdt> list( )
    {


        //找到唯一的单价记录    利用日期 最大值判断
        String sql_find_distinct_cst_std="select a.prd_no,a.UP_CST_ML as price from CST_STD a  inner join  (select distinct  prd_no ,MAX(SYS_DATE) as sys_date  from CST_STD  group by PRD_NO ) c on c.PRD_NO =a.PRD_NO  and c.sys_date=a.sys_date where UP_CST_ML>0  ";



        List result=   getEntityManager().createNativeQuery("select CAST(p.prd_no AS varchar) as prd_no ,CAST(p.name AS varchar) as name ,CAST(p.ut AS varchar) as ut ,CAST(p.spc AS varchar) as  spec ,CAST(p.rem AS varchar) as rem ,cs.price,p.nouse_dd from (select * from  prdt  where prdt.knd='4') p inner join ("+sql_find_distinct_cst_std+") cs on p.prd_no=cs.prd_no  " ).getResultList(); // where p.prd_no='22030001'


      return  convertToPojo(result);



    }

    public  List<Prdt> convertToPojo(List sqlResult)
    {

        List<Prdt> prdts=new ArrayList<>();
        Iterator it = sqlResult.iterator( );

        while (it.hasNext( )) {
            Object[] row = (Object[])it.next(); // Iterating through array object
            // prdts
            Prdt prdt=new Prdt();

            prdt.prd_no=row[0]==null?"":row[0].toString();

            prdt.name=row[1]==null?"" : row[1].toString();

            prdt.ut=row[2]==null?"":row[2].toString();

            prdt.spec=row[3]==null?"":row[3].toString();

            prdt.rem=row[4]==null?"":row[4].toString();

            prdt.price=row[5]==null?0: Float.valueOf(row[5].toString());


            prdt.nouse_dd=row[6]==null?0:((Timestamp)row[6]).getTime();

            prdts.add(prdt);


        }

        return prdts;

    }

    /**
     * 根据产品名称获取产品类型
     * @param prdNo
     * @return
     */
    public  String  findIdx1ByPrdno(String  prdNo)
    {

         Query query =   getEntityManager().createNativeQuery(" SELECT  idx1  FROM [DB_YF01].[dbo].prdt where knd='2' and prd_no ='"+prdNo.trim()+"'").unwrap(SQLQuery.class) ;

        return (String) (query .list().get(0));
    }

}

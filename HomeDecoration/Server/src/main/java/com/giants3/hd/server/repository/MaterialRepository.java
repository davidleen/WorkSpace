package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
* 材料
 *
*/
public interface MaterialRepository extends JpaRepository<Material,Long> {


    public Page<Material> findByCodeLikeOrNameLike(String code,String name ,Pageable pageable);
    public Page<Material> findByCodeLikeAndClassIdOrNameLikeAndClassIdEquals(String code, String classId,String name,String classId1,Pageable pageable);

    public Page<Material> findByCodeLikeAndOutOfServiceNotOrNameLikeAndOutOfServiceNot(String code,boolean outService,String name ,boolean outService1,Pageable pageable);
    public Page<Material> findByNameLike( String codeOrName,Pageable pageable);

    public Material findFirstByCodeEquals(String code);


    public Material findFirstByNameEquals(String name);



    public Material findFirstByClassIdEquals(String classId);

     List< Material> findByClassIdEquals(String classId);



}

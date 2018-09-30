package com.giants3.hd.server.service;

import com.giants3.hd.entity.*;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.server.repository.MaterialClassRepository;
import com.giants3.hd.server.repository.MaterialRepository;
import com.giants3.hd.server.repository.MaterialTypeRepository;
import com.giants3.hd.server.repository.PackMaterialClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by davidleen29 on 2018/6/11.
 */
@Service
public class MaterialRelateService extends AbstractService {
    @Autowired
    private MaterialRepository materialRepository;
    @Autowired
    private MaterialClassRepository materialClassRepository;
    @Autowired
    private MaterialTypeRepository materialTypeRepository;
    @Autowired
    private JpaRepository<MaterialEquation, Long> equationRepository;

    @Autowired
    private PackMaterialClassRepository packMaterialClassRepository;
    @Autowired
    private JpaRepository<Pack, Long> packMRepository;


    @Autowired
    private JpaRepository<PackMaterialType,Long> packMaterialTypeRepository;


    @Autowired
    private JpaRepository<PackMaterialPosition, Long> packMaterialPositionJpaRepository;

    @Transactional
    public RemoteData<MaterialClass> saveClassList(List<MaterialClass> materialClasses) {


        for (MaterialClass materialClass : materialClasses) {


            MaterialClass oldData = materialClassRepository.findFirstByCodeEquals(materialClass.code);
            if (oldData == null) {
                materialClass.id = -1;


            } else {
                materialClass.id = oldData.id;


            }
            materialClassRepository.save(materialClass);


        }


        return listClass();

    }


    public RemoteData<MaterialClass> listClass() {


        return wrapData(materialClassRepository.findAll());
    }


    /**
     * 删除材料类型类别
     *
     * @param materialClassId
     * @return
     */

    @Transactional
    public RemoteData<Void> deleteClass(long materialClassId) {


        MaterialClass materialClass = materialClassRepository.findOne(materialClassId);
        if (materialClass == null)
            return wrapError("材料类别不存在");


        Material material = materialRepository.findFirstByClassIdEquals(materialClass.code);
        if (material != null) {

            return wrapError("该材料类别正在使用，材料：" + material.code + "," + material.name + " ...等");
        }


        materialClassRepository.delete(materialClassId);


        return wrapData();
    }

    public List<MaterialType> listTypes() {


        return materialTypeRepository.findAll();
    }

    public List<MaterialEquation> listEquation() {


        return equationRepository.findAll();
    }

    public List<Pack> findAllPack() {


        return packMRepository.findAll();
    }

    public List<PackMaterialClass> listPackMaterialClasses() {

        return packMaterialClassRepository.findAll();
    }


    @Transactional
    public RemoteData<PackMaterialClass> savePackMaterialClassList(List<PackMaterialClass> materialClasses) {


        for (PackMaterialClass materialClass : materialClasses) {


            PackMaterialClass oldData = packMaterialClassRepository.findFirstByNameEquals(materialClass.name);
            if (oldData == null) {
                materialClass.id = -1;


            } else {
                materialClass.id = oldData.id;

            }
            packMaterialClassRepository.save(materialClass);

        }

        return wrapData(listPackMaterialClasses());
    }

    public List<PackMaterialPosition> listPackMaterialPositions() {

        return packMaterialPositionJpaRepository.findAll();
    }

    public List<PackMaterialType> listPackMaterialTypes() {


        return packMaterialTypeRepository.findAll();
    }
}

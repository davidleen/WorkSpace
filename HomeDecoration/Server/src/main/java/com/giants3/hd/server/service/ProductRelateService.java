package com.giants3.hd.server.service;

import com.giants3.hd.entity.*;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.server.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by davidleen29 on 2018/6/11.
 */
@Service
public class ProductRelateService extends AbstractService {
    @Autowired
    private ProductMaterialRepository productMaterialRepository;
    @Autowired
    private JpaRepository<PClass, Long> productClassRepository;
    @Autowired
    private ProductWageRepository productWageRepository;
    @Autowired
    private ProductPaintRepository productPaintRepository;
    @Autowired
    private ProductProcessRepository processRepository;
    @Autowired
    private XiankangRepository xiankangRepository;
    @Autowired
    private Xiankang_JingzaRepository xiankang_jingzaRepository;

    @Transactional
    public RemoteData<ProductProcess> saveProductProcessList(List<ProductProcess> productProcesses) {


        for (ProductProcess process : productProcesses) {


            ProductProcess oldData = processRepository.findOne(process.id);
            if (oldData == null) {
                process.id = -1;


                //如果是空数据  略过添加
                if (process.isEmpty()) {
                    continue;
                }


            } else {


                process.id = oldData.id;

            }

            processRepository.save(process);


        }


        return wrapData(processRepository.findAll());
    }

    public RemoteData<ProductProcess> listProductProcesses() {

        return wrapData(processRepository.findAll());
    }

    public RemoteData<ProductProcess> searchProductProcesses(String name, int pageIndex, int pageSize) {

        Pageable pageable = constructPageSpecification(pageIndex, pageSize);
        Page<ProductProcess> pageValue = processRepository.findByNameLikeOrCodeLike("%" + name + "%", "%" + name + "%", pageable);

        List<ProductProcess> products = pageValue.getContent();
        return wrapData(pageIndex, pageable.getPageSize(), pageValue.getTotalPages(), (int) pageValue.getTotalElements(), products);


    }

    public List<ProductMaterial> findProductMaterialsByProductIdFlow(long productId, int productFlow) {

        return productMaterialRepository.findByProductIdEqualsAndFlowIdEquals(productId, productFlow);
    }

    public List<PClass> listProductClasses() {


        return productClassRepository.findAll();
    }

    @Transactional
    public RemoteData<Void> updateXiankang() {


        //找出所有咸康数据
        List<Xiankang> xiankangs = xiankangRepository.findAll();

        for (Xiankang xiankang : xiankangs) {

            if (xiankang.xiankang_jingza == null) {


                Xiankang_Jingza newJingza = new Xiankang_Jingza();
                //复制转移的属性
                newJingza.setBeizhu(xiankang.getBeizhu());
                newJingza.setBoliguige_gao(xiankang.getBoliguige_gao());
                newJingza.setBoliguige_kuan(xiankang.getCaizhi());
                newJingza.setCaokuan(xiankang.getCaokuan());
                newJingza.setCaoshen(xiankang.getCaoshen());
                newJingza.setGuaju(xiankang.getGuaju());
                newJingza.setHuangui_gao(xiankang.getHuangui_gao());
                newJingza.setHuangui_kuan(xiankang.getHuangui_kuan());
                newJingza.setBiankuang(xiankang.getBiankuang());
                newJingza.setJingzi_gao(xiankang.getJingzi_gao());
                newJingza.setJingzi_kuan(xiankang.getJingzi_kuan());
                newJingza.setMobian(xiankang.getMobian());
                newJingza.setCaizhi(xiankang.getCaizhi());
                newJingza.setHuaxinbianhao(xiankang.getHuaxinbianhao());
                newJingza.setHuaxinxiaoguo(xiankang.getHuaxinxiaoguo());
                newJingza.setHuaxinchangshang(xiankang.getHuaxinchangshang());

                newJingza = xiankang_jingzaRepository.save(newJingza);
                xiankang.setXiankang_jingza(newJingza);


                xiankangRepository.save(xiankang);


            }
        }
        return wrapData();
    }

    public RemoteData<ProductProcess> updateProductProcesses(ProductProcess productProcess) {

        ProductProcess save = processRepository.save(productProcess);
        return wrapData(save);

    }

    public RemoteData<Void> deleteProductProcess(long processId) {


        //检查productprocess 相关的数据， 是否有关联，有关联不能删除


        ProductWage firstByProcessIdEquals = productWageRepository.findFirstByProcessIdEquals(processId);
        if(firstByProcessIdEquals!=null)
        {
            return wrapError("工资数据有关联到当前工序，不能删除!");
        }


        ProductPaint productPaint = productPaintRepository.findFirstByProcessIdEquals(processId);
        if(productPaint!=null)
        {
            return wrapError("油漆相关数据有关联到当前工序，不能删除!");
        }



        processRepository.delete(processId);
        return wrapMessageData("删除成功");
    }
}

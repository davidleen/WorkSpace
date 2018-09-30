package com.giants3.hd.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 生产流程
 * Created by davidleen29 on 2016/9/1.
 */
public class ErpWorkFlow {


    public static final String FIRST_STEP_CODE = "S";
    public static final String SECOND_STEP_CODE = "A";
    public static final String CODE_ZUZHUANG = "B";
    //生产完成状态码
        public static final int STATE_COMPLETE = 99;
    public static final String STATE_NAME_COMPLETE = "生产完成";
    //生产进行流程状态码
    public static final int STATE_WORKING = 9;


    public static final int PICTURE_COUNT = 3;
    public static final String CODE_CHENGPIN = "E";
    public static final String CODE_YANSE = "D";
    public static final String CODE_MU = "M";
    public static final String CODE_TIE = "T";
    public static final String CODE_BAOZHUANG = "C";
    public static String[] CODES = new String[]{FIRST_STEP_CODE, SECOND_STEP_CODE, CODE_YANSE, CODE_BAOZHUANG, CODE_CHENGPIN};


    public static String NAME_ZUZHUANG = "组装";
    public static String NAME_BAOZHUANG = "包装";
    private static final String NAME_CHENGPIN = "成品仓";
    public  static final String NAME_BAIPEI = "白胚";
    public static final String NAME_YANSE = "颜色";
    public static String[] NAMES = new String[]{"胚体加工", NAME_BAIPEI, NAME_YANSE, NAME_ZUZHUANG + NAME_BAOZHUANG, NAME_CHENGPIN};
    public static final int FIRST_STEP = 1000;
    public static final int LAST_STEP = 6000;
    public static final int STEP_PEITI = 2000;
    public static final int STEP_CHENGPIN = LAST_STEP;
    public static final int STEP_YANSE = 3000;
    public static final int STEP_BAOZHUANG = 5000;
    public static int[] STEPS = new int[]{FIRST_STEP, STEP_PEITI, STEP_YANSE, STEP_BAOZHUANG, LAST_STEP};


    public ErpWorkFlow() {

    }

    public String code;
    public String name;
    public int step;


    public static List<ErpWorkFlow> WorkFlows;

    public static List<ErpWorkFlow> erpRealWorkFlows;

    static {
        int count = CODES.length;
        WorkFlows = new ArrayList<>(count);
        erpRealWorkFlows = new ArrayList<>(count - 2);
        for (int i = 0; i < count; i++) {

            ErpWorkFlow workFlow;
            workFlow = new ErpWorkFlow();
            workFlow.code = CODES[i];
            workFlow.name = NAMES[i];
            workFlow.step = STEPS[i];
            WorkFlows.add(workFlow);
            if (i > 0 && i < count - 1)
                erpRealWorkFlows.add(workFlow);

        }


    }


    @Override
    public String toString() {
        return code + "    " + name;
    }

    public static ErpWorkFlow findByStep(int flowStep ) {

            for (ErpWorkFlow workFLow :
                    WorkFlows) {
                if (workFLow.step == flowStep) return workFLow;
            }
            return null;

    }

    public static ErpWorkFlow findByCode(String flowCode) {


            for (ErpWorkFlow workFLow :
                    WorkFlows) {
                if (workFLow.code.equals(flowCode)) return workFLow;
            }
            return null;

    }


    public static int findIndexByCode(String code ) {



            final int length = CODES.length;
            int index = length - 1;

            for (int i = 0; i < length; i++) {
                if (CODES[i].equals(code))
                    return i;
            }
            return index;


    }

    public static int findPrevious(int flowStep ) {


            final int length = STEPS.length;


            for (int i = 1; i < length; i++) {
                if (STEPS[i] == flowStep)
                    return STEPS[i - 1];
            }
            return -1;

    }


    public static List<ErpWorkFlow> purchaseWorkFLows;

    static {
        purchaseWorkFLows = new ArrayList<>();
        ErpWorkFlow workFlow;
        workFlow = new ErpWorkFlow();
        workFlow.code = ErpWorkFlow.FIRST_STEP_CODE;
        workFlow.name = "成品制作";
        workFlow.step = ErpWorkFlow.FIRST_STEP;
        purchaseWorkFLows.add(workFlow);


        workFlow = new ErpWorkFlow();
        workFlow.code = ErpWorkFlow.CODE_CHENGPIN;
        workFlow.name = ErpWorkFlow.NAME_CHENGPIN;
        workFlow.step = ErpWorkFlow.STEP_CHENGPIN;
        purchaseWorkFLows.add(workFlow);


    }



    public static ErpWorkFlow findPurchaseNext(int workflowStep)
    {


        final int size = purchaseWorkFLows.size();
        for (int i = 0; i < size; i++) {
            ErpWorkFlow erpWorkFlow = purchaseWorkFLows.get(i);

            if (erpWorkFlow.step == workflowStep) {

                if(i+1<size)
                {
                    return purchaseWorkFLows.get(i+1);
                }


            }
        }

        return null;

    }

    public static int findPurchasePrevious(int flowStep ) {


        final int length = purchaseWorkFLows.size();


        for (int i = 1; i < length; i++) {
            if (purchaseWorkFLows.get(i).step == flowStep)
                return STEPS[i - 1];
        }
        return -1;

    }

    public static ErpWorkFlow findPurchaseByStep(int flowStep) {

        for (ErpWorkFlow workFLow :
                purchaseWorkFLows) {
            if (workFLow.step == flowStep) return workFLow;
        }
        return null;
    }
}

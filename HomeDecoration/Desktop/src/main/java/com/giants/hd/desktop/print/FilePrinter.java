package com.giants.hd.desktop.print;

import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;
import java.io.File;
import java.io.FileInputStream;

/**
 * Created by davidleen29 on 2018/4/8.
 */
public class FilePrinter {

    private final String fileName;
    private final int count;

    public FilePrinter(String fileName, int count) {

        this.fileName = fileName;
        this.count = count;
    }


    public void doPrint() {


        File file = new File(fileName);


        DocFlavor docFlavor = null;
        if (fileName.endsWith(".gif")) {
            docFlavor = DocFlavor.INPUT_STREAM.GIF;
        } else if (fileName.endsWith(".jpg")) {
            docFlavor = DocFlavor.INPUT_STREAM.JPEG;
        } else if (fileName.endsWith(".png")) {
            docFlavor = DocFlavor.INPUT_STREAM.PNG;
        }else
        if (fileName.endsWith(".pdf")) {
            docFlavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
        }
        // 构建打印请求属性集

        HashPrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();


        // 查找所有的可用的打印服务
        PrintService printService[] = PrintServiceLookup.lookupPrintServices(docFlavor, pras);
        System.out.println("-------------------All services-------------------");
        for (int i = 0; i < printService.length; i++) {
            System.out.println(printService[i]);
        }

        // 定位默认的打印服务

        //PrintService service1 = PrintServiceLookup.lookupDefaultPrintService();
        if (printService.length > 0) {

            System.out.println("-------------------Choose Printer-------------------");
            System.out.println(printService[0]);
            //指定使用 Microsoft XPS Document Writer
            PrintService service = printService[1];

            //获取打印机属性
            AttributeSet attributes = service.getAttributes();
            for (Attribute a : attributes.toArray()) {
                String name = a.getName();
                String value = attributes.get(a.getClass()).toString();
                System.out.println(name + " : " + value);
            }
            // 显示打印对话框

            //PrintService service = ServiceUI.printDialog(null, 200, 200, printService,

            //    service1, flavor, pras);

//            service=PrintServiceLookup.lookupDefaultPrintService();



            service = ServiceUI.printDialog(null, 200, 200, printService,
               service, docFlavor, pras);
                      System.out.println("-------------------Choose Printer-------------------");
               System.out.println(service);
            if (service != null) {

                try {
                    System.out.println("Begin Print PDF: " +fileName+",exists:"+file.exists()+",fileSize="+file.length());
                    DocPrintJob job = service.createPrintJob(); // 创建打印作业

                    FileInputStream fis = new FileInputStream(file); // 构造待打印的文件流

                    DocAttributeSet das = new HashDocAttributeSet();
                    //请求一个彩色打印机
//                    pras.add(Chromaticity.COLOR);

                    //请求横向模式
//                    pras.add(OrientationRequested.LANDSCAPE);

                    //美国Letter大小的纸质属性
//                    pras.add(MediaSizeName.NA_LETTER);

                    // European A4 paper
                     pras.add(MediaSizeName.ISO_A4);

                    //请求装订
//                    pras.add(Finishings.STAPLE);

                    //整理多个副本
//                    pras.add(SheetCollate.COLLATED);

                    //请求双面
//                    pras.add(Sides.DUPLEX);

                    // 2页到一个工作表
//                    pras.add(new NumberUp(2));

                    //多少个副本
//                    pras.add(new Copies(2));

                    Doc doc = new SimpleDoc(fis, docFlavor, das);

                    job.print(doc, pras);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

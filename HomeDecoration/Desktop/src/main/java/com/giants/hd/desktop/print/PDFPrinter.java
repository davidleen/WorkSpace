//package com.giants.hd.desktop.print;
//
//import java.awt.print.PrinterException;
//import java.awt.print.PrinterJob;
//import java.io.File;
//import java.io.IOException;
//
///**
// * Created by davidleen29 on 2018/7/27.
// */
//public class PDFPrinter {
//
//    public static void print(String filePath)
//    {
//        try {
//            PDDocument document = PDDocument.load(new File(filePath));
//            PrinterJob job = PrinterJob.getPrinterJob();
//            job.setPageable(new PDFPageable(document));
//            if (job.printDialog()){
//                job.print();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (PrinterException e) {
//            e.printStackTrace();
//        }
//    }
//}

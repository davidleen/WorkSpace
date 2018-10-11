package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.local.LocalFileHelper;
import com.giants.hd.desktop.utils.QRHelper;
import com.giants3.hd.entity.Product;
import com.giants3.hd.noEntity.QRProduct;
import com.giants3.hd.utils.FileUtils;
import com.giants3.report.jasper.ProductWithQR;
import com.giants3.report.jasper.QrProductReport;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ProductQRDialog extends BaseDialog<Product> {
    private final Window window;
    private final Product product;
    private JPanel contentPane;
    private JLabel iv_qr;
    private JButton a4Print;
    private JButton barPrint;
    private JButton barPrintLand;
    BufferedImage scaledImg;

    String qrLocalPath;

    public ProductQRDialog(Window window,final Product product) {
        super(window, "二维码");
        this.window = window;
        this.product = product;

        qrLocalPath= LocalFileHelper.path+"/qr/temp.png";

        setContentPane(contentPane);


        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                QRProduct qrProduct=  QRHelper.generate(product);


                int qrSize=300;

                  scaledImg = QRHelper.generateQRCode(qrProduct,qrSize, qrSize) ;
                iv_qr.setIcon(new ImageIcon( scaledImg));


            }
        });




        a4Print.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            //保存qrCode 到本地临时文件


                try {
                    ProductWithQR productWithQR = getProductWithQR(product);
                    new QrProductReport(productWithQR, "qrproducta4",3*6).report();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(ProductQRDialog.this,e1.getMessage());
                }




            }
        });


        barPrint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    ProductWithQR productWithQR = getProductWithQR(product);
                    new QrProductReport(productWithQR, "qrproduct",1).report();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(ProductQRDialog.this,e1.getMessage());
                }
            }
        });



        barPrintLand.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    ProductWithQR productWithQR = getProductWithQR(product);
                    new QrProductReport(productWithQR, "qrproduct_land",1).report();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(ProductQRDialog.this,e1.getMessage());
                }
            }
        });

        setModal(false);
    }

    public ProductWithQR getProductWithQR(Product product) throws IOException {
        FileUtils.makeDirs(qrLocalPath);
        ImageIO.write(scaledImg, "png",new File( qrLocalPath));


        ProductWithQR productWithQR=new ProductWithQR();
        productWithQR.product=product;
        productWithQR.qrFilePath=qrLocalPath;
        return productWithQR;
    }


}

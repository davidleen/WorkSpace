//package com.giants3.android.zxing;
//
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//
//import com.google.zxing.BarcodeFormat;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class SimpleScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
//
//    private static final String TAG = "INDIGO";
//    private ZXingScannerView mScannerView;
//
//    public static String SCANNER_GIVE_RESULT = "SCANNER_GIVE_RESULT";
//    public static String SCANNER_RESULT = "SCANNER_RESULT";
//
//    private boolean returnResult;
//
//    @Override
//    public void onCreate(Bundle state) {
//        super.onCreate(state);
//        mScannerView = new ZXingScannerView(this) {
//
//            @Override
//            protected IViewFinder createViewFinderView(Context context) {
//                return new CustomZXingScannerView(context);
//            }
//
//        };    // Programmatically initialize the scanner view
//        List<BarcodeFormat> formats = new ArrayList<>();
//        formats.add(BarcodeFormat.QR_CODE);
//        setContentView(mScannerView);                // Set the scanner view as the content view
//
//        returnResult = getIntent().getBooleanExtra(SCANNER_GIVE_RESULT, false);
//    }
//}
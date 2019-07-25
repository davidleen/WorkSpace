package com.rnmap_wb.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.giants3.android.ToastHelper;
import com.giants3.android.frame.util.StringUtil;
import com.giants3.android.reader.domain.GsonUtils;
import com.rnmap_wb.LatLngUtil;
import com.rnmap_wb.R;
import com.rnmap_wb.activity.mapwork.SimpleMvpActivity;
import com.rnmap_wb.entity.MapElement;
import com.rnmap_wb.helper.AndroidRouter;
import com.rnmap_wb.helper.AndroidUtils;
import com.rnmap_wb.helper.CapturePictureHelper;
import com.rnmap_wb.helper.ImageLoaderFactory;
import com.rnmap_wb.utils.IntentConst;

import org.osmdroid.util.GeoPoint;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class AddMarkActivity extends SimpleMvpActivity implements View.OnClickListener, AndroidRouter {


    @Bind(R.id.name)
    EditText name;
    @Bind(R.id.lat)
    EditText lat;
    @Bind(R.id.lng)
    EditText lng;
    @Bind(R.id.memo)
    EditText memo;
    @Bind(R.id.confirm)
    View confirm;
    @Bind(R.id.cancel)
    View cancel;

    @Bind(R.id.picture1)
    ImageView picture1;

    @Bind(R.id.picture2)
    ImageView picture2;

    @Bind(R.id.picture3)
    ImageView picture3;
    @Bind(R.id.pictureGroup1)
    View pictureGroup1;

    @Bind(R.id.pictureGroup2)
    View pictureGroup2;

    @Bind(R.id.pictureGroup3)
    View pictureGroup3;
    @Bind(R.id.delete1)
    ImageView delete1;

    @Bind(R.id.delete2)
    ImageView delete2;

    @Bind(R.id.delete3)
    ImageView delete3;
    @Bind(R.id.addImage)
    View addImage;


    ImageView[] pictures;
    ImageView[] deletes;
    View[] pictureGroups;


    public List<String> urls = new ArrayList<>();
    public List<String> localFilePaths = new ArrayList<>();

    GeoPoint latLng;

    MapElement mapElement;
    CapturePictureHelper capturePictureHelper;

    public static void start(Activity activity, MapElement mapElement, int requestCode) {


        Intent intent = new Intent(activity, AddMarkActivity.class);
        intent.putExtra(IntentConst.KEY_MAP_ELEMENT, GsonUtils.toJson(mapElement));
        activity.startActivityForResult(intent, requestCode);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getNavigationController().setLeftView(R.drawable.icon_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        latLng = getIntent().getParcelableExtra(IntentConst.KEY_LATLNG);
        mapElement = GsonUtils.fromJson(getIntent().getStringExtra(IntentConst.KEY_MAP_ELEMENT), MapElement.class);
        getNavigationController().setTitle(mapElement != null ? "反馈" : "添加标记");
        pictures = new ImageView[]{picture1, picture2, picture3};
        pictureGroups = new View[]{pictureGroup1, pictureGroup2, pictureGroup3};
        deletes = new ImageView[]{delete1, delete2, delete3};

        addImage.setOnClickListener(this);
        if (latLng != null) {
            lat.setText(String.valueOf(latLng.getLatitude()));
            lng.setText(String.valueOf(latLng.getLongitude()));
        }


        if (mapElement != null) {
            List<GeoPoint> latLngs = LatLngUtil.convertStringToGeoPoints(mapElement.latLngs);
            lat.setText(String.valueOf(latLngs.get(0).getLatitude()));
            lng.setText(String.valueOf(latLngs.get(0).getLongitude()));
            name.setText(mapElement.name);
            memo.setText(mapElement.memo);

            String[] pics = mapElement.picture == null ? null : mapElement.picture.split(MapElement.PICTURE_REGEX);
            if (pics != null)
                for (String url : pics)
                    if (!StringUtil.isEmpty(url))
                        urls.add(url);
            String[] paths = mapElement.filePath == null ? null : mapElement.filePath.split(MapElement.PICTURE_REGEX);
            if (paths != null)
                for (String path : paths) {
                    if (!StringUtil.isEmpty(path))
                        localFilePaths.add(path);
                }


        }

        handleImageLayouts();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (mapElement == null) {
                    mapElement = new MapElement();
                    mapElement.type = MapElement.TYPE_MARKER;
                }

                mapElement.picture = StringUtil.toString(MapElement.PICTURE_REGEX, urls);
                mapElement.filePath = StringUtil.toString(MapElement.PICTURE_REGEX, localFilePaths);
                mapElement.name = name.getText().toString();
                mapElement.latLngs = lat.getText().toString().trim() + "," + lng.getText().toString().trim();

                mapElement.memo = memo.getText().toString().trim();
                Intent intent = new Intent();
                intent.putExtra(IntentConst.KEY_MAP_ELEMENT, GsonUtils.toJson(mapElement));
                setResult(RESULT_OK, intent);
                finish();


            }
        });
        capturePictureHelper = new CapturePictureHelper(this, new CapturePictureHelper.OnPictureGetListener() {


            @Override
            public void onPictureFileGet(String filePath) {
                File newPath = new File(AndroidUtils.getCacheDir(), Calendar.getInstance().getTimeInMillis() + "");

                File tempFile = new File(filePath);
                tempFile.renameTo(newPath);
                localFilePaths.add(newPath.getAbsolutePath());

                handleImageLayouts();


            }
        });

        View.OnClickListener deleteOnclickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int removeIndex = -1;

                switch (v.getId()) {
                    case R.id.delete1:
                        removeIndex = 0;


                        break;
                    case R.id.delete2:
                        removeIndex = 1;
                        break;
                    case R.id.delete3:
                        removeIndex = 2;
                        break;
                }


                if (removeIndex > -1) {
                    if (urls.size() > removeIndex )
                        urls.remove(removeIndex);
                    if (localFilePaths.size() > removeIndex)
                        localFilePaths.remove(removeIndex);
                }
                handleImageLayouts();

            }
        };
        for (View delete : deletes) {
            delete.setOnClickListener(deleteOnclickListener);
        }

    }

    private void handleImageLayouts() {


        int urlSize = urls.size();
        int filePathSize = localFilePaths.size();
        for (int i = 0; i < pictureGroups.length; i++) {
            View pictureGroup = pictureGroups[i];
            ImageView picture = pictures[i];

            String url = i < urlSize ? urls.get(i) : null;
            String filePath = i < filePathSize ? localFilePaths.get(i) : null;

            pictureGroup.setVisibility(View.VISIBLE);
            if (!StringUtil.isEmpty(url)) {

                ImageLoaderFactory.getInstance().displayImage(url, picture);
            } else if (!StringUtil.isEmpty(filePath)) {

                ImageLoaderFactory.getInstance().displayImage(filePath, picture);
            } else {
                pictureGroup.setVisibility(View.GONE);
            }


        }


        addImage.setVisibility(urlSize >= 3 || filePathSize >= 3 ? View.GONE : View.VISIBLE);


    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_add_marker;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.addImage:

                AddMarkActivityPermissionsDispatcher.openCameraWithPermissionCheck(this);


                break;
        }
    }


    @NeedsPermission({Manifest.permission.CAMERA})
    public void openCamera() {
        capturePictureHelper.pickFromCamera(true);

    }


    @OnShowRationale({Manifest.permission.CAMERA})
    void showRationaleForCamera(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.permission_camera)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();

                    }
                })
                .show();
    }

    @OnNeverAskAgain(value = {Manifest.permission.CAMERA})
    void showNeverAskForCamera() {
        ToastHelper.show(R.string.permission_camera_neverask);

    }

    @OnPermissionDenied({Manifest.permission.CAMERA})
    void showDeniedForCamera() {
        AddMarkActivityPermissionsDispatcher.openCameraWithPermissionCheck(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        capturePictureHelper.onActivityResult(requestCode, resultCode, data);
    }
}

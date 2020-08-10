package com.dlf.contentprovider;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class MainActivity extends AppCompatActivity implements EasyPermissions.RationaleCallbacks, EasyPermissions.PermissionCallbacks, View.OnClickListener{

    private static final int REQUEST_FILE_CODE = 67;
    private Button mClick1;
    private Button mClick2;
    private Button mClick3;
    private Button mClick4;
    private Button mClick5;

    String[] permissions = {Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_PHONE_NUMBERS,
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE};
    private TextView te;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        // 判断有没有这些权限
        if (EasyPermissions.hasPermissions(this, permissions)) {

            initView();

        } else {

            EasyPermissions.requestPermissions(
                    new PermissionRequest.Builder(this, REQUEST_FILE_CODE, permissions)
                            .setRationale("请确认相关权限！！")
                            .setPositiveButtonText("ok")
                            .setNegativeButtonText("cancal")
                            .build());
        }
    }

    private void initView() {
        mClick1 = (Button) findViewById(R.id.click1);
        mClick2 = (Button) findViewById(R.id.click2);
        mClick3 = (Button) findViewById(R.id.click3);
        mClick4 = (Button) findViewById(R.id.click4);
        mClick5 = (Button) findViewById(R.id.click5);
        te = findViewById(R.id.te);

        mClick1.setOnClickListener(this);
        mClick2.setOnClickListener(this);
        mClick3.setOnClickListener(this);
        mClick4.setOnClickListener(this);
        mClick5.setOnClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    private static final String TAG = "MainActivity";

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        //同意授权
        initView();

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        // 拒绝授权

        Log.d(TAG, "onPermissionsDenied: ");
    }

    @Override
    public void onRationaleAccepted(int requestCode) {
        //同意授权
    }

    @Override
    public void onRationaleDenied(int requestCode) {
        //拒绝授权
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.click1:
                // TODO 获取通讯录
                getPhoneNumber();
                break;
            case R.id.click2:
                // TODO 短信
                getSMS();
                break;
            case R.id.click3:
                // TODO 图片
                getPhoto();
                break;
            case R.id.click4:
                // TODO 19/10/12
                getAudio();
                break;
            case R.id.click5:// TODO 19/10/12
                getVideo();
                break;
        }
    }

    /**
     * 获取视频
     */
    private void getVideo() {
        Cursor cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        StringBuilder stringBuilder = new StringBuilder();

        while (cursor.moveToNext()){

            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
            stringBuilder.append("video--path=="+path+"\n");
        }

        Log.d("TAG", "getVideo: "+stringBuilder.toString());
        te.setText(stringBuilder.toString());

    }

    /**
     * 获取音频
     */
    private void getAudio() {

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, null, null, null);
        StringBuilder stringBuilder = new StringBuilder();

        while (cursor.moveToNext()){

            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            stringBuilder.append("audio--path=="+path+"\n");

        }

        Log.d("TAG", "getAudio: "+stringBuilder.toString());
        te.setText(stringBuilder.toString());

    }

    private void getPhoto() {
        // 获取文件的数据

        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, null, null, null);
        StringBuilder stringBuilder = new StringBuilder();

        while (cursor.moveToNext()){
            // 图片的地址
            String imgPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

            stringBuilder.append(imgPath+"\n");
        }

        Log.d("TAG", "getPhoto: "+stringBuilder.toString());
        te.setText(stringBuilder.toString());

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getSMS() {
        //获取系统短信应用的数据

        Cursor cursor = getContentResolver().query(Telephony.Sms.CONTENT_URI,
                null, null, null, null);
        StringBuilder stringBuilder = new StringBuilder();

        while (cursor.moveToNext()){

            String sendNumber = cursor.getString(cursor.getColumnIndex(Telephony.Sms.ADDRESS));
            String content = cursor.getString(cursor.getColumnIndex(Telephony.Sms.BODY));

            stringBuilder.append("address="+sendNumber+"--content="+content+"\n");
        }

        Log.d("TAG", "getSMS: "+stringBuilder.toString());
        te.setText(stringBuilder.toString());

    }

    private void getPhoneNumber() {

        // 获取系统的通讯录，相当于 当前程序  通过uri  来获取系统通讯录的数据库数据

        // 内容解析者
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null);

        StringBuilder stringBuilder = new StringBuilder();
        while (cursor.moveToNext()) {

            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            phoneNumber = phoneNumber.substring(0, 6);

            stringBuilder.append("name=" + name + "--phone=" + phoneNumber + "\n");
        }


        Log.d("TAG", "getPhoneNumber: " + stringBuilder);
        te.setText(stringBuilder.toString());

    }
}

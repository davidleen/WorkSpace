package com.rnmap_wb.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.giants3.android.ToastHelper;
import com.giants3.android.frame.util.Log;
import com.giants3.android.frame.util.StringUtil;
import com.giants3.android.reader.domain.DefaultUseCaseHandler;
import com.giants3.android.reader.domain.ResApiFactory;
import com.giants3.android.reader.domain.UseCaseFactory;
import com.rnmap_wb.BuildConfig;
import com.rnmap_wb.R;
import com.rnmap_wb.activity.mapwork.SimpleMvpActivity;
import com.rnmap_wb.android.data.LoginResult;
import com.rnmap_wb.android.data.RemoteData;
import com.rnmap_wb.url.DigestUtils;
import com.rnmap_wb.url.HttpUrl;
import com.rnmap_wb.utils.IntentConst;
import com.rnmap_wb.utils.SessionManager;

import butterknife.Bind;

/**
 *
 */
public class LoginActivity extends SimpleMvpActivity implements View.OnClickListener {


    public static final int REQUEST_REGISTER = 111;
    @Bind(R.id.login)
    View login;
    @Bind(R.id.register)
    View register;


    @Bind(R.id.email)
    EditText email;

    @Bind(R.id.password)
    EditText password;

    public static void start(Activity activity, int requestLogin) {

        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivityForResult(intent, requestLogin);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getNavigationController().setTitle("登录");


        if (BuildConfig.DEBUG) {
            email.setText("admin@admin.com");
            password.setText("111111");
        }

        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void onClick(View v) {

        String emailString = email.getText().toString().trim();
        switch (v.getId()) {


            case R.id.login:


                String passwordString = password.getText().toString().trim();


                if (StringUtil.isEmpty(emailString)) {

                    ToastHelper.show("请输入邮箱");
                    email.requestFocus();
                    return;
                }
                if (StringUtil.isEmpty(passwordString)) {

                    ToastHelper.show("请输入密码");
                    password.requestFocus();
                    return;
                }
                String passwordMd5 = DigestUtils.md5(passwordString);

                doLogin(emailString, passwordString);


                break;
            case R.id.register:


                Intent intent = new Intent(this, RegisterActivity.class);
                intent.putExtra(IntentConst.KEY_EMAIL, emailString);
                startActivityForResult(intent, REQUEST_REGISTER);


                break;


        }

    }

    private void doLogin(String emailString, String passwordString) {
        showWaiting();
        UseCaseFactory.getInstance().createPostUseCase(HttpUrl.getLogin(emailString, passwordString), LoginResult.class).execute(new DefaultUseCaseHandler<RemoteData<LoginResult>>() {


            @Override
            public void onError(Throwable e) {
                hideWaiting();
                showMessage(e.getMessage());
                Log.e(e);
            }

            @Override
            public void onNext(RemoteData<LoginResult> remoteData) {
                hideWaiting();

                if (remoteData.isSuccess()) {
                    HttpUrl.token = remoteData.data.get(0).token;
                    SessionManager.saveLoginUser(LoginActivity.this, remoteData.data
                            .get(0));
                    ResApiFactory.getInstance().getResApi().setHeader("x-token", HttpUrl.token);
                    setResult(Activity.RESULT_OK);

                    finish();
                } else
                    ToastHelper.show(remoteData.errmsg);
            }


        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_REGISTER:

                String email = data.getStringExtra(IntentConst.KEY_EMAIL);
                String password = data.getStringExtra(IntentConst.KEY_PASSWORD);

                showMessage("自动登录");
                doLogin(email, password);

                break;
        }


    }
}

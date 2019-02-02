package com.rnmap_wb.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.giants3.android.frame.util.Log;
import com.giants3.android.frame.util.StringUtil;
import com.giants3.android.reader.domain.DefaultUseCaseHandler;
import com.giants3.android.reader.domain.UseCaseFactory;
import com.rnmap_wb.BuildConfig;
import com.rnmap_wb.R;
import com.rnmap_wb.activity.mapwork.SimpleMvpActivity;
import com.rnmap_wb.android.data.RemoteData;
import com.rnmap_wb.android.data.VerifyCodeResult;
import com.rnmap_wb.url.HttpUrl;
import com.rnmap_wb.utils.IntentConst;

import butterknife.Bind;

public class RegisterActivity extends SimpleMvpActivity implements View.OnClickListener {

    @Bind(R.id.register)
    View register;
    @Bind(R.id.getCode)
    View getCode;

    @Bind(R.id.email)
    EditText email;
    @Bind(R.id.verifyCode)
    EditText verifyCode;
    @Bind(R.id.password)
    EditText password;
    @Bind(R.id.password2)
    EditText password2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getNavigationController().setTitle(getString(R.string.register));
        getCode.setOnClickListener(this);
        register.setOnClickListener(this);

        String para_email = getIntent().getStringExtra(IntentConst.KEY_EMAIL);
        if (!StringUtil.isEmpty(para_email))
            email.setText(para_email);

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void onClick(View v) {
        String emailString = email.getText().toString().trim();
        switch (v.getId()) {
            case R.id.register:


                String verifyCodeString = verifyCode.getText().toString().trim();
                String passwordString = password.getText().toString().trim();
                String password2String = password2.getText().toString().trim();
                if (StringUtil.isEmpty(emailString)) {

                    showMessage("请输入邮箱");
                    email.requestFocus();
                    return;
                }
                if (!checkEmail(emailString)) {
                    showMessage("邮箱地址格式不正确");
                    email.requestFocus();
                    return;
                }
                if (StringUtil.isEmpty(verifyCodeString)) {

                    showMessage("请输入验证码");
                    verifyCode.requestFocus();
                    return;
                }
                if (StringUtil.isEmpty(passwordString)) {

                    showMessage("请输入密码");
                    password.requestFocus();
                    return;
                }
                if (StringUtil.isEmpty(password2String)) {

                    showMessage("请再输入一次密码");
                    password2.requestFocus();
                    return;
                }
                if (!password2String.equals(passwordString)) {
                    showMessage("两次密码输入不一致");
                    password2.requestFocus();
                    return;
                }
                register(emailString, verifyCodeString, passwordString, password2String);


                break;
            case R.id.getCode:


                if (StringUtil.isEmpty(emailString)) {

                    showMessage("请输入邮箱");
                    email.requestFocus();
                    return;
                }
                if (!checkEmail(emailString)) {
                    showMessage("邮箱地址格式不正确");
                    email.requestFocus();
                    return;
                }

                getVerifyCode(emailString);


                break;
        }

    }

    private boolean checkEmail(String em) {
        if (em.contains("@") && em.contains(".")) {
            return true;
        }
        return false;
    }

    private void getVerifyCode(String emailString) {
        showWaiting();

        UseCaseFactory.getInstance().createPostUseCase(HttpUrl.getVerifyCode(emailString), VerifyCodeResult.class).execute(new DefaultUseCaseHandler<RemoteData<VerifyCodeResult>>() {


            @Override
            public void onError(Throwable e) {
                hideWaiting();
                showMessage(e.getMessage());
                Log.e(e);
            }

            @Override
            public void onNext(RemoteData<VerifyCodeResult> remoteData) {
                hideWaiting();

                if (remoteData.isSuccess()) {
                    showMessage("验证码已经发送成功");
                    if (BuildConfig.DEBUG&&remoteData.data!=null&&remoteData.data.size()>0) {
                        VerifyCodeResult result = remoteData.data.get(0);

                        verifyCode.setText(result.code);

                    }
                } else
                    showMessage(remoteData.errmsg);
            }


        });
    }

    private void register(final String email, String code, final String password, String password2) {


        showWaiting();

        UseCaseFactory.getInstance().createPostUseCase(HttpUrl.register(email, code, password, password2), Void.class).execute(new DefaultUseCaseHandler<RemoteData<Void>>() {


            @Override
            public void onError(Throwable e) {
                hideWaiting();
                showMessage(e.getMessage());
                Log.e(e);
            }

            @Override
            public void onNext(RemoteData<Void> remoteData) {
                hideWaiting();

                if (remoteData.isSuccess()) {
                    showMessage("注册成功");
                    Intent intent = new Intent();
                    intent.putExtra(IntentConst.KEY_EMAIL, email);
                    intent.putExtra(IntentConst.KEY_PASSWORD, password);
                    setResult(Activity.RESULT_OK,intent);

                    finish();
                } else
                    showMessage(remoteData.errmsg);
            }


        });


    }
}

package com.agl.product.adw8_new.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.agl.product.adw8_new.ActivityBase;
import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.model.PermissionData;
import com.agl.product.adw8_new.model.Usuario;
import com.agl.product.adw8_new.retrofit.ApiClient;
import com.agl.product.adw8_new.service.Post;
import com.agl.product.adw8_new.service.data.RequestDataAddCient;
import com.agl.product.adw8_new.service.data.RequestDataLogin;
import com.agl.product.adw8_new.service.data.ResponseDataAddCient;
import com.agl.product.adw8_new.service.data.ResponseDataLogin;
import com.agl.product.adw8_new.utils.Session;
import com.agl.product.adw8_new.utils.Utils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends ActivityBase implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    public String TAG = "LoginActivity";
    private final int RC_SIGN_IN = 0x1;
    GoogleApiClient mGoogleApiClient;
    SignInButton signInButton;
    Button loginButton;
    EditText emailEditText, passwordEditText;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        session = new Session(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        setTitle(getResources().getString(R.string.login_title));
        toolbar.setTitleTextColor(Color.WHITE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInButton = (SignInButton) findViewById(R.id.btn_sign_in_google);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        loginButton = (Button) findViewById(R.id.login_btn);
        emailEditText = (EditText) findViewById(R.id.editText_email);
        passwordEditText = (EditText) findViewById(R.id.editText_password);

        loginButton.setOnClickListener(this);
        signInButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in_google:
                signIn();
                break;

            case R.id.login_btn:
                emailEditText.clearFocus();
                passwordEditText.clearFocus();
                Utils.closeKeyboard(this, v);
                if(rotinaErro()) {
                    if(Utils.isNetworkAvailable(this)) {
                        signInButton.setEnabled(false);
                        serviceLogin(emailEditText.getText().toString().trim(), passwordEditText.getText().toString().trim());
                    } else {
                        Snackbar.make(v, getResources().getString(R.string.no_internet), Snackbar.LENGTH_SHORT).show();
                    }
                }
                break;

            default:
                break;
        }
    }

    private boolean rotinaErro() {
        if(!validateEmail()) {
            return false;
        }

        if(!validatePassword()) {
            return false;
        }

        return true;
    }

    private boolean validateEmail() {
        if (emailEditText.getText().toString().trim().isEmpty()) {
            emailEditText.setError(getResources().getString(R.string.digiteEmail));
            requestFocus(emailEditText);
            return false;
        } else if(!isValidEmail(emailEditText.getText().toString().trim())) {
            emailEditText.setError(getResources().getString(R.string.digiteValidEmail));
            requestFocus(emailEditText);
            return false;
        } else {
            emailEditText.setError(null);
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validatePassword() {
        if (passwordEditText.getText().toString().trim().isEmpty()) {
            passwordEditText.setError(getResources().getString(R.string.digitePassword));
            requestFocus(passwordEditText);
            return false;
        } else {
            passwordEditText.setError(null);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            serviceGoogleSignUp(acct.getDisplayName(), acct.getEmail(), acct.getId());
        } else {
            // Signed out, show unauthenticated UI.
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void serviceGoogleSignUp(final String accountName, final String accountEmail, String accountGoogleId) {
        RequestDataAddCient requestDataAddCient = new RequestDataAddCient();
        requestDataAddCient.setAccess_token("{725AC31C-43DE-A62B-AB57-67D5C59E7F96}");
        requestDataAddCient.setUserEmail(accountEmail);
        requestDataAddCient.setName(accountName);

        Post apiAddClientService = ApiClient.getClient().create(Post.class);
        Call<ResponseDataAddCient> call = apiAddClientService.addClient(requestDataAddCient);
        call.enqueue(new Callback<ResponseDataAddCient>() {
            @Override
            public void onResponse(Call<ResponseDataAddCient>call, Response<ResponseDataAddCient> response) {
                if(response != null) {
                    Log.e(TAG, response.body().getMessage());
                    if(response.body().getError() == 0) {
                        serviceLogin(response.body().getUserEmail(), response.body().getUserPassword());
                    } else {
                        AlertDialog builder = new showErrorDialog(LoginActivity.this, response.body().getMessage());
                        builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        builder.setCanceledOnTouchOutside(false);
                        builder.setCancelable(false);
                        builder.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseDataAddCient>call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                AlertDialog builder = new showErrorDialog(LoginActivity.this, getResources().getString(R.string.instabilidade_servidor));
                builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                builder.setCanceledOnTouchOutside(false);
                builder.setCancelable(false);
                builder.show();
            }
        });
    }

    private void serviceLogin(String email, String password) {
        RequestDataLogin requestDataLogin = new RequestDataLogin();
        requestDataLogin.setsKeys("1r2a3k4s5h6s7i8n9h10");
        requestDataLogin.setUserEmail(email);
        requestDataLogin.setPassword(password);

        Post apiLoginService = ApiClient.getClient().create(Post.class);
        Call<ResponseDataLogin> call = apiLoginService.loginVerification(requestDataLogin);
        call.enqueue(new Callback<ResponseDataLogin>() {
            @Override
            public void onResponse(Call<ResponseDataLogin>call, Response<ResponseDataLogin> response) {
                if(response != null) {
                    Log.e(TAG, response.body().getMessage());
                    if(response.body().getError() == 0) {
                        PermissionData permissionData = response.body().getPermission_array();
                        Usuario usuario = response.body().getUsuario();

                        session.createLoginSession(permissionData, usuario);
                        startActivity(new Intent(LoginActivity.this, DataActivity.class));
                    } else {
                        AlertDialog builder = new showErrorDialog(LoginActivity.this, response.body().getMessage());
                        builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        builder.setCanceledOnTouchOutside(false);
                        builder.setCancelable(false);
                        builder.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseDataLogin>call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                AlertDialog builder = new showErrorDialog(LoginActivity.this, getResources().getString(R.string.instabilidade_servidor));
                builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                builder.setCanceledOnTouchOutside(false);
                builder.setCancelable(false);
                builder.show();
            }
        });
    }

    private class showErrorDialog extends AlertDialog {
        protected showErrorDialog(Context context, String message) {
            super(context);
            LayoutInflater inflater = getLayoutInflater();
            final View dialoglayout = inflater.inflate(R.layout.custom_alert_layout_single, (ViewGroup) getCurrentFocus());
            setView(dialoglayout);
            final TextView textviewTitle = (TextView) dialoglayout.findViewById(R.id.textview_title);
            textviewTitle.setText(getResources().getString(R.string.app_name));
            final TextView textviewMessage = (TextView) dialoglayout.findViewById(R.id.textview_text);
            textviewMessage.setText(message);
            final TextView textviewPositive = (TextView) dialoglayout.findViewById(R.id.textview_positive);
            textviewPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }
}

package com.example.chengen.mupetune;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class LoginPage extends AppCompatActivity implements View.OnClickListener{
    private EditText user,password;
    private Button login;
    private TextView forget,signUp;
    private final String USER_AGENT = "Mozilla/5.0";
    private final static String Url = "http://45.55.182.4:3000/login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        user = (EditText)findViewById(R.id.etusername);
        password  =(EditText)findViewById(R.id.etpassword);
        login = (Button)findViewById(R.id.btnlogin);
        forget = (TextView)findViewById(R.id.tvforget);
        signUp = (TextView)findViewById(R.id.tvsignup);
        user.setHint("UserName/e-mail");
        password.setHint("Password");
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnlogin:
                String username = user.getText().toString();
                String password = user.getText().toString();
                upLoadToDB(username, password); //WHY IS THERE AN ERROR HERE? I COULDNT FIND CODE FOR REGULAR HTTP LOGIN
                break;
            case R.id.tvforget:
                break;
            case R.id.tvsignup:
                break;
        }
    }
    private void upLoadToDB(String username, String password) throws Exception{
        String string = "username=" + username + "&password=" + password;
        InputStream is = new BufferedInputStream(getAssets().open("dst_root_ca_x3.pem"));
        Certificate ca = CertificateFactory.getInstance("X.509").generateCertificate(is);
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(null, null);
        ks.setCertificateEntry(Integer.toString(1), ca);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(null, tmf.getTrustManagers(), null);
        HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());
        URL oracle = new URL(Url);
        URLConnection yc = oracle.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null)
            System.out.println(inputLine);
        in.close();
        sendPost(string, username, password);
    }
    private void sendPost(String postParameters, String username, String password) throws Exception {
        URL obj = new URL(Url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(postParameters);
        wr.flush();
        wr.close();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        if(response.toString() == "Success"){
            Intent succeed = new Intent(LoginPage.this,Tabs.class);

            //I ADDED THIS CHENGEN -----------------------------------------------
            SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("username", username);
            editor.putString("password", password);
            editor.apply();
            //TILL HERE ----------------------------------------------------------

            startActivity(succeed);
        }
    }
}

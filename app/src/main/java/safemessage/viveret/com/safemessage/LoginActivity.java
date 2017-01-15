package safemessage.viveret.com.safemessage;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class LoginActivity extends Activity {
    // private CallbackManager callbackManager;

    private static final int MY_PERMISSIONS_REQUEST = 1;

    private static final String[] MY_PERMS_REQ = {
            Manifest.permission.INTERNET,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.RECEIVE_MMS,
            Manifest.permission.SEND_SMS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        checkPerms();

        // callbackManager = CallbackManager.Factory.create();


//        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
//        loginButton.setReadPermissions(Arrays.asList("email", "public_profile", "read_mailbox"));

        // If using in a fragment
        // loginButton.setFragment(this);
        // Other app specific specialization

        // Callback registration
//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                fbContext c = new fbContext(loginResult.getAccessToken().getToken(),
//                        loginResult.getAccessToken().getUserId());
//                // Intent i = new Intent(LoginActivity.this, HomeActivity.class);
//                // startActivity(i);
//            }
//
//            @Override
//            public void onCancel() {
//                Toast.makeText(LoginActivity.this, "Must login to use SafeMessage", Toast.LENGTH_LONG);
//            }
//
//            @Override
//            public void onError(FacebookException exception) {
//                Toast.makeText(LoginActivity.this, exception.toString(), Toast.LENGTH_LONG);
//            }
//        });
    }

    private void checkPerms() {
        boolean notAllPermsGranted = false;
        for (String permToCheck : MY_PERMS_REQ) {
            if (ContextCompat.checkSelfPermission(this,
                    permToCheck)
                    != PackageManager.PERMISSION_GRANTED) {
                notAllPermsGranted = true;
            }
        }

        if (notAllPermsGranted) {
            ActivityCompat.requestPermissions(this,
                    MY_PERMS_REQ,
                    MY_PERMISSIONS_REQUEST);
        } else {
            continueActivity();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    continueActivity();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    private void continueActivity() {
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
        finish();
    }
}

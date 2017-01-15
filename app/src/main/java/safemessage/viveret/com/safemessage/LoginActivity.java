package safemessage.viveret.com.safemessage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class LoginActivity extends Activity {
    // private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}

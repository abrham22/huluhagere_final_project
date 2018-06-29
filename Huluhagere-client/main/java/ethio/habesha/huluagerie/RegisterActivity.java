package ethio.habesha.huluagerie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterActivity extends Activity {

    Button register;
    TextView login;
    EditText edEmail, edPassword, edRePassword;
    String TAG = "REGISTER";


    // The Idling Resource which will be null in production.
    @Nullable
    private IdleSimpleIdlingResource mIdlingResource;

    /**
     * Only called from test, creates and returns a new {@link IdleSimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new IdleSimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edPassword = findViewById(R.id.password);
        edRePassword = findViewById(R.id.rePassword);
        edEmail = findViewById(R.id.email);
        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edEmail.getText().toString();
                String password = edPassword.getText().toString();
                String rePassword = edRePassword.getText().toString();
                if(isValidEmail(email) && validateAndMatchPasswords(password, rePassword)) {
                    registerUser(email, password, mIdlingResource);
                }
            }
        });
        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void registerUser(String email, String password,
                              @Nullable final IdleSimpleIdlingResource idlingResource) {

        // prepare user
        DataController.User user = new DataController.User();
        user.email = email;
        user.password = password;

        // prepare data controller
        DataController dataController = new DataController();

        // The IdlingResource is null in production.
        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }

        // make the request
        dataController.registerUser(new Callback<DataController.GeneralResponse>() {

            @Override
            public void onResponse(
                    Call<DataController.GeneralResponse> call,
                    Response<DataController.GeneralResponse> response) {

                if (response.isSuccessful()) {

                    DataController.GeneralResponse resp = response.body();
                    Utility.toastMessage(getApplicationContext(), TAG,resp);

                    String email = edEmail.getText().toString();
                    Intent intent = new Intent(RegisterActivity.this,
                            SavedRoadmapActivity.class);
                    intent.putExtra("email", email);
                    if (idlingResource != null) {
                        idlingResource.setIdleState(true);
                        Utility.toastText(getApplicationContext(), TAG,
                                getString(R.string.register_success_toast));
                    } else {
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                } else {
                    Utility.toastText(getApplicationContext(), TAG,response.errorBody().toString());
                }

            }

            @Override
            public void onFailure(Call<DataController.GeneralResponse> call, Throwable t) {
                Utility.toastText(getApplicationContext(), TAG,"Failure: registerUser");
                t.printStackTrace();
            }

        }, user);

    }

    private boolean validateAndMatchPasswords(String pass1, String pass2) {
        return pass1.equals(pass2);
    }

    private boolean isValidEmail(CharSequence email) {

        /**
         * Email validation pattern,
         */
        final Pattern EMAIL_PATTERN = Pattern.compile(
                "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                        "\\@" +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                        "(" +
                        "\\." +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                        ")+"
        );

        return email != null && EMAIL_PATTERN.matcher(email).matches();

    }
}

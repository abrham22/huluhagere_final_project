package ethio.habesha.huluagerie;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText edEmail, edPassword;
    private String TAG = "LOGIN";

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
        setContentView(R.layout.activity_login);

        edEmail = findViewById(R.id.email);
        edPassword = findViewById(R.id.password);
        Button login = findViewById(R.id.login);
        TextView register = findViewById(R.id.register);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edEmail.getText().toString();
                String password = edPassword.getText().toString();
                loginUser(email, password, mIdlingResource);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    private void loginUser(final String email, String password,
                           @Nullable final IdleSimpleIdlingResource idlingResource) {

        DataController dataController = new DataController();

        // prepare user
        DataController.User user = new DataController.User();
        user.email = email;
        user.password = password;

        // The IdlingResource is null in production.
        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }

        // make the request
        dataController.loginUser(new Callback<DataController.GeneralResponse>() {

            @Override
            public void onResponse(
                    Call<DataController.GeneralResponse> call,
                    Response<DataController.GeneralResponse> response) {


                if (response.isSuccessful()) {

                    DataController.GeneralResponse resp = response.body();
                    if(resp.success) {

                        if(resp.is_admin) {
                            Intent intent = new Intent(LoginActivity.this,
                                    SitesListActivity.class);
                            if (idlingResource != null) {
                                idlingResource.setIdleState(true);
                            }
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent();
                            intent.putExtra("email", email);
                            if (idlingResource != null) {
                                idlingResource.setIdleState(true);
                                Utility.toastText(getApplicationContext(), TAG,
                                        getString(R.string.login_success_toast));
                            } else {
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        }

                    } else {
                        Utility.toastText(getApplicationContext(), TAG, "Can not login");
                    }

                } else {
                    Utility.toastText(getApplicationContext(), TAG, response.errorBody().toString());
                }


            }

            @Override
            public void onFailure(Call<DataController.GeneralResponse> call, Throwable t) {
                Utility.toastText(getApplicationContext(), TAG, "Failure loginUser");
                t.printStackTrace();
            }

        }, user);
    }

}

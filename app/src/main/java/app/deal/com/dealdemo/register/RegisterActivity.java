package app.deal.com.dealdemo.register;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import javax.inject.Inject;

import app.deal.com.dealdemo.R;
import app.deal.com.dealdemo.databinding.ActivityRegisterationBinding;
import app.deal.com.dealdemo.main.MainActivity;
import app.deal.com.dealdemo.model.remote.responses.RegisterResponse;

public class RegisterActivity extends AppCompatActivity implements LifecycleOwner {

    ActivityRegisterationBinding binding;
    private RegisterViewModel viewModel;
    @Inject
    RegisterViewModel.Factory factory;
    private String errorMessage = "Something went wrong please try again.";
    private String successMessage = "RegisterActivity done successful";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registeration);
        binding.regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subscribeDataStreams();
            }
        });
        initViewModel();
    }

    private void initViewModel() {
        factory = new RegisterViewModel.Factory(getApplication(), RegisterActivity.this);
        viewModel = ViewModelProviders.of(this, factory).get(RegisterViewModel.class);
        binding.setRegisterViewModel(viewModel);
    }

    private void subscribeDataStreams() {
        binding.regButton.setEnabled(false);
        LiveData<RegisterResponse> registerResponseLiveData = viewModel.onRegisterClicked();

        if (registerResponseLiveData != null) {
            // Update the UI
            binding.progressBar.setVisibility(View.VISIBLE);
            registerResponseLiveData.observe(this, new Observer<RegisterResponse>() {
                @Override
                public void onChanged(@Nullable RegisterResponse registerResponse) {
                    binding.progressBar.setVisibility(View.GONE);

                //    Log.e("registerResponse", registerResponse.getResult().getUid());

                    if (registerResponse != null) {
                        viewModel.saveUserSignedUp(registerResponse.getResult().getUid() + "");
                        Toast.makeText(getApplication(), successMessage, Toast.LENGTH_SHORT).show();
                        moveToMainActivity();
                    } else {
                        Toast.makeText(getApplication(), errorMessage, Toast.LENGTH_SHORT).show();
                        binding.regButton.setEnabled(true);
                    }
                }
            });
        } else {
            binding.regButton.setEnabled(true);
        }
    }

    private void moveToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}

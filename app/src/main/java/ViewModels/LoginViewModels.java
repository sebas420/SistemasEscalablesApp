package ViewModels;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.apolo.sistemasescalablesapp.R;
import com.example.apolo.sistemasescalablesapp.VerificarPassword;
import com.example.apolo.sistemasescalablesapp.databinding.ActivityVerificarEmailBinding;
import com.example.apolo.sistemasescalablesapp.databinding.ActivityVerificarPasswordBinding;

import Interface.IonClick;
import Library.Validate;
import Models.LoginModels;

public class LoginViewModels extends LoginModels implements IonClick {
    private Activity _activity;
    private static ActivityVerificarEmailBinding _bindingEmail;
    private static ActivityVerificarPasswordBinding _passBinding;
    private static String emailData;

    public LoginViewModels(Activity activity,ActivityVerificarEmailBinding bindingEmail,ActivityVerificarPasswordBinding passBinding){
        _activity = activity;
        _bindingEmail = bindingEmail;
        _passBinding = passBinding;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.email_sign_in_button:
                verificarEmail();
                break;
            case R.id.password_sign_in_button:
                login();
                break;
        }

    }
    private void verificarEmail(){
        boolean cancel = false;
        View focusView = null;
        _bindingEmail.email.setError(null);
        if (TextUtils.isEmpty(emailUI.getValue())){
            _bindingEmail.email.setError(_activity.getString(R.string.error_field_required));
            focusView = _bindingEmail.email;
            cancel = true;
        }else if (!isEmailValid(emailUI.getValue())){
            _bindingEmail.email.setError(_activity.getString(R.string.error_invalid_email));
            focusView = _bindingEmail.email;
            cancel = true;
        }
        if (cancel){
            focusView.requestFocus();
        }else{
            emailData = emailUI.getValue();
            _activity.startActivity(new Intent(_activity, VerificarPassword.class));
            _activity.overridePendingTransition(R.anim.left_in,R.anim.left_out);
            //Toast.makeText(_activity,emailUI.getValue(), Toast.LENGTH_SHORT).show();
        }

    }
    private boolean isEmailValid(String email){
        return Validate.isEmail(email);
    }
    private void login(){
        boolean cancel = false;
        View focusView = null;
        _passBinding.password.setError(null);
        if (TextUtils.isEmpty(passwordUI.getValue())){
            _passBinding.password.setError(_activity.getString(R.string.error_field_required));
            focusView = _passBinding.password;
            cancel = true;
        }else if (!isPasswordValid(passwordUI.getValue())){
            _passBinding.password.setError(_activity.getString(R.string.error_invalid_password));
            focusView = _passBinding.password;
            cancel = true;
        }
        if (cancel){
            focusView.requestFocus();
        }else{

            Toast.makeText(_activity,emailData, Toast.LENGTH_SHORT).show();
        }
    }
    private boolean isPasswordValid(String password){
        return password.length() >= 6;
    }
}

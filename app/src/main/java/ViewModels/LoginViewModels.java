package ViewModels;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.apolo.sistemasescalablesapp.R;
import com.example.apolo.sistemasescalablesapp.databinding.ActivityVerificarEmailBinding;

import Interface.IonClick;
import Models.LoginModels;

public class LoginViewModels extends LoginModels implements IonClick {
    private Activity _activity;
    private ActivityVerificarEmailBinding _bindingEmail;
    public LoginViewModels(Activity activity,ActivityVerificarEmailBinding bindingEmail){
        _activity = activity;
        _bindingEmail = bindingEmail;
    }
    @Override
    public void onClick(View view) {
        verificarEmail();
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
            Toast.makeText(_activity,emailUI.getValue(), Toast.LENGTH_SHORT).show();
        }

    }
    private boolean isEmailValid(String email){
        return true;
    }
}
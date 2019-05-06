package ViewModels;

import android.app.Activity;
import android.view.View;

import com.example.apolo.sistemasescalablesapp.R;
import com.example.apolo.sistemasescalablesapp.databinding.CrearUsuariosBinding;

import Interface.IonClick;
import Models.UserModels;

public class AddUserViewModels extends UserModels implements IonClick {
    private Activity _activity;
    private CrearUsuariosBinding _binding;
    public AddUserViewModels(Activity activity, CrearUsuariosBinding binding) {
        _activity = activity;
        _binding = binding;
    }

    @Override
    public void onClick(View view) {
        registrarUsuarios();
    }
    private void registrarUsuarios(){
        String data = _activity.getResources().getStringArray(R.array.divipola)[item.getSelectedItemPosition()];
    }
}


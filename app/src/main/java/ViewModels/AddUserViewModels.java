package ViewModels;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;

import com.example.apolo.sistemasescalablesapp.R;
import com.example.apolo.sistemasescalablesapp.databinding.CrearUsuariosBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import Interface.IonClick;
import Library.Networks;
import Library.Validate;
import Models.Collections;
import Models.UserModels;

public class AddUserViewModels extends UserModels implements IonClick {
    private Activity _activity;
    private CrearUsuariosBinding _binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore _db;
    private DocumentReference _documentRef;

    public AddUserViewModels(Activity activity, CrearUsuariosBinding binding) {
        _activity = activity;
        _binding = binding;
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        registrarUsuarios();
    }

    private void registrarUsuarios() {

        View focusView;
        if (TextUtils.isEmpty(nombreUI.getValue())) {
            _binding.txtnombre.setError(_activity.getString(R.string.error_field_required));
            focusView = _binding.txtnombre;
            focusView.requestFocus();
        } else {
            if (TextUtils.isEmpty(apellidoUI.getValue())) {
                _binding.txtapellido.setError(_activity.getString(R.string.error_field_required));
                focusView = _binding.txtapellido;
                focusView.requestFocus();
            } else {
                if (TextUtils.isEmpty(emailUI.getValue())) {
                    _binding.txtemail.setError(_activity.getString(R.string.error_field_required));
                    focusView = _binding.txtemail;
                    focusView.requestFocus();

                } else {
                    if (!isEmailValid(emailUI.getValue())) {
                        _binding.txtemail.setError(_activity.getString(R.string.error_invalid_email));
                        focusView = _binding.txtemail;
                        focusView.requestFocus();
                    } else {
                        if (TextUtils.isEmpty(cedulaUI.getValue())) {
                            _binding.txtcedula.setError(_activity.getString(R.string.error_field_required));
                            focusView = _binding.txtcedula;
                            focusView.requestFocus();
                        } else {
                            if (!isPasswordValid(cedulaUI.getValue())) {
                                _binding.txtcedula.setError(_activity.getString(R.string.error_invalid_password));
                                focusView = _binding.txtcedula;
                                focusView.requestFocus();
                            } else {
                                if (new Networks(_activity).verificaConexion()) {
                                    insertUser();
                                } else {
                                    focusView = _binding.txtcedula;
                                    Snackbar.make(focusView, R.string.networks, Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void insertUser() {
        mAuth.createUserWithEmailAndPassword(emailUI.getValue(), cedulaUI.getValue())
                .addOnCompleteListener(_activity, (task) -> {
                    if (task.isSuccessful()) {
                        String role = _activity.getResources().getStringArray(R.array.item_roles)[item.getSelectedItemPosition()];
                        _db = FirebaseFirestore.getInstance();
                        _documentRef = _db.collection(Collections.Usuarios.USUARIOS).document(emailUI.getValue());
                        Map<String, Object> user = new HashMap<>();
                        user.put(Collections.Usuarios.APELLIDO,apellidoUI.getValue());
                        user.put(Collections.Usuarios.EMAIL,emailUI.getValue());
                        user.put(Collections.Usuarios.NOMBRE,nombreUI.getValue());
                        user.put(Collections.Usuarios.ROLE, role);
                        _documentRef.set(user).addOnCompleteListener((task2)->{

                        });
                        //Toast.makeText(_activity, data, Toast.LENGTH_SHORT).show();
                    } else {
                        View view = _binding.txtcedula;
                        Snackbar.make(view, R.string.fail_registrer, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });
    }

    private boolean isEmailValid(String email) {
        return Validate.isEmail(email);
    }
    private boolean isPasswordValid(String password){
        return password.length() >= 6;
    }
}

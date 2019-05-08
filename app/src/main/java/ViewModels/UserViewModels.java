package ViewModels;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.apolo.sistemasescalablesapp.CrearUsuarios;
import com.example.apolo.sistemasescalablesapp.R;
import com.example.apolo.sistemasescalablesapp.databinding.UsuariosBinding;

import java.util.ArrayList;
import java.util.List;

import Interface.IonClick;
import Models.Pojo.User;
import ViewModels.Adapter.UserAdapter;

public class UserViewModels implements IonClick ,UserAdapter.AdapterListener{
    private Activity _activity;
    private UsuariosBinding _binding;
    private RecyclerView _recycler;
    private RecyclerView.LayoutManager _lManager;
    private UserAdapter _userAdapter;

    public UserViewModels(Activity activity, UsuariosBinding binding) {
        _activity = activity;
        _binding = binding;
        _recycler = binding.recyclerViewUser;
        _recycler.setHasFixedSize(true);
        _lManager = new LinearLayoutManager(activity);
        _recycler.setLayoutManager(_lManager);
        initRecyclerView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_addUser:
                _activity.startActivity(new Intent(_activity, CrearUsuarios.class));
                break;
        }
    }
    private void initRecyclerView(){
        List<User> postList =  new ArrayList<>();
        postList.add(new User("Pagoada","Alex","alejpz17@gmail.com","Admin"));
        postList.add(new User("Suazo","Joel","joel@gmail.com","User"));
        _userAdapter = new UserAdapter(postList,this);
        _recycler.setAdapter(_userAdapter);
    }
    @Override
    public void onUserClicked(User user){
        Toast.makeText(_activity, "User clicked! " + user.getNombre(), Toast.LENGTH_SHORT).show();
    }
}

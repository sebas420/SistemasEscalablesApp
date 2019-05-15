package ViewModels;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.apolo.sistemasescalablesapp.CrearUsuarios;
import com.example.apolo.sistemasescalablesapp.R;
import com.example.apolo.sistemasescalablesapp.databinding.UsuariosBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Animation.FloatingAction;
import Animation.RecyclerViewScroll;
import Interface.IonClick;
import Library.MemoryData;
import Library.Networks;
import Models.Collections;
import Models.Pojo.User;
import ViewModels.Adapter.UserAdapter;

public class UserViewModels extends RecyclerViewScroll implements IonClick ,UserAdapter.AdapterListener, SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener {
    private Activity _activity;
    private UsuariosBinding _binding;
    private RecyclerView _recycler;
    private RecyclerView.LayoutManager _lManager;
    private UserAdapter _userAdapter;
    private FirebaseFirestore _db;
    private FirebaseStorage _storage;
    private Gson gson = new Gson();
    private MemoryData _memoryData;
    private StorageReference _storageRef;
    private  List<User> userList =  new ArrayList<>();
    private FloatingAction _floatingAction;

    public UserViewModels(Activity activity, UsuariosBinding binding) {
        _activity = activity;
        _binding = binding;
        _recycler = binding.recyclerViewUser;
        _binding.progressBar.setVisibility(ProgressBar.INVISIBLE);
        _recycler.setHasFixedSize(true);
        _lManager = new LinearLayoutManager(activity);
        _recycler.setLayoutManager(_lManager);
        _recycler.addOnScrollListener(this);
        _db = FirebaseFirestore.getInstance();
        _storage = FirebaseStorage.getInstance();
        _storageRef = _storage.getReference();
        _binding.swipeRefresh.setColorSchemeResources(
                R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark);
        _binding.swipeRefresh.setOnRefreshListener(this);
        _memoryData = MemoryData.getInstance(activity);
        binding.fabAddUser.setScaleX(0);
        binding.fabAddUser.setScaleY(0);
        _floatingAction = new FloatingAction(activity,_binding.fabAddUser);
        CloudFirestore();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_addUser:
                _memoryData.saveData("User","");
                _activity.startActivity(new Intent(_activity, CrearUsuarios.class));
                break;
        }
    }
    private void CloudFirestore(){
        _binding.progressBar.setVisibility(ProgressBar.VISIBLE);
        final long ONE_MEGABYTE = 1024 * 1024;
        _db.collection(Collections.Usuarios.USUARIOS).addSnapshotListener((snapshots,e) ->{
            userList =  new ArrayList<>();
            for (QueryDocumentSnapshot document : snapshots){
                String Apellido = document.getData().get(Collections.Usuarios.APELLIDO).toString();
                String Email = document.getData().get(Collections.Usuarios.EMAIL).toString();
                String Nombre = document.getData().get(Collections.Usuarios.NOMBRE).toString();
                String Role = document.getData().get(Collections.Usuarios.ROLE).toString();
                _storageRef.child(Collections.Usuarios.USUARIOS+"/"+Email)
                        .getBytes(ONE_MEGABYTE).addOnSuccessListener((bytes)->{
                    userList.add(new User(Apellido,Nombre,Email,Role,bytes));
                    initRecyclerView(userList);
                });
            }
        });
    }
    private void initRecyclerView(List<User> list){
        _userAdapter = new UserAdapter(list,this);
        _recycler.setAdapter(_userAdapter);
        _binding.progressBar.setVisibility(ProgressBar.INVISIBLE);
        _binding.swipeRefresh.setRefreshing(false);
    }
    @Override
    public void onUserClicked(User user) {
        if (new Networks(_activity).verificaConexion()) {
            _memoryData.saveData("User", gson.toJson(new User(
                    user.getApellido(),
                    user.getNombre(),
                    user.getEmail(),
                    user.getRole(),
                    null
            )));
            _activity.startActivity(new Intent(_activity, CrearUsuarios.class));
        } else {
            Snackbar.make(_binding.progressBar, R.string.networks, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    @Override
    public void onRefresh() {
        CloudFirestore();
    }
    public void onCreateOptionsMenu(Menu menu){
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(_activity.getText(R.string.action_search));
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            List<User> list =  userList.stream().filter(u -> u.getNombre().startsWith(newText)
                    || u.getApellido().startsWith(newText)).collect(Collectors.toList());
            initRecyclerView(list);
        }
        return false;
    }

    @Override
    public void show() {
        _floatingAction.show();
    }

    @Override
    public void hide() {
        _floatingAction.hide();
    }
}




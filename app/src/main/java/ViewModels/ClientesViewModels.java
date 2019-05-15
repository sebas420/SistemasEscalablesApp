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

import com.example.apolo.sistemasescalablesapp.CrearCliente;
import com.example.apolo.sistemasescalablesapp.DetailsCliente;
import com.example.apolo.sistemasescalablesapp.R;
import com.example.apolo.sistemasescalablesapp.databinding.ClientesBinding;
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
import Library.MemoryData;
import Library.Networks;
import Models.Collections;
import Models.Pojo.Cliente;
import ViewModels.Adapter.ClienteAdapter;

public class ClientesViewModels extends RecyclerViewScroll implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener ,ClienteAdapter.AdapterListener, SearchView.OnQueryTextListener {
    private Activity _activity;
    private ClientesBinding _binding;
    private RecyclerView _recycler;
    private RecyclerView.LayoutManager _lManager;
    private ClienteAdapter _clinteAdapter;
    private FirebaseFirestore _db;
    private FirebaseStorage _storage;
    private StorageReference _storageRef;
    private FloatingAction _floatingAction;
    private Gson gson = new Gson();
    private MemoryData _memoryData;
    private List<Cliente> clienteList;
    private boolean valor =  true;

    public ClientesViewModels(Activity activity, ClientesBinding binding){
        _activity = activity;
        _binding = binding;
        clienteList =  new ArrayList<>();
        _recycler = binding.recyclerViewClientes;
        _binding.progressBar.setVisibility(ProgressBar.INVISIBLE);
        _recycler.setHasFixedSize(true);
        _recycler.addOnScrollListener(this);
        _lManager = new LinearLayoutManager(activity);
        _recycler.setLayoutManager(_lManager);
        _db = FirebaseFirestore.getInstance();
        _storage = FirebaseStorage.getInstance();
        _storageRef = _storage.getReference();
        _binding.swipeRefresh.setColorSchemeResources(
                R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark);
        _binding.swipeRefresh.setOnRefreshListener(this);
        _memoryData = MemoryData.getInstance(activity);
        binding.fabAddCliente.setScaleX(0);
        binding.fabAddCliente.setScaleY(0);
        _floatingAction = new FloatingAction(activity,_binding.fabAddCliente);
        binding.fabAddCliente.setOnClickListener(this);
        CloudFirestore();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_addCliente:
                _activity.startActivity(new Intent(_activity, CrearCliente.class));
                break;
        }
    }

    @Override
    public void onRefresh() {
        valor = false;
        CloudFirestore();
    }
    private void CloudFirestore(){
        if (new Networks(_activity).verificaConexion()){
            if (valor){
                _binding.progressBar.setVisibility(ProgressBar.VISIBLE);
            }

            final long ONE_MEGABYTE = 2024 * 2024;
            _db.collection(Collections.Clientes.CLIENTES).addSnapshotListener((snapshots, e) ->{
                clienteList =  new ArrayList<>();
                for (QueryDocumentSnapshot document : snapshots){
                    String Apellido = document.getData()
                            .get(Collections.Clientes.APELLIDO).toString();
                    String Email = document.getData()
                            .get(Collections.Clientes.EMAIL).toString();
                    String Nombre = document.getData().
                            get(Collections.Clientes.NOMBRE).toString();
                    String Nid = document.getData()
                            .get(Collections.Clientes.NID).toString();
                    String Telefono = document.getData()
                            .get(Collections.Clientes.TELEFONO).toString();
                    String Direccion = document.getData()
                            .get(Collections.Clientes.DRECCION).toString();
                    _storageRef.child(Collections.Clientes.CLIENTES+"/"+Nid)
                            .getBytes(ONE_MEGABYTE).addOnSuccessListener((bytes)->{
                        clienteList.add(new Cliente(Apellido,Nombre,Email,Nid,Telefono,Direccion,bytes));
                        initRecyclerView(clienteList);
                    });
                }
            });
        }else{
            Snackbar.make(_binding.progressBar, R.string.networks, Snackbar.LENGTH_LONG).show();
        }
    }
    private void initRecyclerView(List<Cliente> list){
        _clinteAdapter = new ClienteAdapter(list,this);
        _recycler.setAdapter(_clinteAdapter);
        _binding.progressBar.setVisibility(ProgressBar.INVISIBLE);
        _binding.swipeRefresh.setRefreshing(false);
        _binding.swipeRefresh.setRefreshing(false);
        valor = true;
    }

    @Override
    public void onClienteClicked(Cliente cliente) {
        if (new Networks(_activity).verificaConexion()){
            _memoryData.saveData("Cliente",gson.toJson(new Cliente(
                    cliente.getApellido(),
                    cliente.getNombre(),
                    cliente.getEmail(),
                    cliente.getNid(),
                    cliente.getTelefono(),
                    cliente.getDireccion(),
                    null
            )));
            _activity.startActivity(new Intent(_activity, DetailsCliente.class));
        }else{
            Snackbar.make(_binding.progressBar, R.string.networks, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void show() {
        _floatingAction.show();
    }

    @Override
    public void hide() {
        _floatingAction.hide();
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
            List<Cliente> list =  clienteList.stream().filter(u -> u.getNombre().startsWith(newText)
                    || u.getApellido().startsWith(newText)).collect(Collectors.toList());
            initRecyclerView(list);
        }
        return false;
    }
}
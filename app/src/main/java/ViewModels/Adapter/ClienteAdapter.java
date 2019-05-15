package ViewModels.Adapter;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.apolo.sistemasescalablesapp.R;
import com.example.apolo.sistemasescalablesapp.databinding.ClienteRowItemBinding;
import java.util.List;

import Models.Pojo.Cliente;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.MyViewHolder>{
    private List<Cliente> _clienteList;
    private LayoutInflater _layoutInflater;
    private AdapterListener _listener;
    public ClienteAdapter(List<Cliente> clienteList, AdapterListener listener){
        _clienteList = clienteList;
        _listener = listener;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private final ClienteRowItemBinding _binding;
        public MyViewHolder(final ClienteRowItemBinding binding){
            super(binding.getRoot());
            _binding = binding;
        }
    }
    @Override
    public ClienteAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (_layoutInflater == null){
            _layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ClienteRowItemBinding binding =
                DataBindingUtil.inflate(_layoutInflater, R.layout.cliente_row_item, parent, false);
        return new ClienteAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ClienteAdapter.MyViewHolder holder, int position) {
        holder._binding.setCliente(_clienteList.get(position));
        byte[]bytes = _clienteList.get(position).getBytes();
        Bitmap _selectedImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        holder._binding.thumbnail.setImageBitmap(_selectedImage);
        holder._binding.cardViewCliente.setOnClickListener((v)->{
            if (_listener != null) {
                _listener.onClienteClicked(_clienteList.get(position));
            }

        });
    }

    @Override
    public int getItemCount() {
        return _clienteList.size();
    }
    public interface AdapterListener{
        void onClienteClicked(Cliente cliente);
    }
}

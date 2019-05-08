package ViewModels.Adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.apolo.sistemasescalablesapp.R;
import com.example.apolo.sistemasescalablesapp.databinding.UserRowItemBinding;

import java.util.List;

import Models.Pojo.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    private List<User> _userList;
    private LayoutInflater _layoutInflater;
    private AdapterListener _listener;

    public UserAdapter(List<User> userList,AdapterListener listener) {
        _userList = userList;
        _listener = listener;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        private final UserRowItemBinding _binding;
        public MyViewHolder(final UserRowItemBinding binding){
            super(binding.getRoot());
            _binding = binding;
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (_layoutInflater == null){
            _layoutInflater = LayoutInflater.from(parent.getContext());
        }
        UserRowItemBinding binding =
                DataBindingUtil.inflate(_layoutInflater, R.layout.user_row_item, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder._binding.setUser(_userList.get(position));
        holder._binding.cardViewUser.setOnClickListener((v)-> {

            if (_listener != null) {
                _listener.onUserClicked(_userList.get(position));
            }

        });
    }

    @Override
    public int getItemCount() {
        return _userList.size();
    }
    public interface AdapterListener{
        void onUserClicked(User user);
    }
}

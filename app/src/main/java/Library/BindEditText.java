package Library;

import android.databinding.BindingAdapter;
import android.util.Pair;
import android.widget.EditText;

import com.example.apolo.sistemasescalablesapp.R;

import Models.BindableString;

public class BindEditText {
    private static MemoryData _memoryData;
    @BindingAdapter({"app:binding"})
    public static void bindEditText(EditText view, final BindableString bindableString){

        _memoryData = MemoryData.getInstance(view.getContext());
        //if(_memoryData.getData("DATA").equals("") ) {
        /*Contenedor para facilitar el paso alrededor de una tupla de dos objetos.
        Este objeto proporciona una implementación sensata de equals (),
        que devuelve verdadero si equals () es verdadero en cada uno de los objetos contenidos.*/
        Pair<BindableString, TextWatcherAdapter> pair = (Pair) view.getTag(R.id.bound_observable);
        if (pair == null || pair.first != bindableString) {
            if (pair != null) {
                view.removeTextChangedListener(pair.second);
            }
            TextWatcherAdapter watcher = new TextWatcherAdapter() {
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    bindableString.setValue(s.toString());
                }
            };
            view.setTag(R.id.bound_observable, new Pair<>(bindableString, watcher));
            view.addTextChangedListener(watcher);
        }
        String newValue = bindableString.getValue();
        if (!view.getText().toString().equals(newValue)) {
            view.setText(newValue);
        }
        // }
    }
}

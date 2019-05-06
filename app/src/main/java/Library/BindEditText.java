package Library;

import android.databinding.BindingAdapter;
import android.support.v4.util.Pair;
import android.widget.EditText;

import com.example.apolo.sistemasescalablesapp.R;

import Models.BindableString;

public class BindEditText {
    @BindingAdapter(("app:binding"))
    public static void bindEditText(EditText view, final BindableString bindableString){
        /*contenedo para facilitar el paso alrededor de una tupla de dos objetos.
        este objeto proporciona una implementacion sensata de equals (),
        que devuelve verdadero si equals ( es verdadero en cada uno de los contenidos
         */
        android.support.v4.util.Pair<BindableString, TextWatcherAdapter> pair = (android.support.v4.util.Pair) view.getTag(R.id.bound_observable);
        if (pair == null || pair.first != bindableString){
            if (pair != null){
                view.removeTextChangedListener(pair.second);
            }
            TextWatcherAdapter watcher = new TextWatcherAdapter(){
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    bindableString.setValue(s.toString());
                }
            };
            view.setTag(R.id.bound_observable, new Pair<>(bindableString, watcher));
            view.addTextChangedListener(watcher);
        }
        String newValue = bindableString.getValue();
        if (!view.getText().toString().equals(newValue)){
            view.setText(newValue);
        }
    }
}

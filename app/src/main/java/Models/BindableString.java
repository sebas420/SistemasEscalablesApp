package Models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

import java.util.Objects;

public class BindableString extends BaseObservable {
    private String value;

    @Bindable
    public String getValue() {
        return value != null ? value : "";
    }

    public void setValue(String value) {
        if(!Objects.equals(this.value,value)){
            this.value = value;
            notifyPropertyChanged(BR.value);
        }

    }
}

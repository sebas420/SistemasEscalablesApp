package Library;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;

public class Networks extends AppCompatActivity {
    private Activity _activity;
    public Networks(Activity activity){
        _activity = activity;
    }
    public boolean verificaConexion(){
        boolean valor = false;
        ConnectivityManager cm = (ConnectivityManager) _activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null){
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
                valor = true;
            }else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){
                valor = true;
            }
        }else{
            valor = false;
        }
        return valor;
    }
}

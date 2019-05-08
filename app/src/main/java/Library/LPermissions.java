package Library;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class LPermissions {
    private Activity _activity;
    public LPermissions(Activity activity){
        _activity = activity;
    }
    public boolean STORAGE(){
        if ((ContextCompat.checkSelfPermission(_activity, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(_activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) ){
            ActivityCompat.requestPermissions(_activity,new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            },1);
            return false;
        }else{
            return true;
        }
    }
}

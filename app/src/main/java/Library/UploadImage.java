package Library;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;

public class UploadImage {
    private static final int RESQUEST_CODE_CROP_IMAGE = 0X3;
    private static final String TEMP_PHOYO_FILE = "temprary_img.png";
    private Activity _activity;

    public UploadImage(Activity activity){
        _activity = activity;
    }
    public void getImage(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        photoPickerIntent.setType("image/*");
        photoPickerIntent.putExtra("crop","true");
        photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT,getTempoUri());
        photoPickerIntent.putExtra("outputFormat", Bitmap.CompressFormat.PNG);
        _activity.startActivityForResult(photoPickerIntent,RESQUEST_CODE_CROP_IMAGE);
    }
    private Uri getTempoUri(){
        return Uri.fromFile(getTempoFile());
    }
    public File getTempoFile(){
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File file = new File(Environment.getExternalStorageDirectory(),TEMP_PHOYO_FILE);
            try {
                file.createNewFile();
            } catch (IOException e) {}
            return file;
        }else{
            return null;
        }
    }
}

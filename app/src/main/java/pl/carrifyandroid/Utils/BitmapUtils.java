package pl.carrifyandroid.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import timber.log.Timber;

public class BitmapUtils {

    private Context context;

    public BitmapUtils(Context context) {
        this.context = context;
    }

    public File setBitmapToFile(Bitmap bmp) {
        File f = new File(context.getCacheDir(), UUID.randomUUID().toString() + ".png");
        try {
            f.createNewFile();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 30, bos);

            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(((float) newWidth) / width, ((float) newHeight) / height);
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
    }

    public Bitmap decodeBitmapFromFile(File f) {
        Bitmap bitmap = null;
        if (f != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            try {
                bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }
}

package com.example.bruno.uri;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by bruno on 02/05/16.
 */
public class CopyAsyncTask extends AsyncTask<Uri, Void, File> {
  private File copy;
  private WeakReference<Activity> reference;
  private String extension;

  public CopyAsyncTask(Activity activity, String extension) {
    this.reference = new WeakReference<>(activity);
    this.extension = extension;

  }

  @Override
  protected File doInBackground(Uri... uris) {
    Uri uri = uris[0];
    try {
      copy = File.createTempFile("IMG - " + new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.getDefault()).format(new Date()), extension, reference.get().getExternalFilesDir("Uri"));


      InputStream inputStream = reference.get().getContentResolver().openInputStream(uri);
      OutputStream outputStream = new FileOutputStream(copy);

      byte[] buffer = new byte[1024];
      int bytesRead;
      while ((bytesRead = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, bytesRead);
      }

      inputStream.close();
      outputStream.close();

      ContentValues values = new ContentValues();

      values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
      values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
      values.put(MediaStore.MediaColumns.DATA, copy.getPath());

      reference.get().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return copy;
  }

  @Override
  protected void onPostExecute(File file) {
    Toast.makeText(reference.get(), "The copy of the file is done on " + file.getPath(), Toast.LENGTH_SHORT).show();
  }
}

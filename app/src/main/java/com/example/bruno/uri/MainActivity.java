package com.example.bruno.uri;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.bruno.uri.databinding.ActivityMainBinding;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  private static final int PICK_FILE_REQ = 123;
  private ActivityMainBinding binding;
  private CopyAsyncTask task;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    binding.pickFile.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    Intent pickPhoto = new Intent(Intent.ACTION_GET_CONTENT);
    pickPhoto.setType("image/*");
    startActivityForResult(pickPhoto, PICK_FILE_REQ );
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if(requestCode == PICK_FILE_REQ && resultCode == RESULT_OK){
      handleResult(data);
    }
  }

  private void handleResult(Intent data){
    if(data!=null){
      Uri uri = data.getData();
      
      if(uri!=null) {
        ContentResolver cr = getContentResolver();

        binding.image.setImageURI(uri);
        int width = 500, height = 500;
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
            .setResizeOptions(new ResizeOptions(width, height))
            .build();
        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
            .setOldController(binding.image.getController())
            .setImageRequest(request)
            .build();
        binding.image.setController(controller);

        setNameAndSize(cr, uri);
        String path = UriUtils.getPath(this, uri);
        if(path == null){
          task = new CopyAsyncTask(this, "jpg");
          Toast.makeText(this, "Creating a copy of the file", Toast.LENGTH_SHORT).show();
          task.execute(uri);
        }
        binding.path.setText(getString(R.string.path, path));
        binding.mimeType.setText(getString(R.string.mime_type, cr.getType(uri)));
        binding.authority.setText(getString(R.string.authority, uri.getAuthority()));
        binding.scheme.setText(getString(R.string.authority, uri.getScheme()));
        binding.uriPath.setText(getString(R.string.path, uri.getPath()));
        binding.userInfo.setText(getString(R.string.user_info, uri.getUserInfo()));
        binding.scheme.setText(getString(R.string.absolute, uri.isAbsolute()) + " " +
            getString(R.string.hierarchical, uri.isHierarchical()) + " " +
            getString(R.string.opaque, uri.isOpaque()) + " " +
            getString(R.string.relative, uri.isRelative()));
      }else{
        Toast.makeText(MainActivity.this, "Uri null", Toast.LENGTH_SHORT).show();
      }
    }else{
      Toast.makeText(MainActivity.this, "Data null", Toast.LENGTH_SHORT).show();
    }
  }

  private void setNameAndSize(ContentResolver cr, Uri uri){
    Cursor returnCursor = cr.query(uri, null, null, null, null);
    /*
     * Get the column indexes of the data in the Cursor,
     * move to the first row in the Cursor, get the data,
     * and display it.
     */
    if(returnCursor!=null){
      int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
      int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
      returnCursor.moveToFirst();

      binding.filename.setText(getString(R.string.filename, returnCursor.getString(nameIndex)));
      binding.size.setText(getString(R.string.size, Long.toString(returnCursor.getLong(sizeIndex))));
    }else {
      Toast.makeText(MainActivity.this, "Cursor null", Toast.LENGTH_SHORT).show();
    }
  }
}

package com.example.bruno.uri;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.bruno.uri.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  private ActivityMainBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    binding.pickFile.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    Toast.makeText(MainActivity.this, "TODO", Toast.LENGTH_SHORT).show();
  }
}

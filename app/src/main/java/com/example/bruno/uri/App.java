package com.example.bruno.uri;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by bruno on 28/04/16.
 */
public class App extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    Fresco.initialize(this);
  }
}

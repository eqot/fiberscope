package com.eqot.fiberscope;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        showAnnotationMessage();
    }

//    private void showAnnotationMessage() {
//        GeneratedClass generatedClass = new GeneratedClass();
//        String message = generatedClass.getMessage();
//
//        new AlertDialog.Builder(this)
//                .setPositiveButton("Ok", null)
//                .setTitle("Annotation Processor Messages")
//                .setMessage(message)
//                .show();
//    }
}

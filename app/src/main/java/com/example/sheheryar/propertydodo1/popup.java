package com.example.sheheryar.propertydodo1;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class popup extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
        final LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);


        // btnShow
        Button btnShow = (Button) findViewById(R.id.btnShow);
        btnShow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final View Viewlayout = inflater.inflate(R.layout.featured,
                        (ViewGroup) findViewById(R.id.layout_dialog));

                popDialog.setIcon(android.R.drawable.btn_star_big_on);
                popDialog.setTitle("Input User and Password ");
                popDialog.setView(Viewlayout);

                // Button OK
                popDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        TextView result = (TextView) findViewById(R.id.txtResult);
//                        result.setText(" Username : " + user.getText().toString() + " \n" +
//                                " Password : " + pass.getText().toString() + " ");
                    }
                })
                // Button Cancel
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.cancel();
                        }
                    });

                popDialog.create();
                popDialog.show();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}

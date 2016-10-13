package com.example.tibo000.tp_asynctask;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


public class MainActivity extends Activity {

    private ProgressBar mProgressBar;
    private Button mButton;

    private ProgressBar mProgressBar2;
    private Button mButton2;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // On récupère les composants de notre layout
        mProgressBar = (ProgressBar) findViewById(R.id.pBAsync);
        mButton = (Button) findViewById(R.id.btnLaunch);
        mProgressBar2 = (ProgressBar) findViewById(R.id.pBAsync2);
        mButton2 = (Button) findViewById(R.id.btnLaunch2);

        // On met un Listener sur le bouton
        mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AsyncTaskRunner1 AT1=new AsyncTaskRunner1();
                AT1.execute();
            }
        });


        // On met un Listener sur le bouton
        mButton2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AsyncTaskRunner2 AT2=new AsyncTaskRunner2();
                AT2.execute();
            }
        });
    }

    /** 1ère tâche asynchrone*/
    private class AsyncTaskRunner1 extends AsyncTask<Void, Integer, Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), "Début du traitement asynchrone", Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onProgressUpdate(Integer... values){
            super.onProgressUpdate(values);
            // Mise à jour de la ProgressBar
            mProgressBar.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            int progress;
            for (progress=0;progress<=100;progress++)
            {
                try{
                    Thread.sleep(500);
                } catch (InterruptedException e) {}
                //la méthode publishProgress,1 met à jour l'interface en invoquant la méthode onProgressUpdate
                publishProgress(progress);
                progress++;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Toast.makeText(getApplicationContext(), "Le traitement asynchrone est terminé", Toast.LENGTH_LONG).show();
           /* AlertDialog.Builder alertDialog= new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle("Résultat : "+ result);
            alertDialog.show();*/
        }
    }

    /** 2 eme tâche asynchrone*/
    private class AsyncTaskRunner2 extends AsyncTask<String, String, String>
    {



        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                int time = 5000;

                Thread.sleep(time);
                resp = "Slept for 5 seconds";
            } catch (InterruptedException e) {
                e.printStackTrace();
                resp = e.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();
        }


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MainActivity.this,
                    "ProgressDialog",
                    "Wait for 5 seconds");
        }

    }
}
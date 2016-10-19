package com.example.tibo000.tp_asynctask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {

    //requete http
    private Button bAsyncTask;
    private Button bTask;
    private TextView tvAsyncTask;
    private TextView tvTask;

    // autres
    private ProgressBar mProgressBar;
    private Button mButton;
    private Button mButton2;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // pour requete http
        tvAsyncTask = (TextView) findViewById(R.id.text_asyncTask);
        bAsyncTask = (Button) findViewById(R.id.button_async);
        bAsyncTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncHttpGetRequest httpGet;
                httpGet = new AsyncHttpGetRequest();
                httpGet.execute();
                while (httpGet.getHttpAnswer() == null){}
                tvAsyncTask.setText(httpGet.getHttpAnswer());
            }
        });

        tvTask = (TextView) findViewById(R.id.text_task);
        bTask = (Button) findViewById(R.id.button_sync);
        bTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localeHttpGetRequest();
            }
        });

        // 2 autres exemples
/*        mProgressBar = (ProgressBar) findViewById(R.id.pBAsync);
        mButton = (Button) findViewById(R.id.btnLaunch);
        mButton2 = (Button) findViewById(R.id.btnLaunch2);

        mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AsyncTaskRunner1 AT1=new AsyncTaskRunner1();
                AT1.execute();
            }
        });

        mButton2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AsyncTaskRunner2 AT2=new AsyncTaskRunner2();
                AT2.execute();
            }
        });*/
    }

    /**execution asynchrone*/
    private class AsyncHttpGetRequest extends AsyncTask<Void, Void, Void> {
        private String httpAnswer = null;

        @Override
        protected Void doInBackground(Void... urls) {
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            StringBuffer buffer = null;
            BufferedReader reader = null;

            try {
                URL url = new URL("https://jsonplaceholder.typicode.com/posts/1");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                inputStream = urlConnection.getInputStream();
                buffer = new StringBuffer();
                if (inputStream != null){
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = reader.readLine()) != null){
                        buffer.append(line + "\n");
                    }
                    if (buffer.length() != 0){
                        this.httpAnswer = buffer.toString();
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                System.out.println("Erreur lors de la requête HTTP" + e.getMessage());
            }
            finally{
                if (urlConnection != null){
                    urlConnection.disconnect();
                }
                if (reader != null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Erreur lors de la requête HTTP " + e.getMessage());
                    }
                }
            }
            return null;
        }

        public String getHttpAnswer() {
            return httpAnswer;
        }
    }


    /**execution synchrone*/
    private String localeHttpGetRequest(){
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        StringBuffer buffer = null;
        BufferedReader reader = null;
        String httpAnswer = null;

        try {
            URL url = new URL("https://jsonplaceholder.typicode.com/posts/1");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            inputStream = urlConnection.getInputStream();
            buffer = new StringBuffer();
            if (inputStream != null){
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null){
                    buffer.append(line + "\n");
                }
                if (buffer.length() != 0){
                    httpAnswer = buffer.toString();
                }
            }
            return httpAnswer;
        }
        catch (Exception e) {
            tvTask.setText("Erreur : " + e.getClass().getName());
            e.printStackTrace();
            System.out.println("Erreur lors de la requête HTTP locale " + e.getMessage());
            return null;
        }
    }



    /** Autres exemple */
    /** 1ère tâche asynchrone*/
    private class AsyncTaskRunner1 extends AsyncTask<Void, Integer, Void>
    {
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
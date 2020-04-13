/******************************************************************************
 * Copyright (c) 2018, Joaquín Pérez Valera
 *
 * This software is available under the following "MIT Style" license,
 * or at the option of the licensee under the LGPL (see COPYING).
 * --
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 ******************************************************************************/

package com.per.hikra.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.per.hikra.R;
import com.per.hikra.results.AllResults;
import com.per.hikra.statistics.Statistics;
import com.per.hikra.utils.Toaster;


public class CalculationActivity extends AppCompatActivity {

    private static final String TAG = "CalcActivity";
    private ProgressBar mProgressBar;

    private TextView mTVCalculated;
    private TextView mTVTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_calculation);
            mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
            mTVTask = (TextView) findViewById(R.id.textViewTastk);
            mProgressBar.setVisibility(View.VISIBLE);
            mTVCalculated = (TextView) findViewById(R.id.tvCalculated);
            mProgressBar.setMax(15);


            new MyTask(getApplicationContext()).execute(10);

        } catch(Exception e){
            Log.i(TAG, "Failure on Calculation Activity");
            Log.e(TAG, e.getStackTrace().toString());
        }
    }


     class MyTask extends AsyncTask<Integer, Integer, String> {
        Context context;

        public MyTask(Context context){
            this.context = context;

        }


        @Override
        protected String doInBackground(Integer... params) {
            Statistics dataset = (Statistics) getApplication();

            dataset.setEncalculo(1);
            //dataset.addTestData();
            dataset.showAllD();
            dataset.OrderData();
            dataset.showOrdered();
            //dataset.wwtest();
            //dataset.getTime();
            dataset.Basicos();

            //Avance: 0;
            dataset.CalcNormal();
            publishProgress(dataset.getAvance());

            Log.i("Hay-calc.-Doinback","Avance: " +dataset.getAvance());
            Log.i("Hay-calc.-Doinback","El tamaño de la lista de valor proyectados es:" +dataset.sizeprojectlist());
            //Avance: 1;
            dataset.Calc_MOM_LogNormal2P();
            publishProgress(dataset.getAvance());
            Log.i("Hay-calc.-Doinback","Avance: " +dataset.getAvance());
            Log.i("Hay-calc.-Doinback","El tamaño de la lista de valor proyectados es:" +dataset.sizeprojectlist());

            //Avance: 2;
            dataset.CalcML_LNormal2p();
            publishProgress(dataset.getAvance());
            Log.i("Hay-calc.-Doinback","Avance: " +dataset.getAvance());
            Log.i("Hay-calc.-Doinback","El tamaño de la lista de valor proyectados es:" +dataset.sizeprojectlist());

            //Avance: 3;
            dataset.CalcMOM_LNormal3p();
            publishProgress(dataset.getAvance());
            Log.i("Hay-calc.-Doinback","Avance: " +dataset.getAvance());
            Log.i("Hay-calc.-Doinback","El tamaño de la lista de valor proyectados es:" +dataset.sizeprojectlist());

            //Avance: 4;
            dataset.CalcML_LNormal3P();
            publishProgress(dataset.getAvance());

            Log.i("Hay-calc.-Doinback","Avance: " +dataset.getAvance());
            Log.i("Hay-calc.-Doinback","El tamaño de la lista de valor proyectados es:" +dataset.sizeprojectlist());

            //Avance: 5;
            dataset.CalcMOM_Gumbel();
            publishProgress(dataset.getAvance());
            Log.i("Hay-calc.-Doinback","Avance: " +dataset.getAvance());
            Log.i("Hay-calc.-Doinback","El tamaño de la lista de valor proyectados es:" +dataset.sizeprojectlist());

            //Avance: 6;
            dataset.CalcML_Gumbel();
            publishProgress(dataset.getAvance());
            Log.i("Hay-calc.-Doinback","Avance: " +dataset.getAvance());
            Log.i("Hay-calc.-Doinback","El tamaño de la lista de valor proyectados es:" +dataset.sizeprojectlist());

            //Avance: 7;
            dataset.CalcMOM_Exp();
            publishProgress(dataset.getAvance());
            Log.i("Hay-calc.-Doinback","Avance: " +dataset.getAvance());
            Log.i("Hay-calc.-Doinback","El tamaño de la lista de valor proyectados es:" +dataset.sizeprojectlist());

            //Avance: 8;
            dataset.CalcML_Exp();
            publishProgress(dataset.getAvance());
            Log.i("Hay-calc.-Doinback","Avance: " +dataset.getAvance());
            Log.i("Hay-calc.-Doinback","El tamaño de la lista de valor proyectados es:" +dataset.sizeprojectlist());

            //Avance: 9;
            dataset.CalcMOM_G2P();
            publishProgress(dataset.getAvance());
            Log.i("Hay-calc.-Doinback","Avance: " +dataset.getAvance());
            Log.i("Hay-calc.-Doinback","El tamaño de la lista de valor proyectados es:" +dataset.sizeprojectlist());

            //Avance: 10;
            dataset.CalcML_G2P();
            publishProgress(dataset.getAvance());
            Log.i("Hay-calc.-Doinback","Avance: " +dataset.getAvance());
            Log.i("Hay-calc.-Doinback","El tamaño de la lista de valor proyectados es:" +dataset.sizeprojectlist());

            //Avance: 11;
            dataset.CalcMOM_G3P();
            publishProgress(dataset.getAvance());
            Log.i("Hay-calc.-Doinback","Avance: " +dataset.getAvance());
            Log.i("Hay-calc.-Doinback","El tamaño de la lista de valor proyectados es:" +dataset.sizeprojectlist());

            //Avance: 12;
            dataset.CalcML_G3P();
            publishProgress(dataset.getAvance());
            Log.i("Hay-calc.-Doinback","Avance: " +dataset.getAvance());
            Log.i("Hay-calc.-Doinback","El tamaño de la lista de valor proyectados es:" +dataset.sizeprojectlist());

            //Avance: 13;
            dataset.findmin();
            publishProgress(dataset.getAvance());


            //Avance = 14;
            dataset.fillfitted();
            publishProgress(dataset.getAvance());
            Log.i("Hay-calc.-Doinback","Avance: " +dataset.getAvance());
            Log.i("Hay-calc.-Doinback","El tamaño de la lista de valor proyectados es:" +dataset.sizeprojectlist());
            dataset.showresults();

            dataset.setEncalculo(0);
            dataset.setIsCalculated(1);

            Log.i("Hay-calc.","El tamaño de la lista de valor proyectados es:" +dataset.sizeprojectlist());
            //           }//
            return "Task Completed.";
        }
        @Override
        protected void onPostExecute(String result) {
            mProgressBar.setVisibility(View.GONE);
            mTVTask.setText("");
            Intent Resultados = new Intent(context, AllResults.class);
            Resultados.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            Toaster mToaster = new Toaster(context);
            mToaster.showCustomToastLong("Opening results...", R.color.success_blue);
            context.startActivity(Resultados);
            finish();
        }
        @Override
        protected void onPreExecute() {
            mTVTask.setText("Task Starting...");
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            switch (values[0]){
                case 0:        //0 - EEA de la distribución normal, MOM.
                    mTVTask.setText("Analyzing Normal");
                    break;
                case 1:        //1 EEA de la distribución LN, MOM con dos parámetros.
                    mTVTask.setText("Analyzing LogNormal 2P with MOM");
                    //mTVCalculated.append("Calculated FDP: \"");
                    //mTVCalculated.append("Normal \"");
                    break;

                case 2:        //2 EEA de la distribución LN 2P, MML
                    mTVTask.setText("Analyzing LogNormal 2P with MML");
                    //mTVCalculated.append("LogNormal 2P with MOM \"");
                    break;

                case 3:        //3 EEA de la distribución Gumbel MOM
                    mTVTask.setText("Analyzing LogNormal 3P with MOM");
                    //mTVCalculated.append("LogNormal 2P with MML");
                    break;

                case 4:        //4 EEA de la distribución LogNormal 3P with MML
                    mTVTask.setText("Analyzing LogNormal 3P with MML");
                    break;

                case 5:        //5 EEA de la distribución Gumbel with MOM
                    mTVTask.setText("Analyzing Gumbel with MOM");
                    break;
                case 6:        //6 EEA de la distribución Gumbel with MML
                    mTVTask.setText("Analyzing Gumbel with MML");
                    break;

                case 7:             //7 EEA de la distribución Exponential with MOM
                    mTVTask.setText("Analyzing Exponential with MOM");
                    break;

                case 8:        //8 EEA de la distribución Exponential with MML
                    mTVTask.setText("Analyzing Exponential with MML");
                    break;

                case 9:         //9 EEA de la distribución Gamma 2P with MOM
                    mTVTask.setText("Analyzing Gamma 2P with MOM");
                    break;

                case 10:         //10 EEA de la distribución Gamma 2P with ML
                    mTVTask.setText("Analyzing Gamma 2P with ML");
                    break;

                case 11:         //11 EEA de la distribución Gamma 3P with MOM
                    mTVTask.setText("Analyzing Gamma 3P with MOM");
                    break;

                case 12:         //12 EEA de la distribución Gamma 3P with MML
                    mTVTask.setText("Analyzing Gamma 3P with MML");
                    break;
                case 13:
                    mTVTask.setText("Finding best fitting function");
                    break;
                case 14:
                    mTVTask.setText("Forecasting with selected FDP");
                    break;
            }
            //txt.setText("Running..."+ values[0]);
            mProgressBar.setProgress(values[0]);

        }
    }


}

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


package com.per.hikra.results;

import android.icu.text.NumberFormat;
import android.os.Bundle;

import android.renderscript.Sampler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.annotation.Nullable;

import com.per.hikra.R;
import com.per.hikra.statistics.Statistics;
import com.per.hikra.utils.Constants;


public class FittedDataFragment extends Fragment {

    private static String TAG = "FittedDataFrag";
    private String TXT, Fdp;
    //private WebView WvFitTable;
    private TextView FittedTitle;
    private LinearLayout mLLayoutTable;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_fitted_data, container, false);


        FittedTitle = (TextView) view.findViewById(R.id.TVFdp);
        FittedTitle.setText(Fdp);
        Statistics dataset = (Statistics) getContext().getApplicationContext();

        int size = dataset.getDataList().size();

        LinearLayout mLayout = (LinearLayout) view.findViewById(R.id.lLayoutFittedData);

        TableLayout mTable = new TableLayout(getContext());

        TableRow rowHeader = new TableRow(getContext());
        rowHeader.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.azul_perval));

        for(int i=0; i<4; i++){
            TextView mTv = new TextView(getContext());
            mTv.setText("Hola mundo" + i) ;
            mTv.setTextColor(ContextCompat.getColor(getContext(), R.color.white));

            mTv.setTextSize(18);
            mTv.setGravity(Gravity.CENTER);
            mTv.setMinWidth(60);
            mTv.setPadding(5, 5, 5, 5);
            switch (i){
                case 0:
                    mTv.setText("Tr");
                    break;
                case 1:
                    mTv.setText(Constants.XLS_VALUE_HEADER);
                    break;
                case 2:
                    mTv.setText(Constants.XLS_FITTED_HEADER);
                    break;
                case 3:
                    mTv.setText(Constants.XLS_ERROR_HEADER);
                    break;
            }

            rowHeader.addView(mTv);
        }

        mTable.addView(rowHeader);

        for(int j=0; j<size; j++){
            TableRow mRow = new TableRow(getContext());

            if((j%2)!=0){
                mRow.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.row_odd));
            }


            /**
             *
             *   double valor1 = getTi(j);
             *             double valor2 = getVi(j);
             *             double valor3 = getFiti(j);
             *             double valor4 = getEi(j);
             */
            double mTr =dataset.getTi(j);
            double mValue = dataset.getVi(j);
            double mFitted = dataset.getFiti(j);
            double mError = dataset.getEi(j);


            NumberFormat nf = NumberFormat.getInstance();
            String[] arrayValues = new String[4];



            //arrayValues[0] = nf.format(mTr);
            arrayValues[0] = String.format("%.3f",mTr);
            arrayValues[1]= nf.format(mValue);
            arrayValues[2]= nf.format(mFitted);
            arrayValues[3] = String.format("%.3f",mError);



            for(int i=0; i<4; i++){
                TextView mTv = new TextView(getContext());
                mTv.setTextColor(ContextCompat.getColor(getContext(), R.color.azul_perval));
                mTv.setTextSize(16);
                mTv.setGravity(Gravity.CENTER);
                mTv.setMinWidth(60);
                mTv.setPadding(5, 5, 5, 5);
                mTv.setText(arrayValues[i]);
                mRow.addView(mTv);
            }
            mTable.addView(mRow);
        }



        mLayout.addView(mTable);
        //Tr
        //Value
        //Fitted
        //Error


        Log.i(TAG, "Adding table to linear layout");



        return view;


    }

    public void setFitted(String texto){

        TXT = "";
        TXT = texto;
    }

    public void setFdp(String funcion){

        Fdp = funcion;
    }

}

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

import android.graphics.Color;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.text.Layout;
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

import java.util.Random;


public class ProjectedDataFragment extends Fragment {
    private String Projected = "";

    private String TAG = "ProjectedData";
    private int rowNumber ;
    //private WebView ProjTable;
    private TextView titleproj;
    private String titleP;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_projected_data, container, false);


        Statistics dataset = (Statistics) getContext().getApplicationContext();

        TableLayout table = new TableLayout(getContext());

        //table.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.border));

        table.setHorizontalGravity(LinearLayout.HORIZONTAL);

        TableRow headerRow = new TableRow(getContext());

        //headerRow.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.border));
        TextView trView, pxView, valueView;

        trView = new TextView(getContext());
        pxView = new TextView(getContext());
        valueView = new TextView(getContext());

        trView.setTextSize(18);
        pxView.setTextSize(18);
        valueView.setTextSize(18);
        trView.setGravity(Gravity.CENTER);
        pxView.setGravity(Gravity.CENTER);
        valueView.setGravity(Gravity.CENTER);

        trView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.azul_perval));
        pxView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.azul_perval));
        valueView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.azul_perval));
        trView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        pxView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        valueView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));


        int padding = 5;
        trView.setPadding(padding,padding,padding,padding);
        pxView.setPadding(padding,padding,padding,padding);
        valueView.setPadding(padding,padding,padding,padding);

        trView.setText("Period");
        pxView.setText(Constants.XLS_PX_HEADER);
        valueView.setText(Constants.XLS_VALUE_HEADER);

        headerRow.addView(trView);
        headerRow.addView(pxView);
        headerRow.addView(valueView);

        table.addView(headerRow);
        rowNumber = dataset.getProjectedValuesList().size();
        Log.i(TAG, "rowNumber: " + rowNumber);
        Log.i(TAG, "Populating table");
        for (int i=0; i < rowNumber; i++) {
            Log.i(TAG, "row: "+ i );
            TableRow row = new TableRow(getContext());

            if((i%2)!=0){
                row.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.row_odd));
            }

            double mPeriodo =dataset.getProjectedValuesList().get(i).getPeriodo();
            double mPx = dataset.getProjectedValuesList().get(i).getPx();
            double prjValue = dataset.getProjectedValuesList().get(i).getFittedV();

            NumberFormat nf = NumberFormat.getInstance();
            String val1 = nf.format(mPeriodo);
            String val2 = String.format("%.5f",mPx);
            String val3 = nf.format(prjValue);

            for (int j=0; j < 3; j++) {
                Random random = new Random();
                int value = random.nextInt(100) + 1;
                TextView tv = new TextView(getContext());
                tv.setText(String.valueOf(value));
                tv.setTextSize(16);
                tv.setGravity(Gravity.CENTER);
                tv.setMinWidth(60);
                tv.setPadding(5, 5, 5, 5);
                switch (j){
                    case 0:
                        tv.setText(val1);
                        break;

                    case 1:
                        tv.setText(val2);
                        break;
                    case 2:
                        tv.setText(val3);
                        break;

                        default:
                            break;
                }
                row.addView(tv);

            }

            table.addView(row);
        }
        LinearLayout mainLayout = view.findViewById(R.id.mainLayout);



        titleproj = (TextView) view.findViewById(R.id.TVProjected);
        titleproj.setText(titleP);


        mainLayout.addView(table);


        return view;
    }


    public void setProjected(String textoin){
        Projected = "";
        Projected = textoin;

    }

    public void setTitleproj(String title){

        titleP = "Projected values with "+title;
    }

}

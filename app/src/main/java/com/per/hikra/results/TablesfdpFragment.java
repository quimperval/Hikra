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


import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import androidx.annotation.Nullable;

import com.per.hikra.R;

import java.util.ArrayList;


public class TablesfdpFragment extends Fragment {
    WebView WvTables;
    TextView TituloE, Textparam;
    private ArrayList<String> tablafunciones;
    private Spinner espinfdp;
    private int pos;
    private Button settable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_tablesfdp, container, false);


        String[] values = {"Normal Distribution",  // 0
                "LogNormal 2P by MOM", //1
                "LogNormal 2P by MML", //2
                "LogNormal 3P by MOM", //3
                "LogNormal 3P by MML", //4
                "Gumbel by MOM", //5
                "Gumbel by MML",  //6
                "Exponential by MOM", //7
                "Exponential by MML", //8
                "Gamma 2P by MOM", //9
                "Gamma 2P by MML", //10
                "Gamma 3P by MOM", //11
                "Gamma 3P by MML" //12
                };

        espinfdp = (Spinner) view.findViewById(R.id.spinnerfdp);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, values);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        espinfdp.setAdapter(adapter);

        espinfdp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                setpos(i);
                Log.i("Hay-TablesfdpFragment", "Posición de spiner:"+pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        settable = (Button) view.findViewById(R.id.button10);
        WvTables = (WebView) view.findViewById(R.id.WvTable);

        settable.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view){
                    String contenidotabla;

                    contenidotabla = tablafunciones.get(getpos());

                    WvTables.loadDataWithBaseURL(null,contenidotabla,"text/html","utf-8",null);
                    Log.i("Hay-TablesfdpFragment", "Posición de spiner:"+pos);
                    Log.i("Hay-TablesfdpFragment", "contenido de tabla"+contenidotabla);

                }
            }

        );



        return view;
    }






    public void setTablafunciones(ArrayList<String> tabla){
        tablafunciones = tabla;
    }

    public void setpos(int j){

        pos = j;
    }


    public int getpos()
    {
        return pos;

    }
}

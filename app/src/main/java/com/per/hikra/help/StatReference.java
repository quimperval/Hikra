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

package com.per.hikra.help;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import androidx.annotation.Nullable;
import com.per.hikra.R;


public class StatReference extends Fragment {


    private String Projected;

    private WebView Reference;


    private TextView Texto1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statref, container, false);

        Reference = (WebView) view.findViewById(R.id.WvSref);


        String StrTXT1;
        StrTXT1 = "Main references to develop the statistics calculation engine were the books \n<br> <br>" +
                "- Aparicio, F., (1988), Fundamentos de hidrología de superficie. México, Limusa. <br>"+
                "- Ramachandra, Rao., (2000), Flood Frequency Analysis. U.S. CRC Press.  \n<br>" +
                "- Campos Aranda, Daniel., (2006),  Analisis probabilistico Univariado de datos hidrologicos. México: IMTA. \n<br> <br>"+
                "\nFor validation purpose were compared our results for different datasets with those obtained with the old 16-bit" +
                " programm \"AX\"developed by the CENAPRED, and with examples shown in the reference books.\n \n \n<br> <br>"+
                "Special thanks to engineer Daniel Campos Aranda who helped to clear doubts about" +
                " some Density Probability Fuctions.\n\n<br> <br>"+
                "Source code of this app is available at GitHub, in this way you can check statistic engine and add"+
                " your own methods for calc or show results.";

        Reference.loadDataWithBaseURL(null,StrTXT1,"text/html","utf-8",null);

        /**
         * Código del banner
         */


        /**
         *
         * Código del banner
         */


        return view;


    }
}

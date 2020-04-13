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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;


import androidx.fragment.app.Fragment;
import androidx.annotation.Nullable;

import com.per.hikra.R;

public class About extends Fragment implements View.OnClickListener {

    private Button contact;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        TextView AboutIntro = view.findViewById(R.id.tvAbIntro);
        String Intro = "Hikra is intended as a tool to help proffesionals"+
                " who deals with historical hydrometeorological data (as flood and rainfall) to fit it to different probability density functions "+
                "with the Methods of Moments (MOM) and Maximum Likelihood (MML).\n"+
                "This app is released for free download and use. ";
        AboutIntro.setText(Intro);

        TextView About = view.findViewById(R.id.tvAbout);
        String contents = "Hikra v2.0 is developed by Joaquín Pérez Valera\n"+
                "Copyright (c) 2018\n"+
                "And Released under the MIT License";
        About.setText(contents);


        contact = (Button) view.findViewById(R.id.ContactBut);

       contact.setOnClickListener((View.OnClickListener) this);

        return view;


    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ContactBut:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","joaquinperezvalera@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Suggestions and comments");
                emailIntent.putExtra(Intent.EXTRA_TEXT, " ");
                startActivity(Intent.createChooser(emailIntent, " "));
                break;

        }
    }
}

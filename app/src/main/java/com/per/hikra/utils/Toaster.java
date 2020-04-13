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

package com.per.hikra.utils;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.per.hikra.R;

public class Toaster {

    Context mContext;

    public Toaster(Context mContext){
        this.mContext = mContext;
    }


    public void showCustomToast(String message){
        Toast toast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
        View view = toast.getView();
        view.setBackgroundResource(android.R.drawable.toast_frame);
        view.setBackgroundColor(ContextCompat.getColor(mContext,R.color.red));
        TextView text = view.findViewById(android.R.id.message);

        text.setTextColor(ContextCompat.getColor(mContext,R.color.white));
        toast.show();
    }

    public void showCustomToast(String message, int mBgColor){
        Toast toast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
        View view = toast.getView();
        view.setBackgroundResource(android.R.drawable.toast_frame);
        view.setBackgroundColor(mBgColor);
        TextView text = view.findViewById(android.R.id.message);


        text.setTextColor(ContextCompat.getColor(mContext,R.color.white));
        toast.show();
    }

    public void showCustomToastLong(String message, int mBgColor){
        Toast toast = Toast.makeText(mContext, message, Toast.LENGTH_LONG);
        View view = toast.getView();
        view.setBackgroundResource(android.R.drawable.toast_frame);
        view.setBackgroundColor(mBgColor);
        TextView text = view.findViewById(android.R.id.message);


        text.setTextColor(ContextCompat.getColor(mContext,R.color.white));
        toast.show();
    }
}

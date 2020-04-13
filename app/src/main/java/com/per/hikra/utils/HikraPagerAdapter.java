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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class HikraPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> listOfFragments = null;
    private ArrayList<String> listOfFragmentTitles = null;

    private Context mContext;

    public HikraPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        listOfFragmentTitles = new ArrayList<String>();
        listOfFragments = new ArrayList<Fragment>();
    }

    public HikraPagerAdapter(@NonNull FragmentManager fm, Context mContext) {
        super(fm);
        this.mContext = mContext;
        listOfFragments = new ArrayList<Fragment>();
        listOfFragmentTitles = new ArrayList<String>();
    }



    public HikraPagerAdapter(@NonNull FragmentManager fm, ArrayList<Fragment> listOfFragments, ArrayList<String> listOfFragmentTitles, Context mContext) {
        super(fm);
        this.listOfFragments = listOfFragments;
        this.mContext = mContext;
        this.listOfFragmentTitles = listOfFragmentTitles;
    }

    public void AddFragment(Fragment fragment){
        if(listOfFragments==null){
            listOfFragments = new ArrayList<Fragment>();
        }
        this.listOfFragments.add(fragment);

    }

    public void AddFragment(Fragment fragment, String title){
        this.listOfFragments.add(fragment);
        this.listOfFragmentTitles.add(title);
    }



    @NonNull
    @Override
    public Fragment getItem(int position) {
        return listOfFragments.get(position);
    }

    @Override
    public int getCount() {
        return listOfFragments.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {

        return listOfFragmentTitles.get(position);
    }
}

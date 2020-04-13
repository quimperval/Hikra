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



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class Pagerhelp extends FragmentPagerAdapter {

    ArrayList<Fragment> mFragmentList;
    ArrayList<String>   mFragmentTitleList;

    int tabCount;
    String Errores1;
    String TXTFdp;
    String Fitted;
    String TXProjected;


    public Pagerhelp(FragmentManager fmng){
        super(fmng);
        mFragmentList = new ArrayList<>();
        mFragmentTitleList = new ArrayList<>();

    }

    public Pagerhelp(FragmentManager fm, int tabCount, String Errores, String FuncionProb, String datafit, String proy){
        super(fm);
        this.tabCount=tabCount;
        Errores1 = Errores;
        TXTFdp = FuncionProb;
        Fitted = datafit;
        TXProjected = "";

        TXProjected = proy;
    }

    public void AddFragment(Fragment fragment, String title){
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }


    //For android jetpack
    //If we don't write it, we don't get the title

    @Nullable
    @Override
    public CharSequence getPageTitle(int position){
        return mFragmentTitleList.get(position);
    }

    public void replaceFragment(int pos, Fragment mFragment){
        //Log.i("DWPageAdapter", "inside replaceFragment");
        mFragmentList.set(pos, mFragment);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}

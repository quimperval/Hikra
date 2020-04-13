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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.Nullable;

import com.per.hikra.R;

import java.util.ArrayList;

import io.github.kexanie.library.MathView;

public class DensityPFFragment extends Fragment {

    private ArrayList<String> AllFDPData = new ArrayList<>();
    //Element 0: Mean, standard deviation.
    MathView MV_basic;
    //Element 1: Formula FDP Normal.
    MathView MV_Normal;
    //Element 2: Parameters FDP Normal
    MathView MV_pNormal;

    //Element 3: Formula LogNormal 2P, MOM
    MathView MV_LN2P;
    //Element 4: Parameters LogNormal 2P, MOM
    MathView MV_pLN2PMOM;
    //Element 5: Parameters LogNormal 2P, ML
    MathView MV_pLN2PML;

    //Element 6: Formula LogNormal 3P, MOM
    MathView MV_LN3P;
    //Element 7: Parameters LogNormal 3P, MOM
    MathView MV_pLN3PMOM;
    //Element 8: Parameters LogNormal 3P, ML
    MathView MV_pLN3PML;

    //Element 9: Formula Gumbel MOM
    MathView MV_Gumbel;
    //Element 10: Parameters Gumbel MOM
    MathView MV_pGumbelMOM;
    //Element 11: Parameters Gumbel ML
    MathView MV_pGumbelML;

    //Element 12: Formula EXP MOM
    MathView MV_EXP;
    //Element 13: Parameters EXP MOM
    MathView MV_pEXPMOM;
    //Element 14: Parameters EXP ML
    MathView MV_pEXPML;

    //Element 15: Formula Gamma 2P MOM
    MathView MV_G2P;
    //Element 16: Parameters Gamma 2P MOM
    MathView MV_pG2PMOM;
    //Element 17: Parameters Gamma 2P ML
    MathView MV_pG2PML;

    //Element 18: Formula Gamma 3P MOM
    MathView MV_G3P;
    //Element 19: Parameters Gamma 3P MOM
    MathView MV_pG3PMOM;
    //Element 20: Parameters Gamma 3P ML
    MathView MV_pG3PML;




    MathView formula_one, formula_two;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_alldpf, container, false);

        MV_basic = view.findViewById(R.id.FormBasic);
        MV_basic.setText(AllFDPData.get(0));

        MV_Normal = view.findViewById(R.id.FormNormal);
        MV_Normal.setText(AllFDPData.get(1));

        MV_pNormal = view.findViewById(R.id.form_paramNormal);
        MV_pNormal.setText(AllFDPData.get(2));

        /////////////LN 2P
        MV_LN2P = view.findViewById(R.id.form_LN2P);
        MV_LN2P.setText(AllFDPData.get(3));

        MV_pLN2PMOM = view.findViewById(R.id.form_paramLN2PMOM);
        MV_pLN2PMOM.setText(AllFDPData.get(4));

        MV_pLN2PML = view.findViewById(R.id.form_paramLN2PML);
        MV_pLN2PML.setText(AllFDPData.get(5));


        ///////////LN 3P
        MV_LN3P = view.findViewById(R.id.form_LN3P);
        MV_LN3P.setText(AllFDPData.get(6));

        MV_pLN3PMOM = view.findViewById(R.id.form_paramLN3PMOM);
        MV_pLN3PMOM.setText(AllFDPData.get(7));

        MV_pLN3PML = view.findViewById(R.id.form_paramLN3PML);
        MV_pLN3PML.setText(AllFDPData.get(8));

        ////////////Gumbel
        MV_Gumbel = view.findViewById(R.id.form_Gumbel);
        MV_Gumbel.setText(AllFDPData.get(9));

        MV_pGumbelMOM = view.findViewById(R.id.form_paramGumbelMOM);
        MV_pGumbelMOM.setText(AllFDPData.get(10));

        MV_pGumbelML = view.findViewById(R.id.form_paramGumbelML);
        MV_pGumbelML.setText(AllFDPData.get(11));

        ////////////Exponencial
        MV_EXP = view.findViewById(R.id.form_EXP);
        MV_EXP.setText(AllFDPData.get(12));

        MV_pEXPMOM = view.findViewById(R.id.form_paramEXPMOM);
        MV_pEXPMOM.setText(AllFDPData.get(13));

        MV_pEXPML = view.findViewById(R.id.form_paramEXPML);
        MV_pEXPML.setText(AllFDPData.get(14));

        /////////////////Gamma 2p
        MV_G2P = view.findViewById(R.id.form_G2P);
        MV_G2P.setText(AllFDPData.get(15));

        MV_pG2PMOM = view.findViewById(R.id.form_paramG2PMOM);
        MV_pG2PMOM.setText(AllFDPData.get(16));

        MV_pG2PML = view.findViewById(R.id.form_paramG2PML);
        MV_pG2PML.setText(AllFDPData.get(17));

        ////////////////Gamma 3p

        MV_G3P = view.findViewById(R.id.form_G3P);
        MV_G3P.setText(AllFDPData.get(18));

        MV_pG3PMOM = view.findViewById(R.id.form_paramG3PMOM);
        MV_pG3PMOM.setText(AllFDPData.get(19));

        MV_pG3PML = view.findViewById(R.id.form_paramG3PML);
        MV_pG3PML.setText(AllFDPData.get(20));


        return view;
    }


    public void setAllFDPData(ArrayList<String> DataList){
        AllFDPData = DataList;
    }



}

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
import android.graphics.Typeface;
import android.os.Bundle;

import android.text.Html;
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

import io.github.kexanie.library.MathView;

public class ErrorSummaryFragment extends Fragment {
    WebView WvErrorSummary;
    TextView TituloE, Textparam;
    private String ErrorS; // Data in HTML format to show errors in a table. Will be the 0 element in the list.
    private String minfdp; //Min Error and name of the PDF. Will be the element 1 of the list.
    private String TEXFdp; //Equation of PDF in LaTeX Format. Will be the element 2 in the list.
    private String TEXparam; //Parameters of the PDF in Latex Format; Will be the element 3 of the list.
    private String pos; //Position in the list of PDF.



    private LinearLayout mLayoutResultsMOM, mLayoutResultsMLH;
    private TableLayout summaryTableMOM, summaryTableMLH;

    MathView formula_one, formula_two;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_error_summary, container, false);

        formula_one = view.findViewById(R.id.FormBasic);

        formula_one.setText(TEXFdp);
        mLayoutResultsMOM = (LinearLayout) view.findViewById(R.id.lLayoutSummaryResMOM);
        mLayoutResultsMLH = (LinearLayout) view.findViewById(R.id.lLayoutSummaryResMLH);

        summaryTableMOM = (TableLayout) new TableLayout(getContext());
        summaryTableMLH = (TableLayout) new TableLayout(getContext());

        //WvErrorSummary = (WebView) view.findViewById(R.id.WvBasic);
        //WvErrorSummary.loadDataWithBaseURL(null,ErrorS,"text/html","utf-8",null);

        populateSummaryTable();
        TituloE = (TextView) view.findViewById(R.id.TVErrors2);
//        Log.i("Hay-ErrorSummaryFragment",TXTResults);


        TituloE.setTextColor(Color.BLACK);
        TituloE.setText(Html.fromHtml(minfdp, Html.FROM_HTML_MODE_LEGACY));

        Textparam = (TextView) view.findViewById(R.id.TVParam);
        Textparam.setTextColor(Color.BLACK);

        int posnum = Integer.parseInt(pos);
        if(posnum<=6){
            Textparam.setText("with parameters estimated by the MOM");
        } else {
            Textparam.setText("with parameters estimated by the MML");
        }

        formula_two = view.findViewById(R.id.formula_two);
        formula_two.setText(TEXparam);

        return view;
    }



    public void setErrorS(String text){
        ErrorS = text;
    }

    public String getErrorS(){
        return ErrorS;
    }

    public void setTEXFdp(String Fdpin){

        TEXFdp = Fdpin;
    }

    public void setTEXparam(String paramin){

        TEXparam = paramin;
    }

    public void setMinfdp(String minin){

        minfdp = minin;
    }

    public void setposition(String pos_in){
        pos = pos_in;
    }


    private void populateSummaryTable(){
        Statistics dataset = (Statistics) getContext().getApplicationContext();
        int headerSize1 = 18;
        int bodyTextSize = 16;
        int headerSize2 = 16;
        int mPadd  =5;
        int maxWidth = 40;

        int mPaddZero = 3;
        //////TextViews of Functions and subheaders


        /**
         * * Functions
         *
         * Normal
         * LogNormal
         * Gumbel
         * Exponential
         * Gamma
         */

        TableRow.LayoutParams lpMatchWidth = new TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,1);

        TableRow.LayoutParams lpWrapCont = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT,0);

        TableLayout.LayoutParams tableWrap = new TableLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 0);

        LinearLayout.LayoutParams tvLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        summaryTableMOM.setLayoutParams(tableWrap);

        TextView FunctionsHeaderTV = new TextView(getContext());

        FunctionsHeaderTV.setTextSize(headerSize2);
        FunctionsHeaderTV.setText(Constants.XLS_TXT_FUNCTIONS);
        FunctionsHeaderTV.setGravity(Gravity.CENTER);
        FunctionsHeaderTV.setTypeface(null, Typeface.BOLD);
        FunctionsHeaderTV.setPadding(mPaddZero, mPaddZero, mPaddZero, mPaddZero);
        //FunctionsHeaderTV.setLayoutParams(tvLayoutParams);
        FunctionsHeaderTV.setMaxWidth(maxWidth);


        TextView FunctionsHeader2TV = new TextView(getContext());

        FunctionsHeader2TV.setTextSize(headerSize2);
        FunctionsHeader2TV.setText(Constants.XLS_TXT_FUNCTIONS);
        FunctionsHeader2TV.setGravity(Gravity.CENTER);
        FunctionsHeader2TV.setTypeface(null, Typeface.BOLD);
        FunctionsHeader2TV.setPadding(mPaddZero, mPaddZero, mPaddZero, mPaddZero);
        //FunctionsHeader2TV.setLayoutParams(tvLayoutParams);
        FunctionsHeader2TV.setMaxWidth(maxWidth);

        //TextViews in subheaders

        String twoParmsStr = "2 Params";
        String threeParmsStr = "3 Params";

        TextView twoParms = new TextView(getContext());
        twoParms.setTextSize(headerSize2);
        twoParms.setText(twoParmsStr);
        twoParms.setGravity(Gravity.CENTER);
        twoParms.setPadding(mPadd, mPadd, mPadd, mPadd);
        twoParms.setTypeface(null, Typeface.BOLD);
        //twoParms.setLayoutParams(lpWrapCont);

        TextView threeParms = new TextView(getContext());
        threeParms.setTextSize(headerSize2);
        threeParms.setText(threeParmsStr);
        threeParms.setGravity(Gravity.CENTER);
        threeParms.setPadding(mPadd, mPadd, mPadd, mPadd);
        threeParms.setTypeface(null, Typeface.BOLD);
        //threeParms.setLayoutParams(lpWrapCont);

        TextView twoParms2 = new TextView(getContext());
        twoParms2.setTextSize(headerSize2);
        twoParms2.setText(twoParmsStr);
        twoParms2.setGravity(Gravity.CENTER);
        twoParms2.setPadding(mPadd, mPadd, mPadd, mPadd);
        twoParms2.setTypeface(null, Typeface.BOLD);
        //twoParms2.setLayoutParams(lpWrapCont);


        TextView threeParms2 = new TextView(getContext());
        threeParms2.setTextSize(headerSize2);
        threeParms2.setText(threeParmsStr);
        threeParms2.setGravity(Gravity.CENTER);
        threeParms2.setPadding(mPadd, mPadd, mPadd, mPadd);
        threeParms2.setTypeface(null, Typeface.BOLD);
        //threeParms2.setLayoutParams(lpWrapCont);

        //SubHeader Row
        TableRow subHeader = new TableRow(getContext());
        subHeader.setGravity(Gravity.CENTER);
        subHeader.addView(FunctionsHeaderTV);
        subHeader.addView(twoParms);
        subHeader.addView(threeParms);

        TableRow subHeader2 = new TableRow(getContext());
        subHeader2.setGravity(Gravity.CENTER);
        subHeader2.addView(FunctionsHeader2TV);
        subHeader2.addView(twoParms2);
        subHeader2.addView(threeParms2);


        //////Method of Moments Header
        TableRow MOMHeader = new TableRow(getContext());

        TextView MomHeaderTV = new TextView(getContext());

        MomHeaderTV.setText(Constants.XLS_TXT_MOMENTS);
        MomHeaderTV.setTextColor(ContextCompat.getColor(getContext(), R.color.azul_perval));
        MomHeaderTV.setGravity(Gravity.CENTER);
        MomHeaderTV.setTextSize(headerSize1);
        MomHeaderTV.setLayoutParams(lpMatchWidth);
        MomHeaderTV.setTypeface(null, Typeface.BOLD);

        MOMHeader.addView(MomHeaderTV);


        //////////Maximum Likelihood Header

        TableRow MLHHeader = new TableRow(getContext());

        TextView MlhHeaderTV = new TextView(getContext());

        MlhHeaderTV.setText(Constants.XLS_TXT_MLH);
        MlhHeaderTV.setTextColor(ContextCompat.getColor(getContext(), R.color.azul_perval));
        MlhHeaderTV.setGravity(Gravity.CENTER);
        MlhHeaderTV.setTextSize(headerSize1);
        MlhHeaderTV.setLayoutParams(lpMatchWidth);
        MlhHeaderTV.setTypeface(null, Typeface.BOLD);

        MLHHeader.addView(MlhHeaderTV);
        //////Populating Table


        summaryTableMOM.addView(subHeader);

        for(int i =0; i<5; i++){
            TableRow RowContent = new TableRow(getContext());
           // RowContent.setLayoutParams(lpWrapCont);

            for(int j=0; j<3; j++) {
                TextView mTextV = new TextView(getContext());
                mTextV.setTextSize(bodyTextSize);
                //mTextV.setLayoutParams(tvLayoutParams);
                mTextV.setPadding(mPadd,mPadd,mPadd,mPadd);

                mTextV.setGravity(Gravity.CENTER);
                mTextV.setTextColor(ContextCompat.getColor(getContext(), R.color.azul_perval));

                switch (j){

                    case 0:
                        mTextV.setPadding(mPaddZero,mPaddZero,mPaddZero,mPaddZero);
                        //mTextV.setMaxWidth(maxWidth);

                        switch (i){
                            case 0:
                                mTextV.setText(Constants.XLS_TXT_NORMAL);
                                break;
                            case 1:
                                mTextV.setText(Constants.XLS_TXT_LOGNORMAL);
                                break;

                            case 2:
                                mTextV.setText(Constants.XLS_TXT_GUMBEL);
                                break;
                            case 3:
                                mTextV.setText(Constants.XLS_TXT_EXP);
                                break;
                            case 4:
                                mTextV.setText(Constants.XLS_TXT_GAMMA);
                                break;

                            default:
                                break;
                        };
                        break;
                    case 1:
                        switch (i){
                            case 0:
                                mTextV.setText( String.format("%.3f",dataset.getMOM_NError()));
                                break;
                            case 1:
                                mTextV.setText( String.format("%.3f",dataset.getMOM_LN2PError()));
                                break;

                            case 2:
                                mTextV.setText( String.format("%.3f",dataset.getMOM_GumbelError()));
                                break;
                            case 3:
                                mTextV.setText( String.format("%.3f",dataset.getMOM_EXPError()));
                                break;
                            case 4:
                                mTextV.setText( String.format("%.3f",dataset.getMOM_G2PError()));
                                break;

                            default:

                                break;
                        };
                        break;
                    case 2:
                        switch (i){
                            case 0:
                                mTextV.setText( "-----");
                                break;
                            case 1:
                                mTextV.setText( String.format("%.3f",dataset.getMOM_LN3PError()));
                                break;

                            case 2:
                                mTextV.setText( "-----");
                                break;
                            case 3:
                                mTextV.setText( "-----");
                                break;
                            case 4:
                                mTextV.setText( String.format("%.3f", dataset.getMOM_G3PError()));
                                break;

                            default:
                                break;
                        };
                        break;


                    default:
                            break;
                }
                RowContent.addView(mTextV);
            }

            if((i%2)==0){
                RowContent.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.row_odd));
            }
            summaryTableMOM.addView(RowContent);
        }





        summaryTableMLH.addView(subHeader2);

        for(int i =0; i<5; i++){
            TableRow RowContent = new TableRow(getContext());
            for(int j=0; j<3; j++){
                TextView mTextV = new TextView(getContext());
                mTextV.setTextSize(bodyTextSize);
                //mTextV.setLayoutParams(tvLayoutParams);
                mTextV.setPadding(mPadd,mPadd,mPadd,mPadd);
                mTextV.setGravity(Gravity.CENTER);
                mTextV.setTextColor(ContextCompat.getColor(getContext(), R.color.azul_perval));

                switch (j){
                    case 0:
                        switch (i){
                            case 0:
                                mTextV.setPadding(mPaddZero,mPaddZero,mPaddZero,mPaddZero);
                                //mTextV.setMaxWidth(maxWidth);
                                mTextV.setText(Constants.XLS_TXT_NORMAL);
                                break;
                            case 1:
                                mTextV.setText(Constants.XLS_TXT_LOGNORMAL);
                                break;

                            case 2:
                                mTextV.setText(Constants.XLS_TXT_GUMBEL);
                                break;
                            case 3:
                                mTextV.setText(Constants.XLS_TXT_EXP);
                                break;
                            case 4:
                                mTextV.setText(Constants.XLS_TXT_GAMMA);
                                break;

                            default:
                                break;
                        };
                        break;
                    case 1:
                        switch (i){
                            case 0:
                                mTextV.setText( String.format("%.3f",dataset.getMOM_NError()));
                                break;
                            case 1:
                                mTextV.setText( String.format("%.3f",dataset.getML_LN2PError()));
                                break;

                            case 2:
                                mTextV.setText( String.format("%.3f",dataset.getML_GumbelError()));
                                break;
                            case 3:
                                mTextV.setText( String.format("%.3f",dataset.getML_EXPError()));
                                break;
                            case 4:
                                mTextV.setText( String.format("%.3f", dataset.getML_G2PError()));
                                break;

                            default:

                                break;
                        };
                        break;
                    case 2:
                        switch (i){
                            case 0:
                                mTextV.setText( "-----");
                                break;
                            case 1:
                                mTextV.setText( String.format("%.3f",dataset.getML_LN3PError()));
                                break;

                            case 2:
                                mTextV.setText( "-----");
                                break;
                            case 3:
                                mTextV.setText( "-----");
                                break;
                            case 4:
                                mTextV.setText( String.format("%.3f",dataset.getML_G3PError()));
                                break;

                            default:
                                break;
                        };
                        break;


                    default:
                        break;
                }
                RowContent.addView(mTextV);
            }

            if((i%2)==0){
                RowContent.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.row_odd));
            }
            summaryTableMLH.addView(RowContent);
        }
        mLayoutResultsMOM.addView(summaryTableMOM);
        mLayoutResultsMLH.addView(summaryTableMLH);
    }
}
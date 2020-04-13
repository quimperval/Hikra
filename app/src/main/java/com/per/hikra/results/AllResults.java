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

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.RequiresApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import android.util.Log;
import android.view.Menu;

import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.per.hikra.utils.HikraPagerAdapter;

import com.per.hikra.R;
import com.per.hikra.statistics.Statistics;
import com.per.hikra.utils.Constants;
import com.per.hikra.utils.XLSHandler;

import java.io.File;
import java.util.ArrayList;

public class AllResults extends AppCompatActivity {

    private static final String TAG ="AllResults";
    private ViewPager mViewPager;

    private TabLayout mResultsTabLayout;

    private ArrayList<String> tablasfdp;
    private RelativeLayout mRelLayout;
    private ErrorSummaryFragment mErrorSummaryFrag = null;
    private FittedDataFragment mFittedDataFrag = null;
    private DensityPFFragment mDensityFragment = null;
    private ProjectedDataFragment mProjectedDataFrag = null;
    private TablesfdpFragment mTablesFragment= null;
    private String TXTFitted;
    private String TXTProj;

    private String projectName;
    private HikraPagerAdapter mHikraPagerAdapter = null;

    private String errorsHTMLContent = "";

    private ArrayList<String> listaalldata;
    private ArrayList<String> datosfdp;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_all_results);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);


            Statistics dataset = (Statistics) getApplication();

            projectName = dataset.getmProjectName();

            String Erroresin = "";

            Erroresin = dataset.getErrorSummary(); //Formato tabular de errores, elemento 0 de la lista.
            double minVal = dataset.getMinVal();

            String minfdp = "";
            minfdp = dataset.getminfdp();


            String minimo = "Minimum Standard Error: " + String.format("%.3f", minVal) + " " +
                    "calculated with the <br>" + minfdp;  //Corresponde al ErrorS de Error Summary. Elemento 1 de la lista.


            String eqmin = dataset.TEXfunction(); //Corresponde al elemento 2 de la lista.

            String param = "";
            param = dataset.TEXparam(); //Corresponde al elemento 3 de la lista.

            datosfdp = new ArrayList<>();

            String posinlist = String.valueOf(dataset.getPosmin());
            datosfdp.add(Erroresin);
            datosfdp.add(minimo);
            datosfdp.add(eqmin);
            datosfdp.add(param);
            datosfdp.add(posinlist);
            initializeErrorSummaryFragment();

            tablasfdp = dataset.gettableAllFdp();


            TXTFitted = dataset.getFittedData();

            TXTProj = dataset.getProjected();
            listaalldata = new ArrayList<>();
            listaalldata = dataset.setandgetlist();
            initializeFittedDataFragment();
            initializeProjectedDataFragment();
            initializePFDensityFragment();
            initializeTablesFragment();

            mHikraPagerAdapter = new HikraPagerAdapter(getSupportFragmentManager(), this);

            mHikraPagerAdapter.AddFragment(mErrorSummaryFrag, Constants.TITLE_TAB_ERRORS);
            mHikraPagerAdapter.AddFragment(mFittedDataFrag, Constants.TITLE_TAB_FITTED);
            mHikraPagerAdapter.AddFragment(mProjectedDataFrag, Constants.TITLE_TAB_PROJECTED);
            mHikraPagerAdapter.AddFragment(mDensityFragment, Constants.TITLE_TAB_PDF);
            mHikraPagerAdapter.AddFragment(mTablesFragment, Constants.TITLE_TAB_TABLES);

            mViewPager = (ViewPager) findViewById(R.id.viewPagerResults);

            mViewPager.setAdapter(mHikraPagerAdapter);

            mResultsTabLayout = (TabLayout) findViewById(R.id.tabs);

            mResultsTabLayout.setupWithViewPager(mViewPager);
        } catch (Exception e){

            Log.e(TAG, "Error on AllResults Activity");
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_results, menu);


        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.share:
                Toast.makeText(this, "Sharing results", Toast.LENGTH_SHORT).show();

                Intent emailIntent2 = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));

                emailIntent2.putExtra(android.content.Intent.EXTRA_SUBJECT, "Hikra Results of Project - " + projectName);

                XLSHandler mXLSHandler = new XLSHandler(this);
                mXLSHandler.createExcelWorkBook();
                String carpeta = "Hikra";

                File folder = new File( this.getExternalFilesDir(null).getAbsolutePath() + File.separator + carpeta);

                if(folder == null){
                    Toast.makeText(this, "XLS file can't be found", Toast.LENGTH_SHORT).show();
                } else {
                    String fileName = projectName + ".xls";
                    File xlsFile = new File(folder, fileName);

                    Uri uri = Uri.fromFile(xlsFile);
                    emailIntent2.putExtra(Intent.EXTRA_STREAM, uri);
                    startActivity(Intent.createChooser(emailIntent2, "Send mail..."));
                }



            default:
                return super.onOptionsItemSelected(item);
        }

    }


    private void initializeErrorSummaryFragment(){
        mErrorSummaryFrag = new ErrorSummaryFragment();
        mErrorSummaryFrag.setErrorS(datosfdp.get(0));
        mErrorSummaryFrag.setMinfdp(datosfdp.get(1));
        mErrorSummaryFrag.setTEXFdp(datosfdp.get(2));
        mErrorSummaryFrag.setTEXparam(datosfdp.get(3));
        mErrorSummaryFrag.setposition(datosfdp.get(4));
    }

    private void buildErrorContentHTML(){

        errorsHTMLContent  = datosfdp.get(0) + "<br>" +datosfdp.get(1) + "<br>" +datosfdp.get(2) + "<br>" + datosfdp.get(3) ;

    }



    private void initializeFittedDataFragment(){
        mFittedDataFrag=new FittedDataFragment();
        mFittedDataFrag.setFitted(TXTFitted);
        mFittedDataFrag.setFdp(listaalldata.get(21));
    }

    private void initializeProjectedDataFragment(){
        mProjectedDataFrag=new ProjectedDataFragment();
        mProjectedDataFrag.setProjected(TXTProj);
        mProjectedDataFrag.setTitleproj(listaalldata.get(21));
    }

    private void initializePFDensityFragment(){
        mDensityFragment = new DensityPFFragment();
        mDensityFragment.setAllFDPData(listaalldata);
    }

    private void initializeTablesFragment(){
        mTablesFragment= new TablesfdpFragment();
        mTablesFragment.setTablafunciones(tablasfdp);
    }

}

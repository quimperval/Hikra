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

package com.per.hikra.activities;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.per.hikra.R;
import com.per.hikra.adapters.VariableAdapter;
import com.per.hikra.help.Help;
import com.per.hikra.models.Variable;
import com.per.hikra.statistics.Statistics;
import com.per.hikra.utils.Constants;
import com.per.hikra.utils.FileHandler;
import com.per.hikra.utils.Toaster;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class ProjectView extends AppCompatActivity {


    private double[] listOfYears;
    private int isCalculated =0;
    private int isEditing = -1;

    private VariableAdapter mVariableAdapter;

    private EditText mETValue, mETYear;
    private EditText mProjectName;
    private LinearLayoutManager mLayoutManager;


    private RelativeLayout DataListContainer;
    private TextView NoDataTV;
    ArrayList<Variable> listOfValues;
    RecyclerView mRecyclerView;
    private Button mAddButton;
    private Button mCalcButton;

    private ImageButton mSaveButton;
    private static float TEXT_SIZE = 18;
    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_view);

        mAddButton = (Button) findViewById(R.id.addButton);
        mAddButton.setText(Constants.ADD_VALUE_TEXT);
        DataListContainer = (RelativeLayout) findViewById(R.id.rlDataList);
        NoDataTV = (TextView) findViewById(R.id.tvNoData);
        mSaveButton = (ImageButton) findViewById(R.id.saveButton);
        mCalcButton = (Button) findViewById(R.id.calculateButton);
        mProjectName = (EditText) findViewById(R.id.etProjectName);

        isEditing = getIntent().getIntExtra(Constants.STR_EXISTING_PROJECT,0);

        mETYear = (EditText) findViewById(R.id.etYear);
        mETValue = (EditText) findViewById(R.id.etValue);

        Statistics dataset = (Statistics) getApplication();

        Log.i("Project View", "isEditing value: " + isEditing);
        if(isEditing == Constants.EXISTING_PROJECT_1 || isEditing==Constants.EXISTING_PROJECT_2){
            Log.i("Project View", "Existing project");

            switch(isEditing){
                case Constants.EXISTING_PROJECT_1:

                    if(dataset.getProjectedValuesList()==null){
                        dataset.CreateProjList();
                    } else {
                        if(dataset.getProjectedValuesList().size()==0){
                            dataset.CreateProjList();
                        }
                    }

                    this.listOfValues = dataset.getDataList();
                    mProjectName.setText(dataset.getmProjectName());
                    break;

                case Constants.EXISTING_PROJECT_2:
                    this.listOfValues = (ArrayList<Variable>) getIntent().getSerializableExtra(Constants.PROJECT_DATA_LIST);
                    dataset.CreateProjList();
                    dataset.setDataList(this.listOfValues);
                    int projectId = getIntent().getIntExtra(Constants.PROJECT_ID, 0);

                    dataset.setProjectId(projectId);
                    dataset.setmProjectName(getIntent().getStringExtra(Constants.PROJECT_NAME));
                    mProjectName.setText(getIntent().getStringExtra(Constants.PROJECT_NAME));
                    break;

                default:
                    Log.i("Project View", "Doing Nothing");
            }

        } else {

        }

        if(listOfValues==null){
            Log.i("Project View", "List of values is null");
            NoDataTV.setVisibility(View.VISIBLE);
            DataListContainer.setVisibility(View.GONE);
        } else {
            if(listOfValues.size()==0){
                Log.i("Project View", "List of values size is 0");
                NoDataTV.setVisibility(View.VISIBLE);
                DataListContainer.setVisibility(View.GONE);
            } else {
                NoDataTV.setVisibility(View.GONE);
                DataListContainer.setVisibility(View.VISIBLE);

                SetupAdapterRecycler();
            }
        }




        mCalcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculate();
            }
        });

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addValuePair();
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Save(false);
            }
        });



    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void Save(boolean fromCalculationButton){
        FileHandler mSaveHandler = new FileHandler();

        /**
         * public void save2file(ArrayList<Variable> listOfVariablesForSaving, HashMap<String, Double> basicStatistics,
         *                           String projectName, int wasCalculated, ArrayList<Variable> ProjectedValues) {
         *
         */

        String projectName = mProjectName.getText().toString();
        Toaster mToaster = new Toaster(this);
        if(projectName.isEmpty() || projectName.equals("")){
            mToaster.showCustomToast("Please add a name for the project");


        } else {
            Statistics dataset = (Statistics) getApplication();
            HashMap<String, Double> basicStats = dataset.getBasicStatistics();
            int wasCalculated = dataset.getIsCalculated();
            ArrayList<Variable> ProjectedValues = dataset.getProjectedValuesList();

            mSaveHandler.save2file(listOfValues, basicStats, projectName, wasCalculated,ProjectedValues , this,
                    dataset.getProjectId());

            if(mSaveHandler.getSave_status()==200){
                if(!fromCalculationButton){
                    //mToaster.showCustomToast("response save" + mSaveHandler.getSave_status(), R.color.success_blue);

                    mToaster.showCustomToast("File was saved", R.color.success_blue);
                }

            } else {
                String fileNotSaved = "File wasn't saved. ";
                switch (mSaveHandler.getSave_status()){

                    case Constants.SAVE_INT_RESPONSE_1:
                        mToaster.showCustomToast(fileNotSaved +Constants.SAVE_RESPONSE_1);
                        break;

                    case Constants.SAVE_INT_RESPONSE_2:
                        mToaster.showCustomToast(fileNotSaved +Constants.SAVE_RESPONSE_2);
                        break;

                    case Constants.SAVE_INT_RESPONSE_3:
                        mToaster.showCustomToast(fileNotSaved +Constants.SAVE_RESPONSE_3);
                        break;

                    default:
                        mToaster.showCustomToast("Please check your inputs.");
                        break;
                }
               // showCustomToast("File wasn't saved");
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_proj, menu);


        return true;
    }



    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_proj, menu);


        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case Constants.CALCULATION_ACTIVITY:

                if(resultCode==RESULT_OK){
                    Toast.makeText(this, "Calculation Completed", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(this, "Calculation cannot be completed", Toast.LENGTH_SHORT).show();
                }

                break;
            default:
                break;
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Log.i("onOptionsItemSelected", "item: " + item.toString());
        //Log.i("onOptionsItemSelected", "itemid: " + item.getItemId());


        switch (item.getItemId()) {
            case R.id.menu_help:
                Intent helpIntent = new Intent(this, Help.class);
                startActivity(helpIntent);
                return true;
            case R.id.menu_close:
                return true;

            case R.id.menu_save:
                return true;

            case R.id.menu_results:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    private void calculate(){

        if(listOfValues!=null){
            if(listOfValues.size()>=20){

                Statistics dataset = (Statistics) getApplication();


                if(mProjectName.getText().equals("")){


                    Toaster mToaster = new Toaster(this);
                    mToaster.showCustomToast("Please set a name for the Project");

                } else {


                    FileHandler mFileHandler  = new FileHandler(this);
                    String projectName = mProjectName.getText().toString();

                    if(mFileHandler.validateProjectName(projectName)){

                        Toaster mToaster = new Toaster(this);
                        mToaster.showCustomToast("Don't use special characters for the name of the project");


                        Log.i("ProjectView", "primer if");
                    } else {

                        if(validateListOfYears()){

                            Save(true);


                            dataset.setDataList(listOfValues);

                            dataset.limpiarResultados();
                            dataset.setmProjectName(projectName);

                            Log.i("calculate", "Sorting");

                            Intent calcIntent = new Intent(this, CalculationActivity.class);

                            startActivityForResult(calcIntent, Constants.CALCULATION_ACTIVITY);


                            Log.i("ProjectView", "segundo if");
                        } else {

                        }


                    }

                }



            } else {
                Log.i("calculate", "Doing nothing");
                Toaster mToaster = new Toaster(this);
                mToaster.showCustomToast("Are needed at least 20 values for calculations");

            }
        } else {
            Log.i("calculate", "Doing nothing");
            Toaster mToaster = new Toaster(this);
            mToaster.showCustomToast("Are needed at least 20 values for calculations");

        }

    }





    private void SetupAdapterRecycler(){
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerValues);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(false);


        mVariableAdapter = new VariableAdapter(this, listOfValues, mETYear, mETValue, mAddButton);
        mRecyclerView.setAdapter(mVariableAdapter);

    }

    private void addValuePair(){
        Log.i("addValuePair", "Click on AddValuePair");

        if(mAddButton.getText().equals(Constants.ADD_VALUE_TEXT)){

            Log.i("addValuePair", "Adding value");
            getValuesAddToList();
            mAddButton.setText(Constants.ADD_VALUE_TEXT);
        } else {
            if(mAddButton.getText().equals(Constants.UPDATE_TEXT)){
                Log.i("addValuePair", "Editing value");
                getValuesAddToList();
                mAddButton.setText(Constants.ADD_VALUE_TEXT);
            }

        }


    }


    private void getValuesAddToList(){

        boolean validYear = false;
        boolean validValue = false;

        if(mETValue.getText().equals("")|| mETValue.getText().length()==0){
            Toast.makeText(this, "Please insert a value.", Toast.LENGTH_SHORT).show();
        } else {
            if(mETYear.getText().equals("")|| mETYear.getText().length()==0){
                Toast.makeText(this, "Please insert a year. ", Toast.LENGTH_SHORT).show();
            } else {

                if(validateYear(mETYear.getText().toString())==-1){
                    Toast.makeText(this, "Please insert a valid year. ", Toast.LENGTH_SHORT).show();
                } else {
                    validYear = true;

                    if(validateValue(mETValue.getText().toString())==null){
                        Toast.makeText(this, "Please insert a valid year. ", Toast.LENGTH_SHORT).show();
                    } else {
                        validValue=  true;
                        if(validYear && validValue){
                            if(listOfValues==null){
                                listOfValues = new ArrayList<Variable>();
                                SetupAdapterRecycler();

                            }

                            int year = validateYear(mETYear.getText().toString());
                            double value = validateValue(mETValue.getText().toString());

                            listOfValues.add(new Variable(year, value));
                            if(listOfValues.size()>0){
                                this.NoDataTV.setVisibility(View.GONE);

                            }
                            mVariableAdapter.notifyDataSetChanged();
                            this.isCalculated = 0;
                            mETYear.setText("");
                            mETValue.setText("");

                            View view = getCurrentFocus();

                            if(view!=null){
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(),0);
                            }


                        } else {
                            //Do nothing
                        }
                    }
                }


            }
        }

    }

    private int validateYear(String input){
        int response = -1;

        try {
            response = Integer.parseInt(input);
        } catch(NumberFormatException nfe) {
            //Toast.makeText(this, "Please insert a valid year. ", Toast.LENGTH_SHORT).show();
            response = -1;
        }

        return response ;
    }

    private Double validateValue(String input){
        Double response = null;

        try {
            response = Double.parseDouble(input);
        } catch(NumberFormatException nfe) {
            //Toast.makeText(this, "Please insert a valid year. ", Toast.LENGTH_SHORT).show();
            response = null;
        }


        return response;
    }



    private boolean validateListOfYears(){
        boolean response = false;
        int counter = 0;
        listOfYears = new double[listOfValues.size()];

        Log.i("ProjectView", "listOfValues.size: "  + listOfValues.size());

        for(int i=0; i<listOfValues.size(); i++){
//            Log.i("ProjectView", "i: "  + i);
            listOfYears[i] = listOfValues.get(i).getyear();
        }

        Log.i("ProjectView", "Ordering years");
        Log.i("ProjectView", "Outer limit" + (listOfYears.length-2));



        for(int i=(listOfYears.length-1); i>0 ; i--){
            Log.i("ProjectView", "i: " +i);
            for(int j=0; j<i; j++){
                Log.i("ProjectView", "j: " +j);
                double a =listOfYears[j];
                double b =listOfYears[j+1];
                Log.i("ProjectView", "a: " + a + ", b: " + b);
                if(a>b){
                    swap(j, j+1);
                }


            }
            for(int m=0; m<listOfYears.length; m++){
                Log.i("ProjectView", "m: " + m + ", " + listOfYears[m]);
            }
        }

        for(int k=0; k<listOfYears.length; k++){
            Log.i("ProjectView", "year: " + listOfYears[k]);
        }


        for(int i=(listOfYears.length-2); i>0; i--){
            double yearGap = listOfYears[i] - listOfYears[i-1];
            if(yearGap>1 || yearGap==0){

                Toaster mToaster = new Toaster(getApplicationContext());
                mToaster.showCustomToast("Please check year values aren't consecutives or are some duplicated ");
                counter++;
            }

        }

        if(counter==0){
            response = true;
        } else {
            response = false;
        }


        return response;
    }

    private void swap(int one, int two){
        Log.i("ProjectView" , "Swap function");
        Log.i("ProjectView" , "Before - a: " + listOfYears[one]+ ", b: "+ listOfYears[two]);
        double temp = listOfYears[one];
        listOfYears[one] = listOfYears[two];
        listOfYears[two] = temp;
        Log.i("ProjectView" , "After - a: " + listOfYears[one]+ ", b: "+ listOfYears[two]);
    }


}

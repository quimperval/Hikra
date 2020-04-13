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


package com.per.hikra;
import com.per.hikra.activities.OpenProjectActivity;
import com.per.hikra.activities.ProjectView;
import com.per.hikra.dbutils.SQLUtils;
import com.per.hikra.help.Help;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.per.hikra.statistics.Statistics;
import com.per.hikra.utils.Constants;
import com.per.hikra.utils.FileHandler;
import com.per.hikra.utils.Toaster;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

public class StartActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private Button openButton;
    private Button newButton;
    private Button importProjectButton;
    static final Integer WRITE_EXST = 0x3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mToolbar = (Toolbar) findViewById(R.id.startToolbar);
        setSupportActionBar(mToolbar);
        openButton = (Button) findViewById(R.id.openProjectButton);
        newButton = (Button) findViewById(R.id.newProjectButton);
        importProjectButton = (Button) findViewById(R.id.importProjectButton);

        importProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importProject();
            }
        });

        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProject();
            }
        });

        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearAll();
            }
        });

        Ask(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXST);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_main, menu);


        return true;
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
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void openProject(){
        Statistics dataset = (Statistics) getApplication();
        dataset.limpiartodo();
        SQLUtils utils = new SQLUtils(this);

        int count = utils.getCountOfProjects();

        Log.i("openProject", "Count of projects "+ count);
        if(count>0){
            Intent openActivity = new Intent(this, OpenProjectActivity.class);
            startActivity(openActivity);
        } else {
            Toaster mToaster = new Toaster(this);
            mToaster.showCustomToast("There are no listed projects in the database, try importing");
        }

    }

    public void importProject() {


        Statistics dataset = (Statistics) getApplication();
        dataset.limpiartodo();
        int progress = dataset.getEncalculo();

        if(progress==1) {



        }else {


            int data = dataset.size();


            if (data == 0 ) {

                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("text/*");
                startActivityForResult(intent, Constants.READ_REQUEST_CODE);


            } else {
                FileHandler mFileHandler = new FileHandler();
                //public void save2file(ArrayList<Variable> listOfVariablesForSaving, HashMap<String, Double> basicStatistics,
                //                          String projectName, int wasCalculated, ArrayList<Variable> ProjectedValues, Context mContext) {

                if(dataset.getProjectedValuesList() == null){
                    dataset.CreateProjList();
                } else {
                    if(dataset.getProjectedValuesList().size()==0){
                        dataset.CreateProjList();
                    }
                }

                mFileHandler.save2file(dataset.getDataList(), dataset.getBasicStatistics(),
                        dataset.getmProjectName(), dataset.getIsCalculated(), dataset.getProjectedValuesList(), this,dataset.getProjectId());
                dataset.limpiartodo();
                dataset.setIsCalculated(0);
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("text/*");
                startActivityForResult(intent, Constants.READ_REQUEST_CODE);

            }

        }




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            Uri uri = null;
            if (data != null) {
                uri = data.getData();

                readFileFromUri(uri);

            }
        }
    }

    private void readFileFromUri(Uri contentURI) {
        try {
            ParcelFileDescriptor mParcelFile = getContentResolver().openFileDescriptor(contentURI, "rw");


            FileInputStream mInput = new FileInputStream(mParcelFile.getFileDescriptor());
            BufferedInputStream bis = new BufferedInputStream(mInput);

            BufferedReader br = new BufferedReader(new InputStreamReader(mInput));

            FileHandler mFileHandler = new FileHandler(br);
            mFileHandler.readfile();


            if(mFileHandler.hasErrors()){
                String Error = "Error in reading process. ";
                Toaster mToaster = new Toaster(this);
                switch (mFileHandler.getRead_response_status()){

                    case 1:
                        mToaster.showCustomToast(Error + Constants.RESPONSE_TEXT_1);
                        break;


                    case 2:
                        mToaster.showCustomToast(Error + Constants.RESPONSE_TEXT_2);
                        break;


                    case 3:
                        mToaster.showCustomToast(Error + Constants.RESPONSE_TEXT_3);
                        break;


                    case 4:
                        mToaster.showCustomToast(Error + Constants.RESPONSE_TEXT_4);

                        break;


                    case 5:
                        mToaster.showCustomToast(Error + Constants.RESPONSE_TEXT_5);
                        break;


                    case 6:
                        mToaster.showCustomToast(Error + Constants.RESPONSE_TEXT_6);
                        break;


                    case 7:
                        mToaster.showCustomToast(Error + Constants.RESPONSE_TEXT_7);
                        break;


                    case 8:
                        mToaster.showCustomToast(Error + Constants.RESPONSE_TEXT_8);
                        break;


                    case 9:
                        mToaster.showCustomToast(Error + Constants.RESPONSE_TEXT_9);
                        break;


                    case 10:
                        mToaster.showCustomToast(Error + Constants.RESPONSE_TEXT_10);
                        break;


                    case 11:
                        mToaster.showCustomToast(Error + Constants.RESPONSE_TEXT_11);
                        break;

                    case 12:
                        mToaster.showCustomToast(Error + Constants.RESPONSE_TEXT_12);
                        break;

                    case 13:
                        mToaster.showCustomToast(Error + Constants.RESPONSE_TEXT_13);
                        break;

                    default:

                        break;

                }
            } else {
                Toaster mToaster = new Toaster(this);
                mToaster.showCustomToast("File reading is successful.", R.color.success_blue);
                Statistics dataset = (Statistics) getApplication();
                Log.i("StartActivity", "Size of list: " + mFileHandler.getReadedVariables().size());
                dataset.setDataList(mFileHandler.getReadedVariables());
                dataset.setmProjectName(mFileHandler.getProjectName());
                Intent existingProjInt = new Intent(this, ProjectView.class);


                existingProjInt.putExtra(Constants.STR_EXISTING_PROJECT, Constants.EXISTING_PROJECT_1);
                startActivity(existingProjInt);
            }

        } catch (FileNotFoundException e) {
            Toaster mToaster = new Toaster(this);
            mToaster.showCustomToast("File wasn't found. Please check your file.");

            e.printStackTrace();
        }
    }



    public void Ask(String Permission, Integer RequestCode) {

        if (ContextCompat.checkSelfPermission(this, Permission)
                != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Permission)) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Permission}, RequestCode);


            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Permission}, RequestCode);
            }

        } else {

        }


    }


    public void ClearAll(){

        Statistics dataset = (Statistics) getApplication();

        if(dataset.size()>0){
            FileHandler mFileHandler = new FileHandler();

            mFileHandler.save2file(dataset.getDataList(), dataset.getBasicStatistics(),
                    dataset.getmProjectName(), dataset.getIsCalculated(), dataset.getProjectedValuesList(), this, dataset.getProjectId());

            dataset.limpiartodo();
            dataset.CreateProjList();

            Toast.makeText(getApplicationContext(), "All data were erased. You can start a new project.", Toast.LENGTH_SHORT).show();
            dataset.setIsCalculated(0);

        } else {
            if(dataset.size()==0){

            }
        }


        Intent projectIntent =  new Intent(this, ProjectView.class);
        startActivity(projectIntent);
    }
}

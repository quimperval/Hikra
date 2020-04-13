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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.per.hikra.R;
import com.per.hikra.adapters.AdapterProject;
import com.per.hikra.dbutils.SQLUtils;
import com.per.hikra.models.Project;
import com.per.hikra.utils.Toaster;

import java.util.ArrayList;

public class OpenProjectActivity extends AppCompatActivity {

    private RecyclerView mRecycler ;
    private ArrayList<Project> listOfProjects;
    private AdapterProject mAdapter;
    private ProgressBar mProgresBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_project);

        mProgresBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgresBar.setVisibility(View.GONE);
        mRecycler = (RecyclerView) findViewById(R.id.recyclerView);
        //    public AdapterProject(ArrayList<Project> listOfProjects, Context mContext ){

        SQLUtils utils = new SQLUtils(this);
        listOfProjects = utils.getListOfProjects();

        int size = listOfProjects.size();

        Log.i("OpenProject", "There are " + size + " projects saved");

        for(int i = 0; i <listOfProjects.size(); i++){
            Log.i("OpenProject", "Project Name: " + listOfProjects.get(i).getProjectName());
        }

        mAdapter = new AdapterProject(listOfProjects, this, mProgresBar);

        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setHasFixedSize(false);
        mRecycler.setAdapter(mAdapter);

        Toaster mToaster = new Toaster(this);
        mToaster.showCustomToast("Click on the project name to open", R.color.alert_orange);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            mProgresBar.setVisibility(View.GONE);

    }





}

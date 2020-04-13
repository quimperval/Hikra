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

package com.per.hikra.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.per.hikra.activities.ProjectView;
import com.per.hikra.R;
import com.per.hikra.models.Project;
import com.per.hikra.statistics.Statistics;
import com.per.hikra.utils.Constants;
import com.per.hikra.utils.FileHandler;
import com.per.hikra.utils.Toaster;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class AdapterProject extends RecyclerView.Adapter<AdapterProject.ProjectViewHolder> {

    private ArrayList<Project> listOfProjects;

    private Context mContext;

    private ProgressBar mProgressBar;

    public AdapterProject(){

    }

    public AdapterProject(ArrayList<Project> listOfProjects, Context mContext , ProgressBar mProgressBar){
        Log.i("AdapterProject", "Constructor");
        this.listOfProjects  = listOfProjects;
        this.mContext = mContext;
        this.mProgressBar = mProgressBar;
    }


    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(mContext).inflate(R.layout.card_project, parent,false);


        return new AdapterProject.ProjectViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {


        Project mProj = listOfProjects.get(position);
        holder.mProjectName.setText(mProj.getProjectName());
        holder.mLastUpdate.setText(mProj.getLastUpdate());

        if(position==0){
            holder.lastUpdateTitle.setVisibility(View.VISIBLE);
            holder.projectTitle.setVisibility(View.VISIBLE);
            holder.separator1TV.setVisibility(View.VISIBLE);
        }

        holder.mProjectName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readProjectAndOpenActivity(mProj.getPathOfFile(), mProj);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOfProjects.size();
    }

    public class ProjectViewHolder extends RecyclerView.ViewHolder {

        private TextView mProjectName;
        private TextView mLastUpdate;

        private TextView projectTitle;
        private TextView lastUpdateTitle;
        private TextView separator1TV;

        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);

            //projectTitleList
            //lastUpdateTitleList
            mProjectName = itemView.findViewById(R.id.tvProjItemName);
            mLastUpdate = itemView.findViewById(R.id.tvlastUpdate);
            projectTitle = itemView.findViewById(R.id.projectTitleList);
            lastUpdateTitle = itemView.findViewById(R.id.lastUpdateTitleList);
            separator1TV = itemView.findViewById(R.id.separator1);
            separator1TV.setVisibility(View.GONE);

            projectTitle.setVisibility(View.GONE);
            lastUpdateTitle.setVisibility(View.GONE);

        }
    }

    private void readProjectAndOpenActivity(String filePath, Project mProject){
        mProgressBar.setVisibility(View.VISIBLE);

        //Toast.makeText(mContext, "click on project item", Toast.LENGTH_SHORT).show();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            //Toast.makeText(mContext, "reader: " + reader.toString(), Toast.LENGTH_SHORT).show();

            FileHandler mFileHandler = new FileHandler(reader);
            mFileHandler.readfile();

            if(mFileHandler.hasErrors()){
                mProgressBar.setVisibility(View.GONE);
                String Error = "Error in reading process. ";
                Toaster mToaster = new Toaster(mContext);
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
                Toaster mToaster = new Toaster( mContext);

                if(mFileHandler.getReadedVariables()==null){
                    mToaster.showCustomToast("File was empty, weren't found data.");
                    mFileHandler.initializeListOfInputVariables();
                }

                Statistics dataset = (Statistics) ((Activity) mContext).getApplication();
                dataset.limpiartodo();
                Log.i("StartActivity", "Size of list: " + mFileHandler.getReadedVariables().size());

                //mToaster.showCustomToast("Project Name: " + mFileHandler.getProjectName() + " with " + mFileHandler.getReadedVariables().size() + " pair data values.", R.color.success_blue);
                //dataset.setDataList(mFileHandler.getReadedVariables());
                //dataset.setmProjectName(mFileHandler.getProjectName());

                Intent existingProjInt = new Intent(mContext, ProjectView.class);
                existingProjInt.putExtra(Constants.PROJECT_ID, mProject.getProjectId());
                existingProjInt.putExtra(Constants.PROJECT_DATA_LIST, mFileHandler.getReadedVariables());
                existingProjInt.putExtra(Constants.PROJECT_NAME, mFileHandler.getProjectName());
                existingProjInt.putExtra(Constants.STR_EXISTING_PROJECT, Constants.EXISTING_PROJECT_2);
                //mProgressBar.setVisibility(View.GONE);
                //mToaster.showCustomToast("File reading is successful.", R.color.success_blue);
                ((Activity) mContext).startActivity(existingProjInt);
                ((Activity) mContext).finish();
            }


        } catch (FileNotFoundException e) {
            mProgressBar.setVisibility(View.GONE);
            Toaster mToaster = new Toaster(mContext);
            mToaster.showCustomToast("Something went wrong, try importing the project file or creating a new project", R.color.success_blue);

            e.printStackTrace();
        }
    }
}

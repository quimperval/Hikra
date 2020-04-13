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

package com.per.hikra.dbutils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.per.hikra.models.Project;

import java.util.ArrayList;

public class SQLUtils {

    private Context mContext;

    public SQLUtils(Context mContext){
        this.mContext = mContext;
    }


    public ArrayList<Project> getListOfProjects(){
        Log.i("SQLUtils", "getListOfProjects ");
        ArrayList<Project> response = null;
        SQLHelper conn = new SQLHelper(mContext, ConstantsDatabase.DB_NAME, null,ConstantsDatabase.DB_VERSION);

        SQLiteDatabase sqlDb = conn.getReadableDatabase();

        Cursor mCursor = sqlDb.rawQuery(ConstantsDatabase.SELECT_ALL_PROJECTS, null);

        int id_col_id  = -1;
        int id_col_prj_name = -1;
        int id_col_path = -1;
        int id_col_last_update = -1;

        id_col_id = mCursor.getColumnIndex(ConstantsDatabase.COL_ID);
        id_col_last_update = mCursor.getColumnIndex(ConstantsDatabase.COL_LAST_UPDATE);
        id_col_prj_name = mCursor.getColumnIndex(ConstantsDatabase.COL_PROJ_NAME);
        id_col_path = mCursor.getColumnIndex(ConstantsDatabase.COL_PATH);

        int counter = 0;
        while(mCursor.moveToNext()){
            Log.i("SQLUtils", "getListOfProjects counter: " + counter);
            if(response==null){
                response = new ArrayList<Project>();
            }

            int projectId = mCursor.getInt(id_col_id);
            String projectName = mCursor.getString(id_col_prj_name);
            String projectPath = mCursor.getString(id_col_path);
            String lastUpdate = mCursor.getString(id_col_last_update);

            //    public void Project(String projectName, String pathOfFile, String lastUpdate){

            Project mProject = new Project(projectId, projectName, projectPath, lastUpdate);

            response.add(mProject);
            counter++;
        }

        if(response==null){
            Log.i("SQLUtils", "getListOfProjects response is null");
        } else {
            Log.i("SQLUtils", "getListOfProjects response is not null, has " + response.size() + " records.");
        }

        mCursor.close();
        sqlDb.close();
        conn.close();
        return response;
    }


    public int getCountOfProjects(){
        int response = 0;

        SQLHelper conn = new SQLHelper(mContext, ConstantsDatabase.DB_NAME, null,ConstantsDatabase.DB_VERSION);

        SQLiteDatabase sqlDb = conn.getReadableDatabase();

        Cursor mCursor = sqlDb.rawQuery(ConstantsDatabase.GET_COUNT_OF_PROJECTS, null);


        while(mCursor.moveToNext()){
            response = mCursor.getInt(0);
        }

        mCursor.close();
        sqlDb.close();
        conn.close();

        return response;
    }




    public int updateProjectData(Project mProject){
        Log.i("updateProjectData","updating project info in the list");

        int response = -1;

        SQLHelper conn = new SQLHelper(mContext, ConstantsDatabase.DB_NAME, null,ConstantsDatabase.DB_VERSION);

        SQLiteDatabase sqlDb = conn.getWritableDatabase();

        ContentValues projectValues = new ContentValues();
        projectValues.put(ConstantsDatabase.COL_PROJ_NAME, mProject.getProjectName());
        projectValues.put(ConstantsDatabase.COL_LAST_UPDATE, mProject.getLastUpdate());

        String whereClause =         ConstantsDatabase.COL_ID + " = ?";
        response = sqlDb.update(ConstantsDatabase.TABLE_PROJECTS, projectValues, whereClause, new String[] { String.valueOf(mProject.getProjectId()) });

        sqlDb.close();
        conn.close();
        return response;

    }

    public Long insertProject(Project mProject){

        Log.i("insertProject","inserting new project into the list");
        Long response;

        ContentValues projectValues = new ContentValues();

        projectValues.put(ConstantsDatabase.COL_PROJ_NAME, mProject.getProjectName());
        projectValues.put(ConstantsDatabase.COL_LAST_UPDATE, mProject.getLastUpdate());
        projectValues.put(ConstantsDatabase.COL_PATH, mProject.getPathOfFile());

        SQLHelper conn = new SQLHelper(mContext, ConstantsDatabase.DB_NAME, null,ConstantsDatabase.DB_VERSION);
        SQLiteDatabase sqlDb = conn.getWritableDatabase();


        response = sqlDb.insert(ConstantsDatabase.TABLE_PROJECTS, null, projectValues);

        sqlDb.close();
        conn.close();

        return response;

    }
}

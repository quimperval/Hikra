/******************************************************************************
 * Copyright (c) 2018, Joaquín Pérez Valera
 *
 * This software is available under the following "MIT Style" license,
 * or at the option of the licensee under the LGPL (see COPYING).
 *
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

public class ConstantsDatabase {

    public static final int DB_VERSION = 3;

    public static final String DB_NAME = "PROJ_DB";



    public static final String TABLE_PROJECTS = "PROJECTS";
    public static final String COL_ID = "ID";
    public static final String COL_PROJ_NAME = "PROJECT";
    public static final String COL_PATH = "PATH";
    public static final String COL_LAST_UPDATE = "LAST_UPDATE";
    public static final String CREATE_TABLE_PROJECTS = "CREATE TABLE " + TABLE_PROJECTS +
                        " ( "  +COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_PROJ_NAME + " TEXT," + COL_PATH + " TEXT, " + COL_LAST_UPDATE + " TEXT )";


    public static final String SELECT_ALL_PROJECTS = "SELECT * FROM " + TABLE_PROJECTS;
    public static final String GET_COUNT_OF_PROJECTS = "SELECT COUNT(*) FROM " + TABLE_PROJECTS;


    public static final String TRUNCATE_PROJECT_TABLE =  "DELETE FROM "+ TABLE_PROJECTS;

}

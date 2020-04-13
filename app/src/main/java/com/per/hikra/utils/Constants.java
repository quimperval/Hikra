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

package com.per.hikra.utils;

public class Constants {

    public static final boolean showLogs = false;

    public static final String YEAR_HEADER = "YEAR";
    public static final String VALUE_HEADER = "DATA";

    public static final String NAME_GUION = "--------------";
    public static final String VALUES_GUION = "----------";

    public static final String END_OF_LINE = "\n";
    public static final String BLANK = " ";
    public static final String BLANK_0 =  " "+END_OF_LINE;

    public static final String CALCULATED_NO = "CALCULATED: NO";
    public static final String CALCULATED_YES = "CALCULATED: YES";

    public static final int READ_REQUEST_CODE = 42;
    public static final int ADD_VALUE_TO_LIST = 43;
    public static final int CALCULATION_ACTIVITY =44;

    public static final int NEW_PROJECT = 0;
    public static final String STR_EXISTING_PROJECT = "Existing project";
    //EXISTNG_PROJECT: 1 - Start with values from import option
    //EXISTING_PROJECT_1: 2 - Start with values from open Option
    public static final int EXISTING_PROJECT_1 = 1;
    public static final int EXISTING_PROJECT_2 = 2;

    public  static final int READING_MODE = 45;
    public static final int NEW_PROJ_MODE  = 46;

    public static final String UPDATE_TEXT = "Update";
    public static final String ADD_VALUE_TEXT = "Add Value";


    public static final int IS_READING_TITLE = 1;
    public static final String TITLE_HEADER  ="[TITLE]";

    public static final int IS_READING_RAW_DATA =2 ;
    public static final String RAW_HEADER = "[RAW DATA]";

    public static final int IS_READING_FITTED_DATA = 3;
    public static final String FITTED_HEADER = "[FITTED VALUES]";

    public static final int IS_READING_PROJECTED_DATA = 4;
    public static final String PROJ_HEADER= "[PROJECTED VALUES]";

    public static final int IS_READING_BASIC_STATISTICS = 5;
    public static final String BASIC_HEADER = "[BASIC STATISTICS]";

    public static final int IS_READING_FLAGS = 6;
    public static final String FLAGS = "[FLAGS]";

    public static final String RESPONSE_TEXT_1 = "There is no Project Title defined. Verify file, there is more than one Data's header.";

    public static final String RESPONSE_TEXT_2 = "Verify file, there is more than one title defined.";

    public static final String RESPONSE_TEXT_3 = "There are no flags, will be setted default values.";
    public static final String RESPONSE_TEXT_4 = "Can't be located data list. Verify file.";

    public static final String RESPONSE_TEXT_5 = "Verify file, there is more than one data's header.";

    public static final String RESPONSE_TEXT_6 = "Can't be located data list. Verify file.";

    public static final String RESPONSE_TEXT_7 = "Verify file, there is more than one fitted data header.";

    public static final String RESPONSE_TEXT_8 = "Can't be located fitted data  list. Verify file.";
    public static final String RESPONSE_TEXT_9 = "Verify file, there is more than one pipes's header.";
    public static final String RESPONSE_TEXT_10 = "Can't be located project header. Verify file.";
    public static final String RESPONSE_TEXT_11 = "Verify file, there is more than one basic statistic's header.";
    public static final String RESPONSE_TEXT_12 = "Can't be located basic statistics header . Verify file.";
    public static final String RESPONSE_TEXT_13 = "Failed to load file.";
    public static final String RESPONSE_IN_ERROR = "Please verify file contents, there are some elements missing or misleads.";


    public static final String BSTAT_MEAN = "Mean";
    public static final String BSTAT_STD_DEV = "Standard Deviation";
    public static final String BSTAT_SKEW = "Skewness";

    //save_status = 1: empty list of variables.
    //save_status = 2; "Set a name for the project."
    //save_status = 3; "Don't use special characters."


    public static final int SAVE_INT_RESPONSE_1 = 1;
    public static final String SAVE_RESPONSE_1 = "List of variables is empty. Add values to save your project";
    public static final int SAVE_INT_RESPONSE_2 = 2;
    public static final String SAVE_RESPONSE_2 ="Set a name for the project.";
    public static final int SAVE_INT_RESPONSE_3 = 3;
    public static final String SAVE_RESPONSE_3 ="Don't use special characters in the project name.";


    public static final String TR_HEADER = "Tr";
    public static final String SEPARATOR_HEADER = " ";
    public static final String DATA_HEADER = "DATA";
    public static final String FITTED_VALUE_HEADER = "Fitted_Value";
    public static final String ERROR_HEADER = "Error^2";
    public static final String PX_HEADER = "Px";
    public static final String PROJECTED_VALUE_HEADER = "Projected_Value";

    public static final String PROJECT_NAME = "Project_name";
    public static final String PROJECT_DATA_LIST = "Data_list";

    public static final String TITLE_TAB_ERRORS = "Errors Summary";
    public static final String TITLE_TAB_FITTED = "Fitted Data";
    public static final String TITLE_TAB_PROJECTED = "Projected \nData";
    public static final String TITLE_TAB_PDF = "All \nPDF";
    public static final String TITLE_TAB_TABLES = "Tables";
    public static final String PROJECT_ID = "project_id";

    public static final String XLS_INPUT_DATA = "Input Data";
    public static final String XLS_PROJECTED_DATA = "Projected Data";
    public static final String XLS_FITTED_DATA = "Fitted Data";
    public static final String XLS_SUMMARY = "Error Summary";

    public static final String XLS_TR_HEADER = "Tr";
    public static final String XLS_VALUE_HEADER = "Value";
    public static final String XLS_FITTED_HEADER = "Fitted Value";
    public static final String XLS_ERROR_HEADER = "Error^2";

    public static final String XLS_PERIOD_HEADER = "Return period";
    public static final String XLS_PX_HEADER = "Probability";
    public static final String XLS_PROJ_VALUE = "Projected Value";



    public static final String XLS_TXT_FUNCTIONS = "Functions";
    public static final String XLS_TXT_MOMENTS = "Moments";
    public static final String XLS_TXT_MLH = "Maximum Likelihood";
    public static final String XLS_TXT_2PARM = "2 Parameters";
    public static final String XLS_TXT_3PARM = "3 Parameters";

    public static final String XLS_TXT_NORMAL = "Normal";
    public static final String XLS_TXT_LOGNORMAL = "LogNormal";
    public static final String XLS_TXT_GUMBEL = "Gumbel";
    public static final String XLS_TXT_EXP = "Exponential";
    public static final String XLS_TXT_GAMMA = "Gamma";




}

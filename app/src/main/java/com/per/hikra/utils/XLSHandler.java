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
import android.app.Activity;
import android.content.Context;

import android.util.Log;

import com.per.hikra.statistics.Statistics;

import java.io.File;
import java.io.IOException;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.*;
import jxl.write.Number;
import jxl.write.biff.RowsExceededException;
import jxl.format.Alignment;
import jxl.write.WritableFont;
import jxl.format.Colour;




public class XLSHandler {

    private static final String TAG = "XLSHandler";

    private HikraLogger mLogger;
    Context mContext;

    public XLSHandler(){

    }


    public XLSHandler(Context mContext){
        this.mContext = mContext;
        this.mLogger = new HikraLogger(mContext);
    }

    public void createExcelWorkBook(){
        Log.i(TAG, "create Excel Workbook");

        Statistics dataset = (Statistics) ((Activity) mContext).getApplicationContext() ;

        double minVal = dataset.getMinVal();

        String minfdp = "";
        minfdp = dataset.getminfdp();


        String minimo = "Minimum Standard Error: " + String.format("%.3f",minVal)+" "+
                "calculated with the "+ minfdp;


        String fileName = dataset.getmProjectName() + ".xls";

        String folder = "Hikra";


        File folderPath = new File( mContext.getExternalFilesDir(null).getAbsolutePath() + File.separator + folder);

        Log.i(TAG, "Folder AbsolutePath: " + folderPath.getAbsolutePath());

        if (!folderPath.exists()) {
            folderPath.mkdirs();
            Log.i(TAG, "creating folder");
        }

        File xlsFile = new File(folderPath, fileName);

        String FullPath = xlsFile.getAbsolutePath();

        Log.i(TAG, "fullpath: " + FullPath);

        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setUseTemporaryFileDuringWrite(false);

        WritableWorkbook wb = null;

        try{
            //create a new WritableWorkbook using the java.io.File and
            //WorkbookSettings from above


            wb = Workbook.createWorkbook(xlsFile,wbSettings);


            WritableFont boldFont = new WritableFont(WritableFont.ARIAL);
            boldFont.setBoldStyle(WritableFont.BOLD);

            NumberFormat fivedps = new NumberFormat("#.#####");

            WritableCellFormat mNumberFormat = new WritableCellFormat(fivedps);
            mNumberFormat.setAlignment(Alignment.CENTRE);
            mNumberFormat.setShrinkToFit(true);

            WritableCellFormat mNumberFormatFloat = new WritableCellFormat(NumberFormats.FLOAT);
            mNumberFormat.setAlignment(Alignment.CENTRE);
            mNumberFormat.setShrinkToFit(true);

            WritableCellFormat mNumberFormatRaw = new WritableCellFormat();
            mNumberFormat.setAlignment(Alignment.CENTRE);
            mNumberFormat.setShrinkToFit(true);

            WritableCellFormat cellCenterBoldFormat = new WritableCellFormat(boldFont);
            cellCenterBoldFormat.setAlignment(Alignment.CENTRE);
            cellCenterBoldFormat.setShrinkToFit(true);

            WritableCellFormat cellCenterFormat = new WritableCellFormat();
            cellCenterBoldFormat.setAlignment(Alignment.CENTRE);
            cellCenterBoldFormat.setShrinkToFit(true);

            WritableCellFormat numCenterFormat = new WritableCellFormat();
            numCenterFormat.setAlignment(Alignment.CENTRE);
            numCenterFormat.setShrinkToFit(true);

            WritableCellFormat selectedDistFormat = new WritableCellFormat(NumberFormats.FLOAT);
            selectedDistFormat.setAlignment(Alignment.CENTRE);
            selectedDistFormat.setBackground(Colour.GREEN);


            WritableSheet inputDataSheet = wb.createSheet(Constants.XLS_INPUT_DATA, 0);

            Label labelYear = new Label(0,0,Constants.YEAR_HEADER, cellCenterBoldFormat);
            Label labelValue = new Label(1,0,Constants.VALUE_HEADER, cellCenterBoldFormat);

            inputDataSheet.addCell(labelValue);
            inputDataSheet.addCell(labelYear);

            int rowNum = 1;
            int size = dataset.getDataList().size();

            for(int i=0; i<size; i++){
                //Adding Year Value to column 0
                Number yearNumber = new Number(0, rowNum, dataset.getDataList().get(i).getyear(),numCenterFormat);
                inputDataSheet.addCell(yearNumber);

                //Adding Year Value to column 1
                Number valueNumber = new Number(1, rowNum, dataset.getDataList().get(i).getvalue(),numCenterFormat);
                inputDataSheet.addCell(valueNumber);

                rowNum++;
            }



            WritableSheet summaryDataSheet = wb.createSheet(Constants.XLS_SUMMARY, 1);

            Label selectedFDP = new Label (0,8, minimo, cellCenterFormat);
            summaryDataSheet.addCell(selectedFDP);

            summaryDataSheet.mergeCells(0, 0, 0, 1);
            Label functionsLabel = new Label(0,0,Constants.XLS_TXT_FUNCTIONS,cellCenterBoldFormat);
            summaryDataSheet.addCell(functionsLabel);

            summaryDataSheet.mergeCells(1,0,2,0);
            Label momentsLabel = new Label(1,0,Constants.XLS_TXT_MOMENTS, cellCenterBoldFormat);
            summaryDataSheet.addCell(momentsLabel);

            summaryDataSheet.mergeCells(3,0,4,0);
            Label mlhLabel = new Label(3,0,Constants.XLS_TXT_MLH, cellCenterBoldFormat);
            summaryDataSheet.addCell(mlhLabel);


            Label twoParmLabelMOM = new Label(1,1, Constants.XLS_TXT_2PARM, cellCenterBoldFormat);
            Label threeParmLabelMOM = new Label(2,1, Constants.XLS_TXT_3PARM, cellCenterBoldFormat);
            summaryDataSheet.addCell(twoParmLabelMOM);
            summaryDataSheet.addCell(threeParmLabelMOM);


            Label twoParmLabelMLH = new Label(3,1, Constants.XLS_TXT_2PARM, cellCenterBoldFormat);
            Label threeParmLabelMLH = new Label(4,1, Constants.XLS_TXT_3PARM, cellCenterBoldFormat);
            summaryDataSheet.addCell(twoParmLabelMLH);
            summaryDataSheet.addCell(threeParmLabelMLH);


            Label normalLabel = new Label(0,2, Constants.XLS_TXT_NORMAL, cellCenterFormat);
            Label logNormalLabel = new Label(0,3, Constants.XLS_TXT_LOGNORMAL, cellCenterFormat);
            Label gumbelLabel = new Label(0,4, Constants.XLS_TXT_GUMBEL, cellCenterFormat);
            Label expLabel = new Label(0,5, Constants.XLS_TXT_EXP, cellCenterFormat);
            Label gammaLabel = new Label(0,6, Constants.XLS_TXT_GAMMA, cellCenterFormat);
            summaryDataSheet.addCell(normalLabel);
            summaryDataSheet.addCell(logNormalLabel);
            summaryDataSheet.addCell(gumbelLabel);
            summaryDataSheet.addCell(expLabel);
            summaryDataSheet.addCell(gammaLabel);

            int posMin = dataset.getPosmin();

            int fittedRow = -1;
            int fittedCol = -1;

            switch (posMin){
                case 0:

                    fittedRow = 2; //Normal
                    fittedCol = 1;
                    break;

                case 1:
                        fittedRow = 3;
                        fittedCol = 1;

                    break;

                case 2:

                        fittedRow = 3;
                        fittedCol = 2;

                    break;

                case 3:

                    fittedRow = 4;
                    fittedCol = 1;

                    break;
                case 4:

                    fittedRow = 5;
                    fittedCol = 1;
                    break;
                case 5:

                    fittedRow = 6;
                    fittedCol = 1;

                    break;
                case 6:

                    fittedRow = 6;
                    fittedCol = 2;


                    break;
                case 7:

                    fittedRow = 3;
                    fittedCol = 3;


                    break;
                case 8:

                    fittedRow = 3;
                    fittedCol = 4;

                    break;

                case 9:

                    fittedRow = 4;
                    fittedCol = 3;

                    break;
                case 10:

                    fittedRow = 5;
                    fittedCol = 3;

                    break;
                case 11:

                    fittedRow = 6;
                    fittedCol = 3;

                    break;
                case 12:

                    fittedRow = 6;
                    fittedCol = 4;

                    break;

                default:
                    break;
            }

            String nullValue = "-----";

            //Creating cells of Normal Dist

            int nextRowForInsert = 2;
            int nextColForInsert = 1;

            Number normalMOM2P = null;

            if(nextColForInsert==fittedCol && nextRowForInsert==fittedRow){
                normalMOM2P = new Number(1,2, dataset.getMOM_NError(), selectedDistFormat);

            } else {
                if( dataset.getMOM_NError()<0.0001){
                    normalMOM2P = new Number(1,2, dataset.getMOM_NError(), mNumberFormatRaw);
                } else {
                    normalMOM2P = new Number(1,2, dataset.getMOM_NError(), mNumberFormat);
                }

            }

            summaryDataSheet.addCell(normalMOM2P);

            Number normalMLH2P = null;
            if( dataset.getMOM_NError()<0.0001){
                normalMLH2P = new Number(3,2, dataset.getMOM_NError(), mNumberFormatRaw);
            } else {
                normalMLH2P = new Number(3,2, dataset.getMOM_NError(), mNumberFormat);
            }


            Label emptyNormalMOM = new Label(2,2,nullValue, cellCenterFormat);

            Label emptyNormalMLM = new Label(4,2,nullValue, cellCenterFormat);

            summaryDataSheet.addCell(emptyNormalMOM);
            summaryDataSheet.addCell(emptyNormalMLM);
            summaryDataSheet.addCell(normalMLH2P);


            //Creating cells of LogNormal Dist

            nextRowForInsert = 3;
            nextColForInsert = 1;

            Number logNormal2PMOM = null;
            if(nextColForInsert==fittedCol && nextRowForInsert==fittedRow){
                logNormal2PMOM = new Number(nextColForInsert, nextRowForInsert, dataset.getMOM_LN2PError(), selectedDistFormat);

            } else {
                if(dataset.getMOM_LN2PError()<0.0001){
                    logNormal2PMOM = new Number(nextColForInsert, nextRowForInsert, dataset.getMOM_LN2PError(), mNumberFormatRaw);
                } else {
                    logNormal2PMOM = new Number(nextColForInsert, nextRowForInsert, dataset.getMOM_LN2PError(), mNumberFormat);
                }


            }

            summaryDataSheet.addCell(logNormal2PMOM);

            nextColForInsert = 2;

            Number logNormal3PMOM = null;

            if(nextColForInsert==fittedCol && nextRowForInsert==fittedRow){
                logNormal3PMOM = new Number(nextColForInsert, nextRowForInsert, dataset.getMOM_LN3PError(), selectedDistFormat);

            } else {

                if( dataset.getMOM_LN3PError()<0.0001){
                    logNormal3PMOM = new Number(nextColForInsert, nextRowForInsert, dataset.getMOM_LN3PError(), mNumberFormatRaw);
                } else {
                    logNormal3PMOM = new Number(nextColForInsert, nextRowForInsert, dataset.getMOM_LN3PError(), mNumberFormat);
                }


            }

            summaryDataSheet.addCell(logNormal3PMOM);

            nextColForInsert = 3;

            Number logNormal2PMLH = null;

            if(nextColForInsert==fittedCol && nextRowForInsert==fittedRow){
                logNormal2PMLH = new Number(nextColForInsert, nextRowForInsert, dataset.getML_LN2PError(), selectedDistFormat);

            } else {

                if(dataset.getML_LN2PError()<0.0001){
                    logNormal2PMLH = new Number(nextColForInsert, nextRowForInsert, dataset.getML_LN2PError(), mNumberFormatRaw);
                } else {
                    logNormal2PMLH = new Number(nextColForInsert, nextRowForInsert, dataset.getML_LN2PError(), mNumberFormat);
                }


            }

            summaryDataSheet.addCell(logNormal2PMLH);

            nextColForInsert = 4;

            Number logNormal3PMLH = null;

            if(nextColForInsert==fittedCol && nextRowForInsert==fittedRow){
                logNormal3PMLH = new Number(nextColForInsert, nextRowForInsert, dataset.getML_LN3PError(), selectedDistFormat);


            } else {
                if(dataset.getML_LN3PError() <0.0001){
                    logNormal3PMLH = new Number(nextColForInsert, nextRowForInsert, dataset.getML_LN3PError(), mNumberFormatRaw);
                } else {
                    logNormal3PMLH = new Number(nextColForInsert, nextRowForInsert, dataset.getML_LN3PError(), mNumberFormat);
                }


            }

            summaryDataSheet.addCell(logNormal3PMLH);

            //Populating Gumbel Row

            nextRowForInsert = 4;
            nextColForInsert = 1;

            Number gumbelMOM2P = null;

            if(nextColForInsert==fittedCol && nextRowForInsert==fittedRow){
                gumbelMOM2P = new Number(nextColForInsert, nextRowForInsert, dataset.getMOM_GumbelError(), selectedDistFormat);

            } else {

                if(dataset.getMOM_G2PError()<0.0001){
                    gumbelMOM2P = new Number(nextColForInsert, nextRowForInsert, dataset.getMOM_GumbelError(), mNumberFormatRaw);
                } else {
                    gumbelMOM2P = new Number(nextColForInsert, nextRowForInsert, dataset.getMOM_GumbelError(), mNumberFormat);
                }


            }
            summaryDataSheet.addCell(gumbelMOM2P);

            nextColForInsert = 3;
            Number gumbelMLH2P = null;

            if(nextColForInsert==fittedCol && nextRowForInsert==fittedRow){
                gumbelMLH2P = new Number(nextColForInsert, nextRowForInsert, dataset.getML_GumbelError(), selectedDistFormat);

            } else {
                if(dataset.getML_G2PError()<0.0001){
                    gumbelMLH2P = new Number(nextColForInsert, nextRowForInsert, dataset.getML_GumbelError(), mNumberFormatRaw);
                } else {
                    gumbelMLH2P = new Number(nextColForInsert, nextRowForInsert, dataset.getML_GumbelError(), mNumberFormat);
                }


            }

            summaryDataSheet.addCell(gumbelMLH2P);

            Label gumbelLab3PMOM = new Label(2, nextRowForInsert, nullValue, cellCenterFormat);
            Label gumbelLab3PMLH = new Label(4, nextRowForInsert, nullValue, cellCenterFormat);
            summaryDataSheet.addCell(gumbelLab3PMOM);
            summaryDataSheet.addCell(gumbelLab3PMLH);

            //Populating Exponential Row

            nextRowForInsert = 5;
            nextColForInsert = 1;


            Number expMOM2P =  null;

            if(nextColForInsert==fittedCol && nextRowForInsert==fittedRow){
                expMOM2P = new Number(nextColForInsert, nextRowForInsert, dataset.getMOM_EXPError(),selectedDistFormat);
            } else {

                if(dataset.getMOM_EXPError()<0.0001){
                    expMOM2P = new Number(nextColForInsert, nextRowForInsert, dataset.getMOM_EXPError(),mNumberFormatRaw);
                } else {
                    expMOM2P = new Number(nextColForInsert, nextRowForInsert, dataset.getMOM_EXPError(),mNumberFormat);
                }

            }
            summaryDataSheet.addCell(expMOM2P);


            nextColForInsert = 3;

            Number expMLH2P = null;

            if(nextColForInsert==fittedCol && nextRowForInsert==fittedRow){

                expMLH2P = new Number (nextColForInsert, nextRowForInsert, dataset.getML_EXPError(), selectedDistFormat);
            } else {
                if(dataset.getML_EXPError()<0.0001){
                    expMLH2P = new Number (nextColForInsert, nextRowForInsert, dataset.getML_EXPError(), mNumberFormatRaw);
                } else {
                    expMLH2P = new Number (nextColForInsert, nextRowForInsert, dataset.getML_EXPError(), mNumberFormat);
                }

            }

            summaryDataSheet.addCell(expMLH2P);


            Label expLab3PMOM = new Label(2, nextRowForInsert, nullValue, cellCenterFormat);
            Label expLab3PMLH = new Label(4, nextRowForInsert, nullValue, cellCenterFormat);
            summaryDataSheet.addCell(expLab3PMOM);
            summaryDataSheet.addCell(expLab3PMLH);


            //Populating gamma Rows


            nextRowForInsert = 6;
            nextColForInsert = 1;

            Number  gammaMOM2P = null;
            if(nextColForInsert==fittedCol && nextRowForInsert==fittedRow){
                gammaMOM2P = new Number(nextColForInsert, nextRowForInsert, dataset.getMOM_G2PError(), selectedDistFormat);

            } else {
                if(dataset.getMOM_G2PError()<0.0001){
                    gammaMOM2P = new Number(nextColForInsert, nextRowForInsert, dataset.getMOM_G2PError(), mNumberFormatRaw);
                } else {
                    gammaMOM2P = new Number(nextColForInsert, nextRowForInsert, dataset.getMOM_G2PError(), mNumberFormat);
                }

            }

            summaryDataSheet.addCell(gammaMOM2P);

            nextColForInsert = 2;

            Number  gammaMOM3P = null;
            if(nextColForInsert==fittedCol && nextRowForInsert==fittedRow){

                gammaMOM3P = new Number(nextColForInsert, nextRowForInsert, dataset.getMOM_G3PError(), selectedDistFormat);
            } else {
                if(dataset.getMOM_G3PError()<0.0001){
                    gammaMOM3P = new Number(nextColForInsert, nextRowForInsert, dataset.getMOM_G3PError(), mNumberFormatRaw);
                } else {
                    gammaMOM3P = new Number(nextColForInsert, nextRowForInsert, dataset.getMOM_G3PError(), mNumberFormat);
                }

            }

            summaryDataSheet.addCell(gammaMOM3P);

            nextColForInsert = 3;

            Number gammaMLH2P = null;
            if(nextColForInsert==fittedCol && nextRowForInsert==fittedRow){
                gammaMLH2P = new Number(nextColForInsert, nextRowForInsert, dataset.getML_G2PError(), selectedDistFormat);

            } else {
                if(dataset.getML_G2PError()<0.0001){
                    gammaMLH2P = new Number(nextColForInsert, nextRowForInsert, dataset.getML_G2PError(), mNumberFormatRaw);
                } else {
                    gammaMLH2P = new Number(nextColForInsert, nextRowForInsert, dataset.getML_G2PError(), mNumberFormat);
                }

            }

            summaryDataSheet.addCell(gammaMLH2P);

            nextColForInsert = 4;

            Number gammaMLH3P = null;
            if(nextColForInsert==fittedCol && nextRowForInsert==fittedRow){

                gammaMLH3P = new Number(nextColForInsert, nextRowForInsert, dataset.getML_G3PError(), selectedDistFormat);
            } else {
                if(dataset.getML_G3PError()<0.0001){
                    gammaMLH3P = new Number(nextColForInsert, nextRowForInsert, dataset.getML_G3PError(), mNumberFormatRaw);
                } else {
                    gammaMLH3P = new Number(nextColForInsert, nextRowForInsert, dataset.getML_G3PError(), mNumberFormat);
                }

            }

            summaryDataSheet.addCell(gammaMLH3P);

            //////////////////////////////////////////
            //////////////////////////////////////////
            //////////////////////////////////////////
            ///Populating projected data into datasheet

            WritableSheet projectedDataSheet = wb.createSheet(Constants.XLS_PROJECTED_DATA, 2);

            Label periodLabel = new Label(0,0,Constants.XLS_PERIOD_HEADER, cellCenterBoldFormat);
            Label pxLabel = new Label(1,0,Constants.XLS_PX_HEADER, cellCenterBoldFormat);
            Label projLabel = new Label(2,0,Constants.XLS_PROJ_VALUE, cellCenterBoldFormat);

            projectedDataSheet.addCell(periodLabel);
            projectedDataSheet.addCell(pxLabel);
            projectedDataSheet.addCell(projLabel);

            size = dataset.getProjectedValuesList().size();
            rowNum = 1;

            for(int i=0; i<size; i++){

                Number periodNumber = new Number(0,rowNum, dataset.getProjectedValuesList().get(i).getPeriodo(),mNumberFormatRaw);
                Number pxNumber;

                if(dataset.getProjectedValuesList().get(i).getPx()<0.0001){
                    pxNumber = new Number(1,rowNum, dataset.getProjectedValuesList().get(i).getPx(),mNumberFormatRaw);
                } else {
                    pxNumber = new Number(1,rowNum, dataset.getProjectedValuesList().get(i).getPx(),mNumberFormat);
                }


                Number prjValueNumber = new Number(2,rowNum, dataset.getProjectedValuesList().get(i).getFittedV(),mNumberFormat);

                projectedDataSheet.addCell(periodNumber);
                projectedDataSheet.addCell(pxNumber);
                projectedDataSheet.addCell(prjValueNumber);

                rowNum++;
            }

            size = dataset.getDataList().size();

            WritableSheet fittedDataSheet = wb.createSheet(Constants.XLS_FITTED_DATA, 3);

            Label trLabel = new Label(0,0,Constants.XLS_TR_HEADER, cellCenterBoldFormat);
            Label valueLabel = new Label(1,0,Constants.XLS_VALUE_HEADER, cellCenterBoldFormat);
            Label fittedLabel = new Label(2,0,Constants.XLS_FITTED_HEADER, cellCenterBoldFormat);
            Label errorLabel = new Label(3,0,Constants.XLS_ERROR_HEADER, cellCenterBoldFormat);

            fittedDataSheet.addCell(trLabel);
            fittedDataSheet.addCell(valueLabel);
            fittedDataSheet.addCell(fittedLabel);
            fittedDataSheet.addCell(errorLabel);

            rowNum = 1;


            for(int i=0; i<size; i++){
                Number trNumber = new Number(0,rowNum, dataset.getTi(i),mNumberFormat);
                Number valueNumber = new Number(1,rowNum, dataset.getVi(i),mNumberFormat);
                Number fittedNumber = new Number(2,rowNum, dataset.getFiti(i),mNumberFormat);
                Number errorNumber = new Number(3,rowNum, dataset.getEi(i),mNumberFormat);

                fittedDataSheet.addCell(trNumber);
                fittedDataSheet.addCell(valueNumber);
                fittedDataSheet.addCell(fittedNumber);
                fittedDataSheet.addCell(errorNumber);

                rowNum++;
            }






            wb.write();
            wb.close();



        }catch(IOException ex){
            ex.printStackTrace();
            Log.e(TAG, ex.getMessage());
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }


    }

}

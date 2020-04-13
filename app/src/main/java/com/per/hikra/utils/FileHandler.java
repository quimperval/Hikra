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

import android.annotation.TargetApi;
import android.content.Context;
import android.icu.text.NumberFormat;
import android.icu.util.Calendar;
import android.net.ParseException;

import android.os.Build;

import android.util.Log;

import androidx.annotation.RequiresApi;


import com.per.hikra.models.Project;
import com.per.hikra.dbutils.SQLUtils;
import com.per.hikra.models.Variable;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileHandler {

    ArrayList<String> FileLines =null;
    private ArrayList<String> lineasarchivo = new ArrayList<String>();
    private String FullPathOpenedFile ="";
    ArrayList<String> TitleData = new ArrayList<>();

    private ArrayList<Variable> listOfInputVariables = null;
    private int posYearHeader = -1;
    private int posValueHeader = -1;


    private HashMap<String, Double> basicStatistics = null;

    private int whatIsBeingReaded = 0;

    private int isCalculated = -1;

    private boolean readingCalculatedFlag = false;
    //read_response_status = 1 : No title header
    //read_response_status = 2 : More than one title header
    //read_response_status = 3 : No flags in the file
    //read_response_status = 4 : "Can't be located data list. Verify file."
    //read_response_status = 5 : "Verify file, there is more than one data's header."
    //read_response_status = 6 : "Can't be located data list. Verify file."
    //read_response_status = 7 : "Verify file, there is more than one fitted data header."
    //read_response_status = 8 : "Can't be located fitted data  list. Verify file."
    //read_response_status = 9 : "Verify file, there is more than one projects's header."
    //read_response_status = 10: "Can't be located project header. Verify file."
    //read_response_status = 11: "Verify file, there is more than one basic statistic's header."
    //read_response_status = 12: "Can't be located basic statistics header . Verify file."

    //read_response_status = 13: "Failed to load file"

    private ArrayList<String> listOfErrors = new ArrayList<String>();
    private int read_response_status = -1;
    BufferedReader mBuffReader = null;

    private Context mContext;
    private String projectName = "";

    //save status = 1;
    private int save_status = -1;

    public FileHandler(){

    }

    public FileHandler(Context mContext){
        this.mContext = mContext;
    }

    public FileHandler(BufferedReader mBuffReader){
        this.mBuffReader = mBuffReader;
    }

    public FileHandler(String FullPathOpenedFile){
        this.FullPathOpenedFile = FullPathOpenedFile;
    }

    public String getProjectName() {

        if(TitleData==null){
            //Nothing
        } else {
            if(TitleData.size()>0){
                projectName = TitleData.get(0);
            }
        }

        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getRead_response_status(){
        return read_response_status;
    }


    public void setRead_response_status(int read_response_status) {
        this.read_response_status = read_response_status;
    }

    public int getSave_status() {
        return save_status;
    }

    public void setSave_status(int save_status) {
        this.save_status = save_status;
    }

    public void readfile(){
        if (lineasarchivo.isEmpty() || lineasarchivo.size() == 0) {
            //Do nothing
        } else {
            lineasarchivo.clear();
        }

        readFileV2();

        if(this.listOfInputVariables==null){
            Log.i("FileHandler", "listOfInputVariables is null");
        } else {
            Log.i("FileHandler", "listOfInputVariables size is " + listOfInputVariables.size());
        }
    }




    public String removeseparator(String separador, String cadenax) {
        String valor;
        int XX = 1;
        if (!cadenax.contains(separador)) {

        } else {
            do {
                if (cadenax.substring(0, 1).equals(separador)) {
                    cadenax = cadenax.substring(1, cadenax.length());

                } else {

                    XX = 0;
                }

            } while (XX > 0);


        }

        return cadenax;
    }


    public String searchString(String cadena, ArrayList<String> Lista) {
        int posicion;
        String pos = "";
        for (int k3 = 0; k3 <= (Lista.size() - 1); k3++) {
            if (cadena.equals(Lista.get(k3))) {
                posicion = k3;
                pos = String.valueOf(posicion);
            } else {

            }

        }
        return pos;

    }

    public boolean doubleParseable(String numero) {
        boolean parseable = true;
        try {

            double num = Double.parseDouble(numero);

        } catch (ParseException e) {
            parseable = false;

        }
        return parseable;
    }

    public boolean IntParseable(String numero) {
        boolean parseable = true;
        try {

            int num = Integer.parseInt(numero);

        } catch (ParseException e) {
            parseable = false;

        }
        return parseable;
    }

    public int repeticiones(ArrayList<String> listado1, String palabra){
        int repeticion = 0;

        int contarlista;

        int listasize;

        listasize = listado1.size();

        String nombre;

        for(contarlista=0; contarlista<=(listasize-1);contarlista++){
            nombre = listado1.get(contarlista);
            if (nombre.equals(palabra)){
                repeticion = repeticion+1;
            } else {

            }

        }
        return repeticion;
    }


    public boolean hasErrors(){
        int counter = 0;

        boolean response = false;

        for(int i=1; i<14; i++){
            if(read_response_status ==i){
                counter++;
            }
        }

        if(counter==0){
            response = false;
        } else {
            response = true;
        }
        return response;
    }


    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void save2file(ArrayList<Variable> listOfVariablesForSaving, HashMap<String, Double> basicStatistics,
                          String projectName, int wasCalculated, ArrayList<Variable> ProjectedValues, Context mContext, int projectId) {


        if(listOfVariablesForSaving==null){

            //save_status = 1: empty list of variables.
            this.save_status = 1;

        } else {


            if (projectName.isEmpty()) {

                //save_status = 2; "Set a name for the project."
                this.save_status = 2;


            } else {

                Pattern p = Pattern.compile("[^A-Za-z0-9]", Pattern.CASE_INSENSITIVE);
                Matcher m = p.matcher(projectName);
                boolean prueba2 = m.find();

                if (prueba2) {
                    save_status = 3; //"Don't use special characters.";


                } else {

                    String FILENAME = projectName+ ".csv";
                    //    private void populateFileList(String ProjectName, ArrayList<Variable> listOfVariablesForSaving, int wasCalculated) {
                    populateFileList(projectName, listOfVariablesForSaving, wasCalculated, basicStatistics,
                            ProjectedValues);



                    /**
                     *
                     * AQUI INICIA LA CREACIÓN DE LA CARPETA Crapper
                     *
                     */

                    String carpeta = "Hikra";

                    //String FullPath =

                    File folder = new File( mContext.getExternalFilesDir(null).getAbsolutePath() + File.separator + carpeta);
                    boolean success = false;
                    boolean success2 = false;
                    if (!folder.exists()) {
                        success = folder.mkdirs();


                        if (success) {
                        } else {
                        }
                    }
                    /**
                     * AQUI INICIA LA CREACIÓN DEL ARCHIVO.
                     */

                    try {


                        File inpFile = new File(folder, FILENAME);

                        String FullPath = inpFile.getAbsolutePath();
                        Log.i("save2File", "fullpath: " + FullPath);

                        Date currentTime = Calendar.getInstance().getTime();

                        Log.i("save2file", "currentTime: " + currentTime.toString());

                        String LastUpdate = currentTime.toString();

                        if (inpFile.exists()) {
                            success2 = inpFile.mkdir();


                        }

                        if (success2) {

                            Log.i("save2File", "success2 Value: " + success2);



                            FileOutputStream streaming = new FileOutputStream(inpFile);


                            int max = FileLines.size();


                            for (int contar = 0; contar <= (max - 1); contar++) {
                                streaming.write(FileLines.get(contar).getBytes());
                            }
                            streaming.flush();
                            streaming.close();


                            this.save_status = 200;


                            Project mProject = new Project();
                            mProject.setLastUpdate(LastUpdate);
                            mProject.setProjectName(projectName);
                            mProject.setPathOfFile(FullPath);

                            SQLUtils mSQL = new SQLUtils(mContext);

                            Log.i("save2File", "This is a new file, New Record will be inserted");

                            Log.i("save2File","Inserting record: " + mSQL.insertProject(mProject));
                            Log.i("FileHandler", "File Created");
                        } else {
                            Log.i("FileHandler", "File: " + inpFile.getParent() + " already exists.");


                            FileOutputStream streaming = new FileOutputStream(inpFile, false);


                            int max = FileLines.size();


                            for (int contar = 0; contar <= (max - 1); contar++) {
                                streaming.write(FileLines.get(contar).getBytes());
                            }
                            streaming.flush();
                            streaming.close();

                            this.save_status = 200;


                            Project mProject = new Project();
                            mProject.setProjectId(projectId);
                            mProject.setLastUpdate(LastUpdate);
                            mProject.setProjectName(projectName);
                            mProject.setPathOfFile(FullPath);

                            SQLUtils mSQL = new SQLUtils(mContext);


                            Log.i("save2File", "Project Id: " + projectId);
                            if(projectId<=0){

                                Log.i("save2File", "New Record will be inserted");
                                Log.i("save2File","Inserting record: " + mSQL.insertProject(mProject));
                            } else {
                                Log.i("save2File", "Record will be updated");
                                Log.i("save2File","Updating record: " + mSQL.updateProjectData(mProject));
                            }

                            Log.i("FileHandler", "File overwrited: " + inpFile.getParent());
                        }


                    } catch (IOException e) {
                        Log.i("FileHandler", "Failed to create file.");
                    }


                }

                /**
                 * AQUI TERMINA LA CREACIÓN DEL ARCHIVO DE TEXTO, CREATE - SAVE - CLOSE
                 *
                 */

            }
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.N)

    private void populateFileList(String ProjectName, ArrayList<Variable> listOfVariablesForSaving,
                                  int wasCalculated,HashMap<String, Double> basicStatistics,  ArrayList<Variable> ProjectedValues) {

        FileLines = new ArrayList<String>();


        String SEPARATOR = " ";
        String END_OF_LINE = Constants.END_OF_LINE;

        String spaceline = "";
        String puntoycoma = ";;";

        int cantData;



        //Log.i("Hay-populatefilelist", "Cantidad de datos por agregar"+cantData);
        /**
         * Crear encabezado para el archivo.
         */
        String Titleheader = Constants.TITLE_HEADER + END_OF_LINE;

        FileLines.add(Titleheader);

        FileLines.add(ProjectName + END_OF_LINE);

        /**
         * Crear encabezado de rawdata
         *
         *
         */

        String Nodesheader1 = Constants.RAW_HEADER + END_OF_LINE;
        String Name_guion = Constants.NAME_GUION;
        String values_guion = Constants.VALUES_GUION;


        String Nodesheader2 = ";;" + Constants.YEAR_HEADER + SEPARATOR +  Constants.VALUE_HEADER + END_OF_LINE;

        FileLines.add(Nodesheader1);

        FileLines.add(Nodesheader2);

        String Nodesheader3;

        Nodesheader3 = puntoycoma + Name_guion + SEPARATOR + values_guion + END_OF_LINE;

        FileLines.add(Nodesheader3);

        /** Add rawdata
         *
         *
         */
        for (int i = 0; i <= (listOfVariablesForSaving.size() - 1); i++) {
            Variable Pairdummy;
            int year;
            double Data;

            Pairdummy = listOfVariablesForSaving.get(i);
            year = Pairdummy.getyear();
            Data = Pairdummy.getvalue();
            String DataS, YearS;

            YearS = String.valueOf(year);
            DataS = String.format("%.3f",Data);
            String Lineadummy = year + SEPARATOR + Data + END_OF_LINE;
            //Log.i("Hay-populatefilelist","Line added:"+Lineadummy);
            FileLines.add(Lineadummy);
        }

        /**
         * Agregar header para estadisticos
         */

        String flagsheader1 = "[FLAGS]"+END_OF_LINE;

        String Statheader1 = "[BASIC STATISTICS]" + END_OF_LINE;
        //String Name_guion = "--------------";
        //String values_guion = "----------";
        String Statheader2 = Name_guion+values_guion+END_OF_LINE;

        String Fitheader1 = "[FITTED VALUES]"+END_OF_LINE;
        String Fitheader2 = Name_guion+values_guion+END_OF_LINE;
        String Fitheader3 = ";;" + Constants.TR_HEADER + SEPARATOR + Constants.DATA_HEADER +
                SEPARATOR + Constants.FITTED_HEADER+SEPARATOR + Constants.ERROR_HEADER +END_OF_LINE;

        String ProjHeader1 = "[PROJECTED VALUES]"+END_OF_LINE;
        String ProjHeader2 = Name_guion+values_guion+END_OF_LINE;

        // double valor1 = dummyV.getPeriodo();
        //                    double valor2 = dummyV.getPx();
        //                    double valor3 = dummyV.getFittedV();
        String ProjHeader3 = ";;" + Constants.TR_HEADER + SEPARATOR + Constants.PX_HEADER + SEPARATOR + Constants.PROJECTED_VALUE_HEADER;

        String blank0 = Constants.BLANK_0;
        FileLines.add(blank0);

        int calcdone = -1;
        calcdone = wasCalculated;

        if(calcdone==0){
            //Do Nothing.

            String blank1 = " "+END_OF_LINE;

            FileLines.add(flagsheader1);
            FileLines.add("CALCULATED: NO");
            FileLines.add(blank1);

            FileLines.add(Statheader1);
            FileLines.add(Statheader2);
            FileLines.add(blank1);

            FileLines.add(Fitheader1);
            FileLines.add(Fitheader2);
            FileLines.add(Fitheader3);
            FileLines.add(blank1);

            FileLines.add(ProjHeader1);
            FileLines.add(Fitheader2);
            FileLines.add(blank1);

        } else {

            if(calcdone==1){

                //Create list for statistics and for calculated values.
                /**
                 *
                 * Crear contenido para los datos estadísticos básicos.
                 *
                 */

                String mean;
                String std_dev;
                String skewness;
                mean = Constants.BSTAT_MEAN + ": "+String.format("%.3f",basicStatistics.get(Constants.BSTAT_MEAN))+END_OF_LINE;
                std_dev = Constants.BSTAT_STD_DEV+ ": " +String.format("%.3f",basicStatistics.get(Constants.BSTAT_STD_DEV))+END_OF_LINE;
                skewness = Constants.BSTAT_SKEW + ": " +String.format("%.3f",basicStatistics.get(Constants.BSTAT_SKEW))+END_OF_LINE;
                String blank1 = " "+END_OF_LINE;

                FileLines.add(Statheader1);
                FileLines.add(Statheader2);
                FileLines.add(mean);
                FileLines.add(std_dev);
                FileLines.add(skewness);
                FileLines.add(blank1);

                /**
                 *
                 * Crear contenido para FLAGS
                 *
                 *
                 */

                FileLines.add(flagsheader1);
                FileLines.add("CALCULATED: YES");
                FileLines.add(blank1);


                /**
                 * Crear contenido para los datos ajustados.
                 *
                 */
                FileLines.add(Fitheader1);
                FileLines.add(Fitheader2);

                for (int k=0; k<=(listOfVariablesForSaving.size()-1);k++) {
                    double valor1 = listOfVariablesForSaving.get(k).getT();
                    double valor2 = listOfVariablesForSaving.get(k).getvalue();
                    double valor3 = listOfVariablesForSaving.get(k).getFittedV();
                    double valor4 = listOfVariablesForSaving.get(k).getFittedE();

                    NumberFormat nf = NumberFormat.getInstance();
                    String val1 = nf.format(valor1);
                    String val2 = nf.format(valor2);
                    String val3 = nf.format(valor3);
                    String val4 = nf.format(valor4);


                    String dummy = val1 +" " + val2 +" " + val3 +" " +val4 +END_OF_LINE;
                    FileLines.add(dummy);
                }

                FileLines.add(ProjHeader1);
                FileLines.add(ProjHeader2);
                FileLines.add(ProjHeader3);
                int NP = ProjectedValues.size();

                for(int k1 = 0; k1<=(NP-1); k1++){
                    Variable dummyV = ProjectedValues.get(k1);
                    double valor1 = dummyV.getPeriodo();
                    double valor2 = dummyV.getPx();
                    double valor3 = dummyV.getFittedV();

                    String v1, v2, v3;

                    v1 = String.format("%.3f", valor1);
                    v2 = String.format("%.3f", valor2);
                    v3 = String.format("%.3f", valor3);

                    String lineai = v1 +" " +v2 + " "+v3 +" " + END_OF_LINE;
                    FileLines.add(lineai);
                }

                //FileLines.add(blank1);

            }

        }
        FileLines.add(spaceline + END_OF_LINE);

    }


    public void initializeListOfInputVariables(){
        if(listOfInputVariables==null){
            listOfInputVariables  = new ArrayList<Variable>();
        }
    }

    public ArrayList<Variable> getReadedVariables(){
        return listOfInputVariables;
    }


    public void readFileV2(){

        String line = "";
            try {
                while ((line = mBuffReader.readLine()) != null){
                    //Log.i("readFileV2", "linea: " + line);
                    if(checkHeader(line)){
                        Log.i("readFileV2", "go to next line");
                        continue;
                    } else {
                        readLineContent(line);
                    }


                }
            } catch (IOException e) {
                e.printStackTrace();
            }



    }

    public boolean checkHeader(String line){
        boolean response = false;
        //[TITLE]
        if(line.equals(Constants.TITLE_HEADER)){
            this.whatIsBeingReaded = Constants.IS_READING_TITLE;
            response = true;
        }

        //[RAW DATA]

        if(line.equals(Constants.RAW_HEADER)){
            Log.i("checkHeader", "Starts raw header reading part");
            Log.i("checkHeader", line);
            this.whatIsBeingReaded = Constants.IS_READING_RAW_DATA;
            response = true;
        }
        //[FLAGS]

        if(line.equals(Constants.FLAGS)){
            Log.i("checkHeader", "Starts flags header reading part");
            Log.i("checkHeader", line);
            this.whatIsBeingReaded = Constants.IS_READING_FLAGS;
            response = true;
        }

        // [BASIC STATISTICS]

        if(line.equals(Constants.BASIC_HEADER)){
            this.whatIsBeingReaded = Constants.IS_READING_BASIC_STATISTICS;
            response = true;
        }

        // [FITTED VALUES]

        if(line.equals(Constants.FITTED_HEADER)){
            this.whatIsBeingReaded = Constants.IS_READING_FITTED_DATA;
            response = true;
        }

        //[PROJECTED VALUES]

        if(line.equals(Constants.PROJ_HEADER)){
            this.whatIsBeingReaded = Constants.IS_READING_PROJECTED_DATA;
            response = true;
        }

        return response;
    }

    public void readLineContent(String line){


        if(line !=null){
            if(line.equals("") || line.isEmpty() || line.equals(Constants.BLANK) || line.equals(Constants.BLANK_0)){
                Log.i("Read line Content", "Doing nothing, line empty.");
            } else {
                Log.i("Read line Content","Reading line");

                switch (this.whatIsBeingReaded){
                    case Constants.IS_READING_TITLE:
                        readTitle(line);
                        break;

                    case Constants.IS_READING_RAW_DATA:
                        readRawData(line);
                        break;

                    case Constants.IS_READING_FLAGS:
                        readFlags(line);
                        break;

                    case Constants.IS_READING_BASIC_STATISTICS:
                        readBasicStatistics(line);
                        break;

                    case Constants.IS_READING_FITTED_DATA:
                        readingFittedData(line);
                        break;

                    case Constants.IS_READING_PROJECTED_DATA:
                        readProjectedData(line);
                        break;

                    default:
                        Log.i("ReadFile", "Doing nothing");
                        break;
                }


            }
        } else {
            Log.i("Read line Content", "Doing nothing, line is null");
        }



    }

    public void readTitle(String line){
        if(line.substring(0,1).equals(";")){
            //Do nothing
            Log.i("readTitle", "Comment line");

        } else {
            this.projectName = line;
            Log.i("readTitle", "Project Name: " + projectName);
        }
    }

    public void readRawData(String line){
        Log.i("readRawData", "Reading raw data");

        Log.i("readRawData", line);

        if(line.substring(0,1).equals(";")){


            do{
                Log.i("readRawData", "value to check: " +line.substring(0,1));

                if(line.substring(0,1).equals(";")){
                    Log.i("readRawData", "before: " + line);
                    line = line.substring(1,line.length());
                    Log.i("readRawData", "after:" + line);
                }

            } while (line.substring(0,1).equals(";"));

            String[] rawHeaders = line.split(" ");

            Log.i("readRawData", "Checking headers");
            if(rawHeaders[0].equals(Constants.YEAR_HEADER)){

                this.posYearHeader = 0;

            } else {
                if(rawHeaders[1].equals(Constants.YEAR_HEADER)){
                    this.posYearHeader = 1;
                }
            }

            if(rawHeaders[0].equals(Constants.VALUE_HEADER)){
                this.posValueHeader = 0;
            } else {
                if(rawHeaders[1].equals(Constants.VALUE_HEADER)){
                    this.posValueHeader = 1;
                }
            }

            Log.i("readRawData", "Year header pos: " + this.posYearHeader);
            Log.i("readRawData", "Value header pos: " + this.posValueHeader);

        } else {
            Log.i("readRawData", "Reading year-value data pair");
            String[] yearData = line.split(" ");

            if(yearData[0].equals(Constants.NAME_GUION) || yearData[0].equals(Constants.VALUES_GUION) ||
                yearData[1].equals(Constants.NAME_GUION) || yearData[1].equals(Constants.VALUES_GUION)){
                Log.i("readRawData", "Doing nothing");
            } else {

                    Variable mVar = new Variable();

                    try
                    {
                        mVar.setyear(Integer.parseInt(yearData[this.posYearHeader]));
                        mVar.setvalue(Double.parseDouble(yearData[this.posValueHeader]));
                        Log.i("readRawData", "Year: " + mVar.getyear() + ", Value: " + mVar.getvalue());
                        if(listOfInputVariables ==null){
                            listOfInputVariables = new ArrayList<Variable>();
                        }

                        listOfInputVariables.add(mVar);
                    }
                    catch (NumberFormatException nfe)
                    {
                        System.out.println("NumberFormatException: " + nfe.getMessage());
                    }


            }


        }


    }


    public void readFlags(String line){
        Log.i("readFlags", "reading flags line: " + line);
        if(isCalculated==1){
            //Do nothing
        } else {
            if(line.contains(Constants.CALCULATED_NO)){
                Log.i("readFlags", "is not calculated");
                this.isCalculated = 0;
            }

            if(line.contains(Constants.CALCULATED_YES)){
                Log.i("readFlags", "is calculated");
                this.isCalculated = 1;
            }
        }

    }


    public void readBasicStatistics(String line){

        if(isCalculated==1){
            if(line.contains(Constants.BSTAT_MEAN)){
                if(basicStatistics==null){
                    basicStatistics = new HashMap<String, Double>();
                }

                if(line.contains(":")){
                    String meanStr = line.substring(line.indexOf(":")).trim();

                    try{

                        Double meanValue = Double.parseDouble(meanStr);
                        basicStatistics.put(Constants.BSTAT_MEAN, meanValue);

                    } catch (NumberFormatException nfe) {
                        Log.i("readBasicStatistics", "Error reading mean value from basic statistics");
                        System.out.println("NumberFormatException: " + nfe.getMessage());

                    }


                }
            }

            if(line.contains(Constants.BSTAT_STD_DEV)){
                if(basicStatistics==null){
                    basicStatistics = new HashMap<String, Double>();
                }


                if(line.contains(":")){

                    String stdDevStr = line.substring(line.indexOf(":")).trim();

                    try{
                        Double stdDev = Double.parseDouble(stdDevStr);
                        basicStatistics.put(Constants.BSTAT_STD_DEV, stdDev);

                    } catch (NumberFormatException nfe) {
                        Log.i("readBasicStatistics", "Error reading Standard Deviation value from basic statistics");
                        System.out.println("NumberFormatException: " + nfe.getMessage());

                    }
                }
            }

            if(line.contains(Constants.BSTAT_SKEW)){
                if(basicStatistics==null){
                    basicStatistics = new HashMap<String, Double>();
                }

                if(line.contains(":")){
                    String skewStr = line.substring(line.indexOf(":")).trim();

                    try{
                        Double skewness = Double.parseDouble(skewStr);
                        basicStatistics.put(Constants.BSTAT_SKEW, skewness);
                    } catch (NumberFormatException nfe) {
                        Log.i("readBasicStatistics", "Error reading Skewness value from basic statistics");
                        System.out.println("NumberFormatException: " + nfe.getMessage());

                    }
                }
            }
        } else {
            //Do Nothing
        }

    }

    public void readingFittedData(String line){

        if(isCalculated==1){
            if(line.contains(Constants.TR_HEADER)){
                Log.i("readingFittedData", "reading headers");

                do{

                    if(line.substring(0,1).equals(";")){
                        Log.i("readingFittedData", "before: " + line);
                        line = line.substring(1,line.length());
                        Log.i("readingFittedData", "after:" + line);
                    }

                }while(line.substring(0,1).equals(";"));

            }
        } else {
            Log.i("readingFittedData", "skipping as calculation wasn't performed");
            //Do Nothing
        }

    }

    public void readProjectedData(String line){
        if(isCalculated==1){
            if(line.contains(Constants.TR_HEADER)){
                Log.i("readProjectedData", "reading headers");

                do{

                    if(line.substring(0,1).equals(";")){
                        Log.i("readProjectedData", "before: " + line);
                        line = line.substring(1,line.length());
                        Log.i("readProjectedData", "after:" + line);
                    }

                }while(line.substring(0,1).equals(";"));

            }
        } else {
            Log.i("readProjectedData", "skipping as calculation wasn't performed");
            //Do Nothing
        }

    }


    public boolean validateProjectName(String name){

        Pattern p = Pattern.compile("[^A-Za-z0-9]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(name);
        boolean prueba2 = m.find();

        return prueba2;
    }

}

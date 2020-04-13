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

package com.per.hikra.statistics;

import android.app.Application;
import android.icu.text.NumberFormat;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.per.hikra.utils.Constants;
import com.per.hikra.models.DataPair;
import com.per.hikra.models.Variable;
import com.per.hikra.utils.HikraLogger;

import static java.lang.System.out;
import java.lang.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.joda.time.LocalTime;


import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;





public class Statistics extends Application{

    private String TAG = "Statistics";
    private String mProjectName;

    private HikraLogger mLogger;

    private HashMap<String, Double> basicStatisticsHM  = null;
    private int Avance=0;
    //If isCalculated == 0, Then should be performed all the calculations
    //If isCalculated ==1, calculations are completed and could be visualized, saved or shared.
    private int isCalculated;

    //Esta lista tiene los valores sin ordenar.
    private ArrayList<Variable> DataList;

    private int projectId = -1;

    private ArrayList<Variable> ProjectedValues = new ArrayList<>();
    // Si el valor es 1 se está haciendo el cálculo, si es igual a 0 no se está calculando.
    private int encalculo;

    private String ErrorSummary;
    private ArrayList<String> listaerrores = new ArrayList<>();


    //Resultados del test de Wald Wolfowitz.
    private double pvalue_wwtest, Z_wwtest, R_wwtest;
    private int posmin;
    private double Media, Desv_std, Var, GMed, Mm2, Mm3;
    private int N_data;
    private ArrayList<Double> List_values;
    private double Min_val;
    private String Minimo;
    private double skewness;
    private ArrayList<Double> Errores = new ArrayList<>();
    private ArrayList<String> nombrefdp = new ArrayList<>();

    private String rawdata="";
    ////////////////////////////////////////
    //Estos son los valores del ajuste por momentos, MOM
    /**
     *
     * Distribución Normal
     *
     * */

    private double MOM_mu, MOM_sigma;
    private double EEA_MOM_N;

    /**
     *
     * Distribución LogNormal dos parámetros.
     *
     * */
    private double MOM_sigma_LN2P;
    private double MOM_mu_LN2P;
    private double EEA_MOM_LN2P;

    /**
     *
     * Distribución LogNormal tres parámetros.
     *
     * */
    private double MOM_sigma_LN3P;
    private double MOM_mu_LN3P;
    private double MOM_a_LN3P;
    private double MOM_media_x_a_LN3P;
    private double MOM_var_x_a_LN3P;
    private double MOM_z2_LN3P;
    private double MOM_w_LN3P;
    private double EEA_MOM_LN3P;

    /**
     *
     * Distribución Gumbel
     *
     * */

    private double MOM_alpha_Gumbel;
    private double MOM_beta_Gumbel;
    private double EEA_MOM_Gumbel;

    /**
     *
     * Distribución exponencial
     *
     * */
    private double MOM_alpha_EXP;
    private double MOM_epsilon_EXP;
    private double EEA_MOM_EXP;

    /**
     *
     * Distribución gamma de dos parámetros.
     *
     * */


    private double MOM_alpha_Gamma2p;
    private double MOM_beta_Gamma2p;
    private double EEA_MOM_G2p;


    /**
     *
     * Gamma de tres parámetros
     *
     * */

    private double MOM_alpha_Gamma3p;
    private double MOM_beta_Gamma3p;
    private double EEA_MOM_G3p;
    private double MOM_g_Gamma3p;

    ////////////////////////////////////////
    //Estos son los valores del ajuste por máxima verosimilitud, ML
    /**
     *
     * Distribución Normal
     *
     * */
    private double ML_mu, ML_sigma;
    private double EEA_ML_N;

    /**
     *
     * Distribución LogNormal de 2 parámetros.
     *
     * */

    private double ML_sigma_LN2P;
    private double ML_mu_LN2P;
    private double EEA_ML_LN2P;

    /**
     *
     * Distribución LogNormal de 3 parámetros.
     *
     * */

    private double ML_sigma_LN3P;
    private double ML_mu_LN3P;
    private double ML_a_LN3P;
    private double ML_media_x_a_LN3P;
    private double ML_var_x_a_LN3P;
    private double ML_z2_LN3P;
    private double ML_w_LN3P;
    private double EEA_ML_LN3P;

    /**
     * Distribución Gumbel ML
     * */

    private double ML_alpha_Gumbel;
    private double ML_beta_Gumbel;
    private double EEA_ML_Gumbel;

    /**
     *
     * Distribución exponencial ML
     *
     * */
    private double ML_alpha_EXP;
    private double ML_epsilon_EXP;
    private double EEA_ML_EXP;

    /**
     *
     * Distribución gamma de dos parámetros.
     *
     * */


    private double ML_alpha_Gamma2p;
    private double ML_beta_Gamma2p;
    private double EEA_ML_G2p;
    private double ML_A_Gamma2p;
    private double ML_U_Gamma2p;

    /**
     *
     * Gamma de tres parámetros
     *
     * */

    private double ML_alpha_Gamma3p;
    private double ML_beta_Gamma3p;
    private double EEA_ML_G3p;
    private double ML_g_Gamma3p;



    public Statistics(){

        mProjectName = "";
        DataList = new ArrayList<>();

        List_values = new ArrayList<>();

    }


    public String getmProjectName() {
        return mProjectName;
    }

    public void setmProjectName(String mProjectName) {
        this.mProjectName = mProjectName;
    }

    public ArrayList<Variable> getDataList() {
        return DataList;
    }

    public void setDataList(ArrayList<Variable> dataList) {

        Log.i(TAG, "setDataList");

        this.DataList = dataList;

        Log.i(TAG, "DataList size is: " + DataList.size());
        isCalculated = 0;
    }

    public void adddata(Variable datoi){

        DataList.add(datoi);
        isCalculated = 0;
    }

    public int size(){

        return DataList.size();

    }

    public DataPair getData(int i){

        return DataList.get(i);

    }

    public void eraseData(int i){
        DataList.remove(i);
    }


    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public void addTestData(){
        DataList.clear();

        mLogger.printLog("Hay-AddData","Agregando datos de prueba.");

        int max = Prueba.length;
        for(int i=0; i<=(max-1); i++) {

            Variable dato = new Variable();

            dato.setyear(Prueba[i][0]);
            dato.setvalue(Prueba[i][1]);
            DataList.add(dato);
            List_values.add((double)Prueba[i][1]);

        }

        int max2 = DataList.size();
        mLogger.printLog("Hay-AddData","Se agregaron: "+ max2+" datos.");
        N_data = max2;

    }

    public void showAllD(){


        if(mLogger==null){
            mLogger = new HikraLogger(getApplicationContext());
        }

        int max = DataList.size();

        for(int i=0; i<=(max-1); i++) {

            DataPair dummy = new DataPair();

            dummy = DataList.get(i);

            int year = dummy.getyear();
            double value = dummy.getvalue();

            mLogger.printLog("Hay-showAllD","Dato: " + i +", año: " + year + " valor: "+ value +".");

        }
    }




    public int posbyValue(ArrayList<DataPair> listaentrada, double value){

        int limit3 = listaentrada.size();
        int pos = 0;

        for(int j3= 0; j3<=(limit3-1); j3++){

            double valor = listaentrada.get(j3).getvalue();


            if(value == valor){
                pos = j3;
                break;
            } else {
                //NADA
            }
        }
        return pos;
    }


    public void OrderData(){
        // Populate DummyList with values from DataList.

        Log.i("Statistics", "size of list: " + DataList.size());

       Collections.sort(DataList, new Sortbyvalue());

       int SizeOrder = DataList.size();

        for(int j5 = 0; j5<=(SizeOrder-1);j5++) {
            float T1 = (SizeOrder+1)/ ((float)j5+1);
            DataList.get(j5).setT(T1);
            DataList.get(j5).setPx(1/T1);
        }

    }




    public void showOrdered(){

        int SizeOrder2 = DataList.size();


        for (int j4= 0; j4<=(SizeOrder2-1);j4++){
            mLogger.printLog("ShowOrdered","Dato: " + (j4+1) + "; Valor: " + DataList.get(j4).getvalue()
                    + "; T = "+ DataList.get(j4).getT());


        }

    }

    public void getTime(){

        LocalTime currentTime = new LocalTime();
        mLogger.printLog("Hay-getTime","The current local time is: " + currentTime);
    }


    public void findmin(){
        Min_val = EEA_MOM_N;
        posmin=0;


        //0 - EEA de la distribución normal, MOM.
        Errores.add(EEA_MOM_N);
        nombrefdp.add("Normal distribution.");

        //1 EEA de la distribución LN, MOM con dos parámetros.
        Errores.add(EEA_MOM_LN2P);
        nombrefdp.add("LogNormal Distribution with two parameters.");

        //2 EEA de la distribución LN 3P, MOM
        Errores.add(EEA_MOM_LN3P);
        nombrefdp.add("LogNormal Distribution with three parameters.");

        //3 EEA de la distribución Gumbel
        Errores.add(EEA_MOM_Gumbel);
        nombrefdp.add("Gumbel Distribution.");

        //4 EEA de la distribución Exponencial

        Errores.add(EEA_MOM_EXP);
        nombrefdp.add("Exponential Distribution.");

        //5 EEA de la distribución Gamma de dos parámetros.
        Errores.add(EEA_MOM_G2p);
        nombrefdp.add("Gamma Distribution with two parameters.");


        // /6 EEA de la distribución Gamma de tres parámetros.

        Errores.add(EEA_MOM_G3p);
        nombrefdp.add("Gamma Distribution with three parameters.");


         //7 EEA de la distribución Log Normal ML con dos parámetros.
        Errores.add(EEA_ML_LN2P);
        nombrefdp.add("LogNormal Distribution with two parameters.");

        //8 EEA de la distribución LogNormal con tres parámetros.
        Errores.add(EEA_ML_LN3P);
        nombrefdp.add("LogNormal Distribution with three parameters.");

        //9 EEA de la distribución Gumbel
        Errores.add(EEA_ML_Gumbel);
        nombrefdp.add("Gumbel Distribution.");

        //10 EEA de la distribución exponencial
        Errores.add(EEA_ML_EXP);
        nombrefdp.add("Exponential Distribution.");

        //11 EEA de la distribución Gamma con dos parámetros.
        Errores.add(EEA_ML_G2p);
        nombrefdp.add("Gamma Distribution with two parameters.");

        //12 EEA de la distribución Gamma con tres parámetros.
        Errores.add(EEA_ML_G3p);
        nombrefdp.add("Gamma Distribution with three parameters.");


        int Esize = Errores.size();

        for(int k1=0; k1<=(Esize-1);k1++){

            if (Min_val>(Errores.get(k1))){
                Min_val = Errores.get(k1);
                posmin=k1;

            } else {
                //NADA
            }

        }

        setminimo(posmin);

        Avance = 14;
    }

    public void setminimo(int pos){
        //0 - EEA de la distribución normal, MOM.
        Errores.add(EEA_MOM_N);
        nombrefdp.add("Normal distribution.");

        //1 EEA de la distribución LN, MOM con dos parámetros.
        Errores.add(EEA_MOM_LN2P);
        nombrefdp.add("LogNormal Distribution with two parameters.");

        //2 EEA de la distribución LN 3P, MOM
        Errores.add(EEA_MOM_LN3P);
        nombrefdp.add("LogNormal Distribution with three parameters.");

        //3 EEA de la distribución Gumbel
        Errores.add(EEA_MOM_Gumbel);
        nombrefdp.add("Gumbel Distribution.");

        //4 EEA de la distribución Exponencial

        Errores.add(EEA_MOM_EXP);
        nombrefdp.add("Exponential Distribution.");

        //5 EEA de la distribución Gamma de dos parámetros.
        Errores.add(EEA_MOM_G2p);
        nombrefdp.add("Gamma Distribution with two parameters.");


        // /6 EEA de la distribución Gamma de tres parámetros.

        Errores.add(EEA_MOM_G3p);
        nombrefdp.add("Gamma Distribution with three parameters.");


        //7 EEA de la distribución Log Normal ML con dos parámetros.
        Errores.add(EEA_ML_LN2P);
        nombrefdp.add("LogNormal Distribution with two parameters.");

        //8 EEA de la distribución LogNormal con tres parámetros.
        Errores.add(EEA_ML_LN3P);
        nombrefdp.add("LogNormal Distribution with three parameters.");

        //9 EEA de la distribución Gumbel
        Errores.add(EEA_ML_Gumbel);
        nombrefdp.add("Gumbel Distribution.");

        //10 EEA de la distribución exponencial
        Errores.add(EEA_ML_EXP);
        nombrefdp.add("Exponential Distribution.");

        //11 EEA de la distribución Gamma con dos parámetros.
        Errores.add(EEA_ML_G2p);
        nombrefdp.add("Gamma Distribution with two parameters.");

        //12 EEA de la distribución Gamma con tres parámetros.
        Errores.add(EEA_ML_G3p);
        nombrefdp.add("Gamma Distribution with three parameters.");

        Minimo = nombrefdp.get(posmin);
    }


    public void showresults(){
        if(mLogger==null){
            mLogger = new HikraLogger(getApplicationContext());
        }

        mLogger.printLog("Hay-results","//////////////////////////////");
        mLogger.printLog("Hay-results","//////////RESULTS/////////////");

        mLogger.printLog("Hay-results","La media es: "+String.format("%.3f",Media));
        mLogger.printLog("Hay-results","La desviación estándar es: "+String.format("%.3f",Desv_std));
        mLogger.printLog("Hay-results","La varianza es: "+String.format("%.3f",Var));
        mLogger.printLog("Hay-results","La media geométrica es: "+String.format("%.3f",GMed));
        mLogger.printLog("Hay-results","El coeficiente de asimetría es: "+String.format("%.3f",skewness));


        mLogger.printLog("Hay-results","//////////////////////////////////////////////////////////");
        mLogger.printLog("Hay-results","//////////AJUSTE POR EL MÉTODO DE MOMENTOS////////////////");
        mLogger.printLog("Hay-results","Distribución normal");
        mLogger.printLog("Hay-results","Media: "+String.format("%.3f", Media));
        mLogger.printLog("Hay-results","Desviación estándar: "+String.format("%.3f",Desv_std));
        mLogger.printLog("Hay-results","EEA: "+String.format("%.3f",EEA_MOM_N));


        mLogger.printLog("Hay-results","Distribución Lognormal de dos parámetros.");
        mLogger.printLog("Hay-results","Sigma: "+String.format("%.3f", MOM_sigma_LN2P));
        mLogger.printLog("Hay-results","Mu: "+String.format("%.3f", MOM_mu_LN2P));
        mLogger.printLog("Hay-results","EEA es: "+String.format("%.3f",EEA_MOM_LN2P));

        mLogger.printLog("Hay-results","Distribución Lognormal de tres parámetros.");
        mLogger.printLog("Hay-results","Sigma: "+String.format("%.3f", MOM_sigma_LN3P));
        mLogger.printLog("Hay-results","Mu: "+String.format("%.3f", MOM_mu_LN3P));
        mLogger.printLog("Hay-results","a: "+String.format("%.3f", MOM_a_LN3P));
        mLogger.printLog("Hay-results","EEA es: "+String.format("%.3f",EEA_MOM_LN3P));

        mLogger.printLog("Hay-results","Distribución Gumbel");
        mLogger.printLog("Hay-results","Alpha: "+String.format("%.3f",MOM_alpha_Gumbel));
        mLogger.printLog("Hay-results","Beta: "+String.format("%.3f",MOM_beta_Gumbel));
        mLogger.printLog("Hay-results","EEA es: "+String.format("%.3f",EEA_MOM_Gumbel));

        mLogger.printLog("Hay-results","Distribución Exponencial.");
        mLogger.printLog("Hay-results","Alpha: "+String.format("%.3f",MOM_alpha_EXP));
        mLogger.printLog("Hay-results","Epsilon: "+String.format("%.3f",MOM_epsilon_EXP));
        mLogger.printLog("Hay-results","EEA es: "+String.format("%.3f",EEA_MOM_EXP));

        mLogger.printLog("Hay-results","Distribución Gamma de dos parámetros");
        mLogger.printLog("Hay-results","Alpha: "+String.format("%.3f",MOM_alpha_Gamma2p));
        mLogger.printLog("Hay-results","Beta: "+String.format("%.3f",MOM_beta_Gamma2p));
        mLogger.printLog("Hay-results","EEA es: "+String.format("%.3f",EEA_MOM_G2p));

        mLogger.printLog("Hay-results","Distribución Gamma de tres parámetros");
        mLogger.printLog("Hay-results","Alpha: "+String.format("%.3f",MOM_alpha_Gamma3p));
        mLogger.printLog("Hay-results","Beta: "+String.format("%.3f",MOM_beta_Gamma3p));
        mLogger.printLog("Hay-results","gamma: "+String.format("%.3f",MOM_g_Gamma3p));
        mLogger.printLog("Hay-results","EEA es: "+String.format("%.3f",EEA_MOM_G3p));



        mLogger.printLog("Hay-results","//////////////////////////////////////////////////////////");
        mLogger.printLog("Hay-results","//////////AJUSTE POR EL MÉTODO DE MAXIMA VEROSIMILITUD////////////////");


        mLogger.printLog("Hay-results","Para la fdp normal el EEA es: "+String.format("%.3f",EEA_ML_N));

        mLogger.printLog("Hay-results","Distribución Lognormal de dos parámetros.");
        mLogger.printLog("Hay-results","Sigma: "+String.format("%.3f", ML_sigma_LN2P));
        mLogger.printLog("Hay-results","Mu: "+String.format("%.3f", ML_mu_LN2P));
        mLogger.printLog("Hay-results","EEA es: "+String.format("%.3f",EEA_ML_LN2P));
        mLogger.printLog("Hay-results","Distribución Lognormal de tres parámetros.");
        mLogger.printLog("Hay-results","Sigma: "+String.format("%.3f", ML_sigma_LN3P));
        mLogger.printLog("Hay-results","Mu: "+String.format("%.3f", ML_mu_LN3P));
        mLogger.printLog("Hay-results","a: "+String.format("%.3f", ML_a_LN3P));

        mLogger.printLog("Hay-results","EEA es: "+String.format("%.3f",EEA_ML_LN3P));
        mLogger.printLog("Hay-results","Distribución Gumbel");
        mLogger.printLog("Hay-results","Alpha: "+String.format("%.3f",ML_alpha_Gumbel));
        mLogger.printLog("Hay-results","Beta: "+String.format("%.3f",ML_beta_Gumbel));

        mLogger.printLog("Hay-results","EEA es: "+String.format("%.3f", EEA_ML_Gumbel));

        mLogger.printLog("Hay-results","Distribución Exponencial.");
        mLogger.printLog("Hay-results","Alpha: "+String.format("%.3f",ML_alpha_EXP));
        mLogger.printLog("Hay-results","Epsilon: "+String.format("%.3f",ML_epsilon_EXP));
        mLogger.printLog("Hay-results","EEA es: "+String.format("%.3f", EEA_ML_EXP));

        mLogger.printLog("Hay-results","Distribución Gamma de dos parámetros.");
        mLogger.printLog("Hay-results","Alpha: "+String.format("%.3f",ML_alpha_Gamma2p));
        mLogger.printLog("Hay-results","Beta: "+String.format("%.3f",ML_beta_Gamma2p));
        mLogger.printLog("Hay-results","EEA es: "+String.format("%.3f",EEA_ML_G2p));

        mLogger.printLog("Hay-results","Distribución Gamma de tres parámetros.");
        mLogger.printLog("Hay-results","Alpha: "+String.format("%.3f",ML_alpha_Gamma3p));
        mLogger.printLog("Hay-results","Beta: "+String.format("%.3f",ML_beta_Gamma3p));
        mLogger.printLog("Hay-results","gamma: "+String.format("%.3f",ML_g_Gamma3p));
        mLogger.printLog("Hay-results","EEA es: "+String.format("%.3f",EEA_ML_G3p));



        mLogger.printLog("Hay-results","///////////////////////////////////////////////////////////");
        mLogger.printLog("Hay-results","El menor EEA es de " + String.format("%.3f",Min_val));
        mLogger.printLog("Hay-results","y corresponde a la "+Minimo);



    }




    public double find_a_4_LN3P(){

        double Fxo=0;
        double Fneg;
        double xo=0.50;
        double neg;
        double retpos, retneg;
        int must = 0;
        double compara;
        double dummy=1000;

        do{
            must = 0;
            neg = -xo;
            //out.println("Evaluando para "+xo + " y " + neg);

            if(xo<DataList.get(DataList.size()-1).getvalue()) {
                Fxo = Math.abs(evaluatefunction(xo));
            } else {
                Fxo = Fxo;
                must = 1;
            }



            Fneg = Math.abs(evaluatefunction(neg));

            //out.println("Fxo: "+Fxo + " y Fneg: " + Fneg);

            retpos=xo;

            retneg=neg;

            xo= xo+0.50;

            if (must==1) {
                compara = 0.0000000001;
            } else {
                compara = Fxo;
            }
            //out.println("Fxo: "+Fxo + " y Fneg: " + Fneg);


        }while(compara>0.00000001||Fneg>0.00000001);

        double retorno = 0;

        if(Fxo<0.00000001){
            retorno = retpos;
        } else {
            if(Fneg<0.00000001)
                retorno = retneg;
        }

        return retorno;
    }


    public double evaluatefunction(double a){
        double Fun;
        //N_data

        //Suma 1/(xi-xo)
        double Suma_0;
        double Az = 0;
        double B=0;
        double C=0;
        double D=0;
        double E=0;

        double muy=0;
        double div = 0;
        //Cálculo de mu_y

        for(int k= 0; k<=(N_data-1); k++){
            double X = List_values.get(k)-a;
            //out.println("Xi - a es: " + String.format("%.2f",X));
            muy = muy + Math.log(X);
            //out.println("A es: " + String.format("%.20f",Az));

        }
        muy = muy /N_data;
        //Cálculo de sigma_y al cuadrado
        double VarLN=0;

        for(int k= 0; k<=(N_data-1); k++){
            double X = List_values.get(k)-a;
            //out.println("Xi - X0 es: " + String.format("%.2f",X));
            B = Math.log(X)-muy;
            VarLN = VarLN + Math.pow(B,2);

        }

        //Cálculo de AZ
        for(int k= 0; k<=(N_data-1); k++){
            double X = List_values.get(k)-a;
            //out.println("Xi - X0 es: " + String.format("%.2f",X));

            Az = Az + 1/X;

        }

        //Cálculo de LogX
        for(int k= 0; k<=(N_data-1); k++){
            double X = List_values.get(k)-a;
            //out.println("Xi - X0 es: " + String.format("%.2f",X));
            D = 1/X;
            C = Math.log(X);
            E = E + D*C;

        }



        VarLN = VarLN/N_data;

        Fun = Az*(muy-VarLN) - E;

        return Fun;
    }



    public double FdealphaGumbel(double alfav){

        if(mLogger==null){
            mLogger = new HikraLogger(getApplicationContext());
        }
        double Fvalue;

        double A=0; //A será el valor de Sum( Xi * exp^(-xi/alpha) )
        double B=0; //B será el valor de sum(Xi-alpha)
        double C=0; //C será el valor de sum( exp^(-xi/alpha) )

        mLogger.printLog("Hay-Fdealpha", "/////////////////////////////////////////////////");
        mLogger.printLog("Hay-Fdealpha", "El valor de xo es: "+String.format("%.3f",alfav));
        double euler = Math.E;
        for(int m = 0; m<=(N_data-1); m++){

            double xi = DataList.get(m).getvalue();
            double D = xi/alfav;
            //xi*e^(-xi/alfa)
            A = A + xi*Math.pow(euler,-D);

            //e^(-xi/alfa)
            C = C + Math.pow(euler,-D);


           // mLogger.printLog("Hay-Fdealpha", "Iteración: "+m);
           // mLogger.printLog("Hay-Fdealpha", "xi: "+String.format("%.3f",xi));
           // mLogger.printLog("Hay-Fdealpha", "xi/alfa: "+String.format("%.3f",D));

            //mLogger.printLog("Hay-Fdealpha", "xi*e^(xi/alfa): "+String.format("%.3f",xi*Math.pow(euler,-D)));
           // mLogger.printLog("Hay-Fdealpha", "(xi-alfa): "+String.format("%.3f",xi-alfav));
            //mLogger.printLog("Hay-Fdealpha", "e^(xi/alfa): "+String.format("%.3f",Math.pow(euler,-D)));
        }



        mLogger.printLog("Hay-Fdealpha", "El valor de A es: "+String.format("%.3f",A));
        B = Media-alfav;
        mLogger.printLog("Hay-Fdealpha", "El valor de C es: "+String.format("%.3f",C));
        Fvalue = A - B*C;

        return Fvalue;

    }



    public double derivFdealphaGumbel(double alfav){
        double derivF;

        double A=0; //A será el valor de Sum( Xi^2 * exp^(-xi/alpha) )
        double B=0; //C será el valor de sum( exp^(-xi/alpha) )
        double C=0; //B será el valor de sum(Xi*exp^(-xi/alpha))


        for(int m = 0; m<=(N_data-1); m++){
            double xi = DataList.get(m).getvalue();
            double D = xi/alfav;
            A = A + xi*xi*Math.exp(-D);
            B = B + Math.exp(-D);
            C = C + xi*Math.exp(-D);

        }

        derivF = (1/(alfav*alfav))*A +B  + (1/alfav)*C;

        return derivF;

    }




    public double find_alpha_Gumbel(){
        if(mLogger==null){
            mLogger = new HikraLogger(getApplicationContext());
        }
        double Fxo=0;
        double DerivF;
        double xo= DataList.get(0).getvalue();
        double dummy;


        double retorno;

        double decision=0;
        do{
            decision = 0;


            //out.println("Evaluando para "+xo + " y " + neg);

            Fxo = FdealphaGumbel(xo);
            DerivF = derivFdealphaGumbel(xo);




            retorno = xo;
            mLogger.printLog("Hay-find_alpha_Gumbel", "El valor de xo es: "+String.format("%.3f",xo) );
            mLogger.printLog("Hay-find_alpha_Gumbel", "El valor de Fxo es: "+String.format("%.9f",Fxo) );
            xo = xo -Fxo/DerivF;


        }while(Fxo>0.00000001);


        mLogger.printLog("Hay-find_alpha_Gumbel", "El valor de alfa será: "+String.format("%.3f",retorno) );
        return retorno;
    }


    public double phibeta(double beta){
        double result;

        result = Math.log(beta)-(1/(2*beta)) - 1/(12*beta*beta) - 1/(120*Math.pow(beta,4)) - 1/(252*Math.pow(beta,6))+1/(240*Math.pow(beta,8))-1/(132*Math.pow(beta,10));

        return result;

    }

    public double calcBeta(double xo){
        double A=0;
        double B=0;
        double Beta;

        for(int k= 0; k<=(N_data-1);k++){
            double C;
            C = DataList.get(k).getvalue()-xo;
            A = A + 1/(C);
            B = B + C;

        }

        double D;

        D = 1/(B*A);
        Beta = 1/(1-N_data*N_data*D);

        return Beta;


    }

    public double calcAlpha(double xo){

        double A=0;
        double B=0;
        double Alpha;

        for(int k= 0; k<=(N_data-1);k++){
            double C;
            C = DataList.get(k).getvalue()-xo;
            A = A + 1/(C);
            B = B + C;

        }
        A = N_data/A;
        B = B/N_data;
        //mLogger.printLog("Hay-CalcAlpha","A: "+String.format("%.3f",A)+" B: "+String.format("%.3f",B));
       // Alpha = ((1/N_data)*B)-(N_data)*(1/A);
        Alpha = B-A;
        return Alpha;
    }

    public void calcGamma3pML(){

        if(mLogger==null){
            mLogger = new HikraLogger(getApplicationContext());
        }

        double Fxo=0;
        double Fneg;
        double xo=0.50;
        double neg;
        double retpos, retneg;
        int must = 0;
        double compara;
        double dummy=1000;
        double decision=0;
        double Alphapos;
        double Alphaneg;
        double Betapos;
        double Betaneg;
        double phibetapos;
        double phibetaneg;


        ///Evaluar si hay un cambio de signo al tomar x0 ligeramente mayor al mínimo valor registrado
        /// y al tomar x0 igual al negativo del máximo valor registrado.

        //Si hay un cambio de signo, el valor está entre esos dos. De lo contrario será mayor al máximo registrado.

        double minV;
        double dummyV;

        int inicio=0; //Posición del valor en la lista a donde se da el cambio de signo;
        int fin=0; //Posición del valor inicio +1 en donde se da el cambio de signo;

        double AlphaLP, BetaLP, phiBetaLP;
        double AlphaLN, BetaLN, phiBetaLN;

        double XLP = DataList.get(N_data-1).getvalue()-1;
        double XLN = -1*DataList.get(0).getvalue();
        AlphaLP = calcAlpha(XLP);
        BetaLP = calcBeta(XLP);
        phiBetaLP = phibeta(BetaLP);

        AlphaLN = calcAlpha(XLN);
        BetaLN = calcBeta(XLN);
        phiBetaLN = phibeta(BetaLN);


        double Sum1=0;
        double Sum2 = 0;
        for(int j= 0; j<=(N_data-1);j++){
            double C = DataList.get(j).getvalue()-XLP;
            double D = DataList.get(j).getvalue()-XLN;
            Sum1 = Sum1+Math.log(C);
            Sum2 = Sum2+Math.log(D);
            }

        double FXlp = -(N_data*phiBetaLP)+Sum1-(N_data*Math.log(AlphaLP));
        double V1, V2, V3;
        V1 = -N_data*phiBetaLP;
        V2 = Sum1;
        V3 = -N_data*Math.log(AlphaLP);
        double FXln = -(N_data*phiBetaLN)+Sum2-(N_data*Math.log(AlphaLN));
        double Y1, Y2, Y3;
        Y1 = -N_data*phiBetaLN;
        Y2 = Sum2;
        Y3 = -N_data*Math.log(AlphaLN);


        mLogger.printLog("Hay-ML_G3P","El valor de Xlp es "+String.format("%.3f",XLP));
        mLogger.printLog("Hay-ML_G3P","El valor de Alphalp es "+String.format("%.3f",AlphaLP));
        mLogger.printLog("Hay-ML_G3P","El valor de Betalp es "+String.format("%.3f",BetaLP));
        mLogger.printLog("Hay-ML_G3P","El valor de Philp es "+String.format("%.3f",phiBetaLP));
        mLogger.printLog("Hay-ML_G3P","V1: "+String.format("%.3f",V1)+" V2: "+String.format("%.3f",V2)+" V3: "+String.format("%.3f",V3));
        mLogger.printLog("Hay-ML_G3P","El valor de Fxlp es "+String.format("%.3f",FXlp));


        mLogger.printLog("Hay-ML_G3P","El valor de Xln es "+String.format("%.3f",XLN));
        mLogger.printLog("Hay-ML_G3P","El valor de Alphaln es "+String.format("%.3f",AlphaLN));
        mLogger.printLog("Hay-ML_G3P","El valor de Betaln es "+String.format("%.3f",BetaLN));
        mLogger.printLog("Hay-ML_G3P","El valor de Philn es "+String.format("%.3f",phiBetaLN));
        mLogger.printLog("Hay-ML_G3P","Y1: "+String.format("%.3f",Y1)+" Y2: "+String.format("%.3f",Y2)+" Y3: "+String.format("%.3f",Y3));
        mLogger.printLog("Hay-ML_G3P","El valor de Fxln es "+String.format("%.3f",FXln));

        if(FXlp>0 && FXln<0){
            mLogger.printLog("Hay-ML_G3P","Evaluando dentro de los valores registrados");
            mLogger.printLog("Hay-ML_G3P","Fxlp positivo y Fxln negativo. Se buscan los valores i e i+1 donde se da el cambio de signo.");
            //El valor de Xo está entre los valores existentes de la lista.
            //Buscar los dos valores, Xi y Xi+1 en donde se da el cambio de signo.

            int td = N_data-1;

            mLogger.printLog("Hay-ML_G3P","Hay "+String.valueOf(N_data)+" valores cargados");

            for(int p = td; p>=1; p--){

                if(p==td){
                    mLogger.printLog("Hay-ML_G3P","/////////////////////////////////////////////////////");
                    mLogger.printLog("Hay-ML_G3P","Iteración: "+String.valueOf(p));
                    double Alphaj, Betaj, phiBetaj;//Variables asociadas al valor j de la lista.
                    double Alpham, Betam, phiBetam;//Variables asociadas al valor j-1 de la lista.

                    double Xj = DataList.get(p).getvalue()-1;
                    double Xm = -1*DataList.get(p-1).getvalue();

                    Alphaj = calcAlpha(Xj);
                    Betaj = calcBeta(Xj);
                    phiBetaj = phibeta(Betaj);

                    Alpham = calcAlpha(Xm);
                    Betam = calcBeta(Xm);
                    phiBetam = phibeta(Betam);


                    double Sumj=0;
                    double Summ = 0;

                    for(int q= 0; q<=(N_data-1);q++){
                        double C = DataList.get(q).getvalue()-Xj;
                        double D = DataList.get(q).getvalue()-Xm;
                        Sumj = Sumj+Math.log(C);
                        Summ = Summ+Math.log(D);
                    }

                    double FXj = -(N_data*phiBetaj)+Sumj-(N_data*Math.log(Alphaj));
                    //double V1, V2, V3;
                    V1 = -N_data*phiBetaj;
                    V2 = Sumj;
                    V3 = -N_data*Math.log(Alphaj);

                    double FXm = -(N_data*phiBetam)+Summ-(N_data*Math.log(Alpham));
                    //double Y1, Y2, Y3;
                    Y1 = -N_data*phiBetam;
                    Y2 = Summ;
                    Y3 = -N_data*Math.log(Alpham);

                    mLogger.printLog("Hay-ML_G3P","Xj: "+String.valueOf(Xj));
                    mLogger.printLog("Hay-ML_G3P","El valor de Alphaj es "+String.format("%.3f",Alphaj));
                    mLogger.printLog("Hay-ML_G3P","El valor de Betaj es "+String.format("%.3f",Betaj));
                    mLogger.printLog("Hay-ML_G3P","El valor de Phij es "+String.format("%.3f",phiBetaj));
                    mLogger.printLog("Hay-ML_G3P","V1: "+String.format("%.3f",V1)+" V2: "+String.format("%.3f",V2)+" V3: "+String.format("%.3f",V3));
                    mLogger.printLog("Hay-ML_G3P","El valor de Fxj es "+String.format("%.3f",FXj));


                    mLogger.printLog("Hay-ML_G3P","Xm: "+String.valueOf(Xm));
                    mLogger.printLog("Hay-ML_G3P","El valor de Alpham es "+String.format("%.3f",Alpham));
                    mLogger.printLog("Hay-ML_G3P","El valor de Betam es "+String.format("%.3f",Betam));
                    mLogger.printLog("Hay-ML_G3P","El valor de Phim es "+String.format("%.3f",phiBetam));
                    mLogger.printLog("Hay-ML_G3P","Y1: "+String.format("%.3f",Y1)+" Y2: "+String.format("%.3f",Y2)+" Y3: "+String.format("%.3f",Y3));
                    mLogger.printLog("Hay-ML_G3P","El valor de Fxm es "+String.format("%.3f",FXm));

                    if(FXj>0&&FXm>0){
                        //NADA
                    } else {
                        if(FXj>0&&FXm<0){
                            mLogger.printLog("Hay-ML_G3P", "Hay un cambio de signo, se detiene el ciclo for");
                            //inicio = p-1;
                            //fin = p;

                            inicio = p;
                            fin = p-1;
                            break;

                        }
                    }


                } else {

                    mLogger.printLog("Hay-ML_G3P","/////////////////////////////////////////////////////");
                    mLogger.printLog("Hay-ML_G3P","Iteración: "+String.valueOf(p));
                    double Alphaj, Betaj, phiBetaj;//Variables asociadas al valor j de la lista.
                    double Alpham, Betam, phiBetam;//Variables asociadas al valor j-1 de la lista.

                    double Xj = -1*DataList.get(p).getvalue();
                    double Xm = -1*DataList.get(p-1).getvalue();
                    Alphaj = calcAlpha(Xj);
                    Betaj = calcBeta(Xj);
                    phiBetaj = phibeta(Betaj);

                    Alpham = calcAlpha(Xm);
                    Betam = calcBeta(Xm);
                    phiBetam = phibeta(Betam);


                    double Sumj=0;
                    double Summ = 0;

                    for(int q= 0; q<=(N_data-1);q++){
                        double C = DataList.get(q).getvalue()-Xj;
                        double D = DataList.get(q).getvalue()-Xm;
                        Sumj = Sumj+Math.log(C);
                        Summ = Summ+Math.log(D);
                    }

                    double FXj = -(N_data*phiBetaj)+Sumj-(N_data*Math.log(Alphaj));
                    //double V1, V2, V3;
                    V1 = -N_data*phiBetaj;
                    V2 = Sumj;
                    V3 = -N_data*Math.log(Alphaj);
                    double FXm = -(N_data*phiBetam)+Summ-(N_data*Math.log(Alpham));
                    //double Y1, Y2, Y3;
                    Y1 = -N_data*phiBetam;
                    Y2 = Summ;
                    Y3 = -N_data*Math.log(Alpham);

                    mLogger.printLog("Hay-ML_G3P","Xj: "+String.valueOf(Xj));
                    mLogger.printLog("Hay-ML_G3P","El valor de Alphaj es "+String.format("%.3f",Alphaj));
                    mLogger.printLog("Hay-ML_G3P","El valor de Betaj es "+String.format("%.3f",Betaj));
                    mLogger.printLog("Hay-ML_G3P","El valor de Phij es "+String.format("%.3f",phiBetaj));
                    mLogger.printLog("Hay-ML_G3P","V1: "+String.format("%.3f",V1)+" V2: "+String.format("%.3f",V2)+" V3: "+String.format("%.3f",V3));
                    mLogger.printLog("Hay-ML_G3P","El valor de Fxj es "+String.format("%.3f",FXj));


                    mLogger.printLog("Hay-ML_G3P","Xm: "+String.valueOf(Xm));
                    mLogger.printLog("Hay-ML_G3P","El valor de Alpham es "+String.format("%.3f",Alpham));
                    mLogger.printLog("Hay-ML_G3P","El valor de Betam es "+String.format("%.3f",Betam));
                    mLogger.printLog("Hay-ML_G3P","El valor de Phim es "+String.format("%.3f",phiBetam));
                    mLogger.printLog("Hay-ML_G3P","Y1: "+String.format("%.3f",Y1)+" Y2: "+String.format("%.3f",Y2)+" Y3: "+String.format("%.3f",Y3));
                    mLogger.printLog("Hay-ML_G3P","El valor de Fxm es "+String.format("%.3f",FXm));


                    if(FXj>0&&FXm>0){
                        //NADA
                    } else {
                        if(FXj>0&&FXm<0){
                            mLogger.printLog("Hay-ML_G3P", "Hay un cambio de signo, se detiene el ciclo for");
                            inicio = p-1;
                            fin = p;
                            break;

                        }
                    }

                }
           }
            //Fuera del ciclo for


            double end;
            double start;
            if(inicio==(N_data-1)){
                start = (DataList.get(inicio).getvalue()-0.01);
                end = -1*DataList.get(fin).getvalue();
            } else {
                start = -1* DataList.get(fin).getvalue();
                end = -1*DataList.get(inicio).getvalue();
            }





            mLogger.printLog("Hay-ML_G3P", "El Valor donde se inicia a iterar para obtener Xo es: "+String.format("%.3f",start));
            mLogger.printLog("Hay-ML_G3P", "El Valor donde termina la iteración para obtener Xo es: "+String.format("%.3f",end));

            double diferencia = Math.abs(end-start);
            double delta;
            int limite;
            if(diferencia>1000){
                delta = diferencia /1000;
                limite = 1000;
            } else {
                if(diferencia>100){

                    delta = diferencia/800;
                    limite = 800;
                } else {
                    if(diferencia>10){
                        delta = diferencia/150;
                        limite = 150;
                    } else {
                        if(diferencia>1){
                            delta = diferencia/20;
                            limite = 20;
                        } else {
                            delta = diferencia/10;
                            limite = 10;
                        }

                    }
                }
            }

            mLogger.printLog("Hay-ML_G3P","Delta vale:" +String.format("%.3f",delta));



            if(inicio==(N_data-1)){
                mLogger.printLog("Hay-ML_G3P","El valor de inicio para obtener Xo es el menor de la lista.");

                double X2 = start;

                for(int m1=0; m1<=limite; m1++){

                    mLogger.printLog("Hay-ML_G3P","/////////////////////////////////////////////////////");
                    mLogger.printLog("Hay-ML_G3P","Iteración: "+String.valueOf(m1));
                    double Alpham1, Betam1, phiBetam1;//Variables asociadas al valor m1 de la lista.




                    Alpham1 = calcAlpha(X2);
                    Betam1 = calcBeta(X2);
                    phiBetam1 = phibeta(Betam1);

                    double Summ1=0;


                    for(int q= 0; q<=(N_data-1);q++){
                        double C = DataList.get(q).getvalue()-X2;

                        Summ1 = Summ1+Math.log(C);

                    }

                    double FXm1 = -(N_data*phiBetam1)+Summ1-(N_data*Math.log(Alpham1));
                    //double V1, V2, V3;
                    V1 = -N_data*phiBetam1;
                    V2 = Summ1;
                    V3 = -N_data*Math.log(Alpham1);



                    mLogger.printLog("Hay-ML_G3P","X0: "+String.valueOf(X2));
                    mLogger.printLog("Hay-ML_G3P","El valor de Alpha0 es "+String.format("%.3f",Alpham1));
                    mLogger.printLog("Hay-ML_G3P","El valor de Beta0 es "+String.format("%.3f",Betam1));
                    mLogger.printLog("Hay-ML_G3P","El valor de Phi0 es "+String.format("%.3f",phiBetam1));
                    mLogger.printLog("Hay-ML_G3P","V1: "+String.format("%.3f",V1)+" V2: "+String.format("%.3f",V2)+" V3: "+String.format("%.3f",V3));
                    mLogger.printLog("Hay-ML_G3P","El valor de Fx0 es "+String.format("%.3f",FXm1));


                    if(Math.abs(FXm1)<0.025){
                        mLogger.printLog("Hay-ML_G3P","Se ha terminado el proceso. X0=  "+String.format("%.3f",X2));
                        ML_alpha_Gamma3p = Alpham1;
                        ML_beta_Gamma3p = Betam1;

                        ML_g_Gamma3p = X2;

                        break;
                    } else {
                        X2 = X2-delta;

                    }
                }



            } else {
                mLogger.printLog("Hay-ML_G3P","El valor de inicio para obtener Xo NO es el menor de la lista.");
                double X1= -start;
                double X2;
                for(int m1=0; m1<=limite; m1++){
                    X2 = -X1;
                    mLogger.printLog("Hay-ML_G3P","/////////////////////////////////////////////////////");
                    mLogger.printLog("Hay-ML_G3P","Iteración: "+String.valueOf(m1));
                    double Alpham1, Betam1, phiBetam1;//Variables asociadas al valor m1 de la lista.




                    Alpham1 = calcAlpha(X2);
                    Betam1 = calcBeta(X2);
                    phiBetam1 = phibeta(Betam1);

                    double Summ1=0;


                    for(int q= 0; q<=(N_data-1);q++){
                        double C = DataList.get(q).getvalue()-X2;

                        Summ1 = Summ1+Math.log(C);

                    }

                    double FXm1 = -(N_data*phiBetam1)+Summ1-(N_data*Math.log(Alpham1));
                    //double V1, V2, V3;
                    V1 = -N_data*phiBetam1;
                    V2 = Summ1;
                    V3 = -N_data*Math.log(Alpham1);



                    mLogger.printLog("Hay-ML_G3P","X0: "+String.valueOf(X2));
                    mLogger.printLog("Hay-ML_G3P","El valor de Alpha0 es "+String.format("%.3f",Alpham1));
                    mLogger.printLog("Hay-ML_G3P","El valor de Beta0 es "+String.format("%.3f",Betam1));
                    mLogger.printLog("Hay-ML_G3P","El valor de Phi0 es "+String.format("%.3f",phiBetam1));
                    mLogger.printLog("Hay-ML_G3P","V1: "+String.format("%.3f",V1)+" V2: "+String.format("%.3f",V2)+" V3: "+String.format("%.3f",V3));
                    mLogger.printLog("Hay-ML_G3P","El valor de Fx0 es "+String.format("%.3f",FXm1));


                    if(Math.abs(FXm1)<0.0001){
                        mLogger.printLog("Hay-ML_G3P","Se ha terminado el proceso. X0=  "+String.format("%.3f",X2));
                        ML_alpha_Gamma3p = Alpham1;
                        ML_beta_Gamma3p = Betam1;

                        ML_g_Gamma3p = X2;

                        break;
                    } else {
                        X1 = X1+delta;

                    }
                }


            }







        } else {
            mLogger.printLog("Hay-ML_G3P","El valor de x0 se encuentra fuera del límite superior de valores registrados");
            if(FXlp>0&&FXln>0){
                //El valor de Xo es mayor al límite superior.
                mLogger.printLog("Hay-ML_G3P","El valor de x0 se encuentra fuera del límite superior de valores registrados");

                mLogger.printLog("Hay-ML_G3P","Fxlp positivo y Fxln positivo. Se buscan los valores i e i+1 donde se da el cambio de signo.");
            } else {

                if(FXlp<0&&FXln<0) {
                    mLogger.printLog("Hay-ML_G3P","El valor de x0 es menor al negativo del máximo. Caso atípico.");

                    double delta = (DataList.get(0).getvalue())/2000;

                    double X2 = -1*DataList.get(0).getvalue();

                    double FXm1;

                    do{

                        double Alpham1, Betam1, phiBetam1;//Variables asociadas al valor m1 de la lista.




                        Alpham1 = calcAlpha(X2);
                        Betam1 = calcBeta(X2);
                        phiBetam1 = phibeta(Betam1);

                        double Summ1=0;


                        for(int q= 0; q<=(N_data-1);q++){
                            double C = DataList.get(q).getvalue()-X2;

                            Summ1 = Summ1+Math.log(C);

                        }

                        FXm1 = -(N_data*phiBetam1)+Summ1-(N_data*Math.log(Alpham1));
                        //double V1, V2, V3;
                        V1 = -N_data*phiBetam1;
                        V2 = Summ1;
                        V3 = -N_data*Math.log(Alpham1);



                        mLogger.printLog("Hay-ML_G3P","X0: "+String.valueOf(X2));
                        mLogger.printLog("Hay-ML_G3P","El valor de Alpha0 es "+String.format("%.3f",Alpham1));
                        mLogger.printLog("Hay-ML_G3P","El valor de Beta0 es "+String.format("%.3f",Betam1));
                        mLogger.printLog("Hay-ML_G3P","El valor de Phi0 es "+String.format("%.3f",phiBetam1));
                        mLogger.printLog("Hay-ML_G3P","V1: "+String.format("%.3f",V1)+" V2: "+String.format("%.3f",V2)+" V3: "+String.format("%.3f",V3));
                        mLogger.printLog("Hay-ML_G3P","El valor de Fx0 es "+String.format("%.3f",FXm1));


                        if(Math.abs(FXm1)<0.001){
                            mLogger.printLog("Hay-ML_G3P","Se ha terminado el proceso. X0=  "+String.format("%.3f",X2));
                            ML_alpha_Gamma3p = Alpham1;
                            ML_beta_Gamma3p = Betam1;

                            ML_g_Gamma3p = X2;

                        } else {
                            X2 = X2-delta;
                            mLogger.printLog("Hay-ML_G3P","El nuevo valor para iterar X0=  "+String.format("%.3f",X2));
                        }

                    }while(Math.abs(FXm1)>0.001);



                }



            }

        }

    }



    public double getMOM_NError(){
        return EEA_MOM_N;

    }

    public double getMOM_LN2PError(){
        return EEA_MOM_LN2P;
    }

    public double getML_LN2PError(){
        return EEA_ML_LN2P;
    }

    public double getMOM_LN3PError(){
        return EEA_MOM_LN3P;

    }

    public double getML_LN3PError(){
        return EEA_ML_LN3P;

    }

    public double getMOM_GumbelError(){
        return EEA_MOM_Gumbel;

    }

    public double getML_GumbelError(){
        return EEA_ML_Gumbel;

    }

    public double getMOM_EXPError(){
        return EEA_MOM_EXP;

    }

    public double getML_EXPError(){
        return EEA_ML_EXP;

    }

    public double getMOM_G2PError(){
        return EEA_MOM_G2p;

    }

    public double getML_G2PError(){
        return EEA_ML_G2p;

    }

    public double getMOM_G3PError(){
        return EEA_MOM_G3p;

    }

    public double getML_G3PError(){
        return EEA_ML_G3p;

    }

    public double getMinVal(){

        return Min_val;
    }

    public String getminfdp(){

        return Minimo;
    }

    public int getPosmin(){

        return posmin;
    }

    public void setPosmin(int a){
        posmin = a;
    }

    public double getMean(){
        return Media;
    }

    public double getDesv_std(){
        return Desv_std;

    }

    public double getskewness(){

        return skewness;
    }


    public double getMOM_sigma_LN2P(){

        return MOM_sigma_LN2P;
    }

    public double getMOM_mu_LN2P(){

        return MOM_mu_LN2P;
    }

    public double getML_sigma_LN2P(){
        return ML_sigma_LN2P;
    }

    public double getML_mu_LN2P(){
        return ML_mu_LN2P;
    }


    public double getMOM_mu_LN3P(){

        return MOM_mu_LN3P;

    }

    public double getMOM_sigma_LN3P(){
        return MOM_sigma_LN3P;

    }

    public double getMOM_a_LN3P(){

        return MOM_a_LN3P;
    }

    public double getML_mu_LN3P(){
        return ML_mu_LN3P;
    }

    public double getML_sigma_LN3P(){
        return ML_sigma_LN3P;
    }

    public double getML_a_LN3P(){

        return ML_a_LN3P;
    }

    public double getMOM_alpha_Gumbel(){

        return MOM_alpha_Gumbel;
    }

    public double getMOM_beta_Gumbel(){
        return MOM_beta_Gumbel;

    }

    public double getML_alpha_Gumbel(){
        return ML_alpha_Gumbel;
    }


    public double getML_beta_Gumbel(){
        return ML_beta_Gumbel;
    }

    public double getMOM_alpha_EXP(){

        return MOM_alpha_EXP;
    }

    public double getMOM_epsilon_EXP(){
        return MOM_epsilon_EXP;
    }

    public double getML_alpha_EXP(){
        return ML_alpha_EXP;
    }

    public double getML_epsilon_EXP(){
        return ML_epsilon_EXP;
    }



    public double getMOM_alpha_Gamma2p(){
        return MOM_alpha_Gamma2p;
    }

    public double getMOM_beta_Gamma2p(){

        return MOM_beta_Gamma2p;
    }

    public double getML_alpha_Gamma2p(){
        return ML_alpha_Gamma2p;

    }

    public double getML_beta_Gamma2p(){
        return ML_beta_Gamma2p;
    }


    public double getMOM_alpha_Gamma3p(){
        return MOM_alpha_Gamma3p;
    }
    public double getMOM_beta_Gamma3p(){
        return MOM_beta_Gamma3p;
    }

    public double getMOM_g_Gamma3p(){
        return MOM_g_Gamma3p;
    }


    public double getML_alpha_Gamma3p(){
        return ML_alpha_Gamma3p;
    }

    public double getML_beta_Gamma3p(){
        return ML_beta_Gamma3p;
    }

    public double getML_g_Gamma3p(){
        return ML_g_Gamma3p;
    }


    public String getnamefdp(){
        String name="";

        name = nombrefdp.get(posmin);
        return name;
    }

    public int getNdata(){
        return N_data;
    }

    public void fillfitted(){


        if(mLogger==null){
            mLogger = new HikraLogger(getApplicationContext());
        }

        mLogger.printLog("fillfitted", "Starts filling of projected values");
        int NP = ProjectedValues.size();
        switch(posmin){

            case 0:        //0 - EEA de la distribución normal, MOM.
                for(int k=0; k<=(N_data-1); k++){
                    double valorx = DataList.get(k).getMOM_Nvalue();
                    DataList.get(k).setFitted(valorx);
                    DataList.get(k).setFittedE(Math.pow(DataList.get(k).getvalue()-valorx,2));
                }


                for(int k=0; k<=(NP-1); k++){
                    double valorx = ProjectedValues.get(k).getMOM_Nvalue();
                    ProjectedValues.get(k).setFitted(valorx);
                 }


                break;
            case 1:        //1 EEA de la distribución LN, MOM con dos parámetros.
                for(int k=0; k<=(N_data-1); k++){
                    double valorx = DataList.get(k).getMOM_LNvalue();
                    DataList.get(k).setFitted(valorx);
                    DataList.get(k).setFittedE(Math.pow(DataList.get(k).getvalue()-valorx,2));
                }

                for(int k=0; k<=(NP-1); k++){
                    double valorx = ProjectedValues.get(k).getMOM_LNvalue();
                    ProjectedValues.get(k).setFitted(valorx);

                }

                break;
            case 2:        //2 EEA de la distribución LN 3P, MOM
                for(int k=0; k<=(N_data-1); k++){
                    double valorx = DataList.get(k).getMOM_LN3pV();
                    DataList.get(k).setFitted(valorx);
                    DataList.get(k).setFittedE(Math.pow(DataList.get(k).getvalue()-valorx,2));
                }

                for(int k=0; k<=(NP-1); k++){
                    double valorx = ProjectedValues.get(k).getMOM_LN3pV();
                    ProjectedValues.get(k).setFitted(valorx);

                }


                break;
            case 3:        //3 EEA de la distribución Gumbel MOM
                for(int k=0; k<=(N_data-1); k++){
                    double valorx = DataList.get(k).getMOM_GumbelV();
                    DataList.get(k).setFitted(valorx);
                    DataList.get(k).setFittedE(Math.pow(DataList.get(k).getvalue()-valorx,2));
                }
                for(int k=0; k<=(NP-1); k++){
                    double valorx = ProjectedValues.get(k).getMOM_GumbelV();
                    ProjectedValues.get(k).setFitted(valorx);

                }


                break;
            case 4:        //4 EEA de la distribución Exponencial MOM
                for(int k=0; k<=(N_data-1); k++){
                    double valorx = DataList.get(k).getMOMExpV();
                    DataList.get(k).setFitted(valorx);
                    DataList.get(k).setFittedE(Math.pow(DataList.get(k).getvalue()-valorx,2));
                }

                for(int k=0; k<=(NP-1); k++){
                    double valorx = ProjectedValues.get(k).getMOMExpV();
                    ProjectedValues.get(k).setFitted(valorx);

                }

                break;
            case 5:        //5 EEA de la distribución Gamma de dos parámetros. MOM
                for(int k=0; k<=(N_data-1); k++){
                    double valorx = DataList.get(k).getMOM_G2pV();
                    DataList.get(k).setFitted(valorx);
                    DataList.get(k).setFittedE(Math.pow(DataList.get(k).getvalue()-valorx,2));
                }
                for(int k=0; k<=(NP-1); k++){
                    double valorx = ProjectedValues.get(k).getMOM_G2pV();
                    ProjectedValues.get(k).setFitted(valorx);

                }

                break;
            case 6:        //6 EEA de la distribución Gamma de tres parámetros.MOM
                for(int k=0; k<=(N_data-1); k++){
                    double valorx = DataList.get(k).getMOM_G3pV();
                    DataList.get(k).setFitted(valorx);
                    DataList.get(k).setFittedE(Math.pow(DataList.get(k).getvalue()-valorx,2));
                }
                for(int k=0; k<=(NP-1); k++){
                    double valorx = ProjectedValues.get(k).getMOM_G3pV();
                    ProjectedValues.get(k).setFitted(valorx);

                }

                break;

            /***
             *
             * INICIAN LOS CASOS CON EL MÉTODO DE MÁXIMA VEROSIMILITUD
             *
             *
             */
            case 7:             //7 EEA de la distribución Log Normal ML con dos parámetros.
                for(int k=0; k<=(N_data-1); k++){
                    double valorx = DataList.get(k).getML_LNvalue();
                    DataList.get(k).setFitted(valorx);
                    DataList.get(k).setFittedE(Math.pow(DataList.get(k).getvalue()-valorx,2));
                }

                for(int k=0; k<=(NP-1); k++){
                    double valorx = ProjectedValues.get(k).getML_LNvalue();
                    ProjectedValues.get(k).setFitted(valorx);

                }

                break;
            case 8:        //8 EEA de la distribución LogNormal con tres parámetros.
                for(int k=0; k<=(N_data-1); k++){
                    double valorx = DataList.get(k).getML_LN3pV();
                    DataList.get(k).setFitted(valorx);
                    DataList.get(k).setFittedE(Math.pow(DataList.get(k).getvalue()-valorx,2));
                }

                for(int k=0; k<=(NP-1); k++){
                    double valorx = ProjectedValues.get(k).getML_LN3pV();
                    ProjectedValues.get(k).setFitted(valorx);

                }

                break;
            case 9:         //9 EEA de la distribución Gumbel ML
                for(int k=0; k<=(N_data-1); k++){
                    double valorx = DataList.get(k).getML_Gumbel();
                    DataList.get(k).setFitted(valorx);
                    DataList.get(k).setFittedE(Math.pow(DataList.get(k).getvalue()-valorx,2));
                }

                for(int k=0; k<=(NP-1); k++){
                    double valorx = ProjectedValues.get(k).getML_Gumbel();
                    ProjectedValues.get(k).setFitted(valorx);

                }

                break;
            case 10:         //10 EEA de la distribución exponencial ML
                for(int k=0; k<=(N_data-1); k++){
                    double valorx = DataList.get(k).getMLExpV();
                    DataList.get(k).setFitted(valorx);
                    DataList.get(k).setFittedE(Math.pow(DataList.get(k).getvalue()-valorx,2));
                }

                for(int k=0; k<=(NP-1); k++){
                    double valorx = ProjectedValues.get(k).getMLExpV();
                    ProjectedValues.get(k).setFitted(valorx);

                }

                break;
            case 11:         //11 EEA de la distribución Gamma con dos parámetros. ML
                for(int k=0; k<=(N_data-1); k++){
                    double valorx = DataList.get(k).getML_G2pV();
                    DataList.get(k).setFitted(valorx);
                    DataList.get(k).setFittedE(Math.pow(DataList.get(k).getvalue()-valorx,2));
                }

                for(int k=0; k<=(NP-1); k++){
                    double valorx = ProjectedValues.get(k).getML_G2pV();
                    ProjectedValues.get(k).setFitted(valorx);

                }


                break;
            case 12:         //12 EEA de la distribución Gamma con tres parámetros.ML
                for(int k=0; k<=(N_data-1); k++){
                    double valorx = DataList.get(k).getML_G3pV();
                    DataList.get(k).setFitted(valorx);
                    DataList.get(k).setFittedE(Math.pow(DataList.get(k).getvalue()-valorx,2));
                }

                for(int k=0; k<=(NP-1); k++){
                    double valorx = ProjectedValues.get(k).getML_G3pV();
                    ProjectedValues.get(k).setFitted(valorx);

                }


                break;


        }

        Avance = 15;
    }

    public double getTi(int pos){
        double valory;
        valory = DataList.get(pos).getT();
        return valory;

    }

    public double getVi(int pos){
        double valory1;
        valory1 = DataList.get(pos).getvalue();
        return valory1;

    }

    public double getFiti(int pos){
        double valory2;
        valory2 = DataList.get(pos).getFittedV();
        return valory2;

    }

    public double getEi(int pos){
        double valory3;
        valory3 = DataList.get(pos).getFittedE();
        return valory3;

    }



    public String getRawTable(){
        rawdata = "";
        String header="";
        String foot="";
        String contents= "";
        int Size = DataList.size();
        header = "<TABLE border=\"1\" align=\"center\">" +
                "<TR> " +
                "<TD >Year</TD>" +
                "<TD align=\"center\">Value</TD>"+
                "</TR>";
        foot =  "</TABLE>";


        if(Size==0){
            rawdata = header + foot;

        } else {



            for (int k=0; k<=(Size-1);k++) {
                String dummy = "<TR> " +
                        "<TD>"+DataList.get(k).getyear() +"</TD>" +
                        "<TD>"+String.format("%.2f",DataList.get(k).getvalue()) +"</TD>" +
                        "</TR>";
                contents = contents+dummy;
            }


            rawdata = header +  contents + foot;

            //return rawdata;
        }

        return rawdata;
    }


    public String getAddedTable(){
        rawdata = "";
        String header="";
        String foot="";
        String contents= "";
        int Size = DataList.size();
        header = "<TABLE border=\"1\" align=\"center\">" +
                "<TR> " +
                "<TD align=\"center\">ID</TD>"+
                "<TD align=\"center\">Year</TD>" +
                "<TD align=\"center\">Value</TD>"+
                "</TR>";
        foot =  "</TABLE>";


        if(Size==0){
            rawdata = header + foot;

        } else {



            for (int k=0; k<=(Size-1);k++) {
                String dummy = "<TR> " +
                        "<TD>"+ (k+1) +"</TD>" +
                        "<TD>"+DataList.get(k).getyear() +"</TD>" +
                        "<TD>"+String.format("%.2f",DataList.get(k).getvalue()) +"</TD>" +
                        "</TR>";
                contents = contents+dummy;
            }


            rawdata = header +  contents + foot;

            //return rawdata;
        }

        return rawdata;
    }


    /*
public void addData2(){
out.println("Botón para agregar datos.");

}*/


    /**
     *
     *
     *
     * AQUI INICIAN LAS FUNCIONES PARA OBTENER LOS PARÁMETROS ESTADÍSTICOS DE LAS FDP.
     *
     *
     *
     *
     *
     */

    public void Basicos(){

        if(mLogger==null){
            mLogger = new HikraLogger(getApplicationContext());
        }

        int size = DataList.size();
        N_data = DataList.size();
        for(int n=0; n<=(size-1);n++){
            double valorx = DataList.get(n).getvalue();
            List_values.add(valorx);
        }


        int max = List_values.size();
        double SumaX=0;
        double SumaM=0;

        mLogger.printLog("Hay-Basicos","Cantidad de datos: "+max);

        //Suma de valores para cálculo de la media. SumaX/Ndatos
        for(int i=0; i<=(max-1);i++){
            SumaX = SumaX + List_values.get(i);

        }
        mLogger.printLog("Hay-Basicos","SumaX: "+String.format("%.3f",SumaX));

        double[] value = new double[size];


        for(int i=0; i<=(size-1); i++){

            if (i==0) {


            }
            //Se crea un array de valores tipo double para calcular la media geometrica.
            value[i] = DataList.get(i).getvalue();
            mLogger.printLog("Hay-Basicos","The value[" + i +"] is: " + String.valueOf(value[i]));

        }

        DescriptiveStatistics estadistica = new DescriptiveStatistics(value);
        mLogger.printLog("Hay-Basicos", "La suma de todos los valores es:" + String.valueOf(SumaX));
        Media = SumaX/max;

        GMed = estadistica.getGeometricMean();

        mLogger.printLog("Hay-Basicos","Media geométrica: "+String.format("%.3f",GMed));

        //Cálculo de los momentos m2 y m3..

        double Sum_m2, Sum_m3;
        Sum_m2 = 0;
        Sum_m3 = 0;
        for(int j=0; j<=(max-1);j++){
            double valx = (List_values.get(j)-Media);
            Sum_m2 = Sum_m2 + Math.pow(valx,2);
            Sum_m3  = Sum_m3 + Math.pow(valx,3);

            mLogger.printLog("Hay-Basicos","valor i: "+List_values.get(j)+", Media: "+ String.format("%.3f",Media));
        }

        mLogger.printLog("Hay-Basicos","Sum_m2: "+String.format("%.3f",Sum_m2));
        mLogger.printLog("Hay-Basicos","Sum_m3: "+String.format("%.3f",Sum_m3));

        Var = Sum_m2/(N_data-1);
        Mm2 = Sum_m2/(N_data-1);

        double A = (N_data-1);
        double B = (N_data-2);
        Desv_std = Math.pow(Var,0.5);
        Mm3 = (Sum_m3 *N_data) /(A*B);

        mLogger.printLog("Hay-Basicos","AxB: "+ String.format("%.3f",A*B));

        double C = N_data*Sum_m3;
        double Y1 = (double) 3/ (double)2;

        double D = Math.pow(Mm2,Y1);


        skewness = (Mm3)/D;
        mLogger.printLog("Hay-Basicos","N*Sum_m3: "+ String.format("%.3f",C));
        mLogger.printLog("Hay-Basicos","Y1: "+ String.format("%.3f",Y1));
        mLogger.printLog("Hay-Basicos","m2 ^ (3/2): "+ String.format("%.3f",D));

        mLogger.printLog("Hay-Basicos","Media: "+ String.format("%.3f",Media)+" Desv_std. "+String.format("%.3f",Desv_std));

        mLogger.printLog("Hay-Basicos","m2: "+ String.format("%.3f",Mm2)+", m3: "+String.format("%.3f",Mm3));

        mLogger.printLog("Hay-Basicos", "Coeficiente de asimetría: "+String.format("%.3f",skewness));

    }




    public void CalcNormal(){

        if(mLogger==null){
            mLogger = new HikraLogger(getApplicationContext());
        }

        /////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////
        //Ajuste para la distribución normal
        /////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////

        MOM_mu = 1*Media;

        double SumaN=0;
        mLogger.printLog("Hay-Normal","La media para cálculo de sigma es: "+String.format("%.3f",MOM_mu));
        for(int n=0; n<=(N_data-1);n++){
            double valorN = DataList.get(n).getvalue();
            SumaN = SumaN + Math.pow(valorN-MOM_mu,2);
        }
        mLogger.printLog("Hay-Normal","Suma de la diferencia xi-media para cálculo de sigma es: "+String.format("%.3f",SumaN));

        MOM_sigma = Math.pow(SumaN/(N_data-1),0.5);
        mLogger.printLog("Hay-Normal","Mu: "+ String.format("%.3f",MOM_mu)+" Sigma: "+String.format("%.3f",MOM_sigma));

        NormalDistribution normal = new NormalDistribution(MOM_mu, MOM_sigma);
        NormalDistribution NORMALNORMAL = new NormalDistribution(0,1);
        double SumDif=0;
        for (int k=0; k<=(N_data-1);k++){
            double prob;
            prob = DataList.get(k).getPx();
            double valor, valor2;
            valor = normal.inverseCumulativeProbability(1-prob);
            valor2 = NORMALNORMAL.inverseCumulativeProbability(1-prob);
            DataList.get(k).setMOM_Nvalue(valor);
            mLogger.printLog("Hay-Normal","Normal MOM; Para la probabilidad: " +prob+" el valor conocido es de "+DataList.get(k).getvalue()+" y el valor isCalculated es: "+DataList.get(k).getMOM_Nvalue());
            mLogger.printLog("Hay-Normal","La variable Z para esta probabilidad es: "+String.format("%.4f",valor2));
            SumDif = SumDif +Math.pow((DataList.get(k).getvalue()-valor),2);
        }
        //normal.cumulativeProbability(x);
        EEA_MOM_N = Math.pow(SumDif/N_data,0.5);
        EEA_ML_N = 1*EEA_MOM_N;

        /**
         * Para la lista de valores projectados con T de interés se hará el válvulo del valor correspondiente
         * a la fdp Norma.
         *
         */
        mLogger.printLog("Hay-Normal","Inicia caĺculo de valores proyectados. ");

        int NP = ProjectedValues.size();

        mLogger.printLog("Hay-Normal", "Cantida de valores a proyectar: " + NP);

        for(int j= 0; j<=(NP-1); j++){

            double prob;
            prob = ProjectedValues.get(j).getPx();
            double valor, valor2;
            valor = normal.inverseCumulativeProbability(1-prob);
            ProjectedValues.get(j).setMOM_Nvalue(valor);
            mLogger.printLog("Hay-NormalProj","El periodo de retorno es " + ProjectedValues.get(j).getPeriodo());
            mLogger.printLog("Hay-NormalProj","Elemento: "+ j +" con la probabilidad: " +prob+" el valor isCalculated es de "+ProjectedValues.get(j).getMOM_Nvalue());
            valor2 = NORMALNORMAL.inverseCumulativeProbability(1-prob);

            mLogger.printLog("Hay-NormalProj","La variable Z para esta probabilidad es: "+String.format("%.4f",valor2));

        }

        Avance = 1;


    }

    public void Calc_MOM_LogNormal2P(){


        if(mLogger==null){
            mLogger = new HikraLogger(getApplicationContext());
        }

        double SumDif;
        /**
         * /////////////////////////////////////////////////////////
         * Distribución LogNormal de 2 parámetros. MOM
         * /////////////////////////////////////////////////////////
         * /////////////////////////////////////////////////////////
         * */
        mLogger.printLog("Hay-MOMLN2P","Para el cálculo de los parámetros de la Distribución LogNormal");
        mLogger.printLog("Hay-MOMLN2P","La media es " +Media+" y la varianza es: "+Var);

        MOM_sigma_LN2P = Math.pow(Math.log((Var/(Math.pow(Media,2)))+1),0.5);
        MOM_mu_LN2P = Math.log(Media)-Math.pow(MOM_sigma_LN2P,2)/2;
        mLogger.printLog("Hay-MOMLN2P","Para la dist LogNormal, sigma es: " +MOM_sigma_LN2P+" y mu es: "+MOM_mu_LN2P);

        LogNormalDistribution LogNormal = new LogNormalDistribution(MOM_mu_LN2P, MOM_sigma_LN2P);

        SumDif = 0;

        for (int k=0; k<=(N_data-1);k++){

            double prob1;
            prob1 = DataList.get(k).getPx();
            double valor1;
            valor1 = LogNormal.inverseCumulativeProbability(1-prob1);
            DataList.get(k).setMOM_LNvalue(valor1);
            mLogger.printLog("Hay-MOMLN2P","LN MOM; Para la probabilidad: " +prob1+" el valor conocido es de "+DataList.get(k).getvalue()+" y el valor isCalculated es: "+DataList.get(k).getMOM_LNvalue());
            SumDif = SumDif +Math.pow((DataList.get(k).getvalue()-valor1),2);
        }

        EEA_MOM_LN2P = Math.pow(SumDif/N_data,0.5);
        mLogger.printLog("Hay-MOMLN2P","SumDif: " +String.valueOf(SumDif));

        /**
         * Para la lista de valores projectados con T de interés se hará el válvulo del valor correspondiente
         * a la fdp LogNormal 2p, MOM.
         *
         */

        int NP = ProjectedValues.size();

        for (int k=0; k<=(NP-1);k++){

            double prob1;
            prob1 = ProjectedValues.get(k).getPx();
            double valor1;
            valor1 = LogNormal.inverseCumulativeProbability(1-prob1);
            ProjectedValues.get(k).setMOM_LNvalue(valor1);
            mLogger.printLog("Hay-MOMLN2Proj", "Elemento " + k + " con Periodo de: " + ProjectedValues.get(k).getPeriodo());
            mLogger.printLog("Hay-MOMLN2Proj","LN MOM; Para la probabilidad: " +prob1+" el valor isCalculated es de: "+ProjectedValues.get(k).getMOM_LNvalue());

        }


        Avance = 2;

    }


    public void CalcML_LNormal2p(){
        /**
         * Distribución LogNormal de 2 parámetros. ML
         *
         * */


        double SumDif = 0;
        double sumaLog = 0;
        for (int k=0; k<=(N_data-1);k++){
            sumaLog = sumaLog + Math.log(DataList.get(k).getvalue());
        }

        ML_mu_LN2P = (sumaLog)/N_data;

        sumaLog = 0;

        for (int k=0; k<=(N_data-1);k++){
            sumaLog = sumaLog + Math.pow(Math.log(DataList.get(k).getvalue())-ML_mu_LN2P,2);
        }
        mLogger.printLog("Hay-MLLN2P","SumaLog: "+String.format("%.3f",sumaLog));
        mLogger.printLog("Hay-MLLN2P","N_data: "+N_data);

        double Y1 = sumaLog /(N_data);
        mLogger.printLog("Hay-MLLN2P","Y1: "+String.format("%.7f",Y1));

        ML_sigma_LN2P = Math.pow(Y1,0.5);

        mLogger.printLog("Hay-MLLN2P","mu: "+String.format("%.6f",ML_mu_LN2P));
        mLogger.printLog("Hay-MLLN2P","Sigma"+String.format("%.3f",ML_sigma_LN2P));

        LogNormalDistribution LogNormal_ML = new LogNormalDistribution(ML_mu_LN2P, ML_sigma_LN2P);

        for (int k=0; k<=(N_data-1);k++){

            double prob2;
            prob2 = DataList.get(k).getPx();
            double valor2;
            mLogger.printLog("Hay-MLLN2P","Previo a valor2");
            valor2 = LogNormal_ML.inverseCumulativeProbability(1-prob2);
            mLogger.printLog("Hay-MLLN2P","Previo a asignación");
            DataList.get(k).setML_LNvalue(valor2);
            mLogger.printLog("Hay-MLLN2P","LN ML; Para la probabilidad: " +prob2+" el valor conocido es de "+DataList.get(k).getvalue()+" y el valor isCalculated es: "+DataList.get(k).getML_LNvalue());
            SumDif = SumDif +Math.pow((DataList.get(k).getvalue()-valor2),2);

        }

        mLogger.printLog("Hay-fitMLLN2P","SumDif: " +String.valueOf(SumDif));

        EEA_ML_LN2P = Math.pow(SumDif/N_data,0.5);

        /**
         * Para la lista de valores projectados con T de interés se hará el válvulo del valor correspondiente
         * a la fdp LogNormal 2p, MOM.
         *
         */

        int NP = ProjectedValues.size();



        for (int k=0; k<=(NP-1);k++){

            double prob2;
            prob2 = ProjectedValues.get(k).getPx();
            double valor2;
            mLogger.printLog("Hay-MLLN2PProj","Previo a valor2");
            valor2 = LogNormal_ML.inverseCumulativeProbability(1-prob2);
            mLogger.printLog("Hay-MLLN2PProj","Previo a asignación");

            ProjectedValues.get(k).setML_LNvalue(valor2);
            mLogger.printLog("Hay-MLLN2PProj", "Elemento " + k + " con Periodo de: " + ProjectedValues.get(k).getPeriodo());
            mLogger.printLog("Hay-MLLN2PProj","LN ML; Para la probabilidad: " +prob2+" el valor isCalculated es: "+ProjectedValues.get(k).getML_LNvalue());
        }




        Avance = 3;

    }


    public void CalcMOM_LNormal3p(){
        double SumDif;
        /**
         * Distribución LogNormal de 3 parámetros. MOM
         *
         * */

        int size = DataList.size();
        double[] value = new double[size];

        for(int i=0; i<=(size-1); i++){

            if (i==0) {


            }
            value[i] = DataList.get(i).getvalue();
            mLogger.printLog("Hay-MOMLN3P","The value[" + i +"] is: " + String.valueOf(value[i]));

        }
       // Skewness asimetria = new Skewness();
       // skewness = asimetria.evaluate(value, 0, value.length);

        MOM_w_LN3P = (-skewness+Math.pow((skewness*skewness)+4,0.5))/2;
        mLogger.printLog("Hay-MOMLN3P","El coeficiente de asimetría es: "+ String.format("%.3f",skewness));

        mLogger.printLog("Hay-MOMLN3P","w "+ String.format("%.3f",MOM_w_LN3P));
        mLogger.printLog("Hay-MOMLN3P","El denominador es "+ Math.pow(MOM_w_LN3P,0.3333333));
        MOM_z2_LN3P = (1-Math.pow(MOM_w_LN3P,0.6666666666666667))/(Math.pow(MOM_w_LN3P,0.33333333333333));

        mLogger.printLog("Hay-MOMLN3P","z2 "+ String.format("%.3f",MOM_z2_LN3P));
        mLogger.printLog("Hay-MOMLN3P","Datos para LogNormal 3P para el cálculo de a,  z2: " +String.valueOf(MOM_z2_LN3P)+", w: "+String.valueOf(MOM_w_LN3P));

        MOM_a_LN3P = Media - Math.pow(Var,0.5)/MOM_z2_LN3P;

        MOM_mu_LN3P = Math.log(Desv_std/MOM_z2_LN3P)-0.5*Math.log(Math.pow(MOM_z2_LN3P,2)+1);
        MOM_sigma_LN3P = Math.pow(Math.log(MOM_z2_LN3P*MOM_z2_LN3P+1),0.5);

        SumDif = 0;
        mLogger.printLog("Hay-MOMLN3P","Datos para LogNormal 3P, a: " +String.valueOf(MOM_a_LN3P)+", mu: "+MOM_mu_LN3P + ", sigma: "+ MOM_sigma_LN3P);
        LogNormalDistribution LogNormal_MOM_3P = new LogNormalDistribution(MOM_mu_LN3P, MOM_sigma_LN3P);

        for (int k=0; k<=(N_data-1);k++){

            double prob3;
            prob3 = DataList.get(k).getPx();
            double valor3;
            mLogger.printLog("Hay-MOMLN3P","Previo a valor3");
            valor3 = LogNormal_MOM_3P.inverseCumulativeProbability(1-prob3)+MOM_a_LN3P;
            mLogger.printLog("Hay-MOMLN3P","Previo a asignación");
            DataList.get(k).setMOM_LN3pV(valor3);
            mLogger.printLog("Hay-MOMLN3P","LN ML; Para la probabilidad: " +prob3+" el valor conocido es de "+DataList.get(k).getvalue()+" y el valor isCalculated es: "+DataList.get(k).getMOM_LN3pV());
            SumDif = SumDif +Math.pow((DataList.get(k).getvalue()-valor3),2);

        }

        mLogger.printLog("Hay-MOMLN3P","SumDif: " +String.valueOf(SumDif));

        EEA_MOM_LN3P = Math.pow(SumDif/N_data,0.5);

        /**
         * Para la lista de valores projectados con T de interés se hará el válvulo del valor correspondiente
         * a la fdp LogNormal 3p, MOM.
         *
         */

        int NP = ProjectedValues.size();

        for (int k=0; k<=(NP-1);k++){

            double prob3;
            prob3 = ProjectedValues.get(k).getPx();
            double valor3;
            mLogger.printLog("Hay-MOMLN3Proj","Previo a valor3");
            valor3 = LogNormal_MOM_3P.inverseCumulativeProbability(1-prob3)+MOM_a_LN3P;
            mLogger.printLog("Hay-MOMLN3Proj","Previo a asignación");
            ProjectedValues.get(k).setMOM_LN3pV(valor3);
            mLogger.printLog("Hay-MOMLN3Proj","LN ML; Para la probabilidad: " +prob3+" el valor isCalculated es: "+ProjectedValues.get(k).getMOM_LN3pV());


        }




        Avance = 4;

    }


    public void CalcML_LNormal3P(){


        if(mLogger==null){
            mLogger = new HikraLogger(getApplicationContext());
        }


        double SumDif;
        /**
         * Distribución LogNormal de 3 parámetros. ML
         *
         * */
        mLogger.printLog("Hay-MLLNormal3P","////////////////////////////////////////////////////////");
        mLogger.printLog("Hay-MLLNormal3P","////////////////////////////////////////////////////////");
        mLogger.printLog("Hay-MLLNormal3P","////////////////////////////////////////////////////////");
        mLogger.printLog("Hay-MLLNormal3P","////////////////////////////////////////////////////////");
        mLogger.printLog("Hay-MLLNormal3P","////////////////////////////////////////////////////////");

        mLogger.printLog("Hay-MLLNormal3P","Previo a la búsqueda de a para ML LogNormal 3 Parámetros");

        ML_a_LN3P = find_a_4_LN3PV2();
        mLogger.printLog("Hay-MLLNormal3P","El valor de a para Ml es:" + String.format("%.2f",ML_a_LN3P));


        //Una vez que tengo a puedo obtener la media y la varianza.
        //Cálculo de mu_y
        double helpmuy = 0;

        double helpVarLN = 0;
        for(int k= 0; k<=(N_data-1); k++){
            double X = List_values.get(k)-ML_a_LN3P;
            //out.println("Xi - a es: " + String.format("%.2f",X));
            helpmuy = helpmuy + Math.log(X);
            //out.println("A es: " + String.format("%.20f",Az));

        }

        mLogger.printLog("Hay-MLLNormal3P","F(a) es:" + String.format("%.9f",evaluatefunction(ML_a_LN3P)));
        mLogger.printLog("Hay-MLLNormal3P","Helpmuy es:" + String.format("%.2f",helpmuy));
        ML_mu_LN3P = helpmuy /N_data;
        //Cálculo de sigma_y al cuadrado


        for(int k= 0; k<=(N_data-1); k++){
            double X = List_values.get(k)-ML_a_LN3P;
            //out.println("Xi - X0 es: " + String.format("%.2f",X));
            double B;
            B = Math.log(X)-ML_mu_LN3P;
            out.println("Log(Xi - a) -mu es: " + String.format("%.2f",B));
            helpVarLN = helpVarLN + Math.pow(B,2);

        }
        mLogger.printLog("Hay-MLLNormal3P","HelpVarLN es:" + String.format("%.2f",helpVarLN));
        ML_var_x_a_LN3P = helpVarLN / N_data;
        ML_sigma_LN3P = Math.pow(ML_var_x_a_LN3P,0.5);
        mLogger.printLog("Hay-MLLNormal3P","La media de (xi-a) para Ml es:" + String.format("%.2f",ML_mu_LN3P));
        mLogger.printLog("Hay-MLLNormal3P","La DesvStd de (xi-a) para Ml es:" + String.format("%.2f",ML_sigma_LN3P));

        LogNormalDistribution LogNormal_ML_3P = new LogNormalDistribution(ML_mu_LN3P, MOM_sigma_LN3P);

        SumDif = 0;
        for (int k=0; k<=(N_data-1);k++){

            double prob4;
            prob4 = DataList.get(k).getPx();
            double valor4;
            mLogger.printLog("Hay-MLLNormal3P","Previo a valor4");
            valor4 = LogNormal_ML_3P.inverseCumulativeProbability(1-prob4)+ML_a_LN3P;
            mLogger.printLog("Hay-MLLNormal3P","Previo a asignación");
            DataList.get(k).setML_LN3pV(valor4);
            mLogger.printLog("Hay-MLLNormal3P","LN ML; Para la probabilidad: " +prob4+" el valor conocido es de "+DataList.get(k).getvalue()+" y el valor isCalculated es: "+DataList.get(k).getML_LN3pV());
            SumDif = SumDif +Math.pow((DataList.get(k).getvalue()-valor4),2);

        }

        mLogger.printLog("Hay-fitMLLNormal3P","SumDif: " +String.valueOf(SumDif));

        EEA_ML_LN3P = Math.pow(SumDif/N_data,0.5);

        /**
         * Para la lista de valores projectados con T de interés se hará el válvulo del valor correspondiente
         * a la fdp LogNormal 3p, MOM.
         *
         */

        int NP = ProjectedValues.size();

        for (int k=0; k<=(NP-1);k++){

            double prob4;
            prob4 = ProjectedValues.get(k).getPx();
            double valor4;
            mLogger.printLog("Hay-MLLN3Proj","Previo a valor4");
            valor4 = LogNormal_ML_3P.inverseCumulativeProbability(1-prob4)+ML_a_LN3P;
            mLogger.printLog("Hay-MLLN3Proj","Previo a asignación");
            ProjectedValues.get(k).setML_LN3pV(valor4);
            mLogger.printLog("Hay-MLLN3Proj","LN ML; Para la probabilidad: " +prob4+" el valor isCalculated es: "+ProjectedValues.get(k).getML_LN3pV());


        }



        Avance = 5;


    }


    public void CalcMOM_Gumbel(){


        if(mLogger==null){
            mLogger = new HikraLogger(getApplicationContext());
        }

        double SumDif;
        /**
         * Distribución Gumbel MOM
         * */

        MOM_alpha_Gumbel = 0.7797 * Desv_std;
        MOM_beta_Gumbel = Media - 0.45005*Desv_std;

        mLogger.printLog("Hay-MOMGumbel","Alpha para la fdp Gumbel: "+String.format("%.3f",MOM_alpha_Gumbel));
        mLogger.printLog("Hay-MOMGumbel","Beta para la fdp Gumbel: "+String.format("%.3f",MOM_beta_Gumbel));


        SumDif = 0;

        for (int k=0; k<=(N_data-1);k++){

            double prob5;
            prob5 = DataList.get(k).getPx();
            double valor5;

            mLogger.printLog("Hay-MOMGumbel","Previo a valor5");
            //x = B-a*ln(-ln(F))
            valor5 = MOM_beta_Gumbel -(MOM_alpha_Gumbel)*Math.log(-1*Math.log(1-prob5));
            DataList.get(k).setMOM_GumbelV(valor5);
            mLogger.printLog("Hay-MOMGumbel","Valor conocido: "+ String.format("%.3f",DataList.get(k).getvalue()) +" y el valor isCalculated es: "+String.format("%.3f",valor5));
            SumDif = SumDif +Math.pow((DataList.get(k).getvalue()-DataList.get(k).getMOM_GumbelV()),2);
            DataList.get(k).setMOM_GumbelV(valor5);

        }

        mLogger.printLog("Hay-MOMGumbel","SumDif "+String.valueOf(SumDif));
        EEA_MOM_Gumbel = Math.pow(SumDif/N_data,0.5);

        /**
         * Para la lista de valores projectados con T de interés se hará el válvulo del valor correspondiente
         * a la fdp Gumbel, MOM.
         *
         */

        int NP = ProjectedValues.size();

        for (int k=0; k<=(NP-1);k++){

            double prob5;
            prob5 = ProjectedValues.get(k).getPx();
            double valor5;

            mLogger.printLog("Hay-MOMGumbelProj","Previo a valor5");
            //x = B-a*ln(-ln(F))
            valor5 = MOM_beta_Gumbel -(MOM_alpha_Gumbel)*Math.log(-1*Math.log(1-prob5));
            ProjectedValues.get(k).setMOM_GumbelV(valor5);
            mLogger.printLog("Hay-MOMGumbelProj","Elemento: "+ k +", con prob: "+ String.format("%.8f",prob5) +", cuyo valor isCalculated es: "+String.format("%.3f",ProjectedValues.get(k).getMOM_GumbelV()));


        }



        Avance = 6;



    }


    public void CalcML_Gumbel(){


        if(mLogger==null){
            mLogger = new HikraLogger(getApplicationContext());
        }

        double SumDif;

        /**
         * Distribución Gumbel ML
         * */

        ML_alpha_Gumbel = find_alpha_Gumbel();

        double sumexp = 0;

        for(int k1 = 0; k1<=(N_data-1);k1++){
            double value1 = DataList.get(k1).getvalue();
            sumexp = sumexp + Math.exp(-value1/ML_alpha_Gumbel);

        }

        mLogger.printLog("Hay-MLGumbel","Sumexp: "+String.format("%.3f",sumexp));
        mLogger.printLog("Hay-MLGumbel","Alpha: "+String.format("%.3f",ML_alpha_Gumbel));
        mLogger.printLog("Hay-MLGumbel","N_data: "+N_data);
        double valorm;
        valorm = Math.log(N_data/sumexp);
        mLogger.printLog("Hay-MLGumbel","valorm: "+String.format("%.3f",valorm));
        ML_beta_Gumbel = ML_alpha_Gumbel*valorm;
        mLogger.printLog("Hay-MLGumbel","Beta: "+String.format("%.3f",ML_beta_Gumbel));
        SumDif = 0;

        for (int k=0; k<=(N_data-1);k++){
            double prob6;
            prob6 = DataList.get(k).getPx();
            double valor6;

            double T = 1/prob6;
            double XA = (T-1)/T;

            mLogger.printLog("Hay-MLGumbel","Gumbel, Previo a valor6, Px ="+String.format("%.3f",prob6)+"; T: "+String.format("%.3f",T));


            valor6 = ML_beta_Gumbel -(ML_alpha_Gumbel)*Math.log(-1*Math.log(1-prob6));
            //valor5 = MOM_beta_Gumbel -(MOM_alpha_Gumbel)*Math.log(-1*Math.log(1-prob5));

            mLogger.printLog("Hay-MLGumbel","Valor registrado: "+String.format("%.3f",DataList.get(k).getvalue())+", Valor isCalculated para la fdp Gumbel: "+String.format("%.3f",valor6));
            SumDif = SumDif +Math.pow((DataList.get(k).getvalue()-valor6),2);
            DataList.get(k).setML_Gumbel(valor6);
        }

        mLogger.printLog("Hay-MLGumbel","SumDif "+String.valueOf(SumDif));
        EEA_ML_Gumbel = Math.pow(SumDif/N_data,0.5);

        /**
         * Para la lista de valores projectados con T de interés se hará el válvulo del valor correspondiente
         * a la fdp Gumbel, ML.
         *
         */

        int NP = ProjectedValues.size();

        for (int k=0; k<=(NP-1);k++){
            double prob6;
            prob6 = ProjectedValues.get(k).getPx();
            double valor6;

            double T = 1/prob6;
            double XA = (T-1)/T;
            mLogger.printLog("Hay-MLGumbelProj","Elemento:"+k);
            mLogger.printLog("Hay-MLGumbelProj","Gumbel, Previo a valor6, Px ="+String.format("%.3f",prob6)+"; T: "+String.format("%.3f",T));


            valor6 = ML_beta_Gumbel -(ML_alpha_Gumbel)*Math.log(Math.log(T));
            mLogger.printLog("Hay-MLGumbelProj","Valor para la fdp Gumbel: "+String.format("%.3f",valor6));

            ProjectedValues.get(k).setML_Gumbel(valor6);
        }




        Avance =7;



    }


    public void CalcMOM_Exp(){


        if(mLogger==null){
            mLogger = new HikraLogger(getApplicationContext());
        }

        double SumDif = 0;
        /**
         *
         * Distribución exponencial
         *
         * */

        MOM_alpha_EXP = Math.pow(Var,0.5);

        MOM_epsilon_EXP = Media - MOM_alpha_EXP;

        //x = Eps - Alpha * ln(Alpha*f(x))
        for (int k2 = 0; k2<=(N_data-1); k2++) {
            double prob7;
            prob7 = DataList.get(k2).getPx();
            double valor7;

            mLogger.printLog("Hay-MOMExp","Previo a valor7");
            mLogger.printLog("Hay-MOMExp","La probabilidad es: "+String.format("%.6f",prob7));

            valor7 = MOM_epsilon_EXP - MOM_alpha_EXP*Math.log(prob7);

            DataList.get(k2).setMOMExpV(valor7);
            mLogger.printLog("Hay-MOMExp","El valor registrado es: "+ String.format("%.3f",DataList.get(k2).getvalue())+" y el valor para la fdp Exponencial: "+String.format("%.3f",valor7));
            SumDif = SumDif +Math.pow((DataList.get(k2).getvalue()-valor7),2);
            DataList.get(k2).setMOMExpV(valor7);

        }

        mLogger.printLog("Hay-MOMExp","SumDif "+String.valueOf(SumDif));
        EEA_MOM_EXP = Math.pow(SumDif/N_data,0.5);

        /**
         * Para la lista de valores projectados con T de interés se hará el válvulo del valor correspondiente
         * a la fdp Exponencial, MOM.
         *
         */

        int NP = ProjectedValues.size();
        for (int k2 = 0; k2<=(NP-1); k2++) {
            double prob7;
            prob7 = ProjectedValues.get(k2).getPx();
            double valor7;

            mLogger.printLog("Hay-MOMExpProj","Previo a valor7");

            valor7 = MOM_epsilon_EXP - MOM_alpha_EXP*Math.log(prob7);
            ProjectedValues.get(k2).setMOMExpV(valor7);
            mLogger.printLog("Hay-MOMExpProj","Valor para la fdp Exponencial: "+String.format("%.3f",valor7));



        }





        Avance = 8;



    }

    public void CalcML_Exp(){


        if(mLogger==null){
            mLogger = new HikraLogger(getApplicationContext());
        }

        double SumDif;
        /***
         *
         * Distribución exponencial ML
         *
         * */

        double A1 = N_data*(Media-DataList.get(N_data-1).getvalue());
        double B1 = N_data-1;
        ML_alpha_EXP = (A1/B1);

        double A2 = N_data*DataList.get(N_data-1).getvalue()-Media;

        ML_epsilon_EXP = (A2/B1);


        SumDif= 0;
        //x = Eps - Alpha * ln(Alpha*f(x))
        for (int k3 = 0; k3<=(N_data-1); k3++) {
            double prob8;
            prob8 = DataList.get(k3).getPx();
            double valor8;

            mLogger.printLog("Hay-ML_Exp","Previo a valor8");

            valor8 = ML_epsilon_EXP - ML_alpha_EXP*Math.log(prob8);
            mLogger.printLog("Hay-ML_Exp","El valor existente es "+String.format("%.3f",DataList.get(k3).getvalue())+"Valor para la fdp Exponencial: "+String.format("%.3f",valor8));
            SumDif = SumDif +Math.pow((DataList.get(k3).getvalue()-valor8),2);
            DataList.get(k3).setMLExpV(valor8);

        }

        mLogger.printLog("Hay-ML_Exp","SumDif "+String.valueOf(SumDif));
        EEA_ML_EXP = Math.pow(SumDif/N_data,0.5);


        /**
         * Para la lista de valores projectados con T de interés se hará el válvulo del valor correspondiente
         * a la fdp Exponencial, ML.
         *
         */

        int NP = ProjectedValues.size();

        for (int k3 = 0; k3<=(NP-1); k3++) {
            double prob8;
            prob8 = ProjectedValues.get(k3).getPx();
            double valor8;

            mLogger.printLog("Hay-MLEXProj","Previo a valor8");

            valor8 = ML_epsilon_EXP - ML_alpha_EXP*Math.log(ML_alpha_EXP*prob8);
            mLogger.printLog("Hay-MLEXProj","Valor para la fdp Exponencial: "+String.format("%.3f",valor8));
            SumDif = SumDif +Math.pow((DataList.get(k3).getvalue()-valor8),2);
            ProjectedValues.get(k3).setMLExpV(valor8);

        }


        Avance = 9;


    }

    public void CalcMOM_G2P(){


        if(mLogger==null){
            mLogger = new HikraLogger(getApplicationContext());
        }

        double SumDif;
        /**
         *
         * Distribución Gamma dos parámetros MOM
         *
         *
         * */
        //Alpha es el parámetro de escala.
        MOM_alpha_Gamma2p = Mm2/Media;
        //Beta es el parámetro de forma.
        MOM_beta_Gamma2p = Media*Media/Mm2;

        GammaDistribution MOM_Gamma_2p = new GammaDistribution(MOM_beta_Gamma2p, MOM_alpha_Gamma2p);
        mLogger.printLog("Hay-MOM_G2P","MOM. Cálculo para la distribución Gamma de dos parámetros. ");

        SumDif = 0;
        for (int k=0; k<=(N_data-1);k++){

            double prob9;
            prob9 = DataList.get(k).getPx();
            double valor9;
            mLogger.printLog("Hay-MOM_G2P","Previo a valor 9");
            valor9 = MOM_Gamma_2p.inverseCumulativeProbability(1-prob9);
            mLogger.printLog("Hay-MOM_G2P","Previo a asignación");
            DataList.get(k).setMOM_G2pV(valor9);
            mLogger.printLog("Hay-MOM_G2P","MOM Gamma 2p; Para la probabilidad: " +String.format("%.3f",prob9)+" el valor conocido es de "+DataList.get(k).getvalue()+" y el valor isCalculated es: "+String.format("%.3f",DataList.get(k).getMOM_G2pV()));
            SumDif = SumDif +Math.pow((DataList.get(k).getvalue()-valor9),2);

        }
        EEA_MOM_G2p= Math.pow(SumDif/N_data,0.5);

        /**
         * Para la lista de valores projectados con T de interés se hará el válvulo del valor correspondiente
         * a la fdp G2P, MOM.
         *
         */
        int NP = ProjectedValues.size();

        for (int k=0; k<=(NP-1);k++){

            double prob9;
            prob9 = ProjectedValues.get(k).getPx();
            double valor9;
            mLogger.printLog("Hay-MOMG2Proj","Previo a valor9");
            valor9 = MOM_Gamma_2p.inverseCumulativeProbability(1-prob9);
            mLogger.printLog("Hay-MOMG2Proj","Previo a asignación");
            ProjectedValues.get(k).setMOM_G2pV(valor9);
            mLogger.printLog("Hay-MOMG2Proj","MOM Gamma 2p; Para la probabilidad: " +String.format("%.3f",prob9)+" el valor isCalculated es: "+String.format("%.3f",ProjectedValues.get(k).getMOM_G2pV()));


        }



        Avance = 10;



    }

    public void CalcML_G2P(){


        if(mLogger==null){
            mLogger = new HikraLogger(getApplicationContext());
        }


        double SumDif;
        /**
         *
         * Distribución Gamma dos parámetros ML
         *
         *
         * */
        mLogger.printLog("Hay-ML_G2P","ML. Cálculo para la distribución Gamma de dos parámetros. ");

        ML_A_Gamma2p = Media;

        ML_U_Gamma2p = Math.log(Media)-Math.log(GMed);

        //mLogger.printLog("Hay-ML_G2P","U: "+String.format("%.3f",ML_U_Gamma2p));

        if(ML_U_Gamma2p<0.5772){

            ML_beta_Gamma2p = (1/ML_U_Gamma2p)*(0.5000876+0.1648852*ML_U_Gamma2p-0.054427*Math.pow(ML_U_Gamma2p,2));

        } else {
            if(ML_U_Gamma2p<17){
                double X1, X2;
                X1 = (8.898919+9.059950*ML_U_Gamma2p+0.9775373*Math.pow(ML_U_Gamma2p,2));
                X2 = ML_U_Gamma2p*((17.7928+11.968477*ML_U_Gamma2p+Math.pow(ML_U_Gamma2p,2)));
            }

        }

        ML_alpha_Gamma2p = ML_A_Gamma2p/ML_beta_Gamma2p;

        mLogger.printLog("Hay-ML_G2P","Alpha: "+String.format("%.3f",ML_alpha_Gamma2p));
        mLogger.printLog("Hay-ML_G2P","Beta: "+String.format("%.3f",ML_beta_Gamma2p));
        mLogger.printLog("Hay-ML_G2P","A: "+String.format("%.3f",ML_A_Gamma2p));
        mLogger.printLog("Hay-ML_G2P","U: "+String.format("%.3f",ML_U_Gamma2p));


        GammaDistribution ML_Gamma_2p = new GammaDistribution(ML_beta_Gamma2p, ML_alpha_Gamma2p);

        SumDif = 0;
        for (int k=0; k<=(N_data-1);k++){

            double prob10;
            prob10 = DataList.get(k).getPx();
            double valor10;
            mLogger.printLog("Hay-ML_G2P","Previo a valor 10");
            valor10 = ML_Gamma_2p.inverseCumulativeProbability(1-prob10);
            mLogger.printLog("Hay-ML_G2P","Previo a asignación");
            DataList.get(k).setML_G2pV(valor10);
            mLogger.printLog("Hay-ML_G2P","ML Gamma 2p; Para la probabilidad: " +String.format("%.3f",prob10)+" el valor conocido es de "+DataList.get(k).getvalue()+" y el valor isCalculated es: "+DataList.get(k).getML_G2pV());
            SumDif = SumDif +Math.pow((DataList.get(k).getvalue()-valor10),2);

        }
        EEA_ML_G2p= Math.pow(SumDif/N_data,0.5);

        /**
         * Para la lista de valores projectados con T de interés se hará el válvulo del valor correspondiente
         * a la fdp G2P, ML.
         *
         */
        int NP = ProjectedValues.size();

        for (int k=0; k<=(NP-1);k++){

            double prob10;
            prob10 = ProjectedValues.get(k).getPx();
            double valor10;
            mLogger.printLog("Hay-MLG2Proj","Previo a valor 10");
            valor10 = ML_Gamma_2p.inverseCumulativeProbability(1-prob10);
            mLogger.printLog("Hay-MLG2Proj","Previo a asignación");
            ProjectedValues.get(k).setML_G2pV(valor10);
            mLogger.printLog("Hay-MLG2Proj","ML Gamma 2p; Para la probabilidad: " +String.format("%.3f",prob10)+" el valor isCalculated es: "+ProjectedValues.get(k).getML_G2pV());
        }



        Avance = 11;


    }

    public void CalcMOM_G3P(){

        if(mLogger==null){
            mLogger = new HikraLogger(getApplicationContext());
        }

        double SumDif;
            /***
         *
         * Gamma de tres parámetros con el MOM
         *
         * */
        mLogger.printLog("Hay-MOM_G3P","MOM. Cálculo para la distribución Gamma de tres parámetros. ");

        mLogger.printLog("Hay-MOM_G3P", "Cs: "+String.format("%.4f",skewness));

        MOM_beta_Gamma3p = Math.pow(2/skewness,2);

        MOM_alpha_Gamma3p = Math.pow(Mm2/MOM_beta_Gamma3p,0.5);

        MOM_g_Gamma3p = Media - Math.pow(Mm2*MOM_beta_Gamma3p,0.5);

        //X = alpha*y+gamma
        mLogger.printLog("Hay-MOM_G3P","Skewness: "+String.format("%.3f",skewness));

        mLogger.printLog("Hay-MOM_G3P","Alpha: "+String.format("%.3f",MOM_alpha_Gamma3p));
        mLogger.printLog("Hay-MOM_G3P","Beta: "+String.format("%.3f",MOM_beta_Gamma3p));


        GammaDistribution MOM_Gamma_3p = new GammaDistribution(MOM_beta_Gamma3p, MOM_alpha_Gamma3p);

        SumDif = 0;
        for (int k=0; k<=(N_data-1);k++){

            double prob11;
            prob11 = DataList.get(k).getPx();
            double valor11;
            mLogger.printLog("Hay-MOM_G3P","Previo a valor 10");
            valor11 = MOM_Gamma_3p.inverseCumulativeProbability(1-prob11)-MOM_g_Gamma3p;
            mLogger.printLog("Hay-MOM_G3P","Previo a asignación");
            DataList.get(k).setMOM_G3pV(valor11);
            mLogger.printLog("Hay-MOM_G3P","MOM Gamma 3p; Para la probabilidad: " +String.format("%.3f",prob11)+" el valor conocido es de "+DataList.get(k).getvalue()+" y el valor isCalculated es: "+DataList.get(k).getMOM_G3pV());
            SumDif = SumDif +Math.pow((DataList.get(k).getvalue()-valor11),2);

        }
        EEA_MOM_G3p= Math.pow(SumDif/N_data,0.5);

        /**
         * Para la lista de valores projectados con T de interés se hará el válvulo del valor correspondiente
         * a la fdp G3P, MOM.
         *
         */
        int NP = ProjectedValues.size();

        for (int k=0; k<=(NP-1);k++){

            double prob11;
            prob11 = ProjectedValues.get(k).getPx();
            double valor11;
            mLogger.printLog("Hay-MOM_G3Proj","Previo a valor 10");
            valor11 = MOM_Gamma_3p.inverseCumulativeProbability(1-prob11)-MOM_g_Gamma3p;
            mLogger.printLog("Hay-MOM_G3Proj","Previo a asignación");
            ProjectedValues.get(k).setMOM_G3pV(valor11);
            mLogger.printLog("Hay-MOM_G3Proj","Elemento: "+k +" con Periodo "+ProjectedValues.get(k).getPeriodo());
            mLogger.printLog("Hay-MOM_G3Proj","MOM Gamma 3p; Para la probabilidad: " +String.format("%.3f",prob11)+" el valor valor isCalculated es: "+ProjectedValues.get(k).getMOM_G3pV());


        }


        Avance = 12;


    }

    public void CalcML_G3P(){


        if(mLogger==null){
            mLogger = new HikraLogger(getApplicationContext());
        }

        double SumDif;
        /***
         *
         * Gamma de tres parámetros con el ML
         *
         * */

        calcGamma3pML();
        mLogger.printLog("Hay-ML_G3P", "Beta Gamma 3P"+ String.format("%.3f",ML_beta_Gamma3p));
        mLogger.printLog("Hay-ML_G3P", "Alpha Gamma 3P"+ String.format("%.3f",ML_alpha_Gamma3p));
        GammaDistribution ML_Gamma_3p = new GammaDistribution(ML_beta_Gamma3p, ML_alpha_Gamma3p);

        SumDif = 0;
        for (int k=0; k<=(N_data-1);k++){

            double prob12;
            prob12 = DataList.get(k).getPx();
            double valor12;
            mLogger.printLog("Hay-ML_G3P","Previo a valor 12");
            valor12 = ML_Gamma_3p.inverseCumulativeProbability(1-prob12)-ML_g_Gamma3p;
            mLogger.printLog("Hay-ML_G3P","Previo a asignación");
            DataList.get(k).setML_G3pV(valor12);
            mLogger.printLog("Hay-ML_G3P","MOM Gamma 3p; Para la probabilidad: " +String.format("%.3f",prob12)+" el valor conocido es de "+DataList.get(k).getvalue()+" y el valor isCalculated es: "+DataList.get(k).getML_G3pV());
            SumDif = SumDif +Math.pow((DataList.get(k).getvalue()-valor12),2);

        }
        EEA_ML_G3p= Math.pow(SumDif/N_data,0.5);




        /**
         * Para la lista de valores projectados con T de interés se hará el válvulo del valor correspondiente
         * a la fdp Gamma3P, ML.
         *
         */
        int NP = ProjectedValues.size();

        for (int k=0; k<=(NP-1);k++){

            double prob12;
            prob12 = ProjectedValues.get(k).getPx();
            double valor12;
            mLogger.printLog("Hay-MLG3Proj","Previo a valor 12");
            valor12 = ML_Gamma_3p.inverseCumulativeProbability(1-prob12)-ML_g_Gamma3p;
            mLogger.printLog("Hay-MLG3Proj","Previo a asignación");
            ProjectedValues.get(k).setML_G3pV(valor12);
            mLogger.printLog("Hay-MLG3Proj","MOM Gamma 3p; Para la probabilidad: " +String.format("%.3f",prob12)+" el valor isCalculated es: "+ProjectedValues.get(k).getML_G3pV());


        }



        Avance = 13;


    }


    public int getAvance(){
        return Avance;
    }

    public int[][] Prueba ={{1907, 41500},
            {1908, 57000},
            {1909, 44000},
            {1910, 49000},
            {1911, 31000},
            {1912, 45900},
            {1913, 19000},
            {1914, 41100},
            {1915, 37300},
            {1916, 76000},
            {1917, 33200},
            {1918, 61200},
            {1919, 76000},
            {1920, 59800},
            {1921, 44400},
            {1922, 58400},
            {1923, 53600},
            {1924, 59800},
            {1925, 63300},
            {1929, 38000},
            {1930, 74600},
            {1931, 13100},
            {1932, 37600},
            {1933, 67500},
            {1934, 21700},
            {1935, 37000},
            {1936, 93500},
            {1937, 58500},
            {1938, 63300},
            {1939, 74400},
            {1940, 34200},
            {1941, 14600},
            {1942, 44200},
            {1944, 73300},
            {1945, 46600},
            {1946, 39400},
            {1947, 41200},
            {1948, 41300},
            {1949, 62000},
            {1950, 90000},
            {1951, 50600},
            {1952, 41900},
            {1953, 35000},
            {1954, 16500},
            {1955, 35300},
            {1956, 30000},
            {1957, 52600},
            {1958, 99000},
            {1959, 89000},
            {1960, 39500},
            {1961, 55400},
            {1962, 46000},
            {1963, 63000},
            {1964, 58300},
            {1965, 36500},
            {1966, 14600},
            {1967, 64900},
            {1968, 68500},
            {1969, 69100},
            {1970, 42600},
            {1971, 31000},
            {1972, 39400},
            {1973, 40700},
            {1974, 53400},
            {1975, 36000},
            {1976, 43900},
            {1977, 23600},
            {1978, 50500},
            {1979, 49700},
            {1980, 48100},
            {1981, 44500},
            {1982, 56400},
            {1983, 60800},
            {1984, 40400},
            {1985, 80400},
            {1986, 41600},
            {1987, 14700},
            {1988, 33300},
            {1989, 40700},
            {1990, 53300},
            {1991, 77400},
            {1926, 57700},
            {1927, 64000},
            {1928, 63500},};


    public double find_a_4_LN3PV2(){


        if(mLogger==null){
            mLogger = new HikraLogger(getApplicationContext());
        }


        double retorno =0;
        double xo = MOM_a_LN3P;
        double Fxo=1000;
        double DerivFxo;

        //Evaluar para xo = MOM_a_LN3P;

        //comparar el resultado con: 0.0000000001
        //Evaluar para xo  al mayor número de la lista, pasado a negativo.
        //Empezar a iterar con el menos negativo de los valores.

        double uno, dos;
        double Maxi = DataList.get(0).getvalue();
        uno = evaluatefunction(xo);
        dos = evaluatefunction(-1*Maxi);

        if(dos>uno){
            double incremento = Math.abs(MOM_a_LN3P)-Math.abs(Maxi);
            incremento = Math.abs(incremento)/10;
            if(MOM_a_LN3P<(-1*Maxi)){
                mLogger.printLog("Hay-findaLN3Pv2","a obtenido con MOM es menor que el negativo del mayor");
                for(int m6= 0; m6<=10; m6++){
                    double val = -1*Maxi-m6*incremento;
                    if(val<0.0001){
                        xo = -1*Maxi-(m6-1)*incremento;
                    break;
                    }
                }

            } else {
                mLogger.printLog("Hay-findaLN3Pv2","a obtenido con MOM es mayor que el negativo del mayor");
                for(int m6= 0; m6<=10; m6++){
                    double val = -1*Maxi+m6*incremento;
                    if(val<0.0001){
                        xo = -1*Maxi-(m6-1)*incremento;
                        break;
                    }
                }



            }

        } else {
            mLogger.printLog("Hay-findaLN3Pv2","Esto no debería pasar");

            double incremento = Math.abs(MOM_a_LN3P)-Math.abs(Maxi);
            incremento = Math.abs(incremento)/10;
            if(MOM_a_LN3P<(-1*Maxi)){
                mLogger.printLog("Hay-findaLN3Pv2","a obtenido con MOM es menor que el negativo del mayor");
                for(int m6= 0; m6<=10; m6++){
                    double val = -1*Maxi-m6*incremento;
                    if(val<0.0001){
                        xo = -1*Maxi-(m6-1)*incremento;
                        break;
                    }
                }

            } else {
                mLogger.printLog("Hay-findaLN3Pv2","a obtenido con MOM es mayor que el negativo del mayor");
                for(int m6= 0; m6<=10; m6++){
                    double val = -1*Maxi+m6*incremento;
                    if(val<0.0001){
                        xo = -1*Maxi-(m6-1)*incremento;
                        break;
                    }
                }



            }



            //NADA
        }

        double valorinicial = xo;
        mLogger.printLog("Hay-findaLN3Pv2","El valor para empezar a iterar es"+String.format("%.3f",valorinicial));
        do{

            Fxo = evaluatefunction(xo);

            DerivFxo = derivadaFa(xo);
            //mLogger.printLog("Hay-findaLN3Pv2","Para xo: "+String.format("%.3f",xo));
          //  mLogger.printLog("Hay-findaLN3Pv2","Fxo: "+String.format("%.3f",Fxo));
        //    mLogger.printLog("Hay-findaLN3Pv2","F'xo: "+String.format("%.3f",DerivFxo));

            retorno = xo;
            xo = xo -Fxo/DerivFxo;
        }while (Fxo > 0.0001);



        mLogger.printLog("Hay-findaLN3Pv2","El valor de a obtenido es: "+String.format("%.3f",retorno));
        mLogger.printLog("Hay-findaLN3Pv2","El valor inicial para empezar a iterar es:  "+String.format("%.3f",valorinicial));
        return  retorno;

    }

    public double derivadaFa(double a){


        if(mLogger==null){
            mLogger = new HikraLogger(getApplicationContext());
        }

        double Fprima = 0;
        double B, D, C, E;
        //Cálculo de mu_y
        double muy=0;

        for(int k= 0; k<=(N_data-1); k++){
            double X = List_values.get(k)-a;
            //out.println("Xi - a es: " + String.format("%.2f",X));
            muy = muy + Math.log(X);
            //out.println("A es: " + String.format("%.20f",Az));

        }
        muy = muy /N_data;
        //Cálculo de sigma_y al cuadrado
        double VarLN=0;

        for(int k= 0; k<=(N_data-1); k++){
            double X = List_values.get(k)-a;
            //out.println("Xi - X0 es: " + String.format("%.2f",X));
            B = Math.log(X)-muy;
            VarLN = VarLN + Math.pow(B,2);

        }
        VarLN = VarLN/N_data;


        C = 0;//Sumatoria de (xi-a)^-2
        D = 0;//Sumateria de [(xi-a)^-2] * Log(xi-a)
        E = 0;//Sumatoria de (xi-a))^-1

        for(int k=0; k<=(N_data-1);k++){
            double X =List_values.get(k)-a;
            C = C+1/Math.pow(X,2);
            D = D+(1/Math.pow(X,2))*Math.log(X);
            E = E+1/X;
        }

        double deriv_mu, deriv_sigma2;

        deriv_mu = 0;

        for(int k=0; k<=(N_data-1);k++){
            double X =List_values.get(k)-a;
            deriv_mu = deriv_mu +1/X;
        }

        deriv_mu = deriv_mu/N_data;

        deriv_sigma2=0;
        double F=0;
        for(int k=0; k<=(N_data-1);k++){
            double X =List_values.get(k)-a;
            F = F+(X-muy)*(-(1/X)-deriv_mu);
        }
        deriv_sigma2 = (2/N_data)*deriv_sigma2;


        Fprima = (muy-VarLN)*C - (D) + (C)+(deriv_mu-deriv_sigma2)*E;



        return Fprima;
    }


    public int getIsCalculated(){

        return isCalculated;
    }


    public void setIsCalculated(int calc){
        isCalculated = calc;
    }

    public void CreateProjList(){

        int tamano = ProjectedValues.size();
        if(tamano>0){
            ProjectedValues.clear();
        }
        //Años
        //2, 5, 10, 20, 50, 100, 200, 500, 1000, 2000, 5000, 10000

        //Calcular T para cada periodo de retorno y agregarlo a la lista.


        double Tx;
        double Pxd;

        //Para 2 años.
        Variable T2 = new Variable();
        Tx = 2;
        Pxd = (Tx-1)/Tx;
        T2.setPeriodo(Tx);
        T2.setPx(1-Pxd);
        ProjectedValues.add(T2);

        //Para 5 años
        Variable T5 = new Variable();
        Tx = 5;
        Pxd = (Tx-1)/Tx;
        T5.setPeriodo(Tx);
        T5.setPx(1-Pxd);
        ProjectedValues.add(T5);

        //Para 10 años
        Variable T10 = new Variable();
        Tx = 10;
        Pxd = (Tx-1)/Tx;
        T10.setPeriodo(Tx);
        T10.setPx(1-Pxd);
        ProjectedValues.add(T10);

        //Paa 50 años
        Variable T50 = new Variable();
        Tx = 50;
        Pxd = (Tx-1)/Tx;
        T50.setPeriodo(Tx);
        T50.setPx(1-Pxd);
        ProjectedValues.add(T50);

        //Paa 60 años
        Variable T60 = new Variable();
        Tx = 60;
        Pxd = (Tx-1)/Tx;
        T60.setPeriodo(Tx);
        T60.setPx(1-Pxd);
        ProjectedValues.add(T60);

        //Para 100 años
        Variable T100 = new Variable();
        Tx = 100;
        Pxd = (Tx-1)/Tx;
        T100.setPeriodo(Tx);
        T100.setPx(1-Pxd);
        ProjectedValues.add(T100);

        //Para 200 años
        Variable T200 = new Variable();
        Tx = 200;
        Pxd = (Tx-1)/Tx;
        T200.setPeriodo(Tx);
        T200.setPx(1-Pxd);
        ProjectedValues.add(T200);

        //Para 500 años
        Variable T500 = new Variable();
        Tx = 500;
        Pxd = (Tx-1)/Tx;
        T500.setPeriodo(Tx);
        T500.setPx(1-Pxd);
        ProjectedValues.add(T500);

        //Para 1000 años
        Variable T1000 = new Variable();
        Tx = 1000;
        Pxd = (Tx-1)/Tx;
        T1000.setPeriodo(Tx);
        T1000.setPx(1-Pxd);
        ProjectedValues.add(T1000);

        //Para 2000 años.
        Variable T2000 = new Variable();
        Tx = 2000;
        Pxd = (Tx-1)/Tx;
        T2000.setPeriodo(Tx);
        T2000.setPx(1-Pxd);
        ProjectedValues.add(T2000);

        //Para 5000 años.
        Variable T5000 = new Variable();
        Tx = 5000;
        Pxd = (Tx-1)/Tx;
        T5000.setPeriodo(Tx);
        T5000.setPx(1-Pxd);
        ProjectedValues.add(T5000);

        //Para 10000 años.
        Variable T10000 = new Variable();
        Tx = 10000;
        Pxd = (Tx-1)/Tx;
        T10000.setPeriodo(Tx);
        T10000.setPx(1-Pxd);
        ProjectedValues.add(T10000);

        int limit = ProjectedValues.size();

        //for(int m=0; m<=(limit-1); m++){

          //  mLogger.printLog("Hay-SetProj", "Para T ="+ProjectedValues.get(m).getPeriodo()+ " la probabilidad es: "+String.format("%.9f",ProjectedValues.get(m).getPx()));
        //}



    }


    public void limpiartodo(){

        if(DataList.size()>0){
            CreateProjList();
            DataList.clear();
            List_values.clear();
            Errores.clear();
            nombrefdp.clear();
            N_data = 0;
            isCalculated = 0;
            ErrorSummary = "";
        }
    }

    public void limpiarResultados(){
        if(DataList.size()>0){
            CreateProjList();
         //   DataList.clear();
            List_values.clear();
            Errores.clear();
            nombrefdp.clear();
            N_data = 0;
            isCalculated = 0;
            ErrorSummary = "";
        }
    }

    public int getNProj(){

        return ProjectedValues.size();
    }





    public String getErrorSummary(){
        ErrorSummary = "";

        ErrorSummary = "<TABLE border=\"1\" align=\"center\">" +
                "<TR> " +
                "<TD rowspan=\"2\">Functions</TD>" +
                "<TD colspan = \"2\" align=\"center\">Moments</TD>"+
                "<TD colspan =\"2\" align=\"center\">Maximum Likelihood</TD>"+


                "</TR>"+

                "<TR>"+
                "<TD align=\"center\">2 parameters </TD>"+
                "<TD align=\"center\">3 parameters </TD>"+
                "<TD align=\"center\">2 parameters </TD>"+
                "<TD align=\"center\">3 parameters </TD>"+
                "</TR>"+

                "<TR>"+
                "<TD>Normal</TD>"+
                "<TD align=\"center\">"+String.format("%.3f",EEA_MOM_N) +"</TD>"+
                "<TD align=\"center\">-----</TD>"+
                "<TD align=\"center\">"+String.format("%.3f",EEA_MOM_N) +"</TD>"+
                "<TD align=\"center\">-----</TD>"+
                "</TR>"+

                "<TR>"+
                "<TD>Lognormal</TD>"+
                "<TD align=\"center\">"+String.format("%.3f",EEA_MOM_LN2P)+"</TD>"+
                "<TD align=\"center\">"+String.format("%.3f",EEA_MOM_LN3P)+"</TD>"+
                "<TD align=\"center\">"+String.format("%.3f",EEA_ML_LN2P)+"</TD>"+
                "<TD align=\"center\">"+String.format("%.3f",EEA_ML_LN3P)+"</TD>"+
                "</TR>"+

                "<TR>"+
                "<TD>Gumbel</TD>"+
                "<TD align=\"center\">"+String.format("%.3f",EEA_MOM_Gumbel)+"</TD>"+
                "<TD align=\"center\">-----</TD>"+
                "<TD align=\"center\">"+String.format("%.3f",EEA_ML_Gumbel)+"</TD>"+
                "<TD align=\"center\">-----</TD>"+
                "</TR>"+

                "<TR>"+
                "<TD>Exponential</TD>"+
                "<TD align=\"center\">"+String.format("%.3f",EEA_MOM_EXP)+"</TD>"+
                "<TD align=\"center\">-----</TD>"+
                "<TD align=\"center\">"+String.format("%.3f",EEA_ML_EXP)+"</TD>"+
                "<TD align=\"center\">-----</TD>"+
                "</TR>"+

                "<TR>"+
                "<TD>Gamma</TD>"+
                "<TD align=\"center\">"+String.format("%.3f",EEA_MOM_G2p)+"</TD>"+
                "<TD align=\"center\">"+String.format("%.3f",EEA_MOM_G3p)+"</TD>"+
                "<TD align=\"center\">"+String.format("%.3f",EEA_ML_G2p)+"</TD>"+
                "<TD align=\"center\">"+String.format("%.3f",EEA_ML_G3p)+"</TD>"+
                "</TR>"+




                "</TABLE>";

        return ErrorSummary;

    }

    public static Statistics instance;

    public static Statistics getInstance()
    {
        if (instance== null) {
            synchronized(Statistics.class) {
                if (instance == null)
                    instance = new Statistics();
            }
        }
        // Return the instance
        return instance;
    }

    public String getminparameters(){
        String parameters="";
        //dataset.setPosmin(12);
        //dataset.setminimo(dataset.getPosmin());
        switch(getPosmin()){

            case 0:        //0 - EEA de la distribución normal, MOM.
                parameters = " with the Method of Moments, and its parameters are <br>"+
                        "Mu: "+String.format("%.3f",MOM_mu)+ "<br>"+
                        "Sigma: "+String.format("%.3f",MOM_sigma);

                break;
            case 1:        //1 EEA de la distribución LN, MOM con dos parámetros.
                parameters = " with the Method of Moments, and its parameters are <br>"+
                        "Mu: "+String.format("%.3f",getMOM_mu_LN2P())+ "<br>"+
                        "Sigma: "+String.format("%.3f",getMOM_sigma_LN2P());
                break;
            case 2:        //2 EEA de la distribución LN 3P, MOM
                parameters = " with the Method of Moments, and its parameters are <br>"+
                        "Mu: "+String.format("%.3f",getMOM_mu_LN3P())+ "<br>"+
                        "Sigma: "+String.format("%.3f",getMOM_sigma_LN3P())+"<br>"+
                        "a: "+String.format("%.3f",getMOM_a_LN3P());
                break;
            case 3:        //3 EEA de la distribución Gumbel MOM
                parameters = " with the Method of Moments, and its parameters are <br>"+
                        "Alpha: "+String.format("%.3f",getMOM_alpha_Gumbel())+ "<br>"+
                        "Beta: "+String.format("%.3f",getMOM_beta_Gumbel());
                break;
            case 4:        //4 EEA de la distribución Exponencial MOM
                parameters = " with the Method of Moments, and its parameters are <br>"+
                        "Alpha: "+String.format("%.3f",getMOM_alpha_EXP())+ "<br>"+
                        "Epsilon: "+String.format("%.3f",getMOM_epsilon_EXP());
                break;
            case 5:        //5 EEA de la distribución Gamma de dos parámetros. MOM
                parameters = " with the Method of Moments, and its parameters are <br>"+
                        "Alpha: "+String.format("%.3f",getMOM_alpha_Gamma2p())+ "<br>"+
                        "Beta: "+String.format("%.3f",getMOM_beta_Gamma2p());
                break;
            case 6:        //6 EEA de la distribución Gamma de tres parámetros.MOM
                parameters = " with the Method of Moments, and its parameters are <br>"+
                        "Alpha: "+String.format("%.3f",getMOM_alpha_Gamma3p())+ "<br>"+
                        "Beta: "+String.format("%.3f",getMOM_beta_Gamma3p())+"<br>"+
                        "x<sub>0</sub>: "+ String.format("%.3f",getMOM_g_Gamma3p());
                break;

            /***
             *
             * INICIAN LOS CASOS CON EL MÉTODO DE MÁXIMA VEROSIMILITUD
             *
             *
             */
            case 7:             //7 EEA de la distribución Log Normal ML con dos parámetros.
                parameters = " with the Method of Maximum Likelihood, and its parameters are <br>"+
                        "Mu: "+String.format("%.3f",getML_mu_LN2P())+ "<br>"+
                        "Sigma: "+String.format("%.3f",getML_sigma_LN2P());
                break;
            case 8:        //8 EEA de la distribución LogNormal con tres parámetros.
                parameters = " with the Method of Maximum Likelihood, and its parameters are <br>"+
                        "Mu: "+String.format("%.3f",getML_mu_LN3P())+ "<br>"+
                        "Sigma: "+String.format("%.3f",getML_sigma_LN3P())+"<br>"+
                        "a: "+String.format("%.3f",getML_a_LN3P());
                break;
            case 9:         //9 EEA de la distribución Gumbel ML
                parameters = " with the Method of Maximum Likelihood, and its parameters are <br>"+
                        "Alpha: "+String.format("%.3f",getML_alpha_Gumbel())+ "<br>"+
                        "Beta: "+String.format("%.3f",getML_alpha_Gumbel());
                break;
            case 10:         //10 EEA de la distribución exponencial ML
                parameters = " with the Method of Maximum Likelihood, and its parameters are <br>"+
                        "Alpha: "+String.format("%.3f",getML_alpha_EXP())+ "<br>"+
                        "Epsilon: "+String.format("%.3f",getML_epsilon_EXP());
                break;
            case 11:         //11 EEA de la distribución Gamma con dos parámetros. ML
                parameters = " with the Method of Maximum Likelihood, and its parameters are <br>"+
                        "Alpha: "+String.format("%.3f",getML_alpha_Gamma2p())+ "<br>"+
                        "Beta: "+String.format("%.3f",getML_beta_Gamma2p());
                break;
            case 12:         //12 EEA de la distribución Gamma con tres parámetros.ML
                parameters = " with the Method of Maximum Likelihood, and its parameters are <br>"+
                        "Alpha: "+String.format("%.3f",getML_alpha_Gamma3p())+ "<br>"+
                        "Beta: "+String.format("%.3f",getML_beta_Gamma3p())+"<br>"+
                        "x<sub>0</sub>: "+ String.format("%.3f",getML_g_Gamma3p());
                break;


        }

        return parameters;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getFittedData(){

        String TXFitted = "";
        //Definir encabezados de la tabla.
        String header, foot, contents;
        contents="";
        //Tr - Value - Calc/Fitted - Error
        header = "<TABLE border=\"1\" align=\"center\">" +
                "<TR> " +
                "<TD >Tr</TD>" +
                "<TD align=\"center\">Value</TD>"+
                "<TD  align=\"center\">Fitted</TD>"+
                "<TD align=\"center\"> Error^2</TD>"+
                "</TR>";

        mLogger.printLog("Hay-setFitted", "Hay "+N_data+" datos por agregar a la tabla. ");
        for (int k=0; k<=(N_data-1);k++) {
            double valor1 = getTi(k);
            double valor2 = getVi(k);
            double valor3 = getFiti(k);
            double valor4 = getEi(k);

            NumberFormat nf = NumberFormat.getInstance();
            String val1 = nf.format(valor1);
            String val2 = nf.format(valor2);
            String val3 = nf.format(valor3);
            String val4 = nf.format(valor4);


            String dummy = "<TR> " +
                    "<TD align=\"center\">"+val1 +"</TD>" +
                    "<TD align=\"center\">"+val2 +"</TD>" +
                    "<TD align=\"center\">"+val3 +"</TD>" +
                    "<TD align=\"center\">"+val4 +"</TD>" +
                    "</TR>";
            contents = contents+dummy;
        }

        foot =  "</TABLE>";


        TXFitted = header + contents+ foot;

    return TXFitted;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getProjected(){


        if(mLogger==null){
            mLogger = new HikraLogger(getApplicationContext());
        }

        String TXFitted = "";
        int NP = ProjectedValues.size();
        //Definir encabezados de la tabla.
        String header, foot, contents;
        contents="";
        //Tr - Value - Calc/Fitted - Error
        header = "<TABLE border=\"1\" align=\"center\">" +
                "<TR> " +
                "<TD >Tr</TD>" +
                "<TD align=\"center\">Px</TD>"+
                "<TD  align=\"center\">Value</TD>"+

                "</TR>";

        mLogger.printLog("Hay-setFitted", "Hay "+N_data+" datos por agregar a la tabla. ");
        for (int k=0; k<=(NP-1);k++) {
            double valor1 = ProjectedValues.get(k).getPeriodo();
            double valor2 = ProjectedValues.get(k).getPx();
            double valor3 = ProjectedValues.get(k).getFittedV();


            NumberFormat nf = NumberFormat.getInstance();
            String val1 = nf.format(valor1);
            String val2 = String.format("%.5f",valor2);
            String val3 = nf.format(valor3);



            String dummy = "<TR> " +
                    "<TD align=\"center\">"+val1 +"</TD>" +
                    "<TD align=\"center\">"+val2 +"</TD>" +
                    "<TD align=\"center\">"+val3 +"</TD>" +

                    "</TR>";
            contents = contents+dummy;
        }

        foot =  "</TABLE>";


        TXFitted = header + contents+ foot;

        return TXFitted;


    }

    public String TEXfunction(){
        String fdp_eq= "";

        switch(getPosmin()){

            case 0:        //0 - EEA de la distribución normal, MOM.
                fdp_eq = "$$f(x) =  \\frac{1}{\\sigma \\sqrt{2\\pi}}e^{-\\frac{1}{2\\sigma^2}(X-\\mu)^{2}}$$";

                break;
            case 1:        //1 EEA de la distribución LN, MOM con dos parámetros.
                fdp_eq = "$$f(x) =  \\frac{1}{x \\sigma_{y} \\sqrt{2\\pi}}e^{-\\frac{1}{2\\sigma^{2}_{y}}(ln x-\\mu_y)^{2}}$$";

                break;
            case 2:        //2 EEA de la distribución LN 3P, MOM
                fdp_eq = "$$f(x) =  \\frac{1}{(x-a) \\sigma_{y} \\sqrt{2\\pi}}e^{-\\frac{1}{2\\sigma^{2}_{y}}(ln( x-a) -\\mu_y)^{2}}$$";

                break;
            case 3:        //3 EEA de la distribución Gumbel MOM
                fdp_eq = "$$f(x) =  \\frac{1}{\\alpha }e^{-\\frac{x - \\beta}{\\alpha}-e^{\\frac{x - \\beta}{\\alpha}}}$$";

                break;
            case 4:        //4 EEA de la distribución Exponencial MOM
                fdp_eq = "$$f(x) =  \\frac{1}{\\alpha }e^{-\\frac{x - \\epsilon}{\\alpha}}$$";

                break;
            case 5:        //5 EEA de la distribución Gamma de dos parámetros. MOM
                fdp_eq = "$$f(x) =  \\frac{1}{\\alpha^{\\beta} \\Gamma(\\beta)}x^{\\beta -1}e^{-\\frac{x}{\\alpha}}$$";
                        //"Alpha: "+String.format("%.3f",getMOM_alpha_Gamma2p())+ "<br>"+
                        //"Beta: "+String.format("%.3f",getMOM_beta_Gamma2p());
                break;
            case 6:        //6 EEA de la distribución Gamma de tres parámetros.MOM
                fdp_eq = "$$f(x) =  \\frac{1}{\\alpha^{\\beta} \\Gamma(\\beta)}(\\frac{x-\\gamma}{\\alpha})^{( \\beta -1 )}e^{-\\frac{x-\\gamma}{\\alpha}}$$";
                        //"Alpha: "+String.format("%.3f",getMOM_alpha_Gamma3p())+ "<br>"+
                        //"Beta: "+String.format("%.3f",getMOM_beta_Gamma3p())+"<br>"+
                        //"x<sub>0</sub>: "+ String.format("%.3f",getMOM_g_Gamma3p());
                break;

            /***
             *
             * INICIAN LOS CASOS CON EL MÉTODO DE MÁXIMA VEROSIMILITUD
             *
             *
             */
            case 7:             //7 EEA de la distribución Log Normal ML con dos parámetros.
                fdp_eq = "$$f(x) =  \\frac{1}{x \\sigma_{y} \\sqrt{2\\pi}}e^{-\\frac{1}{2\\sigma^{2}_{y}}(ln x-\\mu_y)^{2}}$$";

                break;
            case 8:        //8 EEA de la distribución LogNormal con tres parámetros.
                fdp_eq = "$$f(x) =  \\frac{1}{(x-a) \\sigma_{y} \\sqrt{2\\pi}}e^{-\\frac{1}{2\\sigma^{2}_{y}}(ln( x-a) -\\mu_y)^{2}}$$";

                break;
            case 9:         //9 EEA de la distribución Gumbel ML
                fdp_eq = "$$f(x) =  \\frac{1}{\\alpha }e^{-\\frac{x - \\beta}{\\alpha}-e^{\\frac{x - \\beta}{\\alpha}}}$$";

                break;
            case 10:         //10 EEA de la distribución exponencial ML
                fdp_eq = "$$f(x) =  \\frac{1}{\\alpha }e^{-\\frac{x - \\epsilon}{\\alpha}}$$";

                break;
            case 11:         //11 EEA de la distribución Gamma con dos parámetros. ML
                fdp_eq = "$$f(x) =  \\frac{1}{\\alpha^{\\beta} \\Gamma(\\beta)}x^{\\beta -1}e^{-\\frac{x}{\\alpha}}$$";
                        //"Alpha: "+String.format("%.3f",getML_alpha_Gamma2p())+ "<br>"+
                        //"Beta: "+String.format("%.3f",getML_beta_Gamma2p());
                break;
            case 12:         //12 EEA de la distribución Gamma con tres parámetros.ML
                fdp_eq = "$$f(x) =  \\frac{1}{\\alpha^{\\beta} \\Gamma(\\beta)}(\\frac{x-\\gamma}{\\alpha})^{( \\beta -1 )}e^{-\\frac{x-\\gamma}{\\alpha}}$$";
                        //"Alpha: "+String.format("%.3f",getML_alpha_Gamma3p())+ "<br>"+
                        //"Beta: "+String.format("%.3f",getML_beta_Gamma3p())+"<br>"+
                        //"x<sub>0</sub>: "+ String.format("%.3f",getML_g_Gamma3p());
                break;


        }

        return fdp_eq;


    }


    public String TEXparam(){
        String parameters= "";

        switch(getPosmin()){
            case 0:        //0 - EEA de la distribución normal, MOM.
                parameters = "$$\\mu = " + String.format("%.3f",MOM_mu) + "; \\sigma =" + String.format("%.3f",MOM_sigma) +"$$";


                break;
            case 1:        //1 EEA de la distribución LN, MOM con dos parámetros.
                parameters =  "$$\\mu = " + String.format("%.3f",getMOM_mu_LN2P()) + "; \\sigma =" + String.format("%.3f",getMOM_sigma_LN2P()) +"$$";
                      //  "Mu: "++ "<br>"+
                      //  "Sigma: "+;
                break;
            case 2:        //2 EEA de la distribución LN 3P, MOM
                parameters ="$$\\mu = " + String.format("%.3f",getMOM_mu_LN3P()) + "; \\sigma =" + String.format("%.3f",getMOM_sigma_LN3P()) +"; a=" + String.format("%.3f",getMOM_a_LN3P())+"$$";
                     //   "Mu: "++ "<br>"+
                     //   "Sigma: "++"<br>"+
                     //   "a: "+;
                break;
            case 3:        //3 EEA de la distribución Gumbel MOM
                parameters = "$$\\alpha = " + String.format("%.3f",getMOM_alpha_Gumbel()) + "; \\beta =" + String.format("%.3f",getMOM_beta_Gumbel()) +"$$";
                        //"Alpha: "++ "<br>"+
                        //"Beta: "+;
                break;
            case 4:        //4 EEA de la distribución Exponencial MOM
                parameters = "$$\\alpha = " + String.format("%.3f",getMOM_alpha_EXP()) + "; \\epsilon =" + String.format("%.3f",getMOM_epsilon_EXP()) +"$$";
                       // "Alpha: "++ "<br>"+
                      //  "Epsilon: "+;
                break;
            case 5:        //5 EEA de la distribución Gamma de dos parámetros. MOM
                parameters = "$$\\alpha = " + String.format("%.3f",getMOM_alpha_Gamma2p()) + "; \\beta =" + String.format("%.3f",getMOM_beta_Gamma2p()) +"$$";
                       // "Alpha: "++ "<br>"+
                       // "Beta: "+;
                break;
            case 6:        //6 EEA de la distribución Gamma de tres parámetros.MOM
                parameters = "$$\\mu = " + String.format("%.3f",getMOM_alpha_Gamma3p()) + "; \\beta =" +String.format("%.3f",getMOM_beta_Gamma3p())  +"; \\gamma =" + String.format("%.3f",getMOM_g_Gamma3p())+"$$";
                     //   "Alpha: "++ "<br>"+
                     //   "Beta: "++"<br>"+
                     //   "x<sub>0</sub>: "+ ;
                break;

            /***
             *
             * INICIAN LOS CASOS CON EL MÉTODO DE MÁXIMA VEROSIMILITUD
             *
             *
             */
            case 7:             //7 EEA de la distribución Log Normal ML con dos parámetros.
                parameters = "$$\\mu = " + String.format("%.3f",getML_mu_LN2P()) + "; \\sigma =" + String.format("%.3f",getML_sigma_LN2P()) +"$$";
                        //"Mu: "++ "<br>"+
                        //"Sigma: "+;
                break;
            case 8:        //8 EEA de la distribución LogNormal con tres parámetros.
                parameters = "$$\\mu = " + String.format("%.3f",getML_mu_LN3P()) + "; \\sigma =" + String.format("%.3f",getML_sigma_LN3P()) +"; a=" +String.format("%.3f",getML_a_LN3P()) +"$$";
                     //   "Mu: "++ "<br>"+
                     //   "Sigma: "++"<br>"+
                     //   "a: "+;
                break;
            case 9:         //9 EEA de la distribución Gumbel ML
                parameters = "$$\\alpha = " + String.format("%.3f",getML_alpha_Gumbel()) + "; \\beta =" + String.format("%.3f",getML_alpha_Gumbel()) +"$$";
                       // "Alpha: "++ "<br>"+
                       // "Beta: "+;
                break;
            case 10:         //10 EEA de la distribución exponencial ML
                parameters = "$$\\alpha = " + String.format("%.3f",getML_alpha_EXP())  + "; \\epsilon =" +String.format("%.3f",getML_epsilon_EXP())  +"$$";
                        //"Alpha: "++ "<br>"+
                        //"Epsilon: "+;
                break;
            case 11:         //11 EEA de la distribución Gamma con dos parámetros. ML
                parameters = "$$\\alpha = " + String.format("%.3f",getML_alpha_Gamma2p()) + "; \\beta =" +String.format("%.3f",getML_beta_Gamma2p()) +"$$";
                       // "Alpha: "++ "<br>"+
                      //  "Beta: "+;
                break;
            case 12:         //12 EEA de la distribución Gamma con tres parámetros.ML
                parameters = "$$\\mu = " +String.format("%.3f",getML_alpha_Gamma3p()) + "; \\beta =" + String.format("%.3f",getML_beta_Gamma3p()) +"; \\gamma =" +String.format("%.3f",getML_g_Gamma3p()) +"$$";
                       // "Alpha: "++ "<br>"+
                       // "Beta: "++"<br>"+
                       // "x<sub>0</sub>: "+ ;
                break;

        }

        return parameters;


    }


    public int getSizeList(){
        int SizeD;

        if(DataList.size()==0){
            SizeD = 0;
        } else {
            SizeD = DataList.size();
        }
        return SizeD;

    }

    public int getEncalculo(){
        return encalculo;
    }

    public void setEncalculo(int k){

        encalculo = k;
    }

    public Variable getProjVariable(int i){
        return ProjectedValues.get(i);
    }

    public ArrayList<Variable> getProjectedValuesList(){
        return ProjectedValues;
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


    public void addError(String error){
        listaerrores.add(error);

    }
    public String getError(int k){
        return listaerrores.get(k);
    }

    public int cantErrores(){

        return listaerrores.size();
    }

    public int sizeprojectlist(){
        return ProjectedValues.size();
    }

    public ArrayList<String> setandgetlist(){

        String parameters, fdp_eq;
        ArrayList<String> AllFDPDATA = new ArrayList<>();
        //Elemento 0: Media, desviación estándar.
        parameters = "$$\\mu = " + String.format("%.3f",getMean()) + "; \\sigma =" + String.format("%.3f",getDesv_std()) +
                "; \\ C_s =  "+String.format("%.3f",skewness)+ "$$";
        AllFDPDATA.add(parameters);
        //Elemento 1: Fórmula FDP Normal.
        fdp_eq = "$$f(x) =  \\frac{1}{\\sigma \\sqrt{2\\pi}}e^{-\\frac{1}{2\\sigma^2}(X-\\mu)^{2}}$$";
        AllFDPDATA.add(fdp_eq);

        //Elemento 2: Parámetros FDP Normal
                //2 - EEA de la distribución normal, MOM.
        parameters = "$$\\mu = " + String.format("%.3f",MOM_mu) + "; \\sigma =" + String.format("%.3f",MOM_sigma) +"$$";
        AllFDPDATA.add(parameters);

        //Elemento 3: Fórmula LogNormal 2P, MOM
        fdp_eq = "$$f(x) =  \\frac{1}{x \\sigma_{y} \\sqrt{2\\pi}}e^{-\\frac{1}{2\\sigma^{2}_{y}}(ln x-\\mu_y)^{2}}$$";
        AllFDPDATA.add(fdp_eq);
        //Elemento 4: Parámetros LogNormal 2P, MOM
        parameters =  "$$\\mu_y = " + String.format("%.3f",getMOM_mu_LN2P()) + "; \\sigma_y =" + String.format("%.3f",getMOM_sigma_LN2P()) +"$$";
        AllFDPDATA.add(parameters);
        //Elemento 5: Parámetros LogNormal 2P, ML
        parameters = "$$\\mu_y = " + String.format("%.3f",getML_mu_LN2P()) + "; \\sigma_y =" + String.format("%.3f",getML_sigma_LN2P()) +"$$";
        AllFDPDATA.add(parameters);

        //Elemento 6: Fórmula LogNormal 3P, MOM
        fdp_eq = "$$f(x) =  \\frac{1}{(x-a) \\sigma_{y} \\sqrt{2\\pi}}e^{-\\frac{1}{2\\sigma^{2}_{y}}(ln( x-a) -\\mu_y)^{2}}$$";
        AllFDPDATA.add(fdp_eq);
        //Elemento 7: Parámetros LogNormal 3P, MOM
        parameters ="$$\\mu_y = " + String.format("%.3f",getMOM_mu_LN3P()) + "; \\sigma_y =" + String.format("%.3f",getMOM_sigma_LN3P()) +"; a=" + String.format("%.3f",getMOM_a_LN3P())+"$$";
        AllFDPDATA.add(parameters);
        //Elemento 8: Parámetros LogNormal 3P, ML
        parameters = "$$\\mu_y = " + String.format("%.3f",getML_mu_LN3P()) + "; \\sigma_y =" + String.format("%.3f",getML_sigma_LN3P()) +"; a=" +String.format("%.3f",getML_a_LN3P()) +"$$";
        AllFDPDATA.add(parameters);

        //Elemento 9: Fórmula Gumbel MOM
        fdp_eq = "$$f(x) =  \\frac{1}{\\alpha }e^{-\\frac{x - \\beta}{\\alpha}-e^{\\frac{x - \\beta}{\\alpha}}}$$";
        AllFDPDATA.add(fdp_eq);
        //Elemento 10: Parámetros Gumbel MOM
        parameters = "$$\\alpha = " + String.format("%.3f",getMOM_alpha_Gumbel()) + "; \\beta =" + String.format("%.3f",getMOM_beta_Gumbel()) +"$$";
        AllFDPDATA.add(parameters);
        //Elemento 11: Parámetros Gumbel ML
        parameters = "$$\\alpha = " + String.format("%.3f",getML_alpha_Gumbel()) + "; \\beta =" + String.format("%.3f",getML_beta_Gumbel()) +"$$";
        AllFDPDATA.add(parameters);

        //Elemento 12: Fórmula EXP MOM
        fdp_eq = "$$f(x) =  \\frac{1}{\\alpha }e^{-\\frac{x - \\epsilon}{\\alpha}}$$";
        AllFDPDATA.add(fdp_eq);
        //Elemento 13: Parámetros EXP MOM
        parameters = "$$\\alpha = " + String.format("%.3f",getMOM_alpha_EXP()) + "; \\epsilon =" + String.format("%.3f",getMOM_epsilon_EXP()) +"$$";
        AllFDPDATA.add(parameters);
        //Elemento 14: Parámetros EXP ML
        parameters = "$$\\alpha = " + String.format("%.3f",getML_alpha_EXP())  + "; \\epsilon =" +String.format("%.3f",getML_epsilon_EXP())  +"$$";
        AllFDPDATA.add(parameters);

        //Elemento 15: Fórmula Gamma 2P MOM
        fdp_eq = "$$f(x) =  \\frac{1}{\\alpha^{\\beta} \\Gamma(\\beta)}x^{\\beta -1}e^{-\\frac{x}{\\alpha}}$$";
        AllFDPDATA.add(fdp_eq);
        //Elemento 16: Parámetros Gamma 2P MOM
        parameters = "$$\\alpha = " + String.format("%.3f",getMOM_alpha_Gamma2p()) + "; \\beta =" + String.format("%.3f",getMOM_beta_Gamma2p()) +"$$";
        AllFDPDATA.add(parameters);
        //Elemento 17: Parámetros Gamma 2P ML
        parameters = "$$\\alpha = " + String.format("%.3f",getML_alpha_Gamma2p()) + "; \\beta =" +String.format("%.3f",getML_beta_Gamma2p()) +"$$";
        AllFDPDATA.add(parameters);

        //Elemento 18: Fórmula Gamma 3P MOM
        fdp_eq = "$$f(x) =  \\frac{1}{\\alpha^{\\beta} \\Gamma(\\beta)}(\\frac{x-\\gamma}{\\alpha})^{( \\beta -1 )}e^{-\\frac{x-\\gamma}{\\alpha}}$$";
        AllFDPDATA.add(fdp_eq);
        //Elemento 19: Parámetros Gamma 3P MOM
        parameters = "$$\\mu = " + String.format("%.3f",getMOM_alpha_Gamma3p()) + "; \\beta =" +String.format("%.3f",getMOM_beta_Gamma3p())  +"; \\gamma =" + String.format("%.3f",getMOM_g_Gamma3p())+"$$";
        AllFDPDATA.add(parameters);
        //Elemento 20: Parámetros Gamma 3P ML
        parameters = "$$\\mu = " +String.format("%.3f",getML_alpha_Gamma3p()) + "; \\beta =" + String.format("%.3f",getML_beta_Gamma3p()) +"; \\gamma =" +String.format("%.3f",getML_g_Gamma3p()) +"$$";
        AllFDPDATA.add(parameters);

        //Elemento 21: sólo el nombre de la distribución de probabilidad.
        parameters = Minimo;
        AllFDPDATA.add(parameters);
        return AllFDPDATA;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<String> gettableAllFdp(){

        String header, foot, contents;

        header = "<TABLE border=\"1\" align=\"center\">" +
                "<TR> " +
                "<TD >T</TD>" +
                "<TD align=\"center\">Value</TD>"+
                "<TD  align=\"center\">Fitted Value</TD>"+

                "</TR>";


        foot =  "</TABLE>";

        ArrayList<String> tablas = new ArrayList<>();

        //0 - EEA de la distribución normal, MOM.
                String Table_MOM_N;
                contents = "";

                for(int k=0; k<=(N_data-1); k++){
                    double valor1 = DataList.get(k).getT();
                    double valor2 = DataList.get(k).getvalue();
                    double valor3 = DataList.get(k).getMOM_Nvalue();


                    NumberFormat nf = NumberFormat.getInstance();
                    String val1 = String.format("%.2f",valor1);
                    String val2 = nf.format(valor2);
                    String val3 = nf.format(valor3);



                    String dummy = "<TR> " +
                            "<TD align=\"center\">"+val1 +"</TD>" +
                            "<TD align=\"center\">"+val2 +"</TD>" +
                            "<TD align=\"center\">"+val3 +"</TD>" +

                            "</TR>";
                    contents = contents+dummy;

                }

                Table_MOM_N = header + "<center>Normal by MOM/MML </center><br>"+contents + foot;
                tablas.add(Table_MOM_N);





            //1 EEA de la distribución LN, MOM con dos parámetros.

            String Table_MOM_LN2P;
                contents = "";
                for(int k=0; k<=(N_data-1); k++){
                    double valor1 = DataList.get(k).getT();
                    double valor2 = DataList.get(k).getvalue();
                    double valor3 = DataList.get(k).getMOM_LNvalue();


                    NumberFormat nf = NumberFormat.getInstance();
                    String val1 = String.format("%.2f",valor1);
                    String val2 = nf.format(valor2);
                    String val3 = nf.format(valor3);



                    String dummy = "<TR> " +
                            "<TD align=\"center\">"+val1 +"</TD>" +
                            "<TD align=\"center\">"+val2 +"</TD>" +
                            "<TD align=\"center\">"+val3 +"</TD>" +

                            "</TR>";
                    contents = contents+dummy;

                }
                Table_MOM_LN2P = header + "<center>LogNormal 2P by MOM </center><br>"+contents + foot;
                tablas.add(Table_MOM_LN2P);


        //2 EEA de la distribución LN, MML con dos parámetros.

        String Table_ML_LN2P;
        contents = "";
        for(int k=0; k<=(N_data-1); k++){
            double valor1 = DataList.get(k).getT();
            double valor2 = DataList.get(k).getvalue();
            double valor3 = DataList.get(k).getML_LNvalue();


            NumberFormat nf = NumberFormat.getInstance();
            String val1 = String.format("%.2f",valor1);
            String val2 = nf.format(valor2);
            String val3 = nf.format(valor3);



            String dummy = "<TR> " +
                    "<TD align=\"center\">"+val1 +"</TD>" +
                    "<TD align=\"center\">"+val2 +"</TD>" +
                    "<TD align=\"center\">"+val3 +"</TD>" +

                    "</TR>";
            contents = contents+dummy;

        }
        Table_ML_LN2P = header +"<center>LogNormal 2P by MML</center><br>"+ contents + foot;
        tablas.add(Table_ML_LN2P);


        //3 EEA de la distribución LN 3P, MOM

        String Table_MOM_LN3P;
        contents = "";
        for(int k=0; k<=(N_data-1); k++){
            double valor1 = DataList.get(k).getT();
            double valor2 = DataList.get(k).getvalue();
            double valor3 = DataList.get(k).getMOM_LN3pV();


            NumberFormat nf = NumberFormat.getInstance();
            String val1 = String.format("%.2f",valor1);
            String val2 = nf.format(valor2);
            String val3 = nf.format(valor3);



            String dummy = "<TR> " +
                    "<TD align=\"center\">"+val1 +"</TD>" +
                    "<TD align=\"center\">"+val2 +"</TD>" +
                    "<TD align=\"center\">"+val3 +"</TD>" +

                    "</TR>";
            contents = contents+dummy;

        }
        Table_MOM_LN3P = header +"<center>LogNormal 3P by MOM</center><br>"+ contents + foot;
        tablas.add(Table_MOM_LN3P);

        //4 EEA de la distribución LN 3P, MOM

        String Table_ML_LN3P;
        contents = "";
        for(int k=0; k<=(N_data-1); k++){
            double valor1 = DataList.get(k).getT();
            double valor2 = DataList.get(k).getvalue();
            double valor3 = DataList.get(k).getML_LN3pV();


            NumberFormat nf = NumberFormat.getInstance();
            String val1 = String.format("%.2f",valor1);
            String val2 = nf.format(valor2);
            String val3 = nf.format(valor3);



            String dummy = "<TR> " +
                    "<TD align=\"center\">"+val1 +"</TD>" +
                    "<TD align=\"center\">"+val2 +"</TD>" +
                    "<TD align=\"center\">"+val3 +"</TD>" +

                    "</TR>";
            contents = contents+dummy;

        }
        Table_ML_LN3P = header +"<center>LogNormal 3P by MML</center><br>"+ contents + foot;
        tablas.add(Table_ML_LN3P);


        //5 EEA de la distribución Gumbel, MOM

        String Table_MOM_Gumbel;
        contents = "";
        for(int k=0; k<=(N_data-1); k++){
            double valor1 = DataList.get(k).getT();
            double valor2 = DataList.get(k).getvalue();
            double valor3 = DataList.get(k).getMOM_GumbelV();


            NumberFormat nf = NumberFormat.getInstance();
            String val1 = String.format("%.2f",valor1);
            String val2 = nf.format(valor2);
            String val3 = nf.format(valor3);



            String dummy = "<TR> " +
                    "<TD align=\"center\">"+val1 +"</TD>" +
                    "<TD align=\"center\">"+val2 +"</TD>" +
                    "<TD align=\"center\">"+val3 +"</TD>" +

                    "</TR>";
            contents = contents+dummy;

        }
        Table_MOM_Gumbel = header +"<center>Gumbel by MOM</center><br>"+ contents + foot;
        tablas.add(Table_MOM_Gumbel);

        //6 EEA de la distribución Gumbel, MML

        String Table_ML_Gumbel;
        contents = "";
        for(int k=0; k<=(N_data-1); k++){
            double valor1 = DataList.get(k).getT();
            double valor2 = DataList.get(k).getvalue();
            double valor3 = DataList.get(k).getML_Gumbel();


            NumberFormat nf = NumberFormat.getInstance();
            String val1 = String.format("%.2f",valor1);
            String val2 = nf.format(valor2);
            String val3 = nf.format(valor3);



            String dummy = "<TR> " +
                    "<TD align=\"center\">"+val1 +"</TD>" +
                    "<TD align=\"center\">"+val2 +"</TD>" +
                    "<TD align=\"center\">"+val3 +"</TD>" +

                    "</TR>";
            contents = contents+dummy;

        }
        Table_ML_Gumbel = header +"<center>Gumbel by MML</center><br>"+ contents + foot;
        tablas.add(Table_ML_Gumbel);


        //7 EEA de la distribución Exponencial MOM

        String Table_MOM_EXP;
        contents = "";
        for(int k=0; k<=(N_data-1); k++){
            double valor1 = DataList.get(k).getT();
            double valor2 = DataList.get(k).getvalue();
            double valor3 = DataList.get(k).getMOMExpV();


            NumberFormat nf = NumberFormat.getInstance();
            String val1 = String.format("%.2f",valor1);
            String val2 = nf.format(valor2);
            String val3 = nf.format(valor3);



            String dummy = "<TR> " +
                    "<TD align=\"center\">"+val1 +"</TD>" +
                    "<TD align=\"center\">"+val2 +"</TD>" +
                    "<TD align=\"center\">"+val3 +"</TD>" +

                    "</TR>";
            contents = contents+dummy;

        }
        Table_MOM_EXP = header +"<center>Exponential by MOM</center><br>"+ contents + foot;
        tablas.add(Table_MOM_EXP);

        //8 EEA de la distribución Exponencial, MML

        String Table_ML_EXP;
        contents = "";
        for(int k=0; k<=(N_data-1); k++){
            double valor1 = DataList.get(k).getT();
            double valor2 = DataList.get(k).getvalue();
            double valor3 = DataList.get(k).getMLExpV();


            NumberFormat nf = NumberFormat.getInstance();
            String val1 = String.format("%.2f",valor1);
            String val2 = nf.format(valor2);
            String val3 = nf.format(valor3);



            String dummy = "<TR> " +
                    "<TD align=\"center\">"+val1 +"</TD>" +
                    "<TD align=\"center\">"+val2 +"</TD>" +
                    "<TD align=\"center\">"+val3 +"</TD>" +

                    "</TR>";
            contents = contents+dummy;

        }
        Table_ML_EXP = header +"<center>Exponential by MML</center><br>"+ contents + foot;
        tablas.add(Table_ML_EXP);


        //9 EEA de la distribución Gamma2P, MOM

        String Table_MOM_G2P;
        contents = "";
        for(int k=0; k<=(N_data-1); k++){
            double valor1 = DataList.get(k).getT();
            double valor2 = DataList.get(k).getvalue();
            double valor3 = DataList.get(k).getMOM_G2pV();


            NumberFormat nf = NumberFormat.getInstance();
            String val1 = String.format("%.2f",valor1);
            String val2 = nf.format(valor2);
            String val3 = nf.format(valor3);



            String dummy = "<TR> " +
                    "<TD align=\"center\">"+val1 +"</TD>" +
                    "<TD align=\"center\">"+val2 +"</TD>" +
                    "<TD align=\"center\">"+val3 +"</TD>" +

                    "</TR>";
            contents = contents+dummy;

        }
        Table_MOM_G2P = header +"<center>Gamma 2P by MOM</center><br>"+ contents + foot;
        tablas.add(Table_MOM_G2P);

        //10 EEA de la distribución Gamma2P, ML

        String Table_ML_G2P;
        contents = "";
        for(int k=0; k<=(N_data-1); k++){
            double valor1 = DataList.get(k).getT();
            double valor2 = DataList.get(k).getvalue();
            double valor3 = DataList.get(k).getML_G2pV();


            NumberFormat nf = NumberFormat.getInstance();
            String val1 = String.format("%.2f",valor1);
            String val2 = nf.format(valor2);
            String val3 = nf.format(valor3);



            String dummy = "<TR> " +
                    "<TD align=\"center\">"+val1 +"</TD>" +
                    "<TD align=\"center\">"+val2 +"</TD>" +
                    "<TD align=\"center\">"+val3 +"</TD>" +

                    "</TR>";
            contents = contents+dummy;

        }
        Table_ML_G2P = header +"<center>Gamma 2P by MML</center><br>"+ contents + foot;
        tablas.add(Table_ML_G2P);


        //11 EEA de la distribución Gamma3P, MOM

        String Table_MOM_G3P;
        contents = "";
        for(int k=0; k<=(N_data-1); k++){
            double valor1 = DataList.get(k).getT();
            double valor2 = DataList.get(k).getvalue();
            double valor3 = DataList.get(k).getMOM_G3pV();


            NumberFormat nf = NumberFormat.getInstance();
            String val1 = String.format("%.2f",valor1);
            String val2 = nf.format(valor2);
            String val3 = nf.format(valor3);



            String dummy = "<TR> " +
                    "<TD align=\"center\">"+val1 +"</TD>" +
                    "<TD align=\"center\">"+val2 +"</TD>" +
                    "<TD align=\"center\">"+val3 +"</TD>" +

                    "</TR>";
            contents = contents+dummy;

        }
        Table_MOM_G3P = header +"<center>Gamma 3P by MOM</center><br>"+ contents + foot;
        tablas.add(Table_MOM_G3P);


        //12 EEA de la distribución Gamma3P, ML

        String Table_ML_G3P;
        contents = "";
        for(int k=0; k<=(N_data-1); k++){
            double valor1 = DataList.get(k).getT();
            double valor2 = DataList.get(k).getvalue();
            double valor3 = DataList.get(k).getML_G3pV();


            NumberFormat nf = NumberFormat.getInstance();
            String val1 = String.format("%.2f",valor1);
            String val2 = nf.format(valor2);
            String val3 = nf.format(valor3);



            String dummy = "<TR> " +
                    "<TD align=\"center\">"+val1 +"</TD>" +
                    "<TD align=\"center\">"+val2 +"</TD>" +
                    "<TD align=\"center\">"+val3 +"</TD>" +

                    "</TR>";
            contents = contents+dummy;

        }
        Table_ML_G3P = header +"<center>Gamma 3P by MML</center><br>"+ contents + foot;
        tablas.add(Table_ML_G3P);





    return tablas;

    }

    class Sortbyvalue implements Comparator<Variable>
    {
        // Used for sorting in ascending order of
        // roll number

        public int compare(Variable o1, Variable o2) {
            int response = 0;
            if (o1.getvalue()>o2.getvalue()){
                response = -1;
            } else {
                if(o1.getvalue()<o2.getvalue()){
                    response= 1;
                }
            }
            return response;
        }
    }


    public HashMap<String,Double> getBasicStatistics(){
        if(isCalculated==0){
            basicStatisticsHM = null;
        } else {
            basicStatisticsHM = new HashMap<String,Double>();
            basicStatisticsHM.put(Constants.BSTAT_MEAN, Media);
            basicStatisticsHM.put(Constants.BSTAT_STD_DEV, Desv_std);
            basicStatisticsHM.put(Constants.BSTAT_SKEW, skewness);
        }

        return basicStatisticsHM;
    }


}

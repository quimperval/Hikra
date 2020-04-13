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


package com.per.hikra.models;
import java.io.Serializable;

public class Variable extends DataPair  implements Serializable {




    private double T;
    private double Px;
    private double Periodo;
    //////////////////////////////////
    ////VALUES OBTAINED WITH MOM
    private double MOM_Nvalue;
    private double MOM_LogNValue;
    private double MOM_Log3pV;
    private double MOM_GumbelV;
    private double MOM_ExpV;
    private double MOM_Gamma2pV;
    private double MOM_Gamma3pV;


    //////////////////////////////////
    ////VALUES OBTAINED WITH ML
    private double ML_Nvalue;
    private double ML_LogNValue;
    private double ML_Log3pV;
    private double ML_GumbelV;
    private double ML_ExpV;
    private double ML_Gamma2pV;
    private double ML_Gamma3pV;

    ////////////////////
    private double fittedV;
    private double fittedE;


    public Variable() {

    }

    public Variable(int year, double value){
        setyear(year);
        setvalue(value);
    }


    ///////////////////////////////////
    /////METHODS FOR MOM VARIABLES.
    public void setMOM_Nvalue(double Nval_in){
        MOM_Nvalue = Nval_in;
    }

    public double getMOM_Nvalue(){
        return MOM_Nvalue;
    }

    /////////////Lognormal de 2 parámetros

    public void setMOM_LNvalue(double LN_val){
        MOM_LogNValue = LN_val;
    }


    public double getMOM_LNvalue(){
        return MOM_LogNValue;
    }

    ////////////Lognormal de 3 parámetros

    public void setMOM_LN3pV(double valor1){
        MOM_Log3pV = valor1;
    }
    public double getMOM_LN3pV(){
        return MOM_Log3pV;
    }

    ////////////////Gumbel
    public void setMOM_GumbelV(double valuein){
        MOM_GumbelV = valuein;
    }

    public double getMOM_GumbelV(){
        return MOM_GumbelV;
    }

    ///////Exponential

    public void setMOMExpV(double valuein){
        MOM_ExpV = valuein;
    }

    public double getMOMExpV(){
        return MOM_ExpV;
    }

    public void setMOM_G2pV(double valorg){
        MOM_Gamma2pV = valorg;
    }

    public double getMOM_G2pV(){
        return MOM_Gamma2pV;
    }

    public void setMOM_G3pV(double valorg){
        MOM_Gamma3pV = valorg;
    }

    public double getMOM_G3pV(){
        return MOM_Gamma3pV;
    }
    ///////////////////////////////////
    /////METHODS FOR ML VARIABLES.

    public void setML_LNvalue(double ML_LNval){
        ML_LogNValue = ML_LNval;
    }

    public double getML_LNvalue(){
        return ML_LogNValue;
    }

    ///////////////Lognormal 3p

    public void setML_LN3pV(double valor2){
        ML_Log3pV = valor2;
    }

    public double getML_LN3pV(){
        return ML_Log3pV;
    }

    ///////////////Gumbel

    public void setML_Gumbel(double valorin){
        ML_GumbelV = valorin;
    }

    public double getML_Gumbel(){
        return ML_GumbelV;

    }

    public void setMLExpV ( double valor_in){
        ML_ExpV = valor_in;
    }

    public double getMLExpV(){
        return ML_ExpV;
    }

    public void setML_G2pV(double valor1){
        ML_Gamma2pV = valor1;
    }

    public double getML_G2pV(){
        return ML_Gamma2pV;
    }

    public void setML_G3pV(double valor1){
        ML_Gamma3pV = valor1;
    }

    public double getML_G3pV(){
        return ML_Gamma3pV;
    }

    ////////////////////////////////////////
    ///////////////////////////////////////
    ///////////////////////////////////////
    public void variable(){
        T=0;
        Px=0;

    }

    public void setT(double Tin){
        T = Tin;

    }

    public void setPx(double Pxin){
        Px = Pxin;
    }

    public double getT(){

        return T;
    }

    public double getPx(){

        return Px;
    }

    public void setFitted(double valor){
        fittedV = valor;
    }

    public double getFittedV(){
        return fittedV;
    }

    public void setFittedE(double valorx){
        fittedE = valorx;
    }

    public double getFittedE(){
        return fittedE;
    }

    public void setPeriodo (double Per){

        Periodo = Per;
    }

    public double getPeriodo(){
        return Periodo;
    }





}

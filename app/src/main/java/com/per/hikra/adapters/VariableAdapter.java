/******************************************************************************
 * Copyright (c) 2018, Joaquín Pérez Valera
 *
 * This software is available under the following "MIT Style" license,
 * or at the option of the licensee under the LGPL (see COPYING). *
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

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.per.hikra.R;
import com.per.hikra.models.Variable;
import com.per.hikra.utils.Constants;

import java.util.ArrayList;

public class VariableAdapter extends RecyclerView.Adapter<VariableAdapter.VariableViewHolder> {


    private ArrayList<Variable> listOfData;

    private EditText yearET;

    private EditText valueET;

    private Context mContext;



    private Button mButton;

    public VariableAdapter(){

    }

    public VariableAdapter(Context mContext, ArrayList<Variable> listOfData, EditText yearET, EditText valueET,  Button mButton){

        this.mButton = mButton;
        this.yearET = yearET;
        this.valueET = valueET;
        this.listOfData = listOfData;
        this.mContext = mContext;
        Log.i("VariableAdapter", "Variable Adapter constructor, size of data: " + listOfData.size());
    }

    public void AddItem( Variable mVariable){
        this.listOfData.add(mVariable);

    }

    @NonNull
    @Override
    public VariableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.card_id_item,parent, false);
        return new VariableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VariableViewHolder holder, int position) {

        Variable mVar = listOfData.get(position);

        holder.tvItemId.setText(String.valueOf(position+1));
        holder.tvItemValue.setText(String.valueOf(mVar.getvalue()));
        holder.tvItemYear.setText(String.valueOf(mVar.getyear()));
        holder.imbEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editItem(position, mVar.getyear(), mVar.getvalue());
            }
        });

        holder.imbDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listOfData.size();
    }

    public class VariableViewHolder extends RecyclerView.ViewHolder{

        public TextView tvItemId;
        public TextView tvItemYear;
        public TextView tvItemValue;

        public ImageButton imbEditButton;
        public ImageButton imbDeleteButton;

        public VariableViewHolder(@NonNull View itemView) {

            super(itemView);
            tvItemId =  itemView.findViewById(R.id.itemNumber);
            tvItemYear =  itemView.findViewById(R.id.itemYear);
            tvItemValue =itemView.findViewById(R.id.itemValue);

            imbEditButton =  itemView.findViewById(R.id.editButton);
            imbDeleteButton =  itemView.findViewById(R.id.deleteButton);

        }
    }

    public void deleteItem(int position){
        listOfData.remove(position);
        notifyDataSetChanged();

        Log.i("deleteItem", "Delete item position");
    }

    public void editItem(int position, int year, double value){
        Log.i("editItem", "Edit item position");
        yearET.setText(String.valueOf(year));
        valueET.setText(String.valueOf(value));

        mButton.setText(Constants.UPDATE_TEXT);
        listOfData.remove(position);
        notifyDataSetChanged();
    }





}

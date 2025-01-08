package com.example.practiceoffirebase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(var itemData: ArrayList<ItemData>, recInterface: RecInterface) :
    RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {

        var sName: TextView = view.findViewById(R.id.tvName)
        var sClass: TextView = view.findViewById(R.id.tvClass)
        var sRollNo: TextView = view.findViewById(R.id.tvRollNo)
        val update: Button = view.findViewById(R.id.btnUpdate)
        val delete: Button = view.findViewById(R.id.btnDelete)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_layout, parent, false)
        )

    }

    override fun onBindViewHolder(holder: MyAdapter.ViewHolder, position: Int) {
        holder.sName.setText(itemData[position].stuName)
        holder.sClass.setText(itemData[position].stuClass)
        holder.sRollNo.setText(itemData[position].stuRoll.toString())
        holder.update.setOnClickListener {
            RecInterface.updateBtn(position)

        }

        holder.delete.setOnClickListener {
            RecInterface.deleteBtn(position)
        }

    }

    override fun getItemCount(): Int {
        return itemData.size

    }
}



package com.example.appcalorias.activities.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appcalorias.R
import com.example.appcalorias.databinding.ItemRecordBinding
import com.example.appcalorias.db.model.Record

class RecordAdapter (private var records: List<Record>, private val fn : ((Record) -> Unit)? = null)
    : RecyclerView.Adapter<RecordAdapter.RecordViewHolder>()
{

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            return RecordViewHolder(layoutInflater.inflate(R.layout.item_record, parent, false))
        }

        override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
            holder.bind(records[position])
        }

        override fun getItemCount(): Int = records.size


        inner class RecordViewHolder (view : View) : RecyclerView.ViewHolder (view) {

            private val b = ItemRecordBinding.bind(view)

            fun bind (record : Record) {

                b.tvRecordDate.text = record.date
                b.tvRecordCalories.text = record.calories.toString()
                b.tvUserBMRValue.text = "tontopollas"

                itemView.setOnClickListener { fn?.invoke(record) }
            }
        }
}
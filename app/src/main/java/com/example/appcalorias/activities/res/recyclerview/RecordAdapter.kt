package com.example.appcalorias.activities.res.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.appcalorias.R
import com.example.appcalorias.databinding.ItemRecordBinding
import com.example.appcalorias.db.model.Record

class RecordAdapter(private var records: List<Record>, private val fn: ((Record) -> Unit)? = null) :
    RecyclerView.Adapter<RecordAdapter.RecordViewHolder>() {

    private val recordsToDelete = mutableListOf<Record>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return RecordViewHolder(layoutInflater.inflate(R.layout.item_record, parent, false))
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        holder.bind(records[position])
    }

    override fun getItemCount(): Int = records.size


    /**
     * Metodo para obtener los registros que se han seleccionado para eliminar.
     * @return Lista de registros seleccionados para eliminar.
     */
    fun getRecordsToDelete(): List<Record> {
        return recordsToDelete
    }


    inner class RecordViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        private val b = ItemRecordBinding.bind(view)

        fun bind(record: Record) {
            b.tvRecordDate.text = record.date
            b.tvCaloriesValue.text = record.calories.toString().plus(" kcal")
            b.tvCarbohydratesValue.text = record.carbohydrates.toString().plus(" g")
            b.tvProteinsValue.text = record.proteins.toString().plus(" g")
            b.tvFatsValue.text = record.fats.toString().plus(" g")

            itemView.setOnClickListener { fn?.invoke(record) }
            b.btnDeleteRecord.setOnClickListener {
                if (!recordsToDelete.contains(record)) {
                    recordsToDelete.add(record)
                    b.generalDiv.setBackgroundColor(ContextCompat.getColor(view.context, R.color.deletion_red))
                }else {
                    recordsToDelete.remove(record)
                    b.generalDiv.setBackgroundColor(ContextCompat.getColor(view.context, android.R.color.transparent))
                }
            }
        }
    }
}
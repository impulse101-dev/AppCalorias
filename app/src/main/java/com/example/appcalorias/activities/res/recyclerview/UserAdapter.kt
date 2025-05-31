package com.example.appcalorias.activities.res.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appcalorias.R
import com.example.appcalorias.databinding.ItemUserBinding
import com.example.appcalorias.db.model.User
import com.example.appcalorias.db.model.res.Gender

class UserAdapter (private var records: List<User>, private val fn : ((User) -> Unit)? = null)
    : RecyclerView.Adapter<UserAdapter.UserViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return UserViewHolder(layoutInflater.inflate(R.layout.item_user, parent, false))
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(records[position])
    }

    override fun getItemCount(): Int = records.size


    inner class UserViewHolder (view : View) : RecyclerView.ViewHolder (view) {

        private val b = ItemUserBinding.bind(view)

        fun bind (user : User) {

            b.tvUserId.text = user.id.toString()
            b.tvUserDate.text = user.dateUpdate
            b.tvAge.text = user.age.toString()
            b.tvBmr.text = user.bmr.toString()
            if (user.gender == Gender.MALE) b.tvGender.text = "Masculino" else {b.tvGender.text = "Femenino"}
            b.tvWeight.text = user.weight.toString()
            b.tvHeight.text = user.height.toString()

            itemView.setOnClickListener { fn?.invoke(user) }
        }
    }
}
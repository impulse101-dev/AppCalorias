package com.example.appcalorias.activities.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appcalorias.R
import com.example.appcalorias.databinding.ItemUserBinding
import com.example.appcalorias.db.model.user.User

class UserAdapter (private var users: List<User>, private val fn : (User) -> Unit)
    : RecyclerView.Adapter<UserAdapter.UserViewHolder>()
{

        companion object {

        }

        //todo ADAPTAR PARA LAS CALORIAS DE CADA DIA ESTE RECYCLERVIEW
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            return UserViewHolder(layoutInflater.inflate(R.layout.item_user, parent, false))
        }

        override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
            holder.bind(users[position])
        }

        override fun getItemCount(): Int = users.size


        inner class UserViewHolder (view : View) : RecyclerView.ViewHolder (view) {

            private val b = ItemUserBinding.bind(view)

            fun bind (user : User) {
//                Picasso
//                    .get()
//                    .load(user.image)
//                    .into(b.ivUserPhoto)
//
//                b.tvName.text = user.name.toString()
//                println("Usuario: ${user.name} con image: ${user.image}")

                itemView.setOnClickListener { fn(user) }
            }
        }
}
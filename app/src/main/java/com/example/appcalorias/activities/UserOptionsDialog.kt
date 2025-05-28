package com.example.appcalorias.activities

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.appcalorias.databinding.UserOptionsDialogBinding
import com.example.appcalorias.db.AppCaloriesDB
import com.example.appcalorias.db.DatabaseProvider
import com.example.appcalorias.db.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
//todo commit Deleted unused activities (UserOptionsDialog)
class UserOptionsDialog(private val user: User, private val onUserDeleted: (() -> Unit)? = null)
    : DialogFragment() {

    private lateinit var b : UserOptionsDialogBinding
    private lateinit var room : AppCaloriesDB

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        b = UserOptionsDialogBinding.inflate(layoutInflater)
        initProperties()

        return Dialog(requireContext()).apply {
            setContentView(b.root)
        }
    }

    private fun initProperties () {
        setText()
        room = DatabaseProvider.getDatabase(this.requireContext())
        initActionListeners()
    }

    private fun setText () {
//        Picasso.get().load(user.image).into(b.ivPhoto)
//        b.tvName.text = user.name
        b.tvBMRValue.text = user.bmr.toString()
    }


    private fun initActionListeners () {
        b.btBorrar.setOnClickListener {
            val userDao = room.getUserDAO()

            CoroutineScope(Dispatchers.IO).launch {
                userDao.deleteUser(user)


                activity?.runOnUiThread {
                    onUserDeleted?.invoke()
                    dismiss()       //cierra el DialogFragment
                }
            }
        }
    }

}
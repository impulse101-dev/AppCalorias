package com.example.appcalorias.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appcalorias.R
import com.example.appcalorias.activities.recyclerview.UserAdapter
import com.example.appcalorias.databinding.ActivityListaUsuariosBinding
import com.example.appcalorias.db.AppCaloriesDB
import com.example.appcalorias.db.DatabaseProvider
import com.example.appcalorias.db.model.user.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListaUsuarios : AppCompatActivity() {

    private lateinit var b : ActivityListaUsuariosBinding
    private val recyclerView : RecyclerView by lazy { b.rvUserList }
    private lateinit var userAdapter : UserAdapter
    private lateinit var room : AppCaloriesDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        b = ActivityListaUsuariosBinding.inflate(layoutInflater)

        setSupportActionBar(b.toolBar)

        setContentView(b.root)
        ViewCompat.setOnApplyWindowInsetsListener(b.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initProperties()
    }

    private fun initRecyclerView (userList: List<User>) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        userAdapter = UserAdapter(userList) {onclickItem(it)}
        recyclerView.adapter = userAdapter
    }

    private fun onclickItem (user : User) {
        //Intent(this, UserOptionsDialog::class.java).also { startActivity(it) }
        UserOptionsDialog(
            user,
            onUserDeleted = {loadEpisodes()}
        ).show(
            supportFragmentManager,
            null
        )
    }

    private fun loadEpisodes() {
        CoroutineScope(Dispatchers.IO).launch {
            //val users : List<User> = room.getUserDAO().getAll()

           // Log.d("Lista de usuarios", users.toString())

//            runOnUiThread {
//                if (users.isNotEmpty()) {
//                    initRecyclerView(users)
//                } else {
//                    Log.d("loadEpisodios","Error al cargar los usuarios, no hay usuarios en la base de datos")
//                }
//            }
        }
    }

    private fun initProperties() {
        room = DatabaseProvider.getDatabase(this)
        loadEpisodes()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.custom_main_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
//            R.id.miAddProfile -> {
//                Intent(this, AddEditProfile::class.java).also{ startActivity(it) }
//            }

            R.id.miCalendar -> {

            }
        }
        return true
    }
}
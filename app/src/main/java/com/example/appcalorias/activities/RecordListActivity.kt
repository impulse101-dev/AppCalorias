package com.example.appcalorias.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appcalorias.R
import com.example.appcalorias.activities.res.recyclerview.RecordAdapter
import com.example.appcalorias.activities.res.toolbar.ToolbarManager
import com.example.appcalorias.databinding.ActivityListaUsuariosBinding
import com.example.appcalorias.db.AppCaloriesDB
import com.example.appcalorias.db.DatabaseProvider
import com.example.appcalorias.db.model.Record
import com.example.appcalorias.db.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RecordListActivity : AppCompatActivity() {

    private lateinit var b : ActivityListaUsuariosBinding
    private val recyclerView : RecyclerView by lazy { b.rvRecordsList }
    private lateinit var recordAdapter : RecordAdapter
    private lateinit var room : AppCaloriesDB
    private var user : User? = null
    private lateinit var toolbarManager: ToolbarManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        b = ActivityListaUsuariosBinding.inflate(layoutInflater)

        setContentView(b.root)
        ViewCompat.setOnApplyWindowInsetsListener(b.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initProperties()
    }

    private fun initRecyclerView (recordList: List<Record>) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recordAdapter = RecordAdapter(recordList) {onclickItem(it)}
        recyclerView.adapter = recordAdapter
    }

    private fun onclickItem (record : Record) {
        //Intent(this, UserOptionsDialog::class.java).also { startActivity(it) }
//        UserOptionsDialog(
//            user,
//            onUserDeleted = {loadEpisodes()}
//        ).show(
//            supportFragmentManager,
//            null
//        )
    }
//todo este metodo utiliza ifs innecesarios, se puede simplificar
    private fun loadEpisodes() {
        CoroutineScope(Dispatchers.IO).launch {
            var recordsForUser : List<Record>

            if (user != null) {
                recordsForUser = room.getRecordDAO().getRecordsByUserId(user!!.id)
            } else {
                recordsForUser = emptyList()
            }

            println("Lista de records por el usuario $recordsForUser, \tusuario: $user")

            runOnUiThread {
                if (recordsForUser.isNotEmpty()) {
                    initRecyclerView(recordsForUser)
                } else {
                    Log.d("loadEpisodios","Error al cargar los usuarios, no hay usuarios en la base de datos")
                }
            }
        }
    }

    private fun initProperties() {
        room = DatabaseProvider.getDatabase(this)
        user = intent.getSerializableExtra(User.PREFS_USER_ID, User::class.java)
        toolbarManager = ToolbarManager(this, b.toolBar, user)
        toolbarManager.setup()
        loadEpisodes()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return toolbarManager.createMenu(menu)
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean { todo el de arriba no lo has comentado
//        when (item.itemId) {
//            R.id.miProfile -> {
//                Intent(this, MainActivity::class.java).also{ startActivity(it) }
//            }
//
//            R.id.miCalendar -> {
//                Toast.makeText(this,"Ya estas en esta pantalla", Toast.LENGTH_SHORT).show()
//            }
//        }
//        return true
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return toolbarManager.handleItemClick(item)
    }
}
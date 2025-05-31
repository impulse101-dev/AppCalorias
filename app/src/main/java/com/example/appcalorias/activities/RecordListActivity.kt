package com.example.appcalorias.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appcalorias.activities.res.recyclerview.RecordAdapter
import com.example.appcalorias.activities.res.toolbar.ToolbarManager
import com.example.appcalorias.databinding.ActivityRecordsListBinding
import com.example.appcalorias.db.AppCaloriesDB
import com.example.appcalorias.db.DatabaseProvider
import com.example.appcalorias.db.model.Record
import com.example.appcalorias.db.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RecordListActivity : AppCompatActivity() {

    private lateinit var b: ActivityRecordsListBinding
    private var recordAdapter: RecordAdapter? = null
    private lateinit var room: AppCaloriesDB
    private var user: User? = null
    private lateinit var toolbarManager: ToolbarManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        b = ActivityRecordsListBinding.inflate(layoutInflater)

        setContentView(b.root)
        ViewCompat.setOnApplyWindowInsetsListener(b.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initProperties()
    }

    private fun initRecyclerView(recordList: List<Record>) {
        b.rvRecordsList.layoutManager = LinearLayoutManager(this)
        recordAdapter = RecordAdapter(recordList) //{}
        b.rvRecordsList.adapter = recordAdapter
    }


    private fun loadRecords() {
        CoroutineScope(Dispatchers.IO).launch {

            val recordsForUser = if (user != null) {
                room.getRecordDAO().getRecordsByUserId(user!!.id)
            } else {
                emptyList()
            }

            withContext(Dispatchers.IO) {
                if (recordAdapter == null) {
                    initRecyclerView(recordsForUser)
                }
            }
        }
    }
    private fun initProperties() {
        room = DatabaseProvider.getDatabase(this)
        user = intent.getSerializableExtra("user_prueba", User::class.java)
        toolbarManager = ToolbarManager(this, b.toolBar)
        toolbarManager.setup()
        loadRecords()
        initActionListeners()
    }

    private fun initActionListeners() {
        b.fabSaveChanges.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {

                val recordsToDelete = recordAdapter?.getRecordsToDelete() ?: emptyList()

                if (recordsToDelete.isNotEmpty()) {
                    // 1. Borrar registros
                    room.getRecordDAO().deleteRecords(recordsToDelete)

                    // 2. Comprobar usuario para posible eliminación
                    if (user != null &&
                        room.getUserDAO().getLastUser() != user!! &&
                        room.getRecordDAO().getRecordsByUserId(user!!.id).isEmpty()
                    ) {
                        room.getUserDAO().deleteUser(user!!)
                    }
                }

                runOnUiThread {
                    loadRecords()
                    finish()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return toolbarManager.createMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return toolbarManager.handleItemClick(item)
    }
}
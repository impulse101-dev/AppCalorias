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

/**
 * Actividad que muestra una lista con los registros del usuario que se le pasa por el intent desde
 * [UserSelectionActivity].
 * Desde esta se accede a cada uno de los registros guardados en la base de datos. Se pueden borrar
 * los registros pulsando en el icono de la papelera que aparece al pulsar el boton de guardar en la
 * esquina inferior derecha. (Esto podria cambiar)
 *
 * @see UserSelectionActivity
 * @property recordAdapter Adaptador para mostrar los registros de usuarios en un RecyclerView.
 * @property room Singleton para el acceso a la base de datos.
 * @property user Usuario que se carga desde el intent y se usa para mostrar sus registros.
 * @property toolbarManager Encargado de gestionar la toolbar de la actividad.
 * @author Adrian Salazar Escoriza
 */
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

    /**
     * Inicializa el RecyclerView con la lista de registros.
     */
    private fun initRecyclerView(recordList: List<Record>) {
        b.rvRecordsList.layoutManager = LinearLayoutManager(this)
        recordAdapter = RecordAdapter(recordList) //{}
        b.rvRecordsList.adapter = recordAdapter
    }


    /**
     * Carga los registros del usuario desde la base de datos.
     */
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

    /**
     * Inicializa las propiedades necesarias para la actividad.
     * Desde aqui se carga el usuario.
     */
    private fun initProperties() {
        room = DatabaseProvider.getDatabase(this)
        user = intent.getSerializableExtra("user_prueba", User::class.java)
        toolbarManager = ToolbarManager(this, b.toolBar)
        toolbarManager.setup()
        loadRecords()
        initActionListeners()
    }

    /**
     * Inicializa los listeners de los botones de la actividad.
     */
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
package com.example.appcalorias.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appcalorias.activities.res.recyclerview.UserAdapter
import com.example.appcalorias.activities.res.toolbar.ToolbarManager
import com.example.appcalorias.databinding.ActivityUserSelectionBinding
import com.example.appcalorias.db.AppCaloriesDB
import com.example.appcalorias.db.DatabaseProvider
import com.example.appcalorias.db.model.User
import com.example.appcalorias.db.model.res.Gender
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * Actividad que permite al usuario ver y gestionar los distintos registros del usuario que se han
 * agregado a la base de datos.
 * Esta actividad debe de mostrar siempre un usuario principal, el cual estara estatico en la parte
 * superior de la pantalla. En caso de que haya mas usuarios, se iran mostrando debajo del usuario principal
 * mediante un recyclerView.
 * Un usuario se borra de la base de datos cuando este no tiene registros asociados y no es el usuario
 * principal.
 * Vease la actividad que se encarga de mostrar la los registros de un usuario [RecordListActivity]
 *
 * @see RecordListActivity
 * @property userAdapter Adaptador para mostrar los usuarios en un RecyclerView.
 * @property room Singleton para el acceso a la base de datos.
 * @property toolbarManager Encargado de gestionar la toolbar de la actividad.
 * @property mainUser Usuario principal de la aplicacion, que se muestra en la parte superior de la pantalla.
 */
class UserSelectionActivity : AppCompatActivity() {

    private lateinit var b : ActivityUserSelectionBinding
    private lateinit var userAdapter: UserAdapter
    private lateinit var room : AppCaloriesDB
    private lateinit var toolbarManager : ToolbarManager
    private var mainUser : User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        b = ActivityUserSelectionBinding.inflate(layoutInflater)

        setContentView(b.root)

        ViewCompat.setOnApplyWindowInsetsListener(b.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initProperties()
    }

    /**
     * Cada vez que la actividad vuelve a primer plano, se recargan los usuarios.
     */
    override fun onResume() {
        loadUsers()
        super.onResume()
    }

    /**
     * Inicializa las propiedades necesarias para la actividad.
     */
    private fun initProperties () {
        room = DatabaseProvider.getDatabase(this)
        toolbarManager = ToolbarManager(this, b.toolBar)
        //mainUser = intent.getSerializableExtra(User.PREFS_USER_ID, User::class.java)
        CoroutineScope(Dispatchers.Main).launch {
            mainUser = room.getUserDAO().getLastUser()
            toolbarManager.setup()
            bindMainUser()
            loadUsers()
        }

    }

    /**
     * Bindea el usuario principal. Es necesario para mostrar sus valores. Ya que este
     * no forma parte del recyclerView.
     */
    private fun bindMainUser () {
        if (mainUser != null) {
            b.itemUsuarioPrincipal.tvUserId.text = mainUser!!.id.toString()
            b.itemUsuarioPrincipal.tvUserDate.text = mainUser!!.dateUpdate.toString()
            if (mainUser!!.gender == Gender.MALE) b.itemUsuarioPrincipal.tvGender.text = "Masculino"
            else {b.itemUsuarioPrincipal.tvGender.text = "Femenino"}
            b.itemUsuarioPrincipal.tvBmr.text = mainUser!!.bmr.toString()
            b.itemUsuarioPrincipal.tvAge.text = mainUser!!.age.toString()
            b.itemUsuarioPrincipal.tvHeight.text = mainUser!!.height.toString()
            b.itemUsuarioPrincipal.tvWeight.text = mainUser!!.weight.toString()

            b.linearLayoutMainUserDiv.setOnClickListener { onClickItem(mainUser!!) }
        } else {
            println("el main user es nulo")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return toolbarManager.handleItemClick(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return toolbarManager.createMenu(menu)
    }

    /**
     * Inicializa el RecyclerView con la lista de usuarios.
     * @param userList Lista de usuarios a mostrar en el RecyclerView.
     */
    private fun initRecyclerView (userList: List<User>) {
        b.rvUsersList.layoutManager = LinearLayoutManager(this)
        userAdapter = UserAdapter(userList) { onClickItem(it) }
        b.rvUsersList.adapter = userAdapter
    }

    /**
     * Metodo que se ejecuta al hacer click en un item del RecyclerView.
     * @param user Usuario sobre el que se ha hecho click.
     */
    private fun onClickItem (user: User) {
        val intent = Intent(this, RecordListActivity::class.java)
        intent.putExtra("user_prueba", user)
        startActivity(intent)
    }

    /**
     * Carga los usuarios de la base de datos y los muestra en el RecyclerView.
     */
    private fun loadUsers() {
        CoroutineScope(Dispatchers.IO).launch {
            var users : List<User>

            if (mainUser != null) {
                users = room.getUserDAO().getAllOtherUsers(mainUser!!.id)
            } else {
                users = emptyList()
            }

            //println("Lista de records por el usuario $users, \tusuario: $mainUser")

            runOnUiThread {
                    initRecyclerView(users)
            }
        }
    }

}
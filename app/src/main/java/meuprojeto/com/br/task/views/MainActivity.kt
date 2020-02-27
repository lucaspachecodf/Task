package meuprojeto.com.br.task.views

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.navigation.findNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.ui.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import meuprojeto.com.br.task.R
import meuprojeto.com.br.task.business.PriorityBusiness
import meuprojeto.com.br.task.business.TaskBusiness
import meuprojeto.com.br.task.constants.TaskConstants
import meuprojeto.com.br.task.repository.PriorityCacheConstants
import meuprojeto.com.br.task.util.SecurityPreferences
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var mSecurityPreferences: SecurityPreferences
    private lateinit var mPriorityBusiness: PriorityBusiness

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        //Remover Título
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)

        val toogle = ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0)
        drawerLayout.setDrawerListener(toogle)
        toogle.syncState()

        val navView: NavigationView = findViewById(R.id.nav_view)

        /*val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_feita,
                R.id.nav_pendente,
                R.id.nav_sair
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)*/

        navView.setNavigationItemSelectedListener(this)

        mSecurityPreferences = SecurityPreferences(this)
        mPriorityBusiness = PriorityBusiness(this)

        startDefaultFragment()
        loadPriorityCache()
        formatUserName()
        formatDate()

    }

    /* override fun onCreateOptionsMenu(menu: Menu): Boolean {
         // Inflate the menu; this adds items to the action bar if it is present.
         menuInflater.inflate(R.menu.main, menu)
         return true
     }*/

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        var fragment: Fragment? = null

        when (item.itemId) {

            R.id.nav_feita -> fragment = TaskListFragment.newInstance(TaskConstants.TASKFILTER.COMPLETE)
            R.id.nav_pendente -> fragment = TaskListFragment.newInstance(TaskConstants.TASKFILTER.TODO)
            R.id.nav_sair -> {
                logout()
                return false
            }
        }

        val fragmentManager = supportFragmentManager

        if (fragment != null) {
            fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit()
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun logout() {

        mSecurityPreferences.removeStroreString(TaskConstants.KEY.USER_ID)
        mSecurityPreferences.removeStroreString(TaskConstants.KEY.USER_NAME)
        mSecurityPreferences.removeStroreString(TaskConstants.KEY.USER_EMAIL)

        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun loadPriorityCache() {
        PriorityCacheConstants.setCache(mPriorityBusiness.getList())
    }

    private fun startDefaultFragment() {
        val fragment: Fragment = TaskListFragment.newInstance(TaskConstants.TASKFILTER.COMPLETE)
        supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit()
    }

    private fun formatUserName() {
        val nomeUserLogado =
            "Olá, ${mSecurityPreferences.getStoreString(TaskConstants.KEY.USER_NAME)}"

        textUser.text = nomeUserLogado

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val header = navigationView.getHeaderView(0)
        val name = header.findViewById<TextView>(R.id.textUserNameMenu)
        val email = header.findViewById<TextView>(R.id.textEmail)

        name.text = mSecurityPreferences.getStoreString(TaskConstants.KEY.USER_NAME)
        email.text = mSecurityPreferences.getStoreString(TaskConstants.KEY.USER_EMAIL)
    }

    private fun formatDate() {
        val calender = Calendar.getInstance()

        val month = arrayListOf(
            "Janeiro",
            "Fevereiro",
            "Março",
            "Abril",
            "Maio",
            "Junho",
            "Julho",
            "Agosto",
            "Setembro",
            "Outubro",
            "Novembro",
            "Dezembro"
        )
        val days = arrayListOf(
            "Domingo",
            "Segunda-Feira",
            "Terça-Feira",
            "Quarta-Feira",
            "Quinta-Feira",
            "Sexta-Feira",
            "Sábado"
        )

        val dateFormated = "${days[calender.get(Calendar.DAY_OF_WEEK) - 1]}, ${calender.get(Calendar.DAY_OF_MONTH)} de ${month[calender.get(Calendar.MONTH)]}"
        textDayOfWeek.text = dateFormated
    }
}

package meuprojeto.com.br.task.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import meuprojeto.com.br.task.R
import meuprojeto.com.br.task.business.UserBusiness
import meuprojeto.com.br.task.constants.TaskConstants
import meuprojeto.com.br.task.util.SecurityPreferences

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mUserBusiness: UserBusiness
    private lateinit var mSecurityPreferences: SecurityPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mUserBusiness = UserBusiness(this)
        mSecurityPreferences = SecurityPreferences(this)

        setListeners()

        verifyLoggedUser()
    }

    private fun verifyLoggedUser() {
        val userId = mSecurityPreferences.getStoreString(TaskConstants.KEY.USER_ID)
        val name = mSecurityPreferences.getStoreString(TaskConstants.KEY.USER_NAME)

        if (userId != "" && name != "") {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun setListeners() {
        btnLogin.setOnClickListener(this)
        textRegister.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnLogin -> {
                handleLogin()
            }
            R.id.textRegister -> {
                startActivity(Intent(this, RegisterActivity::class.java))
            }
        }
    }

    private fun handleLogin() {
        val email = editEmail.text.toString()
        val password = editPassword.text.toString()

        if (mUserBusiness.login(email, password)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else
            Toast.makeText(
                this,
                getString(R.string.usuario_senha_incorretos),
                Toast.LENGTH_LONG
            ).show()
    }
}

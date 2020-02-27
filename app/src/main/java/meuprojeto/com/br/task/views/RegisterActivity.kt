package meuprojeto.com.br.task.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_register.*
import meuprojeto.com.br.task.R
import meuprojeto.com.br.task.business.UserBusiness
import meuprojeto.com.br.task.util.ValidationException
import java.lang.Exception

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mUserBusiness: UserBusiness

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setListerners()

        mUserBusiness = UserBusiness(this)
    }

    private fun setListerners() {
        btnSalvar.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.btnSalvar -> {
                salvar()
            }
        }
    }

    private fun salvar() {

        try {
            val name = editNome.text.toString()
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()

            mUserBusiness.insert(name, email, password)

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        catch (e: ValidationException){
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
        catch (e: Exception){
            Toast.makeText(this, getString(R.string.erro_default), Toast.LENGTH_LONG).show()
        }
    }
}

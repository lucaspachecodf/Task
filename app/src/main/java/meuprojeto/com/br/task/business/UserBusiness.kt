package meuprojeto.com.br.task.business

import android.content.Context
import meuprojeto.com.br.task.R
import meuprojeto.com.br.task.constants.TaskConstants
import meuprojeto.com.br.task.entities.UserEntity
import meuprojeto.com.br.task.repository.UserRepository
import meuprojeto.com.br.task.util.SecurityPreferences
import meuprojeto.com.br.task.util.ValidationException
import java.lang.Exception

class UserBusiness(val context: Context) {
    private val mUserRepository: UserRepository = UserRepository.getInstance(context)
    private val mSecurityPreferences: SecurityPreferences = SecurityPreferences(context)

    fun login(email: String, password: String): Boolean {
        val user: UserEntity? = mUserRepository.get(email, password)

        return if (user != null) {
            saveShared(user.id, user.name, user.email)
            true
        } else
            false
    }

    fun insert(name: String, email: String, password: String) {

        try {
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                throw ValidationException(context.getString(R.string.informe_todos_campos))
            }

            if (mUserRepository.isEmailExistent(email)) {
                throw ValidationException(context.getString(R.string.email_cadastrado))
            }

            val userId = mUserRepository.insert(name, email, password)

            saveShared(userId, name, email)

        } catch (e: Exception) {
            throw e
        }
    }

    //Salvar dados do usuário no sharedPreference
    fun saveShared(userId: Int, name: String, email: String) {
        mSecurityPreferences.storeString(TaskConstants.KEY.USER_ID, userId.toString())
        mSecurityPreferences.storeString(TaskConstants.KEY.USER_NAME, name)
        mSecurityPreferences.storeString(TaskConstants.KEY.USER_EMAIL, email)
    }

}
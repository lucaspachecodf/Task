package meuprojeto.com.br.task.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import meuprojeto.com.br.task.constants.DataBaseConstants
import meuprojeto.com.br.task.entities.UserEntity
import java.lang.Exception
import java.lang.reflect.Executable

class UserRepository private constructor(context: Context) {

    private var mTaskDataBaseHelper: TaskDataBaseHelper = TaskDataBaseHelper(context)

    companion object {
        fun getInstance(context: Context): UserRepository {
            if (INSTANCE == null) {
                INSTANCE = UserRepository(context)
            }

            return INSTANCE as UserRepository
        }

        private var INSTANCE: UserRepository? = null
    }

    fun insert(name: String, email: String, password: String): Int {
        val db = mTaskDataBaseHelper.writableDatabase

        val insertValues = ContentValues()
        insertValues.put(DataBaseConstants.USER.COLUMNS.NAME, name)
        insertValues.put(DataBaseConstants.USER.COLUMNS.EMAIL, email)
        insertValues.put(DataBaseConstants.USER.COLUMNS.PASSWORD, password)

        return db.insert(DataBaseConstants.USER.TABLE_NAME, null, insertValues).toInt()
    }

    fun isEmailExistent(email: String): Boolean {

        var ret: Boolean

        try {
            val cursor: Cursor
            val db = mTaskDataBaseHelper.readableDatabase

            val projection = arrayOf(DataBaseConstants.USER.COLUMNS.ID)

            val selection = "${DataBaseConstants.USER.COLUMNS.EMAIL} = ?"
            val selectionArgs = arrayOf(email)

            //cursor = db.query(DataBaseConstants.USER.TABLE_NAME, projection, selection, selectionArgs, null, null, null)

            cursor = db.rawQuery(
                "SELECT * FROM ${DataBaseConstants.USER.TABLE_NAME} WHERE ${DataBaseConstants.USER.COLUMNS.EMAIL} = '${email}'",
                null
            )

            ret = cursor.count > 0

            cursor.close()
        } catch (e: Exception) {
            throw e
        }

        return ret
    }

    fun get(email: String, password: String): UserEntity? {
        var userEntity: UserEntity? = null

        try {
            val cursor: Cursor
            val db = mTaskDataBaseHelper.readableDatabase

            val selectionArgs = arrayOf(email, password)
            cursor = db.rawQuery(
                "SELECT * FROM ${DataBaseConstants.USER.TABLE_NAME} WHERE ${DataBaseConstants.USER.COLUMNS.EMAIL} = ? AND ${DataBaseConstants.USER.COLUMNS.PASSWORD} = ?",
                selectionArgs
            )

            if (cursor.count > 0) {
                cursor.moveToFirst()

                val userId = cursor.getInt(cursor.getColumnIndex(DataBaseConstants.USER.COLUMNS.ID))
                val name =
                    cursor.getString(cursor.getColumnIndex(DataBaseConstants.USER.COLUMNS.NAME))
                val email =
                    cursor.getString(cursor.getColumnIndex(DataBaseConstants.USER.COLUMNS.EMAIL))

                userEntity = UserEntity(userId, name, email)
            }

            cursor.close()

        } catch (e: Exception) {
            return userEntity
        }

        return userEntity
    }
}
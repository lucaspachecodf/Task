package meuprojeto.com.br.task.repository

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import meuprojeto.com.br.task.constants.DataBaseConstants

class TaskDataBaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private val DATABASE_VERSION: Int = 1
        private val DATABASE_NAME: String = "task.db"
    }

    private val createTableUser = """ CREATE TABLE ${DataBaseConstants.USER.TABLE_NAME} 
        (${DataBaseConstants.USER.COLUMNS.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${DataBaseConstants.USER.COLUMNS.NAME} TEXT,
        ${DataBaseConstants.USER.COLUMNS.EMAIL} TEXT,
        ${DataBaseConstants.USER.COLUMNS.PASSWORD} TEXT);
    """

    private val createTablePriority = """ CREATE TABLE ${DataBaseConstants.PRIORITY.TABLE_NAME} 
        (${DataBaseConstants.PRIORITY.COLUMNS.ID} INTEGER PRIMARY KEY,
        ${DataBaseConstants.PRIORITY.COLUMNS.DESCRIPTION} TEXT);
    """

    private val createTableTask = """ CREATE TABLE ${DataBaseConstants.TASK.TABLE_NAME} 
        (${DataBaseConstants.TASK.COLUMNS.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${DataBaseConstants.TASK.COLUMNS.USERID} INTEGER,
        ${DataBaseConstants.TASK.COLUMNS.PRIORITYID} INTEGER,
        ${DataBaseConstants.TASK.COLUMNS.DESCRIPTION} TEXT,
        ${DataBaseConstants.TASK.COLUMNS.COMPLETE} INTEGER,
        ${DataBaseConstants.TASK.COLUMNS.DUEDATE} TEXT);
    """

    private val insertPriorities = "INSERT INTO ${DataBaseConstants.PRIORITY.TABLE_NAME} VALUES (1, 'Baixa'), (2, 'Média'), (3, 'Alta'), (4, 'Critica')"

    private val deleteTableUser = "drop table if exists ${DataBaseConstants.USER.TABLE_NAME}"
    private val deleteTablePriority = "drop table if exists ${DataBaseConstants.PRIORITY.TABLE_NAME}"
    private val deleteTableTask = "drop table if exists ${DataBaseConstants.TASK.TABLE_NAME}"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createTableUser)
        db.execSQL(createTablePriority)
        db.execSQL(createTableTask)

        db.execSQL(insertPriorities)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(deleteTableUser)
        db.execSQL(deleteTablePriority)
        db.execSQL(deleteTableTask)

        db.execSQL(createTableUser)
        db.execSQL(createTablePriority)
        db.execSQL(createTableTask)
    }

}
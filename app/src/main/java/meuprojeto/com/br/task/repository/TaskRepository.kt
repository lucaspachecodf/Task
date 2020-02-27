package meuprojeto.com.br.task.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import meuprojeto.com.br.task.constants.DataBaseConstants
import meuprojeto.com.br.task.entities.TaskEntity

class TaskRepository private constructor(context: Context) {

    private var mTaskDataBaseHelper: TaskDataBaseHelper = TaskDataBaseHelper(context)

    companion object {
        fun getInstance(context: Context): TaskRepository {
            if (INSTANCE == null) {
                INSTANCE = TaskRepository(context)
            }

            return INSTANCE as TaskRepository
        }

        private var INSTANCE: TaskRepository? = null
    }

    fun getList(userId: Int, taskFilter: Int): MutableList<TaskEntity> {

        val list = mutableListOf<TaskEntity>()

        try {
            val cursor: Cursor
            val db = mTaskDataBaseHelper.readableDatabase

            //val selectionArgs = arrayOf(userId.toString(), taskFilter.toString())
            cursor = db.rawQuery("SELECT * FROM ${DataBaseConstants.TASK.TABLE_NAME} WHERE ${DataBaseConstants.TASK.COLUMNS.USERID} = $userId AND ${DataBaseConstants.TASK.COLUMNS.COMPLETE} = $taskFilter",null)

            if (cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val id = cursor.getInt(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.ID))
                    val priorityId =
                        cursor.getInt(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.PRIORITYID))
                    val description =
                        cursor.getString(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.DESCRIPTION))
                    val dueDate =
                        cursor.getString(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.DUEDATE))
                    val complete =
                        (cursor.getInt(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.COMPLETE)) == 1)



                    list.add(TaskEntity(id, userId, priorityId, description, dueDate, complete))
                }
            }
            cursor.close()
        } catch (e: Exception) {
            return list
        }

        return list
    }

    fun get(id: Int): TaskEntity? {
        var taskEntity: TaskEntity? = null

        try {
            val cursor: Cursor
            val db = mTaskDataBaseHelper.readableDatabase

            val selectionArgs = arrayOf(id.toString())
            cursor = db.rawQuery(
                "SELECT * FROM ${DataBaseConstants.TASK.TABLE_NAME} WHERE ${DataBaseConstants.TASK.COLUMNS.ID} = ?",
                selectionArgs
            )

            if (cursor.count > 0) {
                cursor.moveToFirst()

                val id = cursor.getInt(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.ID))
                val userId =
                    cursor.getInt(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.USERID))
                val priorityId =
                    cursor.getInt(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.PRIORITYID))
                val description =
                    cursor.getString(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.DESCRIPTION))
                val dueDate =
                    cursor.getString(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.DUEDATE))
                val complete =
                    (cursor.getInt(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.COMPLETE)) == 1)

                taskEntity = TaskEntity(id, userId, priorityId, description, dueDate, complete)
            }

            cursor.close()

        } catch (e: java.lang.Exception) {
            return taskEntity
        }

        return taskEntity
    }

    fun insert(task: TaskEntity) {

        try {
            val db = mTaskDataBaseHelper.writableDatabase

            val complete: Int = if (task.complete) 1 else 0

            val insertValues = ContentValues()
            insertValues.put(DataBaseConstants.TASK.COLUMNS.USERID, task.userId)
            insertValues.put(DataBaseConstants.TASK.COLUMNS.PRIORITYID, task.priorityId)
            insertValues.put(DataBaseConstants.TASK.COLUMNS.DESCRIPTION, task.description)
            insertValues.put(DataBaseConstants.TASK.COLUMNS.DUEDATE, task.dueDate)
            insertValues.put(DataBaseConstants.TASK.COLUMNS.COMPLETE, complete)

            db.insert(DataBaseConstants.TASK.TABLE_NAME, null, insertValues)

        } catch (e: Exception) {
            throw e
        }
    }

    fun update(task: TaskEntity) {

        try {
            val db = mTaskDataBaseHelper.writableDatabase

            val complete: Int = if (task.complete) 1 else 0

            val updateValues = ContentValues()
            updateValues.put(DataBaseConstants.TASK.COLUMNS.USERID, task.userId)
            updateValues.put(DataBaseConstants.TASK.COLUMNS.PRIORITYID, task.priorityId)
            updateValues.put(DataBaseConstants.TASK.COLUMNS.DESCRIPTION, task.description)
            updateValues.put(DataBaseConstants.TASK.COLUMNS.DUEDATE, task.dueDate)
            updateValues.put(DataBaseConstants.TASK.COLUMNS.COMPLETE, complete)

            val selectionArgs = arrayOf(task.id.toString())

            db.update(
                DataBaseConstants.TASK.TABLE_NAME,
                updateValues,
                "${DataBaseConstants.TASK.COLUMNS.ID} = ?",
                selectionArgs
            )
        } catch (e: Exception) {
            throw e
        }

    }

    fun delete(id: Int) {

        try {
            val db = mTaskDataBaseHelper.writableDatabase

            val whereArgs = arrayOf(id.toString())
            db.delete(DataBaseConstants.TASK.TABLE_NAME,"${DataBaseConstants.TASK.COLUMNS.ID} = ?",whereArgs)
        } catch (e: Exception){
            throw e
        }
    }
}
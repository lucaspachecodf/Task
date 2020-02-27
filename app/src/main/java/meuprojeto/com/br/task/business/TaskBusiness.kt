package meuprojeto.com.br.task.business

import android.content.Context
import meuprojeto.com.br.task.constants.TaskConstants
import meuprojeto.com.br.task.entities.TaskEntity
import meuprojeto.com.br.task.repository.TaskRepository
import meuprojeto.com.br.task.util.SecurityPreferences

class TaskBusiness(context: Context) {
    private val mTaskRepository: TaskRepository = TaskRepository.getInstance(context)
    private val mSecurityPreferences: SecurityPreferences = SecurityPreferences(context)

    fun getList(taskFilter: Int): MutableList<TaskEntity> {
        val userId = mSecurityPreferences.getStoreString(TaskConstants.KEY.USER_ID)!!.toInt()

        return mTaskRepository.getList(userId, taskFilter)
    }

    fun insert(task: TaskEntity) = mTaskRepository.insert(task)

    fun delete(id: Int) = mTaskRepository.delete(id)

    fun get(id: Int) = mTaskRepository.get(id)

    fun update(task: TaskEntity) = mTaskRepository.update(task)

    fun complete(taskId: Int, complete: Boolean) {
        val task = mTaskRepository.get(taskId)

        if(task != null){
            task.complete = complete

            mTaskRepository.update(task)
        }
    }

}
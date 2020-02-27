package meuprojeto.com.br.task.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import meuprojeto.com.br.task.R
import meuprojeto.com.br.task.entities.OnTaskListFragmentInteractionListener
import meuprojeto.com.br.task.entities.TaskEntity
import meuprojeto.com.br.task.viewHolder.TaskViewHolder

class TaskListAdapter(val taskList: List<TaskEntity>, val listener: OnTaskListFragmentInteractionListener): RecyclerView.Adapter<TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val context = parent?.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.row_task_list, parent, false)

        return TaskViewHolder(view, listener, context)
    }

    override fun getItemCount(): Int {
        return taskList.count()
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]

        holder.bindData(task)
    }

}
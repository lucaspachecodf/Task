package meuprojeto.com.br.task.viewHolder

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import meuprojeto.com.br.task.R
import meuprojeto.com.br.task.entities.OnTaskListFragmentInteractionListener
import meuprojeto.com.br.task.entities.TaskEntity
import meuprojeto.com.br.task.repository.PriorityCacheConstants

class TaskViewHolder(
    itemView: View,
    val listener: OnTaskListFragmentInteractionListener,
    val context: Context
) : RecyclerView.ViewHolder(itemView) {

    private val mTextDescription: TextView = itemView.findViewById(R.id.textDescription)
    private val mTextPriority: TextView = itemView.findViewById(R.id.textPriority)
    private val mTextDueDate: TextView = itemView.findViewById(R.id.textDueDate)
    private val mImageTask: ImageView = itemView.findViewById(R.id.imageTask)

    fun bindData(task: TaskEntity) {
        mTextDescription.text = task.description
        mTextPriority.text = PriorityCacheConstants.getPriorityDescription(task.priorityId)
        mTextDueDate.text = task.dueDate

        if (task.complete)
            mImageTask.setImageResource(R.drawable.ic_done)

        //Evento de clique para edição
        mTextDescription.setOnClickListener {
            listener.onListClick(task.id)
        }

        //Evento de clique para remoção
        mTextDescription.setOnLongClickListener {
            showConfimationDialog(task)
            true
        }

        mImageTask.setOnClickListener {
            if(task.complete) {
                listener.onUncompleteClick(task.id)
            } else {
                listener.onCompleteClick(task.id)
            }
        }


    }

    private fun showConfimationDialog(task: TaskEntity) {

        AlertDialog.Builder(context).setTitle("Remoção de tarefa")
            .setMessage("Deseja remover ${task.description}?")
            .setIcon(R.drawable.ic_delete_black_24dp)
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Remover") { _: DialogInterface, _ -> listener.onDeleteClick(task.id)
            }.show()

    }
}
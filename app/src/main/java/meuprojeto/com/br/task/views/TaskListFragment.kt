package meuprojeto.com.br.task.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import meuprojeto.com.br.task.R
import meuprojeto.com.br.task.adapter.TaskListAdapter
import meuprojeto.com.br.task.business.TaskBusiness
import meuprojeto.com.br.task.constants.TaskConstants
import meuprojeto.com.br.task.entities.OnTaskListFragmentInteractionListener
import meuprojeto.com.br.task.util.SecurityPreferences

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private var mTaskFilter: Int = 0

class TaskListFragment : Fragment(), View.OnClickListener {

    private lateinit var mContext: Context
    private lateinit var mRecyclerTastList: RecyclerView
    private lateinit var mTaskBusiness: TaskBusiness
    private lateinit var mSecurityPreferences: SecurityPreferences
    private lateinit var mListener: OnTaskListFragmentInteractionListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mTaskFilter = it.getInt(TaskConstants.TASKFILTER.KEY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_task_list, container, false)

        rootView.findViewById<FloatingActionButton>(R.id.floatAddTask).setOnClickListener(this)


        mContext = rootView.context
        mTaskBusiness = TaskBusiness(mContext)
        mSecurityPreferences = SecurityPreferences(mContext)

        mListener = object : OnTaskListFragmentInteractionListener {
            override fun onListClick(taskId: Int) {

                val bundle = Bundle()
                bundle.putInt(TaskConstants.BUNDLE.TASKID, taskId)

                val intent = Intent(mContext, TaskFormActivity::class.java)
                intent.putExtras(bundle)

                startActivity(intent)
            }

            override fun onDeleteClick(taskId: Int) {
                mTaskBusiness.delete(taskId)
                loadTasks()

                Toast.makeText(mContext,getString(R.string.tarefa_removida_sucesso), Toast.LENGTH_LONG).show()
            }

            override fun onUncompleteClick(taskId: Int) {
                mTaskBusiness.complete(taskId, false)
                loadTasks()
            }

            override fun onCompleteClick(taskId: Int) {
                mTaskBusiness.complete(taskId, true)
                loadTasks()
            }
        }

        // 1 Obter o elemento
        mRecyclerTastList = rootView.findViewById(R.id.recyclerTaskList)

        // 2 Definir um adapter com os itens de listagem
        mRecyclerTastList.adapter = TaskListAdapter(mutableListOf(), mListener)

        // 3 Definir um layout
        mRecyclerTastList.layoutManager = LinearLayoutManager(mContext)

        return rootView
    }

    override fun onResume() {
        super.onResume()

        loadTasks()
    }

    private fun loadTasks() {
        mRecyclerTastList.adapter = TaskListAdapter(mTaskBusiness.getList(mTaskFilter), mListener)
    }

    companion object {

        fun newInstance(taskFilter: Int): TaskListFragment {

            val args = Bundle()
            args.putInt(TaskConstants.TASKFILTER.KEY, taskFilter)

            val fragment = TaskListFragment()
            fragment.arguments = args

            return fragment
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.floatAddTask -> {
                startActivity(Intent(mContext, TaskFormActivity::class.java))
            }
        }
    }
}

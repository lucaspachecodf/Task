package meuprojeto.com.br.task.views

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_task_form.*
import meuprojeto.com.br.task.R
import meuprojeto.com.br.task.business.PriorityBusiness
import meuprojeto.com.br.task.business.TaskBusiness
import meuprojeto.com.br.task.constants.TaskConstants
import meuprojeto.com.br.task.entities.PriorityEntity
import meuprojeto.com.br.task.entities.TaskEntity
import meuprojeto.com.br.task.util.SecurityPreferences
import java.text.SimpleDateFormat
import java.util.*

class TaskFormActivity : AppCompatActivity(), View.OnClickListener,
    DatePickerDialog.OnDateSetListener {

    private lateinit var mPriorityBusiness: PriorityBusiness
    private lateinit var mTaskBusiness: TaskBusiness
    private val mSimpleDateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")

    private var mListaPriorityEntity: MutableList<PriorityEntity> = mutableListOf()
    private var mListaPrioritiesId: MutableList<Int> = mutableListOf()
    private lateinit var mSecurityPreferences: SecurityPreferences
    private var mTaskId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_form)

        mPriorityBusiness = PriorityBusiness(this)
        mTaskBusiness = TaskBusiness(this)

        mSecurityPreferences = SecurityPreferences(this)

        loadPriorities()
        setListeners()
        loadDataFromActivity()
    }

    private fun loadDataFromActivity() {
        val bundle = intent.extras
        if (bundle != null) {
            mTaskId = bundle.getInt(TaskConstants.BUNDLE.TASKID)

            val task = mTaskBusiness.get(mTaskId)

            editDescription.setText(task?.description)
            buttonDate.text = task?.dueDate
            checkComplete.isChecked = task?.complete!!
            spinnerPriority.setSelection(getPriorityIndex(task?.priorityId))

            buttonSave.text = getString(R.string.atualizar_tarefa)
        }
    }

    private fun getPriorityIndex(id: Int): Int {
        var index = 0

        for (i in 0..mListaPriorityEntity.size) {
            if (mListaPriorityEntity[i].id == id) {
                index = i
                break
            }
        }

        return index
    }

    private fun setListeners() {
        buttonDate.setOnClickListener(this)
        buttonSave.setOnClickListener(this)
    }

    private fun loadPriorities() {
        mListaPriorityEntity = mPriorityBusiness.getList()
        val listaPriorities = mListaPriorityEntity.map { it.description }
        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            listaPriorities
        )

        mListaPrioritiesId = mListaPriorityEntity.map { it.id }.toMutableList()


        spinnerPriority.adapter = adapter
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.buttonDate -> {
                openDatePickerDialog()
            }
            R.id.buttonSave -> {
                save()
            }
        }
    }

    private fun save() {
        try {


            val description = editDescription.text.toString()
            val priorityId = mListaPrioritiesId[spinnerPriority.selectedItemPosition]
            val complete = checkComplete.isChecked
            val dueDate = buttonDate.text.toString()
            val userId =
                mSecurityPreferences.getStoreString(TaskConstants.KEY.USER_ID)!!.toInt()

            val taskEntity = TaskEntity(mTaskId, userId, priorityId, description, dueDate, complete)

            if (mTaskId == 0) {
                mTaskBusiness.insert(taskEntity)
                Toast.makeText(this, getString(R.string.incluida_sucesso), Toast.LENGTH_LONG).show()
            } else {
                mTaskBusiness.update(taskEntity)
                Toast.makeText(this, getString(R.string.tarefa_alterada_sucesso), Toast.LENGTH_LONG).show()
            }

            finish()

        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.erro_default), Toast.LENGTH_LONG).show()
        }
    }

    private fun openDatePickerDialog() {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val dayOfMonth = c.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, this, year, month, dayOfMonth).show()
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()

        calendar.set(year, month, dayOfMonth)

        buttonDate.text = mSimpleDateFormat.format(calendar.time)
    }
}

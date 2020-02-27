package meuprojeto.com.br.task.business

import android.content.Context
import meuprojeto.com.br.task.entities.PriorityEntity
import meuprojeto.com.br.task.repository.PriorityRepository

class PriorityBusiness(context: Context) {

    private val mPriorityRepository: PriorityRepository = PriorityRepository.getInstance(context)

    fun getList() : MutableList<PriorityEntity> = mPriorityRepository.getList()


}
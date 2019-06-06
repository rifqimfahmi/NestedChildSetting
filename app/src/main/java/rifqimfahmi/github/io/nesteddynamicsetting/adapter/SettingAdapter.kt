package rifqimfahmi.github.io.nesteddynamicsetting.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kotlinx.android.synthetic.main.child_setting.view.sw_child_setting
import kotlinx.android.synthetic.main.section_title.view.tv_title
import kotlinx.android.synthetic.main.setting.view.rv_child
import kotlinx.android.synthetic.main.setting.view.sw_setting
import rifqimfahmi.github.io.nesteddynamicsetting.R
import rifqimfahmi.github.io.nesteddynamicsetting.adapter.ChildSettingAdapter.ChildListener
import rifqimfahmi.github.io.nesteddynamicsetting.adapter.ChildSettingViewHolder.ChildViewListener
import rifqimfahmi.github.io.nesteddynamicsetting.model.EmailSectionSetting
import rifqimfahmi.github.io.nesteddynamicsetting.model.Setting
import rifqimfahmi.github.io.nesteddynamicsetting.model.Visitable

class SettingAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val data: ArrayList<Visitable> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            ViewType.SECTION_TITLE -> SectionTitleViewHolder.createViewHolder(parent)
            ViewType.SECTION_SETTING -> SettingViewHolder.createViewHolder(parent)
            else -> throw IllegalArgumentException("Unidentified viewtype: $viewType")
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is SectionTitleViewHolder -> holder.bind(data[position] as EmailSectionSetting)
            is SettingViewHolder -> holder.bind(data[position] as Setting)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is EmailSectionSetting -> ViewType.SECTION_TITLE
            is Setting -> ViewType.SECTION_SETTING
            else -> super.getItemViewType(position)
        }
    }

    fun updateData(data: ArrayList<Visitable>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    object ViewType {
        val SECTION_TITLE = 1
        val SECTION_SETTING = 2
    }
}

class SectionTitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(data: EmailSectionSetting) {
        with(itemView) {
            tv_title.text = data.section
        }
    }

    companion object {
        fun createViewHolder(parent: ViewGroup): SectionTitleViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.section_title, parent, false)
            return SectionTitleViewHolder(view)
        }
    }
}

class SettingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), ChildListener {

    override fun onAllSameChildCheckState(status: Boolean) {
        itemView.sw_setting.isChecked = status
    }

    fun bind(setting: Setting) {
        with(itemView) {
            sw_setting.text = setting.name
            sw_setting.isChecked = setting.status

            if (setting.hasChildSetting()) {
                displayChildSetting(setting.childSettings)
            }

            sw_setting.setOnClickListener {
                val newStatus = sw_setting.isChecked
                setting.status = newStatus
                if (setting.hasChildSetting()) {
                    adjustChildCheckedStatus(newStatus)
                }
            }
        }
    }

    private fun adjustChildCheckedStatus(newStatus: Boolean) {
        (itemView.rv_child.adapter as ChildSettingAdapter).adjustAllCheckedStatus(newStatus)
    }

    private fun displayChildSetting(childSettings: List<Setting>) {
        with(itemView) {
            rv_child.visibility = View.VISIBLE
            rv_child.layoutManager = LinearLayoutManager(context)
            rv_child.setHasFixedSize(true)
            rv_child.adapter = ChildSettingAdapter(childSettings, this@SettingViewHolder)
        }
    }

    companion object {
        fun createViewHolder(parent: ViewGroup): SettingViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.setting, parent, false)
            return SettingViewHolder(view)
        }
    }
}

class ChildSettingAdapter(private val childSettings: List<Setting>, private val childListener: ChildListener) :
    RecyclerView.Adapter<ChildSettingViewHolder>(), ChildViewListener {

    interface ChildListener {
        fun onAllSameChildCheckState(status: Boolean)
    }

    override fun checkOtherSiblingStatus(newStatus: Boolean) {
        var isAllTheSame = true
        for (childSetting in childSettings) {
            if (childSetting.status != newStatus) {
                isAllTheSame = false
                break
            }
        }

        if (isAllTheSame) {
            childListener.onAllSameChildCheckState(newStatus)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildSettingViewHolder {
        return ChildSettingViewHolder.createViewHolder(parent, this)
    }

    override fun getItemCount(): Int {
        return childSettings.size
    }

    override fun onBindViewHolder(holder: ChildSettingViewHolder, position: Int) {
        holder.bind(childSettings[position])
    }

    fun adjustAllCheckedStatus(newStatus: Boolean) {
        for (childSetting in childSettings) {
            childSetting.status = newStatus
        }
        notifyDataSetChanged()
    }
}

class ChildSettingViewHolder(itemView: View, private val childViewListener: ChildViewListener) :
    RecyclerView.ViewHolder(itemView) {

    interface ChildViewListener {
        fun checkOtherSiblingStatus(newStatus: Boolean)
    }

    fun bind(setting: Setting) {
        with(itemView) {
            sw_child_setting.text = setting.name
            sw_child_setting.isChecked = setting.status

            sw_child_setting.setOnClickListener {
                val newStatus = sw_child_setting.isChecked
                setting.status = newStatus
                childViewListener.checkOtherSiblingStatus(newStatus)
            }
        }
    }

    companion object {
        fun createViewHolder(parent: ViewGroup, childViewListener: ChildViewListener): ChildSettingViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.child_setting, parent, false)
            return ChildSettingViewHolder(view, childViewListener)
        }
    }
}
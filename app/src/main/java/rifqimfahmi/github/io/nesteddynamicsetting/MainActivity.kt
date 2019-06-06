package rifqimfahmi.github.io.nesteddynamicsetting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import rifqimfahmi.github.io.nesteddynamicsetting.adapter.SettingAdapter
import rifqimfahmi.github.io.nesteddynamicsetting.model.NotificationSettingHelper

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerViewSetting: RecyclerView
    private lateinit var settingAdapter: SettingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        recyclerViewSetting = findViewById(R.id.rv_setting)
        with (recyclerViewSetting) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            settingAdapter = SettingAdapter().also { adapter = it }
        }

        val data = NotificationSettingHelper.createDummyResponse()
        settingAdapter.updateData(data)
    }
}

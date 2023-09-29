import com.sutech.photoeditor.R
import com.sutech.photoeditor.base.BaseFragment
import com.sutech.photoeditor.view.setting.initOnClick
import kotlinx.android.synthetic.main.toolbar_base.*


class SettingFrag : BaseFragment(R.layout.fragment_setting) {
    override fun onCreatedView() {
        tvTitleToolbar.setText(R.string.setting)
        initOnClick()
    }
}
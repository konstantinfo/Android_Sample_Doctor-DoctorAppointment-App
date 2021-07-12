package com.telemed.views.activities

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.telemed.R
import com.telemed.adapters.BookAppointmentAdapter
import com.telemed.api.request.AppointmentRequest
import com.telemed.base.BaseActivity
import com.telemed.databinding.ActivityBookAppointmentBinding
import com.telemed.utils.Constants


class BookAppointmentActivity : BaseActivity() {

    lateinit var binding: ActivityBookAppointmentBinding
    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager
    var consultantId = ""
    var request: AppointmentRequest = AppointmentRequest()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_book_appointment)
        if (intent.hasExtra(Constants.INTENT_KEY_DATA_TWO)) {
            consultantId = intent.getStringExtra(Constants.INTENT_KEY_DATA_TWO)!!
        }
        setUpToolBar()
        setUpTabBar()
        setUpAdapter()
        //disable tablayout click
        val tabStrip = binding.tabLayout.getChildAt(0) as LinearLayout
        for (i in 0 until tabStrip.childCount) {
            tabStrip.getChildAt(i).setOnTouchListener { v, event -> true }
        }
        //disable view pager swipe
        binding.viewPager.setPagingEnabled(false)

    }

    private fun setUpAdapter() {
        val adapter = BookAppointmentAdapter(
            this, supportFragmentManager,
            binding.tabLayout.tabCount, consultantId
        )
        binding.viewPager.adapter = adapter
        binding.viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout))
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager.currentItem = tab.position
                val linearLayout =
                    (binding.tabLayout.getChildAt(0) as ViewGroup).getChildAt(tab.position) as LinearLayout
                val tabTextView = linearLayout.getChildAt(1) as TextView
                tabTextView.setTypeface(null, Typeface.BOLD)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val linearLayout =
                    (binding.tabLayout.getChildAt(0) as ViewGroup).getChildAt(tab.position) as LinearLayout
                val tabTextView = linearLayout.getChildAt(1) as TextView
                tabTextView.setTypeface(null, Typeface.NORMAL)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun setUpTabBar() {
        binding.tabLayout.addTab(
            binding.tabLayout.newTab()
                .setText(resources.getString(R.string.patient_detail_and_documents))
        )
        binding.tabLayout.addTab(
            binding.tabLayout.newTab()
                .setText(resources.getString(R.string.schedule_and_communication))
        )
        binding.tabLayout.addTab(
            binding.tabLayout.newTab().setText(resources.getString(R.string.payment_details))
        )
        binding.tabLayout.tabGravity = TabLayout.GRAVITY_FILL
    }

    private fun setUpToolBar() {
        binding.toolBar.tvTitle.text = resources.getString(R.string._book_appointment)
        binding.toolBar.ivBack.background = ContextCompat.getDrawable(this, R.drawable.ic_cross)
        binding.toolBar.ivBack.visibility = View.VISIBLE

    }

    fun selectPage(pageIndex: Int) {
        request.consultantId = consultantId
        binding.tabLayout.setScrollPosition(pageIndex, 0f, true)
        binding.viewPager.currentItem = pageIndex
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ivBack -> {
                yesNoOnInfo(
                    resources.getString(R.string.abort_consultation),
                    resources.getString(R.string.abort_msg),
                    "No",
                    "Yes",
                    this,
                    0
                )

            }


        }
    }

    override fun onDialogClick(isOk: Boolean, tag: Int) {
        super.onDialogClick(isOk, tag)
        if (isOk) {
            finish()
            endAct(this)
        }
    }

    override fun onBackPressed() {
        yesNoOnInfo(
            resources.getString(R.string.abort_consultation),
            resources.getString(R.string.abort_msg),
            "No",
            "Yes",
            this,
            0
        )

    }


}

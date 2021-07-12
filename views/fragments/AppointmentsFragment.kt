package com.telemed.views.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.telemed.R
import com.telemed.adapters.AppointmentsAdapter
import com.telemed.base.BaseFragment
import com.telemed.databinding.FragmentAppointmentsBinding


class AppointmentsFragment : BaseFragment(), View.OnClickListener {
    lateinit var binding: FragmentAppointmentsBinding
    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager
    var profileStep = 2
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_appointments, container, false)
        setUpToolBar()
        setUpTabBar()
        setUpAdapter()

        return binding.root
    }

    private fun setUpToolBar() {
        binding.tvTitle.text = resources.getString(R.string.appointments)
        binding.ivBack.visibility = View.INVISIBLE

    }

    private fun setUpTabBar() {
        binding.tabLayout.addTab(
            binding.tabLayout.newTab().setText(resources.getString(R.string.active))
        )
        binding.tabLayout.addTab(
            binding.tabLayout.newTab().setText(resources.getString(R.string.past))
        )
        binding.tabLayout.tabGravity = TabLayout.GRAVITY_FILL
    }

    private fun setUpAdapter() {
        val adapter = AppointmentsAdapter(
            requireContext(),
            childFragmentManager,
            binding.tabLayout.tabCount
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

    fun selectPage(pageIndex: Int) {
        binding.tabLayout.setScrollPosition(pageIndex, 0f, true)
        binding.viewPager.currentItem = pageIndex
    }

}




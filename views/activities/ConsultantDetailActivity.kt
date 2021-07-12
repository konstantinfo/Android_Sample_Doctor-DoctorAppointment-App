package com.telemed.views.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.telemed.R
import com.telemed.api.response.JsonArrayResponse
import com.telemed.api.response.ProfileResponse
import com.telemed.api.response.Resource
import com.telemed.base.BaseActivity
import com.telemed.databinding.ActivityConsultantDetailBinding
import com.telemed.viewmodels.BookingViewModel
import com.telemed.utils.Constants
import retrofit2.Response

class ConsultantDetailActivity : BaseActivity() {

    lateinit var binding: ActivityConsultantDetailBinding
    lateinit var viewModel: BookingViewModel
    var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_consultant_detail)
        init()
    }

    fun init() {
        if (intent.hasExtra(Constants.INTENT_KEY_DATA_TWO)) {
            id = intent.getStringExtra(Constants.INTENT_KEY_DATA_TWO).toString()
        }
        viewModel = ViewModelProviders.of(this).get(BookingViewModel::class.java)
        observeLoaderAndError(viewModel)
        observeViewModel()
        viewModel.consultantDetails(id)
    }


    private fun observeViewModel() {
        viewModel.consultantDetailObserver.observe(
            this,
            Observer<Resource<Response<JsonArrayResponse<ProfileResponse>>>> { resource ->
                if (resource.code == Constants.SUCCESS && resource.response.body()?.success!!) {
                    if (getStringDataFromPreferences(Constants.PREFERENCE_KEY_USER_ID).equals(
                            resource.response.body()?.list?.get(0)?.id, true
                        )
                    ) {
                        binding.btnBookAppointment.visibility = View.GONE
                    } else {
                        binding.btnBookAppointment.visibility = View.VISIBLE
                    }
                    binding.item = resource.response.body()?.list?.get(0)
                }
            })
    }

    override fun onClick(view: View?) {
        super.onClick(view)
        when (view?.id) {
            R.id.ivBack -> {
                onBackPressed()
            }
            R.id.btnBookAppointment -> {
                resultLauncher.launch(
                    Intent(this, BookAppointmentActivity::class.java).putExtra(
                        Constants.INTENT_KEY_DATA_TWO,
                        id
                    )
                )
                startAct(this)
            }

        }
    }

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == 1003) {
                finish()
                endAct(this)
            }
        }


}
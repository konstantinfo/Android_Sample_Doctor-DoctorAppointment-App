package com.telemed.base

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler
import com.bumptech.glide.Glide
import com.telemed.R
import com.telemed.interfaces.IDialog
import com.telemed.interfaces.InputDialog
import com.telemed.models.utils.MediaType
import com.telemed.models.utils.UploadImage
import com.telemed.viewmodels.BaseViewModel
import com.telemed.views.activities.GalleryImagePreviewActivity
import com.telemed.views.activities.LoginActivity
import com.telemed.utils.Constants
import com.telemed.utils.ExifUtil
import com.telemed.utils.StringUtility
import com.telemed.utils.Utils
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import droidninja.filepicker.models.sort.SortingTypes
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


open class BaseActivity : AppCompatActivity(), View.OnClickListener, IDialog, InputDialog {


    lateinit var alertBuilder: AlertDialog.Builder
    private lateinit var dialog: androidx.appcompat.app.AlertDialog
    private lateinit var dialogBuilder: androidx.appcompat.app.AlertDialog.Builder
    private lateinit var dialogCustomAlert: androidx.appcompat.app.AlertDialog
    private lateinit var dialogYesNoCustomAlert: androidx.appcompat.app.AlertDialog
    private lateinit var suspendCustomAlert: androidx.appcompat.app.AlertDialog.Builder
    private lateinit var inputCustomAlert: androidx.appcompat.app.AlertDialog.Builder
    private lateinit var dialogBuilderCustomAlert: androidx.appcompat.app.AlertDialog.Builder
    private lateinit var dialogYesNoBuilderCustomAlert: androidx.appcompat.app.AlertDialog.Builder
    private lateinit var suspendCustomAlertDialog: androidx.appcompat.app.AlertDialog
    private lateinit var inputAlertDialog: androidx.appcompat.app.AlertDialog
    private var aspectRationX: Int = 1;
    private var aspectRationY: Int = 1;
    private var shouldCropImage: Boolean = false;
    private val CAMERA_IMAGE_REQUEST = 1001;
    private val GALLERY_IMAGE_REQUEST = 1002;
    private val CAMERA_VIDEO_REQUEST = 1003;
    private val GALLERY_IMAGE_AND_VIDEO_REQUEST = 1004;
    private val GALLERY_IMAGE_AND_VIDEO_PREVIEW_REQUEST = 1008;
    private var imagePickRequest: Int = 0;
    private var mCameraImageUri: Uri? = null;
    private var mSelectedUploadImage = UploadImage()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
            /*Below full screen code working fine in Lollipon but there is some problem with this full screen effect on chat screen(Send message section hide below Keyboard)*/
            //window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }


        dialogBuilder = androidx.appcompat.app.AlertDialog.Builder(this)
        dialogBuilder.setCancelable(false)

        dialogBuilder.setView(R.layout.layout_progress_dialog)
        dialog = dialogBuilder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }

    override fun onDialogClick(isOk: Boolean, tag: Int) {
    }


    /*To show error message*/
    fun onError(reason: String) {
        onError(reason, false)
    }

    /*To show error message*/
    fun onError(reason: String, finishOnOk: Boolean) {
        onInfo(reason, finishOnOk)
    }

    /*To show error message*/
    fun onErrorInfo(reason: String) {
        onErrorInfo(reason, false)
    }

    /*To show error message*/
    fun onErrorInfo(reason: String, finishOnOk: Boolean) {

        if (reason != null && StringUtility.validateString(reason) && !reason.equals(
                "null",
                ignoreCase = true
            )
        ) {
            if (!reason.contains("java.", true) && !reason.contains("javax.", true))
                onInfo(reason)
        } else {
            onInfo(getString(R.string.gender))
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val view = currentFocus
        val ret = super.dispatchTouchEvent(event)
        if (view is EditText) {
            val w = currentFocus
            val location = IntArray(2)
            if (w != null) {
                w.getLocationOnScreen(location)
                val x = event.rawX + w.left - location[0]
                val y = event.rawY + w.top - location[1]
                if (event.action == MotionEvent.ACTION_DOWN
                    && (x < w.left || x >= w.right || y < w.top || y > w.bottom)
                ) {
                    val imm: InputMethodManager =
                        getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(window.currentFocus!!.windowToken, 0)
                }
            }
        }
        return ret
    }

    fun forceLogout() {
        switchActivity(
            Intent(
                this,
                LoginActivity::class.java
            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
        runOnUiThread(Runnable {
            Handler().postDelayed({
                clearData()
            }, 1000)
        })
    }

    fun clearData() {
        PreferenceManager.getDefaultSharedPreferences(this).edit()
            .putBoolean(Constants.PREFERENCE_KEY_IS_LOGGED_IN, false).commit()
        PreferenceManager.getDefaultSharedPreferences(this).edit()
            .putString(Constants.PREFERENCE_KEY_USER_ID, "").commit()
        PreferenceManager.getDefaultSharedPreferences(this).edit()
            .putString(Constants.PREFERENCE_KEY_USER_TOKEN, "").commit()
        PreferenceManager.getDefaultSharedPreferences(this).edit()
            .putString(Constants.PREFERENCE_KEY_USER_DATA, "").commit()
        PreferenceManager.getDefaultSharedPreferences(this).edit()
    }

    fun onInfo(message: String) {
        customOnInfo("", message, resources.getString(R.string.ok), false, null, 0)
    }

    fun onInfo(message: String, finishOnOk: Boolean) {
        customOnInfo("", message, resources.getString(R.string.ok), finishOnOk, null, 0)
    }

    fun onInfo(message: String, buttonName: String) {
        customOnInfo("", message, buttonName, false, null, 0)
    }

    fun onInfo(message: String, buttonName: String, finishOnOk: Boolean) {
        customOnInfo("", message, buttonName, false, null, 0)
        /* customOnInfo(message, buttonName, false, null)*/
    }

    fun onInfo(message: String, buttonName: String, finishOnOk: Boolean, callbackDialog: IDialog?) {
        customOnInfo("", message, buttonName, false, callbackDialog, 0)
    }

    fun onInfo(
        title: String,
        message: String,
        buttonName: String,
        callbackDialog: IDialog?
    ) {
        customOnInfo(title, message, buttonName, false, callbackDialog, 0)
    }


    fun onInfo(
        title: String,
        message: String,
        buttonName: String,
        callbackDialog: IDialog?,
        tag: Int
    ) {
        customOnInfo(title, message, buttonName, false, callbackDialog, tag)
    }


    private fun customOnInfo(
        title: String?,
        message: String?,
        buttonName: String,
        finishOnOk: Boolean,
        callbackDialog: IDialog?,
        tag: Int
    ) {

        dialogBuilderCustomAlert =
            androidx.appcompat.app.AlertDialog.Builder(this)
        dialogBuilderCustomAlert.setCancelable(false)
        dialogBuilderCustomAlert.setView(R.layout.dialog_custom_alert)

        dialogCustomAlert = dialogBuilderCustomAlert.create()
        dialogCustomAlert.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogCustomAlert.window?.setDimAmount(0F)

        dialogCustomAlert.show()

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogCustomAlert.window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT

        dialogCustomAlert.window?.attributes = lp

        val btnLeave = dialogCustomAlert.findViewById<EditText>(R.id.btnLeave) as TextView;
        val faceBold = Typeface.createFromAsset(assets, "fonts/optima_bold.ttf")
        val face = Typeface.createFromAsset(assets, "fonts/optima_regular.ttf")
        btnLeave.setTypeface(face)
        if (buttonName.equals("Ok", true)) {
            btnLeave.getLayoutParams().width = resources.getDimension(R.dimen._95sdp).toInt()
        }
        btnLeave.text = buttonName
        val tvAlertMessage =
            dialogCustomAlert.findViewById<EditText>(R.id.tvAlertMessage) as TextView;
        tvAlertMessage.setTypeface(face)
        val tvAlertTitle = dialogCustomAlert.findViewById<EditText>(R.id.tvAlertTitle) as TextView;
        if (title.isNullOrEmpty()) {
            tvAlertTitle.visibility = View.GONE
        } else {
            tvAlertTitle.visibility = View.VISIBLE
            tvAlertTitle.text = title
        }
        tvAlertMessage.text = message
        tvAlertTitle.setTypeface(faceBold)
        btnLeave.setOnClickListener(View.OnClickListener {
            dialogCustomAlert.dismiss()

            if (callbackDialog != null) {
                callbackDialog.onDialogClick(true, tag)
            } else {
                if (finishOnOk) {
                    onBackPressed()
                }
            }
        })
    }

    fun suspendOnInfo(
        title: String?,
        message: String?
    ) {

        suspendCustomAlert =
            androidx.appcompat.app.AlertDialog.Builder(this, R.style.dialog_theme)
        suspendCustomAlert.setCancelable(false)
        suspendCustomAlert.setView(R.layout.dialog_custom_alert)

        suspendCustomAlertDialog = suspendCustomAlert.create()
        suspendCustomAlertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        suspendCustomAlertDialog.window?.setDimAmount(0F)
        suspendCustomAlertDialog.show()

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(suspendCustomAlertDialog.window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT

        suspendCustomAlertDialog.window?.attributes = lp

        val btnLeave = suspendCustomAlertDialog.findViewById<EditText>(R.id.btnLeave) as TextView;
        val faceBold = Typeface.createFromAsset(assets, "fonts/optima_bold.ttf")
        val face = Typeface.createFromAsset(assets, "fonts/optima_regular.ttf")
        btnLeave.setTypeface(face)
        btnLeave.getLayoutParams().width = resources.getDimension(R.dimen._95sdp).toInt()
        btnLeave.text = "Ok"
        val tvAlertMessage =
            suspendCustomAlertDialog.findViewById<EditText>(R.id.tvAlertMessage) as TextView;
        tvAlertMessage.setTypeface(face)
        val tvAlertTitle =
            suspendCustomAlertDialog.findViewById<EditText>(R.id.tvAlertTitle) as TextView;
        if (title.isNullOrEmpty()) {
            tvAlertTitle.visibility = View.GONE
        } else {
            tvAlertTitle.visibility = View.VISIBLE
            tvAlertTitle.text = title
        }
        tvAlertMessage.text = message
        tvAlertTitle.setTypeface(faceBold)
        btnLeave.setOnClickListener(View.OnClickListener {
            suspendCustomAlertDialog.dismiss()
            clearData()
            switchActivity(
                Intent(
                    this,
                    LoginActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
        })
    }

    fun customInputDialog(
        title: String?,
        buttonName: String,
        finishOnOk: Boolean,
        callbackDialog: InputDialog?,
        tag: Int
    ) {

        inputCustomAlert =
            androidx.appcompat.app.AlertDialog.Builder(this)
        inputCustomAlert.setCancelable(true)
        inputCustomAlert.setView(R.layout.dialog_custom_input_alert)

        inputAlertDialog = inputCustomAlert.create()
        inputAlertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        inputAlertDialog.window?.setDimAmount(0F)

        inputAlertDialog.show()

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(inputAlertDialog.window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT

        inputAlertDialog.window?.attributes = lp

        val btnLeave = inputAlertDialog.findViewById<EditText>(R.id.btnLeave) as TextView;
        val face = Typeface.createFromAsset(assets, "fonts/optima_regular.ttf")
        btnLeave.setTypeface(face)
        if (buttonName.equals("Ok", true)) {
            btnLeave.getLayoutParams().width = resources.getDimension(R.dimen._95sdp).toInt()
        }
        btnLeave.text = buttonName
        val edtValue =
            inputAlertDialog.findViewById<EditText>(R.id.edtValue) as EditText;
        edtValue.setTypeface(face)
        edtValue.hint = ""
        val tvAlertTitle = inputAlertDialog.findViewById<EditText>(R.id.tvAlertTitle) as TextView;
        if (title.isNullOrEmpty()) {
            tvAlertTitle.visibility = View.GONE
        } else {
            tvAlertTitle.visibility = View.VISIBLE
            tvAlertTitle.text = title
        }
        tvAlertTitle.setTypeface(face)
        btnLeave.setOnClickListener(View.OnClickListener {
            inputAlertDialog.dismiss()

            if (callbackDialog != null) {
                val value = edtValue.text.toString().trim()
                callbackDialog.onDialogInputClick(true, tag, value)
            } else {
                if (finishOnOk) {
                    onBackPressed()
                }
            }
        })
    }

    fun yesNoOnInfo(
        title: String?,
        message: String?,
        buttonNag: String,
        buttonPos: String,
        callbackDialog: IDialog?,
        tag: Int
    ) {

        dialogYesNoBuilderCustomAlert =
            androidx.appcompat.app.AlertDialog.Builder(this)
        dialogYesNoBuilderCustomAlert.setCancelable(false)
        dialogYesNoBuilderCustomAlert.setView(R.layout.dialog_yes_no_alert)

        dialogYesNoCustomAlert = dialogYesNoBuilderCustomAlert.create()
        dialogYesNoCustomAlert.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogYesNoCustomAlert.window?.setDimAmount(0F)

        dialogYesNoCustomAlert.show()

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogYesNoCustomAlert.window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT

        dialogYesNoCustomAlert.window?.attributes = lp

        val btnNo = dialogYesNoCustomAlert.findViewById<EditText>(R.id.btnNo) as TextView;
        val btnYes = dialogYesNoCustomAlert.findViewById<EditText>(R.id.btnYes) as TextView;
        val faceBold = Typeface.createFromAsset(assets, "fonts/optima_bold.ttf")
        val face = Typeface.createFromAsset(assets, "fonts/optima_regular.ttf")
        btnYes.setTypeface(face)
        btnNo.setTypeface(face)
        btnNo.text = buttonNag
        btnYes.text = buttonPos
        val tvAlertMessage =
            dialogYesNoCustomAlert.findViewById<EditText>(R.id.tvAlertMessage) as TextView;
        tvAlertMessage.setTypeface(face)
        val tvAlertTitle =
            dialogYesNoCustomAlert.findViewById<EditText>(R.id.tvAlertTitle) as TextView;
        if (title.isNullOrEmpty()) {
            tvAlertTitle.visibility = View.GONE
        } else {
            tvAlertTitle.visibility = View.VISIBLE
            tvAlertTitle.text = title
        }
        tvAlertMessage.text = message
        tvAlertTitle.setTypeface(faceBold)
        btnYes.setOnClickListener(View.OnClickListener {
            dialogYesNoCustomAlert.dismiss()

            callbackDialog?.onDialogClick(true, tag)
        })
        btnNo.setOnClickListener(View.OnClickListener {
            dialogYesNoCustomAlert.dismiss()
        })
    }

    private fun getAlertDialogBuilder(title: String?, message: String): AlertDialog.Builder {
        alertBuilder = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(false)
        return alertBuilder
    }

    fun configureToolBar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        if (supportActionBar != null) {
            supportActionBar!!.title = ""
        }
    }

    /*To show progress bar*/
    fun enableLoadingBar(enable: Boolean) {
        if (enable && !isFinishing) {
            dialog.show()
        } else {
            dialog.dismiss()
        }
    }

    private fun loadProgressBar() {
        if (!this.isFinishing) {
            handleProgressLoader(true)
        }

    }

    private fun dismissProgressBar() {
        handleProgressLoader(false)
    }

    fun handleProgressLoader(isLoading: Boolean) {
        val progressView = findViewById<View>(R.id.progress_layout)
        if (progressView != null) {
            if (isLoading) {
                progressView.visibility = View.VISIBLE
                getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                );

            } else {
                progressView.visibility = View.GONE
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        }

    }

    fun dropDownDialog(title: String, listData: Array<String>, @IdRes id: Int) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this@BaseActivity)
        builder.setTitle(title)
        builder.setItems(listData) { dialog, which ->
            dialog.dismiss()
            onDropDownItemSelected(id, which)
        }
        builder.show()
    }

    open fun onDropDownItemSelected(@IdRes id: Int, position: Int) {

    }

    fun openDateDialog() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                val selectedTime = Calendar.getInstance().apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, monthOfYear)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                }.time
                onDOBSelected(
                    SimpleDateFormat(Constants.FORMATE_dd_MMMM_yyyy, Locale.US).format(
                        selectedTime
                    )
                )
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        // min age allowed is 18 yrs for DOB
        calendar.add(Calendar.YEAR, -18)
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis
        datePickerDialog.show()
    }

    open fun onDOBSelected(Dob: String) {

    }

    override fun onClick(view: View?) {
        disableViewForFewSeconds(view)
    }

    fun disableViewForFewSeconds(view: View?) {
        view?.isEnabled = false
        Handler().postDelayed({
            view?.isEnabled = true
        }, 1000)
    }


    fun switchActivity(intent: Intent) {
        startActivity(intent)
        startAct(this)
    }

    fun switchActivity(intent: Intent, requestCode: Int) {
        startActivityForResult(intent, requestCode)
        startAct(this)
    }

    fun startAct(context: Context) {
        try {
            (context as Activity).overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * animation on finishing activity
     *
     * @param context
     */
    fun endAct(context: Context) {
        try {
            (context as Activity).overridePendingTransition(
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun animateActivity() {
        overridePendingTransition(
            R.anim.fade_in_activity_switching,
            R.anim.fade_out_activity_switching
        )
    }

    override fun onBackPressed() {
        super.onBackPressed()
        endAct(this)
    }

    fun getReplaceFragment(container: Int, fragment: Fragment, tag: String) {
        val fragmentTransaction =
            supportFragmentManager.beginTransaction().replace(container, fragment, tag)
        fragmentTransaction.commit()
    }

    fun observeLoaderAndError(viewModel: BaseViewModel) {
        viewModel.loading.observe(this,
            Observer<Boolean> { aBoolean -> enableLoadingBar(aBoolean!!) })

        viewModel.error.observe(this,
            Observer<String> { value ->
                onError(value)
            })
        viewModel.forceUpdate.observe(this,
            Observer<String> { value -> onError(value) })
        viewModel.hideKeyBoard.observe(this,
            Observer<String> { Utils.hideKeyboard(this@BaseActivity) })
    }

    /*============================    Shared Preferences   =====================================*/

    fun putStringDataInPreferences(key: String, value: String?) {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString(key, value).apply()
    }

    fun getStringDataFromPreferences(key: String): String {
        return PreferenceManager.getDefaultSharedPreferences(this).getString(key, "")!!
    }

    fun putBooleanDataInPreferences(key: String, value: Boolean?) {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(key, value!!).apply()
    }

    fun getBooleanDataFromPreferences(key: String): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(key, false)
    }

    fun putIntDataInPreferences(key: String, value: Int?) {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt(key, value!!).apply()
    }

    fun getIntDataFromPreferences(key: String): Int {
        return PreferenceManager.getDefaultSharedPreferences(this).getInt(key, -1)
    }

    fun selectImageFromGalleryOrClickImageWithCamera(
        shouldCropImage: Boolean,
        aspectRationX: Int,
        aspectRationY: Int,
        imagePickRequest: Int
    ) {
        Utils.hideKeyboard(this);
        this.shouldCropImage = shouldCropImage
        this.aspectRationX = aspectRationX;
        this.aspectRationY = aspectRationY;
        this.imagePickRequest = imagePickRequest
        validateAspectsRatios()
        var myAlertDialog: AlertDialog.Builder = AlertDialog.Builder(this);
        myAlertDialog.setTitle(getResources().getString(R.string.app_name));
        myAlertDialog.setMessage(getResources().getString(R.string.select_image_from));

        myAlertDialog.setPositiveButton(resources.getString(R.string.gallery))
        { dialog, which ->
            openGalleryForSelectImage();
        }

        myAlertDialog.setNegativeButton(resources.getString(R.string.camera))
        { dialog, which ->
            openCameraToClickImage();
        }

        myAlertDialog.show();
    }

    private fun validateAspectsRatios() {
        if (aspectRationX == 0 || aspectRationY == 0) {
            aspectRationX = 1
            aspectRationY = 1
        }
    }

    private fun openGalleryForSelectImage() {
        if (Build.VERSION.SDK_INT >= 23) {
            if ((ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED)
            ) {
                requestPermissions(
                    Constants.PERMISSIONS_STORAGE,
                    Constants.PERMISSIONS_REQUEST_GALLERY
                );
            } else {
                actionGalleryForSelectImage();
            }
        } else {
            actionGalleryForSelectImage();
        }
    }


    /*To get image from gallery*/
    private fun actionGalleryForSelectImage() {

        var intent: Intent = Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(
                Intent.createChooser(intent, getString(R.string.select_picture)),
                GALLERY_IMAGE_REQUEST
            );
            animateActivity();
        } else {
            onInfo(getString(R.string.gallery_unavailable));
        }
    }

    fun openCameraToClickImage() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED ||
                (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED)
            ) {
                requestPermissions(
                    Constants.PERMISSIONS_CAMERA,
                    Constants.PERMISSIONS_REQUEST_CAMERA_IMAGE
                );
            } else {
                actionCameraToClickImage();
            }
        } else {
            actionCameraToClickImage();
        }
    }

    /*To pic image from camera*/
    private fun actionCameraToClickImage() {

        mCameraImageUri = Utils.getOutputMediaFileUri(this)
        if (mCameraImageUri != null) {
            var intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraImageUri);
            } else {
                var file: File = File(mCameraImageUri!!.getPath());
                val photoUri: Uri = FileProvider.getUriForFile(
                    getApplicationContext(),
                    getPackageName() + ".provider",
                    file
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            }

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, CAMERA_IMAGE_REQUEST);
                startAct(this)
            } else {
                onInfo(getResources().getString(R.string.camera_unavailable));
            }
        } else {
            onInfo(getResources().getString(R.string.file_save_error));
        }
    }

    public fun selectDocument() {
        if (Build.VERSION.SDK_INT >= 23) {
            if ((ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED)
            ) {
                requestPermissions(
                    Constants.PERMISSIONS_STORAGE,
                    Constants.PERMISSIONS_REQUEST_DOCUMENT
                );
            } else {
                actionSelectDocument();
            }
        } else {
            actionSelectDocument();
        }
    }

    private fun actionSelectDocument() {
        val pdfs = arrayOf(".pdf", ".txt")
        FilePickerBuilder.instance
            .setMaxCount(1)
            .setActivityTheme(R.style.MyCustomTheme)
            .setActivityTitle(resources.getString(R.string.please_select_doc))
            .enableDocSupport(true)
            .addFileSupport("PDF", pdfs)
            .sortDocumentsBy(SortingTypes.name)
            .withOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            .pickFile(this, FilePickerConst.REQUEST_CODE_DOC)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            CAMERA_IMAGE_REQUEST -> if (resultCode == Activity.RESULT_OK) {
                if (mCameraImageUri != null) {
                    mSelectedUploadImage.imageFilePath =
                        Utils.getRealPathFromURI_2(this, mCameraImageUri!!)
                    mSelectedUploadImage.mediaType = MediaType.CAMERA
                    if (shouldCropImage) {
                        cropImage(mCameraImageUri)
                    } else {
                        compressImage(Intent(), mCameraImageUri!!)
                    }
                }
            }

            GALLERY_IMAGE_REQUEST -> if (resultCode == Activity.RESULT_OK) {
                if (data != null && data.data != null) {
                    mSelectedUploadImage.mediaType = MediaType.GALLERY
                    if (shouldCropImage) {
                        cropImage(data.data)
                    } else {
                        compressImage(Intent(), data.data!!)
                    }
                }
            }

            GALLERY_IMAGE_REQUEST -> if (resultCode == Activity.RESULT_OK) {
                if (data != null && data.data != null) {
                    writeContactPermissionGrantted()
                }
            }


            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data);

                if (resultCode == Activity.RESULT_OK) {
                    compressImage(data, CropImage.getActivityResult(data).uri)
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                }
            }

            GALLERY_IMAGE_AND_VIDEO_REQUEST -> if (resultCode == Activity.RESULT_OK) {
                var cR: ContentResolver = getContentResolver()
                var selectedMediaUri: Uri = data!!.getData()!!
                var type: String = cR.getType(selectedMediaUri)!!
                if (type.contains("image")) {
                    switchActivity(
                        Intent(
                            this,
                            GalleryImagePreviewActivity::class.java
                        ).putExtra(Constants.INTENT_KEY_DATA, data.data.toString()).putExtra(
                            Constants.INTENT_KEY_DATA_TWO, true
                        ), GALLERY_IMAGE_AND_VIDEO_PREVIEW_REQUEST
                    )

                }
            }

            GALLERY_IMAGE_AND_VIDEO_PREVIEW_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    var cR: ContentResolver = getContentResolver();
                    var selectedMediaUri: Uri? = null
                    val bundle = data?.extras
                    if (bundle != null) {
                        val uriString = bundle.getString(Constants.INTENT_KEY_DATA)
                        selectedMediaUri = Uri.parse(uriString)
                    }
                    if (selectedMediaUri != null) {
                        var type: String = cR.getType(selectedMediaUri)!!
                        if (type.contains("image")) {
                            if (shouldCropImage) {
                                cropImage(selectedMediaUri)
                            } else {
                                compressImage(Intent(), selectedMediaUri)
                            }
                        }
                    }
                }
            }
            FilePickerConst.REQUEST_CODE_DOC -> if (resultCode == Activity.RESULT_OK && data != null) {
                var documentPathList: ArrayList<String> =
                    data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS)!!;
                if (documentPathList != null && documentPathList.size > 0) {
                    onDocumentSelected(documentPathList.get(0))
                }
            }
        }
    }

    open fun onDocumentSelected(documentPath: String) {

    }

    open fun writeContactPermissionGrantted() {

    }

    /*To crop image*/
    private fun cropImage(mCameraImageUri: Uri?) {
        try {
            CropImage.activity(mCameraImageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(aspectRationX, aspectRationY)
                .start(this)
        } catch (e: Exception) {
        }
    }

    private fun compressImage(data: Intent?, uri: Uri) {
        var imageBitmap: Bitmap? = null
        lateinit var imagePath: String

        if (shouldCropImage == true) {
            if (data == null) {
                return
            }
            /*if (Crop.getError(data) != null) {
                return
            }*/
        }
        try {

            mSelectedUploadImage.imageFilePath = Utils.getRealPathFromURI_2(this, uri);
            var rotation: Int = ExifUtil.getExifRotation(mSelectedUploadImage.imageFilePath);

            mSelectedUploadImage.bitmap =
                MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            if (rotation != 0) {
                var m: Matrix = Matrix();
                m.preRotate(rotation.toFloat());
                mSelectedUploadImage.bitmap = Bitmap.createBitmap(
                    mSelectedUploadImage.bitmap as Bitmap,
                    0,
                    0,
                    mSelectedUploadImage.bitmap!!.getWidth(),
                    mSelectedUploadImage.bitmap!!.getHeight(),
                    m,
                    true
                );
            } else {
                mSelectedUploadImage.bitmap = Bitmap.createBitmap(
                    mSelectedUploadImage.bitmap as Bitmap,
                    0,
                    0,
                    mSelectedUploadImage.bitmap!!.getWidth(),
                    mSelectedUploadImage.bitmap!!.getHeight()
                );

            }

            /**Create a temporary file **/
            var outFile: File? = Utils.getOutputMediaFile(this);
            var finalImagePath: String? = outFile?.getAbsolutePath();
            if (!TextUtils.isEmpty(finalImagePath)) {
                var mHandler: Handler = Handler(Looper.getMainLooper());

                mHandler.post(Runnable {
                    kotlin.run {
                        var out: FileOutputStream? = null;
                        try {
                            out = FileOutputStream(finalImagePath);
                            mSelectedUploadImage.bitmap!!.compress(
                                Bitmap.CompressFormat.JPEG,
                                100,
                                out
                            );

                            mSelectedUploadImage.imageFilePath = finalImagePath;

                            imageBitmap = mSelectedUploadImage.bitmap!!;
                            imagePath = mSelectedUploadImage.imageFilePath!!;

                            imageBitmap =
                                Utils.compressImage(
                                    mSelectedUploadImage.imageFilePath!!,
                                    this
                                );
                            if (imageBitmap != null) {
                                onImagePickSuccess(imageBitmap!!, imagePickRequest);
                                imagePickRequest = 0
                            }
                        } catch (e: Exception) {

                        }
                    }
                })
            }


        } catch (e: Exception) {

        }

    }

    fun showFullScreenImage(url: String) {
        try {
            val now = System.currentTimeMillis()
            val nagDialog = Dialog(this, R.style.dialogTheme)
            nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            nagDialog.setCancelable(false)
            nagDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            nagDialog.setContentView(R.layout.preview_image)
            val btnClose: ImageView = nagDialog.findViewById(R.id.btnIvClose)
            val ivPreview: ImageView = nagDialog.findViewById(R.id.iv_preview_image)
            ivPreview.setOnTouchListener(ImageMatrixTouchHandler(this))
            Glide
                .with(this)
                .load(Constants.IMAGE_BASE_URL + url)
                .placeholder(resources.getDrawable(R.mipmap.ic_launcher))
                .into(ivPreview)
            btnClose.setOnClickListener(View.OnClickListener {
                nagDialog.dismiss()

            })
            nagDialog.show()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    open fun onImagePickSuccess(bitmap: Bitmap, imagePickRequest: Int) {

    }

    override fun onDialogInputClick(isOk: Boolean, tag: Int, value: String?) {
        TODO("Not yet implemented")
    }

    fun redirectToWhatsApp(number: String) {
        val url = "https://api.whatsapp.com/send?phone=${Constants.countryCode} $number"
        try {
            packageManager.getPackageInfo(
                "com.whatsapp",
                PackageManager.GET_ACTIVITIES
            )
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            i.setPackage("com.whatsapp")
            startActivity(i)
        } catch (e: PackageManager.NameNotFoundException) {
            onInfo(getString(R.string.whatsapp_not_installed))
            e.printStackTrace()
        }
    }


}



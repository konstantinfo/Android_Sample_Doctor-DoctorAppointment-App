package com.telemed.base

import android.Manifest
import android.app.Activity
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
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
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
import com.telemed.utils.Utils
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import droidninja.filepicker.models.sort.SortingTypes
import java.io.File
import java.io.FileOutputStream


open class BaseFragment : Fragment(), TextWatcher,
    TabLayout.BaseOnTabSelectedListener<TabLayout.Tab>,
    ViewPager.OnPageChangeListener, View.OnClickListener, IDialog, InputDialog {
    lateinit var dialog: androidx.appcompat.app.AlertDialog
    lateinit var dialogBuilder: androidx.appcompat.app.AlertDialog.Builder
    private lateinit var dialogCustomAlert: AlertDialog
    private lateinit var suspendCustomAlertDialog: AlertDialog
    private lateinit var dialogBuilderCustomAlert: AlertDialog.Builder
    private lateinit var suspendCustomAlert: AlertDialog.Builder
    private lateinit var inputAlertDialog: androidx.appcompat.app.AlertDialog
    private lateinit var inputCustomAlert: androidx.appcompat.app.AlertDialog.Builder
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialogBuilder = androidx.appcompat.app.AlertDialog.Builder(requireActivity())
        dialogBuilder.setCancelable(false)
        dialogBuilder.setView(R.layout.layout_progress_dialog)
        dialog = dialogBuilder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }


    override fun onClick(view: View?) {
    }


    fun log(value: String) {
        Log.e(javaClass.simpleName, value)
    }

    fun toast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    fun clearPreferences() {
        PreferenceManager.getDefaultSharedPreferences(activity).edit().clear().commit()
    }

    fun switchActivity(intent: Intent) {
        startActivity(intent)
        startAct(requireContext())
    }

    fun switchActivity(intent: Intent, requestCode: Int) {
        startActivityForResult(intent, requestCode)
        startAct(requireContext())
    }

    fun animateActivity() {
        activity?.overridePendingTransition(
            R.anim.fade_in_activity_switching,
            R.anim.fade_out_activity_switching
        )
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

    fun putStringDataInPreferences(key: String, value: String?) {
        PreferenceManager.getDefaultSharedPreferences(activity).edit().putString(key, value).apply()
    }

    fun getStringDataFromPreferences(key: String): String {
        return PreferenceManager.getDefaultSharedPreferences(activity).getString(key, "").toString()
    }

    fun putBooleanDataInPreferences(key: String, value: Boolean?) {
        PreferenceManager.getDefaultSharedPreferences(activity).edit().putBoolean(key, value!!)
            .apply()
    }

    fun getBooleanDataFromPreferences(key: String): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(activity).getBoolean(key, false)
    }


    fun onInfo(message: String) {
        customOnInfo("", message, resources.getString(R.string.ok), false, null, 0)
    }


    override fun onDialogClick(isOk: Boolean, tag: Int) {
    }

    fun onInfo(
        title: String,
        message: String,
        buttonName: String,
        finishOnOk: Boolean,
        callbackDialog: IDialog?
    ) {
        customOnInfo(title, message, buttonName, finishOnOk, callbackDialog, 0)

    }

    fun onInfo(message: String, finishOnOk: Boolean) {
        customOnInfo("", message, resources.getString(R.string.ok), finishOnOk, null, 0)
    }

    fun onInfo(message: String, buttonName: String) {
        customOnInfo("", message, buttonName, false, null, 0)
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

    fun onInfo(message: String, buttonName: String, finishOnOk: Boolean) {

        customOnInfo("", message, buttonName, false, null, 0)


        customOnInfo("", message, buttonName, false, null, 0)

    }

    /* fun onInfo(message: String?, finishOnOk: Boolean) {
         getAlertDialogBuilder(null, message, false).setPositiveButton(getString(R.string.ok),
             if (finishOnOk)
                 DialogInterface.OnClickListener { dialogInterface, i -> activity?.onBackPressed(); }
             else
                 null).show()
     }*/

    fun getAlertDialogBuilder(
        title: String?,
        message: String?,
        cancellable: Boolean
    ): androidx.appcompat.app.AlertDialog.Builder {
        return androidx.appcompat.app.AlertDialog.Builder(requireActivity())
            .setTitle(title)
            .setMessage(message)
            .setCancelable(cancellable)
    }
    /*=========================================================*/

    fun getMyContext(): Context? {
        return activity
    }


    fun getMyActivity(): Activity? {
        return activity
    }

    /*To show progress bar*/
    fun enableLoadingBar(enable: Boolean) {
        if (enable && !requireActivity().isFinishing) {
            dialog.show()
        } else {
            dialog.dismiss()
        }
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

            androidx.appcompat.app.AlertDialog.Builder(requireContext())

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
        val faceBold = Typeface.createFromAsset(requireActivity().assets, "fonts/optima_bold.ttf")
        val face = Typeface.createFromAsset(requireActivity().assets, "fonts/optima_regular.ttf")
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
                    requireActivity().onBackPressed()
                }
            }
        })
    }

    fun suspendOnInfo(
        title: String?,
        message: String?
    ) {

        suspendCustomAlert =
            androidx.appcompat.app.AlertDialog.Builder(requireContext(), R.style.dialog_theme)
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
        val faceBold = Typeface.createFromAsset(requireActivity().assets, "fonts/optima_bold.ttf")
        val face = Typeface.createFromAsset(requireActivity().assets, "fonts/optima_regular.ttf")
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
                    requireContext(),
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
            androidx.appcompat.app.AlertDialog.Builder(requireContext())
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
        val face = Typeface.createFromAsset(requireActivity().assets, "fonts/optima_regular.ttf")
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
                    requireActivity().onBackPressed()
                }
            }
        })
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
            Observer<String> { Utils.hideKeyboard(getMyActivity()) })
    }

    /*To show error message*/
    fun onError(reason: String) {
        onError(reason, false)
    }

    /*To show error message*/
    fun onError(reason: String, finishOnOk: Boolean) {

    }

    fun clearData() {
        PreferenceManager.getDefaultSharedPreferences(getMyActivity()).edit()
            .putBoolean(Constants.PREFERENCE_KEY_IS_LOGGED_IN, false).commit()
        PreferenceManager.getDefaultSharedPreferences(getMyActivity()).edit()
            .putString(Constants.PREFERENCE_KEY_USER_ID, "").commit()
        PreferenceManager.getDefaultSharedPreferences(getMyActivity()).edit()
            .putString(Constants.PREFERENCE_KEY_USER_TOKEN, "").commit()
        PreferenceManager.getDefaultSharedPreferences(getMyActivity()).edit()
            .putString(Constants.PREFERENCE_KEY_USER_DATA, "").commit()
        PreferenceManager.getDefaultSharedPreferences(getMyActivity()).edit()
    }

    override fun onTabReselected(p0: TabLayout.Tab?) {

    }

    override fun onTabUnselected(p0: TabLayout.Tab?) {

    }

    override fun onTabSelected(p0: TabLayout.Tab?) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        TODO("Not yet implemented")
    }

    override fun onPageSelected(position: Int) {
        TODO("Not yet implemented")
    }

    override fun onPageScrollStateChanged(state: Int) {
        TODO("Not yet implemented")
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        TODO("Not yet implemented")
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        TODO("Not yet implemented")
    }

    override fun afterTextChanged(p0: Editable?) {
        TODO("Not yet implemented")
    }


    fun putIntDataInPreferences(key: String, value: Int?) {
        PreferenceManager.getDefaultSharedPreferences(activity).edit().putInt(key, value!!).apply()
    }

    fun getIntDataFromPreferences(key: String): Int {
        return PreferenceManager.getDefaultSharedPreferences(activity).getInt(key, -1)
    }

    fun selectImageFromGalleryOrClickImageWithCamera(
        shouldCropImage: Boolean,
        aspectRationX: Int,
        aspectRationY: Int,
        imagePickRequest: Int
    ) {
        Utils.hideKeyboard(activity);
        this.shouldCropImage = shouldCropImage
        this.aspectRationX = aspectRationX;
        this.aspectRationY = aspectRationY;
        this.imagePickRequest = imagePickRequest
        validateAspectsRatios()
        var myAlertDialog: AlertDialog.Builder = AlertDialog.Builder(requireActivity());
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

    fun selectImageDocument(
        shouldCropImage: Boolean,
        aspectRationX: Int,
        aspectRationY: Int,
        imagePickRequest: Int
    ) {
        Utils.hideKeyboard(activity);
        this.shouldCropImage = shouldCropImage
        this.aspectRationX = aspectRationX;
        this.aspectRationY = aspectRationY;
        this.imagePickRequest = imagePickRequest
        validateAspectsRatios()
        var myAlertDialog: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        myAlertDialog.setTitle(getResources().getString(R.string.app_name))

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
                    requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(
                    requireActivity(),
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
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
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
                    requireActivity(),
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED ||
                (ActivityCompat.checkSelfPermission(
                    requireActivity(),
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

        mCameraImageUri = Utils.getOutputMediaFileUri(requireContext())
        if (mCameraImageUri != null) {
            var intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraImageUri);
            } else {
                var file: File = File(mCameraImageUri!!.getPath());
                val photoUri: Uri = FileProvider.getUriForFile(
                    requireActivity().getApplicationContext(),
                    requireActivity().getPackageName() + ".provider",
                    file
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            }

            if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
                startActivityForResult(intent, CAMERA_IMAGE_REQUEST);
                startAct(requireContext())
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
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(
                    requireContext(),
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
                        Utils.getRealPathFromURI_2(requireContext(), mCameraImageUri!!)
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
                    log(result.error.message.toString())
                }
            }

            GALLERY_IMAGE_AND_VIDEO_REQUEST -> if (resultCode == Activity.RESULT_OK) {
                var cR: ContentResolver = requireActivity().getContentResolver()
                var selectedMediaUri: Uri = data!!.getData()!!
                var type: String = cR.getType(selectedMediaUri)!!
                if (type.contains("image")) {
                    switchActivity(
                        Intent(
                            activity,
                            GalleryImagePreviewActivity::class.java
                        ).putExtra(Constants.INTENT_KEY_DATA, data.data.toString()).putExtra(
                            Constants.INTENT_KEY_DATA_TWO, true
                        ), GALLERY_IMAGE_AND_VIDEO_PREVIEW_REQUEST
                    )

                }
            }

            GALLERY_IMAGE_AND_VIDEO_PREVIEW_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    var cR: ContentResolver = requireActivity().getContentResolver();
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
                .start(requireContext(), this)
        } catch (e: Exception) {
            log(e.toString())
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

            mSelectedUploadImage.imageFilePath = Utils.getRealPathFromURI_2(requireActivity(), uri);
            var rotation: Int = ExifUtil.getExifRotation(mSelectedUploadImage.imageFilePath);

            mSelectedUploadImage.bitmap =
                MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
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
            var outFile: File? = Utils.getOutputMediaFile(requireContext());
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
                                    requireActivity()
                                );
                            if (imageBitmap != null) {
                                onImagePickSuccess(imageBitmap!!, imagePickRequest);
                                imagePickRequest = 0
                            }
                        } catch (e: Exception) {
                            log(e.toString());
                        }
                    }
                })
            }


        } catch (e: Exception) {
            log(e.toString());
        }

    }

    open fun onImagePickSuccess(bitmap: Bitmap, imagePickRequest: Int) {

    }

    fun showFullScreenImage(url: String) {
        try {
            val now = System.currentTimeMillis()
            val nagDialog = Dialog(requireContext(), R.style.dialogTheme)
            nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            nagDialog.setCancelable(false)
            nagDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            nagDialog.setContentView(R.layout.preview_image)
            val btnClose: ImageView = nagDialog.findViewById(R.id.btnIvClose)
            val ivPreview: ImageView = nagDialog.findViewById(R.id.iv_preview_image)
            ivPreview.setOnTouchListener(ImageMatrixTouchHandler(requireContext()))
            Glide
                .with(this)
                .load(Constants.IMAGE_BASE_URL + url)
                .placeholder(requireContext().resources.getDrawable(R.mipmap.ic_launcher))
                .into(ivPreview)
            btnClose.setOnClickListener(View.OnClickListener {
                nagDialog.dismiss()

            })
            nagDialog.show()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun redirectToWhatsApp(number: String) {
        val url = "https://api.whatsapp.com/send?phone=${Constants.countryCode} $number"
        try {
            requireContext().packageManager.getPackageInfo(
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

    override fun onDialogInputClick(isOk: Boolean, tag: Int, value: String?) {
        TODO("Not yet implemented")
    }
}

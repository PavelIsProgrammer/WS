package com.petrs.smartlab.ui.fragments.main.order_register

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.media.MediaRecorder.*
import android.os.Build
import android.provider.MediaStore.Audio.Media
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.petrs.smartlab.*
import com.petrs.smartlab.ui.fragments.main.order_register.dialogs.NewPatientBottomSheetDialogParams
import com.petrs.smartlab.data.ErrorType
import com.petrs.smartlab.databinding.FragmentOrderRegisterBinding
import com.petrs.smartlab.domain.DomainResult
import com.petrs.smartlab.ui.base.BaseFragment
import com.petrs.smartlab.ui.base.error_dialog.ErrorDialog
import com.petrs.smartlab.ui.base.error_dialog.ErrorDialogParams
import com.petrs.smartlab.ui.fragments.main.order_register.dialogs.*
import com.petrs.smartlab.ui.fragments.main.order_register.models.CartItem
import com.petrs.smartlab.ui.fragments.main.order_register.models.PatientItem
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class OrderRegisterFragment : BaseFragment<FragmentOrderRegisterBinding, OrderRegisterViewModel>(
    FragmentOrderRegisterBinding::inflate
) {
    override val viewModel: OrderRegisterViewModel by viewModel()
    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {}
    private lateinit var mediaRecorder: MediaRecorder
    private val recordedAudioFile = File.createTempFile("AUDIO_" + System.currentTimeMillis().toString(), ".mp3")
    private var audioRecorded = false

    private lateinit var patientsAdapter: PatientsAdapter

    @SuppressLint("NewApi", "ClickableViewAccessibility")
    override fun initView() {
        prepareMediaRecorder()

        viewModel.getCart()
        viewModel.getPatients()

        patientsAdapter = PatientsAdapter(
            object : PatientsEventListener {
                override fun analysisCheckedChanged(patient: PatientItem, changedItem: CartItem) {
                    viewModel.staticResult.find { it.first.profile == patient.profile }?.second?.find { it.catalogItem == changedItem.catalogItem }?.selected =
                        changedItem.selected
                    viewModel.submitNewAnalyzes(viewModel.staticResult) { validateOrder() }
                }

                override fun onCloseClicked(patient: PatientItem) {
                    viewModel.selectedPatients.find { it == patient }?.selected = false
                    val newList = patientsAdapter.currentList.toMutableList()
                        .filter { it.first != patient }
                    viewModel.submitNewAnalyzes(newList) { validateOrder() }

                    if (newList.size > 1) {
                        patientsAdapter.submitList(newList)
                    } else {
                        binding.apply {
                            rvPatients.isVisible = false
                            constraintOnePatient.isVisible = true

                            spinnerPatient.text =
                                "${newList[0].first.profile.lastName} ${newList[0].first.profile.firstName}"
                        }
                    }
                }
            }
        )

        binding.apply {
            rvPatients.adapter = patientsAdapter

            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }

            tvAddress.setOnClickListener {
                val dialog = FragmentAddressBottomSheetDialog.getInstance(
                    AddressBottomSheetDialogParams { address, saveAddress, addressTitle ->
                        tvAddress.text = address
                        tvAddress.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.black
                            )
                        )
                        viewModel.orderRequestBody.address = address
                        validateOrder()
                        if (saveAddress) {
                            //ToDo viewModel.saveAddress(addressTitle, address)
                        }
                    }
                )

                dialog.setStyle(
                    DialogFragment.STYLE_NO_TITLE,
                    R.style.BottomSheetDialogTheme
                )

                dialog.show(childFragmentManager, FragmentAddressBottomSheetDialog.TAG)
            }

            tvDateTime.setOnClickListener {
                val dialog = FragmentDateTimeBottomSheetDialog.getInstance(
                    DateTimeBottomSheetDialogParams {
                        tvDateTime.text = it
                        tvDateTime.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.black
                            )
                        )
                        viewModel.orderRequestBody.dateTime = it
                        validateOrder()
                    }
                )

                dialog.setStyle(
                    DialogFragment.STYLE_NO_TITLE,
                    R.style.BottomSheetDialogTheme
                )

                dialog.show(childFragmentManager, FragmentDateTimeBottomSheetDialog.TAG)
            }

            spinnerPatient.setOnClickListener {
                val dialog = FragmentSelectPatientsBottomSheetDialog.getInstance(
                    SelectPatientsBottomSheetDialogParams(
                        viewModel.selectedPatients, { patientItems ->
                            viewModel.selectedPatients = patientItems as ArrayList<PatientItem>
                            viewModel.submitNewAnalyzes(patientItems.map { patientItem ->
                                Pair(
                                    patientItem,
                                    viewModel.staticCart.map { catalog ->
                                        CartItem(
                                            catalog,
                                            true
                                        )
                                    }
                                )
                            }
                            ) {
                                validateOrder()
                            }
                        }, {
                            val dialog = FragmentAddNewPatientBottomSheetDialog.getInstance(
                                NewPatientBottomSheetDialogParams { name, lastName, midName, birthDate, sexOrientation ->
                                    viewModel.createProfile(
                                        name,
                                        lastName,
                                        midName,
                                        birthDate,
                                        sexOrientation
                                    )
                                }
                            )

                            dialog.setStyle(
                                DialogFragment.STYLE_NO_TITLE,
                                R.style.BottomSheetDialogTheme
                            )

                            dialog.show(
                                childFragmentManager,
                                FragmentAddNewPatientBottomSheetDialog.TAG
                            )
                        }
                    )
                )

                dialog.setStyle(
                    DialogFragment.STYLE_NO_TITLE,
                    R.style.BottomSheetDialogTheme
                )

                dialog.show(childFragmentManager, FragmentSelectPatientsBottomSheetDialog.TAG)
            }

            btnAddNewPatient.setOnClickListener {
                val dialog = FragmentAddNewPatientBottomSheetDialog.getInstance(
                    NewPatientBottomSheetDialogParams { name, lastName, midName, birthDate, sexOrientation ->
                        viewModel.createProfile(name, lastName, midName, birthDate, sexOrientation)
                    }
                )

                dialog.setStyle(
                    DialogFragment.STYLE_NO_TITLE,
                    R.style.BottomSheetDialogTheme
                )

                dialog.show(childFragmentManager, FragmentAddNewPatientBottomSheetDialog.TAG)
            }

            etComment.doAfterTextChanged {
                viewModel.orderRequestBody.comment =
                    if (!etComment.text.isNullOrBlank()) etComment.text.toString() else ""
            }

            etPhone.doAfterTextChanged {
                viewModel.orderRequestBody.phone =
                    if (!etPhone.text.isNullOrBlank()) etPhone.text.toString() else ""
                validateOrder()
            }

            btnCreateOrder.setOnClickListener {
                viewModel.createOrder()
            }

            ivSendVoiceMessage.setOnTouchListener { view, motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> if (checkRecordPermission()) startRecording() else permissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
                    MotionEvent.ACTION_UP -> stopRecording()
                }
                true
            }
        }
    }

    private fun startRecording() {
        try {
            mediaRecorder.reset()
            mediaRecorder.setAudioSource(AudioSource.VOICE_RECOGNITION)
            mediaRecorder.setAudioEncodingBitRate(128000)
            mediaRecorder.setAudioSamplingRate(16000)
            mediaRecorder.setOutputFormat(OutputFormat.MPEG_4)
            mediaRecorder.setAudioEncoder(AudioEncoder.AAC)
            mediaRecorder.setOutputFile(recordedAudioFile.absolutePath)
            mediaRecorder.setMaxDuration(20000)
            mediaRecorder.setOnInfoListener { _, what, _ ->
                if (what == MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                    Toast.makeText(requireContext(), "Запись остановлена. Максимальная длина аудио - 20 сек", Toast.LENGTH_SHORT).show()
                    stopRecording()
                }
            }
            mediaRecorder.prepare()
            mediaRecorder.start()
        } catch (e: Exception) {
            Log.d("RECORDER_START", e.stackTraceToString())
        }
    }

    private fun stopRecording() {
        try {
            mediaRecorder.stop()
            audioRecorded = true
            Toast.makeText(requireContext(), "Аудиокомментарий добавлен к заказу", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.d("RECORDER_STOP", e.stackTraceToString())
        }
    }

    private fun prepareMediaRecorder() {
        try {
            mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) MediaRecorder(requireContext()) else MediaRecorder()
        }catch (e: Exception) {
            Log.d("RECORDER_PREPARE", e.stackTraceToString())
        }

    }

    private fun checkRecordPermission() = ContextCompat.checkSelfPermission(
        requireContext(),
        android.Manifest.permission.RECORD_AUDIO
    ) == PackageManager.PERMISSION_GRANTED

    override fun observeViewModel() {
        viewModel.apply {
            cart.observe { domainResult ->
                when (domainResult) {
                    is DomainResult.Success -> {
                        staticCart = domainResult.data
                    }
                    is DomainResult.Error -> {}
                    is DomainResult.Loading -> {}
                }
            }
            result.observe(viewLifecycleOwner) { pairs ->
                if (pairs.isNotEmpty()) {
                    if (pairs.filter { it.first.selected }.size > 1) {
                        binding.rvPatients.isVisible = true
                        binding.constraintOnePatient.isVisible = false

                        patientsAdapter.submitList(
                            viewModel.staticResult
                        )
                    } else {
                        binding.constraintOnePatient.isVisible = true
                        binding.rvPatients.isVisible = false

                        binding.spinnerPatient.text =
                            "${pairs.filter { it.first.selected }[0].first.profile.lastName} ${pairs.filter { it.first.selected }[0].first.profile.firstName}"
                    }
                }

                var analyzesQuantity = 0
                var analyzesSum = 0

                pairs.forEach { pair ->
                    pair.second.forEach { cartItem ->
                        if (cartItem.selected) {
                            analyzesQuantity++
                            analyzesSum += cartItem.catalogItem.price.toInt()
                        }
                    }
                }

                binding.tvAnalyzesQuantity.text = formatQuantity(analyzesQuantity)
                binding.tvAnalyzesSum.text =
                    getString(R.string.price_rub, analyzesSum.toString())
            }
            profileInfo.observe { domainResult ->
                when (domainResult) {
                    is DomainResult.Success -> {
                        saveProfile(domainResult.data)
                    }
                    is DomainResult.Error -> {
                        val message = if (domainResult.type == ErrorType.NO_INTERNET)
                            getString(R.string.network_error)
                        else
                            domainResult.errors
                        val errorDialog =
                            ErrorDialog.getInstance(ErrorDialogParams(message))
                        errorDialog.show(childFragmentManager, ErrorDialog.TAG)
                    }
                    is DomainResult.Loading -> {}
                }
            }
            savedPatients.observe { profiles ->
                if (profiles.isNotEmpty()) {
                    binding.spinnerPatient.text =
                        "${profiles[0].lastName} ${profiles[0].firstName}"
                    Glide.with(requireContext())
                        .load(if (profiles[0].sexOrientation == "Мужской") R.drawable.male else R.drawable.female)
                        .into(binding.ivSexOrientation)
                }

                submitNewAnalyzes(
                    listOf(
                        Pair(
                            PatientItem(profiles[0], true),
                            staticCart.map { CartItem(it, true) })
                    )
                ) {
                    validateOrder()
                }
            }
            createOrder.observe { domainResult ->
                when (domainResult) {
                    is DomainResult.Success -> {
                        if (audioRecorded) {
                            addAudioMessage(domainResult.data.orderId, recordedAudioFile)
                        } else {
                            findNavController().navigate(OrderRegisterFragmentDirections.actionOrderRegisterFragmentToPaymentFragment())
                        }
                    }
                    is DomainResult.Error -> {
                        val message = if (domainResult.type == ErrorType.NO_INTERNET)
                            getString(R.string.network_error)
                        else
                            domainResult.errors
                        val errorDialog =
                            ErrorDialog.getInstance(ErrorDialogParams(message))
                        errorDialog.show(childFragmentManager, ErrorDialog.TAG)
                    }
                    is DomainResult.Loading -> {}
                }
            }
            addAudioToOrder.observe { domainResult ->
                when (domainResult) {
                    is DomainResult.Error -> {
                        val message = if (domainResult.type == ErrorType.NO_INTERNET)
                            getString(R.string.network_error)
                        else
                            domainResult.errors
                        val errorDialog =
                            ErrorDialog.getInstance(ErrorDialogParams(message))
                        errorDialog.show(childFragmentManager, ErrorDialog.TAG)
                    }
                    is DomainResult.Loading -> {}
                    is DomainResult.Success -> {
                        findNavController().navigate(OrderRegisterFragmentDirections.actionOrderRegisterFragmentToPaymentFragment())
                    }
                }
            }
        }
    }

    private fun formatQuantity(quantity: Int): String =
        if (quantity % 10 == 1 && quantity != 11) {
            "$quantity анализ"
        } else if (quantity % 10 in 2..4 && quantity !in 12..14) {
            "$quantity анализа"
        } else {
            "$quantity анализов"
        }

    private fun validateOrder() {
        binding.btnCreateOrder.apply {
            if (viewModel.orderRequestBody.address.isNotBlank() && viewModel.orderRequestBody.dateTime.isNotBlank() && viewModel.orderRequestBody.phone.isNotBlank() && viewModel.orderRequestBody.patients.isNotEmpty()) {
                background = ContextCompat.getDrawable(requireContext(), R.drawable.filled_btn)
                isEnabled = true
            } else {
                background = ContextCompat.getDrawable(requireContext(), R.drawable.blocked_btn)
                isEnabled = false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        mediaRecorder.release()
    }
}
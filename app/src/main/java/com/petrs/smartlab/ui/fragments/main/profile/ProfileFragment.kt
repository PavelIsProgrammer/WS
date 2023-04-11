package com.petrs.smartlab.ui.fragments.main.profile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.petrs.smartlab.R
import com.petrs.smartlab.data.ErrorType
import com.petrs.smartlab.databinding.FragmentProfileBinding
import com.petrs.smartlab.domain.DomainResult
import com.petrs.smartlab.ui.base.BaseFragment
import com.petrs.smartlab.ui.base.error_dialog.ErrorDialog
import com.petrs.smartlab.ui.base.error_dialog.ErrorDialogParams
import com.petrs.smartlab.utils.getRealPathFromURI
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>(
    FragmentProfileBinding::inflate
) {
    override val viewModel: ProfileViewModel by viewModel()
    private lateinit var photoFile: File

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) photoLauncher.launch(getContentIntent())
        }

    private var photoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (activityResult.data != null) {
                photoFile = File(
                    getRealPathFromURI(
                        requireContext(),
                        activityResult.data!!.data!!
                    )!!
                )
                viewModel.updateProfilePhoto(
                    getRealPathFromURI(
                        requireContext(),
                        activityResult.data!!.data!!
                    )!!
                )
            }
        }

    override fun initView() {
        viewModel.getProfile()
        binding.apply {
            cardView.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.profile_photo_background)
            cardView.setOnClickListener {
                if (checkReadStoragePermission()) {
                    photoLauncher.launch(getContentIntent())
                } else {
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
            btnSave.setOnClickListener {
                viewModel.updateProfile(
                    name = etName.text.toString(),
                    lastName = etLastname.text.toString(),
                    midName = etMidname.text.toString(),
                    birthDate = etBirthDate.text.toString(),
                    sexOrientation = spinnerSexOrientation.selectedItem.toString()
                )
            }
        }
    }

    override fun observeViewModel() {
        viewModel.apply {
            profile.observe { domainResult ->
                when (domainResult) {
                    is DomainResult.Success -> {
                        if (domainResult.data.id != -1) {
                            binding.apply {
                                Glide.with(requireContext())
                                    .load(domainResult.data.image)
                                    .error(R.drawable.ic_camera)
                                    .into(ivProfilePhoto)

                                etName.setText(domainResult.data.firstName)
                                etLastname.setText(domainResult.data.lastName)
                                etMidname.setText(domainResult.data.midName)
                                etBirthDate.setText(domainResult.data.birth)

                                if (spinnerSexOrientation.selectedItem.toString() != domainResult.data.sexOrientation) {
                                    spinnerSexOrientation.setSelection(1)
                                }

                                saveProfile(domainResult.data)
                            }
                        } else {
                            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToCreateClientCardFragment2())
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
            avatar.observe { domainResult ->
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
                        val result = profile.value
                        if (result is DomainResult.Success) {
                            val newProfilesData = result.data
                            newProfilesData.image = photoFile.path
                            saveProfileUseCase(newProfilesData)

                            Glide.with(requireContext())
                                .load(photoFile.path)
                                .error(R.drawable.ic_camera)
                                .into(binding.ivProfilePhoto)
                        }
                    }
                }
            }
        }
    }

    private fun checkReadStoragePermission() = ContextCompat.checkSelfPermission(
        requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    private fun getContentIntent() = Intent()
        .setType("*/*")
        .putExtra(
            Intent.EXTRA_MIME_TYPES,
            arrayOf("image/jpg", "image/png", "image/jpeg", "video/mov", "video/mp4")
        )
        .setAction(Intent.ACTION_GET_CONTENT)

}
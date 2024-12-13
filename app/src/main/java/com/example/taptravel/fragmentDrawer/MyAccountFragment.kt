package com.example.taptravel.fragmentDrawer

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.taptravel.Home_Page
import com.example.taptravel.R
import com.example.taptravel.databinding.FragmentMyaccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest

class MyAccountFragment : Fragment() {

    private var _binding: FragmentMyaccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var user: FirebaseUser

    private val PICK_IMAGE_REQUEST = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyaccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        user = firebaseAuth.currentUser!!

        updateUI(user)

        binding.editName.setOnClickListener {
            toggleEditMode()
        }

        binding.btnChangePassword.setOnClickListener {
            handleChangePassword()
        }

        binding.btnDeleteAccount.setOnClickListener {
            deleteAccount()
        }

        binding.editProfileButton.setOnClickListener {
            pickImageFromGallery()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                Log.d("MyAccountFragment", "Selected image URI: $uri")
                binding.ivProfileImage.setImageURI(uri)
                updateProfileImage(uri)
            }
        }
    }

    private fun updateProfileImage(uri: Uri) {
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setPhotoUri(uri)
            .build()

        user.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("MyAccountFragment", "Profile image updated successfully")
                    Toast.makeText(requireContext(), "Profile image updated successfully", Toast.LENGTH_SHORT).show()
                    updateUI(user)
                    updateNavDrawerHeader()
                } else {
                    Log.d("MyAccountFragment", "Failed to update profile image: ${task.exception?.message}")
                    Toast.makeText(requireContext(), "Failed to update profile image", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun updateUI(user: FirebaseUser) {
        binding.tvUserName.text = user.displayName
        binding.userName.text = user.displayName
        binding.tvEmail.text = user.email

        val photoUrl = user.photoUrl
        Log.d("ProfileImageURL", "Photo URL: $photoUrl")

        photoUrl?.let { uri ->
            Glide.with(this)
                .load(uri)
                .placeholder(R.mipmap.ic_launcher_round) // Add a placeholder
                .error(R.mipmap.ic_launcher_round) // Add an error image
                .into(binding.ivProfileImage)
        } ?: run {
            binding.ivProfileImage.setImageResource(R.mipmap.ic_launcher_round)
        }
    }

    private fun updateNavDrawerHeader() {
        val activity = requireActivity() as Home_Page
        activity.loadUserProfile()
    }

    private fun toggleEditMode() {
        val isEditing = binding.etUserName.visibility == View.VISIBLE
        val inputMethodManager = context?.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (!isEditing) {
            binding.etUserName.setText(binding.tvUserName.text)
            binding.etUserName.requestFocus()
        }
        if (isEditing) {
            val newName = binding.etUserName.text.toString().trim()
            if (newName.isNotEmpty()) {
                user.updateProfile(buildProfileUpdate(newName))
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            updateUI(user)
                            Toast.makeText(requireContext(), "Name updated successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "Failed to update name", Toast.LENGTH_SHORT).show()
                        }
                        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
                    }
            } else {
                Toast.makeText(requireContext(), "Please enter a name", Toast.LENGTH_SHORT).show()
            }
        } else {
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }

        binding.tvUserName.visibility = if (isEditing) View.VISIBLE else View.GONE
        binding.userName.visibility = if (isEditing) View.VISIBLE else View.GONE
        binding.etUserName.visibility = if (isEditing) View.GONE else View.VISIBLE
        binding.editName.setImageResource(if (isEditing) R.drawable.ic_edit else R.drawable.ic_save)
    }

    private fun buildProfileUpdate(newName: String): UserProfileChangeRequest {
        return UserProfileChangeRequest.Builder()
            .setDisplayName(newName)
            .build()
    }

    private fun deleteAccount() {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Account")
            .setMessage("Are you sure you want to delete your account?")
            .setPositiveButton("Yes") { dialog, _ ->
                user.delete().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "Account deleted successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Failed to delete account", Toast.LENGTH_SHORT).show()
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun handleChangePassword() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Change Password")

        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        input.hint = "Enter new password"
        builder.setView(input)

        builder.setPositiveButton("Change") { dialog, _ ->
            val newPassword = input.text.toString().trim()

            if (newPassword.isNotEmpty()) {
                val user = FirebaseAuth.getInstance().currentUser
                user?.updatePassword(newPassword)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Password updated successfully
                            Toast.makeText(requireContext(), "Password updated successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            // Handle failure
                            Toast.makeText(requireContext(), "Failed to update password", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(requireContext(), "Please enter a new password", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

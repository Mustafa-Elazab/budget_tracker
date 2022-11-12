package com.example.budgettracker.view.setting


import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.budgettracker.R
import com.example.budgettracker.databinding.FragmentSettingBinding
import com.example.budgettracker.storage.StorageViewModel
import com.example.budgettracker.utils.LocaleHelper
import com.example.budgettracker.utils.LocaleHelper.setLocale
import com.example.budgettracker.view.activity.MainActivity
import com.example.budgettracker.view.fragment.base.BaseFragment
import com.example.budgettracker.view.fragment.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding, StorageViewModel>() {
    override val viewModel: StorageViewModel
            by viewModels()

    val homeViewModel: HomeViewModel by viewModels()

    var isNightMode = false
    var checkedItem: Int = -1
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSettingBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.appBar.toolbar.setTitle(R.string.settings)

        initView()


    }

    private fun initView() = with(binding) {
        cardTheme.setOnClickListener {
            isNightMode = !isNightMode
            viewModel.setDarkMode(isNightMode)
        }



        cardDelete.setOnClickListener {


            showDeleteDialog()
        }

        cardAbout.setOnClickListener {
            moveToAboutScreen()
        }

        cardContactUs.setOnClickListener {
            findNavController().navigate(R.id.action_settingFragment_to_contactFragment)
        }


        cardBalance.setOnClickListener {
            showdialog(requireContext())
        }

    }

    private fun changeLanguage() {



        // Initialize an array of colors
        val array = arrayOf(
            "English", "Arabic"
        )

        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(context)
//        // Set a title for alert dialog
//        builder.setTitle(R.string.choose_your_language)
//
//        builder.setSingleChoiceItems(array, checkedItem) { _, which ->
//            // Get the dialog selected item index
//            checkedItem = which
//
//
//        }
//
//        builder.setPositiveButton("OK") { _, _ ->
//            // Show the dialog selected item to text view
//            if (checkedItem != -1) {
//                val selected = array[checkedItem]
//
//                if (selected == "English") {
//
//                    setLocale(requireContext(), "en")
//                    reloadActivity()
//                } else {
//                    setLocale(requireContext(), "ar")
//                    reloadActivity()
//                }
//
//            } else {
//
//            }
//        }
//
//        // Set the dialog neutral/cancel button
//        builder.setNeutralButton("Cancel") { _, _ ->
//        }
//
//        // Initialize the AlertDialog using builder object
//        dialog = builder.create()
//
//        // Finally, display the alert dialog
//        dialog.show()


        val customLayout: View = layoutInflater.inflate(R.layout.custom_dialog, null)

        builder.setView(customLayout)
        var btnArabic = customLayout.findViewById<Button>(R.id.btn_arabic)
        var btnEnglish = customLayout.findViewById<Button>(R.id.btn_english)
        btnArabic.setOnClickListener {

            LocaleHelper.setLocale(requireContext(), "ar")
            requireActivity().recreate()
        }
        btnEnglish.setOnClickListener {

            LocaleHelper.setLocale(requireContext(), "en")
            requireActivity().recreate()
        }


        builder.show()
    }

    private fun reloadActivity() {
        val i = Intent(requireContext(), MainActivity::class.java)
        requireActivity().finish()
        requireActivity().overridePendingTransition(0, 0)
        startActivity(i)
        requireActivity().overridePendingTransition(0, 0)
    }

    private fun showDeleteDialog() {
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(context)
        builder.setTitle(R.string.dialog_delete_content)
        builder.setPositiveButton(
            R.string.ok,
            DialogInterface.OnClickListener { dialogInterface, i ->

                deleteAllTransactions()
                Toast.makeText(context, "Success Delete All Transactions ", Toast.LENGTH_SHORT)
                    .show()
            })

        builder.setNegativeButton(
            R.string.cancel,
            DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.cancel()
            })
        builder.show()
    }

    fun showdialog(context: Context) {
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(context)
        builder.setTitle(R.string.dialog_balance_content)

// Set up the input
        val input = EditText(context)
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setHint(R.string.balance)

        input.inputType = InputType.TYPE_CLASS_NUMBER
        builder.setView(input)

// Set up the buttons
        builder.setPositiveButton(R.string.ok, DialogInterface.OnClickListener { dialog, which ->
            // Here you get get input text from the Edittext

            if (input.text.isEmpty()) {
                Toast.makeText(context, "Balance Must Not Empty ", Toast.LENGTH_SHORT).show()
            } else {
                val m_Text = input.text.toString()
                viewModel.saveInt("balance", m_Text.toInt())
            }


        })
        builder.setNegativeButton(
            R.string.cancel,
            DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        builder.show()
    }


    private fun moveToAboutScreen() {
        findNavController().navigate(R.id.action_settingFragment_to_aboutFragment)
    }

    private fun deleteAllTransactions() {

        lifecycleScope.launchWhenCreated {
            homeViewModel.deleteAllTransactions()
        }
    }


}
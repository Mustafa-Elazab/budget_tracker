package com.example.budgettracker.view.fragment.add_budget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.budgettracker.R
import com.example.budgettracker.databinding.FragmentAddBudgetBinding
import com.example.budgettracker.model.Transaction
import com.example.budgettracker.utils.Constants
import com.example.budgettracker.utils.parseDouble
import com.example.budgettracker.utils.snack
import com.example.budgettracker.utils.transformIntoDatePicker
import com.example.budgettracker.view.fragment.base.BaseFragment
import com.example.budgettracker.view.fragment.home.HomeViewModel
import com.maltaisn.calcdialog.CalcDialog
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

@AndroidEntryPoint
class AddBudgetFragment : BaseFragment<FragmentAddBudgetBinding, HomeViewModel>(),
    CalcDialog.CalcDialogCallback {

    private var value: BigDecimal? = null
    private var nbFmt = NumberFormat.getInstance()
    override val viewModel: HomeViewModel
            by activityViewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentAddBudgetBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appBar.toolbar.setTitle(R.string.text_add_transaction)

        binding.appBar.toolbar.setNavigationOnClickListener {

            findNavController().popBackStack()
        }
        val fm = childFragmentManager
        val calcDialog = fm.findFragmentByTag(DIALOG_TAG) as? CalcDialog ?: CalcDialog()

        initViews()

        // Open dialog click listener
        val openDialogClickListener = View.OnClickListener {
            if (fm.findFragmentByTag(DIALOG_TAG) != null) {
                // Dialog is already shown.
                return@OnClickListener
            }


            // Update dialog settings
            calcDialog.settings.let {
                it.initialValue = value

                it.numberFormat = nbFmt

            }

            // Show the dialog
            calcDialog.show(fm, DIALOG_TAG)
        }

        binding.contentAddBudgetLayout.btnCalculator.setOnClickListener(openDialogClickListener)


    }


    private fun initViews() {


        val transactionTypeAdapter =
            ArrayAdapter(
                requireContext(),
                R.layout.item_autocomplete_layout,
                Constants.transactionType
            )
        val tagTypeAdapter =
            ArrayAdapter(
                requireContext(),
                R.layout.item_autocomplete_layout,
                Constants.transactionTags
            )

        with(binding) {
            // Set list to TextInputEditText adapter
            contentAddBudgetLayout.etTransactionType.setAdapter(transactionTypeAdapter)

            contentAddBudgetLayout.etTag.setAdapter(tagTypeAdapter)
            // Transform TextInputEditText to DatePicker using Ext function
            contentAddBudgetLayout.etWhen.transformIntoDatePicker(
                requireContext(),
                "dd/MM/yyyy",
                Date()
            )
//           contentAddBudgetLayout.etTag.setOnClickListener {
//               var action =AddBudgetFragmentDirections.actionAddBudgetFragmentToCategoryFragment()
//               findNavController().navigate(action)
//           }

            btnSaveTransaction.setOnClickListener {
                binding.contentAddBudgetLayout.apply {
                    val (title, amount, transactionType, tag, date, note) = getTransactionContent()
                    // validate if transaction content is empty or not
                    when {
                        title.isEmpty() -> {
                            this.etTitle.error = "Title must not be empty"
                        }
                        amount.isNaN() -> {
                            this.etAmount.error = "Amount must not be empty"
                        }
                        transactionType.isEmpty() -> {
                            this.etTransactionType.error = "Transaction type must not be empty"
                        }
                        tag.isEmpty() -> {
                            this.etTag.error = "Tag must not be empty"
                        }
                        date.isEmpty() -> {
                            this.etWhen.error = "Date must not be empty"
                        }
                        note.isEmpty() -> {
                            this.etNote.error = "Note must not be empty"
                        }
                        else -> {
                            viewModel.insertTransaction(getTransactionContent()).run {
                                binding.root.snack(
                                    string = R.string.success_expense_saved
                                )

                                findNavController().navigate(
                                    R.id.action_addBudgetFragment_to_homeFragment, null, NavOptions.Builder().setPopUpTo(
                                        R.id.addBudgetFragment, true
                                    ).build()
                                )
                            }
                        }
                    }
                }
            }
        }

    }

    private fun getTransactionContent(): Transaction = binding.contentAddBudgetLayout.let {
        val title = it.etTitle.text.toString()
        val amount = parseDouble(it.etAmount.text.toString())
        val transactionType = it.etTransactionType.text.toString()
        val tag = it.etTag.text.toString()
        val date = it.etWhen.text.toString()
        val note = it.etNote.text.toString()

        return Transaction(title, amount, transactionType, tag, date, note)
    }

    override fun onValueEntered(requestCode: Int, value: BigDecimal?) {
        if (requestCode == DIALOG_REQUEST_CODE) {
            this.value = value
            updateSelectedValueText()
        }
    }

    private fun updateSelectedValueText() {
        val valueTxv = binding.contentAddBudgetLayout.etAmount
        if (value == null) {
            valueTxv.setText(R.string.selection_value_none)
            valueTxv.alpha = 0.5f
        } else {
            valueTxv.setText(nbFmt.format(value))
            valueTxv.alpha = 1.0f
        }
    }

    companion object {
        private const val DIALOG_REQUEST_CODE = 0
        private const val DIALOG_TAG = "calc_dialog"
    }

}
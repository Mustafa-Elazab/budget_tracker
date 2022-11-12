package com.example.budgettracker.view.fragment.edit_budget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.budgettracker.R
import com.example.budgettracker.databinding.FragmentEditBudgetBinding
import com.example.budgettracker.model.Transaction
import com.example.budgettracker.utils.Constants
import com.example.budgettracker.utils.parseDouble
import com.example.budgettracker.utils.snack
import com.example.budgettracker.utils.transformIntoDatePicker
import com.example.budgettracker.view.fragment.base.BaseFragment
import com.example.budgettracker.view.fragment.home.HomeViewModel
import java.util.*


class EditBudgetFragment : BaseFragment<FragmentEditBudgetBinding, HomeViewModel>() {


    override val viewModel: HomeViewModel
            by activityViewModels()
    private val args: EditBudgetFragmentArgs by navArgs()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentEditBudgetBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appBar.toolbar.setTitle(R.string.text_edit)

        binding.appBar.toolbar.setNavigationOnClickListener {

            findNavController().popBackStack()
        }
        val transaction = args.transaction
        initViews()
        loadData(transaction)
    }
    private fun loadData(transaction: Transaction) = with(binding) {
        contentAddBudgetLayout.etTitle.setText(transaction.title)
        contentAddBudgetLayout.etAmount.setText(transaction.amount.toString())
        contentAddBudgetLayout.etTransactionType.setText(transaction.transactionType, false)
        contentAddBudgetLayout.etTag.setText(transaction.tag, false)
        contentAddBudgetLayout.etWhen.setText(transaction.date)
        contentAddBudgetLayout.etNote.setText(transaction.note)
    }

    private fun initViews() = with(binding) {
        val transactionTypeAdapter =
            ArrayAdapter(
                requireContext(),
                R.layout.item_autocomplete_layout,
                Constants.transactionType
            )
        val tagsAdapter = ArrayAdapter(
            requireContext(),
            R.layout.item_autocomplete_layout,
            Constants.transactionTags
        )

        // Set list to TextInputEditText adapter
        contentAddBudgetLayout.etTransactionType.setAdapter(transactionTypeAdapter)
        contentAddBudgetLayout.etTag.setAdapter(tagsAdapter)

        // Transform TextInputEditText to DatePicker using Ext function
        contentAddBudgetLayout.etWhen.transformIntoDatePicker(
            requireContext(),
            "dd/MM/yyyy",
            Date()
        )
        btnSaveTransaction.setOnClickListener {
            binding.contentAddBudgetLayout.apply {
                val (title, amount, transactionType, tag, date, note) =
                    getTransactionContent()
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
                        viewModel.updateTransaction(getTransactionContent()).also {

                            binding.root.snack(
                                string = R.string.success_expense_saved
                            ).run {
                                findNavController().popBackStack()
                            }
                        }
                    }
                }
            }
        }
    }
    private fun getTransactionContent(): Transaction = binding.contentAddBudgetLayout.let {

        val id = args.transaction.id
        val title = it.etTitle.text.toString()
        val amount = parseDouble(it.etAmount.text.toString())
        val transactionType = it.etTransactionType.text.toString()
        val tag = it.etTag.text.toString()
        val date = it.etWhen.text.toString()
        val note = it.etNote.text.toString()

        return Transaction(
            title = title,
            amount = amount,
            transactionType = transactionType,
            tag = tag,
            date = date,
            note = note,
            createdAt = System.currentTimeMillis(),
            id = id
        )
    }
}
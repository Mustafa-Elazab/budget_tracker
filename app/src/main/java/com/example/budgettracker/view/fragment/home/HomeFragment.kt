package com.example.budgettracker.view.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.budgettracker.R
import com.example.budgettracker.databinding.FragmentHomeBinding
import com.example.budgettracker.model.Transaction
import com.example.budgettracker.storage.StorageViewModel
import com.example.budgettracker.utils.*
import com.example.budgettracker.view.adapter.TransactionAdapter
import com.example.budgettracker.view.fragment.base.BaseFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Math.abs


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    private lateinit var transactionAdapter: TransactionAdapter
    override val viewModel: HomeViewModel
            by activityViewModels()
    val storageViewModel: StorageViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.appBar.toolbar.setTitle(R.string.home)
        setupRV()
        initViews()
        observeFilter()
        observeTransaction()
        swipeToDelete()

    }

    private fun initViews() = with(binding) {
        floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addBudgetFragment)
        }

        scroll.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { _, sX, sY, oX, oY ->
                if (abs(sY - oY) > 10) {
                    when {
                        sY > oY -> floatingActionButton.hide()
                        oY > sY -> floatingActionButton.show()
                    }
                }
            }
        )





        transactionAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("transaction", it)
            }
            findNavController().navigate(
                R.id.action_homeFragment_to_detailBudgetFragment,
                bundle
            )
        }


    }


    private fun observeFilter() = with(binding) {
        lifecycleScope.launchWhenCreated {

            viewModel.transactionFilter.collect { filter ->
                when (filter) {
                    "Overall" -> {
                        totalBlance.tvTotalBalance.text =
                            getString(R.string.text_total_balance)
                        linear.show()

                        incomeCardView.tvTotalTitle.text = getString(R.string.text_total_income)
                        expenseCardView.tvTotalTitle.text = getString(R.string.text_total_expense)
                        expenseCardView.totalIcon.setImageResource(R.drawable.ic_expense)
                    }

                }
                viewModel.getAllTransaction(filter)
            }
        }
    }

    private fun setupRV() = with(binding) {
        transactionAdapter = TransactionAdapter()
        rvRecentTransaction.apply {
            adapter = transactionAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun swipeToDelete() {
        // init item touch callback for swipe action
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // get item position & delete notes
                val position = viewHolder.adapterPosition
                val transaction = transactionAdapter.differ.currentList[position]
                val transactionItem = Transaction(
                    transaction.title,
                    transaction.amount,
                    transaction.transactionType,
                    transaction.tag,
                    transaction.date,
                    transaction.note,
                    transaction.createdAt,
                    transaction.id
                )
                viewModel.deleteTransaction(transactionItem)
                Snackbar.make(
                    binding.root,
                    getString(R.string.success_transaction_delete),
                    Snackbar.LENGTH_LONG
                )
                    .apply {
                        setAction(getString(R.string.text_undo)) {
                            viewModel.insertTransaction(
                                transactionItem
                            )
                        }
                        show()
                    }
            }
        }

        // attach swipe callback to rv
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvRecentTransaction)
        }
    }

    private fun onTotalTransactionLoaded(transaction: List<Transaction>) = with(binding) {
        val (totalIncome, totalExpense) = transaction.partition {
            it.transactionType == "Income"
        }

        val income = totalIncome.sumOf {
            it.amount
        }
        val expense = totalExpense.sumOf {
            it.amount
        }


        incomeCardView.tvTotalValue.text = "+ ".plus(egyPound(income))
        expenseCardView.tvTotalValue.text = "- ".plus(egyPound(expense))


        if (storageViewModel.getInt("balance") != null) {
            totalBlance.tvTotalBalanceNumber.text =

                egyPound(
                    (storageViewModel.getInt("balance")?.plus((income - expense)) ?: 0) as Double
                )
        } else {
            totalBlance.tvTotalBalanceNumber.text =


                egyPound(income - expense)
        }


    }


    private fun observeTransaction() = lifecycleScope.launchWhenStarted {
        viewModel.uiState.collect { uiState ->
            when (uiState) {
                is ViewState.Loading -> {
                }
                is ViewState.Success -> {
                    showAllViews()
                    onTransactionLoaded(uiState.transaction)
                    onTotalTransactionLoaded(uiState.transaction)
                }
                is ViewState.Error -> {
                    binding.root.snack(
                        string = R.string.text_error
                    )
                }
                is ViewState.Empty -> {
                    hideAllViews()
                }
            }
        }
    }

    private fun onTransactionLoaded(list: List<Transaction>) {
        transactionAdapter.differ.submitList(list)

    }


    private fun showAllViews() = with(binding) {
        relative.show()
        emptyStateLayout.hide()
        rvRecentTransaction.show()
    }


    private fun hideAllViews() = with(binding) {
        relative.hide()
        emptyStateLayout.show()
    }

//    private fun setUIMode(item: MenuItem, isChecked: Boolean) {
//        if (isChecked) {
//
//            item.setIcon(R.drawable.ic_day)
//            viewModel.setDarkMode(true)
//        } else {
//
//            item.setIcon(R.drawable.ic_night)
//            viewModel.setDarkMode(false)
//        }
//    }


}
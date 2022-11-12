package com.example.budgettracker.view.fragment.expense

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.budgettracker.R
import com.example.budgettracker.databinding.FragmentExpenseBinding
import com.example.budgettracker.model.Transaction
import com.example.budgettracker.utils.ViewState
import com.example.budgettracker.utils.hide
import com.example.budgettracker.utils.show
import com.example.budgettracker.utils.snack
import com.example.budgettracker.view.adapter.TransactionAdapter
import com.example.budgettracker.view.fragment.base.BaseFragment
import com.example.budgettracker.view.fragment.home.HomeViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ExpenseFragment : BaseFragment<FragmentExpenseBinding, HomeViewModel>() {
    override val viewModel: HomeViewModel
            by viewModels()
    private lateinit var transactionAdapter: TransactionAdapter

    var s1: Double = 0.0
    var s2: Double = 0.0
    var s3: Double = 0.0
    var s4: Double = 0.0
    var s5: Double = 0.0
    var s6: Double = 0.0
    var s7: Double = 0.0
    var s8: Double = 0.0
    var s9: Double = 0.0
    var s10: Double = 0.0


    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentExpenseBinding.inflate(
        inflater, container, false
    )


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.allExpense()
        setupRV()
        initViews()
        observeFilter()
        observeTransaction()
        swipeToDelete()
    }


    private fun initViews() = with(binding) {

        appBar.toolbar.setTitle(R.string.expense)

        transactionAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("transaction", it)
            }
            findNavController().navigate(
                R.id.action_expenseFragment_to_detailBudgetFragment,
                bundle
            )
        }


    }

    private fun observeFilter() = with(binding) {
        lifecycleScope.launchWhenCreated {
            viewModel.transactionFilter.collect { filter ->
                when (filter) {
                    "Expense" -> {
                        totalExpense.tvTotalBalance.text =
                            getString(R.string.text_total_expense)

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
        val (totalIncomeBalance) = transaction.partition {
            it.transactionType == "Expense"
        }

        val expense = totalIncomeBalance.sumOf {
            it.amount
        }





        totalExpense.tvTotalBalanceNumber.text =
            expense.toString()
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
        var allIncome = list.sumOf {
            it.amount
        }
        setUpPieChart(list, allIncome)
    }

    private fun setUpPieChart(list: List<Transaction>, allIncome: Double) = with(binding) {


        list.forEach {
            if (it.tag == "Housing") {

                s1 += it.amount.div(allIncome)


            }
            if (it.tag == "Transportation") {

                s2 += it.amount.div(allIncome)

            }
            if (it.tag == "Food") {
                s3 += it.amount.div(allIncome)

            }
            if (it.tag == "Utilities") {
                s4 += it.amount.div(allIncome)

            }
            if (it.tag == "Insurance") {
                s5 += it.amount.div(allIncome)

            }
            if (it.tag == "Healthcare") {

                s6 += it.amount.div(allIncome)

            }
            if (it.tag == "Saving & Debts") {

                s7 += it.amount.div(allIncome)

            }
            if (it.tag == "Personal Spending") {

                s8 += it.amount.div(allIncome)

            }
            if (it.tag == "Entertainment") {

                s9 += it.amount.div(allIncome)

            }

            if (it.tag == "Other") {

                s10 += it.amount.div(allIncome)

            }


            // setting description as enabled and offset for pie chart
            pieChart.setUsePercentValues(true)
            pieChart.description.isEnabled = false
            pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

            // on below line we are setting drag for our pie chart
            pieChart.dragDecelerationFrictionCoef = 0.95f

            // on below line we are setting hole
            // and hole color for pie chart
            pieChart.isDrawHoleEnabled = true
            pieChart.setHoleColor(Color.WHITE)

            // on below line we are setting circle color and alpha
            pieChart.setTransparentCircleColor(Color.WHITE)
            pieChart.setTransparentCircleAlpha(110)

            // on  below line we are setting hole radius
            pieChart.holeRadius = 58f
            pieChart.transparentCircleRadius = 61f

            // on below line we are setting center text
            pieChart.setDrawCenterText(true)

            // on below line we are setting
            // rotation for our pie chart
            pieChart.rotationAngle = 0f

            // enable rotation of the pieChart by touch
            pieChart.isRotationEnabled = true
            pieChart.isHighlightPerTapEnabled = true

            // on below line we are setting animation for our pie chart
            pieChart.animateY(1400, Easing.EaseInOutQuad)

            // on below line we are disabling our legend for pie chart
            pieChart.legend.isEnabled = false
            pieChart.setEntryLabelColor(Color.WHITE)
            pieChart.setEntryLabelTextSize(12f)

            // on below line we are creating array list and
            // adding data to it to display in pie chart
            val entries: ArrayList<PieEntry> = ArrayList()
            entries.add(PieEntry(s1.toFloat()))
            entries.add(PieEntry(s2.toFloat()))
            entries.add(PieEntry(s3.toFloat()))
            entries.add(PieEntry(s4.toFloat()))
            entries.add(PieEntry(s5.toFloat()))
            entries.add(PieEntry(s6.toFloat()))
            entries.add(PieEntry(s7.toFloat()))
            entries.add(PieEntry(s8.toFloat()))
            entries.add(PieEntry(s9.toFloat()))
            entries.add(PieEntry(s10.toFloat()))


            // on below line we are setting pie data set
            val dataSet = PieDataSet(entries, "Chart")

            // on below line we are setting icons.
            dataSet.setDrawIcons(false)

            // on below line we are setting slice for pie
            dataSet.sliceSpace = 3f
            dataSet.iconsOffset = MPPointF(0f, 40f)
            dataSet.selectionShift = 5f



            // add a lot of colors to list
            val colors: ArrayList<Int> = ArrayList()
            colors.add(resources.getColor(R.color.Housing))
            colors.add(resources.getColor(R.color.Transportation))
            colors.add(resources.getColor(R.color.Food))
            colors.add(resources.getColor(R.color.Utilities))
            colors.add(resources.getColor(R.color.Insurance))
            colors.add(resources.getColor(R.color.Healthcare))
            colors.add(resources.getColor(R.color.Saving_Debts))
            colors.add(resources.getColor(R.color.Personal_Spending))
            colors.add(resources.getColor(R.color.Entertainment))
            colors.add(resources.getColor(R.color.Other))


            // on below line we are setting colors.
            dataSet.colors = colors

            // on below line we are setting pie data set
            val data = PieData(dataSet)
            data.setValueFormatter(PercentFormatter())
            data.setValueTextSize(15f)
            data.setValueTypeface(Typeface.DEFAULT_BOLD)
            data.setValueTextColor(Color.WHITE)
            pieChart.data = data

            // undo all highlights
            pieChart.highlightValues(null)

            // loading chart
            pieChart.invalidate()




        }

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
}
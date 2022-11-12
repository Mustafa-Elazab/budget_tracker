package com.example.budgettracker.view.fragment.detail

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.core.view.drawToBitmap
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.budgettracker.R
import com.example.budgettracker.databinding.FragmentDetailBudgetBinding
import com.example.budgettracker.model.Transaction
import com.example.budgettracker.utils.*
import com.example.budgettracker.view.fragment.base.BaseFragment
import com.example.budgettracker.view.fragment.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailBudgetFragment : BaseFragment<FragmentDetailBudgetBinding, HomeViewModel>() {


    private val args: DetailBudgetFragmentArgs by navArgs()
    override val viewModel: HomeViewModel
            by activityViewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentDetailBudgetBinding.inflate(inflater, container, false)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appBar.toolbar.setTitle(R.string.text_detail)

        binding.appBar.toolbar.setNavigationOnClickListener {

            findNavController().popBackStack()
        }

        binding.appBar.toolbar.inflateMenu(R.menu.menu_share)
        binding.appBar.toolbar.setOnMenuItemClickListener{
            onOptionsItemSelected(it)
        }
        val transaction = args.transaction
        getTransaction(transaction.id)
        observeTransaction()
    }

    private fun getTransaction(id: Int) {
        viewModel.getByID(id)
    }

    private fun observeTransaction() = lifecycleScope.launchWhenCreated {

        viewModel.detailState.collect { detailState ->

            when (detailState) {
                DetailState.Loading -> {
                }
                is DetailState.Success -> {
                    onDetailsLoaded(detailState.transaction)
                }
                is DetailState.Error -> {
                    binding.root.snack(
                        string = R.string.text_error
                    )
                }
                DetailState.Empty -> {
                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun onDetailsLoaded(transaction: Transaction) = with(binding.transactionDetails) {
        title.text = transaction.title
        amount.text = egyPound(transaction.amount)
        type.text = transaction.transactionType
        tag.text = transaction.tag
        date.text = transaction.date
        note.text = transaction.note
        createdAt.text = transaction.createdAtDateFormat

        binding.editTransaction.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("transaction", transaction)
            }
            findNavController().navigate(
                R.id.action_detailBudgetFragment_to_editBudgetFragment,
                bundle
            )
        }
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> {


                viewModel.deleteByID(args.transaction.id)
                    .run {
                        findNavController().navigateUp()
                    }
            }
            R.id.action_share -> shareText()

        }
        return super.onOptionsItemSelected(item)
    }




    @SuppressLint("StringFormatMatches", "StringFormatInvalid")
    private fun shareText() = with(binding) {
        val shareMsg = getString(
            R.string.share_message,
            transactionDetails.title.text.toString(),
            transactionDetails.amount.text.toString(),
            transactionDetails.type.text.toString(),
            transactionDetails.tag.text.toString(),
            transactionDetails.date.text.toString(),
            transactionDetails.note.text.toString(),
            transactionDetails.createdAt.text.toString()
        )

        val intent = ShareCompat.IntentBuilder(requireActivity())
            .setType("text/plain")
            .setText(shareMsg)
            .intent

        startActivity(Intent.createChooser(intent, null))
    }

}
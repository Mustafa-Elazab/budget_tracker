package com.example.budgettracker.view.fragment.board

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.budgettracker.databinding.FragmentOnBoardBinding
import com.example.budgettracker.model.CurrencyResponse
import com.example.budgettracker.storage.StorageViewModel
import com.example.budgettracker.utils.getJsonDataFromAsset
import com.example.budgettracker.view.activity.MainActivity
import com.example.budgettracker.view.fragment.base.BaseFragment
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardFragment : BaseFragment<FragmentOnBoardBinding, StorageViewModel>() {
    override val viewModel: StorageViewModel
            by viewModels()
    val gson = Gson()
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentOnBoardBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initValue()

    }

    private fun getJsonData() {
        val jsonFileString = getJsonDataFromAsset(requireContext(), "Currency.json")
        val currencyModel = gson.fromJson(jsonFileString, CurrencyResponse::class.java)

        Log.i("TAG", "getJsonData: $currencyModel")

    }

    private fun initValue() {

        binding.btnSaveGainMonthly.setOnClickListener {

            if (binding.etGain.text.isNullOrEmpty()) {

            } else {
                val balance = Integer.parseInt(binding.etGain.text.toString())
                viewModel.saveInt("balance", balance)
                Log.i("TAG", "getJsonData: ${viewModel.getInt("balance")}")
                Handler().postDelayed({
                    val intent  = Intent(requireActivity(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                },2000L)
            }


        }

    }


}
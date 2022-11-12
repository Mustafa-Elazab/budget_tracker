package com.example.budgettracker.view.fragment.about



import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.budgettracker.R
import com.example.budgettracker.databinding.FragmentAboutBinding
import com.example.budgettracker.storage.StorageViewModel
import com.example.budgettracker.view.fragment.base.BaseFragment
import com.nitish.typewriterview.TypeWriterView.OnAnimationChangeListener
import java.util.*


class AboutFragment : BaseFragment<FragmentAboutBinding, StorageViewModel>() {
    override val viewModel: StorageViewModel
            by viewModels()


    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentAboutBinding.inflate(
        inflater, container, false
    )


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appBar.toolbar.setTitle(R.string.text_about)

        binding.appBar.toolbar.setNavigationOnClickListener {

            findNavController().popBackStack()
        }


      // setText("Budget Tracker can certainly help you when you need to manage your budget")

        binding.tvAppContext.animateText("Budget Tracker can certainly help you when you need to manage your budget")

        binding.tvAppContext.setOnAnimationChangeListener(OnAnimationChangeListener {
            //Do something
        })
    }

    fun setText(s: String) {


        val i = IntArray(1)
        i[0] = 0
        val length = s.length
        val handler: Handler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                val c = s[i[0]]
                binding.tvAppContext.append(c.toString())
                i[0]++
            }
        }
        val timer = Timer()
        val taskEverySplitSecond: TimerTask = object : TimerTask() {
            override fun run() {
                handler.sendEmptyMessage(0)
                if (i[0] == length - 1) {
                    timer.cancel()
                }
            }
        }
        timer.schedule(taskEverySplitSecond, 1, 20)
    }


}


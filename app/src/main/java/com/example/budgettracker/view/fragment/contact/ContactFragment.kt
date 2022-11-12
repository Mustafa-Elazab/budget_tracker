package com.example.budgettracker.view.fragment.contact

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.budgettracker.R
import com.example.budgettracker.databinding.FragmentContactBinding
import com.example.budgettracker.view.fragment.base.BaseFragment
import com.example.budgettracker.view.fragment.home.HomeViewModel


class ContactFragment : BaseFragment<FragmentContactBinding, HomeViewModel>() {
    override val viewModel: HomeViewModel
            by viewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentContactBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appBar.toolbar.setTitle(R.string.contact_us)

        binding.appBar.toolbar.setNavigationOnClickListener {

            findNavController().popBackStack()
        }


        initViews()

    }

    private fun initViews() {
        binding.cardFacebook.setOnClickListener {
            openFacebookPage()
        }
        binding.cardGmail.setOnClickListener {
            sendEmail(
                recipient = "mustafaelazab97@gmail.com",
                subject = "Contact and Feed back",
                ""
            )
        }

        binding.cardWhats.setOnClickListener {
            sendWhatsAppMessage("hello")
        }
    }

    fun openFacebookPage() {

        // FacebookページのID
        val facebookPageID = "100009818748074"

        // URL
        val facebookUrl = "https://www.facebook.com/$facebookPageID"

        // URLスキーム
        val facebookUrlScheme = "fb://page/$facebookPageID"
        try {
            // Facebookアプリのバージョンを取得
            val versionCode: Int =
                requireActivity().getPackageManager()
                    .getPackageInfo("com.facebook.katana", 0).versionCode
            if (versionCode >= 3002850) {
                // Facebook アプリのバージョン 11.0.0.11.23 (3002850) 以上の場合
                val uri: Uri = Uri.parse("fb://facewebmodal/f?href=$facebookUrl")
                startActivity(Intent(Intent.ACTION_VIEW, uri))
            } else {
                // Facebook アプリが古い場合
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrlScheme)))
            }
        } catch (e: PackageManager.NameNotFoundException) {
            // Facebookアプリがインストールされていない場合は、ブラウザで開く
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl)))
        }
    }

    @SuppressLint("IntentReset")
    fun sendEmail(recipient: String, subject: String, message: String) {
        /*ACTION_SEND action to launch an email client installed on your Android device.*/
        val mIntent = Intent(Intent.ACTION_SEND)
        /*To send an email you need to specify mailto: as URI using setData() method
        and data type will be to text/plain using setType() method*/
        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"
        // put recipient email in intent
        /* recipient is put as array because you may wanna send email to multiple emails
           so enter comma(,) separated emails, it will be stored in array*/
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        //put the Subject in the intent
        mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        //put the message in the intent
        mIntent.putExtra(Intent.EXTRA_TEXT, message)


        try {
            //start email intent
            startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
        } catch (e: Exception) {
            //if any thing goes wrong for example no email client application or any exception
            //get and show exception message

        }


    }

    fun sendWhatsAppMessage(message: String) {

        val phoneNumberWithCountryCode = "+201555465611"
        val message = message

        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    String.format(
                        "https://api.whatsapp.com/send?phone=%s&text=%s",
                        phoneNumberWithCountryCode,
                        message
                    )
                )
            )
        )
    }
}
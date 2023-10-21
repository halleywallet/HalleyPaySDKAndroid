package ir.millennium.halleywallet.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import ir.github.halleypay.HalleyPay
import ir.github.halleypay.presentation.utils.Constants.SECRET_KEY
import ir.millennium.halleywallet.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<MaterialButton>(R.id.btnPaymentWithHalleyWallet).setOnClickListener {
            HalleyPay.start(this, SECRET_KEY)
        }
    }
}
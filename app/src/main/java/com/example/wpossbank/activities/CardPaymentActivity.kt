package com.example.wpossbank.activities

import android.app.DatePickerDialog
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.wpossbank.R
import com.example.wpossbank.database.Database
import com.example.wpossbank.fragments.Dialogs.ConfirmUpdateAdmin
import com.example.wpossbank.models.*
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur
import java.util.*

class CardPaymentActivity : AppCompatActivity() {
    lateinit var context: Context
    lateinit var res: Resources
    lateinit var sp: SharedPreference
    lateinit var db: Database
    lateinit var validate: Validate
    lateinit var messages: MakeMessages
    lateinit var admin: Admin
    private lateinit var card: CreditCard
    lateinit var blurView: BlurView
    private lateinit var cardTextView: TextView
    private lateinit var cardNumberInput: EditText
    private lateinit var expDateInput: EditText
    private lateinit var ccvInput: EditText
    private lateinit var nameInput: EditText
    private lateinit var lastnameInput: EditText
    private lateinit var paymentAmountInput: EditText
    private lateinit var duesInput: EditText
    lateinit var confirmButton: Button
    lateinit var backArrow: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_payment)
        context = this
        res = resources
        sp = SharedPreference(context)

        //Declaracion de metodos
        validate = Validate(context)

        //Declaracion de objetos
        db = Database(context)
        messages = MakeMessages()
        admin = Admin()
        card = CreditCard()

        blurView = findViewById(R.id.blurView)
        blurBackground()

        //Declaracion de elementos del layout
        cardTextView = findViewById(R.id.cardTextView)
        cardNumberInput = findViewById(R.id.cardNumberInput)
        expDateInput = findViewById(R.id.expDateInput)
        ccvInput = findViewById(R.id.ccvInput)
        nameInput = findViewById(R.id.nameInput)
        lastnameInput = findViewById(R.id.lastnameInput)
        paymentAmountInput = findViewById(R.id.paymentAmountInput)
        duesInput = findViewById(R.id.duesInput)
        confirmButton = findViewById(R.id.confirmButton)
        backArrow = findViewById(R.id.backArrow)

        //Abre el calendario para seleccionar la fecha al tocar el campo de texto
        expDateInput.setOnClickListener {
            val calendar = Calendar.getInstance()
            val dpd = DatePickerDialog(
                context,
                { datePicker: DatePicker, _: Int, _: Int, _: Int ->
                    //Traduce el objeto calendar a string para mostrar la fecha en el campo de texto
                    val string = datePicker.year.toString() + "/" +
                            datePicker.month + "/" +
                            datePicker.dayOfMonth
                    expDateInput.setText(string)
                    validate.expDate(expDateInput)
                }, calendar[Calendar.YEAR],
                calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH]
            )
            dpd.show()
        }

        //Obtiene el tipo de tarjeta y actualzia el titulo del campo de texto acorde
        cardNumberInput.setOnKeyListener { _: View?, _: Int, _: KeyEvent? ->
            card.number = cardNumberInput.text.toString()
            if (!validate.isEmpty(cardNumberInput)) {
                cardTextView.text = card.getType(context)
            } else {
                cardNumberInput.error = res.getString(R.string.error_invalid)
            }
            false
        }

        //Confirma todos los campos antes de continuar
        confirmButton.setOnClickListener {
            //Declara los valores del formulario en las propiedades correspondientes de la tarjeta
            card.number = cardNumberInput.text.toString()
            card.expDate = expDateInput.text.toString()
            card.ccv = ccvInput.text.toString()
            card.ownerName = nameInput.text.toString() + " " + lastnameInput.text.toString()
            card.paymentAmmount = paymentAmountInput.text.toString()
            card.duesNumber = duesInput.text.toString()

            val cardNumberValidate = validate.cardNumber(cardNumberInput)
            val expDateValidate = validate.expDate(expDateInput)
            val ccvValidate = validate.ccv(ccvInput)
            val nameValidate = validate.name(nameInput)
            val lastnameValidate = validate.name(lastnameInput)
            val paymentValidate = validate.payment(paymentAmountInput)
            val duesValidate = validate.dues(duesInput)

            if (cardNumberValidate && expDateValidate && ccvValidate && nameValidate
                && lastnameValidate && paymentValidate && duesValidate) {
                admin.balance = card.paymentAmmount.toInt()

                val logEntry = LogEntry()
                logEntry.type = "card"
                logEntry.amount = admin.balance
                logEntry.source = cardNumberInput.text.toString()

                ConfirmUpdateAdmin(logEntry, admin, messages.cardPayment(context, card))
                    .show(supportFragmentManager, "Confirm")
            } else {
                // Avisa al usuario si hay un campo con valores invalidos
                Toast.makeText(context, R.string.error_invalid_input, Toast.LENGTH_LONG).show()
            }
        }
        backArrow.setOnClickListener { finish() }
    }

    private fun blurBackground() {
        val radius = 20f
        val decorView = window.decorView
        val rootView = decorView.findViewById<ViewGroup>(android.R.id.content)
        val windowBackground = decorView.background
        blurView.setupWith(rootView)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(radius)
            .setBlurAutoUpdate(true)
            .setHasFixedTransformationMatrix(true)
    }
}
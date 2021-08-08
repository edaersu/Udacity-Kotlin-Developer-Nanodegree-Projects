package com.example.aboutme

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.aboutme.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val myName:MyName=MyName("Eda Ersu")

    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.myName=myName
       /* findViewById<Button>(R.id.button).setOnClickListener {
            addNickname(it)
        }
        */
        binding.button.setOnClickListener {
            addNickname(it) }

    }
    private fun addNickname(view: View){
     //   val nickedittext=findViewById<EditText>(R.id.nickname_edit)
     //   val nicktextview=findViewById<TextView>(R.id.nickname_text)
       // binding.nicknameText.text=binding.nicknameEdit.text

        myName.nickname =binding.nicknameEdit.text.toString()
        binding.nicknameEdit.visibility=View.GONE
        binding.button.visibility=View.GONE
        binding.nicknameText.visibility=View.VISIBLE

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

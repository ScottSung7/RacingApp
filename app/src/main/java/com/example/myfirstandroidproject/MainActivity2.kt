package com.example.myfirstandroidproject

import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myfirstandroidproject.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding
    val TAG: String = "로그"
    var shared : String = "file"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        checkBtn()

    }
    private fun checkBtn(){
        binding.inputText.text = ""
        binding.inputText.visibility = View.GONE
        binding.input1.visibility = View.VISIBLE
        binding.checkbtn.setOnClickListener{

            val text = binding.input1.getText()
            val textSet = binding.inputText.setText(text)
            binding.input1.setText("")
            binding.input1.visibility = View.GONE
            binding.inputText.visibility = View.VISIBLE

        }
    }
    var firstPause : Boolean = false
    override fun onResume() {
        super.onResume()
        showInfo()
    }
    private fun showInfo(){
        if(firstPause && !binding.inputText.text.isNullOrEmpty()) {
            binding.inputText.visibility = View.VISIBLE
            binding.input1.visibility = View.GONE
        }
    }
    override fun onPause() {
        super.onPause()

        hidingInfo()
    }
    private fun hidingInfo(){
        firstPause = true
        if(!binding.inputText.text.isNullOrEmpty()) {
            binding.inputText.visibility = View.GONE
            binding.input1.visibility = View.VISIBLE
        }
    }

    override fun onStop() {
        super.onStop()
        Toast.makeText(this@MainActivity2, "stop 됨", Toast.LENGTH_SHORT).show()

        Log.d(TAG, "MainActivity2 - onStop() called")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "MainActivity2 - onDestroy() called")

        Toast.makeText(this@MainActivity2, "디스트로이 됨", Toast.LENGTH_SHORT).show()

    }


}
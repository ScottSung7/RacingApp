package com.example.myfirstandroidproject

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import com.example.myfirstandroidproject.databinding.ActivityDrawerBinding
import com.example.myfirstandroidproject.databinding.ActivityMain2Binding
import com.example.myfirstandroidproject.databinding.ActivityMain3Binding

class MainActivity3 : AppCompatActivity() {

    private lateinit var binding: ActivityMain3Binding
    val TAG: String = "로그"

    //ON CREATE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toast.makeText(this@MainActivity3, "OnCreate - 로그인", Toast.LENGTH_SHORT).show()

        binding= ActivityMain3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        registerBtnIntent()
        loginBtnIntent()

    }
    private fun registerBtnIntent() {
        binding.registerBtn.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)

        }
    }
    private fun loginBtnIntent() {
        binding.loginBtn.setOnClickListener {
            val idWritten = binding.input1.text
            val pwWritten = binding.input2.text
            if(idWritten.length > 0 && pwWritten.length > 0) {
                val intent = Intent(this, MainActivity4::class.java)
                setLogInInfo(intent)
                startActivity(intent)
            }else{
                loginAlert()
            }
        }


    }
    private fun setLogInInfo(intent: Intent){
        intent.putExtra("idSent",  binding.input1.getText().toString())
        intent.putExtra("pwSent",  binding.input2.getText().toString())
    }
    private fun loginAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("로그인 정보를 입력해 주세요.")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener{dialog, id->

                })
            .show()
    }
    //ON START
    override fun onStart() {
        super.onStart()
        Toast.makeText(this@MainActivity3, "OnCreate - 로그인", Toast.LENGTH_SHORT).show()

        checkLoginInfo()


    }
    private fun checkLoginInfo(){
        var sharedPreferences = getSharedPreferences("login", 0)
        val id = sharedPreferences.getString("id", "")
        val pw = sharedPreferences.getString("pw", "")

        if((!id.equals("null") && !pw.equals("null")) &&
                (!id.isNullOrEmpty() && !pw.isNullOrEmpty())){
            val intent = Intent(this, MainActivity4::class.java)
            startActivity(intent)
        }
    }

    private fun timeoutAlert(time : Long){
        Handler(Looper.getMainLooper()).postDelayed({
            val builder = AlertDialog.Builder(this)
            builder.setTitle("타이틀")
                .setMessage("내용")
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener{dialog, id->

                })
                .setNegativeButton("취소",
                    DialogInterface.OnClickListener{dialog, id ->

                })
                .show()
        }, time)

    }


    override fun onResume() {
        super.onResume()
        Log.d(TAG, "MainActivity2 - onResume() called")
        Toast.makeText(this@MainActivity3, "안녕1", Toast.LENGTH_SHORT)

    }

    override fun onRestart() {
        super.onRestart()
        Toast.makeText(this@MainActivity3, "RESTART - 로그인", Toast.LENGTH_SHORT)

    }
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "MainActivity2 - onPause() called")
        Toast.makeText(this@MainActivity3, "안녕2", Toast.LENGTH_SHORT)

    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "MainActivity2 - onStop() called")
        Toast.makeText(this@MainActivity3, "안녕3", Toast.LENGTH_SHORT).show()

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "MainActivity2 - onDestroy() called")
        Toast.makeText(this@MainActivity3, "Destroy 로그인", Toast.LENGTH_SHORT).show()

    }


}
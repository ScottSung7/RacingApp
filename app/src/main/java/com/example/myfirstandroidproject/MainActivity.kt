package com.example.myfirstandroidproject

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myfirstandroidproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    val TAG: String = "로그"
    var onCreatedHappened : Boolean = false
    //다른 앱 실행.
    // 액티비티가 생성되었을때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "MainActivity - onCreate() called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        goToLogInPageAfterFewsecond(3000)

    }
    private fun goToLogInPageAfterFewsecond(Time : Long){
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent);

        }, 3000)

        onCreatedHappened  = true
    }


    override fun onStart() {

        super.onStart()
        Log.d(TAG, "MainActivity - onStart() called")

        blockComingBackHereFromLogInPage()

    }
    private fun blockComingBackHereFromLogInPage(){
        if(!onCreatedHappened) {
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent);
        }
        onCreatedHappened = false
    }




}
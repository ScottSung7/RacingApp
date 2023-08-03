package com.example.myfirstandroidproject

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import com.example.myfirstandroidproject.databinding.ActivityDrawerBinding
import com.example.myfirstandroidproject.databinding.ActivityMain4Binding
import com.example.myfirstandroidproject.databinding.CameraActivityBinding
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date


class MainActivity4 : AppCompatActivity() {

    private lateinit var binding: ActivityMain4Binding
    private lateinit var binding_drawer: ActivityDrawerBinding
    private lateinit var binding_camera: CameraActivityBinding
    val TAG: String = "로그"

    val REQUEST_IMAGE_CAPUTRE = 1
    lateinit var currentPhotoPath : String

    private var drawerLayout: DrawerLayout? = null
    private var drawerView: View? = null

    private var otherAppCheck = false
    //ON CREATE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Toast.makeText(this@MainActivity4, "CREATE - Main", Toast.LENGTH_SHORT).show()

        binding= ActivityMain4Binding.inflate(layoutInflater)
        binding_drawer = ActivityDrawerBinding.inflate(layoutInflater)
        binding_camera = CameraActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerFunction()
        standingBtnFunction()

        getLoginInfoFromPrevIntent()

        setPermission()


    }
    private fun cameraEvent(){
        binding.cameraBtn.setOnClickListener{
            Toast.makeText(this@MainActivity4, "촬영시작", Toast.LENGTH_SHORT).show()
            takeCapture()
        }
    }

    private fun takeCapture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also{ takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also{
                val photoFile : File? = try{
                    createImageFile()
                } catch (ex : IOException){
                    null
                }
                photoFile?.also{
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.myfirstandroidproject.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPUTRE)


                }
            }
        }
    }

    private fun createImageFile(): File {
        val timestamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir : File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timestamp}", ".jpg", storageDir)
            .apply{ currentPhotoPath = absolutePath}
    }

    private fun setPermission() {
        val permission = object : PermissionListener {
            override fun onPermissionGranted() {
                Toast.makeText(this@MainActivity4, "권한이 허용 됨.", Toast.LENGTH_SHORT)
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(this@MainActivity4, "권한이 거부 됨.", Toast.LENGTH_SHORT)
            }

        }

        TedPermission.create()
            .setPermissionListener(permission)
            .setRationaleMessage("카메라 앱을 사용하시려면 권한을 혀용해주세요")
            .setDeniedMessage("권한을 거부하셨습니다. [앱 설정] -> [권한] 항목에서 허용해주세요.")
            .setPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA)
            .check()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_IMAGE_CAPUTRE && resultCode == Activity.RESULT_OK){
            val bitmap: Bitmap
            val file = File(currentPhotoPath)
            if(Build.VERSION.SDK_INT < 28){
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, Uri.fromFile(file))
                binding_camera.idProfile.setImageBitmap(bitmap)
            }else{
                val decode = ImageDecoder.createSource(
                    this.contentResolver,
                    Uri.fromFile(file)
                )
                bitmap = ImageDecoder.decodeBitmap(decode)
                binding_camera.idProfile.setImageBitmap(bitmap)
            }
            savePhoto(bitmap)

        }
    }

    private fun savePhoto(bitmap: Bitmap) {
        val folderPath = Environment.getExternalStorageDirectory().absolutePath + "/Pictures/"
        val timestamp : String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val fileName = "${timestamp}.jpeg"
        val folder = File(folderPath)
        if(!folder.isDirectory) {
            folder.mkdirs()
        }

        val out = FileOutputStream(folderPath + fileName)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        Toast.makeText(this, "사진이 앨범에 저장되었습니다.", Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        Toast.makeText(this@MainActivity4, "START - Main", Toast.LENGTH_SHORT).show()
        getOtherData()
        setFavDriver()

    }

    private fun drawerFunction(){

        val btn = findViewById<View>(R.id.btn_close) as Button
        btn.setOnClickListener { binding.drawerLayout!!.closeDrawers() }

        binding.drawerLayout!!.setDrawerListener(listener)
        binding_drawer.drawer!!.setOnTouchListener { v, event -> true }

    }

    private fun standingBtnFunction(){
        binding.standingBtn.setOnClickListener{view ->
            otherAppCheck = true
            var intent = Intent("android.intent.action.STANDING")
            startActivity(intent)

        }
    }
    private fun getLoginInfoFromPrevIntent(){

        val id  = intent.getStringExtra("idSent").toString()
        val pw = intent.getStringExtra("pwSent").toString()
        Toast.makeText(this@MainActivity4, id + " " + pw, Toast.LENGTH_LONG).show()

        setLogInInfo(id, pw)

    }
    private fun setLogInInfo(id : String, pw: String){
        println("여기 체크 하기2")
        println(id.equals("null"))
        println(pw.equals("null"))
        if(!id.equals("null") && !pw.equals("null")) {
            val sharedPreferences = getSharedPreferences("login", 0)
            val editor = sharedPreferences.edit()
            editor.putString("id", id).apply()
            editor.putString("pw", pw).apply()
            editor.commit()
        }
    }
    private fun setFavDriver(){
        var sharedPreferences = getSharedPreferences("fav", 0)
        val favDriver = sharedPreferences.getString("favDriver", "NOT YET DECIDED")
        println("SharedPReference 확인")
        println(favDriver)
        println(favDriver.equals("null"))
        println(favDriver.isNullOrEmpty())
        if(!favDriver.equals("null") && !favDriver.isNullOrEmpty()) {
            binding.favDriver.setText(favDriver)
        }
    }
    private fun getOtherData(){

        val intent : Intent = getIntent();
        val action = intent.getAction();
        val type = intent.getType();
        println(action)
        println(type)
        if (Intent.ACTION_SENDTO.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
                if(sharedText != null) {
                    binding.favDriver.setText(sharedText)
                    setFavInfo(sharedText)

                    otherAppCheck = false

                }
            }
        }
    }


    var listener: DrawerListener = object : DrawerListener {
        override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
        override fun onDrawerOpened(drawerView: View) {}
        override fun onDrawerClosed(drawerView: View) {}
        override fun onDrawerStateChanged(newState: Int) {}
    }

    //ON STOP
    override fun onStop() {
        super.onStop()
        Log.d(TAG, "MainActivity2 - onStop() called")
        Toast.makeText(this@MainActivity4, "Stopped", Toast.LENGTH_SHORT).show()


    }
    private fun setFavInfo(driver : String){
        val sharedPreferences = getSharedPreferences("fav", 0)
        val editor = sharedPreferences.edit()
        editor.putString("favDriver", driver.toString()).apply()
        editor.commit()
    }

    //ON DESTROY
    override fun onDestroy() {
        super.onDestroy()
        println(otherAppCheck)
        if(otherAppCheck) {
            Toast.makeText(this@MainActivity4, "Destroyed - Other App", Toast.LENGTH_SHORT).show()
        }else if(backpressed){
            Toast.makeText(this@MainActivity4, "Destroyed - BackPressed", Toast.LENGTH_SHORT).show()

        }else if(!otherAppCheck && !backpressed){
            Toast.makeText(this@MainActivity4, "Destroyed - Not Other App", Toast.LENGTH_SHORT).show()

            removeLogInInfo()

        }

        finishAndRemoveTask(); // 액티비티 종료 + 태스크 리스트에서 지우기

    }
    private fun removeLogInInfo(){
        val pref = getSharedPreferences("login", MODE_PRIVATE)
        val editor = pref.edit()
        editor.remove("id").apply()
        editor.remove("pw").apply()
        editor.commit()
    }
    var backpressed = false
    override fun onKeyDown(keyCode: Int, event: KeyEvent? ): Boolean{
        return backpressedHandle(keyCode, event)
    }
    private fun backpressedHandle(keyCode : Int, event : KeyEvent?) : Boolean{
       Log.d(TAG, "HEEEE - backpressedHandle() called")
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backpressed = true
        }else{
            backpressed = false
        }
        return false
    }



}
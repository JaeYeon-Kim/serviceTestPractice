package com.kjy.servicetest

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    // 안드로이드에 전달할 Intent를 만들고, MyService에 미리 정의해둔 명령을 action에 담아서 같이 전달.
    // 새로운 메서드를 만들 때 파라미터로(view: View)를 사용하면 클릭리스너 연결 없이도 레이아웃파일에서 메서드에 직접 접근 가능.
    fun serviceStart(view : View) {
        val intent = Intent(this, MyService::class.java)
        intent.action = MyService.ACTION_START
        startService(intent)
    }

    // 서비스 중단을 위해 stopService()로 인텐트 전달.
    fun serviceStop(view: View) {

       val intent = Intent(this, MyService::class.java)
       stopService(intent)
    }

    /*
     서비스와 연결할 수 있는 서비스 커넥션 생성.
     만든 서비스 커넥션을 bindService() 메서드를 통해 시스템에 전달되면 서비스와 연결할 수 있음.
     onServiceConnected()는 서비스가 연결되면 호출되지만, OnServiceDisconnected는 서비스가 정상적으로 연결 해제되었을
     경우가 아니라 비정상적으로 연결이 끊어졌을 때 실행되기 때문에 서비스 연결 변수인 isService가 필요함.
     이를 통해 서비스가 연결되어 있는지 확인하는 로직이 필요함.
     */
    var myService:MyService? = null
    var isService = false
    val connection = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as MyService.MyBinder
            myService = binder.getService()
            isService = true
            Log.d("BoundService", "연결되었습니다.")
        }

        override fun onServiceDisconnected(name: ComponentName) {
            isService = false
        }
    }
    fun serviceBind(view: View) {
        val intent = Intent(this, MyService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    fun serviceUnbind(view: View) {
        if (isService) {
            unbindService(connection)
            isService = false
        }
    }

    // serviceMessage() 호출하는 메서드 추가
    fun callServiceFuntion(view: View) {
        if (isService) {
            val message = myService?.serviceMessage()
            Toast.makeText(this, "message: $message", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "서비스가 연결되지 않았습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}
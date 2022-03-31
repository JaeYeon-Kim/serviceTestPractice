package com.kjy.servicetest

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

class MyService : Service() {


    // 스타디드 서비스에서는 사용하지 않음.
    // 바인드서비스에서 사용 binder 반환
    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    // 호출시 onStartCommand 명령엉로 전달.
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        Log.d("StartedService", "action = $action")
        return super.onStartCommand(intent, flags, startId)
    }

    // 테스트로 사용할 명령어 몇개를 companion object로 감싸서 만들어줌.
    companion object {
        val ACTION_START = "kr.co.hanbit.servicetest.START"
        val ACTION_RUN = "kr.co.hanbit.servicetest.RUN"
        val ACTION_STOP = "kr.co.hanbit.servicetest.STOP"

    }

    override fun onDestroy() {
        Log.d("Service", "서비스가 종료 되었습니다.")
        super.onDestroy()
    }

    // 바운드 서비스를 위한 바인더 클래스 생성 = 서비스와 액티비티를 연결해야함. ServiceConnection을 생성
    // 액티비티와 서비스가 연결되면 바인더의 getService() 메서드를 통해 서비스에 접근 가능.
    inner class MyBinder: Binder() {
        fun getService(): MyService {
            return this@MyService
        }
    }
    val binder = MyBinder()


    // 바운드 서비스는 스타티드 서비스와 다르게 서비스의 메서드를 직접 호출해서 사용.
    // 문자열 하나를 반환하는 serviceMessage
    fun serviceMessage(): String {
        return "Hello Activity! I am Service!"
    }

}
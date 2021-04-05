package buct.tzx.servicemanager

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import buct.tzx.adprotool.AdService
import buct.tzx.servicemanager.Impl.AppService
import buct.tzx.servicemanager.Interface.AppServiceImpl
import buct.tzx.servicemanager_api.ServiceManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var text = findViewById<TextView>(R.id.text)
        text.text = ServiceManager.getInstance().getService<AdService>(AdService::class.java).hello()
        try {
            ServiceManager.getInstance().regeister(
                AppService::class.java,
                AppServiceImpl::class.java
            )
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        Toast.makeText(
            this,
            ServiceManager.getInstance().getService<AppService>(AppService::class.java).say(),
            Toast.LENGTH_SHORT
        ).show()
    }
}
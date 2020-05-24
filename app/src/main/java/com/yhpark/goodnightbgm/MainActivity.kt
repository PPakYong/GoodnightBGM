package com.yhpark.goodnightbgm

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat


/**
 * app icon copyright
 * <a href="http://www.freepik.com/" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/kr/" title="Flaticon"> www.flaticon.com</a>
 */

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.mainContainer, MainFragment())
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.fragments.size >= 2) {
            val fragmentManager = supportFragmentManager;
            fragmentManager.beginTransaction().remove(fragmentManager.fragments[1]).commit()
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menuSetting -> {
                supportFragmentManager.beginTransaction()
                    .add(R.id.mainContainer, SettingFragment())
                    .commit()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        when (requestCode) {
            0x0001 -> {
            }
            else -> {
            }
        }
        super.startActivityForResult(intent, requestCode)
    }
}

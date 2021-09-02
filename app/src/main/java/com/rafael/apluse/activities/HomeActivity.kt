package com.rafael.apluse.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rafael.apluse.R
import com.rafael.apluse.fragments.ClassPageFragment
import com.rafael.apluse.fragments.HomeFragment

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val homeFragment = HomeFragment()
        val messageFragment = ClassPageFragment()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,homeFragment)
                .commit()
        }
    }
}
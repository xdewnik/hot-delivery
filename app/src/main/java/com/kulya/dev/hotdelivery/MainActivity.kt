package com.kulya.dev.hotdelivery

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import com.kulya.dev.hotdelivery.data.Order

class MainActivity : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            openFragment()
        }

    }
    //TODO eto kak dialog otkrit
    private fun showDeliveryFragment(order:Order){
        DeliveryDialog.getInstance(order).show(supportFragmentManager, "tag")
    }

    private fun openFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, OrderFragment())
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount>1){
            super.onBackPressed()
        }else{
            finish()
        }

    }
}
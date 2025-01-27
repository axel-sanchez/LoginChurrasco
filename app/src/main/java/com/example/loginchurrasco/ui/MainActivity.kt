package com.example.loginchurrasco.ui

import android.annotation.TargetApi
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.loginchurrasco.R
import com.example.loginchurrasco.ui.customs.BaseFragment
import com.example.loginchurrasco.ui.interfaces.INavigationHost

@TargetApi(Build.VERSION_CODES.N)
@RequiresApi(Build.VERSION_CODES.N)
class MainActivity : AppCompatActivity(), INavigationHost {

    private val INTERVAL = 2000 //2 segundos para salir
    private var tiempoPrimerClick: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val sharedPref = getPreferences(Context.MODE_PRIVATE)

            var preferenceToken = sharedPref.getString("preference_token", "")

            preferenceToken?.let { token ->

                if (token.isNotEmpty()) replaceTo(SitiesFragment(), false)
                else replaceTo(LoginFragment(), false)

            } ?: replaceTo(LoginFragment(), false)
        }
    }

    /**
     * Navigate to the given fragment.
     * @param fragment       Fragment to navigate to.
     * @param addToBackstack Whether or not the current fragment should be added to the backstack.
     */
    override fun navigateTo(fragment: Fragment, addToBackstack: Boolean) {

        val transaction = supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.enter_right_to_left,
                R.anim.exit_right_to_left,
                R.anim.enter_left_to_right,
                R.anim.exit_left_to_right
            )
            .add(R.id.container, fragment)
        if (addToBackstack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    override fun replaceTo(fragment: Fragment, addToBackstack: Boolean) {

        val transaction = supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.enter_right_to_left,
                R.anim.exit_right_to_left,
                R.anim.enter_left_to_right,
                R.anim.exit_left_to_right
            )
            .replace(R.id.container, fragment)

        if (addToBackstack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    override fun finish() {
        supportFragmentManager.popBackStack()
    }

    override fun onBackPressed() {

        val f = supportFragmentManager.findFragmentById(R.id.container) as BaseFragment

        if (supportFragmentManager.backStackEntryCount > 0) {

            if (f.childFragmentManager.backStackEntryCount > 1) {
                f.childFragmentManager.popBackStack()
            } else {
                super.onBackPressed()
            }
        } else {
            if (tiempoPrimerClick + INTERVAL > System.currentTimeMillis()) {
                super.finish()
                return
            } else {
                Toast.makeText(this, "Vuelve a presionar para salir", Toast.LENGTH_SHORT).show()
            }
            tiempoPrimerClick = System.currentTimeMillis()
        }
    }
}
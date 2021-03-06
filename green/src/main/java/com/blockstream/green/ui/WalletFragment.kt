package com.blockstream.green.ui

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.databinding.ViewDataBinding
import com.blockstream.green.NavGraphDirections
import com.blockstream.green.R
import com.blockstream.green.database.Wallet
import com.blockstream.green.gdk.GreenSession
import com.blockstream.green.ui.wallet.WalletViewModel
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.subscribeBy
import java.util.concurrent.TimeUnit

abstract class WalletFragment<T : ViewDataBinding> constructor(
    @LayoutRes layout: Int,
    @MenuRes menuRes: Int
) : AppFragment<T>(layout, menuRes) {

    abstract val wallet: Wallet
    lateinit var session: GreenSession

    private var networkSnackbar: Snackbar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        session = sessionManager.getWalletSession(wallet)

        if (isLoggedInRequired() && !session.isConnected()) {
            navigate(NavGraphDirections.actionGlobalLoginFragment(wallet))
            return
        }

        getWalletViewModel()?.let{

            it.onNetworkEvent.observe(viewLifecycleOwner){ event ->

                if(event.connected){

                    networkSnackbar = networkSnackbar?.let {
                        it.dismiss()
                        null
                    }

                }else{

                    if(networkSnackbar == null){
                        networkSnackbar = Snackbar.make(
                            view,
                            R.string.id_you_are_not_connected,
                            Snackbar.LENGTH_INDEFINITE
                        )
                    }

                    networkSnackbar?.setText(
                        getString(
                            R.string.id_not_connected_connecting_in_ds_,
                            (event.waiting ?: 0)
                        )
                    )?.show()

                    Observable.interval(1, TimeUnit.SECONDS)
                        .take(event.waiting ?: 0)
                        .map {
                            event.waiting ?: 0 - it
                        }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy(
                            onNext = {
                                networkSnackbar?.setText(getString(R.string.id_not_connected_connecting_in_ds_, it))
                            }
                        )
                }

            }
        }
    }

    open fun isLoggedInRequired(): Boolean = true

    abstract fun getWalletViewModel(): WalletViewModel?

    override fun onResume() {
        super.onResume()


    }

    fun logout(){
        session.disconnectAsync()
        NavGraphDirections.actionGlobalLoginFragment(wallet).let {
            navigate(it.actionId, it.arguments, isLogout = true)
        }
    }
}
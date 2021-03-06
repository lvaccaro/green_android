package com.blockstream.green.data

import android.os.Parcelable
import com.blockstream.gdk.data.Network
import com.blockstream.green.database.Wallet
import kotlinx.parcelize.Parcelize

@Parcelize
data class OnboardingOptions(
    val isRestoreFlow: Boolean,
    val isWatchOnly: Boolean = false,
    val isBIP39: Boolean = false,
    val networkType: String? = null,
    val network: Network? = null,
    val walletName: String? = null,
    val deviceId: Int = -1
) : Parcelable{

    fun isHardwareOnboarding() = deviceId > 0

    companion object{
        fun fromWallet(wallet: Wallet, network: Network): OnboardingOptions {

            return OnboardingOptions(
                isRestoreFlow = true,
                isWatchOnly = false,
                networkType = wallet.network,
                network = network,
                walletName = wallet.name
            )

        }
    }
}
package com.blockstream.green.utils

import com.blockstream.green.gdk.GreenSession
import com.blockstream.gdk.data.Balance
import com.blockstream.gdk.data.Settings
import com.blockstream.gdk.data.Transaction
import com.blockstream.gdk.params.Convert
import java.text.DateFormat
import java.text.NumberFormat
import java.util.*

fun getFiatCurrency(session: GreenSession): String{
    return session.getSettings()?.pricing?.currency ?: "N/A"
}

fun getBitcoinOrLiquidUnit(session: GreenSession): String{
    val unit = session.getSettings()?.unit ?: "N/A"
    if(session.network.isLiquid) {
        return "L-$unit"
    }
    return unit
}

fun getDecimals(unit: String): Int {
    return when(unit.toLowerCase(Locale.ROOT)) {
        "btc" -> 8
        "mbtc" -> 5
        "ubtc", "bits", "\u00B5btc" -> 2
        else -> 0
    }
}

fun getNumberFormat(decimals: Int, locale: Locale = Locale.getDefault()): NumberFormat {
    val instance = NumberFormat.getInstance(locale)
    instance.minimumFractionDigits = decimals
    instance.maximumFractionDigits = decimals
    return instance
}

fun Long.feeRateWithUnit(): String {
    val feePerByte = this / 1000.0
    return getNumberFormat(2).format(feePerByte) + " satoshi / vbyte"
}

fun Double.feeRateWithUnit(): String {
    return getNumberFormat(2).format(this) + " satoshi / vbyte"
}

fun CharSequence.parse(decimals: Int = 2): Number? = this.toString().parse(decimals)

fun String.parse(decimals: Int = 2): Number? {
    return try {
        getNumberFormat(decimals).parse(this)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun Balance.fiat(withUnit: Boolean = true): String {
    return try {
        val value = fiat.toDouble()
        getNumberFormat(2).format(value)
    } catch (e: Exception) {
        "N.A."
    } + if (withUnit) " $fiatCurrency" else ""
}

fun Balance.btc(session: GreenSession, withUnit: Boolean = true): String {
    return this.btc(session.getSettings()?.unit ?: "BTC", withUnit)
}

private fun Balance.btc(unit: String, withUnit: Boolean = true): String {
    return try {
        val value = getValue(unit).toDouble()
        getNumberFormat(getDecimals(unit)).format(value)

    } catch (e: Exception) {
        "N.A."
    } + if (withUnit) " ${unit}" else ""
}

fun Long.btc(settings: Settings, withUnit: Boolean = true): String {
    return try {
        getNumberFormat(getDecimals(settings.unit)).format(this)
    } catch (e: Exception) {
        "N.A."
    } + if (withUnit) " ${settings.unit}" else ""
}

fun Balance.asset(withUnit: Boolean = true): String {
    return try {
        getNumberFormat(assetInfo?.precision ?: 0).format(assetValue?.toDouble() ?: satoshi)
    } catch (e: Exception) {
        "N.A."
    } + if (withUnit) " ${assetInfo?.ticker ?: ""}" else ""
}

fun Long.toBTCLook(session: GreenSession, withUnit: Boolean = true, withDirection: Transaction.Type? = null): String {
    val look = session.convertAmount(Convert(this)).btc(session, withUnit = withUnit)

    withDirection?.let {
        if(it == Transaction.Type.REDEPOSIT || it == Transaction.Type.OUT){
            return "-$look"
        }
    }

    return look
}

fun Long.toAssetLook(session: GreenSession, assetId: String, withUnit: Boolean = true, withDirection: Transaction.Type? = null): String {
    val look = session.convertAmount(Convert(this, session.getAssets().assets[assetId])).asset(withUnit = withUnit)

    withDirection?.let {
        if(it == Transaction.Type.REDEPOSIT || it == Transaction.Type.OUT){
            return "-$look"
        }
    }

    return look
}

fun Date.format(): String = DateFormat.getDateInstance(DateFormat.MEDIUM).format(this)
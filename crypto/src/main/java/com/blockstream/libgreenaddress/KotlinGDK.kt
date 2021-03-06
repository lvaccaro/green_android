package com.blockstream.libgreenaddress

import com.blockstream.gdk.params.ConnectionParams
import com.blockstream.gdk.data.PinData
import com.blockstream.gdk.data.Settings
import com.blockstream.gdk.data.TwoFactorMethodConfig
import com.blockstream.gdk.params.*
import kotlinx.serialization.json.JsonElement

typealias GASession = Any
typealias GAAuthHandler = Any

class KotlinGDK {
    fun init(converter: GDK.JSONConverter, config: InitConfig) = GDK.init(converter, config)

    fun setNotificationHandler(notificationHandler: GDK.NotificationHandler) {
        GDK.setNotificationHandler(notificationHandler)
    }

    fun createSession(): GASession = GDK.create_session()
    fun destroySession(session: GASession): GASession = GDK.destroy_session(session)

    fun connect(session: GASession, params: ConnectionParams) = GDK.connect(session, params)
    fun disconnect(session: GASession) = GDK.disconnect(session)

    fun httpRequest(session: GASession, data: JsonElement) = GDK.http_request(session, data)

    fun registerUser(session: GASession, device: JsonElement, mnemonic: String): GAAuthHandler =
        GDK.register_user(session, device, mnemonic)

    fun loginWatchOnly(session: GASession, username: String, password: String) =
        GDK.login_watch_only(
            session,
            username,
            password
        )

    // TODO replace JsonElement with the appropriate data class
    fun loginWithMnemonic(
        session: GASession,
        device: JsonElement,
        mnemonic: String,
        password: String
    ): GAAuthHandler = GDK.login(session, device, mnemonic, password)

    fun loginWithPin(
        session: GASession,
        pin: String,
        pinData: PinData
    ): GAAuthHandler = GDK.login_with_pin(session, pin, pinData)

    fun setPin(session: GASession, mnemonicPassphrase: String, pin: String, device: String) =
        GDK.set_pin(session, mnemonicPassphrase, pin, device)

    // TODO at the moment the encrypted mnemonic is not supported so we are always passing the empty string
    fun getMnemonicPassphrase(session: GASession) =
        GDK.get_mnemonic_passphrase(session, "")

    fun getReceiveAddress(session: GASession, params: ReceiveAddressParams): GAAuthHandler =
        GDK.get_receive_address(session, params)

    fun refreshAssets(session: GASession, params: AssetsParams) =
        GDK.refresh_assets(session, params)

    fun createSubAccount(session: GASession, params: SubAccountParams): GAAuthHandler =
        GDK.create_subaccount(session, params)

    fun getSubAccounts(session: GASession): GAAuthHandler = GDK.get_subaccounts(session)
    fun getSubAccount(session: GASession, index: Long): GASession =
        GDK.get_subaccount(session, index)

    fun renameSubAccount(session: GASession, index: Long, name: String) = GDK.rename_subaccount(session, index, name)

    fun getBalance(session: GASession, details: BalanceParams): GAAuthHandler =
        GDK.get_balance(session, details)

    fun getTransactions(session: GASession, details: TransactionParams): GAAuthHandler =
        GDK.get_transactions(session, details)

    fun getTwoFactorConfig(session: GASession) = GDK.get_twofactor_config(session)

    fun changeSettingsTwoFactor(session: GASession, method: String, methodConfig: TwoFactorMethodConfig) = GDK.change_settings_twofactor(session, method, methodConfig)

    fun getWatchOnlyUsername(session: GASession) = GDK.get_watch_only_username(session)

    fun setWatchOnly(session: GASession, username : String, password: String) = GDK.set_watch_only(session, username, password)

    fun changeSettings(session: GASession, settings: Settings): GAAuthHandler =
        GDK.change_settings(session, settings)

    fun getSettings(session: GASession) = GDK.get_settings(session)

    fun getAvailableCurrencies(session: GASession) = GDK.get_available_currencies(session)

    fun getAuthHandlerStatus(gaAuthHandler: GAAuthHandler): JsonElement =
        GDK.auth_handler_get_status(gaAuthHandler) as JsonElement

    fun authHandlerCall(gaAuthHandler: GAAuthHandler) = GDK.auth_handler_call(gaAuthHandler)
    fun authHandlerRequestCode(method: String, gaAuthHandler: GAAuthHandler) =
        GDK.auth_handler_request_code(gaAuthHandler, method)

    fun authHandlerResolveCode(code: String, gaAuthHandler: GAAuthHandler) =
        GDK.auth_handler_resolve_code(gaAuthHandler, code)

    fun destroyAuthHandler(gaAuthHandler: GAAuthHandler) = GDK.destroy_auth_handler(gaAuthHandler)

    fun twofactorChangeLimits(session: GASession, limits: Limits) = GDK.twofactor_change_limits(session, limits)


    fun convertAmount(session: GASession, convert: Convert) = GDK.convert_amount(session, convert)

    fun getNetworks() = GDK.get_networks()

    fun generateMnemonic(): String = GDK.generate_mnemonic()

    companion object {
        const val GA_OK = GDK.GA_OK
        const val GA_ERROR = GDK.GA_ERROR
        const val GA_RECONNECT = GDK.GA_RECONNECT
        const val GA_SESSION_LOST = GDK.GA_SESSION_LOST
        const val GA_TIMEOUT = GDK.GA_TIMEOUT
        const val GA_NOT_AUTHORIZED = GDK.GA_NOT_AUTHORIZED
        const val GA_NONE = GDK.GA_NONE
        const val GA_INFO = GDK.GA_INFO
        const val GA_DEBUG = GDK.GA_DEBUG
        const val GA_TRUE = GDK.GA_TRUE
        const val GA_FALSE = GDK.GA_FALSE
    }
}
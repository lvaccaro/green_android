package com.blockstream.gdk.data

import com.blockstream.gdk.serializers.DateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Transaction(
    @SerialName("addressees") val addressees: List<String>,
    @SerialName("block_height") val blockHeight: Long,
    @SerialName("can_cpfp") val canCPFP: Boolean,
    @SerialName("can_rbf") val canRBF: Boolean,

    @Serializable(with = DateSerializer::class)
    @SerialName("created_at") val createdAt: Date,

    @SerialName("inputs") val inputs: List<InputOutput>,
    @SerialName("outputs") val outputs: List<InputOutput>,

    @SerialName("fee") val fee: Long,
    @SerialName("fee_rate") val feeRate: Long,

    @SerialName("has_payment_request") val hasPaymentRequest: Boolean,
    @SerialName("instant") val instant: Boolean,
    @SerialName("memo") val memo: String,
    @SerialName("rbf_optin") val rbfOptin: Boolean,

    @SerialName("server_signed") val serverSigned: Boolean,

    @SerialName("spv_verified") val spvVerified: String,
    @SerialName("transaction_size") val txSize: Long,
    @SerialName("transaction_vsize") val txVSize: Long,
    @SerialName("transaction_weight") val txWeight: Long,

    @SerialName("txhash") val txHash: String,

    @SerialName("type") val type: String,
    @SerialName("user_signed") val userSigned: Boolean,

    @SerialName("satoshi") val satoshi: Map<String, Long>
) {

    enum class Type {
        OUT, IN, REDEPOSIT
    }

    val txType: Type
        get() = when (type) {
            "outgoing" -> Type.OUT
            "incoming" -> Type.IN
            "redeposit" -> Type.REDEPOSIT
            else -> Type.OUT
        }

    val isIn
        get() = txType == Type.IN

}
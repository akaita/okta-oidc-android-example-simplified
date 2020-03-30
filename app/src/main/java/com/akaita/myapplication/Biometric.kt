/*
* Created by Mikel Pascual (mikel@4rtstudio.com) on 23/03/2020.
*/
package com.akaita.myapplication

import androidx.biometric.BiometricConstants.ERROR_NEGATIVE_BUTTON
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import java.util.concurrent.Executors


class Biometric(
    fragmentActivity: FragmentActivity,
    onSuccessListener: () -> Unit,
    onCancelListener: () -> Unit,
    onErrorListener: (Int, String) -> Unit
) {
    private val mCallback: BiometricPrompt.AuthenticationCallback =
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                if (errorCode == ERROR_NEGATIVE_BUTTON) {
                    onCancelListener()
                } else {
                    onErrorListener(errorCode, errString.toString())
                }
                prompt.cancelAuthentication()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                onSuccessListener()
            }
        }

    private val prompt: BiometricPrompt
    private val promptInfo: BiometricPrompt.PromptInfo

    init {
        prompt = BiometricPrompt(fragmentActivity, Executors.newSingleThreadExecutor(), mCallback)
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Confirm credentials")
            .setDeviceCredentialAllowed(true)
            .setConfirmationRequired(true)
            .build()
    }

    fun show() {
        prompt.authenticate(promptInfo)
    }
}

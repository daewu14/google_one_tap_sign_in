package id.daewu14.google_one_tap_sign_in

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat.startIntentSenderForResult
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry
import java.util.*

/**
 * GoogleOneTapSignInPlugin
 *
 * @author
 *
 * Daewu Bintara
 * (daewu.bintara1996@gmail.com) | Indonesia
 *
 * Tuesday, 04/01/22 18:29
 *
 * Enjoy coding ☕
 *
 */
class GoogleOneTapSignInPlugin: FlutterPlugin, MethodCallHandler, MethodContract, PluginRegistry.ActivityResultListener, ActivityAware {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel

  private lateinit var oneTapClient: SignInClient
  private lateinit var signInRequest: BeginSignInRequest
  private lateinit var activity: Activity
  private lateinit var context: Context
  private var webCLientId: String? = null
  private lateinit var result: MethodChannel.Result


  private var DAEWU: String = "---DAEWU14---"

  private val REQ_ONE_TAP = 2  // Can be any integer unique to the Activity
  private var showOneTapUI = true

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "google_one_tap_sign_in")
    context = flutterPluginBinding.applicationContext
    channel.setMethodCallHandler(this)
  }


  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    this.result = result
    when {
      call.method == "getPlatformVersion" -> {
        result.success("Android ${android.os.Build.VERSION.RELEASE}")
      }
      call.method == "startSignIn" -> {
        webCLientId = call.argument("web_client_id")
        startSignIn()
      }
      else -> {
        result.notImplemented()
      }
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  override fun startSignIn() {
    if (webCLientId == null) {
      return
    }

    oneTapClient = Identity.getSignInClient(activity)

    oneTapClient.signOut()

    signInRequest = BeginSignInRequest.builder()
      .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
        .setSupported(true)
        .build())
      .setGoogleIdTokenRequestOptions(
        BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
          .setSupported(true)
          // Your server's client ID, not your Android client ID.
          .setServerClientId(webCLientId)
          // Only show accounts previously used to sign in.
          .setFilterByAuthorizedAccounts(false)
          .build())
      // Automatically sign in when exactly one credential is retrieved.
      .setAutoSelectEnabled(true)
      .build()

    oneTapClient.beginSignIn(signInRequest)
      .addOnSuccessListener { rss ->
        Log.d("ON LOGIN","PROCESS")
        activity.startIntentSenderForResult(
          rss.pendingIntent.intentSender, REQ_ONE_TAP,
          null, 0, 0, 0, null)
      }
      .addOnFailureListener { e ->
        e.message?.let { Log.d("Error", it) }
        result.success(null)
      }
      .addOnCanceledListener {
        result.success(null)
      }

  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
    when {
      requestCode == REQ_ONE_TAP -> {
        if (data != null) {
          try {
            val credential = oneTapClient.getSignInCredentialFromIntent(data)
            val idToken = credential.googleIdToken
            val username = credential.id
            val password = credential.password
            val displayName = credential.displayName
            val googleIdToken = credential.googleIdToken
            val id = credential.id
            result.success("{\"credential\":\"$credential\", \"id_token\":\"$idToken\",\"username\":\"$username\",\"password\":\"$password\",\"display_name\":\"$displayName\",\"google_id_token\":\"$googleIdToken\",\"id\":\"$id\"}")
            return true
          } catch (e: ApiException) {
            result.success(null)
            return false
          }
        } else {
          result.success(null)
          return true
        }
      }
      else -> {
        result.success(null)
        return false
      }
    }
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activity = binding.activity
    binding.addActivityResultListener(this)
  }

  override fun onDetachedFromActivityForConfigChanges() {}

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {}

  override fun onDetachedFromActivity() {}


}

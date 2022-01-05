package id.daewu14.google_one_tap_sign_in

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.annotation.NonNull
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.FlutterPlugin.FlutterPluginBinding
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry

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
  private lateinit var pluginBinding: FlutterPluginBinding

  private var activity: Activity? = null
  private var context: Context? = null
  private var webCLientId: String? = null
  private var result: MethodChannel.Result? = null

  /**
   * Get binding to controll all the activity of Flutter App from Flutter Engine
   */
  private var binding: ActivityPluginBinding? = null


  private var DAEWU: String = "---DAEWU14---"
  private var channelName: String = "google_one_tap_sign_in"

  /**
   * Unique [REQUEST_CODE]
   */
  private val REQ_ONE_TAP = 14081996

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    setupPlugin(null, flutterPluginBinding)
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
    detachPlugin()
    channel.setMethodCallHandler(null)
  }

  override fun startSignIn() {
    if (webCLientId == null) {
      return
    }

    if (result == null) {
      return
    }

    if (activity == null) {
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
        activity!!.startIntentSenderForResult(
          rss.pendingIntent.intentSender, REQ_ONE_TAP,
          null, 0, 0, 0, null)
      }
      .addOnFailureListener { e ->
        e.message?.let {
          Log.d("Error", it)
          if (it.contains("Caller has been temporarily blocked due to too many canceled sign-in prompts.")) {
            result!!.success("TEMPORARY_BLOCKED")
          } else {
            result!!.success(null)
          }
        }
        if (e.message == null) {
          result!!.success(null)
        }
      }
      .addOnCanceledListener {
        result!!.success("CANCELED")
      }

  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
    if (binding == null) {
      return false
    }
    Log.d(DAEWU, "Req Code : $requestCode")
    result?.let {
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
              Log.d(DAEWU, "~~~~ ☕ ONE TAP SUCCESS ☕ ~~~~")
              it.success("{\"credential\":\"$credential\", \"id_token\":\"$idToken\",\"username\":\"$username\",\"password\":\"$password\",\"display_name\":\"$displayName\",\"google_id_token\":\"$googleIdToken\",\"id\":\"$id\"}")
              return true
            } catch (e: ApiException) {
              Log.d(DAEWU, "~~~~ !! ONE TAP ApiException !! ~~~~")
              it.success(null)
              return false
            }
          } else {
            Log.d(DAEWU, "~~~~ !! ONE TAP Data Null !! ~~~~")
            it.success(null)
            return true
          }
        }
        else -> {
          return false
        }
      }
    }
    Log.d(DAEWU, "~~~~ !! ONE TAP Result Unknown !! ~~~~")
    return false
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    setupPlugin(binding, null)
  }

  override fun onDetachedFromActivityForConfigChanges() {
    detachPlugin()
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    onAttachedToActivity(binding)
  }

  override fun onDetachedFromActivity() {}

  /**
   * To Detach this Plugin
   */
  private fun detachPlugin() {
    if (binding == null) {
      return
    }
    this.binding!!.removeActivityResultListener(this)
    this.binding = null
  }

  /**
   * Setup this Plugin
   */
  private fun setupPlugin(binding: ActivityPluginBinding?, flutterPluginBinding: FlutterPlugin.FlutterPluginBinding?) {

    // let Binding the FlutterPluginBinding
    flutterPluginBinding?.let {
      pluginBinding = it
    }

    // Let Plugin Binding
    pluginBinding.let {
      context = it.applicationContext
      channel = MethodChannel(it.binaryMessenger, channelName)
      channel.setMethodCallHandler(this)
    }

    // Let Binding the ActivityPluginBinding
    binding?.let {
      activity = it.activity
      this.binding = it
      this.binding!!.addActivityResultListener(this)
    }
  }

}

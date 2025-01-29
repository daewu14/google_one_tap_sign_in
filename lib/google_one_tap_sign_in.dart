import 'dart:async';
import 'dart:convert';

import 'package:flutter/services.dart';
import 'package:google_one_tap_sign_in/exception.dart';
import 'package:google_one_tap_sign_in/platform_support.dart';

part 'models/sign_in_result.dart';

///
/// createdby Daewu Bintara
///
/// Tuesday, 04/01/22 18:29
///
/// Enjoy coding ☕
///
class GoogleOneTapSignIn {

  /// [GoogleOneTapSignIn] channel [MethodChannel]
  static const MethodChannel _channel = MethodChannel('google_one_tap_sign_in');

  ///
  /// Start Simple Sign In One Tap Sign In
  ///
  /// [String] webClientId is required
  ///
  /// You can get the WebClientId from your console project
  ///
  /// return [SignInResult]
  ///
  /// -----------------------------------------------------------------------------
  ///
  /// Example
  ///
  /// ```dart
  ///
  /// void _onSignIn() async {
  ///   var data = await GoogleOneTapSignIn.startSignIn(webClientId: _webClientId);
  ///   if (data != null) {
  ///     /// Whatever you do with [SignInResult] data
  ///     print("Id Token : ${data.idToken ?? "-"}");
  ///     print("ID : ${data.id ?? "-"}");
  ///   }
  /// }
  ///
  /// ```
  ///
  static Future<SignInResult?> startSignIn(
      {required String webClientId}) async {

    // check support platform
    if (!GoogleOneTapSignInPlatformSupport.checkPlatformSupport()) {
      throw GoogleOneTapSignInException("Unsupported for this platform, for now support on : "+GoogleOneTapSignInPlatformSupport.platformSupport().join(","));
    }

    var data = await _channel
        .invokeMethod('startSignIn', {"web_client_id": webClientId});
    if (data == null) return null;
    if (data == "TEMPORARY_BLOCKED") return null;
    var json = jsonDecode(data);
    return SignInResult.fromMap(json);
  }


  ///
  /// Start Advance Sign In One Tap Sign In
  ///
  /// [String] webClientId is required
  ///
  /// You can get the WebClientId from your console project
  ///
  /// return [SignInData]
  ///
  /// -----------------------------------------------------------------------------
  ///
  /// Example
  ///
  /// ```dart
  ///
  /// void _onSignInWithHandle() async {
  ///   var result = await GoogleOneTapSignIn.handleSignIn(webClientId: _webClientId);
  ///
  ///   if (result.isTemporaryBlock) {
  ///     // TODO: Tell your users about this status
  ///     print("Temporary BLOCK");
  ///   }
  ///
  ///   if (result.isCanceled) {
  ///     // TODO: Tell your users about this status
  ///     print("Canceled");
  ///   }
  ///
  ///   if (result.isFail) {
  ///     // TODO: Tell your users about this status
  ///   }
  ///
  ///   if (result.isOk) {
  ///     // TODO: Whatever you do with [SignInResult] data
  ///     print("OK");
  ///     print("Id Token : ${result.data?.idToken ?? "-"}");
  ///     print("Email : ${result.data?.username ?? "-"}");
  ///   }
  /// }
  ///
  /// ```
  ///
  static Future<SignInData> handleSignIn({required String webClientId}) async {
    // check support platform
    if (!GoogleOneTapSignInPlatformSupport.checkPlatformSupport()) {
      throw GoogleOneTapSignInException("Unsupported for this platform, for now support on : "+GoogleOneTapSignInPlatformSupport.platformSupport().join(","));
    }

    var signInData = SignInData();
    var data = await _channel
        .invokeMethod('startSignIn', {"web_client_id": webClientId});

    switch (data) {
      case "TEMPORARY_BLOCKED":
        signInData = SignInData(status: OneTapStatus.TEMPORARY_BLOCK);
        break;
      case "CANCELED":
        signInData = SignInData(status: OneTapStatus.CANCELED);
        break;
      default:
        if (data != null) {
          try {
            var json = jsonDecode(data);
            var userData = SignInResult.fromMap(json);
            signInData = SignInData(status: OneTapStatus.OK, data: userData);
          } catch (e) {
            signInData = SignInData(status: OneTapStatus.UNKNOWN);
          }
        }
    }

    return signInData;
  }
}

/// createdby Daewu Bintara
/// Tuesday, 04/01/22 18:29
/// Enjoy coding ☕

import 'dart:async';
import 'dart:convert';
import 'dart:developer';

import 'package:flutter/services.dart';

part 'models/sign_in_result.dart';

class GoogleOneTapSignIn {

  static const MethodChannel _channel = MethodChannel('google_one_tap_sign_in');

  ///
  /// Start Sign In One Tap Sign In
  ///
  /// [String] webClientId is required
  ///
  /// You can get the WebClientId from your console project
  ///
  /// return [SignInResult]
  ///
  static Future<SignInResult?> startSignIn({required String webClientId}) async {
    var data = await _channel.invokeMethod('startSignIn', {
      "web_client_id" : webClientId
    });
    if (data == null) return null;
    var json = jsonDecode(data);
    return SignInResult.fromMap(json);
  }
}

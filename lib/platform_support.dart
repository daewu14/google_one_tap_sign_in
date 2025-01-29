
import 'dart:io';

class GoogleOneTapSignInPlatformSupport {

  /// Checking platform support
  static bool checkPlatformSupport() {
    if (Platform.isAndroid) return true;
    return false;
  }

  /// List of platform supported
  static List<String> platformSupport() {
    return ["Android"];
  }

}
# Google One Tap Sign In

Google One Tap Sign In (Android)

A Flutter Plugin for [Google One Tap Sign In](https://developers.google.com/identity/one-tap/android/get-started)

## Getting Started

To access Google Sign-In, you'll need to make sure to [register your application](https://firebase.google.com/docs/android/setup).

* Add to your pubspec.yaml
  ```
  dependencies:  
    google_one_tap_sign_in: [any]
  ```

* Add Google Service
  ```
  classpath 'com.google.gms:google-services:[x.x.x]'
  ```
* Import plugin
  ```
  import 'package:google_one_tap_sign_in/google_one_tap_sign_in.dart';
  ```
* Handle Method
  ```
  // Your Web Client Id  
  final String _webClientId = "[XXXXXXXXXXX]";
  void _onSignIn() async {
      var data = await GoogleOneTapSignIn.startSignIn(webClientId: _webClientId);
      if (data != null) {
        // Whatever you do with [SignInResult] data
        print("Id Token : ${data.idToken ?? "-"}");
        print("ID : ${data.id ?? "-"}");
      }
  }
  ```

## Example
Find the example wiring in the [Google One Tap Sign In](https://github.com/daewu14/google_one_tap_sign_in/blob/master/example/lib/main.dart)
import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:google_one_tap_sign_in/google_one_tap_sign_in.dart';

void main() {
  const MethodChannel channel = MethodChannel('google_one_tap_sign_in');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return SignInResult(
        idToken: "XXXXXXXXX",
        credential: "XXXXXXXXX",
        displayName: "XXXXXXXXX",
        googleIdToken: "XXXXXXXXX",
        id: "XXXXXXXXX",
        password: "XXXXXXXXX",
        username: "XXXXXXXXX",
      );
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('startSignIn', () async {
    expect(await GoogleOneTapSignIn.startSignIn(webClientId: "XXXXX"), SignInResult(
      idToken: "XXXXXXXXX",
      credential: "XXXXXXXXX",
      displayName: "XXXXXXXXX",
      googleIdToken: "XXXXXXXXX",
      id: "XXXXXXXXX",
      password: "XXXXXXXXX",
      username: "XXXXXXXXX",
    ));
  });
}

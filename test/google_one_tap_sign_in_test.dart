import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:google_one_tap_sign_in/google_one_tap_sign_in.dart';

void main() {
  const MethodChannel channel = MethodChannel('google_one_tap_sign_in');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await GoogleOneTapSignIn.platformVersion, '42');
  });
}

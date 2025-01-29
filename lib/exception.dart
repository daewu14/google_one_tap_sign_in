class GoogleOneTapSignInException implements Exception {
  /// Creates a [GoogleOneTapSignInException] with an human-readable
  /// error message.
  GoogleOneTapSignInException(this.message);

  /// A human-readable error message.
  final String message;

  @override
  String toString() => 'GoogleOneTapSignInException($message)';
}
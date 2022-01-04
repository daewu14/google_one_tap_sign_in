/// createdby Daewu Bintara
/// Tuesday, 04/01/22 18:29
/// Enjoy coding ☕

part of '../google_one_tap_sign_in.dart';

class SignInResult {

  SignInResult(
      {this.credential,
        this.idToken,
        this.username,
        this.password,
        this.displayName,
        this.googleIdToken,
        this.id});

  final credential;
  final idToken;
  final username;
  final password;
  final displayName;
  final googleIdToken;
  final id;

  factory SignInResult.fromMap(Map<String, dynamic> json) => SignInResult(
    credential: json["credential"] == null ? null : json["credential"],
    username: json["username"] == null ? null : json["username"],
    password: json["password"] == null ? null : json["password"],
    idToken: json["id_token"] == null ? null : json["id_token"],
    displayName: json["display_name"] == null ? null : json["display_name"],
    googleIdToken: json["google_id_token"] == null ? null : json["google_id_token"],
    id: json["id"] == null ? null : json["id"],
  );
}
part of '../google_one_tap_sign_in.dart';


///
/// createdby Daewu Bintara
///
/// Tuesday, 04/01/22 18:29
///
/// Enjoy coding ☕
///
/// User data when success sign in
///
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

  ///
  /// Parsing data from json to this Model
  ///
  factory SignInResult.fromMap(Map<String, dynamic> json) => SignInResult(
    credential: json["credential"] == null ? null : json["credential"],
    username: json["username"] == null ? null : json["username"],
    password: json["password"] == null ? null : json["password"],
    idToken: json["id_token"] == null ? null : json["id_token"],
    displayName: json["display_name"] == null ? null : json["display_name"],
    googleIdToken: json["google_id_token"] == null ? null : json["google_id_token"],
    id: json["id"] == null ? null : json["id"],
  );

  ///
  /// To Json method to use in stringify
  ///
  Map<String, dynamic> toJson(){
    return {
      "credential": credential,
      "username": username,
      "password": password,
      "id_token": idToken,
      "display_name": displayName,
      "google_id_token": googleIdToken,
      "id": id
    };
  }
  
  ///
  /// To String method to print
  /// 
  @override
  String toString(){
    return toJson().toString();
  }
}

///
/// Wrapper sign in result with [OneTapStatus] enum
///
class SignInData {

  ///
  /// **Setup default :**
  ///
  /// status is [OneTapStatus.FAIL]
  ///
  /// data is null
  ///
  SignInData({this.status = OneTapStatus.FAIL, this.data});


  final OneTapStatus status;
  late SignInResult? data;

  /// Getter
  ///
  /// return [bool]
  ///
  /// You Rock 🤘 🤘 🤘 🤘
  /// User will get this [OneTapStatus.OK]
  bool get isOk => status == OneTapStatus.OK;

  /// Getter
  ///
  /// return [bool]
  ///
  /// When to many attemp and canceled action
  /// User will get this [OneTapStatus.TEMPORARY_BLOCK]
  bool get isTemporaryBlock => status == OneTapStatus.TEMPORARY_BLOCK;

  /// Getter
  ///
  /// return [bool]
  ///
  /// We dont know this status is Possible or Not
  /// User will get this [OneTapStatus.BLOCK]
  ///
  /// [!] For now its never called
  ///
  bool get isBlock => status == OneTapStatus.BLOCK;

  /// Getter
  ///
  /// return [bool]
  ///
  /// When geting unknown
  /// User will get this [OneTapStatus.UNKNOWN]
  bool get isUnknown => status == OneTapStatus.UNKNOWN;

  /// Getter
  ///
  /// return [bool]
  ///
  /// Its posible when no internet or other condition inner [KotlinException]
  /// User will get this [OneTapStatus.FAIL]
  bool get isFail => status == OneTapStatus.FAIL;


  /// Getter
  ///
  /// return [bool]
  ///
  /// When the user intentionally cancels the action
  /// User will get this [OneTapStatus.CANCELED]
  bool get isCanceled => status == OneTapStatus.CANCELED;

}

///
/// Status One Tap Sign In
///
enum OneTapStatus {

  /// When to many attemp and canceled action
  /// User will get this [OneTapStatus.TEMPORARY_BLOCK]
  TEMPORARY_BLOCK,

  /// We dont know this status is Possible or Not
  /// User will get this [OneTapStatus.BLOCK]
  ///
  /// [!] For now its never called
  ///
  BLOCK,

  /// When geting unknown
  /// User will get this [OneTapStatus.UNKNOWN]
  UNKNOWN,

  /// When the user intentionally cancels the action
  /// User will get this [OneTapStatus.CANCELED]
  CANCELED,

  /// Its posible when no internet or other condition inner [KotlinException]
  /// User will get this [OneTapStatus.FAIL]
  FAIL,

  /// You Rock 🤘 🤘 🤘 🤘
  /// User will get this [OneTapStatus.OK]
  OK,

}

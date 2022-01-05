## 1.0.0

* Update README.md

## 1.0.0

* Add new features 'GoogleOneTapSignIn.handleSignIn(webClientId: "[YOUR-CLIENT-ID]")'
    ```
      void _onSignInWithHandle() async {
        var result = await GoogleOneTapSignIn.handleSignIn(webClientId: _webClientId);
    
        if (result.isTemporaryBlock) {
          // TODO: Tell your users about this status
          print("Temporary BLOCK");
        }
    
        if (result.isCanceled) {
          // TODO: Tell your users about this status
          print("Canceled");
        }
    
        if (result.isFail) {
          // TODO: Tell your users about this status
        }
    
        if (result.isOk) {
          // TODO: Whatever you do with [SignInResult] data
          print("OK");
          print("Id Token : ${result.data?.idToken ?? "-"}");
          print("Email : ${result.data?.username ?? "-"}");
        }
      }
    ```

## 0.1.1

* Update the Changelog

## 0.1.0

* Minor bug fixing 'onActivityResult' method

## 0.0.2

* Update README.md

## 0.0.1

* Initial Release
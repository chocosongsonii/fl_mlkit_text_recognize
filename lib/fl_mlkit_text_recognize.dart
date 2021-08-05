import 'package:fl_camera/fl_camera.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

export 'package:fl_camera/fl_camera.dart';

part 'src/method_call.dart';

const MethodChannel _flMlKitTextRecognizeChannel =
    MethodChannel(_flMlKitTextRecognize);
const String _flMlKitTextRecognize = 'fl.mlkit.text.recognize';

bool get _supportPlatform {
  if (!kIsWeb && (_isAndroid || _isIOS)) return true;
  print('Not support platform for $defaultTargetPlatform');
  return false;
}

bool get _isAndroid => defaultTargetPlatform == TargetPlatform.android;

bool get _isIOS => defaultTargetPlatform == TargetPlatform.iOS;

# fl_mlkit_text_recognize

基于[Google ML Kit](https://developers.google.com/ml-kit/vision/text-recognition/v2)实现快速稳定识别文字功能，支持Android\IOS

Realize fast and stable text recognition function based
on [Google ML Kit](https://developers.google.com/ml-kit/vision/text-recognition/v2), and support Android \ IOS

相机相关功能依赖于 [fl_camera](https://pub.dev/packages/fl_camera)

Camera related functions depend on [fl_camera](https://pub.dev/packages/fl_camera)

### 使用 use

- ios 添加相机权限 Add camera permissions to IOS

```plist
<key>NSCameraUsageDescription</key>
<string>是否允许FlMlKitScanning使用你的相机？</string>
```

- 预览 preview

```dart

Widget build(BuildContext context) {
  return FlMlKitTextRecognize(

    /// 需要预览的相机
    /// Camera ID to preview
      camera: camera,

      /// 预览相机支持的分辨率
      /// Preview the resolution supported by the camera
      resolution: CameraResolution.high,

      /// 是否自动扫描 默认为[true]
      /// Auto scan defaults to [true]
      autoScanning: false,

      /// 显示在预览上层
      /// Display above preview box
      overlay: const ScannerLine(),

      /// 相机预览位置
      /// How a camera box should be inscribed into another box.
      fit: BoxFit.fitWidth,

      /// 闪光灯状态
      /// Flash status
      onFlashChange: (FlashState state) {
        showToast('$state');
      },

      /// 缩放变化
      /// zoom ratio
      onFlashChange: (FlashState state) {
        showToast('$state');
      },

      /// 相机在未初始化时显示的UI
      /// The UI displayed when the camera is not initialized
      uninitialized: Container(
          color: Colors.black,
          alignment: Alignment.center,
          child:
          const Text(
              'Camera not initialized', style: TextStyle(color: Colors.white))),

      /// 识别文本的语言类型
      /// Identifies the language type of the text
      recognizedLanguage: RecognizedLanguage.latin,

      /// 文本识别回调
      /// Text recognized callback
      onListen: (AnalysisTextModel data) {
        if (data.text != null && data.text!.isNotEmpty) {

        }
      });
}

```

- 方法 method

```dart
void func() {

  /// 设置识别文本的语言类型
  /// Sets the language type that identifies the text
  FlMlKitTextRecognizeMethodCall().setRecognizedLanguage();

  /// 识别图片字节
  /// Identify picture bytes
  FlMlKitTextRecognizeMethodCall().scanImageByte();

  /// 打开\关闭 闪光灯 
  /// Turn flash on / off
  FlMlKitTextRecognizeMethodCall().setFlashMode();

  /// 相机缩放
  /// Camera zoom
  FlMlKitTextRecognizeMethodCall().setZoomRatio();

  /// 获取识别状态
  /// get scan state
  FlMlKitTextRecognizeMethodCall().getScanState();

  /// 暂停识别
  /// Pause recognition
  FlMlKitTextRecognizeMethodCall().pause();

  /// 开始识别
  /// Start recognition
  FlMlKitTextRecognizeMethodCall().start();
}

```
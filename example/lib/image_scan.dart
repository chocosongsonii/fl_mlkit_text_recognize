import 'dart:io';

import 'package:example/main.dart';
import 'package:fl_mlkit_text_recognize/fl_mlkit_text_recognize.dart';
import 'package:flutter/material.dart';
import 'package:flutter_curiosity/flutter_curiosity.dart';
import 'package:flutter_waya/flutter_waya.dart';
import 'package:permission_handler/permission_handler.dart';

class ImageScanPage extends StatefulWidget {
  const ImageScanPage({Key? key}) : super(key: key);

  @override
  _ImageScanPageState createState() => _ImageScanPageState();
}

class _ImageScanPageState extends State<ImageScanPage> {
  String? path;
  AnalysisTextModel? model;

  @override
  Widget build(BuildContext context) {
    return ExtendedScaffold(
        appBar: AppBarText('San file image'),
        padding: const EdgeInsets.all(20),
        isScroll: true,
        children: <Widget>[
          ElevatedText(onPressed: openGallery, text: 'Select Picture'),
          ElevatedText(onPressed: scanByte, text: 'Scanning'),
          ShowText('path', path),
          if (path != null && path!.isNotEmpty)
            Container(
                width: double.infinity,
                height: 300,
                margin:
                    const EdgeInsets.symmetric(vertical: 10, horizontal: 40),
                child: Image.file(File(path!))),
          const SizedBox(height: 20),
          if (model == null) const ShowText('Unrecognized', 'Unrecognized'),
          ShowCode(model, expanded: false)
        ]);
  }

  Future<void> scanByte() async {
    if (path == null || path!.isEmpty) {
      return showToast('Please select a picture');
    }
    bool hasPermission = false;
    if (isAndroid) hasPermission = await getPermission(Permission.storage);
    if (isIOS) hasPermission = true;
    if (hasPermission) {
      final File file = File(path!);
      final AnalysisTextModel? data = await FlMlKitTextRecognizeMethodCall()
          .scanImageByte(file.readAsBytesSync());
      if (data != null) {
        model = data;
        setState(() {});
      } else {
        showToast('no data');
      }
    }
  }

  Future<void> openGallery() async {
    bool hasPermission = false;
    if (isAndroid) hasPermission = await getPermission(Permission.storage);
    if (isIOS) hasPermission = true;
    if (hasPermission) {
      final String? data = await Curiosity().gallery.openSystemGallery();
      path = data;
      setState(() {});
    }
  }
}

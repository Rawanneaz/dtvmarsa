import 'package:flutter/material.dart';
import 'screens/login.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(const MarsaMarocApp());
}

class MarsaMarocApp extends StatelessWidget {
  const MarsaMarocApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Marsa Maroc',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: const Color(0xFF00579F)), // Marsa Maroc blue
        useMaterial3: true,
      ),
      home: const LoginScreen(),
    );
  }
}
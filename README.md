# Gemini Camera Translator

An Android app that translates text from the real world: point the camera at a sign, menu, or page, snap a photo, and **Gemini 2.5 Flash** (multimodal) detects the source language, extracts the text, and translates it into your chosen language. Past translations are saved locally so you can revisit them.

Built as a team final project for **CS 492 (Mobile Development) at Oregon State University**, Winter 2026. Mirrored here from the course organization with full commit history.

## Features
- Live camera preview and photo capture (CameraX)
- Image → Gemini multimodal prompt with a strict **JSON response schema** (`originalLanguage`, `translatedLanguage`, `originalText`, `translatedText`) so the reply parses reliably
- Translation history stored with **Room** (SQLite) and shown in a RecyclerView
- Settings screen with user preferences (e.g. font size)

## Tech
Kotlin · Android Fragments + ViewModel/LiveData · Coroutines · CameraX · Google Generative AI SDK (Gemini 2.5 Flash) · Room

## My contributions (Kevin Rubio)
I built the **camera pipeline**: the camera fragment and fragment-container navigation, runtime camera permission handling, the CameraX preview and capture flow, and converting captured `ImageProxy` frames to `Bitmap` for the Gemini request.

## Running
1. Get a Gemini API key from [Google AI Studio](https://aistudio.google.com/).
2. Add it to `local.properties` (never committed): `GEMINI_API_KEY=your_key_here`
3. Open in Android Studio and run on a device/emulator with a camera.

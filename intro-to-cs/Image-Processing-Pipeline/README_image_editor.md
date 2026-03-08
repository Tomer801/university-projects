# Image Processing Pipeline

A Python image editor implementing six fundamental image processing operations from first principles — no OpenCV or PIL for the core algorithms.

---

## Problem Solved

Build an interactive image processing tool that applies grayscale conversion, blurring, resizing, rotation, edge detection, and colour quantization using manual implementations of the underlying mathematics, not library wrappers.

---

## Technical Highlights

| Operation | Implementation |
|---|---|
| RGB → Grayscale | Luminance-weighted formula: `0.299R + 0.587G + 0.114B` applied per pixel |
| Blur | Discrete 2D convolution with an n×n averaging kernel (`apply_kernel`); handles boundary pixels |
| Resize | Bilinear interpolation — computes each output pixel from the four surrounding input pixels, weighted by sub-pixel distance |
| Rotate 90° | Transposes the pixel matrix and reverses row or column order depending on direction |
| Edge Detection | Adaptive thresholding: each pixel is compared against the mean of a local block; outputs a binary image |
| Quantization | Maps continuous intensity range to N uniform buckets; applied per channel for colour images |

---

## Architecture

```
image_editor.py
├── separate_channels / combine_channels  — RGB channel management
├── RGB2grayscale                         — luminance conversion
├── blur_kernel / apply_kernel            — convolution engine
├── bilinear_interpolation / resize       — smooth scaling
├── rotate_90                             — affine rotation
├── get_edges                             — adaptive edge detector
├── quantize / quantize_colored_image     — colour reduction
└── main_loop                             — interactive menu
```

---

## Tech Stack & Concepts

- **Language:** Python 3
- **Key concepts:** 2D convolution, bilinear interpolation, adaptive thresholding, colour quantization, channel manipulation

---

## Run

```bash
python3 image_editor.py input_image.jpg
```

Interactive menu:
```
1. Convert RGB to grayscale
2. Blur image
3. Resize image
4. Rotate image 90 deg
5. Get edges
6. Quantize image
7. Show image
8. Quit
```

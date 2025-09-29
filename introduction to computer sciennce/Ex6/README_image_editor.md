# Image Editor (Intro2CS Ex6)

A simple Python program for basic image processing operations.  
The program loads an image, allows the user to choose editing operations from a menu, and saves the result.

---

## Features
- **RGB to Grayscale** conversion.
- **Blurring** images with a customizable kernel size.
- **Resizing** images using bilinear interpolation.
- **Rotation** by 90° left or right.
- **Edge detection** using adaptive thresholding.
- **Quantization** into N levels (both grayscale and RGB).
- **Display** the image at any point.
- **Save** the final image to a file.

---

## Requirements
- Python 3.8+
- `ex6_helper.py` module (provided in the course), which must support:
  - `load_image(path)` – load image into nested list representation.
  - `save_image(image, path)` – save image to file.
  - `show_image(image)` – display image in a window.

---

## Usage
Run from the command line:
```bash
python3 image_editor.py input_image.jpg
```

The program opens an interactive menu:

```
1. Convert RGB to grayscale
2. Blur image
3. Resize image
4. Rotate image 90 deg
5. Get edges
6. Quantize image
7. Show image
8. Quit program
```

Enter the number corresponding to the desired operation.  
Each option may ask for additional parameters (e.g., kernel size, resize dimensions, etc.).

---

## Example
```
$ python3 image_editor.py flower.jpg
1.Convert RGB to grayscale
2.blur image
3.resize image
4.rotate image 90 deg
5.get edges
6.quantize image
7.show image
8.quit program
choose: 1
```

---

## Key Functions
- `separate_channels(image)` – split RGB channels.
- `combine_channels(channels)` – merge RGB channels.
- `RGB2grayscale(image)` – convert to grayscale.
- `blur_kernel(size)` – create kernel for blurring.
- `apply_kernel(image, kernel)` – apply convolution.
- `bilinear_interpolation(image, y, x)` – helper for resizing.
- `resize(image, h, w)` – resize image.
- `rotate_90(image, direction)` – rotate 90° left/right.
- `get_edges(image, blur_size, block_size, c)` – detect edges.
- `quantize(image, N)` – quantize grayscale.
- `quantize_colored_image(image, N)` – quantize RGB image.

---

## Limitations
- Edge detection requires odd kernel sizes.
- Quantization requires `N > 1`.
- Only works with the helper module API provided in the assignment.

---

## License
Educational use. Add a license if you plan to publish broadly.

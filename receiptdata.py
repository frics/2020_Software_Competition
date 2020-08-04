import PIL
import pytesseract
import cv2

img_path = 'mark4.jpg'
img = cv2.imread(img_path,cv2.IMREAD_COLOR)
img2 = cv2.cvtColor(img,cv2.COLOR_BGR2GRAY)
img2 = PIL.Image.fromarray(img2)
text = pytesseract.image_to_string(img2,lang = 'kor',config='--psm 1 -c preserve_interword_spaces=1')
print(text)


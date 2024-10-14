import os
from PIL import Image
import pytesseract
import matplotlib.pyplot as plt
import matplotlib.image as mpimg
import threading
import numpy as np

def montreImage(image_path):
    img = mpimg.imread(image_path)

    # Afficher l'image
    plt.imshow(img)
    plt.axis('off')  # Désactiver les axes
    plt.show()

def environ(pixel,r,g,b):
    dif = 0
    dif += abs(pixel[0] - r)
    dif += abs(pixel[1] - g)
    dif += abs(pixel[2] - b)

    if dif < 100:
        return np.asarray([0,0,0,255])
    else:
        return np.asarray([255,255,255,255])

def editImage(image):
    image = np.asarray(image)
    hauteur = len(image)
    largeur = len(image[0])
    image = [np.asarray([environ(j,0xFF,0xFF,0xFF) for j in i]) for i in image[hauteur//20:hauteur//10]]
    image = np.asarray(image)
    image = Image.fromarray((image).astype(np.uint8))

    return image

for dossier in os.listdir():
    if ".py" in dossier:
        continue
    if "modifier" in dossier:
        continue

    listImage = os.listdir(dossier)
    for image in listImage:
        if not "heretoslay" in image:
            continue

        path = f"{dossier}/{image}"
        image = Image.open(path)

        #image = editImage(image)

        #image.show()

        # Utiliser Tesseract pour reconnaître le texte
        texte_reconnu = pytesseract.image_to_string(image)

        # Afficher le texte extrait
        nom = texte_reconnu.split("\n")[0]
        try :
            nom = " ".join([i[0].capitalize() + i[1:].lower() for i in nom.split(" ")])
        except IndexError:
            nom = "n/a"

        print("Nom : " + nom)

        threading.Thread(target=lambda: montreImage(path)).start()

        verif = input("ok?")
        if verif != "":
            nom = verif
        

        os.rename(path, f"{dossier}/{nom}.png")
        


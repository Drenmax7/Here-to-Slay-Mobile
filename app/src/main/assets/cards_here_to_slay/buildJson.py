import json
import os

def renameImage():
    for dossier in os.listdir():
        if ".py" in dossier:
            continue
        if ".json" in dossier:
            continue
        if dossier == "alternativeDesign":
            continue

        for image in os.listdir(dossier):
            path  = f"{dossier}/{image}"
            
            start = "N_1_"
            if (dossier == "back") :
                start = "S_1_"
            elif (dossier in ["warrior","druid"]):
                start = "WD_1_"
            elif (dossier in ["berserker","necromancer"]):
                start = "BN_1_"
            elif (dossier in ["sorcerer"]):
                start = "DS_1_"
            elif (dossier in ["hereToSleigh"]):
                start = "HTS_1_"

            newName = f"{dossier}/{start}{image}"
            print(newName)
            os.rename(path,newName)

extensionConversion = {
    "N" : "normal",
    "WD" : "warriors and druids",
    "BN" : "berserkers and necromancers",
    "DS" : "dragon sorcerer",
    "M" : "monster",
    "L" : "leader",
    "HTS" : "here to sleigh",
    "S" : "special"
}

data = {}


for dossier in os.listdir():
    if ".py" in dossier:
        continue
    if ".json" in dossier:
        continue
    if dossier == "alternativeDesign":
        continue

    for image in os.listdir(dossier):
        extension = image.split("_")[0]
        number = image.split("_")[1]
        name = image.split("_")[2].split(".png")[0]

        extensionDic = data.get(extensionConversion[extension],{})
        

        imageDic = {
            "path" : f"{dossier}/{image}",
            "number" : int(number),
        }

        if (dossier in ["bard","berserker","druid","fighter","guardian","necromancer","ranger","sorcerer","thief","warrior","wizard"]):
            category = "hero"
            imageDic["class"] = dossier
            imageDic["item slot"] = 1
        else:
            category = dossier
        
        if (category in ["leader","monster"]):
            imageDic["passive"] = []
        
        if (category == "hero" and dossier != "druid") or (category == "monster"):
            imageDic["positive"] = {"roll" : 7, "effect" : []}
        
        if (dossier == "druid") or (category == "monster"):
            imageDic["negative"] = {"roll" : 7, "effect" : []}

        if (category == "challenge"):
            imageDic["bonus"] = 0
            imageDic["class"] = ""
        
        if (category in ["item","cursed"]):
            imageDic["effect"] = []
        
        if (category in ["mask"]):
            imageDic["class"] = ""

        if (category in ["leader"]):
            imageDic["class"] = []

        if (category in ["modifier"]):
            imageDic["positive"] = 0
            imageDic["negative"] = 0
            imageDic["special"] = []
        
        if (category in ["monster"]):
            imageDic["special"] = []
            imageDic["requiredClasses"] = []
        
        categoryDic = extensionDic.get(category,{})


        categoryDic[name] = imageDic
        extensionDic[category] = categoryDic
        data[extensionConversion[extension]] = extensionDic




fichier = open("infoDeck.json","w")
json.dump(data, fichier, indent=4)
fichier.close()
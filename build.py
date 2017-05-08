import os
import glob

files = [x for x in os.listdir() + glob.glob("*/**", recursive = True) if os.path.isfile(x) and not (x.startswith("build") and x.endswith(".py"))]
version = "0.0.8"

def replace_text(text_to_replace, replacement):
    for file in files:
        with open(file, "r+", encoding="latin-1") as f:
            fileContents = [x.replace(text_to_replace, replacement) for x in f]
            f.seek(0)
            f.truncate()
            f.write("".join(fileContents))

# r**IC**hard
## Application développée dans le cadre de l'unité 4E-LE1 de Programmation Java Pour Android à l'ESIEE Paris (2021, E4FE), par Jules BENKEMOUN, Anthony CARDOSO & Avesta MOLAEI (Groupe 2E)


# L'appli?

r**IC**hard permet, en TLDR, de contrôler des petits relais branchés sur secteur par bluetooth afin de rendre des prises électriques intelligentes. Son nom, dont la capitalisation douteuse fait référence au petit circuit intégré (IC) qu'elle contrôle (microcontrôleurs AVR Atmel de MicroChip), et est emprunté au [père du logiciel libre, Richard Stallman](https://stallman.org/). 

## Que fait rIChard?

L'Utilisateur peut soit contrôler ses appareils avec une reconnaissance vocale (API Google (non GPL : ( ) ), ou les activer manuellement en naviguant dans des menus. L'application envoie un message sous forme de chaîne de caractères à un module bluetooth connecté à un microcontrôleur qui allume et éteint les relais sélectionnés. Après tout, nous sommes en FE!

## Les devs ~~de génie~~

Tous trois membres de la E3FE 2E
- Jules BENKEMOUN
- Anthony CARDOSO
- Avesta MOLAEI 

## comman sa marches [tuto 100% fonktionel]
C'est tout bête:
- Télécharger l'archive [rIChard.zip](https://github.com/avmolaei/rIChard/archive/refs/heads/master.zip)
- L'extraire dans votre répertoire de travail
- Sous [Android Studio](https://developer.android.com/studio)
- + File>Open
- + Sélectionnez votre répertoire de travail/rIChard
- + Appuyez sur OK
- + ~~Attendre 500 ans le build de Gradle~~
 - Le projet est opérationel!
 
 ## Documents intéressants:
 - La [Licence](https://github.com/avmolaei/rIChard/blob/master/LICENSE)
 - Le [cahier des charges préliminaires](https://github.com/avmolaei/rIChard/blob/master/cahier_de_charges_appli_preliminaire1.pdf)
 - Ce même fichier [README](https://github.com/avmolaei/rIChard/blob/master/README.md) que vous lisez en ce moment même (méta)

## Les trucs techniques pour les nerds

Appli développée et compilée sur
- Thinkpad T430 
- + ParrotOS (4.11, x86_64)
- + DE: MATE 1.24.1
- + CPU: I5 3320M (4) @ 3.300GHz
- + GPU: Intel 3rd Gen Core processor Graphics Controller
- + Memory:15688MiB
- + Android Studio Arctic Fox | 2020.3.1 Patch 2
- +  ~~En dark mode parce qu'on est des h4xx0r~~<br/> 

Appli testée sur
- Xiaomi/Pocophone Poco X3 5G Pro NFC
- + Android 11, MIUI 12.5 
- + Chipset Qualcomm Snapdragon 860 (7nm)
- + CPU octa-coeur 2.96GHz Kryo 485 
- + GPU Adreno 640


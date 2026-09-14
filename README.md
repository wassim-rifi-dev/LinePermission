# LinePermission

**LinePermission** est une application console Java qui reproduit, de manière simplifiée, le système de gestion des utilisateurs, fichiers et permissions d'un système Linux.

Le projet est construit progressivement autour de deux parties :

1. **Compte et session** — création de comptes, connexion et déconnexion.
2. **Fichiers et permissions** — création, lecture, modification, suppression et partage de droits.

L'objectif est également de mettre en pratique les principes de **programmation orientée objet**, de **séparation des responsabilités**, de **persistance des données** et de **contrôle d'accès**.

---

## 📋 Sommaire

* [Présentation](#-présentation)
* [Fonctionnalités](#-fonctionnalités)
* [Modèle de permissions](#-modèle-de-permissions)
* [Commandes](#-commandes)
* [Architecture](#-architecture)
* [Persistance](#-persistance)
* [Sécurité](#-sécurité)
* [Technologies](#-technologies)
* [Structure du projet](#-structure-du-projet)
* [Installation](#-installation)
* [Compilation](#-compilation)
* [Exécution](#-exécution)
* [Exemple d'utilisation](#-exemple-dutilisation)
* [Règles importantes](#-règles-importantes)
* [Compétences mises en pratique](#-compétences-mises-en-pratique)

---

## 📝 Présentation

Sur un véritable système Linux, chaque fichier possède :

* un propriétaire ;
* des permissions ;
* un contenu ;
* des règles déterminant qui peut accéder au fichier.

**LinePermission** reproduit ce fonctionnement avec un modèle volontairement simplifié.

Il existe seulement **deux catégories d'utilisateurs** :

| Catégorie    | Signification                     |
| ------------ | --------------------------------- |
| Propriétaire | Utilisateur ayant créé le fichier |
| Autres       | Tous les autres utilisateurs      |

Il existe également **trois droits** :

| Droit | Signification        |
| ----- | -------------------- |
| `r`   | Lire le contenu      |
| `w`   | Modifier le contenu  |
| `d`   | Supprimer le fichier |

---

# 🔐 Modèle de permissions

Les permissions sont affichées sous la forme :

```text
PROPRIETAIRE|AUTRES
```

Chaque bloc possède toujours trois positions :

```text
rwd
```

Un `-` indique que le droit n'est pas accordé.

### Exemples

```text
rwd|---
```

Le propriétaire possède tous les droits et les autres n'en possèdent aucun.

```text
rw-|r--
```

Le propriétaire peut lire et écrire.

Les autres peuvent uniquement lire.

```text
rwd|r--
```

Le propriétaire possède tous les droits.

Les autres peuvent uniquement lire.

---

## ⚠️ Règle fondamentale

**Une seule catégorie de permissions s'applique à un utilisateur.**

Si l'utilisateur connecté est le propriétaire :

```text
→ seuls les droits du bloc propriétaire sont utilisés.
```

Sinon :

```text
→ seuls les droits du bloc autres sont utilisés.
```

Il n'y a **aucune notion de groupe ou d'administrateur**.

---

# 🚀 Fonctionnalités

## Partie 1 — Comptes et sessions

LinePermission permet de :

* créer un compte ;
* protéger le compte avec un mot de passe ;
* se connecter ;
* se déconnecter ;
* conserver les comptes après redémarrage ;
* empêcher les doublons de login.

Commandes :

```text
signup
login
logout
exit
```

---

## Partie 2 — Fichiers et permissions

Une fois connecté, l'utilisateur peut :

* créer un fichier ;
* lister les fichiers ;
* afficher le contenu d'un fichier ;
* modifier un fichier ;
* supprimer un fichier ;
* modifier les permissions de partage.

Commandes :

```text
ls
touch
cat
nano
chmod
```

---

# 💻 Commandes

## `signup`

Crée un nouveau compte.

```text
linperm> signup
Login: abdelaziz
Password:
```

Le mot de passe n'est jamais sauvegardé en clair.

### Contraintes

Un login :

* ne doit pas être vide ;
* ne doit pas contenir d'espace ;
* ne doit pas contenir `:` ;
* doit être unique.

---

## `login`

Permet de se connecter avec un compte existant.

```text
linperm> login
Login: abdelaziz
Password:
```

Après une connexion réussie, l'invite devient :

```text
abdelaziz@linperm>
```

Un login inexistant et un mauvais mot de passe retournent exactement le même message afin de ne pas révéler l'existence des comptes.

---

## `logout`

Ferme la session actuelle.

```text
abdelaziz@linperm> logout
linperm>
```

La commande est refusée si aucun utilisateur n'est connecté.

---

## `exit`

Quitte l'application.

```text
linperm> exit
```

---

# 📁 Commandes fichiers

## `touch`

Crée un nouveau fichier.

```text
abdelaziz@linperm> touch test.txt
```

Le créateur devient automatiquement propriétaire du fichier.

Les permissions initiales sont :

```text
rwd|---
```

Les autres utilisateurs n'ont donc aucun droit sur le nouveau fichier.

### Exemple

```text
abdelaziz@linperm> touch notes.txt
File created.
```

Un nom contenant un chemin est refusé :

```text
touch ../secret.txt
```

ou :

```text
touch documents/test.txt
```

---

## `ls`

Liste les fichiers disponibles.

```text
abdelaziz@linperm> ls
```

L'utilisateur peut voir les fichiers des autres utilisateurs, même s'il n'a aucun droit dessus.

### `ls -l`

Affiche les permissions et le propriétaire :

```text
abdelaziz@linperm> ls -l

rwd|--- abdelaziz notes.txt
rw-|r-- karim project.txt
r--|r-- sara readme.txt
```

La visibilité d'un fichier dans `ls` ne dépend donc pas du droit `r`.

---

# 📖 `cat`

Affiche le contenu d'un fichier.

```text
abdelaziz@linperm> cat notes.txt
Bonjour LinePermission !
```

La commande nécessite le droit :

```text
r
```

Si l'utilisateur ne possède pas ce droit :

```text
Permission denied
```

Le contenu n'est alors pas affiché.

---

# ✏️ `nano`

Permet de modifier le contenu d'un fichier.

```text
abdelaziz@linperm> nano notes.txt
```

La commande nécessite le droit :

```text
w
```

Si le droit `w` est absent :

```text
Permission denied
```

Le fichier n'est pas modifié.

### Cas particulier : `w` sans `r`

Un utilisateur peut avoir :

```text
-w-
```

sans avoir :

```text
r--
```

Dans ce cas, `nano` permet tout de même la modification, mais **le contenu actuel ne doit pas être affiché**.

L'utilisateur édite donc le fichier « à l'aveugle ».

Cela empêche `nano` de devenir un moyen indirect de contourner le droit de lecture.

---

# 🔧 `chmod`

Permet au propriétaire de partager des droits avec les autres utilisateurs.

Exemple :

```text
abdelaziz@linperm> chmod +r notes.txt
```

Les autres utilisateurs obtiennent alors le droit de lecture.

Le propriétaire peut également partager :

```text
chmod +w notes.txt
chmod +d notes.txt
```

Plusieurs droits peuvent être utilisés selon l'implémentation prévue par le projet.

### Exemple

Avant :

```text
rwd|---
```

Après :

```text
rwd|r--
```

Le propriétaire reste :

```text
rwd
```

Seul le bloc `autres` peut être modifié par `chmod`.

---

## 🔒 Qui peut utiliser `chmod` ?

**Seul le propriétaire du fichier.**

Si un autre utilisateur tente :

```text
chmod +w notes.txt
```

le résultat est :

```text
Permission denied
```

---

# 🗂️ Architecture

Le projet respecte une architecture en couches :

```text
┌──────────────────────┐
│    ConsoleApp        │
│  Interface utilisateur│
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│     Services         │
│ UserService          │
│ FileService          │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│   ControleAcces      │
│ Permissions / accès  │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│       Model          │
│ User / FichierProtege│
└──────────────────────┘
```

### Principe

Chaque couche possède une responsabilité précise.

* `ConsoleApp` affiche les informations et récupère les commandes.
* `UserService` gère les comptes et les sessions.
* `FileService` gère les fichiers et applique les contrôles d'accès.
* `ControleAcces` décide si une opération est autorisée.
* Les classes du package `model` représentent les données.

---

# 🧱 Structure du projet

```text
LinePermission/
│
├── lib/
│   └── jbcrypt-*.jar
│
├── data/
│   ├── users.txt
│   └── files/
│       ├── notes.txt
│       ├── project.txt
│       └── ...
│
├── src/
│   ├── Main.java
│   │
│   ├── model/
│   │   ├── User.java
│   │   └── FichierProtege.java
│   │
│   ├── access/
│   │   └── ControleAcces.java
│   │
│   ├── service/
│   │   ├── UserService.java
│   │   └── FileService.java
│   │
│   └── ui/
│       └── ConsoleApp.java
│
└── README.md
```

---

# 💾 Persistance

Les données doivent survivre au redémarrage de l'application.

## Comptes

Les comptes sont sauvegardés dans un fichier texte.

Format :

```text
login:hash
```

Exemple :

```text
abdelaziz:$2a$10$...
karim:$2a$10$...
```

Le mot de passe réel n'est jamais présent dans le fichier.

---

## Fichiers

Les métadonnées des fichiers sont sauvegardées avec une ligne par fichier.

Format :

```text
nom;proprietaire;rwd;r--
```

Exemple :

```text
notes.txt;abdelaziz;rwd;r--
```

Le contenu du fichier est stocké séparément dans le dossier :

```text
data/
```

Cela permet notamment de gérer correctement les contenus contenant plusieurs lignes.

---

# 🔐 Sécurité

## Hash des mots de passe

LinePermission utilise **jBCrypt** pour protéger les mots de passe.

Lors de l'inscription :

```java
BCrypt.gensalt()
BCrypt.hashpw()
```

Lors de la connexion :

```java
BCrypt.checkpw()
```

Le mot de passe en clair n'est donc jamais sauvegardé.

---

## Protection contre l'énumération des comptes

Les deux situations suivantes doivent produire le même message :

```text
Login inconnu
```

et :

```text
Mot de passe incorrect
```

L'utilisateur doit recevoir un message générique, par exemple :

```text
Invalid credentials
```

Cela évite de révéler si un login existe.

---

# 🛡️ Contrôle d'accès

Le contrôle d'accès est centralisé dans :

```text
access/ControleAcces.java
```

Cette classe ne possède aucun état et fournit des méthodes `static`.

Elle détermine notamment si un utilisateur peut :

* lire ;
* écrire ;
* supprimer ;
* modifier les permissions.

Exemple conceptuel :

```java
ControleAcces.estAutorise(user, fichier, 'r');
```

Le `FileService` doit effectuer le contrôle avant toute opération sensible.

---

# ⚙️ Règles importantes

### Avant connexion

Aucune commande de gestion de fichiers n'est accessible.

```text
linperm> ls
Permission denied
```

L'utilisateur doit d'abord faire :

```text
login
```

---

### Pas de reconnexion pendant une session

Si un utilisateur est déjà connecté :

```text
signup
login
```

sont refusés.

Il faut d'abord :

```text
logout
```

---

### Aucun administrateur

Il n'existe pas de compte administrateur.

Sur le fichier d'un autre utilisateur, tout utilisateur est simplement considéré comme :

```text
autres
```

---

### `ls` ne nécessite pas de permission

Un utilisateur peut voir l'existence et les informations d'un fichier même s'il ne possède aucun droit dessus.

---

### `cat` nécessite `r`

Posséder `w` ne donne pas automatiquement `r`.

---

### `nano` nécessite `w`

Posséder `r` ne donne pas automatiquement `w`.

---

### `chmod` appartient au propriétaire

Seul le propriétaire peut modifier le bloc `autres`.

---

### Les permissions du propriétaire ne changent jamais avec `chmod`

`chmod` agit uniquement sur :

```text
AUTRES
```

---

### Les fichiers ne peuvent pas sortir du dossier de données

Les noms contenant un chemin sont interdits afin d'éviter d'écrire en dehors du dossier prévu.

---

# 🧪 Critères d'acceptation

## Partie 1

* [x] Un compte ne peut pas être créé en double.
* [x] Le mot de passe n'est jamais stocké en clair.
* [x] Un mauvais mot de passe et un login inexistant donnent le même message.
* [x] Aucune commande fichier n'est accessible avant connexion.
* [x] L'invite change après connexion.
* [x] Une ligne vide ne provoque pas d'erreur Java.
* [x] Une commande inconnue ne provoque pas d'erreur Java.
* [x] Les comptes survivent au redémarrage.
* [x] Le premier lancement sans fichier de sauvegarde fonctionne.

## Partie 2

* [x] Un nouveau fichier possède initialement `rwd|---`.
* [x] Le créateur devient propriétaire.
* [x] `ls -l` affiche les permissions et le propriétaire.
* [x] Les fichiers des autres restent visibles.
* [x] `cat` nécessite `r`.
* [x] `nano` nécessite `w`.
* [x] `w` sans `r` permet l'édition à l'aveugle.
* [x] Seul le propriétaire peut utiliser `chmod`.
* [x] `chmod` ne modifie que le bloc `autres`.
* [x] Une permission déjà accordée peut être redonnée sans bloquer l'application.
* [x] Un nom de fichier contenant un chemin est refusé.
* [x] Les permissions survivent au redémarrage.
* [x] Les contenus survivent au redémarrage.

---

# 🛠️ Technologies

* **Java**
* **Java Collections**
* **Java NIO (`java.nio.file`)**
* **jBCrypt**
* **Programmation orientée objet**
* **Console / CLI**
* **Persistance dans des fichiers texte**

---

# 📦 Installation

## 1. Cloner le projet

```bash
git clone <URL_DU_REPOSITORY>
cd LinePermission
```

## 2. Vérifier Java

```bash
java -version
javac -version
```

Le projet utilise les fonctionnalités Java standard telles que :

* `Path`
* `Files`
* `List`
* `ArrayList`
* `HashMap`
* `StringBuilder`

---

## 3. Ajouter jBCrypt

Placer le fichier `.jar` de jBCrypt dans :

```text
lib/
```

Exemple :

```text
lib/jbcrypt-0.4.jar
```

---

# 🔨 Compilation

Depuis la racine du projet :

### Linux / macOS

```bash
javac -cp "lib/jbcrypt-0.4.jar" -d out $(find src -name "*.java")
```

### Windows PowerShell

```powershell
javac -cp "lib/jbcrypt-0.4.jar" -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
```

---

# ▶️ Exécution

### Linux / macOS

```bash
java -cp "out:lib/jbcrypt-0.4.jar" Main
```

### Windows

```powershell
java -cp "out;lib/jbcrypt-0.4.jar" Main
```

> L'application doit être lancée depuis la **racine du projet**, car les chemins vers `data/` sont relatifs au répertoire courant.

---

# 🖥️ Exemple d'utilisation

```text
linperm> signup
Login: abdelaziz
Password:
Account created.

linperm> login
Login: abdelaziz
Password:
Login successful.

abdelaziz@linperm> touch notes.txt
File created.

abdelaziz@linperm> ls -l
rwd|--- abdelaziz notes.txt

abdelaziz@linperm> cat notes.txt

abdelaziz@linperm> nano notes.txt
...

abdelaziz@linperm> chmod +r notes.txt
Permission shared.

abdelaziz@linperm> logout
linperm> exit
```

---

# 📚 Compétences mises en pratique

## Java

* `String`
* `boolean`
* `char`
* `String[]`
* `if / else`
* opérateurs logiques
* `while`
* `switch`
* `return`
* `null`
* méthodes `static`
* surcharge de constructeurs
* `this(...)`
* opérateur ternaire

## Programmation orientée objet

* Classes
* Attributs `private`
* Constructeurs
* Encapsulation
* Getters
* Attributs `final`
* Packages
* Séparation des responsabilités

## Collections

* `Map`
* `HashMap`
* `List`
* `ArrayList`
* `put`
* `get`
* `containsKey`
* `values`
* boucle `for-each`

## Manipulation des chaînes

* `trim()`
* `isEmpty()`
* `equals()`
* `toLowerCase()`
* `contains()`
* `split()`
* `charAt()`
* `substring()`
* `startsWith()`
* concaténation

## Fichiers

* `Path`
* `Path.resolve()`
* `Files.exists()`
* `Files.readAllLines()`
* `Files.write()`
* `Files.readString()`
* `Files.writeString()`
* `try / catch`
* gestion des `IOException`

## Sécurité

* Hashage des mots de passe avec BCrypt
* Vérification des identifiants
* Contrôle d'accès
* Séparation propriétaire / autres
* Protection contre les accès non autorisés

---

# 🏗️ Principes de conception

LinePermission applique plusieurs principes importants :

### Une classe = une responsabilité

```text
User                → représente un compte
FichierProtege      → représente un fichier et ses permissions
ControleAcces       → décide si un accès est autorisé
UserService         → gère les utilisateurs
FileService         → gère les fichiers
ConsoleApp          → gère l'interaction avec l'utilisateur
```

### Séparation des couches

La logique d'affichage reste dans `ConsoleApp`.

La logique métier reste dans les services.

La décision d'autorisation reste dans `ControleAcces`.

Le modèle ne connaît pas les services et ne fait aucun affichage.

---

# 🎯 Objectif pédagogique

Ce projet a pour objectif de construire progressivement une application Java complète en console tout en découvrant des notions fondamentales :

```text
Java
 ↓
POO
 ↓
Collections
 ↓
Fichiers
 ↓
Persistance
 ↓
Authentification
 ↓
Contrôle d'accès
 ↓
Architecture en couches
```

LinePermission permet ainsi de comprendre concrètement comment les concepts de **session, authentification, permissions et persistance** peuvent être combinés dans une application réelle.

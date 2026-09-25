# Line Permission

**Line Permission** est une application console développée en Java qui propose une gestion simplifiée des utilisateurs, des fichiers, des permissions et des journaux d'activité.

Le projet est organisé en plusieurs couches afin de séparer les responsabilités :

* les **DAO** assurent l'accès aux données ;
* les **services** contiennent la logique métier ;
* les **modèles** représentent les données manipulées ;
* la couche **UI** gère les interactions avec l'utilisateur via la console.

Les données applicatives sont persistées dans une base de données **SQLite**.

---

## 📋 Sommaire

* [Présentation](#-présentation)
* [Fonctionnalités](#-fonctionnalités)
* [Modèle de permissions](#-modèle-de-permissions)
* [Architecture](#-architecture)
* [Structure du projet](#-structure-du-projet)
* [Base de données](#-base-de-données)
* [Technologies utilisées](#-technologies-utilisées)
* [Prérequis](#-prérequis)
* [Installation](#-installation)
* [Compilation et exécution](#-compilation-et-exécution)
* [Utilisation](#-utilisation)
* [Statistiques et logs](#-statistiques-et-logs)
* [Limites actuelles](#-limites-actuelles)

---

## 📌 Présentation

Line Permission simule un système simplifié de gestion de fichiers et de permissions.

Chaque fichier possède :

* un nom ;
* un propriétaire ;
* un ensemble de permissions ;
* un contenu stocké dans le système de fichiers.

Les actions réalisées dans l'application sont également enregistrées dans une base SQLite sous forme de **logs**.

Le projet utilise notamment :

```text
User
Fichier
Log
```

pour représenter les principales données métier.

---

## ✨ Fonctionnalités

### Gestion des utilisateurs

L'application permet de :

* créer un compte avec `signup` ;
* se connecter avec `login` ;
* se déconnecter avec `logout` ;
* vérifier l'existence d'un utilisateur ;
* stocker les mots de passe sous forme de hash BCrypt.

Les utilisateurs sont stockés dans la table SQLite `users`.

---

### Gestion des fichiers

Une fois connecté, l'utilisateur peut :

* créer un fichier avec `touch` ;
* afficher la liste des fichiers avec `ls` ;
* lire le contenu d'un fichier avec `cat` ;
* modifier le contenu avec `nano` ;
* modifier les permissions avec `chmod`.

Les fichiers physiques sont créés dans le dossier :

```text
files/
```

Les métadonnées des fichiers sont enregistrées dans la table SQLite `fichiers`.

---

### Gestion des permissions

Les permissions utilisent trois droits :

| Droit | Signification           |
| ----- | ----------------------- |
| `r`   | Lecture                 |
| `w`   | Écriture / modification |
| `d`   | Suppression             |

Les permissions sont représentées sous la forme :

```text
PROPRIETAIRE|AUTRES
```

Par exemple :

```text
rwd|---
```

signifie que le propriétaire possède les trois droits tandis que les autres utilisateurs n'ont aucun droit.

Lors de la création d'un fichier, le code initialise ses permissions à :

```text
rwd|---
```

Le propriétaire dispose donc initialement de tous les droits et les autres utilisateurs d'aucun droit.

---

## 🏗️ Architecture

Le projet suit une organisation en couches.

```text
                    ┌──────────────────┐
                    │    ConsoleApp    │
                    │   Interface CLI  │
                    └────────┬─────────┘
                             │
                             ▼
                    ┌──────────────────┐
                    │     Services     │
                    │                  │
                    │ AuthService      │
                    │ FileService      │
                    │ LogsService      │
                    │ DAOService       │
                    └────────┬─────────┘
                             │
                             ▼
                    ┌──────────────────┐
                    │       DAO        │
                    │                  │
                    │ AbstractDAO      │
                    │ UserDAO          │
                    │ FichierDAO       │
                    │ LogDAO           │
                    └────────┬─────────┘
                             │
                             ▼
                    ┌──────────────────┐
                    │     SQLite       │
                    │   lineperm.db    │
                    └──────────────────┘
```

### Couche UI

`ConsoleApp` gère les interactions avec l'utilisateur :

```text
src/main/java/ma/youcode/lineperm/ui/
└── ConsoleApp.java
```

Elle interprète notamment les commandes :

```text
signup
login
logout
ls
touch
cat
nano
chmod
stats
exit
```

---

### Couche Services

Les services contiennent la logique métier.

```text
services/
├── AuthService.java
├── DAOService.java
├── FileService.java
└── LogsService.java
```

#### `AuthService`

Responsable notamment de :

* l'inscription ;
* la connexion ;
* la déconnexion ;
* la gestion de l'utilisateur courant ;
* le hash des mots de passe avec BCrypt.

#### `FileService`

Gère les opérations liées aux fichiers :

* création ;
* lecture ;
* modification ;
* consultation ;
* modification des permissions.

#### `LogsService`

Expose les fonctionnalités de consultation des statistiques enregistrées dans les logs.

#### `DAOService`

Initialise les tables SQLite :

```text
users
fichiers
logs
```

---

## 🗄️ Couche DAO

Les DAO encapsulent l'accès à la base de données.

```text
dao/
├── AbstractDAO.java
├── DAO.java
└── modelsDAO/
    ├── UserDAO.java
    ├── FichierDAO.java
    └── LogDAO.java
```

### `AbstractDAO`

La classe `AbstractDAO<T>` fournit la connexion SQLite utilisée par les DAO.

La connexion est configurée avec :

```java
jdbc:sqlite:src/main/resources/data/lineperm.db
```

### `DAO<T>`

L'interface définit notamment les opérations :

```java
void save(T t);
T findById(long id);
boolean delete(T t);
```

### `UserDAO`

Gère les utilisateurs en base de données.

### `FichierDAO`

Gère les fichiers et leurs métadonnées.

### `LogDAO`

Gère les journaux et fournit plusieurs requêtes statistiques.

---

## 📂 Structure du projet

La structure principale du code est :

```text
line-permission/
│
├── files/
│   ├── rifi.txt
│   └── test.txt
│
├── lib/
│   ├── jbcrypt-0.4.jar
│   └── sqlite-jdbc-3.53.4.0.jar
│
├── src/
│   └── main/
│       ├── java/
│       │   └── ma/
│       │       └── youcode/
│       │           └── lineperm/
│       │               ├── constants/
│       │               │   └── FilePaths.java
│       │               │
│       │               ├── dao/
│       │               │   ├── AbstractDAO.java
│       │               │   ├── DAO.java
│       │               │   └── modelsDAO/
│       │               │       ├── FichierDAO.java
│       │               │       ├── LogDAO.java
│       │               │       └── UserDAO.java
│       │               │
│       │               ├── models/
│       │               │   ├── Fichier.java
│       │               │   ├── Log.java
│       │               │   ├── User.java
│       │               │   └── enums/
│       │               │       ├── LogResult.java
│       │               │       └── LogType.java
│       │               │
│       │               ├── services/
│       │               │   ├── AuthService.java
│       │               │   ├── DAOService.java
│       │               │   ├── FileService.java
│       │               │   └── LogsService.java
│       │               │
│       │               ├── ui/
│       │               │   └── ConsoleApp.java
│       │               │
│       │               └── LinePermissionMain.java
│       │
│       └── resources/
│           └── data/
│               └── lineperm.db
│
└── lib/
```

Le dépôt contient également un dossier `target/` correspondant à des fichiers compilés.

---

## 🗃️ Base de données

Line Permission utilise **SQLite**.

La base est configurée dans `AbstractDAO` avec :

```text
src/main/resources/data/lineperm.db
```

Les tables sont créées par `DAOService`.

### Table `users`

Elle contient notamment :

```text
id
username
password
```

Le `username` est unique.

---

### Table `fichiers`

Elle contient :

```text
id
permissions
owner_id
file_name
```

`owner_id` référence l'utilisateur propriétaire.

---

### Table `logs`

Elle contient notamment :

```text
id
log_date
log_time
user_id
type
file_id
result
```

Les relations avec les utilisateurs et les fichiers sont définies avec des clés étrangères.

---

## 📊 Logs

Les actions réalisées sur les fichiers peuvent être enregistrées dans la table `logs`.

Les types définis dans `LogType` sont :

```text
CREATION
LECTURE
ECRITURE
CHANGE_PERMISSION
SUPPRESSION
```

Les résultats possibles sont :

```text
OK
REFUSE
```

Les logs sont associés à un utilisateur et, lorsqu'il est disponible, à un fichier.

---

## 📈 Statistiques

La commande `stats` est disponible avant connexion.

Elle propose actuellement :

```text
1) Nombre total d'actions
2) Nombre d'acces refuses
3) Utilisateurs distincts
4) Actions par utilisateur
5) Top 3 des fichiers consultes
6) Acces refuses d'un utilisateur
7) Utilisateur le plus actif
8) Repartition des action par type
0) Quitter
```

Ces informations sont calculées directement depuis la base SQLite par `LogDAO`.

---

## 🛠️ Technologies utilisées

Le projet utilise :

* **Java**
* **SQLite**
* **JDBC**
* **jBCrypt**
* **Java NIO**
* **Java Collections**
* **Programmation orientée objet**

Les bibliothèques présentes dans le projet sont :

```text
lib/jbcrypt-0.4.jar
lib/sqlite-jdbc-3.53.4.0.jar
```

Aucun fichier `pom.xml` ou `build.gradle` n'est présent dans le projet fourni. La compilation doit donc être réalisée avec les bibliothèques `.jar` présentes dans `lib/`.

---

## 📋 Prérequis

Pour compiler et exécuter le projet, il faut disposer de :

1. un **JDK Java** ;
2. les deux bibliothèques présentes dans `lib/` :

   * `jbcrypt-0.4.jar`
   * `sqlite-jdbc-3.53.4.0.jar`

Il est également recommandé d'exécuter l'application depuis la racine du projet, car les chemins utilisés par le code sont relatifs.

---

## 📥 Installation

### 1. Récupérer le projet

```bash
git clone <URL_DU_REPOSITORY>
cd line-permission
```

Ou ouvrir directement le projet dans votre IDE Java.

### 2. Vérifier les bibliothèques

Le dossier `lib/` doit contenir :

```text
lib/
├── jbcrypt-0.4.jar
└── sqlite-jdbc-3.53.4.0.jar
```

### 3. Vérifier Java

```bash
java -version
javac -version
```

Aucune version précise du JDK n'est déclarée dans les fichiers du projet fournis ; il est donc préférable d'utiliser une version compatible avec le code Java présent dans le dépôt.

---

## ▶️ Compilation et exécution

Le projet ne contient pas de système de build Maven ou Gradle. Les commandes ci-dessous utilisent donc directement `javac` et les JAR présents dans `lib/`.

### Linux / macOS

Depuis la racine du projet :

```bash
mkdir -p out
javac -cp "lib/*" -d out $(find src/main/java -name "*.java")
```

Puis :

```bash
java -cp "out:lib/*" ma.youcode.lineperm.LinePermissionMain
```

### Windows PowerShell

```powershell
mkdir out
javac -cp "lib/*" -d out (Get-ChildItem -Recurse src/main/java -Filter *.java).FullName
```

Puis :

```powershell
java -cp "out;lib/*" ma.youcode.lineperm.LinePermissionMain
```

Le point d'entrée de l'application est :

```java
ma.youcode.lineperm.LinePermissionMain
```

---

## 💻 Utilisation

Au démarrage, l'application affiche une invite similaire à :

```text
====================== LinePerm ====================
Non Connecte ? Commandes : signup | login | stats | help | exit

lineperm>
```

### Créer un compte

```text
lineperm> signup
Entrer votre information.
username : wassim
password : ****
```

Le mot de passe est hashé avec BCrypt avant son enregistrement.

Après une inscription réussie, l'utilisateur est directement considéré comme connecté.

---

### Se connecter

```text
lineperm> login
Entrer votre information.
username : wassim
password : ****
```

En cas de connexion réussie, l'invite utilise le nom de l'utilisateur :

```text
wassim@lineperm>
```

---

### Créer un fichier

```text
wassim@lineperm> touch test.txt
```

Le fichier physique est créé dans :

```text
files/
```

Ses métadonnées sont enregistrées dans SQLite.

---

### Lister les fichiers

```text
wassim@lineperm> ls
```

Le code affiche pour chaque fichier :

```text
permissions propriétaire nom_du_fichier
```

Par exemple :

```text
rwd|--- wassim test.txt
```

---

### Lire un fichier

```text
wassim@lineperm> cat test.txt
```

Pour un utilisateur qui n'est pas propriétaire, le code vérifie la présence du droit `r` dans la partie `AUTRES`.

---

### Modifier un fichier

```text
wassim@lineperm> nano test.txt
```

Le programme affiche le contenu actuel puis permet d'ajouter des lignes.

La saisie se termine avec :

```text
EOF
```

Pour un utilisateur qui n'est pas propriétaire, le droit `w` est vérifié avant la modification.

---

### Modifier les permissions

Le propriétaire peut utiliser `chmod`.

Exemple :

```text
wassim@lineperm> chmod +r test.txt
```

Les permissions peuvent également être retirées avec la syntaxe utilisant `-`, par exemple :

```text
wassim@lineperm> chmod -r test.txt
```

Le code actuel applique ces modifications au bloc des permissions des **autres utilisateurs**.

---

### Se déconnecter

```text
wassim@lineperm> logout
```

La session courante est supprimée.

---

### Quitter

Depuis l'invite non authentifiée :

```text
lineperm> exit
```

---

## 🔐 Sécurité

Les mots de passe ne sont pas stockés directement en clair.

`AuthService` utilise :

```java
BCrypt.hashpw(...)
```

lors de l'inscription et :

```java
BCrypt.checkpw(...)
```

lors de la connexion.

La base de données contient donc le hash du mot de passe plutôt que le mot de passe fourni lors de l'inscription.

---

## 🧩 Principes d'organisation

Le projet met en pratique une séparation claire des responsabilités :

```text
UI
 │
 ▼
Services
 │
 ▼
DAO
 │
 ▼
SQLite
```

Les modèles restent indépendants de la logique d'accès aux données :

```text
models/
├── User
├── Fichier
└── Log
```

Les DAO s'occupent de la persistance :

```text
dao/
├── AbstractDAO
├── UserDAO
├── FichierDAO
└── LogDAO
```

Les services orchestrent la logique applicative :

```text
services/
├── AuthService
├── FileService
├── LogsService
└── DAOService
```

---

## ⚠️ Limites actuelles

Cette section reflète volontairement ce qui est présent dans le code fourni.

### Suppression des fichiers

Le modèle de permissions contient le droit :

```text
d
```

et `LogType` contient :

```text
SUPPRESSION
```

Cependant, `FileService` ne fournit actuellement pas de commande utilisateur permettant de supprimer un fichier.

Le README ne considère donc pas la suppression comme une fonctionnalité disponible dans l'interface actuelle.

### `help`

Le message initial mentionne une commande :

```text
help
```

mais `ConsoleApp` ne contient pas actuellement de traitement `case "help"`.

Elle n'est donc pas documentée comme une commande fonctionnelle.

### `ls`

La commande actuellement traitée est :

```text
ls
```

Le code ne distingue pas une variante `ls -l`. Cette dernière n'est donc pas considérée comme une commande distincte.

### `findById` et `delete`

Certaines méthodes de l'interface DAO sont encore partiellement implémentées selon le DAO concerné. Par exemple, `LogDAO.findById()` et `LogDAO.delete()` retournent actuellement des valeurs par défaut.

Le projet semble donc être dans une phase d'évolution autour de la persistance SQLite.

---

## 🎯 Objectif du projet

Line Permission permet de mettre en pratique plusieurs notions de développement Java :

* programmation orientée objet ;
* architecture en couches ;
* séparation DAO / services / modèles ;
* JDBC ;
* base de données relationnelle SQLite ;
* gestion des fichiers avec Java NIO ;
* authentification ;
* hashage des mots de passe ;
* contrôle des permissions ;
* journalisation des actions ;
* requêtes SQL et statistiques.

L'architecture peut être résumée ainsi :

```text
Utilisateur
    │
    ▼
ConsoleApp
    │
    ▼
Services
    │
    ├── AuthService
    ├── FileService
    └── LogsService
    │
    ▼
DAO
    │
    ├── UserDAO
    ├── FichierDAO
    └── LogDAO
    │
    ▼
SQLite
```

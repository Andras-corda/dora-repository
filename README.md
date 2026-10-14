# Ceci est un template
---
# [Nom du mod]

![Minecraft](https://img.shields.io/badge/Minecraft-1.20.1-green)
![Forge](https://img.shields.io/badge/Forge-47.x-orange)
![MCreator](https://img.shields.io/badge/MCreator-2024.x-blue)
[![CurseForge](https://img.shields.io/badge/CurseForge-T%C3%A9l%C3%A9charger-F16436)](https://www.curseforge.com/minecraft/mc-mods/[slug-du-mod])

> [Une phrase d'accroche qui résume le mod.]

---

## Présentation

[Décris le concept du mod en quelques lignes : l'idée, le thème, ce qu'il apporte au jeu.]

## Contenu

### Blocs
| Nom | Description |
|-----|-------------|
| [Bloc] | [Description] |

### Items
| Nom | Description |
|-----|-------------|
| [Item] | [Description] |

### Entités / Mobs
| Nom | Description |
|-----|-------------|
| [Entité] | [Description] |

### Autres
- [Biomes, dimensions, recettes, enchantements, structures…]

## Captures d'écran

<!-- Ajoute tes images dans un dossier docs/ ou screenshots/ -->
![Aperçu](screenshots/apercu.png)

---

## Installation (joueurs)

1. Installer **Minecraft Forge 1.20.1** ([télécharger](https://files.minecraftforge.net/net/minecraftforge/forge/index_1.20.1.html))
2. Télécharger le `.jar` du mod depuis [CurseForge](https://www.curseforge.com/minecraft/mc-mods/[slug-du-mod]) ou les [Releases](../../releases)
3. Placer le fichier dans le dossier `mods/` de Minecraft
4. Lancer le jeu avec le profil Forge

### Dépendances
- [Aucune / liste des mods requis]

---

## Développement

### Prérequis
- [MCreator](https://mcreator.net/download) version **[2024.x]** (tout le monde doit utiliser la même version !)
- Git

### Récupérer le projet
```bash
git clone https://github.com/[utilisateur]/[repo].git
```
Puis dans MCreator : **File → Open workspace** et sélectionner le fichier `.mcreator`.

> Après chaque `git pull`, faire **Build → Regenerate code** dans MCreator.

### Workflow Git
- Une branche par fonctionnalité : `feature/nom-de-la-feature`
- Toujours **pull avant de commencer** à travailler
- Éviter de modifier le même élément MCreator à plusieurs en même temps (risque de conflits dans `elements/`)
- Merge sur `main` via Pull Request

### Structure du workspace
```
├── elements/          # Définitions des éléments MCreator
├── src/               # Code généré + code personnalisé
├── [nom].mcreator     # Fichier du workspace
└── build.gradle
```

---

## Équipe

| Membre | Rôle |
|--------|------|
| [Nom] | [Ex : textures, modèles] |
| [Nom] | [Ex : blocs et items] |
| [Nom] | [Ex : entités et IA] |
| [Nom] | [Ex : recettes, équilibrage] |

## Roadmap

- [x] [Tâche terminée]
- [ ] [Tâche en cours]
- [ ] [Tâche à venir]
- [ ] Publication sur CurseForge

## Licence

MIT

---

<sub>Projet réalisé dans le cadre du cours de métodologie et game design — HEAJ, 2026-2027.</sub>
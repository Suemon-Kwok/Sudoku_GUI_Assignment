# Sudoku Game - GUI Version with Database Integration

**Author:** Suemon Kwok  
**Student ID:** 14883335  
**Course:** COMP603 / ENSE 600 - Software Construction  
**Institution:** Auckland University of Technology

## 📋 Project Overview

A comprehensive Sudoku game application featuring a graphical user interface built with Java Swing and integrated with Apache Derby embedded database for persistent game storage.

## ✨ Features

- **Interactive GUI** - Click-based cell selection with keyboard input
- **Multiple Game Modes** - Classic (untimed) and Timed modes
- **4 Difficulty Levels** - Easy, Medium, Hard, and Expert
- **Smart Hints** - Algorithm-based hint system
- **Undo Functionality** - Reverse moves with full history
- **Save/Load Games** - Persistent storage using Derby database
- **Statistics Tracking** - Performance metrics and progress tracking
- **Auto-Solve** - Automatic puzzle completion using backtracking algorithm

## 🛠️ Technical Stack

- **Language:** Java (JDK 21)
- **GUI Framework:** Java Swing
- **Database:** Apache Derby (Embedded Mode)
- **IDE:** NetBeans 23
- **Testing:** JUnit 4
- **Build System:** Ant

## 🏗️ Architecture & Design Patterns

- **MVC Pattern** - Separation of Model, View, and Controller
- **Singleton Pattern** - DatabaseManager, FileManager
- **Strategy Pattern** - HintStrategy implementations
- **Template Method** - Puzzle solving algorithm
- **Factory Pattern** - Game mode creation
- **Observer Pattern** - UI status updates

## 📦 Project Structure

Sudoku_GUI_Suemon_Kwok_14883335/
├── src/
│   └── assignment_2_sudoku_gui/
│       ├── Assignment_2_Sudoku_GUI.java
│       ├── controller/
│       ├── database/
│       ├── generators/
│       ├── model/
│       ├── strategies/
│       ├── ui/
│       └── test/
├── lib/
│   ├── derby.jar
│   └── derbyclient.jar
└── README.md

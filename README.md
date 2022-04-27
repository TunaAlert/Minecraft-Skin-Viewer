# Minecraft-Skin-Viewer
A software which allows you to view skins in real time, useful for making skins.

## What it does
The software allows you to load files as skins or directly load files from players.  
Loaded files will be updated once per second, provided they have been changed.  
This software is mainly intended to be used while creating new skins, as a way to have a 3D representation in almost real time.

## How to use it
Ctrl+O will open a file dialogue, in which you can choose a PNG file to load. This file will be checked for changes once per second.  
Ctrl+S will open a player dialogue, in which you can enter a UUID or user name of a player, whose skin will then be loaded. This will not be updated live.  
Ctrl+V will paste a file from the clipboard, provided there is one. It works similarly to Ctrl+O.

Pressing M will toggle between the slim model (Alex) and the classic model (Steve).  
Pressing L will toggle the lighting options.  
Pressing H will toggle the sidebar.  
Pressing a number will change the animation.  
Pressing R will reset the pose, but animations will continue playing.  

All of these options have ways to be accessed in the Menu as well.  
Additionally, there is an option to set the window to always be on top, which will allow you to see the preview even when focusing another program (i.e. GIMP, Photoshop).

## The Sidebar
In the sidebar you can toggle various body parts.  
Additionally, you can change the background to another color, an image, or even a skybox.

## Skyboxes

Apart from the included default skybox, you can also load your own skyboxes. The skybox texture follows the same layout as the head of a Minecraft skin.

## Animations

You can add your own animations by clicking `Pose > Animation > Load Animation`. 

# How to compile
The project uses lwjgl, including lwjgl-glfw and lwjgl-util, [json-simple-1.1.1](https://mvnrepository.com/artifact/com.googlecode.json-simple/json-simple/1.1.1), and [Jakarta XML Binding API](https://mvnrepository.com/artifact/jakarta.xml.bind/jakarta.xml.bind-api).

# Download
If you don't want to compile the software yourself or don't know how to, you can download a pre-compiled version [here](https://www.mediafire.com/file/973g81h5g8kvb64/Skin+viewer.jar/file). Keep in mind that I give no guarantee that this software will work as intended on your system.
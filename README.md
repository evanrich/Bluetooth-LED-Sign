# Bluetooth LED Sign - Control Sure 3216 displays from your computer/Android phone via Bluetooth

The Bluetooth sign project started out originally from me wanting a wireless sign I could put in my car to tell drivers to get off my ass when they were following too close.    It started as just a daughter card that was shaped to connect directly to the back of a Sure 3216 (3mm) display.  I then wanted to be able to have a much larger display, and instead of buying 3216 boards at $24+ plus shipping, I went about designing the equivelent of 3x 3216 boards. 

The project has many uses, from digital cube/office name or status signs (out of office?) to vehicle/store signs.

Please note that this is not complete yet.  see the Works/Doesn't work section below for info on what has been tested, and what still may not work before trying any of this yourself.

**Source code**

* https://github.com/evanrich/Bluetooth-LED-Sign

**Licensing**<br />
<a rel="license" href="http://creativecommons.org/licenses/by-sa/3.0/"><img alt="Creative Commons License" style="border-width:0" src="http://i.creativecommons.org/l/by-sa/3.0/88x31.png" /></a><br /><span xmlns:dct="http://purl.org/dc/terms/" property="dct:title">Bluetooth LED Sign</span> by <a xmlns:cc="http://creativecommons.org/ns#" href="https://github.com/evanrich/Bluetooth-LED-Sign" property="cc:attributionName" rel="cc:attributionURL">Evan Richardson</a> is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-sa/3.0/">Creative Commons Attribution-ShareAlike 3.0 Unported License</a>.<br />Based on a work at <a xmlns:dct="http://purl.org/dc/terms/" href="https://code.google.com/p/ht1632c/" rel="dct:source">code.google.com</a>.



**Features**

* Small Size: 52.936x39mm ( 2.08" x 1.54")
* Custom LED Board is 15.15" x 3.20"
* Low cost 2 layer boards (controller PCB can be made for around $6 from BatchPCB.com)
* Multiple mounting options: Controller plugs directly into back of Sure 3216 (3MM) or custom 9616 boards
* Low Power: Controller operates around 50-80mA (needs to be tested) with a 2000mAh battery the displays + controller can run >4 hours on single charge
* Wireless: **BLUETOOTH!!**  works up to 30+ feet away
* Charges/Runs via USB port (500mA only, 12V wall plug option being considered)
* Fuel Guage IC has been added to allow reporting/displaying of remaining capacity (Code needs to be added however)
* Android and VB.NET apps can both control message color and speed, as well as looping the message until a new message is sent.
* Android: Voice Control - enter messages using your voice
* Android: History - Resend any of the last recently used messages by taping on it and hitting send.

**Works/Don't know if works**
Works: Android remote (still improving UI and features however) (tested with arduino, and bluetooth module)
Works: .NET code (tested with arduino Uno hooked to sure 3216 displays)
Works: Using Bluetooth to send messages to displays (tested with arduino
Don't know if works: controller.   The controller itself works, as well as the controller code, on an arduino uno board. In my original two prototypes however, I first hooked up the FT232 wrong (tx->tx, Rx->rx), so i couldn't test loading code that way, and in the 2nd prototype, I hooked the bluetooth up incorrectly (reset wasn't connected right, tx/rx were messed up again, etc) For the third prototype (currently in "Boards" folder, I took my working base controller (arduino-ish) and a known good bluetooth wiring (what i used to make a break out board) and combined them.   I am waiting on the boards to come in from batchpcb.com (in about two weeks or so) to build and test, but they should work.


**Known issues**

2/14/2012 (14/2/2012 for those of you in the rest of the world):  I've discovered that the 8x8 matrix pieces that I ordered from Sure electronics were the wrong type.  They sent me common Anode displays, the schematics/orignal 3216 boards use common cathode displays.   I've contacted a few suppliers to find a cheap display (some retailers are selling pieces for 4$ a piece, cost prohibitive, as the modules alone would cost more than 3 sure 3216 displays.) I'm looking into 100Led.com and a few others suppliers to try and get a price quote, although some will not do anything below 1000 pieces.   The Board build is on hold for now, although the controller is progressing (waiting on boards to be delivered from Batchpcb.com)  I'll provide updates here when I get more info.  

* Arduino library code will not work with more than 2 displays (issue with the library, not my code)
* Android: If a Repeat is issued to the display, it will loop the text until a new message is sent.
* VB.NET: If a repeat and color change are issued, the display will not stop repeating until a new color is sent.

**To-Do**

* Develop some sort of user guide/documentation (Wiki pages started, in-progress)
* Finalize BOM (~95% complete)
* Write Fuel Guage code to allow transmission or display of remaining battery capacity
* Prototype 96x16 board (boards arrived 2/10/2012, hand-soldering first board)
* Source and purchase correct LED matrix pieces, original ones were common anode, need common cathode pieces (this was a supplier error in not stating what type their boards were)
* Finalize board/schematic for controller (device is 1/2 the size of first prototype)
* Find or develop tool to allow updating firmware on the board (USB parts are there, so new firmware updates can be sent out to users)
* Other stuff


**Questions/Support**

* The Wiki: https://github.com/evanrich/Bluetooth-LED-Sign/wiki

* Original library/Sure 3216 information on Arduino.cc Forums @ [http://arduino.cc/forum/index.php/board,6.0.html](http://arduino.cc)


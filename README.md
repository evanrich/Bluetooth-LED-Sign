# Bluetooth LED Sign - Control Sure 3216 displays from your computer/Android phone via Bluetooth

The Bluetooth sign project started out originally from me wanting a wireless sign I could put in my car to tell drivers to get off my ass when they were following too close.    It started as just a daughter card that was shaped to connect directly to the back of a Sure 3216 (3mm) display.  I then wanted to be able to have a much larger display, and instead of buying 3216 boards at $24+ plus shipping, I went about designing the equivelent of 3x 3216 boards. 

The project has many uses, from digital cube/office name or status signs (out of office?) to vehicle/store signs.

Please note that this is not complete yet.  see the Works/Doesn't work section below for info on what has been tested, and what still may not work before trying any of this yourself.

**Updates**

* 5/4/13  - This project is considered depricated, as I stopped working on it for a while due to other life concerns.  I have picked it back up however in a new repository.  Please see the following: https://github.com/evanrich/92x16LED-Display
* 4/10/12 - Built control board, verified working, however something is wrong with serial communications between Atmega328 and both FT232 and bluetooth.  Need to diagnose.  Tested using modified code, when any input comes from bluetooth,display text.  This worked.  However looking at the data coming through, it's garbage text.  Since both the serial data comign into and out of the atmega is suspect, I think it's something with the atmega itself, as doing a simple serial.println("test"); gives garbage out.   I've been busy but need to investigate.
* 3/13/12 - 9616 Board does not fully work.  the first third works, but there seems to be issues with the 2nd and 3rd sections.   Need to get an oscilloscope to check, do not use at this time.  Only the controller should work (waiting on solder paste to arrive to build)
* 3/09/12 - 1/3 of 9616 display finished and working so far, will continue to build rest of it while waiting for solder paste.  I'm thinking that the 1A supply built onto the control board may not be enough to drive every LED (~1.5-2A) for 3 displays, so I'm going to start working on a higher current design in addition to this that can supply up to 3A
* 3/08/12 - Stencils arrived today, waiting on new order of solder paste (old stuff dried out).    NOTE: I identified an issue with a missing trace in the 96x16 board, that caused the 2nd row of the 3rd and 4th displays on top to not be conected.  I'm working on a new layout and in the meantime will adjust with a wire jumper.
* 3/07/12 - Stencils should arive by the end of the week.   I found a supplier for the LED matrix pieces, but they're $3.00 or more each, so this greatly increases the cost of the custom display, somewhat to the fact that unless they were mass-produced, they're be more expensive than just buying 3 3216 boards from sure.   I will be completing the prototype however to validate the design, but I would not recommend it to anyone as cheaper alternative to Sure's offering.   The only way it would be cheaper is if the modules were <$0.99 each.  The controller still remains an option.
* 2/29/12 - Received set of controller boards from batchpcb, waiting on solder stencil to construct.  Updated wiki pages with board scans.
* 2/28/12 - Located and sourced set of LED matrix boards, sign is 1/12 finished and working so far.

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

**Works/Don't know if works** <br>
Works: Android remote (still improving UI and features however) (tested with arduino, and bluetooth module)<br>
Works: .NET code (tested with arduino Uno hooked to sure 3216 displays)<br>
Works: Using Bluetooth to send messages to displays (tested with arduino<br>
Works: controller code (tested on arduino uno)<br>
Don't know if works: the controller.   The controller itself works, as well as the controller code, on an arduino uno board. In my original two prototypes however, I first hooked up the FT232 wrong (tx->tx, Rx->rx), so i couldn't test loading code that way, and in the 2nd prototype, I hooked the bluetooth up incorrectly (reset wasn't connected right, tx/rx were messed up again, etc) For the third prototype (currently in "Boards" folder, I took my working base controller (arduino-ish) and a known good bluetooth wiring (what i used to make a break out board) and combined them.<br>



**Known issues**

* Arduino library code will not work with more than 2 displays (issue with the library, not my code)
* Android: If a Repeat is issued to the display, it will loop the text until a new message is sent.
* VB.NET: If a repeat and color change are issued, the display will not stop repeating until a new color is sent.

**To-Do**

* Develop some sort of user guide/documentation (Wiki pages started, in-progress)
* Finalize BOM (~95% complete)
* Write Fuel Guage code to allow transmission or display of remaining battery capacity
* Prototype 96x16 board (boards arrived 2/10/2012, hand-soldering first board)
* Finalize board/schematic for controller (device is 1/2 the size of first prototype)
* Other stuff

**How You can Help!**

So you want to help contribute and make this better? Awesome!   Here are a few things I could use some help on..

* IOS version of the mobile code (as much as I hate Iphones, some people use them)
* nicer looking GUI for both android and desktop apps
* Code optimization!  (I'm not really a C or C++ coder, so I'm sure there are bits in my code that can be optimized, but I don't know what)
* New Ideas/improvements!


**Questions/Support**

* The Wiki: https://github.com/evanrich/Bluetooth-LED-Sign/wiki

* Original library/Sure 3216 information on Arduino.cc Forums @ [http://arduino.cc/forum/index.php/board,6.0.html](http://arduino.cc)


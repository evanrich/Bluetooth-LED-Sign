
# Bluetooth LED Sign - Control Sure 3216 displays from your computer/phone via Bluetooth

The Bluetooth sign project started out originally from me wanting a wireless sign I could put in my car to tell drivers to get off my ass when they were following too close.    It started as just a daughter card that was shaped to connect directly to the back of a Sure 3216 (3mm) display.  I then wanted to be able to have a much larger display, and instead of buying 3216 boards at $24+ plus shipping, I went about designing the equivelent of 3x 3216 boards. 

The project has many uses, from digital cube/office name or status signs (out of office?) to vehicle/store signs.

**Source code**

* https://github.com/evanrich/Bluetooth-LED-Sign



**Known issues**

* New Arduino 1.0 code will not work with more than 2 displays
* Old Arduino 0023 code is slow when using text scaling (2 instead of 1)
* Android: If a Repeat is issued to the display, it will loop the text until a new message is sent.
* VB.NET: If a repeat and color change are issued, the display will not stop repeating until a new color is sent.




**Features**

* Small Size: 54x39mm ( 2.13" x 1.54")
* Multiple mounting options: Controller plugs directly into back of Sure 3216 or custom 9616 boards
* Low Power: Controller operates around 50-80mA (needs to be tested) with a 2000mAh battery it can run >4 hours on single charge
* Wireless: BLUETOOTH!!  works up to 30+ feet away
* Charges/Runs via USB port (500mA only, 12V wall plug option being added)
* Fuel Guage IC has been added to allow reporting/displaying of remaining capacity (Code needs to be added however)
* Android and VB.NET apps can both controll message color and speed, as well as looping the message until a new message is sent.
* Android: Voice Control - enter messages using your voice
* Android: History - Resend any of the last recently used messages by taping on it and hitting send.



**Questions/Support**

* Forums on [http://arduino.cc/forum/index.php/board,6.0.html](http://arduino.cc)


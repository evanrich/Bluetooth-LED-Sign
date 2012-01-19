
#include <avr/io.h> 
#include <avr/pgmspace.h>
#include <avr/power.h>
#include <avr/sleep.h>
#include <Arduino.h>
#include <ht1632c.h>
#include <SoftwareSerial.h>

// RX pin, TX pin
SoftwareSerial Bluetooth(2, 3);
//ht1632c dotmatrix = ht1632c(PORTD, 7, 6, 4, 5, GEOM_32x16, 2);
ht1632c dotmatrix = ht1632c(PORTB, 2, 3, 5, 4, GEOM_32x16, 2);
//13,12,11,10
//data,wr,clk,cs

const int bufferSize = 64;
char inputBuffer[bufferSize];  
int bufferPointer = 0;

const int Off = 0;
const int On = 1;
int loopstate = Off;           //set loopstate to default off
int displaycolor = GREEN;      //Display Color variable.  defaults to GREEN (1)
int scrollspeed =0;               //Variable to store text display speed.
const int backspace = 8;       //ASCII code for backspace.
//const int repeaton = 44;     //ASCII code for ,.  turns on repeat.
//const int repeatoff = 126;   //ASCII code for ~.  turns off repeat.
//const int displaytext = 46;  //ASCII code for period.  triggers sending buffer to display.
const int displaytext = 97;    //ASCII code for "a".  triggers sending buffer to display.
const int repeaton = 98;       //ASCII code for "b".  turns on repeat.
const int repeatoff = 99;      //ASCII code for "c".  turns off repeat.
//const int Green = 34;        //ASCII code for ".  triggers Green color.
//const int Red = 35;          //ASCII code for #.  triggers Red color.
//const int Orange = 36;       //ASCII code for $.  triggers Orange color.
const int Green = 100;         //ASCII code for "d".  triggers Green color.
const int Red = 101;           //ASCII code for "e".  triggers Red color.
const int Orange = 102;        //ASCII code for "f".  triggers Orange color.

//Direction controls
const int left = 111;          //"o"
const int right = 112;         //"p"
int msgDirection = 0;       //default to Right->Left  "LEFT"

//Speed controls
const int ten = 113;           //"q"
const int twenty = 114;        //"r"
const int thirty = 115;        //"s"
const int fourty = 116;        //"t"
const int fifty = 117;         //"u"
const int sixty = 118;         //"v"
const int seventy = 119;       //"w"
const int eighty = 120;        //"x"
const int ninety = 121;        //"y"
const int hundred = 122;       //"z"


void setup ()
{
  power_adc_disable();
  power_twi_disable();
  dotmatrix.setfont(23);
  Bluetooth.begin(9600);
  //delay(1000);
  //Bluetooth.print("AT+RESET\r\n");
  //delay (2000);
  // Command to set Bluetooth modules name
  // Bluetooth.print("AT+NAMEBluetooth Sign\r\n");
  //delay(1000);
  //Bluetooth.begin(19200);
  dotmatrix.clear();
  dotmatrix.pwm(15);
  dotmatrix.hscrolltext(0,"FW:r1O7",RED, 45, 1, LEFT);
  dotmatrix.clear();
}

//char fps_str[15];

//void compute_fps() {
//  static const byte kComputeEveryFrames = 500;
//  static unsigned long last_millis = 0;
//  static int cnt_frames = 0;
//  if (++cnt_frames == kComputeEveryFrames) {
//    cnt_frames = 0;
//    float fps = kComputeEveryFrames / ((millis() - last_millis) / 1000);
//    sprintf(fps_str, "%d fps ", (int)fps);
//    Serial.println(fps_str); 
//    last_millis = millis();
//  }
//}

// Vertical lines moving from right to left. With this pattern, at each new
// frame half of the pixels are completely modified (i.e. both in the red and green
// plane), and half change only in one plane (i.e. only red or green).
// This leads to ~15fps with two screens.
//void launch_fps_benchmark() {
//  word frames = 0;
//  for (byte k = 0; ; k = (k + 1) % (ORANGE + 1)) {
//    frames++;
//    for (byte i = 0; i <= dotmatrix.y_max; i++) {
//      for (byte j = 0; j <= dotmatrix.x_max; ++j) {
//	dotmatrix.plot(j, i, k++);
//	if (k > ORANGE) k = 0;
//      }
//    }
//    dotmatrix.sendframe();
//    compute_fps();
//    if (frames > 500) return;
//  }
//}

//word counter = 0;

void sleepNow()
{
  /* Now is the time to set the sleep mode. In the Atmega8 datasheet
   * http://www.atmel.com/dyn/resources/prod_documents/doc2486.pdf on page 35
   * there is a list of sleep modes which explains which clocks and 
   * wake up sources are available in which sleep modus.
   *
   * In the avr/sleep.h file, the call names of these sleep modus are to be found:
   *
   * The 5 different modes are:
   *     SLEEP_MODE_IDLE         -the least power savings 
   *     SLEEP_MODE_ADC
   *     SLEEP_MODE_PWR_SAVE
   *     SLEEP_MODE_STANDBY
   *     SLEEP_MODE_PWR_DOWN     -the most power savings
   *
   *  the power reduction management <avr/power.h>  is described in 
   *  http://www.nongnu.org/avr-libc/user-manual/group__avr__power.html
   */

  set_sleep_mode(SLEEP_MODE_IDLE);   // sleep mode is set here

  sleep_enable();          // enables the sleep bit in the mcucr register
  // so sleep is possible. just a safety pin 

  power_adc_disable();
  power_spi_disable();
  power_timer0_disable();
  power_timer1_disable();
  power_timer2_disable();
  power_twi_disable();


  sleep_mode();            // here the device is actually put to sleep!!

  // THE PROGRAM CONTINUES FROM HERE AFTER WAKING UP
  sleep_disable();         // first thing after waking from sleep:
  // disable sleep...

  power_all_enable();

}

void loop ()
{
  dotmatrix.hscrolltext(0,"FW:r1O7",RED, 65, 1, LEFT);
  return;
  char inByte;
  if (Bluetooth.available())
  {  
    inByte = Bluetooth.read();
    switch (inByte){
    case displaytext:            
      // 'newline' character
      inputBuffer[bufferPointer++] = '\0';
      dotmatrix.hscrolltext(0,inputBuffer,displaycolor, scrollspeed, 1, msgDirection);

      //loopstate = digitalRead(LoopSwitch);
      while (loopstate == 1) {      //if looping is enabled, loop text until it is turned off
        dotmatrix.hscrolltext(0,inputBuffer,displaycolor, scrollspeed, 1, msgDirection);
        inByte = Bluetooth.read();

        if (inByte == repeatoff)
        {
          loopstate = Off;  //turn looping function off
        }               
        //loopstate = digitalRead(LoopSwitch);      //check loop value to see if it has changed (gone from high to low, or vice versa.       


        bufferPointer = 0;
        dotmatrix.clear();
      }
      break;
    case backspace:    //'backspace' character
      inputBuffer[bufferPointer--];
      break;  
    case repeaton:    
      loopstate = On;  //turn looping function on
      break;
    case repeatoff:   
      displaycolor = GREEN;
      loopstate = Off;  //turn looping function off
      bufferPointer = 0;
      break;
    case Green: 
      displaycolor = GREEN;  //set color to Green
      bufferPointer = 0;
      break;
    case Red: 
      displaycolor = RED;  //set color to Red
      bufferPointer = 0;
      break;   
    case Orange: 
      displaycolor = ORANGE;  //set color to Orange
      bufferPointer = 0;
      break; 

      // Direction bytes
    case left:
      msgDirection = 0;
      bufferPointer = 0;
      break;
    case right:
      msgDirection = 1;
      bufferPointer = 0;
      break;

      //Check for speed settings
    case ten:    
      scrollspeed = 10;
      bufferPointer = 0;
      break;
    case twenty:   
      scrollspeed = 20;
      bufferPointer = 0;
      break;   
    case thirty:    
      scrollspeed = 30;
      bufferPointer = 0;
      break;
    case fourty:    
      scrollspeed = 40;
      bufferPointer = 0;
      break;
    case fifty:   
      scrollspeed = 50;
      bufferPointer = 0;
      break;   
    case sixty:    
      scrollspeed = 60;
      bufferPointer = 0;
      break;  
    case seventy:    
      scrollspeed = 70;
      bufferPointer = 0;
      break;
    case eighty:   
      scrollspeed = 80;
      bufferPointer = 0;
      break;   
    case ninety:    
      scrollspeed = 90;
      bufferPointer = 0;
      break;  
    case hundred:    
      scrollspeed = 100;
      bufferPointer = 0;
      break;  

    default:
      // not a 'newline' character
      if (bufferPointer < bufferSize - 1)  // Leave room for a null terminator
        inputBuffer[bufferPointer++] = inByte;                
    }
  }
}



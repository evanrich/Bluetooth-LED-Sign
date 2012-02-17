/*
 * ht1632c.h
 * defintions for Holtek ht1632c LED driver.
 */

#ifndef ht1632c_h
#define ht1632c_h

#if defined(ARDUINO) && ARDUINO >= 100
#include <Arduino.h>
#else
#include <WProgram.h>
#endif

#include <avr/pgmspace.h>

/*
 * commands written to the chip consist of a 3 bit "ID", followed by
 * either 9 bits of "Command code" or 7 bits of address + 4 bits of data.
 */
#define HT1632_ID_CMD        4	/* ID = 100 - Commands */
#define HT1632_ID_RD         6	/* ID = 110 - Read RAM */
#define HT1632_ID_WR         5	/* ID = 101 - Write RAM */

#define HT1632_CMD_SYSDIS 0x00	/* CMD= 0000-0000-x Turn off oscil */
#define HT1632_CMD_SYSON  0x01	/* CMD= 0000-0001-x Enable system oscil */
#define HT1632_CMD_LEDOFF 0x02	/* CMD= 0000-0010-x LED duty cycle gen off */
#define HT1632_CMD_LEDON  0x03	/* CMD= 0000-0011-x LEDs ON */
#define HT1632_CMD_BLOFF  0x08	/* CMD= 0000-1000-x Blink ON */
#define HT1632_CMD_BLON   0x09	/* CMD= 0000-1001-x Blink Off */
#define HT1632_CMD_SLVMD  0x10	/* CMD= 0001-00xx-x Slave Mode */
#define HT1632_CMD_MSTMD  0x14	/* CMD= 0001-01xx-x Master Mode */
#define HT1632_CMD_RCCLK  0x18	/* CMD= 0001-10xx-x Use on-chip clock */
#define HT1632_CMD_EXTCLK 0x1C	/* CMD= 0001-11xx-x Use external clock */
#define HT1632_CMD_COMS00 0x20	/* CMD= 0010-ABxx-x commons options */
#define HT1632_CMD_COMS01 0x24	/* CMD= 0010-ABxx-x commons options */
#define HT1632_CMD_COMS10 0x28	/* CMD= 0010-ABxx-x commons options */
#define HT1632_CMD_COMS11 0x2C	/* CMD= 0010-ABxx-x commons options */
#define HT1632_CMD_PWM    0xA0	/* CMD= 101x-PPPP-x PWM duty cycle */

#define HT1632_ID_LEN     (1 << 2)  /* IDs are 3 bits */
#define HT1632_CMD_LEN    (1 << 7)  /* CMDs are 8 bits */
#define HT1632_DATA_LEN   (1 << 7)  /* Data are 4*2 bits */
#define HT1632_ADDR_LEN   (1 << 6)  /* Address are 7 bits */

#define HT1632_CS_NONE       0  /* None of ht1632c selected */
#define HT1632_CS_ALL       15  /* All of ht1632c selected */

#define GEOM_32x16	    32	/* 32x16 */
#define GEOM_24x16	    24	/* 24x16 */
#define GEOM_32x8	     8	/* 32x8 */

#define BLACK        0
#define GREEN        1
#define RED          2
#define ORANGE       3
#define RANDOMCOLOR  4
#define MULTICOLOR   8
#define BLINK       16

#define BOLD         1
#define UNDERLINE    2
#define ITALIC       4
#define PROPORTIONAL 8

#define LEFT         0
#define RIGHT        1
#define UP           0
#define DOWN         1

#define FONT_4x6     1
#define FONT_5x7     2
#define FONT_5x8     3
#define FONT_5x7W    4
//#define FONT_6x10    5
//#define FONT_6x12    6
//#define FONT_6x13    7
//#define FONT_6x13B   8
//#define FONT_6x13O   9
//#define FONT_6x9    10
//#define FONT_7x13   11
//#define FONT_7x13B  12
//#define FONT_7x13O  13
//#define FONT_7x14   14
//#define FONT_7x14B  15
#define FONT_8x8    16
#define FONT_8x13   17
#define FONT_8x13B  18
//#define FONT_8x13O  19
#define FONT_9x15   20
#define FONT_9x15B  21
#define FONT_8x16   22
#define FONT_8x16B  23

#define FONT_8x13BK 118

#define cbi(sfr, bit) (_SFR_BYTE(sfr) &= ~_BV(bit))
#define sbi(sfr, bit) (_SFR_BYTE(sfr) |= _BV(bit))

struct bits {
  uint8_t b0:1;
  uint8_t b1:1;
  uint8_t b2:1;
  uint8_t b3:1;
  uint8_t b4:1;
  uint8_t b5:1;
  uint8_t b6:1;
  uint8_t b7:1;
} __attribute__((__packed__));

#define SBIT(port,pin) ((*(volatile struct bits*)&port).b##pin)
#define _clr(name, port, bit) void inline name##_clr() { SBIT(port, bit) = 0; }
#define _set(name, port, bit) void inline name##_set() { SBIT(port, bit) = 1; }
#define _out(name, port, bit) void name##_out() { SBIT((_SFR_IO8(_SFR_IO_ADDR(port)-1)), bit) = 1; }

class ht1632c : public Print {

#ifdef putchar
#undef putchar
#endif

public:
    //ht1632c(volatile uint8_t *port, const byte data, const byte wr, const byte clk, const byte cs, const byte geometry, const byte number);
    ht1632c(const byte geometry, const byte number = 1);
    //void begin(const byte data, const byte wr, const byte clk, const byte cs, byte geometry, byte number);
    //void begin(const byte data, const byte wr, const byte cs1, const byte cs2, const byte cs3, const byte cs4, byte geometry, byte number);

//    void chipselect(byte cs);
//    void writebits (byte bits, byte len);
    void sendcmd(byte cs, byte command);
    void setup();
    void pwm(byte value);
    void sendframe();
    void clear();
    void update_framebuffer(word addr, byte target_bitval, byte pixel_bitval);
    void plot(byte x, byte y, byte color);
    byte getpixel(byte x, byte y);
    byte putchar(int x, int y, char c, byte color = GREEN, byte attr = 0);
    void putbitmap(int x, int y, prog_uint16_t *bitmap, byte w, byte h, byte color);
    void hscrolltext(int y, char *text, byte color, int delaytime, int times = 1, byte dir = LEFT);
    void vscrolltext(int x, char *text, byte color, int delaytime, int times = 1, byte dir = UP);
    void setfont(byte userfont);
    void line(int x0, int y0, int x1, int y1, byte color);
    void rect(int x0, int y0, int x1, int y1, byte color);
    void circle(int xm, int ym, int r, byte color);
    void ellipse(int x0, int y0, int x1, int y1, byte color);
    void fill_r(byte x, byte y, byte color);
    void fill_l(byte x, byte y, byte color);
    void fill(byte x, byte y, byte color);
    void bezier(int x0, int y0, int x1, int y1, int x2, int y2, byte color);
    void profile();
    
    virtual size_t write(uint8_t chr);
    virtual size_t write(const char *str);
    //virtual size_t write(const uint8_t *buffer, size_t size);
            
    byte *framebuffer;
    word framesize;
    byte cs_max;
    byte x_max;
    byte y_max;
    boolean bicolor;
    prog_uint8_t *font;
    prog_uint16_t *wfont;
    byte font_width;
    byte font_height;
    byte x_cur;
    byte y_cur;
    int fps;

};


#ifdef ht1632c_lib

extern    void _data_set();
extern    void _data_clr();
extern    void _data_out();
extern    void _wr_set();
extern    void _wr_clr();
extern    void _wr_out();
extern    void _clk_set();
extern    void _clk_clr();
extern    void _clk_out();
extern    void _clk_pulse(byte num);
extern    void _cs_set();
extern    void _cs_clr();
extern    void _cs_out();
extern    void _writebits(byte bits, byte msb);
extern    void _chipselect(byte num);

#endif 

//extern ht1632class ht1632c;

#ifndef ht1632c_lib

#define ht1632c(port, data, wr, clk, cs, ...) ht1632c(__VA_ARGS__); \
_clr(_data,port,data); _clr(_wr,port,wr); _clr(_clk,port,clk); _clr(_cs,port,cs); \
_set(_data,port,data); _set(_wr,port,wr); _set(_clk,port,clk); _set(_cs,port,cs); \
_out(_data,port,data); _out(_wr,port,wr); _out(_clk,port,clk); _out(_cs,port,cs); \

void _data_set();
void _data_clr();
void _data_out();
void _wr_set();
void _wr_clr();
void _wr_out();
void _clk_set();
void _clk_clr();
void _clk_out();
void _clk_pulse(byte num);
void _cs_set();
void _cs_clr();
void _cs_out();
void _writebits(byte bits, byte msb);
void _chipselect(byte num);

void inline _clk_pulse(register byte num)
{
  while(num--)
  {
    _clk_set();
    _clk_clr();
  }
}

void _chipselect(register byte cs)
{
  if (cs == HT1632_CS_ALL) {
    _cs_clr();
    _clk_pulse(HT1632_CS_ALL);
  } else if (cs == HT1632_CS_NONE) {
    _cs_set();
    _clk_pulse(HT1632_CS_ALL);
  } else {
    _cs_clr();
    _clk_pulse(1);
    _cs_set();
    _clk_pulse(cs - 1);
  }
}

void _writebits (register byte bits, register byte msb)
{
  while (msb) {
    !!(bits & msb) ? _data_set() : _data_clr();
    _wr_clr();
    _wr_set();
    msb >>= 1;
  }
}

#endif

#endif

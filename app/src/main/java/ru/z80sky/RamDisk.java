package ru.z80sky;

public class RamDisk {// ideas 9.4.2023
    // load file: a number, hl adress, bc len; ret z - file not found
    // save file: hl, adress, bc len: ret - z: no place, nz: a number
    // del file: a number: ret z-no file
    // get free mem: ret hl - free mem
    // set pages: bcde - set 4*16 kb page
    // reset files
    // {id, start, len}
    /// bmp
    // https://medium.com/sysf/bits-to-bitmaps-a-simple-walkthrough-of-bmp-image-format-765dc6857393
    // http://www.java2s.com/Tutorials/Java/Graphics_How_to/Image/Create_BMP_format_image.htm
    // https://ru.wikipedia.org/wiki/BMP
}

package ru.z80sky;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Z80 Snapshot loader. The header size for a version 1.0 Z80 snapshot.
 * <P>
 * This class can load and store internally the contents
 * of a Z80 snapshot.
 *
 * @author <A HREF="mailto:razvan.surdulescu@post.harvard.edu">Razvan Surdulescu</A> (c) 2001 - 2006
 * @author <A HREF="mailto:webmaster@zx-spectrum.net">Erik Kunze</A> (c) 1995, 1996, 1997
 * @author <A HREF="mailto:des@ops.netcom.net.uk">Des Herriott</A> (c) 1993, 1994
 * <BR>
 * You may use and distribute this software for free provided you include
 * this copyright notice. You may not sell this software, use my name for
 * publicity reasons or modify the code without permission from me.
 */
public class Z80Loader {
    /** 48k Spectrum model 2 (issue 2) */
    public static final int ISSUE_2 = 2;
    /** 48k Spectrum model 3 (issue 3) */
    public static final int ISSUE_3 = 3;
    /**
     * The size, in bytes, for a memory page.
     */
    public static final int PAGE_SIZE = 16384;
    /** The first ROM page. */
    public static final int ROM0 = 0;
    /** The second ROM page. */
    public static final int ROM1 = 1;
    /** The third ROM page. */
    public static final int ROM2 = 2;
    /** The fourth ROM page. */
    public static final int ROM3 = 3;
    /** The first RAM page. */
    public static final int RAM0 = 4;
    /** The second RAM page. */
    public static final int RAM1 = 5;
    /** The third RAM page. */
    public static final int RAM2 = 6;
    /** The fourth RAM page. */
    public static final int RAM3 = 7;
    /** The fifth RAM page. */
    public static final int RAM4 = 8;
    /** The sixth RAM page. */
    public static final int RAM5 = 9;
    /** The seventh RAM page. */
    public static final int RAM6 = 10;
    /** The eighth RAM page. */
    public static final int RAM7 = 11;

    /** 48k Spectrum model data */
    public static final int MODE_48 = 0;

    /** 128k Spectrum model data */
    public static final int MODE_128 = 1;

    /** Z80 basic registers. */
    protected int m_af16, m_bc16, m_de16, m_hl16;
    /** Z80 alternate registers. */
    protected int m_af16alt, m_bc16alt, m_de16alt, m_hl16alt;
    /** Z80 index registers. */
    protected int m_ix16, m_iy16;
    /** Z80 core registers. */
    protected int m_sp16, m_pc16;
    /** Z80 interrupt and refresh registers. */
    protected int m_r8, m_i8;
    /** Z80 interrupt mode. */
    protected int m_im2;
    /** Z80 flip-flops. */
    protected int m_iff1a, m_iff1b;

    /** Spectrum model (MODE_48 or MODE_128). */
    protected int m_mode;
    /** Spectrum model (issue). */
    protected int m_issue;
    /** Border color. */
    protected int m_border;
    /** 128k specific saved I/O registers. */
    protected int m_last0x7ffd;
    protected int m_last0xfffd;
    /**
     * The end of file marker as returned from InputStream.read()
     */
    private static final int EOF = -1;

    /** The header size for a version 1.0 Z80 snapshot. */
    private static final int VERSION_1 = 0;

    /** The header size for a version 2.0 Z80 snapshot. */
    private static final int VERSION_2 = 23;

    /** The header size for a version 3.0 Z80 snapshot. */
    private static final int VERSION_3 = 54;

    /** The header size for a version 3.x Z80 snapshot. */
    private static final int VERSION_3x = 58;

    /** The default snapshot version. */
    private int m_version = VERSION_1;

    /** Is this snapshot compressed? */
    private boolean m_compressed;
    /// ago
    public byte[] mem = new byte[65536];
    /** URL used for loading the data. */
    protected URL m_url; /// ago

    public Z80Loader() {}

    /**
     * Load the header and the body for the given snapshot
     * name.
     * <P>
     * @param name The name of the snapshot to be loaded.
     * This parameter is appended to the previously provided
     * URL parameter to form the absolute URL.
     */
    public void load(String name) throws IOException {
        URL url = new URL(m_url, name);
        URLConnection connection = url.openConnection();
        InputStream is = connection.getInputStream();
        loadHeader(is);
        loadBody(is);
        is.close();
    }//
    public void loadFile(String fname) throws IOException {
        URL url = new URL("file:" + fname);
        URLConnection connection = url.openConnection();
        InputStream is = connection.getInputStream();
        loadHeader(is);
        loadBody(is);
        is.close();
    }// 				URL url = new URL("file:" + path);

    /**
     * Load the Z80 snapshot header.
     */
    protected void loadHeader(InputStream is) throws IOException {
        byte a8 = (byte) is.read();
        byte f8 = (byte) is.read();
        m_af16 = (((a8 & 0xff) << 8) | (f8 & 0xff));

        byte c8 = (byte) is.read();
        byte b8 = (byte) is.read();
        m_bc16 = (((b8 & 0xff) << 8) | (c8 & 0xff));

        byte l8 = (byte) is.read();
        byte h8 = (byte) is.read();
        m_hl16 = (((h8 & 0xff) << 8) | (l8 & 0xff));

        byte pcLow = (byte) is.read();
        byte pcHigh = (byte) is.read();
        m_pc16 = (((pcHigh & 0xff) << 8) | (pcLow & 0xff));

        byte spLow = (byte) is.read();
        byte spHigh = (byte) is.read();
        m_sp16 = (((spHigh & 0xff) << 8) | (spLow & 0xff));

        m_i8 = (is.read() & 0xff);
        m_r8 = (is.read() & 0xff);

        byte flags1 = (byte) is.read();

        if (flags1 == (byte) 0xff) {
            flags1 = 1;
        }

        // Bit 0   = bit 7 of the R-register
        // Bit 1-3 = border color
        // Bit 4   = basic SamRom switched in (ignored)
        // Bit 5   = block of data is compressed

        m_r8 |= ((flags1 & 0x01) << 7);
        m_border = ((flags1 & 0x0e) >> 1);
        m_compressed = ((flags1 & 0x20) != 0);

        byte e8 = (byte) is.read();
        byte d8 = (byte) is.read();
        m_de16 = (((d8 & 0xff) << 8) | (e8 & 0xff));

        byte cAlt = (byte) is.read();
        byte bAlt = (byte) is.read();
        m_bc16alt = (((bAlt & 0xff) << 8) | (cAlt & 0xff));

        byte eAlt = (byte) is.read();
        byte dAlt = (byte) is.read();
        m_de16alt = (((dAlt & 0xff) << 8) | (eAlt & 0xff));

        byte lAlt = (byte) is.read();
        byte hAlt = (byte) is.read();
        m_hl16alt = (((hAlt & 0xff) << 8) | (lAlt & 0xff));

        byte aAlt = (byte) is.read();
        byte fAlt = (byte) is.read();
        m_af16alt = (((aAlt & 0xff) << 8) | (fAlt & 0xff));

        byte iyLow = (byte) is.read();
        byte iyHigh = (byte) is.read();
        m_iy16 = (((iyHigh & 0xff) << 8) | (iyLow & 0xff));

        byte ixLow = (byte) is.read();
        byte ixHigh = (byte) is.read();
        m_ix16 = (((ixHigh & 0xff) << 8) | (ixLow & 0xff));

        m_iff1a = ((is.read() & 0xff) != 0 ? 1 : 0);
        m_iff1b = ((is.read() & 0xff) != 0 ? 1 : 0);

        byte flags2 = (byte) is.read();

        // Bit 0-1 = interrupt mode
        // Bit 2   = Issue 2 emulation
        // Bit 3   = double interrupt frquency (ignored)
        // Bit 4-5 = video synchronisation (ignored)
        // Bit 6-7 = joystick type (ignored yet)

        m_im2 = (flags2 & 0x03);
        m_issue = ((flags2 & 0x04) != 0 ? ISSUE_2 : ISSUE_3);

        // Snapshot version is newer than 1.0
        if (m_pc16 == 0) {
            byte verLow = (byte) is.read();
            byte verHigh = (byte) is.read();
            m_version = (((verHigh & 0xff) << 8) | (verLow & 0xff));

            if (m_version != VERSION_2 &&
                    m_version != VERSION_3 &&
                    m_version != VERSION_3x) {
                throw new IOException("Unknown snapshot version: " + m_version);
            }

            if (m_version >= VERSION_2) {
                /* V2.0+ */
                pcLow = (byte) is.read();
                pcHigh = (byte) is.read();
                m_pc16 = (((pcHigh & 0xff) << 8) | (pcLow & 0xff));

                m_mode = (is.read() & 0xff);

                m_last0x7ffd = (is.read() & 0xff);
                byte if1Paged = (byte) is.read();	// not used
                byte flags3 = (byte) is.read();		// not used
                m_last0xfffd = (is.read() & 0xff);
                for (int i = 0; i < 16; i++) {
                    byte psg = (byte) is.read();	// not used
                }
            }

            if (m_version >= VERSION_3) {
                /* V3.0+ */
                for (int i = 0; i < 3; i++) {
                    byte tstates = (byte) is.read();	// not used
                }
                byte flags4 = (byte) is.read();		// not used
                byte mgtPaged = (byte) is.read();	// not used
                byte mf128Paged = (byte) is.read();	// not used
                byte ramAt0x0000 = (byte) is.read();// not used
                byte ramAt0x2000 = (byte) is.read();// not used
                for (int i = 0; i < 10; i++) {
                    byte joystickKeys = (byte) is.read();	// not used
                }
                for (int i = 0; i < 10; i++) {
                    byte keyNames = (byte) is.read();	// not used
                }

                if (m_version == VERSION_3) {
                    byte mgtType = (byte) is.read();	// not used
                    byte discipleInhibit = (byte) is.read(); // not used
                    byte discipleRom = (byte) is.read();	// not used
                } else {
                    for (int i = 0; i < 3; i++) {
                        byte fdc = (byte) is.read();	// not used
                    }
                }
            }

            if (m_version >= VERSION_3x) {
                /* V3.X */
                for (int i = 3; i < 14; i++) {
                    byte fdc = (byte) is.read();	// not used
                }
            }
        }
        // log
        log(VERSION_1==m_version ? "VERSION_1" : m_version == VERSION_2  ? "VERSION_2" : m_version == VERSION_3 ? "VERSION_3" : "VERSION_3x");
        log("IM2 "+ m_im2);
        log("I   "+ m_i8);
        log("compressed "+ m_compressed);
    }
    /**
     * Load the Z80 snapshot body.
     */
    protected void loadBody(InputStream is) throws IOException {
        if (m_version == VERSION_1) {
            if (m_compressed) {
                int addr = 16384;
                int data = 0;
                byte b = 0;
                boolean unget = false;

                while (true) {
                    if (unget) {
                        unget = false;
                    } else {
                        data = is.read();
                        if (data == EOF) {
                            break;
                        }
                        b = (byte) data;
                    }
                    if (b != (byte) 0xed) {
                        mem[addr++ & 0xffff] = (byte)(b & 0xff);
                    } else {
                        data = is.read();
                        if (data == EOF) {
                            break;
                        }
                        b = (byte) data;

                        if (b != (byte) 0xed) {
                            mem[addr++ & 0xffff] = (byte)0xed;
                            unget = true;
                        } else {
                            int count = is.read();
                            if (count == EOF) {
                                break;
                            }
                            count = (count & 0xff);

                            data = is.read();
                            if (data == EOF) {
                                break;
                            }
                            b = (byte) data;

                            while (count-- != 0) {
                                mem[addr++ & 0xffff] = (byte)(b & 0xff);
                            }
                        }
                    }
                }

                if (addr < 65535) {
                    throw new IOException("Premature EOF on snapshot");
                }
            } else {
                for (int addr = 16384; addr < 65536; addr++) {
                    int data = is.read();
                    if (data == EOF) {
                        throw new IOException("Premature EOF on snapshot");
                    }

                    mem[addr]  = (byte)(data & 0xff);
                }
            }
        } else {
            int mode48max, mode48if1, mode48mgt;
            int mode128max, mode128if1, mode128mgt;

            if (m_version == VERSION_3x ||
                    m_version == VERSION_3) {

                // ver3:    "48k", "48k+IF1", "SamRam"," 48k+MGT", "128k", "128k+IF1", "128k+MGT"
                // ver3x:   "48k", "48k+IF1", "SamRam", "48k+MGT", "128k", "128k+IF1", "128k+MGT", "+3"
                mode48max = 3;
                mode48if1 = 1;
                mode48mgt = 3;
                mode128max = 6;
                mode128if1 = 5;
                mode128mgt = 6;
            } else if (m_version == VERSION_2) {
                // ver2:    "48k", "48k+IF1", "SamRam", "128k", "128k+IF1"
                mode48max = 2;
                mode48if1 = 1;
                mode48mgt = -1;
                mode128max = 4;
                mode128if1 = 4;
                mode128mgt = -1;
            } else {
                throw new IOException("Unknown snapshot version: " + m_version);
            }

            if (m_mode <= mode48max) {
                // 48k mode
                if (m_mode == 2) {
                    log("SamRam not supported; defaulting to 48k");

                    m_mode = MODE_48;
                    for (int i = 0; i < 5; i++) {
                        loadPage(is, i, (i == 2 || i == 3));
                    }
                } else {
                    if (m_mode == mode48if1) {
                        log("IF1 not supported; defaulting to 48k");
                    }

                    if (m_mode == mode48mgt) {
                        log("MGT not supported; defaulting to 48k");
                    }

                    m_mode = MODE_48;
                    for (int i = 0; i < 3; i++) {
                        loadPage(is, i, false);
                    }
                }
            } else {//if (m_mode <= mode128max) {
                // 128k mode
                if (m_mode == mode128if1) {
                    log("IF1 not supported; defaulting to 128k");
                }

                if (m_mode == mode128mgt) {
                    log("MGT not supported; defaulting to 48k");
                }

                m_mode = MODE_128;
                for (int i = 0; i < 8; i++) {
                    loadPage(is, i, false);
                }
            } /*else {
				// +3 mode
				throw new IOException("JZXFrame does not support the +3 Spectrum");
			}//*/
        }
    }

    /**
     * Load a memory page from the Z80 snapshot.
     */
    protected void loadPage(InputStream is, int block, boolean skip)
            throws IOException {

//		PageHeader header = new PageHeader();
//		header.load(is);
        int blockLength = (0xff & (byte) is.read())  | ((0xff & (byte) is.read())<<8);//  header.getPageLength();
        int page = 0xff & (byte) is.read(); // header.getPageNumber();

        if (skip) {
            // Z80 v3.05 style uncompressed page
            if (blockLength == 65535) {
                blockLength = PAGE_SIZE;
            }

            // Move the file pointer to the beginning of the next block
            for (int i = 0; i < blockLength; i++) {
                int data = is.read();
                if (data == -1) {
                    break;
                }
            }

            return;
        }
        int jzxPage;
        // Convert Z80 page number to JZXFrame ones
        if (m_mode == MODE_48) {
            switch (page) {
                case 4:
                    jzxPage = RAM2;
                    break;
                case 5:
                    jzxPage = RAM0;
                    break;
                case 8:
                    jzxPage = RAM5;
                    break;
                default:
                    throw new IOException("Invalid page number for 48k snapshot: " + page);
            }
        } else {
            jzxPage = RAM0 + page - 3;
        }

        // The structure of a memory block is:
        // Offset  Length  Description
        // 0       2       Length of data (without this 3-byte header)
        // 2       1       Page number of block
        // 3       [0]     Compressed data

        // Starting with version 3.05, a length field of 65535 (-1) means that
        // the block is not compressed and exactly 16384 bytes long, and the
        // length field will never hold values larger than 16383.
        if (blockLength == 65535) {
            int length = PAGE_SIZE;
        } else {
            byte[] memory = mem;
            int addr = 0;
            boolean unread = false;
            int data = 0;
            byte b = 0;

            for (int i = 0; i < blockLength;) {
                i++;

                if (unread) {
                    unread = false;
                } else {
                    data = is.read();
                    if (data == EOF) {
                        break;
                    }
                    b = (byte) data;
                }

                if (b != (byte) 0xed) {
                    memory[addr++] = b;
                } else {
                    i++;

                    data = is.read();
                    if (data == EOF) {
                        break;
                    }
                    b = (byte) data;

                    if (b != (byte) 0xed) {
                        memory[addr++] = (byte) 0xed;
                        unread = true;
                        i--;
                    } else {
                        int count = is.read();
                        if (count == EOF) {
                            break;
                        }
                        count = (count & 0xff);

                        i++;
                        data = is.read();
                        if (data == EOF) {
                            break;
                        }
                        b = (byte) data;
                        i++;

                        while (count-- != 0) {
                            memory[addr++] = b;
                        }
                    }
                }
            }

            if (addr != PAGE_SIZE) {
                throw new IOException("Block " + block + " contains only: " + addr + " bytes");
            }
        }
    }
    void log(String s){System.out.println("*** ".concat(s));}
}


package ivanwfr.rtabs; // {{{

import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.StringTokenizer;

/*
:!start explorer "http://www.jibble.org/wake-on-lan"
:!start explorer "https://en.wikipedia.org/wiki/Wake-on-LAN"
:!start explorer "https://en.wikipedia.org/wiki/Packet_analyzer"
:!start explorer "https://github.com/kei51e/Wake-on-LAN-client/blob/master/WOL.java"
*/
// }}}
class WOL
{
    public static String SendMagicPacket(String hostname, String subnet, String mac)
    {
        try {
            // MAC -> DATA {{{
            StringTokenizer st = new StringTokenizer(mac, " :-");
            if(st.countTokens() != 6) return "*** Invalid mac address mac=["+ mac +"]";

            byte[] mac_bytes = new byte[6];
            for (int i = 0; i < 6; i++) 
                mac_bytes[i] = (byte) Integer.parseInt(st.nextToken(), 16);

            //}}}
            // SUBNET -> BROADCAST ADDRESS {{{
            byte[]     hostAddr = InetAddress.getByName(hostname).getAddress();
            byte[]   subnetAddr = InetAddress.getByName(subnet  ).getAddress();
            byte[]         addr = new byte[4];

            for(int i=0; i<4; ++i) addr[i] = (byte) (hostAddr[i] | (~subnetAddr[i]));

            InetAddress address = InetAddress.getByAddress( addr );
            //}}}
            // DATA {{{
            ByteArrayOutputStream buf = new ByteArrayOutputStream();

            for(int i = 0; i <  6; ++i) buf.write( (byte)0xff );
            for(int i = 0; i < 16; ++i) buf.write( mac_bytes  );

            buf.flush();
            buf.close();

            byte[]           data = buf.toByteArray();
            //}}}
            // SEND {{{

            DatagramPacket packet = new DatagramPacket(data, data.length, address, 9);
            DatagramSocket socket = new DatagramSocket();

            for(int i=0; i<5; ++i) {
                socket.send( packet );
                try { Thread.sleep(500); } catch(Exception ignored) { }
            }

            socket.close();

            //}}}
            return "WOL SENT TO:\n.HOST "+ hostname +"\n.SUBNET "+subnet +"\n.MAC "+ mac;
        }
        catch(Exception ex) {
            return "*** Failed to send Wake-on-LAN:\n*** "+ ex;
        }
    }
/*
    // wol {{{
    public static String SendMagicPacket(String hostname, String subnet, String mac)
    {

        try {
            byte[] mac_bytes = get_mac_bytes( mac );
            byte[] ff_bytes  = new byte[6 + 16 * mac_bytes.length];

            for(int i=0; i<6; i++) ff_bytes[i] = (byte) 0xff;

            for(int i=6; i<ff_bytes.length; i += mac_bytes.length)
                System.arraycopy(mac_bytes, 0, ff_bytes, i, mac_bytes.length);

            InetAddress   address = InetAddress.getByName( ip );
            DatagramPacket packet = new DatagramPacket(ff_bytes, ff_bytes.length, address, 9); // port
            DatagramSocket socket = new DatagramSocket();
            socket.send(packet);
            socket.close();

            return "Wake-on-LAN packet sent to ip=["+ ip +"] mac["+ mac +"]";
        }
        catch(Exception ex) {
            log("*** Failed to send Wake-on-LAN packet: + ex");
            System.exit(1);
        }

    }
    // }}}
    // get_mac_bytes {{{
    private static byte[] get_mac_bytes(String mac)
        throws IllegalArgumentException
    {
        byte[] bytes = new byte[6];
        String[] hex = mac.split("(\\:|\\-)");
        if(hex.length != 6)
            throw new IllegalArgumentException("Invalid MAC address.");

        try {
            for (int i = 0; i < 6; i++)
                bytes[i] = (byte) Integer.parseInt(hex[i], 16);
        }
        catch(NumberFormatException e) {
            throw new IllegalArgumentException("Invalid hex digit in MAC address.");
        }

        return bytes;
    }
    // }}}
*/
}


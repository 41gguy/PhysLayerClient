

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;


public class PhysLayerClient {

    public static void main(String[] args) {

    	System.setProperty("http.proxyHost", "127.0.0.1");
        System.setProperty("http.proxyPort", "38002");

        try {
        	Socket socket = new Socket("localhost", 38002);
            InputStream is = socket.getInputStream();
            DataInputStream ds = new DataInputStream(is);
            
            System.out.println("Connected to server.");
            
            
            int[] preamble = new int[64];
            double baseline = 0.00;
            for (int i = 0; i < 64; i++) {
            	
            	preamble[i] = ds.readUnsignedByte();
            	baseline += preamble[i];
            	
            }
            
            baseline = baseline/64;
            
            System.out.println("Baseline established from server: " + baseline);

            char[] message = new char[320];
            
            	for (int i = 0; i < 40; i++) {
            	
            		String binary = String.format("%8s", Integer.toBinaryString(ds.readUnsignedByte() & 0xFF)).replace(' ', '0');
            	
            		for (int t = 0; t < 8; t++) {	
            			message[i*8 + t] = binary.charAt(t);
            		}
            	}
            	
            	char[] fivetofour = new char[64];
            	for (int v = 0; v < 64; v++) {
            		String fivebit = "";
            		
            		for (int z = 0; z < 5; z++) {
            			fivebit += message[v*5 + z];
            		}
            		fivetofour[v] = decode(fivebit);
            		
            		System.out.print(fivetofour[v]);
            	}
            
            	OutputStream os = socket.getOutputStream();
        		DataOutputStream dos = new DataOutputStream(os);
        		
        		byte[] decodedByteArray = new byte[64];
        		
        		int t = 0; 
        		for (int v = 0; v < 32; v++) {
        			
        			
        			int byteval = Character.digit(fivetofour[t], 16)*16;
        			t++;
        			byteval += Character.digit(fivetofour[t], 16);
        			t++;
        			
        			byte finalbyte = (byte) byteval;
        			decodedByteArray[v] = finalbyte;
        		}
        		System.out.println();
        		
        		dos.write(decodedByteArray, 0, 32);
        		
        		byte response = ds.readByte();
        		
        		if (response == 0) {
        			System.out.println("Response Bad.");
        		}
        		if (response == 1) {
        			System.out.println("Response Good.");
        		}
        		System.out.println("Disconnected from the Server.");
            
        }
        catch (Exception e) {
        	System.out.print(e);
        }
    }
    
    public static char decode(String s) {
    	if (s.equals("11110")) {
    		return '0';
    	}
    	if (s.equals("01001")) {
    		return '1';
    	}
    	if (s.equals("10100")) {
    		return '2';
    	}
    	if (s.equals("10101")) {
    		return '3';
    	}
    	if (s.equals("01010")) {
    		return '4';
    	}
    	if (s.equals("01011")) {
    		return '5';
    	}
    	if (s.equals("01110")) {
    		return '6';
    	}
    	if (s.equals("01111")) {
    		return '7';
    	}
    	if (s.equals("10010")) {
    		return '8';
    	}
    	if (s.equals("10011")) {
    		return '9';
    	}
    	if (s.equals("10110")) {
    		return 'A';
    	}
    	if (s.equals("10111")) {
    		return 'B';
    	}
    	if (s.equals("11010")) {
    		return 'C';
    	}
    	if (s.equals("11011")) {
    		return 'D';
    	}
    	if (s.equals("11100")) {
    		return 'E';
    	}
    	if (s.equals("11101")) {
    		return 'F';
    	}	
    	
    	return '0';
    }
}






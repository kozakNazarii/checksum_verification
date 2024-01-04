import java.util.*;
import java.net.*;
import java.security.*;
import java.io.*;

class Sock{
    String hash;
    String input;
    Socket socket;
    BufferedReader in;
    PrintWriter out;
    
    public Sock(String hostname, Integer port){
    	try {
    		this.socket = new Socket(hostname, port);
    	}
    	catch (IOException e) {
    		System.out.println(e);
    	}
    }
    
    public String getMD5() throws IOException{
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.out = new PrintWriter(this.socket.getOutputStream(), true);
        this.hash = this.in.readLine().trim();
        return this.hash;
    }
    
    public String receiveStrings() throws IOException, NoSuchAlgorithmException{
        String hex;
        MessageDigest md;
        byte[] byt;
        while(true){
        	md = MessageDigest.getInstance("MD5");
            if((this.input = this.in.readLine()) == null){
                return "";
            }
            
            byt = md.digest(this.input.getBytes("UTF-8"));
            StringBuilder tmp = new StringBuilder();
            hex = "";
            for(byte aaa : byt){
            	tmp.append(String.format("%02x", aaa));
            }
            hex = tmp.toString().toUpperCase();
            if(hex.equals(this.hash)){
                return this.input;
            }
        }
    }

    public void sendString() throws IOException {
        this.out.println(this.input);
        this.socket.close();
    }
}

class Main
{
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException
    {
        Scanner scan = new Scanner(System.in);
        Integer port = scan.nextInt();
        Sock sock = new Sock("127.0.0.1", port);
        sock.getMD5();
        sock.receiveStrings();
        sock.sendString();

    }
}

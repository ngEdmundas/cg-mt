import java.io.*;
import java.net.*;
import java.util.*;

class MoneyTransfer {
  // static String HOST = "server.com";
  static String HOST = "localhost";
  static int PORT = 9999;

  static class Transfer implements Runnable {
    String source;
    String target;
    String key;

    Transfer(String accSource, String accTarget, String bankKey) {
      this.source = accSource;
      this.target = accTarget;
      this.key = bankKey;
      System.out.printf("Transfer source:%s, target:%s, key:%s\n", source, target, key);
    }

    public void run () {
      try {
        Socket bank = new Socket(HOST, PORT);
        DataOutputStream out = new DataOutputStream(bank.getOutputStream());
        InputStreamReader isr = new InputStreamReader(bank.getInputStream());

        long startTime = (new Date()).getTime();

        out.writeBytes(source + target + key);
        char[] resp = new char[10];
        isr.read(resp, 0, 10);
        System.err.printf("resp=%s\n", new String(resp));

        out.writeBytes(new String(resp) + "5000");
        char[] ok = new char[2];
        isr.read(ok, 0, 2);

        long finishTime = (new Date()).getTime();

        System.err.printf("ok=%s, start=%d, end=%d, total=%d\n", new String(ok), startTime, finishTime, finishTime - startTime);
        bank.close();
      }
      catch (UnknownHostException uhe) {
        System.err.printf("UnknownHostException=%s\n", uhe);
      }
      catch (ConnectException ce) {
        System.err.printf("ConnectException=%s\n", ce);
      }
      catch (IOException ioe) {
        System.err.printf("IOException=%s\n", ioe);
      }
    }
  }

  static String decodeKey(String hexValue) {
    StringBuilder output = new StringBuilder("");
    for (int i = 0; i < hexValue.length(); i += 2) {
        String str = hexValue.substring(i, i + 2);
        output.append((char) Integer.parseInt(str, 16));
    }
    return output.toString();
  }

  public static void start(String accSource, String accTarget) {
    String key = decodeKey(accSource + accTarget);
    Transfer tx = new Transfer(accSource, accTarget, key);

    for (int i = 0; i < 5; i++) {
      Thread t = new Thread(tx);
      t.start();
    }
  }

  public static void main(String[] args) {
    String accSource = "374c5a446f";
    String accTarget = "5872303531";
    String key = "7LZDoXr051";
    System.out.printf("Source account=%s, target account=%s, key=%s\n", accSource, accTarget, key);
    start(accSource, accTarget);
  }
}

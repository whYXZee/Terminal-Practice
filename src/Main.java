import application.RunApplication;

import java.lang.Thread;
import java.lang.Runnable;
import java.util.concurrent.*;
// import java.util.concurrent.locks.*;

// class GetFont implements Runnable {
//     @Override
//     public void run() {
//         try {
//             RunApplication.getFontSize();
//         } catch (Exception e) {

//         }
//     }

// }

public class Main {
    public static void main(String[] args) throws Exception {

        // Runnable getFont = new Runnable() {
        // public void run() {
        // RunApplication.getFontSize();
        // System.out.println(RunApplication.fontSize);
        // }
        // };
        // ExecutorService executor = Executors.newCachedThreadPool();
        // executor.submit(getFont);
        // new Thread(getFont).start();
        RunApplication.runApp();
        // executor.shutdown();
    }
}
import java.io.File;


public class AutosaverThread implements Runnable {
    public final long autosaveInterval = 300_000; // 1 minute in milliseconds


    @Override
    public synchronized void run() {
        try {
            while (true) {
                // wait for the interval, do nothing if no valid file path
                this.wait(this.autosaveInterval);
                if (AppManager.getCurrentPath() == null) {
                    continue;
                }

                // save the file
                File file = new File(AppManager.getCurrentPath());
                Window.canvas.save(file);
            }
        } catch (InterruptedException exception) {
            System.out.println("Autosave failed: Interrupt Exception");
            this.run();
        }
    }
}

package app;

import javax.swing.SwingUtilities;

import view.VideoSystem;

public class main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
        		new VideoSystem();
            }
        });
	}
}

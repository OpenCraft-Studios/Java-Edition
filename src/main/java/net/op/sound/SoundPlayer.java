package net.op.sound;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;
import static javax.sound.sampled.AudioSystem.getAudioInputStream;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.SourceDataLine;

public class SoundPlayer {

	private SoundPlayer() {
	}

	public static boolean play(AudioInputStream ain) {
		try {
			final AudioFormat outFormat = getOutFormat(ain.getFormat());
			final Info info = new Info(SourceDataLine.class, outFormat);

			try (final SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info)) {

				if (line != null) {
					line.open(outFormat);
					line.start();
					stream(getAudioInputStream(outFormat, ain), line);
					line.drain();
					line.stop();
				}
			}

			ain.close();
		} catch (Exception ignored) {
			return false;
		}

		return true;
	}

	public static boolean play(InputStream in) {
		boolean state = true;
		try {
			state = play(getAudioInputStream(in));
		} catch (Exception ignored) {
			state = false;
		}

		return state;
	}

	public static boolean playFile(File audioFile) {
		boolean state = true;
		try {
			state = play(getAudioInputStream(audioFile));
		} catch (Exception ignored) {
			state = false;
		}

		return state;
	}

	public static boolean playFile(String audioFile_path) {
		return playFile(new File(audioFile_path));
	}

	private static AudioFormat getOutFormat(AudioFormat inFormat) {
		final int ch = inFormat.getChannels();

		final float rate = inFormat.getSampleRate();
		return new AudioFormat(PCM_SIGNED, rate, 16, ch, ch * 2, rate, false);
	}

	private static void stream(AudioInputStream in, SourceDataLine line) throws IOException {
		final byte[] buffer = new byte[4096];
		for (int n = 0; n != -1; n = in.read(buffer, 0, buffer.length)) {
			line.write(buffer, 0, n);
		}
	}
}
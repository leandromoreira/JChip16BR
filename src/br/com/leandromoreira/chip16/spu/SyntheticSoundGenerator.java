/*
 * CODE Took from MSChip16.
 */
package br.com.leandromoreira.chip16.spu;

import javax.sound.sampled.*;

/**
 * @author leandro-rm
 */
public class SyntheticSoundGenerator {

    private double sampleRate;
    private double frequency;
    private double amplitude;
    private double seconds;
    private double twoPiF;
    private AudioFormat format;
    private float buffer[];
    private byte byteBuffer[];
    private Clip lineClip;

    public SyntheticSoundGenerator(final double freq) {
        this.frequency = freq;
    }

    private void createLineClip(final double secs) {
        sampleRate = 44100.0;
        amplitude = 1;
        seconds = secs;
        twoPiF = 2 * Math.PI * frequency;

        final boolean bigEndian = false;
        final boolean signed = true;
        format = new AudioFormat((int) sampleRate, 16, 1, signed,
                bigEndian);

        buffer = new float[(int) (seconds * sampleRate)];
        byteBuffer = new byte[buffer.length * 2];

        for (int sample = 0; sample < buffer.length; sample++) {
            double time = sample / sampleRate;
            buffer[sample] = (float) (amplitude * Math.sin(twoPiF * time));
        }

        final DataLine.Info info = new DataLine.Info(Clip.class,format);
        if (!AudioSystem.isLineSupported(info)) {
        }
        /*Obtain and open the line.*/
        try {
            lineClip = (Clip) AudioSystem.getLine(info);

        } catch (LineUnavailableException ex) {
            /*Handle the error.*/
        }

        int bufferIndex = 0;
        for (int i = 0; i < byteBuffer.length; i++) {
            /*map to 16 bit pcm by multiplying with ((2^16)-1))/2  */
            final int x = (int) (buffer[bufferIndex++] * 32767.0);
            byteBuffer[i] = (byte) x;
            i++;
            byteBuffer[i] = (byte) (x >>> 8);
        }

        try {
            lineClip.open(format, byteBuffer, 0, byteBuffer.length);
        } catch (LineUnavailableException ex) {
            /*Handle the error.*/
        }
    }

    public void playFor(final int milliseconds) {
        createLineClip(milliseconds / 1000);
        lineClip.start();
    }

    public void stop() {
        if (lineClip != null) {
            lineClip.stop();
        }
    }
}
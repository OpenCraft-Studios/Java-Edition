package net.op.sound;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import net.op.util.Resource;

public class Track {

    private final String name;
    private List<Sound> soundList = new ArrayList<>();

    public Track(String name) {
        this.name = name;
    }

    public Track(String name, List<Sound> soundList) {
        this(name);
        this.soundList = soundList;
    }

    public Stream<Sound> getSounds() {
        return soundList.stream();
    }

    public Optional<Sound> getSound(int si) {
        return Optional.ofNullable(this.soundList.get(si));
    }

    public String getName() {
        return this.name;
    }

    public void appendSound(Sound sound) {
        soundList.add(sound);
    }

    public void appendTrack(Track track) {
        for (Sound sound : track.soundList) {
            appendSound(sound);
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(soundList.toArray());
    }

    public Sound findSound(Resource resource) {
        for (Sound sound : soundList) {
            if (sound.getResource().equals(resource)) {
                return sound;
            }
        }

        return null;
    }

}

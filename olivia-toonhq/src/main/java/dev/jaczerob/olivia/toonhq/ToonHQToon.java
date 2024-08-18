package dev.jaczerob.olivia.toonhq;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ToonHQToon {
    @Id
    private int id;
    private int game;
    private String name;
    private String photo;
    private String species;
    private int laff;
    private int toonup;
    private int trap;
    private int lure;
    private int sound;
    @JsonProperty("throw")
    @BsonProperty("throw")
    private int throwGag;
    private int squirt;
    private int drop;
    private String organic;
    private String sellbot;
    private String cashbot;
    private String lawbot;
    private String bossbot;
    private String flowers;

    public ToonHQToon() {
    }

    public ToonHQToon(
            int id,
            int game,
            String name,
            String photo,
            String species,
            int laff,
            int toonup,
            int trap,
            int lure,
            int sound,
            int throwGag,
            int squirt,
            int drop,
            String organic,
            String sellbot,
            String cashbot,
            String lawbot,
            String bossbot,
            String flowers
    ) {
        this.id = id;
        this.game = game;
        this.photo = photo;
        this.species = species;
        this.laff = laff;
        this.toonup = toonup;
        this.trap = trap;
        this.lure = lure;
        this.sound = sound;
        this.throwGag = throwGag;
        this.squirt = squirt;
        this.drop = drop;
        this.organic = organic;
        this.sellbot = sellbot;
        this.cashbot = cashbot;
        this.lawbot = lawbot;
        this.bossbot = bossbot;
        this.name = name;
        this.flowers = flowers;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ToonHQToon) obj;
        return this.id == that.id &&
                this.game == that.game &&
                Objects.equals(this.photo, that.photo) &&
                Objects.equals(this.species, that.species) &&
                this.laff == that.laff &&
                this.toonup == that.toonup &&
                this.trap == that.trap &&
                this.lure == that.lure &&
                this.sound == that.sound &&
                this.throwGag == that.throwGag &&
                this.squirt == that.squirt &&
                this.drop == that.drop &&
                Objects.equals(this.organic, that.organic) &&
                Objects.equals(this.sellbot, that.sellbot) &&
                Objects.equals(this.cashbot, that.cashbot) &&
                Objects.equals(this.lawbot, that.lawbot) &&
                Objects.equals(this.bossbot, that.bossbot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, game, photo, species, laff, toonup, trap, lure, sound, throwGag, squirt, drop, organic, sellbot, cashbot, lawbot, bossbot);
    }

    @Override
    public String toString() {
        return "ToonHQToon[" +
                "id=" + id + ", " +
                "game=" + game + ", " +
                "photo=" + photo + ", " +
                "species=" + species + ", " +
                "laff=" + laff + ", " +
                "toonup=" + toonup + ", " +
                "trap=" + trap + ", " +
                "lure=" + lure + ", " +
                "sound=" + sound + ", " +
                "throwGag=" + throwGag + ", " +
                "squirt=" + squirt + ", " +
                "drop=" + drop + ", " +
                "organic=" + organic + ", " +
                "sellbot=" + sellbot + ", " +
                "cashbot=" + cashbot + ", " +
                "lawbot=" + lawbot + ", " +
                "bossbot=" + bossbot + ']';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlowers() {
        return flowers;
    }

    @JsonSetter("flowers")
    public void setFlowers(int[] flowers) {
        final StringBuilder flowersOutput = new StringBuilder();
        if (flowers == null) {
            this.flowers = flowersOutput.toString();
            return;
        }

        for (final int flower : flowers) {
            flowersOutput.append("%02d".formatted(flower));
        }

        this.flowers = flowersOutput.toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGame() {
        return game;
    }

    public void setGame(int game) {
        this.game = game;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSpecies() {
        return species;
    }

    @JsonSetter("species")
    public void setSpecies(int species) {
        this.species = switch (species) {
            case 1 -> "bear";
            case 2 -> "cat";
            case 3 -> "dog";
            case 4 -> "duck";
            case 5 -> "horse";
            case 6 -> "monkey";
            case 7 -> "mouse";
            case 8 -> "pig";
            case 9 -> "rabbit";
            case 10 -> "deer";
            case 11 -> "crocodile";
            default -> "unknown";
        };
    }

    public int getLaff() {
        return laff;
    }

    public void setLaff(int laff) {
        this.laff = laff;
    }

    public int getToonup() {
        return toonup;
    }

    public void setToonup(int toonup) {
        this.toonup = toonup;
    }

    public int getTrap() {
        return trap;
    }

    public void setTrap(int trap) {
        this.trap = trap;
    }

    public int getLure() {
        return lure;
    }

    public void setLure(int lure) {
        this.lure = lure;
    }

    public int getSound() {
        return sound;
    }

    public void setSound(int sound) {
        this.sound = sound;
    }

    public int getThrowGag() {
        return throwGag;
    }

    @JsonSetter("throw")
    public void setThrowGag(int throwGag) {
        this.throwGag = throwGag;
    }

    public int getSquirt() {
        return squirt;
    }

    public void setSquirt(int squirt) {
        this.squirt = squirt;
    }

    public int getDrop() {
        return drop;
    }

    public void setDrop(int drop) {
        this.drop = drop;
    }

    public String getOrganic() {
        return organic;
    }

    @JsonSetter("prestiges")
    public void setOrganic(List<String> prestiges) {
        if (prestiges == null || prestiges.isEmpty()) {
            this.organic = "none";
        } else {
            this.organic = prestiges.get(0).toLowerCase();
        }
    }

    public String getSellbot() {
        return sellbot;
    }

    public void setSellbot(String sellbot) {
        this.sellbot = sellbot;
    }

    public String getCashbot() {
        return cashbot;
    }

    public void setCashbot(String cashbot) {
        this.cashbot = cashbot;
    }

    public String getLawbot() {
        return lawbot;
    }

    public void setLawbot(String lawbot) {
        this.lawbot = lawbot;
    }

    public String getBossbot() {
        return bossbot;
    }

    public void setBossbot(String bossbot) {
        this.bossbot = bossbot;
    }
}
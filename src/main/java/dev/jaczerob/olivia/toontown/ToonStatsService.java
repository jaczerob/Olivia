package dev.jaczerob.olivia.toontown;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ToonStatsService {
    private static final Logger log = LogManager.getLogger();

    private static final int MAX_LAFF = 140;

    private final ToonRepository toonRepository;

    private final AtomicInteger amountToons = new AtomicInteger();

    private final AtomicInteger amountBears = new AtomicInteger();
    private final AtomicInteger amountCats = new AtomicInteger();
    private final AtomicInteger amountDogs = new AtomicInteger();
    private final AtomicInteger amountDucks = new AtomicInteger();
    private final AtomicInteger amountHorses = new AtomicInteger();
    private final AtomicInteger amountMonkeys = new AtomicInteger();
    private final AtomicInteger amountMice = new AtomicInteger();
    private final AtomicInteger amountPigs = new AtomicInteger();
    private final AtomicInteger amountRabbits = new AtomicInteger();
    private final AtomicInteger amountDeer = new AtomicInteger();
    private final AtomicInteger amountCrocodiles = new AtomicInteger();

    private final AtomicInteger amountAbove100LaffBears = new AtomicInteger();
    private final AtomicInteger amountAbove100LaffCats = new AtomicInteger();
    private final AtomicInteger amountAbove100LaffDogs = new AtomicInteger();
    private final AtomicInteger amountAbove100LaffDucks = new AtomicInteger();
    private final AtomicInteger amountAbove100LaffHorses = new AtomicInteger();
    private final AtomicInteger amountAbove100LaffMonkeys = new AtomicInteger();
    private final AtomicInteger amountAbove100LaffMice = new AtomicInteger();
    private final AtomicInteger amountAbove100LaffPigs = new AtomicInteger();
    private final AtomicInteger amountAbove100LaffRabbits = new AtomicInteger();
    private final AtomicInteger amountAbove100LaffDeer = new AtomicInteger();
    private final AtomicInteger amountAbove100LaffCrocodiles = new AtomicInteger();

    private final AtomicInteger amountMaxedBears = new AtomicInteger();
    private final AtomicInteger amountMaxedCats = new AtomicInteger();
    private final AtomicInteger amountMaxedDogs = new AtomicInteger();
    private final AtomicInteger amountMaxedDucks = new AtomicInteger();
    private final AtomicInteger amountMaxedHorses = new AtomicInteger();
    private final AtomicInteger amountMaxedMonkeys = new AtomicInteger();
    private final AtomicInteger amountMaxedMice = new AtomicInteger();
    private final AtomicInteger amountMaxedPigs = new AtomicInteger();
    private final AtomicInteger amountMaxedRabbits = new AtomicInteger();
    private final AtomicInteger amountMaxedDeer = new AtomicInteger();
    private final AtomicInteger amountMaxedCrocodiles = new AtomicInteger();

    private final AtomicInteger amountToonsWithToonUp = new AtomicInteger();
    private final AtomicInteger amountToonsWithTrap = new AtomicInteger();
    private final AtomicInteger amountToonsWithLure = new AtomicInteger();
    private final AtomicInteger amountToonsWithSound = new AtomicInteger();
    private final AtomicInteger amountToonsWithThrow = new AtomicInteger();
    private final AtomicInteger amountToonsWithSquirt = new AtomicInteger();
    private final AtomicInteger amountToonsWithDrop = new AtomicInteger();

    private final AtomicInteger amountToonsWithMaxToonUp = new AtomicInteger();
    private final AtomicInteger amountToonsWithMaxTrap = new AtomicInteger();
    private final AtomicInteger amountToonsWithMaxLure = new AtomicInteger();
    private final AtomicInteger amountToonsWithMaxSound = new AtomicInteger();
    private final AtomicInteger amountToonsWithMaxThrow = new AtomicInteger();
    private final AtomicInteger amountToonsWithMaxSquirt = new AtomicInteger();
    private final AtomicInteger amountToonsWithMaxDrop = new AtomicInteger();

    private final AtomicInteger amountToonsWithNoOrganic = new AtomicInteger();
    private final AtomicInteger amountToonsWithOrganicToonUp = new AtomicInteger();
    private final AtomicInteger amountToonsWithOrganicTrap = new AtomicInteger();
    private final AtomicInteger amountToonsWithOrganicLure = new AtomicInteger();
    private final AtomicInteger amountToonsWithOrganicSound = new AtomicInteger();
    private final AtomicInteger amountToonsWithOrganicThrow = new AtomicInteger();
    private final AtomicInteger amountToonsWithOrganicSquirt = new AtomicInteger();
    private final AtomicInteger amountToonsWithOrganicDrop = new AtomicInteger();

    public ToonStatsService(final ToonRepository toonRepository, final MeterRegistry meterRegistry) {
        this.toonRepository = toonRepository;

        Gauge.builder("amount_toons", this.amountBears, AtomicInteger::get)
                .tag("species", "bear")
                .tag("laff", "any")
                .register(meterRegistry);
        Gauge.builder("amount_toons", this.amountAbove100LaffBears, AtomicInteger::get)
                .tag("species", "bear")
                .tag("laff", "100")
                .register(meterRegistry);
        Gauge.builder("amount_toons", this.amountMaxedBears, AtomicInteger::get)
                .tag("species", "bear")
                .tag("laff", "max")
                .register(meterRegistry);

        Gauge.builder("amount_toons", this.amountCats, AtomicInteger::get)
                .tag("species", "cat")
                .tag("laff", "any")
                .register(meterRegistry);
        Gauge.builder("amount_toons", this.amountAbove100LaffCats, AtomicInteger::get)
                .tag("species", "cat")
                .tag("laff", "100")
                .register(meterRegistry);
        Gauge.builder("amount_toons", this.amountMaxedCats, AtomicInteger::get)
                .tag("species", "cat")
                .tag("laff", "max")
                .register(meterRegistry);

        Gauge.builder("amount_toons", this.amountDogs, AtomicInteger::get)
                .tag("species", "dog")
                .tag("laff", "any")
                .register(meterRegistry);
        Gauge.builder("amount_toons", this.amountAbove100LaffDogs, AtomicInteger::get)
                .tag("species", "dog")
                .tag("laff", "100")
                .register(meterRegistry);
        Gauge.builder("amount_toons", this.amountMaxedDogs, AtomicInteger::get)
                .tag("species", "dog")
                .tag("laff", "max")
                .register(meterRegistry);

        Gauge.builder("amount_toons", this.amountDucks, AtomicInteger::get)
                .tag("species", "duck")
                .tag("laff", "any")
                .register(meterRegistry);
        Gauge.builder("amount_toons", this.amountAbove100LaffDucks, AtomicInteger::get)
                .tag("species", "duck")
                .tag("laff", "100")
                .register(meterRegistry);
        Gauge.builder("amount_toons", this.amountMaxedDucks, AtomicInteger::get)
                .tag("species", "duck")
                .tag("laff", "max")
                .register(meterRegistry);

        Gauge.builder("amount_toons", this.amountHorses, AtomicInteger::get)
                .tag("species", "horse")
                .tag("laff", "any")
                .register(meterRegistry);
        Gauge.builder("amount_toons", this.amountAbove100LaffHorses, AtomicInteger::get)
                .tag("species", "horse")
                .tag("laff", "100")
                .register(meterRegistry);
        Gauge.builder("amount_toons", this.amountMaxedHorses, AtomicInteger::get)
                .tag("species", "horse")
                .tag("laff", "max")
                .register(meterRegistry);

        Gauge.builder("amount_toons", this.amountMonkeys, AtomicInteger::get)
                .tag("species", "monkey")
                .tag("laff", "any")
                .register(meterRegistry);
        Gauge.builder("amount_toons", this.amountAbove100LaffMonkeys, AtomicInteger::get)
                .tag("species", "monkey")
                .tag("laff", "100")
                .register(meterRegistry);
        Gauge.builder("amount_toons", this.amountMaxedMonkeys, AtomicInteger::get)
                .tag("species", "monkey")
                .tag("laff", "max")
                .register(meterRegistry);

        Gauge.builder("amount_toons", this.amountMice, AtomicInteger::get)
                .tag("species", "mouse")
                .tag("laff", "any")
                .register(meterRegistry);
        Gauge.builder("amount_toons", this.amountAbove100LaffMice, AtomicInteger::get)
                .tag("species", "mouse")
                .tag("laff", "100")
                .register(meterRegistry);
        Gauge.builder("amount_toons", this.amountMaxedMice, AtomicInteger::get)
                .tag("species", "mouse")
                .tag("laff", "max")
                .register(meterRegistry);

        Gauge.builder("amount_toons", this.amountPigs, AtomicInteger::get)
                .tag("species", "pig")
                .tag("laff", "any")
                .register(meterRegistry);
        Gauge.builder("amount_toons", this.amountAbove100LaffPigs, AtomicInteger::get)
                .tag("species", "pig")
                .tag("laff", "100")
                .register(meterRegistry);
        Gauge.builder("amount_toons", this.amountMaxedPigs, AtomicInteger::get)
                .tag("species", "pig")
                .tag("laff", "max")
                .register(meterRegistry);

        Gauge.builder("amount_toons", this.amountRabbits, AtomicInteger::get)
                .tag("species", "rabbit")
                .tag("laff", "any")
                .register(meterRegistry);
        Gauge.builder("amount_toons", this.amountAbove100LaffRabbits, AtomicInteger::get)
                .tag("species", "rabbit")
                .tag("laff", "100")
                .register(meterRegistry);
        Gauge.builder("amount_toons", this.amountMaxedRabbits, AtomicInteger::get)
                .tag("species", "rabbit")
                .tag("laff", "max")
                .register(meterRegistry);

        Gauge.builder("amount_toons", this.amountDeer, AtomicInteger::get)
                .tag("species", "deer")
                .tag("laff", "any")
                .register(meterRegistry);
        Gauge.builder("amount_toons", this.amountAbove100LaffDeer, AtomicInteger::get)
                .tag("species", "deer")
                .tag("laff", "100")
                .register(meterRegistry);
        Gauge.builder("amount_toons", this.amountMaxedDeer, AtomicInteger::get)
                .tag("species", "deer")
                .tag("laff", "max")
                .register(meterRegistry);

        Gauge.builder("amount_toons", this.amountCrocodiles, AtomicInteger::get)
                .tag("species", "crocodile")
                .tag("laff", "any")
                .register(meterRegistry);
        Gauge.builder("amount_toons", this.amountAbove100LaffCrocodiles, AtomicInteger::get)
                .tag("species", "crocodile")
                .tag("laff", "100")
                .register(meterRegistry);
        Gauge.builder("amount_toons", this.amountMaxedCrocodiles, AtomicInteger::get)
                .tag("species", "crocodile")
                .tag("laff", "max")
                .register(meterRegistry);

        Gauge.builder("amount_gags", this.amountToonsWithToonUp, AtomicInteger::get)
                .tag("gag", "toonup")
                .tag("maxed", "n/a")
                .register(meterRegistry);
        Gauge.builder("amount_gags", this.amountToonsWithMaxToonUp, AtomicInteger::get)
                .tag("gag", "toonup")
                .tag("maxed", "yes")
                .register(meterRegistry);
        Gauge.builder("amount_organic_gags", this.amountToonsWithOrganicToonUp, AtomicInteger::get)
                .tag("gag", "toonup")
                .register(meterRegistry);

        Gauge.builder("amount_gags", this.amountToonsWithTrap, AtomicInteger::get)
                .tag("gag", "trap")
                .tag("maxed", "n/a")
                .register(meterRegistry);
        Gauge.builder("amount_gags", this.amountToonsWithMaxTrap, AtomicInteger::get)
                .tag("gag", "trap")
                .tag("maxed", "yes")
                .register(meterRegistry);
        Gauge.builder("amount_organic_gags", this.amountToonsWithOrganicTrap, AtomicInteger::get)
                .tag("gag", "trap")
                .register(meterRegistry);

        Gauge.builder("amount_gags", this.amountToonsWithLure, AtomicInteger::get)
                .tag("gag", "lure")
                .tag("maxed", "n/a")
                .register(meterRegistry);
        Gauge.builder("amount_gags", this.amountToonsWithMaxLure, AtomicInteger::get)
                .tag("gag", "lure")
                .tag("maxed", "yes")
                .register(meterRegistry);
        Gauge.builder("amount_organic_gags", this.amountToonsWithOrganicLure, AtomicInteger::get)
                .tag("gag", "lure")
                .register(meterRegistry);

        Gauge.builder("amount_gags", this.amountToonsWithSound, AtomicInteger::get)
                .tag("gag", "sound")
                .tag("maxed", "n/a")
                .register(meterRegistry);
        Gauge.builder("amount_gags", this.amountToonsWithMaxSound, AtomicInteger::get)
                .tag("gag", "sound")
                .tag("maxed", "yes")
                .register(meterRegistry);
        Gauge.builder("amount_organic_gags", this.amountToonsWithOrganicSound, AtomicInteger::get)
                .tag("gag", "sound")
                .register(meterRegistry);

        Gauge.builder("amount_gags", this.amountToonsWithThrow, AtomicInteger::get)
                .tag("gag", "throw")
                .tag("maxed", "n/a")
                .register(meterRegistry);
        Gauge.builder("amount_gags", this.amountToonsWithMaxThrow, AtomicInteger::get)
                .tag("gag", "throw")
                .tag("maxed", "yes")
                .register(meterRegistry);
        Gauge.builder("amount_organic_gags", this.amountToonsWithOrganicThrow, AtomicInteger::get)
                .tag("gag", "throw")
                .register(meterRegistry);

        Gauge.builder("amount_gags", this.amountToonsWithSquirt, AtomicInteger::get)
                .tag("gag", "squirt")
                .tag("maxed", "n/a")
                .register(meterRegistry);
        Gauge.builder("amount_gags", this.amountToonsWithMaxSquirt, AtomicInteger::get)
                .tag("gag", "squirt")
                .tag("maxed", "yes")
                .register(meterRegistry);
        Gauge.builder("amount_organic_gags", this.amountToonsWithOrganicSquirt, AtomicInteger::get)
                .tag("gag", "squirt")
                .register(meterRegistry);

        Gauge.builder("amount_gags", this.amountToonsWithDrop, AtomicInteger::get)
                .tag("gag", "drop")
                .tag("maxed", "n/a")
                .register(meterRegistry);
        Gauge.builder("amount_gags", this.amountToonsWithMaxDrop, AtomicInteger::get)
                .tag("gag", "drop")
                .tag("maxed", "yes")
                .register(meterRegistry);
        Gauge.builder("amount_organic_gags", this.amountToonsWithOrganicDrop, AtomicInteger::get)
                .tag("gag", "drop")
                .register(meterRegistry);
    }

    @Scheduled(fixedRate = 60 * 1000)
    public void getToonStats() {
        final Iterable<ToonHQToon> toonsIterable = this.toonRepository.findAll();
        final List<ToonHQToon> toonsList = new ArrayList<>();
        toonsIterable.forEach(toonsList::add);

        this.amountToons.set(toonsList.size());

        this.amountBears.set((int) toonsList.stream().filter(t -> t.getSpecies().equalsIgnoreCase("bear")).count());
        this.amountAbove100LaffBears.set((int) toonsList.stream().filter(t -> t.getSpecies().equalsIgnoreCase("bear") && t.getLaff() >= 100).count());
        this.amountMaxedBears.set((int) toonsList.stream().filter(t -> t.getSpecies().equalsIgnoreCase("bear") && t.getLaff() >= MAX_LAFF).count());

        this.amountCats.set((int) toonsList.stream().filter(t -> t.getSpecies().equalsIgnoreCase("cat")).count());
        this.amountAbove100LaffCats.set((int) toonsList.stream().filter(t -> t.getSpecies().equalsIgnoreCase("cat") && t.getLaff() >= 100).count());
        this.amountMaxedCats.set((int) toonsList.stream().filter(t -> t.getSpecies().equalsIgnoreCase("cat") && t.getLaff() >= MAX_LAFF).count());

        this.amountDogs.set((int) toonsList.stream().filter(t -> t.getSpecies().equalsIgnoreCase("dog")).count());
        this.amountAbove100LaffDogs.set((int) toonsList.stream().filter(t -> t.getSpecies().equalsIgnoreCase("dog") && t.getLaff() >= 100).count());
        this.amountMaxedDogs.set((int) toonsList.stream().filter(t -> t.getSpecies().equalsIgnoreCase("dog") && t.getLaff() >= MAX_LAFF).count());

        this.amountDucks.set((int) toonsList.stream().filter(t -> t.getSpecies().equalsIgnoreCase("duck")).count());
        this.amountAbove100LaffDucks.set((int) toonsList.stream().filter(t -> t.getSpecies().equalsIgnoreCase("duck") && t.getLaff() >= 100).count());
        this.amountMaxedDucks.set((int) toonsList.stream().filter(t -> t.getSpecies().equalsIgnoreCase("duck") && t.getLaff() >= MAX_LAFF).count());

        this.amountHorses.set((int) toonsList.stream().filter(t -> t.getSpecies().equalsIgnoreCase("horse")).count());
        this.amountAbove100LaffHorses.set((int) toonsList.stream().filter(t -> t.getSpecies().equalsIgnoreCase("horse") && t.getLaff() >= 100).count());
        this.amountMaxedHorses.set((int) toonsList.stream().filter(t -> t.getSpecies().equalsIgnoreCase("horse") && t.getLaff() >= MAX_LAFF).count());

        this.amountMonkeys.set((int) toonsList.stream().filter(t -> t.getSpecies().equalsIgnoreCase("monkey")).count());
        this.amountAbove100LaffMonkeys.set((int) toonsList.stream().filter(t -> t.getSpecies().equalsIgnoreCase("monkey") && t.getLaff() >= 100).count());
        this.amountMaxedMonkeys.set((int) toonsList.stream().filter(t -> t.getSpecies().equalsIgnoreCase("monkey") && t.getLaff() >= MAX_LAFF).count());

        this.amountMice.set((int) toonsList.stream().filter(t -> t.getSpecies().equalsIgnoreCase("mouse")).count());
        this.amountAbove100LaffMice.set((int) toonsList.stream().filter(t -> t.getSpecies().equalsIgnoreCase("mouse") && t.getLaff() >= 100).count());
        this.amountMaxedMice.set((int) toonsList.stream().filter(t -> t.getSpecies().equalsIgnoreCase("mouse") && t.getLaff() >= MAX_LAFF).count());

        this.amountPigs.set((int) toonsList.stream().filter(t -> t.getSpecies().equalsIgnoreCase("pig")).count());
        this.amountAbove100LaffPigs.set((int) toonsList.stream().filter(t -> t.getSpecies().equalsIgnoreCase("pig") && t.getLaff() >= 100).count());
        this.amountMaxedPigs.set((int) toonsList.stream().filter(t -> t.getSpecies().equalsIgnoreCase("pig") && t.getLaff() >= MAX_LAFF).count());

        this.amountRabbits.set((int) toonsList.stream().filter(t -> t.getSpecies().equalsIgnoreCase("rabbit")).count());
        this.amountAbove100LaffRabbits.set((int) toonsList.stream().filter(t -> t.getSpecies().equalsIgnoreCase("rabbit") && t.getLaff() >= 100).count());
        this.amountMaxedRabbits.set((int) toonsList.stream().filter(t -> t.getSpecies().equalsIgnoreCase("rabbit") && t.getLaff() >= MAX_LAFF).count());

        this.amountDeer.set((int) toonsList.stream().filter(t -> t.getSpecies().equalsIgnoreCase("deer")).count());
        this.amountAbove100LaffDeer.set((int) toonsList.stream().filter(t -> t.getSpecies().equalsIgnoreCase("deer") && t.getLaff() >= 100).count());
        this.amountMaxedDeer.set((int) toonsList.stream().filter(t -> t.getSpecies().equalsIgnoreCase("deer") && t.getLaff() >= MAX_LAFF).count());

        this.amountCrocodiles.set((int) toonsList.stream().filter(t -> t.getSpecies().equalsIgnoreCase("crocodile")).count());
        this.amountAbove100LaffCrocodiles.set((int) toonsList.stream().filter(t -> t.getSpecies().equalsIgnoreCase("crocodile") && t.getLaff() >= 100).count());
        this.amountMaxedCrocodiles.set((int) toonsList.stream().filter(t -> t.getSpecies().equalsIgnoreCase("crocodile") && t.getLaff() >= MAX_LAFF).count());

        this.amountToonsWithToonUp.set((int) toonsList.stream().filter(t -> t.getToonup() >= 1).count());
        this.amountToonsWithMaxToonUp.set((int) toonsList.stream().filter(t -> t.getToonup() >= 7).count());
        this.amountToonsWithOrganicToonUp.set((int) toonsList.stream().filter(t -> t.getOrganic().equalsIgnoreCase("toonup")).count());

        this.amountToonsWithTrap.set((int) toonsList.stream().filter(t -> t.getTrap() >= 1).count());
        this.amountToonsWithMaxTrap.set((int) toonsList.stream().filter(t -> t.getTrap() >= 7).count());
        this.amountToonsWithOrganicTrap.set((int) toonsList.stream().filter(t -> t.getOrganic().equalsIgnoreCase("trap")).count());

        this.amountToonsWithLure.set((int) toonsList.stream().filter(t -> t.getLure() >= 1).count());
        this.amountToonsWithMaxLure.set((int) toonsList.stream().filter(t -> t.getLure() >= 7).count());
        this.amountToonsWithOrganicLure.set((int) toonsList.stream().filter(t -> t.getOrganic().equalsIgnoreCase("lure")).count());

        this.amountToonsWithSound.set((int) toonsList.stream().filter(t -> t.getSound() >= 1).count());
        this.amountToonsWithMaxSound.set((int) toonsList.stream().filter(t -> t.getSound() >= 7).count());
        this.amountToonsWithOrganicSound.set((int) toonsList.stream().filter(t -> t.getOrganic().equalsIgnoreCase("sound")).count());

        this.amountToonsWithThrow.set((int) toonsList.stream().filter(t -> t.getThrowGag() >= 1).count());
        this.amountToonsWithMaxThrow.set((int) toonsList.stream().filter(t -> t.getThrowGag() >= 7).count());
        this.amountToonsWithOrganicThrow.set((int) toonsList.stream().filter(t -> t.getOrganic().equalsIgnoreCase("throw")).count());

        this.amountToonsWithSquirt.set((int) toonsList.stream().filter(t -> t.getSquirt() >= 1).count());
        this.amountToonsWithMaxSquirt.set((int) toonsList.stream().filter(t -> t.getSquirt() >= 7).count());
        this.amountToonsWithOrganicSquirt.set((int) toonsList.stream().filter(t -> t.getOrganic().equalsIgnoreCase("squirt")).count());

        this.amountToonsWithDrop.set((int) toonsList.stream().filter(t -> t.getDrop() >= 1).count());
        this.amountToonsWithMaxDrop.set((int) toonsList.stream().filter(t -> t.getDrop() >= 7).count());
        this.amountToonsWithOrganicDrop.set((int) toonsList.stream().filter(t -> t.getOrganic().equalsIgnoreCase("drop")).count());

        this.amountToonsWithNoOrganic.set((int) toonsList.stream().filter(t -> t.getOrganic().equalsIgnoreCase("none")).count());
    }
}

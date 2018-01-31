package rc;

import org.powerbot.script.Condition;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.*;

import javax.swing.*;
import java.util.concurrent.Callable;

@Script.Manifest(name="abc", description="abc", properties="author=abc; topic=999; client=4;")

public class nature extends PollingScript<ClientContext> {

    public static final Tile[] pathToBank = {new Tile(3379, 3163, 0), new Tile(3374, 3168, 0), new Tile(3369, 3170, 0)};
    public static final Tile[] pathToPortal = {new Tile(3361, 3164, 0), new Tile(3352, 3163, 0)};
    public static final Tile[] pathToRing = {new Tile(2620, 3222, 0), new Tile(2631, 3223, 0), new Tile(2643, 3226, 0), new Tile(2652, 3232, 0), new Tile(2658, 3228, 0)};
    public static final Tile[] pathToAltar = {new Tile(2803, 2993, 0), new Tile(2812, 2998, 0), new Tile(2821, 3008, 0), new Tile(2835, 3008, 0), new Tile(2848, 3008, 0), new Tile(2861, 3010, 0), new Tile(2868, 3017, 0)};
    public static final Tile clanWarsTile = new Tile(3327, 4751, 0);
    public static final Tile monasteryTile = new Tile(2606, 3222, 0);
    public static final Tile bankTile = new Tile(3387, 3159, 0);
    public static final Tile karamjaTile = new Tile(2801, 3003, 0);

    final static int[] RING_ID = {2552, 2554, 2556, 2558 ,2560, 2562, 2564, 2566};
    final static int SMALL_POUCH = 5509;
    final static int MED_POUCH = 5510;
    final static int RUNE_ESS = 7936;
    final static int FAIRY_ID = 29538;

    @Override
    public void start() {

    }

    @Override
    public void stop() {
        ctx.controller.stop();
    }

    @Override
    public void poll() {
        run();
    }

    public void run() {
        bank();
        enterPortal();
        walkToRing();
        walkToAltar();
        craft();
        walkToBank();
    }

    public void check() {
        Item rings = ctx.equipment.select().id(RING_ID).poll();
        if(ctx.equipment.select().id(rings).isEmpty()) {
            System.out.println("no ring of dueling");
        } else {
            System.out.println("ring of dueling equipped");
        }
    }

    public void bank() {
        System.out.println("banking..");
        Item rings = ctx.equipment.select().id(RING_ID).poll();
        if(ctx.equipment.select().id(rings).isEmpty()){
            System.out.println("no ring of dueling");
            ctx.game.tab(Game.Tab.INVENTORY);
            ctx.bank.open();
            ctx.bank.deposit("Nature rune", 35);
            ctx.bank.withdraw(2552, 1);
            ctx.bank.withdraw(RUNE_ESS, 35);
            ctx.bank.close();
            ctx.inventory.select().id(SMALL_POUCH).poll().interact("Fill");
            Condition.sleep(500);
            ctx.inventory.select().id(MED_POUCH).poll().interact("Fill");
            Condition.sleep(500);
            ctx.inventory.select().id(2552).poll().interact("Wear");
            ctx.bank.open();
            ctx.bank.withdraw(RUNE_ESS, 35);
        } else {
            System.out.println("ring of dueling equipped");
            ctx.game.tab(Game.Tab.INVENTORY);
            ctx.bank.open();
            ctx.bank.deposit("Nature rune", 35);
            ctx.bank.withdraw(RUNE_ESS, 35);
            ctx.bank.close();
            ctx.inventory.select().id(SMALL_POUCH).poll().interact("Fill");
            Condition.sleep(500);
            ctx.inventory.select().id(MED_POUCH).poll().interact("Fill");
            ctx.bank.open();
            ctx.bank.withdraw(RUNE_ESS, 35);
        }
    }

    public void enterPortal() {
        System.out.println("going to portal..");
        ctx.movement.step(pathToPortal[0]);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return pathToPortal[0].distanceTo(ctx.players.local()) < 5;
            }
        });
        ctx.movement.step(pathToPortal[1]);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return !ctx.players.local().inMotion();
            }
        });
        GameObject portal = ctx.objects.select().id(26645).poll();
        portal.interact("Enter");
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return clanWarsTile.distanceTo(ctx.players.local()) < 2;
            }
        },100,30);
        ctx.game.tab(Game.Tab.EQUIPMENT);
        Condition.sleep(500);
        ctx.equipment.select().id(13121).poll().interact("Kandarin Monastery");
        ctx.game.tab(Game.Tab.INVENTORY);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return monasteryTile.distanceTo(ctx.players.local()) < 10;
            }
        },100,30);
    }

    public void walkToBank() {
        ctx.movement.step(pathToBank[0]);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return pathToBank[0].distanceTo(ctx.players.local()) < 5;
            }
        });
        ctx.movement.step(pathToBank[1]);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return pathToBank[1].distanceTo(ctx.players.local()) < 5;
            }
        });
        ctx.movement.step(pathToBank[2]);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return !ctx.players.local().inMotion();
            }
        });
    }

    public void walkToRing() {
        System.out.println("walking to fairy ring..");
        ctx.movement.step(pathToRing[0]);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return pathToRing[0].distanceTo(ctx.players.local()) < 6;
            }
        });
        ctx.movement.step(pathToRing[1]);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return pathToRing[1].distanceTo(ctx.players.local()) < 6;
            }
        });
        ctx.movement.step(pathToRing[2]);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return pathToRing[2].distanceTo(ctx.players.local()) < 6;
            }
        });
        ctx.movement.step(pathToRing[3]);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return pathToRing[3].distanceTo(ctx.players.local()) < 6;
            }
        });
        ctx.movement.step(pathToRing[4]);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return !ctx.players.local().inMotion();
            }
        });
        enter();
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return karamjaTile.distanceTo(ctx.players.local()) < 2;
            }
        });
    }

    public void enter() {
        GameObject portal = ctx.objects.select().id(FAIRY_ID).nearest().poll();
        portal.interact("Last-destination (CKR)", "Fairy ring");


    }

    public void walkToAltar() {
        System.out.println("walking to altar..");
        ctx.movement.step(pathToAltar[0]);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return pathToAltar[0].distanceTo(ctx.players.local()) < 5;
            }
        });
        ctx.movement.step(pathToAltar[1]);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return pathToAltar[1].distanceTo(ctx.players.local()) < 5;
            }
        });
        ctx.movement.step(pathToAltar[2]);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return pathToAltar[2].distanceTo(ctx.players.local()) < 5;
            }
        });
        ctx.movement.step(pathToAltar[3]);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return pathToAltar[3].distanceTo(ctx.players.local()) < 5;
            }
        });
        ctx.movement.step(pathToAltar[4]);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return pathToAltar[4].distanceTo(ctx.players.local()) < 5;
            }
        });
        ctx.movement.step(pathToAltar[5]);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return pathToAltar[5].distanceTo(ctx.players.local()) < 5;
            }
        });
        ctx.movement.step(pathToAltar[6]);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return !ctx.players.local().inMotion();
            }
        });
        enterAlter();
        Condition.sleep(1000);
    }

    public void enterAlter() {
        GameObject altarPortal = ctx.objects.select().id(14832).poll();
        altarPortal.interact("Enter");
    }

    public void craft() {
        System.out.println("crafting..");
        GameObject altar = ctx.objects.select().id(14905).poll();
        Item small = ctx.inventory.select().id(SMALL_POUCH).poll();
        Item med = ctx.inventory.select().id(MED_POUCH).poll();
        altar.interact("Craft-rune");
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.inventory.select().id(561).count() == 26;
            }
        },100,30);
        Condition.sleep(500);
        small.interact("Empty");
        Condition.sleep(700);
        med.interact("Empty");
        Condition.sleep(700);
        altar.interact("Craft-rune");
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().animation() == -1;
            }
        });
        Condition.sleep(500);
        ctx.game.tab(Game.Tab.EQUIPMENT);
        Condition.sleep(500);
        ctx.equipment.select().id(RING_ID).poll().interact("Clan Wars");
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return bankTile.distanceTo(ctx.players.local()) < 10;
            }
        });
    }

    public boolean checkPouches() {
        return false;
    }



}

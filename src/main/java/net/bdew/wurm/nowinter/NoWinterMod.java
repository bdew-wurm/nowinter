package net.bdew.wurm.nowinter;

import com.wurmonline.client.game.SeasonManager;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.classhooks.InvocationHandlerFactory;
import org.gotti.wurmunlimited.modloader.interfaces.Configurable;
import org.gotti.wurmunlimited.modloader.interfaces.Initable;
import org.gotti.wurmunlimited.modloader.interfaces.PreInitable;
import org.gotti.wurmunlimited.modloader.interfaces.WurmClientMod;

import java.lang.reflect.InvocationHandler;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NoWinterMod implements WurmClientMod, Initable, PreInitable, Configurable {
    private static Logger logger = Logger.getLogger("NoWinterMod");

    private static int lockSeason = -1;

    @Override
    public void configure(Properties properties) {
        lockSeason = Integer.parseInt(properties.getProperty("lockSeason", "-1"));
    }

    @Override
    public void init() {
        logger.fine("Initializing");
        try {
            HookManager.getInstance().registerHook("com.wurmonline.client.game.SeasonManager", "selectSeason", "(FF)Lcom/wurmonline/client/game/SeasonManager$Season;", new InvocationHandlerFactory() {
                @Override
                public InvocationHandler createInvocationHandler() {
                    return (proxy, method, args) -> {
                        if (lockSeason >= 0) {
                            return SeasonManager.Season.values()[lockSeason];
                        } else {
                            if ((Float) args[0] < 0)
                                args[0] = 0f;
                            return method.invoke(proxy, args);
                        }
                    };
                }
            });
            logger.fine("Loaded");
        } catch (Throwable e) {
            logger.log(Level.SEVERE, "Error loading mod", e);
        }

    }

    @Override
    public void preInit() {

    }
}

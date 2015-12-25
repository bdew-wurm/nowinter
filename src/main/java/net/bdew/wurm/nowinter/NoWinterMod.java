package net.bdew.wurm.nowinter;

import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.classhooks.InvocationHandlerFactory;
import org.gotti.wurmunlimited.modloader.interfaces.Initable;
import org.gotti.wurmunlimited.modloader.interfaces.PreInitable;
import org.gotti.wurmunlimited.modloader.interfaces.WurmMod;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NoWinterMod implements WurmMod, Initable, PreInitable {
    private static Logger logger = Logger.getLogger("NoWinterMod");

    @Override
    public void init() {
        logger.fine("Initializing");
        try {
            HookManager.getInstance().registerHook("com.wurmonline.client.game.SeasonManager", "selectSeason", "(FF)Lcom/wurmonline/client/game/SeasonManager$Season;", new InvocationHandlerFactory() {
                @Override
                public InvocationHandler createInvocationHandler() {
                    return new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
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

/*
 * Minecraft Forge
 * Copyright (c) 2016-2022.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package net.minecraftforge.fml.javafmlmod;

import com.mohistmc.util.i18n.i18n;
import net.minecraftforge.eventbus.EventBusErrorMessage;
import net.minecraftforge.eventbus.api.BusBuilder;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.IEventListener;
import net.minecraftforge.fml.AutomaticEventSubscriber;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingException;
import net.minecraftforge.fml.ModLoadingStage;
import net.minecraftforge.fml.event.lifecycle.IModBusEvent;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

import static net.minecraftforge.fml.Logging.LOADING;

public class FMLModContainer extends ModContainer
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final ModFileScanData scanResults;
    private final IEventBus eventBus;
    private Object modInstance;
    private final Class<?> modClass;

    public FMLModContainer(IModInfo info, String className, ClassLoader modClassLoader, ModFileScanData modFileScanResults)
    {
        super(info);
        LOGGER.debug(LOADING, i18n.get("fmlmodcontainer.1", className, modClassLoader, getClass().getClassLoader()));
        this.scanResults = modFileScanResults;
        activityMap.put(ModLoadingStage.CONSTRUCT, this::constructMod);
        this.eventBus = BusBuilder.builder().setExceptionHandler(this::onEventFailed).setTrackPhases(false).markerType(IModBusEvent.class).build();
        this.configHandler = Optional.of(this.eventBus::post);
        final FMLJavaModLoadingContext contextExtension = new FMLJavaModLoadingContext(this);
        this.contextExtension = () -> contextExtension;
        try
        {
            modClass = Class.forName(className, true, modClassLoader);
            LOGGER.debug(LOADING, i18n.get("fmlmodcontainer.2", modClass.getName(), modClass.getClassLoader()));
        }
        catch (Throwable e)
        {
            LOGGER.error(LOADING, i18n.get("fmlmodcontainer.3", className), e);
            throw new ModLoadingException(info, ModLoadingStage.CONSTRUCT, "fml.modloading.failedtoloadmodclass", e);
        }
    }

    private void onEventFailed(IEventBus iEventBus, Event event, IEventListener[] iEventListeners, int i, Throwable throwable)
    {
        LOGGER.error(new EventBusErrorMessage(event, i, iEventListeners, throwable));
    }

    private void constructMod()
    {
        try
        {
            LOGGER.debug(LOADING, i18n.get("fmlmodcontainer.4", getModId(), modClass.getName()));
            this.modInstance = modClass.newInstance();
            LOGGER.debug(LOADING, i18n.get("fmlmodcontainer.4", getModId(), modClass.getName()));
        }
        catch (Throwable e)
        {
            LOGGER.error(LOADING, i18n.get("fmlmodcontainer.5", getModId(), modClass.getName()), e);
            throw new ModLoadingException(modInfo, ModLoadingStage.CONSTRUCT, "fml.modloading.failedtoloadmod", e, modClass);
        }
        try {
            LOGGER.debug(LOADING, i18n.get("fmlmodcontainer.6", getModId()));
            AutomaticEventSubscriber.inject(this, this.scanResults, this.modClass.getClassLoader());
            LOGGER.debug(LOADING, i18n.get("fmlmodcontainer.11", getModId()));
        } catch (Throwable e) {
            LOGGER.error(LOADING, i18n.get("fmlmodcontainer.7", getModId(), modClass.getName()), e);
            throw new ModLoadingException(modInfo, ModLoadingStage.CONSTRUCT, "fml.modloading.failedtoloadmod", e, modClass);
        }
    }

    @Override
    public boolean matches(Object mod)
    {
        return mod == modInstance;
    }

    @Override
    public Object getMod()
    {
        return modInstance;
    }

    public IEventBus getEventBus()
    {
        return this.eventBus;
    }

    @Override
    protected <T extends Event & IModBusEvent> void acceptEvent(final T e) {
        try {
            LOGGER.debug(LOADING, i18n.get("fmlmodcontainer.8", this.getModId(), e));
            this.eventBus.post(e);
            LOGGER.debug(LOADING, i18n.get("fmlmodcontainer.9", this.getModId(), e));
        } catch (Throwable t) {
            LOGGER.error(LOADING, i18n.get("fmlmodcontainer.10", e, this.getModId()), t);
            throw new ModLoadingException(modInfo, modLoadingStage, "fml.modloading.errorduringevent", t);
        }
    }
}

/*
 * Hytilities - Hypixel focused Quality of Life mod.
 * Copyright (C) 2020  Sk1er LLC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package club.sk1er.hytilities.handlers.general;

import club.sk1er.hytilities.Hytilities;
import club.sk1er.hytilities.config.HytilitiesConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoStart {

    private int ticks;
    private GuiScreen gui;

    @SubscribeEvent
    public void openGui(GuiScreenEvent.InitGuiEvent event) {
        this.gui = event.gui;
    }

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent event) {
        if (this.gui instanceof GuiMainMenu && Hytilities.INSTANCE.isLoadedCall() && HytilitiesConfig.autoStart) {
            // we need to delay for a second as gl context needs to be created first,
            // otherwise everything is blocky & untextured for a few seconds
            // when joining a world.
            if (this.ticks++ == 20) {
                final GuiMultiplayer guiMultiplayer = new GuiMultiplayer(this.gui);
                guiMultiplayer.setWorldAndResolution(Minecraft.getMinecraft(), this.gui.width, this.gui.height);
                guiMultiplayer.directConnect = true;
                guiMultiplayer.selectedServer = new ServerData("hypixel", "hypixel.net", false);
                guiMultiplayer.confirmClicked(true, 0);
                Hytilities.INSTANCE.setLoadedCall(false);

                MinecraftForge.EVENT_BUS.unregister(this);
            }
        }
    }
}

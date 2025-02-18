/*
 * Mohist - MohistMC
 * Copyright (C) 2018-2022.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.mohistmc.entity;

import com.mohistmc.api.ServerAPI;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraftforge.registries.ForgeRegistries;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftMinecartContainer;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public class CraftCustomMinecartContainer extends CraftMinecartContainer {

    private final EntityType entityType;

    public CraftCustomMinecartContainer(CraftServer server, AbstractMinecartEntity entity) {
        super(server, entity);
        this.entityType = EntityType.valueOf(ServerAPI.entityTypeMap.get(ForgeRegistries.ENTITIES.getKey(entity.getType())));
    }

    @NotNull
    @Override
    public EntityType getType() {
        return entityType;
    }

    @Override
    public String toString() {
        return "CraftCustomMinecartContainer{" + entityType + '}';
    }
}

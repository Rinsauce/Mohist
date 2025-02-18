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

package net.minecraftforge.fml.client;

public class ExtendedServerListData {
    public final String type;
    public final boolean isCompatible;
    public int numberOfMods;
    public String extraReason;
    public final boolean truncated;

    public ExtendedServerListData(String type, boolean isCompatible, int num, String extraReason)
    {
        this(type, isCompatible, num, extraReason, false);
    }

    public ExtendedServerListData(String type, boolean isCompatible, int num, String extraReason, boolean truncated)
    {
        this.type = type;
        this.isCompatible = isCompatible;
        this.numberOfMods = num;
        this.extraReason = extraReason;
        this.truncated = truncated;
    }
}

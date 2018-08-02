/*
 *
 * This file is part of Factionify, licensed under the MIT License (MIT).
 * Copyright (c) Innectic
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package me.ifydev.factionify.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Innectic
 * @since 01/17/2018
 */
public class FactionifyConstants {

    public static final String FACTIONIFY_PREFIX = "&a&lFactionify> ";

    public static final String DEFAULT_OWNER_NAME = "Owner";
    public static final String DEFAULT_MEMBER_NAME = "Member";
    public static final String DEFAULT_DESCRIPTION = "Surely something.";

    public static final String PERMISSION_ADMIN = "factionify.admin";
    public static final String PERMISSION_HELP = "factionify.help";
    public static final String PERMISSION_BASE = "factionify.base";
    public static final String PERMISSION_FACTION_CREATE = "factionify.faction.create";
    public static final String PERMISSION_FACTION_DISBAND = "factionify.faction.disband";
    public static final String PERMISSION_FACTION_MANAGE = "factionify.faction.manage";

    public static final String YOU_DONT_HAVE_PERMISSION = FACTIONIFY_PREFIX + "&c&lYou don't have permission for this command!";
    public static final String YOU_DONT_HAVE_POWER = FACTIONIFY_PREFIX + "&c&lYour power is too low!";
    public static final String YOU_ARENT_A_PLAYER = FACTIONIFY_PREFIX + "&c&lYou must be a player to run this command!";
    public static final String YOU_ARENT_IN_A_FACTION = "&c&lYou aren't in a faction!";

    public static final String DATABASE_HANDLER_NOT_PRESENT = FACTIONIFY_PREFIX + "&c&lUnable to continue: Database handler not present?!";
    public static final String DATABASE_ERROR = FACTIONIFY_PREFIX + "&c&lA database error has occurred. Please check the console for more details.";
    public static final String INTERNAL_ERROR = FACTIONIFY_PREFIX + "&4&lINTERNAL ERROR: &c&l<ERROR>";

    private static final String NOT_ENOUGH_ARGUMENTS_BASE = FACTIONIFY_PREFIX + "&c&lNot enough arguments! ";
    public static final String NOT_ENOUGH_ARGUMENTS_FACTION_CREATE = NOT_ENOUGH_ARGUMENTS_BASE + "/factionify create <name>";
    public static final String NOT_ENOUGH_ARGUMENTS_FACTION_INVITE = NOT_ENOUGH_ARGUMENTS_BASE + "/factionify invite <username>";
    public static final String NOT_ENOUGH_ARGUMENTS_FACTION_PROMOTE = NOT_ENOUGH_ARGUMENTS_BASE + "/factionify promote <username>";
    public static final String NOT_ENOUGH_ARGUMENTS_FACTION_DEMOTE = NOT_ENOUGH_ARGUMENTS_BASE + "/factionify demote <username>";
    public static final String NOT_ENOUGH_ARGUMENTS_GET_ROLES = NOT_ENOUGH_ARGUMENTS_BASE + "/factionify roles";
    public static final String NOT_ENOUGH_ARGUMENTS_GET_ROLE = NOT_ENOUGH_ARGUMENTS_BASE + "/factionify role <username>";
    public static final String NOT_ENOUGH_ARGUMENTS_SET_ROLE = NOT_ENOUGH_ARGUMENTS_BASE + "/factionify role <username> set <role>";
    public static final String NOT_ENOUGH_ARGUMENTS_RULE = NOT_ENOUGH_ARGUMENTS_BASE + "/factionify rule <list|get|set>";
    public static final String NOT_ENOUGH_ARGUMENTS_RULE_GET = NOT_ENOUGH_ARGUMENTS_BASE + "/factionify rule get <rule>";
    public static final String NOT_ENOUGH_ARGUMENTS_RULE_SET = NOT_ENOUGH_ARGUMENTS_BASE + "/factionify rule set <rule> <value>";

    public static final String FACTION_CREATED = FACTIONIFY_PREFIX + "&e&lFaction <NAME> created!";
    public static final String FACTION_CREATED_BROADCAST = FACTIONIFY_PREFIX + "&e&lFaction <NAME> has been formed.";  // :ConfigurableMessages
    public static final String FACTION_DISBANDED = FACTIONIFY_PREFIX + "&e&lFaction <NAME> has been disbanded.";
    public static final String FACTION_DISBANDED_BROADCAST = FACTIONIFY_PREFIX + "&e&lFaction <NAME> has fallen.";  // :ConfigurableMessages

    public static final String FACTIONIFY_HELP_HEADER = "&e================== &a&lFactionify Help &e==================";
    public static final String FACTIONIFY_HELP_FOOTER = "&e=====================================================";
    public static final List<List<String>> FACTIONIFY_HELP = Arrays.asList(
            Arrays.asList(
                    "/factionify create <name>",
                    "/factionify disband",
                    "/factionify invite <username>",
                    "/factionify promote <username>",
                    "/factionify demote <username>",
                    "/factionify roles",
                    "/factionify role <username>",
                    "/factionify role <username> set <role>"
            ),
            Arrays.asList(
                    "/factionify rule list",
                    "/factionify rule get <rule>",
                    "/factionify rule set <rule> <value>"
            )
    );

    public static final List<String> FACTIONIFY_ERROR = new ArrayList<>(Arrays.asList(
            "&c&lError encountered: <ERROR_TYPE>",
            "&c&lShould this be reported?: <SHOULD_REPORT>"
    ));
}

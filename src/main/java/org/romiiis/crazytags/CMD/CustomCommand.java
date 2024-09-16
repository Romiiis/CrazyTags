package org.romiiis.crazytags.CMD;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.romiiis.crazytags.CrazyTags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CustomCommand implements CommandExecutor, TabCompleter {


    /* Tato hashmapa udrzuje nazev podprikazu a obejekt ktery implementuje interface */
    private final HashMap<String, SubCommand> subCommands = new HashMap<>();




    /* Tato metoda se vola pri inicializaci pluginu */
    public CustomCommand() {

        /* Naplneni hashmapy */
        subCommands.put("version", new cmd_version());
        subCommands.put("clean", new cmd_clean());
        subCommands.put("open", new cmd_open());
    }





    /* Tato metoda se vola pri spusteni prikazu */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        /* Pokud prikaz neni zavolany hracem, tak se odesle zprava */
        if(!(commandSender instanceof Player)){
            commandSender.sendMessage("Only players can execute this command!");
            return true;
        }

        /* Pokud je prikaz zavolany bez argumentu, tak se zobrazi napoveda */
        if(strings.length == 0){

            getHelp((Player) commandSender);
            return true;
        }

        /* Pokud je prikaz zavolany s argumentem, tak se zkontroluje jestli je argument nazvem podprikazu */
        String commandName = "ct";
        String commandName2 = "crazytags";
        if(command.getName().equalsIgnoreCase(commandName) || command.getName().equalsIgnoreCase(commandName2)){

            // Check if the first argument is the command name

            if(subCommands.containsKey(strings[0])){

                // Check if the player has PERM_PREF  + permission to execute the command (add permission prefix before the command name)
                for (String permission : subCommands.get(strings[0]).getPermissions()) {
                    if (commandSender.hasPermission(CrazyTags.PERM_PREFIX + permission)) {

                        // Get the subcommand
                        SubCommand subCommand = subCommands.get(strings[0]);
                        subCommand.execute((Player) commandSender, strings);
                        return true;
                    }
                }




            }
        }

        /* Pokud je argument neplatny, tak se zobrazi napoveda */
        getHelp((Player) commandSender);


        return true;

    }






    private void getHelp(Player player) {

        player.sendMessage("----| §SuperTags§r§4 Help§r |----");

        for(SubCommand subCommand : subCommands.values()){

            player.sendMessage(subCommand.getHelp());

        }

    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> tabs = new ArrayList<>();

        if(args.length == 1){
            for(String subCommand : subCommands.keySet()){

                for (String permission : subCommands.get(subCommand).getPermissions()) {
                    if (sender.hasPermission(CrazyTags.PERM_PREFIX + permission)) {
                        tabs.add(subCommand);
                    }
                }
            }
        }

        return tabs;
    }
}

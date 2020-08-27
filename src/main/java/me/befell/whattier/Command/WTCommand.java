package me.befell.whattier.Command;

import me.befell.whattier.WhatTier;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
public class WTCommand extends CommandBase {

    private WhatTier mod;

    public WTCommand(WhatTier mod) {
        this.mod = mod;
    }

    @Override
    public String getCommandName() {
        return "whattier";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args){
        mod.openMenu();
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

}

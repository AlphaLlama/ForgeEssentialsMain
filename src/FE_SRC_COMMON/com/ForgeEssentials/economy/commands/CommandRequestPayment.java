package com.ForgeEssentials.economy.commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import com.ForgeEssentials.core.commands.ForgeEssentialsCommandBase;
import com.ForgeEssentials.economy.Wallet;
import com.ForgeEssentials.util.FunctionHelper;
import com.ForgeEssentials.util.Localization;
import com.ForgeEssentials.util.OutputHandler;

public class CommandRequestPayment extends ForgeEssentialsCommandBase
{

	@Override
	public String getCommandName()
	{
		return "requestpayment";
	}

	@Override
	public void processCommandPlayer(EntityPlayer sender, String[] args)
	{
		if (args.length == 2)
		{
			EntityPlayerMP player = FunctionHelper.getPlayerFromPartialName(args[0]);
			if (player == null)
			{
				sender.sendChatToPlayer(args[0] + " not found!");
			}
			else
			{
				int amount = this.parseIntWithMin(sender, args[1], 0);
				OutputHandler.chatConfirmation(sender, "You have requested " + amount + Wallet.currency(amount) + " from " + player.username + ".");
				OutputHandler.chatConfirmation(player, "You been requested to play " + amount + Wallet.currency(amount) + " by " + player.username + ".");
			}
		}
		else
		{
			OutputHandler.chatError(sender, (Localization.get(Localization.ERROR_BADSYNTAX) + getSyntaxPlayer(sender)));
		}
	}

	@Override
	public void processCommandConsole(ICommandSender sender, String[] args)
	{
		if (args.length == 2)
		{
			EntityPlayerMP player = FunctionHelper.getPlayerFromPartialName(args[0]);
			if (player == null)
			{
				sender.sendChatToPlayer(args[0] + " not found!");
			}
			else
			{
				int amount = this.parseIntWithMin(sender, args[1], 0);
				OutputHandler.chatConfirmation(sender, "You have requested " + amount + Wallet.currency(amount) + " from " + player.username + ".");
				OutputHandler.chatConfirmation(player, "You been requested to play " + amount + Wallet.currency(amount) + " by " + player.username + ".");
			}
		}
		else
		{
			sender.sendChatToPlayer(Localization.get(Localization.ERROR_BADSYNTAX) + getSyntaxConsole());
		}
	}

	@Override
	public boolean canConsoleUseCommand()
	{
		return false;
	}

	@Override
	public String getCommandPerm()
	{
		return "ForgeEssentials.Economy." + getCommandName();
	}
}

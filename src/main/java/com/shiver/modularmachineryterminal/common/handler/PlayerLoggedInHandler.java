package com.shiver.modularmachineryterminal.common.handler;

import com.shiver.modularmachineryterminal.ModularMachineryTerminal;
import com.shiver.modularmachineryterminal.server.MachineList;
import hellfirepvp.modularmachinery.common.machine.DynamicMachine;
import hellfirepvp.modularmachinery.common.tiles.TileMachineController;
import hellfirepvp.modularmachinery.common.tiles.base.TileMultiblockMachineController;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;


@Mod.EventBusSubscriber(modid = ModularMachineryTerminal.MOD_ID)
public class PlayerLoggedInHandler {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.player instanceof EntityPlayerMP)) {
            return;
        }

        EntityPlayerMP player = (EntityPlayerMP) event.player;
        for (TileMultiblockMachineController controller : MachineList.loadedMachines(player)) {
            sendMachineInfo(player, controller);
        }
    }

    public static void sendMachineInfo(EntityPlayerMP player, TileMultiblockMachineController controller) {

        BlockPos pos = controller.getPos();
        int dimension = controller.getWorld().provider.getDimension();
        String formed = controller.isStructureFormed()
                ? "modular_machinery_terminal.formed"
                : "modular_machinery_terminal.unformed";
        String running = controller.isWorking()
                ? "modular_machinery_terminal.running"
                : "modular_machinery_terminal.not_running";
        String command = teleportCommand(player, dimension, pos);

        TextComponentTranslation message = new TextComponentTranslation(
                "modular_machinery_terminal.machine_prefix",
                machineName(controller),
                dimension
        );
        TextComponentString cord = new TextComponentString(pos.getX() + ", " + pos.getY() + ", " + pos.getZ());
        cord.setStyle(new Style()
                .setColor(TextFormatting.AQUA)
                .setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command))
                .setHoverEvent(new HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        new TextComponentTranslation("modular_machinery_terminal.teleport_hover", command)
                )));

        message.appendSibling(cord);
        message.appendSibling(new TextComponentTranslation(
                "modular_machinery_terminal.machine_status",
                new TextComponentTranslation(formed),
                new TextComponentTranslation(running)
        ));
        player.sendMessage(message);
    }

    private static String teleportCommand(EntityPlayerMP player, int dimension, BlockPos pos) {
        return "/mmt_tp " + player.getName() + " " + dimension + " " + pos.getX() + " " + pos.getY() + " " + pos.getZ();
    }

    private static String machineName(TileMultiblockMachineController controller) {
        DynamicMachine machine = controller.getFoundMachine();
        if (machine == null && controller instanceof TileMachineController) {
            machine = ((TileMachineController) controller).getParentMachine();
        }
        if (machine != null) {
            return machine.getRegistryName().toString();
        }
        String formedName = controller.getFormedMachineName();
        return formedName == null || formedName.isEmpty() ? "Unknown Machine" : formedName;
    }

}

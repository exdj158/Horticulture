package uk.joshiejack.horticulture.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkDirection;
import uk.joshiejack.horticulture.tileentity.AbstractStumpTileEntity;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;

@SuppressWarnings("unused")
@PenguinLoader.Packet(NetworkDirection.PLAY_TO_CLIENT)
public class StumpUpdatePacket extends PenguinPacket {
    private BlockPos pos;
    private Item item;
    private int stage;

    public StumpUpdatePacket() {}
    public StumpUpdatePacket(BlockPos pos, ItemStack stack) {
        this.pos = pos;
        this.item = stack.getItem();
    }

    @Override
    public void encode(PacketBuffer to) {
        to.writeLong(pos.asLong());
        to.writeRegistryId(item);
        to.writeByte(stage);
    }

    @Override
    public void decode(PacketBuffer from) {
        pos = BlockPos.of(from.readLong());
        item = from.readRegistryIdSafe(Item.class);
        stage = from.readByte();
    }

    @Override
    public void handle(PlayerEntity player) {
        TileEntity tile = player.level.getBlockEntity(pos);
        if (tile instanceof AbstractStumpTileEntity)
            ((AbstractStumpTileEntity<?>)tile).setMushroomData(item);
    }
}

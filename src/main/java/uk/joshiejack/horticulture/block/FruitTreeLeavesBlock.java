package uk.joshiejack.horticulture.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.RegistryObject;
import uk.joshiejack.horticulture.HorticultureConfig;

import javax.annotation.Nonnull;
import java.util.Random;

@SuppressWarnings("WeakerAccess")
public class FruitTreeLeavesBlock extends LeavesBlock implements IGrowable {
    private final AbstractBlock.IExtendedPositionPredicate<RegistryObject<Block>> predicate;
    private final RegistryObject<Block> block;

    public FruitTreeLeavesBlock(RegistryObject<Block> fruit, AbstractBlock.IExtendedPositionPredicate<RegistryObject<Block>> function) {
        super(AbstractBlock.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS)
                .noOcclusion().isValidSpawn(FruitTreeLeavesBlock::ocelotOrParrot)
                .isSuffocating(FruitTreeLeavesBlock::never).isViewBlocking(FruitTreeLeavesBlock::never));
        this.block = fruit;
        this.predicate = function;
    }

    @Override
    public void randomTick(@Nonnull BlockState state, @Nonnull ServerWorld world, @Nonnull BlockPos pos, @Nonnull Random rand)  {
        super.randomTick(state, world, pos, rand);
        if (HorticultureConfig.leavesGenerateFruit && world.random.nextDouble() <= HorticultureConfig.fruitGrowthChance) {
            performBonemeal(world, rand, pos, state);
        }
    }

    @Override
    public boolean isValidBonemealTarget(@Nonnull IBlockReader worldIn, @Nonnull BlockPos pos, @Nonnull BlockState state, boolean isClient) {
        return false;
    }

    @Override
    public boolean isBonemealSuccess(@Nonnull World worldIn, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        return false;
    }

    @Override
    public void performBonemeal(@Nonnull ServerWorld worldIn, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        if (state.getValue(BlockStateProperties.PERSISTENT) && predicate.test(state, worldIn, pos, block)
                && worldIn.getBlockState(pos.below()).isAir() && worldIn.getBlockState(pos.below(2)).isAir()) {
            worldIn.setBlock(pos.below(), this.block.get().defaultBlockState(), 2);
        }
    }

    private static Boolean ocelotOrParrot(BlockState state, IBlockReader reader, BlockPos pos, EntityType<?> type) {
        return type == EntityType.OCELOT || type == EntityType.PARROT;
    }

    private static boolean never(BlockState state, IBlockReader reader, BlockPos pos) {
        return false;
    }
}
package org.terasology.fences;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.math.Rotation;
import org.terasology.math.Side;
import org.terasology.math.SideBitFlag;
import org.terasology.math.geom.Vector3i;
import org.terasology.naming.Name;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockBuilderHelper;
import org.terasology.world.block.BlockComponent;
import org.terasology.world.block.BlockUri;
import org.terasology.world.block.family.*;
import org.terasology.world.block.loader.BlockFamilyDefinition;
import org.terasology.world.block.shapes.BlockShape;

@RegisterBlockFamily("Fences:fence")
@BlockSections({"no_connections", "one_connection", "line_connection", "2d_corner", "2d_t","cross"})
public class FencesFamily extends MultiConnectFamily {

    public FencesFamily(BlockFamilyDefinition definition, BlockShape shape, BlockBuilderHelper blockBuilder) {
        super(definition, shape, blockBuilder);
    }

    public FencesFamily(BlockFamilyDefinition definition, BlockBuilderHelper blockBuilder) {
        super(definition, blockBuilder);

        BlockUri blockUri = new BlockUri(definition.getUrn());
        Block block = blockBuilder.constructSimpleBlock(definition);

        block.setBlockFamily(this);
        block.setUri(new BlockUri(blockUri,new Name(String.valueOf(0))));
        this.blocks.put((byte) 0, block);

        this.registerBlock(blockUri, definition, blockBuilder, "no_connections", (byte) 0, Rotation.horizontalRotations());
        this.registerBlock(blockUri, definition, blockBuilder, "one_connection", SideBitFlag.getSides(Side.RIGHT), Rotation.horizontalRotations());
        this.registerBlock(blockUri, definition, blockBuilder, "line_connection", SideBitFlag.getSides(Side.LEFT, Side.RIGHT), Rotation.horizontalRotations());
        this.registerBlock(blockUri, definition, blockBuilder, "2d_corner", SideBitFlag.getSides(Side.LEFT, Side.FRONT), Rotation.horizontalRotations());
        this.registerBlock(blockUri, definition, blockBuilder, "2d_t", SideBitFlag.getSides(Side.LEFT, Side.RIGHT, Side.FRONT), Rotation.horizontalRotations());
        this.registerBlock(blockUri, definition, blockBuilder, "cross", SideBitFlag.getSides(Side.RIGHT, Side.LEFT, Side.BACK, Side.FRONT), Rotation.horizontalRotations());
    }

    @Override
    public byte getConnectionSides() {
        return 63;
    }

    @Override
    public Block getBlockForNeighborUpdate(Vector3i location, Block oldBlock) {
        return super.getBlockForNeighborUpdate(location, oldBlock);
    }

    @Override
    public Block getArchetypeBlock() {
        return blocks.get((byte) 0);
    }

    @Override
    public boolean connectionCondition(Vector3i blockLocation, Side connectSide) {

        Vector3i neighborLocation = new Vector3i(blockLocation);
        neighborLocation.add(connectSide.getVector3i());

        EntityRef neighborEntity = blockEntityRegistry.getEntityAt(neighborLocation);
        BlockComponent blockComponent = neighborEntity.getComponent(BlockComponent.class);

        return neighborEntity.hasComponent(ConnectsToFencesComponent.class)||
                (blockComponent != null && blockComponent.getBlock().isFullSide(connectSide));
    }
}

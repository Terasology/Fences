package org.terasology.fences;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.math.Side;
import org.terasology.math.geom.Vector3i;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockBuilderHelper;
import org.terasology.world.block.BlockComponent;
import org.terasology.world.block.family.*;
import org.terasology.world.block.loader.BlockFamilyDefinition;
import org.terasology.world.block.shapes.BlockShape;

@RegisterBlockFamily("Fences:fence")
public class FencesFamily extends MultiConnectFamily {

    public FencesFamily(BlockFamilyDefinition definition, BlockShape shape, BlockBuilderHelper blockBuilder) {
        super(definition, shape, blockBuilder);
    }

    public FencesFamily(BlockFamilyDefinition definition, BlockBuilderHelper blockBuilder) {
        super(definition, blockBuilder);
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

        boolean check = neighborEntity.hasComponent(ConnectsToFencesComponent.class)||
                (blockComponent != null && blockComponent.getBlock().isFullSide(connectSide));
        return neighborEntity != null && check;
    }
}

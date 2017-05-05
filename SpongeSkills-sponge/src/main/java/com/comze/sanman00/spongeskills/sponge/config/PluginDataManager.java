package com.comze.sanman00.spongeskills.sponge.config;

import com.comze.sanman00.spongeskills.api.skill.DefaultSkills;
import com.comze.sanman00.spongeskills.api.skill.Skill;
import com.comze.sanman00.spongeskills.sponge.tracker.BlockTracker;
import com.google.common.reflect.TypeToken;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.block.BlockType;

/**
 * Manages the data of the plugin.
 */
public final class PluginDataManager {
    private static CommentedConfigurationNode acceptedBlocksNode;
    private static Map<Skill, Map<BlockType, Integer>> acceptedBlocks;
    private static Map<UUID, BlockTracker> blockTrackers;
    private static Path blockOwnershipDataFile;

    private PluginDataManager() {
        throw new UnsupportedOperationException();
    }
    
    public static void init(ConfigManager cm) {
        acceptedBlocksNode = cm.getConfig().getNode("Skills", "AcceptedBlocks");
        try {
            Map<Skill, Map<BlockType, Integer>> skillBlockMap = new HashMap<>();
            skillBlockMap.put(DefaultSkills.MINING, SkillTriggerDataManager.DEFAULT_MINING_BLOCK_TRIGGERS);
            skillBlockMap.put(DefaultSkills.WOOD_CUTTING, SkillTriggerDataManager.DEFAULT_WOOD_CUTTING_BLOCK_TRIGGERS);
            acceptedBlocks = acceptedBlocksNode.getValue(new TypeToken<Map<Skill, Map<BlockType, Integer>>>() {}, skillBlockMap);
        }
        catch (ObjectMappingException ex) {
            ex.printStackTrace();
        }
        blockTrackers = new HashMap<>();
        blockOwnershipDataFile = cm.getConfigPath().resolve("block_ownership.dat");
    }

    public static Map<Skill, Map<BlockType, Integer>> getAcceptedBlocks() {
        return acceptedBlocks;
    }

    public static CommentedConfigurationNode getAcceptedBlocksNode() {
        return acceptedBlocksNode;
    }

    public static Path getBlockOwnershipDataFile() {
        return blockOwnershipDataFile;
    }

    public static Map<UUID, BlockTracker> getBlockTrackers() {
        return blockTrackers;
    }
}

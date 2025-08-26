package com.thexfactor117.levels.common.nbt;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents the different types of NBT (Named Binary Tag) used in Minecraft-like binary data formats.
 */
@Getter
@AllArgsConstructor
public enum NBTType {

    /**
     * ID 0 - End tag, used to denote the end of a CompoundTag.
     * No payload.
     */
    END(0),

    /**
     * ID 1 - A single signed byte (8 bits).
     */
    BYTE(1),

    /**
     * ID 2 - A signed short (16 bits), big endian.
     */
    SHORT(2),

    /**
     * ID 3 - A signed integer (32 bits), big endian.
     */
    INT(3),

    /**
     * ID 4 - A signed long (64 bits), big endian.
     */
    LONG(4),

    /**
     * ID 5 - A 32-bit IEEE 754 floating point number, big endian.
     */
    FLOAT(5),

    /**
     * ID 6 - A 64-bit IEEE 754 floating point number, big endian.
     */
    DOUBLE(6),

    /**
     * ID 7 - A byte array.
     * Payload: IntTag for size, followed by that many ByteTag payloads.
     */
    BYTE_ARRAY(7),

    /**
     * ID 8 - A UTF-8 string.
     * Payload: ShortTag for length, followed by that many bytes.
     */
    STRING(8),

    /**
     * ID 9 - A list of tags of the same type.
     * Payload: ByteTag for contained tag ID, IntTag for list size, followed by that many tags of that type.
     */
    LIST(9),

    /**
     * ID 10 - A compound tag containing fully formed tags.
     * Payload: Sequence of named tags ending with an EndTag.
     */
    COMPOUND(10),

    /**
     * ID 11 - An array of integers.
     * Payload: IntTag for size, followed by that many IntTag payloads.
     */
    INT_ARRAY(11),

    /**
     * ID 12 - An array of longs.
     * Payload: IntTag for size, followed by that many LongTag payloads.
     */
    LONG_ARRAY(12);


    private final int id;

    /**
     * Returns the NbtTagType for a given ID.
     *
     * @param id The tag ID.
     * @return The corresponding NbtTagType, or null if not found.
     */
    public static NBTType fromId(int id) {
        for (NBTType type : values()) {
            if (type.id == id) {
                return type;
            }
        }
        return null;
    }
}


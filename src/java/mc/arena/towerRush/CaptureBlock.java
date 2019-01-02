package mc.arena.towerRush;

import java.util.Map;

import mc.alk.arena.alib.bukkitadapter.MaterialAdapter;
import mc.alk.arena.objects.YamlSerializable;
import mc.alk.arena.util.SerializerUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class CaptureBlock implements YamlSerializable {
    Material type;
    byte data;
    Location location;
    static int MAX_HEALTH = 50;
    int health = MAX_HEALTH;

    public CaptureBlock() {}

    public CaptureBlock(Block block) {
        this.type = block.getType();
        this.data = block.getData();
        this.location = block.getLocation();
    }

    public Object yamlToObject(Map<String, Object> map, String value) {
        String[] split = value.split(";");
        CaptureBlock cb = new CaptureBlock();
        cb.type = MaterialAdapter.getMaterial(split[0]);
        cb.data = Byte.valueOf(split[1]).byteValue();
        cb.location = SerializerUtil.getLocation(split[2]);
        return cb;
    }

    public Object objectToYaml() {
        return this.type + ";" + this.data + ";" + SerializerUtil.getBlockLocString(this.location);
    }

    public Material getType() {
        return this.type;
    }

    public void setType(Material type) {
        this.type = type;
    }

    public byte getData() {
        return this.data;
    }

    public void setData(byte data) {
        this.data = data;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getHealth() {
        return this.health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void reset() {
        this.health = MAX_HEALTH;
        this.location.getWorld().getBlockAt(this.location).setType(this.type, false);
        this.location.getWorld().getBlockAt(this.location).getState().setRawData(this.data);
    }

    public void reduceHealth(int points) {
        this.health -= points;
    }
}

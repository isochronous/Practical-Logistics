package sonar.logistics.api.cache;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import net.minecraft.util.EnumFacing;
import sonar.core.api.utils.BlockCoords;
import sonar.logistics.api.connecting.ILogicTile;
import sonar.logistics.api.info.monitor.ILogicMonitor;

/** an implementation of {@link INetworkCache} that acts as an Empty Network, when working with networks the INSTANCE of this should be returned instead of null */
public class EmptyNetworkCache implements INetworkCache {

	public static final EmptyNetworkCache INSTANCE = EmptyNetworkCache.createEmptyCache();

	private EmptyNetworkCache() {
	}

	public static EmptyNetworkCache createEmptyCache() {
		return new EmptyNetworkCache();
	}

	@Override
	public Entry<BlockCoords, EnumFacing> getExternalBlock(boolean includeChannels) {
		return null;
	}

	@Override
	public LinkedHashMap<BlockCoords, EnumFacing> getExternalBlocks(boolean includeChannels) {
		return new LinkedHashMap();
	}

	@Override
	public <T extends ILogicTile> ArrayList<T> getConnections(Class<T> classType, boolean includeChannels){
		return new ArrayList();
	}

	@Override
	public <T extends ILogicTile> T getFirstConnection(Class<T> classType){
		return null;
	}

	@Override
	public int getNetworkID() {
		return -1;
	}

	@Override
	public ArrayList<Integer> getConnectedNetworks(ArrayList<Integer> networks) {
		return networks;
	}

	@Override
	public boolean isFakeNetwork() {
		return true;
	}

}

package sonar.logistics.connections.monitoring;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import sonar.core.SonarCore;
import sonar.core.api.StorageSize;
import sonar.core.api.fluids.ISonarFluidHandler;
import sonar.core.api.fluids.StoredFluidStack;
import sonar.core.api.utils.BlockCoords;
import sonar.logistics.Logistics;
import sonar.logistics.api.asm.TileMonitorHandler;
import sonar.logistics.api.cache.INetworkCache;
import sonar.logistics.api.info.ICustomEntityHandler;
import sonar.logistics.api.info.ITileMonitorHandler;
import sonar.logistics.api.info.monitor.LogicMonitorHandler;
import sonar.logistics.api.info.types.LogicInfo;
import sonar.logistics.info.LogicInfoRegistry;

@TileMonitorHandler(handlerID = FluidMonitorHandler.id, modid = Logistics.MODID)
public class FluidMonitorHandler extends LogicMonitorHandler<MonitoredFluidStack> implements ITileMonitorHandler<MonitoredFluidStack> {

	public static final String id = "fluid";

	@Override
	public String id() {
		return id;
	}

	@Override
	public MonitoredList<MonitoredFluidStack> updateInfo(INetworkCache network, MonitoredList<MonitoredFluidStack> previousList, BlockCoords coords, EnumFacing side) {
		MonitoredList<MonitoredFluidStack> list = MonitoredList.<MonitoredFluidStack>newMonitoredList(network.getNetworkID());
		List<ISonarFluidHandler> providers = SonarCore.fluidHandlers;
		for (ISonarFluidHandler provider : providers) {
			TileEntity fluidTile = coords.getTileEntity();
			if (fluidTile != null && provider.canHandleFluids(fluidTile, side)) {
				List<StoredFluidStack> info = new ArrayList();
				StorageSize size = provider.getFluids(info, fluidTile, side);
				list.sizing.add(size);
				for (StoredFluidStack fluid : info) {
					list.addInfoToList(new MonitoredFluidStack(fluid), previousList);
				}
				break;
			}
		}
		return list;
	}

}

package sonar.logistics.common.multiparts;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import sonar.core.helpers.FontHelper;
import sonar.core.helpers.NBTHelper.SyncType;
import sonar.core.network.sync.SyncEnum;
import sonar.core.network.sync.SyncTagType;
import sonar.core.network.sync.SyncTagType.INT;
import sonar.core.network.utils.IByteBufTile;
import sonar.core.utils.SortingDirection;
import sonar.logistics.LogisticsItems;
import sonar.logistics.api.info.monitor.ChannelType;
import sonar.logistics.api.settings.FluidReader;
import sonar.logistics.connections.monitoring.EnergyMonitorHandler;
import sonar.logistics.connections.monitoring.MonitoredEnergyStack;
import sonar.logistics.connections.monitoring.MonitoredList;
import sonar.logistics.network.SyncMonitoredType;

public class EnergyReaderPart extends ReaderMultipart<MonitoredEnergyStack> implements IByteBufTile {

	public SyncMonitoredType<MonitoredEnergyStack> selected = new SyncMonitoredType<MonitoredEnergyStack>(1);
	public SyncEnum<FluidReader.Modes> setting = (SyncEnum) new SyncEnum(FluidReader.Modes.values(), 2).addSyncType(SyncType.SPECIAL);
	public SyncTagType.INT targetSlot = (INT) new SyncTagType.INT(3).addSyncType(SyncType.SPECIAL);
	public SyncTagType.INT posSlot = (INT) new SyncTagType.INT(4).addSyncType(SyncType.SPECIAL);
	public SyncEnum<SortingDirection> sortingOrder = (SyncEnum) new SyncEnum(SortingDirection.values(), 5).addSyncType(SyncType.SPECIAL);
	public SyncEnum<FluidReader.SortingType> sortingType = (SyncEnum) new SyncEnum(FluidReader.SortingType.values(), 6).addSyncType(SyncType.SPECIAL);
	{
		syncList.addParts(setting, targetSlot, posSlot, sortingOrder, sortingType, selected);
	}
	public EnergyReaderPart() {
		super(EnergyMonitorHandler.id);
	}

	public EnergyReaderPart(EnumFacing face) {
		super(EnergyMonitorHandler.id, face);
	}

	@Override
	public ItemStack getItemStack() {
		return new ItemStack(LogisticsItems.energyReaderPart);
	}
	
	@Override
	public MonitoredList<MonitoredEnergyStack> sortMonitoredList(MonitoredList<MonitoredEnergyStack> updateInfo, int channelID) {
		//FluidHelper.sortFluidList(updateInfo, sortingOrder.getObject(), sortingType.getObject());
		return updateInfo;
	}

	@Override
	public ChannelType channelType() {
		return ChannelType.UNLIMITED;
	}

	@Override
	public void setMonitoredInfo(MonitoredList<MonitoredEnergyStack> updateInfo, int channelID) {
		/*
		IMonitorInfo info = null;
		switch (setting.getObject()) {
		case FLUID:
			MonitoredFluidStack stack = selected.getMonitoredInfo();
			if (stack != null) {
				MonitoredFluidStack dummyInfo = stack.copy();
				Pair<Boolean, IMonitorInfo> latestInfo = updateInfo.getLatestInfo(dummyInfo);
				info = latestInfo.a ? latestInfo.b : dummyInfo;
			}
			break;
		case POS:
			break;
		case STORAGE:
			break;
		case TANKS:
			break;
		default:
			break;
		}
		if (info != null) {
			InfoUUID id = new InfoUUID(getMonitorUUID().hashCode(), 0);
			IMonitorInfo oldInfo = LogicMonitorCache.info.get(id);
			if (oldInfo == null || !oldInfo.isIdenticalInfo(info)) {
				LogicMonitorCache.changeInfo(id, info);
			}
		}
		*/
	}
	@Override
	public Object getServerElement(Object obj, int id, World world, EntityPlayer player, NBTTagCompound tag) {
		switch (id) {
		case 0:
			//return new ContainerEnergyReader(this, player);
		}
		return null;
	}

	@Override
	public Object getClientElement(Object obj, int id, World world, EntityPlayer player, NBTTagCompound tag) {
		switch (id) {
		case 0:
			//return new GuiEnergyReader(this, player);
		}
		return null;
	}

	@Override
	public String getDisplayName() {
		return FontHelper.translate("item.EnergyReader.name");
	}
}

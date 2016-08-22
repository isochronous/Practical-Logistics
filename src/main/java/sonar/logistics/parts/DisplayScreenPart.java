package sonar.logistics.parts;

import java.util.List;

import io.netty.buffer.ByteBuf;
import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.ISlottedPart;
import mcmultipart.multipart.PartSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import sonar.core.helpers.NBTHelper.SyncType;
import sonar.core.network.sync.SyncEnum;
import sonar.core.network.sync.SyncUUID;
import sonar.core.network.utils.IByteBufTile;
import sonar.logistics.LogisticsItems;
import sonar.logistics.api.display.ScreenLayout;
import sonar.logistics.api.info.IInfoContainer;
import sonar.logistics.api.info.InfoContainer;
import sonar.logistics.api.info.InfoUUID;
import sonar.logistics.api.info.monitor.ILogicMonitor;
import sonar.logistics.connections.LogicMonitorCache;

public class DisplayScreenPart extends ScreenMultipart {

	protected SyncUUID uuid = new SyncUUID(-2);
	public SyncEnum<ScreenLayout> layout = new SyncEnum(ScreenLayout.values(), 0);
	public DisplayState state = DisplayState.NONE;
	public ILogicMonitor monitor = null;

	public static enum DisplayState {
		NONE, SET;
	}

	public InfoContainer container = new InfoContainer(this);
	{
		syncParts.add(layout);
	}

	public void update() {
		super.update();
		if (this.isClient()) {
			return;
		}
		switch (state) {
		case SET:
			if (monitor == null) {
				state = DisplayState.NONE;
			}
			break;
		case NONE:
			ISlottedPart part = getContainer().getPartInSlot(PartSlot.getFaceSlot(face));
			monitor = (part != null && part instanceof ILogicMonitor) ? (ILogicMonitor) part : this.getNetwork().getFirstConnection(ILogicMonitor.class);
			if (monitor != null) {
				state = DisplayState.SET;
				int max = Math.min(container().getMaxCapacity(), monitor.getMaxInfo());
				for (int i = 0; i < max; i++) {
					InfoUUID id = new InfoUUID(monitor.getMonitorUUID().hashCode(), i);
					container().setUUID(id, i);
					LogicMonitorCache.changedInfo.add(id);
				}
				this.sendUpdatePacket();
			}
			break;
		default:
			break;

		}
	}

	public void onPartChanged(IMultipart changedPart) {
		super.onPartChanged(changedPart);
		state = DisplayState.NONE;
	}

	public void writeUpdatePacket(PacketBuffer buf) {
		super.writeUpdatePacket(buf);
		ByteBufUtils.writeTag(buf, container.writeData(new NBTTagCompound(), SyncType.SAVE));
	}

	public void readUpdatePacket(PacketBuffer buf) {
		super.readUpdatePacket(buf);
		container.readData(ByteBufUtils.readTag(buf), SyncType.SAVE);
	}

	@Override
	public NBTTagCompound writeData(NBTTagCompound tag, SyncType type) {
		super.writeData(tag, type);
		container.writeData(tag, type);
		return tag;
	}

	@Override
	public void readData(NBTTagCompound tag, SyncType type) {
		super.readData(tag, type);
		container.readData(tag, type);
	}

	public DisplayScreenPart() {
		super();
	}

	public DisplayScreenPart(EnumFacing dir, EnumFacing rotation) {
		super(dir, rotation);
	}

	public void addSelectionBoxes(List<AxisAlignedBB> list) {
		double p = 0.0625;
		double height = p * 16, width = 0, length = p * 1;

		switch (face) {
		case EAST:
			list.add(new AxisAlignedBB(1, p * 4, (width) / 2, 1 - length, 1 - p * 4, 1 - width / 2));
			break;
		case NORTH:
			list.add(new AxisAlignedBB((width) / 2, p * 4, length, 1 - width / 2, 1 - p * 4, 0));
			break;
		case SOUTH:
			list.add(new AxisAlignedBB((width) / 2, p * 4, 1, 1 - width / 2, 1 - p * 4, 1 - length));
			break;
		case WEST:
			list.add(new AxisAlignedBB(length, p * 4, (width) / 2, 0, 1 - p * 4, 1 - width / 2));
			break;
		case DOWN:
			list.add(new AxisAlignedBB(0, 0, 0, 1, 0.0625, 1));
			break;
		case UP:
			list.add(new AxisAlignedBB(0, 1 - 0, 0, 1, 1 - 0.0625, 1));
			break;
		default:
			break;

		}
	}

	@Override
	public ItemStack getItemStack() {
		return new ItemStack(LogisticsItems.displayScreen);
	}

	@Override
	public IInfoContainer container() {
		return container;
	}

	@Override
	public ScreenLayout getLayout() {
		return layout.getObject();
	}

	@Override
	public int maxInfo() {
		return 2;
	}

	@Override
	public boolean monitorsUUID(InfoUUID id) {
		return true;// container().getStoredInfo();
	}
}

package rasterdata;

public interface Presenter<DeviceType> {
	DeviceType present(DeviceType device);
}
